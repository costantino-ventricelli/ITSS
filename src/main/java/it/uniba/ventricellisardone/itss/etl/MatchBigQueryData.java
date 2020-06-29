package it.uniba.ventricellisardone.itss.etl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigquery.*;
import com.google.common.collect.Lists;
import it.uniba.ventricellisardone.itss.csv.CSVRecord;
import it.uniba.ventricellisardone.itss.log.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MatchBigQueryData {

    private String bigQueryDate;
    private final List<CSVRecord> recordList;
    private final String targetTable;

    private static final GoogleCredentials GOOGLE_CREDENTIALS;
    private static final BigQuery BIG_QUERY;
    private static final String TAG = "MatchingBigQueryData";

    static {
        GoogleCredentials googleCredentials;
        BigQuery bigQuery = null;
        try {
            googleCredentials = GoogleCredentials.fromStream(new FileInputStream(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "/Documents/etl-authentication.json"))
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
            bigQuery = BigQueryOptions.newBuilder().setProjectId("biproject-itss").setCredentials(googleCredentials).build().getService();
        } catch (IOException e) {
            googleCredentials = null;
            Log.e(TAG, "Eccezione caricamento credenziali", e);
        }
        BIG_QUERY = bigQuery;
        GOOGLE_CREDENTIALS = googleCredentials;
    }

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

    public boolean isMatching() {
        String firstDate =  new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(this.recordList.get(0).getDataOrdine());
        return firstDate.equals(this.bigQueryDate);
    }

    public int getConflictRowNumber() throws ParseException, InterruptedException {
        int rowCounter = 0;
        if(isMatching()){
            QueryJobConfiguration jobConfiguration = QueryJobConfiguration
                    .newBuilder("SELECT * FROM `biproject-itss.dataset."
                            + this.targetTable +"` WHERE ordine_data = '" + this.bigQueryDate + "'").build();
            List<String> recordList = Transforming.getTransformedRecord(this.recordList);
            for(FieldValueList row : BIG_QUERY.query(jobConfiguration).iterateAll()){
                for(String record : recordList){
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
