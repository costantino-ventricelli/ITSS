package it.uniba.ventricellisardone.itss.etl;

import it.uniba.ventricellisardone.itss.cloud.data.CloudData;
import it.uniba.ventricellisardone.itss.csv.CSVRecord;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class Transforming {
    private static final String CSV_EXTENSION = ".csv";
    private static final String header = "ordine_id_carrello,ordine_data,ordine_giorno_nome,ordine_giorno_dell_anno," +
            "ordine_mese_nome,ordine_anno_valore,ordine_mese_valore,ordine_trimestre,ordine_periodo," +
            "ordine_trimestre_anno,ordine_mese_anno,ordine_feriale_non,ordine_festivo_non,ordine_codice_stato," +
            "ordine_stato_nome,ordine_sesso_acquirente,ordine_quantita,ordine_prezzo_pagato,ordine_sconto," +
            "ordine_outlet,ordine_brand,ordine_collezione,ordine_colore,ordine_sesso_articolo," +
            "ordine_metodo_pagamento,ordine_taglia,ordine_categoria,ordine_macro_categoria";

    private static final String TAG = "Transform.class";
    private static final List<String> COLOR;
    static {
        COLOR = List.of("Rosso", "Multicolor", "Blu", "No Color", "Giallo", "Rosa",
                "Verde", "Nero", "Grigio", "Marrone", "Neutro", "Bianco", "Viola",
                "Arancione", "Fantasia");
    }

    private final String savingPath;
    private Integer lastFileCreated;

    public Transforming(String savingPath) throws IOException {
        this.savingPath = savingPath;
        FileUtils.forceMkdir(new File(savingPath));
        this.lastFileCreated = 0;
    }

    public void transformData(List<CSVRecord> CSVRecordList) throws ParseException, IOException {
        PrintWriter csvFile = new PrintWriter(new FileOutputStream(savingPath + "/load_data_" + lastFileCreated
                + CSV_EXTENSION), true, StandardCharsets.UTF_8);
        csvFile.println(header);
        for (int i = 0; i < CSVRecordList.size(); i++) {
            try {
                System.out.println("Scrivo su file record: " + i + " ne rimangono: " + (CSVRecordList.size() - (i + 1)));
                writeOnFile(csvFile, CSVRecordList.get(i));
            }catch (NullPointerException ex){
                ex.printStackTrace();
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

    private void writeOnFile(PrintWriter csvFile, CSVRecord record) throws ParseException {
        CloudData cloudData = new CloudData(record.getDataOrdine());
        StringBuilder bigQueryRecord = new StringBuilder();
        bigQueryRecord.append(record.getIdOrdine());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getDataString());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getNomeGiorno().toUpperCase());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getNumeroGiornoAnno());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getNomeMese().toUpperCase());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getAnnoValore());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getMeseValore());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getTrimestre());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getPeriodo().toUpperCase());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getTrimestreAnno());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getMeseAnno());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getFeriale());
        bigQueryRecord.append(",");
        bigQueryRecord.append(cloudData.getFestivo());
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getCodiceStatoFattura());
        bigQueryRecord.append(",");
        bigQueryRecord.append(new Locale("IT", record.getCodiceStatoFattura()).getDisplayCountry().toUpperCase());
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getSessoAcquirente());
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getQuantita());
        bigQueryRecord.append(",");
        bigQueryRecord.append(String.format(Locale.ROOT, "%.2f", record.getPrezzoPagato()));
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getSconto());
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.isOutlet() ? "OUTLET" : "NON OUTLET");
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getNomeBrand().toUpperCase());
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getCollezione().toUpperCase());
        bigQueryRecord.append(",");
        bigQueryRecord.append(checkColore(record.getColore()));
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getSessoArticolo().toUpperCase());
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getPagamentoOrdine().toUpperCase());
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getTaglia().toUpperCase());
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getCategoria().toUpperCase());
        bigQueryRecord.append(",");
        bigQueryRecord.append(record.getMacroCategoria().toUpperCase());
        csvFile.println(bigQueryRecord.toString());
    }

    private String checkColore(String colore) throws ParseException {
        if(COLOR.contains(colore))
            return colore.toUpperCase();
        else
            throw new ParseException("Colore " + colore + " non ammesso", colore.length());
    }

} //end class
