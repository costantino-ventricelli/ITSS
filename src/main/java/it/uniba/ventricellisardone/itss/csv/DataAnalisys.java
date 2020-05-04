package it.uniba.ventricellisardone.itss.csv;

import it.uniba.ventricellisardone.itss.log.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAnalisys {

    private static final Map<String, Integer> ANALYSIS_HEADER;
    private List<CSVRecord> csvRecordList;
    private static final String TAG = "DataAnalisys.class";
    private static final String XML_EXTENSION = ".xml";

    static {
        Map<String, Integer> headerMap = new HashMap<>();
        headerMap.put("Colore", 0);
        headerMap.put("CodStatoFattura", 1);
        headerMap.put("NomeBrand", 2);
        headerMap.put("Collezione", 3);
        headerMap.put("PagamentoOrdine", 4);
        headerMap.put("NomeCategoria", 5);
        headerMap.put("MacroCategoria", 6);
        ANALYSIS_HEADER = Collections.unmodifiableMap(headerMap);
    }

    public DataAnalisys(List<CSVRecord> csvRecordList) {
        this.csvRecordList = csvRecordList;
    }

    public Map<String, Map<String, Integer>> performDataAnalysis(){
        Map<String, Map<String, Integer>> dataMap = new HashMap<>();
        for(Map.Entry<String, Integer> entry : ANALYSIS_HEADER.entrySet()){
            dataMap.put(entry.getKey(), new HashMap<>());
        }
        for(CSVRecord record : csvRecordList){
            String[] stringsRecord = CSVRecord.reverseCSVRecordForAnalysis(record);
            dataMap.replaceAll((k, v) -> checkField(stringsRecord[ANALYSIS_HEADER.get(k)], v));
        }
        Log.i(TAG, "Mappa creata");
        Log.i(TAG, dataMap.toString());
        return dataMap;
    }

    private Map<String, Integer> checkField(String fieldValue, Map<String, Integer> fieldMap){
        if(fieldMap.containsKey(fieldValue))
            fieldMap.put(fieldValue, fieldMap.get(fieldValue) + 1);
        else
            fieldMap.put(fieldValue, 1);
        return fieldMap;
    }

    public void logDataAnalysis(Map<String, Map<String, Integer>> analysisReport, String pathDirectory, String fileName){
        pathDirectory += "results";
        fileName = checkDirectoryAndFileName(pathDirectory, fileName, XML_EXTENSION);
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(pathDirectory + "/" + fileName, false));
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            if(fileName.contains("test"))
                writer.println("<data type=\"test\">");
            else
                writer.println("<data type=\"execution\">");
            for(Map.Entry<String, Map<String, Integer>> category : analysisReport.entrySet()){
                writer.println("\t<category key=\"" + category.getKey() + "\">");
                for(Map.Entry<String, Integer> entry : category.getValue().entrySet())
                    writer.println("\t\t<entry key=\"" + entry.getKey() + "\" value=\"" + entry.getValue() + "\"/>");
                writer.println("\t</category>");
            }
            writer.println("</data>");
            writer.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Log data analysis", e);
        }
    }

    private String checkDirectoryAndFileName(String pathDirectory , String fileName, String extension) {
        Log.i(TAG, "Directory: " + pathDirectory);
        Log.i(TAG, "File name: " + fileName);
        File directory = new File(pathDirectory);
        if (directory.mkdir()) {
            System.out.println("[INFO] Created directory: " + pathDirectory);
        }
        if (!fileName.contains(extension)) {
            fileName = fileName.concat(extension);
            System.out.println("[INFO] Added "+ extension + " file extension");
            Log.i(TAG, "Added " + extension + " file extension");
        }
        return fileName;
    }
}
