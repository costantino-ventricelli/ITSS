/**
 * Questa classe ha il compito di leggere la lista di record ottenuta da Extraction e trasformarla in una serie di file
 * .csv da caricare in BigQuery, trasformado i dati e aggiungendo campi strategici per l'analisi dei dati successiva.
 */
package it.uniba.ventricellisardone.itss.etl;

import it.uniba.ventricellisardone.itss.cloud.data.CloudData;
import it.uniba.ventricellisardone.itss.csv.CSVRecord;
import it.uniba.ventricellisardone.itss.log.Log;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Transforming {
    private static final String CSV_EXTENSION = ".csv";
    // Il nuovo header che i file .csv avranno per essere caricati nel cloud.
    private static final String header = "ordine_id_carrello,ordine_data,ordine_giorno_nome,ordine_giorno_dell_anno," +
            "ordine_mese_nome,ordine_anno_valore,ordine_mese_valore,ordine_trimestre,ordine_periodo," +
            "ordine_trimestre_anno,ordine_mese_anno,ordine_feriale_non,ordine_festivo_non,ordine_codice_stato," +
            "ordine_stato_nome,ordine_sesso_acquirente,ordine_quantita,ordine_prezzo_pagato,ordine_sconto," +
            "ordine_outlet,ordine_brand,ordine_collezione,ordine_colore,ordine_sesso_articolo," +
            "ordine_metodo_pagamento,ordine_taglia,ordine_categoria,ordine_macro_categoria";

    private static final String TAG = "Transform.class";
    private static final List<String> COLOR;
    // Lista dei colori ammessi tra i record.
    static {
        COLOR = List.of("ROSSO", "MULTICOLOR", "BLU", "NO COLOR", "GIALLO", "ROSA",
                "VERDE", "NERO", "GRIGIO", "MARRONE", "NEUTRO", "BIANCO", "VIOLA",
                "ARANCIONE", "FANTASIA");
    }

    private final String savingPath;
    private Integer lastFileCreated;

    /**
     * Il costruttore crea la directory dove salvare i file .csv generati.
     * @param savingPath contiene il percorso della cartella nella quale andranno salvati i file creati.
     * @throws IOException viene sollevata se il metodo non riesce ad accedere alla memoria del calcolatore.
     */
    public Transforming(String savingPath) throws IOException {
        this.savingPath = savingPath;
        FileUtils.forceMkdir(new File(savingPath));
        this.lastFileCreated = 0;
    }

    /**
     * Questo metodo seleziona record per record i valori ottenuti da Extraction e avvia così la trasformazione sui dati e la
     * aggiunta dei record necessari alle analisi successive.
     * Contemporaneamente genera i file .csv dopo la trasformazione e tiene il conto dei file generati.
     * @param CSVRecordList contiene la lista dei record da trasformare.
     * @throws ParseException viene sollevata se si cerca di trasformare un record non trasformabile.
     * @throws IOException viene sollevata se il sistema ha problemi ad accedere alla memoria del calcolatore.
     */
    public void transformData(List<CSVRecord> CSVRecordList) throws ParseException, IOException {
        PrintWriter csvFile = new PrintWriter(new FileOutputStream(savingPath + "/load_data_" + lastFileCreated
                + CSV_EXTENSION), true, StandardCharsets.UTF_8);
        csvFile.println(header);
        for (int i = 0; i < CSVRecordList.size(); i++) {
            try {
                System.out.println("Scrivo su file record: " + i + " ne rimangono: " + (CSVRecordList.size() - (i + 1)));
                writeOnFile(csvFile, CSVRecordList.get(i));
            }catch (NullPointerException ex){
                Log.e(TAG, "Eccezione in transforming", ex);
                System.err.println("Riprovo richiesta per record: " + i);
                i--;
            }
        }
        csvFile.close();
        lastFileCreated++;
        PrintWriter logTransform = new PrintWriter(savingPath+"/log_transform.log");
        logTransform.append(lastFileCreated.toString());
        logTransform.close();
    }


    /**
     * Questo metodo ha il compito di trasformare il recor che viene lui passato richiamando anche cloud data che setta i
     * campi supplementari necessari alla creazionde del nuovo record.
     * @param csvFile contiene il riferimento al file che si sta scrivendo.
     * @param record contiene il record da trasformare.
     * @throws ParseException viene sollevata quando si cerca di trasformare un record non trasformabile.
     */
    private void writeOnFile(PrintWriter csvFile, CSVRecord record) throws ParseException {
        CloudData cloudData = new CloudData(record.getOrderDate());
        StringBuilder bigQueryRecord = buildCloudRecord(record, cloudData);
        csvFile.println(bigQueryRecord.toString());
    }

    /**
     * Questo metodo agisce come il precedente solo che effettua la trasformazione senza creare il file .csv ma restituendo
     * una lista di stringhe contenenti i nuovi record.
     * @param recordList lista di record da trasformare.
     * @return lista di stringhe che rappresentano i nuovi record
     * @throws ParseException vine sollevata quando si cerca di trasformare un record non trasformabile.
     */
    public static List<String> getTransformedRecord(List<CSVRecord> recordList) throws ParseException {
        ArrayList<String> stringRecordList = new ArrayList<>();
        for(CSVRecord csvRecord : recordList) {
            CloudData cloudData = new CloudData(csvRecord.getOrderDate());
            stringRecordList.add(buildCloudRecord(csvRecord, cloudData).toString());
        }
        return stringRecordList;
    }

    /**
     * Questo metodo si occupa della creazione della stringa che rappresenta il nuovo record.
     * @param record contiene il record da scrivere.
     * @param cloudData contiene le informazioni aggiuntive sulla data calcolate.
     * @return uno stringBuilder contenente la stringa generata trasformando il record CSV da OLTP a OLAP.
     * @throws ParseException vine sollevata se si cerca di trasformare un record non trasformabile.
     */
    private static StringBuilder buildCloudRecord(CSVRecord record, CloudData cloudData) throws ParseException {
        StringBuilder bigQueryRecord = new StringBuilder();
        bigQueryRecord.append(record.getIdOrder());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getDateString());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getDayName().toUpperCase());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getDayNumber());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getMonthName().toUpperCase());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getYearValue());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getMonthValue());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getQuarter());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getSeason().toUpperCase());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getSeasonYear());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getMonthYear());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getWeekday());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getHoliday());
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getCountryCode());
        bigQueryRecord.append(",");
        bigQueryRecord.append(new Locale("IT", record.getCountryCode()).getDisplayCountry().toUpperCase());
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getCustomerGender());
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getQuantity());
        bigQueryRecord.append(",");
        bigQueryRecord.append(String.format(Locale.ROOT, "%.1f", record.getPayedPrice()));
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getDiscount());
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.isOutlet() ? "OUTLET" : "NON OUTLET");
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getNomeBrand().toUpperCase());
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getCollection().toUpperCase());
        bigQueryRecord.append(",");
        bigQueryRecord.append(checkColor(record.getColor()));
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getItemGender().toUpperCase());
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getPaymentMethod().toUpperCase());
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getSize().toUpperCase());
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getCategory().toUpperCase());
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getMacroCategory().toUpperCase());
        return bigQueryRecord;
    }

    /**
     * Verifica che i colore riportato nel record sia ammesso.
     */
    private static String checkColor(String color) throws ParseException {
        if(COLOR.contains(color.toUpperCase()))
            return color.toUpperCase();
        else
            throw new ParseException("Colore " + color + " non ammesso", color.length());
    }

} //end class
