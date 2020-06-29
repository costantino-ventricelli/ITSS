/**
 * Questa classe permette di effettuare un confronto approfondito tra i dati già presenti nel cloud e gli eventuali dati
 * di refresh, infatti dopo aver prelevato la data con valore più elevato dal cloud si può verificare la presenza di quella data
 * nei nuovi dati di refresh e, se presente, controllare che i dati di refresh non collidano con quelli già presenti in remoto.
 */
package it.uniba.ventricellisardone.itss.etl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.common.collect.Lists;
import it.uniba.ventricellisardone.itss.csv.CSVRecord;
import it.uniba.ventricellisardone.itss.log.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MatchBigQueryData {

    private static final BigQuery BIG_QUERY;
    private static final String TAG = "MatchingBigQueryData";

    private String bigQueryDate;
    private final List<CSVRecord> recordList;
    private final String targetTable;

    static {
        GoogleCredentials googleCredentials;
        BigQuery bigQuery = null;
        try {
            googleCredentials = GoogleCredentials.fromStream(new FileInputStream(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "/Documents/etl-authentication.json"))
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
            bigQuery = BigQueryOptions.newBuilder().setProjectId("biproject-itss").setCredentials(googleCredentials).build().getService();
        } catch (IOException e) {
            Log.e(TAG, "Eccezione caricamento credenziali", e);
        }
        BIG_QUERY = bigQuery;
    }

    /**
     * Il costruttore ha il compito di prelevare la data di valore più elevato presente nel cloud e impostarla come variabile
     * d'istanza assieme alle altre.
     * @param recordList contiene la lista dei record da controllare.
     * @param targetTable contiene la tabella su cui effettuare le query.
     * @throws InterruptedException viene sollevata quando il thread che esegue la query subisce un iterruzione non prevista
     * @throws SQLException viene sollevata quando il risultato della query non è quello atteso
     */
    public MatchBigQueryData(List<CSVRecord> recordList, String targetTable) throws InterruptedException, SQLException {
        this.recordList = recordList;
        this.targetTable = targetTable;
        QueryJobConfiguration jobConfiguration = QueryJobConfiguration
                .newBuilder("SELECT MAX(ordine_data) AS ordine_ultima_data FROM `biproject-itss.dataset."
                        + this.targetTable +"`").build();
        for (FieldValueList row : BIG_QUERY.query(jobConfiguration).iterateAll()) {
            if(row.size() > 1)
                throw new SQLException("Errore nel selezionare il massimo dalla tabella.");
            else
                this.bigQueryDate = row.get(0).getStringValue();
        }
    }

    /**
     * Questo metodo verifica che le date del cloud e dei dati di refresh coincidano.
     * @return true quando coincidono, false altrimenti.
     */
    public boolean isMatching() {
        try {
            Date parseGoogleDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .parse(this.bigQueryDate);
            Date recordDate = recordList.get(0).getOrderDate();
            return parseGoogleDate.before(recordDate) || parseGoogleDate.equals(recordDate);
        } catch (ParseException e) {
            Log.e(TAG, "Eccezione sollevata: ", e);
            return true;
        }
    }

    /**
     * Questo metodo basandosi sul risultato di isMatching() verifica che i record con la data incriminata siano almeno
     * differenti da quelli già presenti nel cloud
     * @return ritorna il numero di record che hanno colliso con i dati del cloud.
     * @throws ParseException viene sollevata quando si cerca di trasformare un record che non può essere trasformato
     * @throws InterruptedException viene sollevato quando il thread di esecuzione della query viene interrotto in maniera
     *                              non prevista.
     */
    public int getConflictRowNumber() throws ParseException, InterruptedException {
        int rowCounter = 0;
        if(isMatching()){
            QueryJobConfiguration jobConfiguration = QueryJobConfiguration
                    .newBuilder("SELECT * FROM `biproject-itss.dataset."
                            + this.targetTable +"` WHERE ordine_data = '" + this.bigQueryDate + "'").build();
            List<String> transformedRecord = Transforming.getTransformedRecord(this.recordList);
            for(FieldValueList row : BIG_QUERY.query(jobConfiguration).iterateAll()){
                for(String record : transformedRecord){
                    if(record.equals(buildStringFromFieldValueList(row)))
                        rowCounter++;
                }
            }
        }
        return rowCounter;
    }

    public String getBigQueryDate() {
        return bigQueryDate;
    }

    private String buildStringFromFieldValueList(FieldValueList row){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < row.size() ; i++) {
            builder.append(row.get(i).getStringValue());
            if(i < row.size() - 1)
                builder.append(",");
        }
        return builder.toString();
    }
}
