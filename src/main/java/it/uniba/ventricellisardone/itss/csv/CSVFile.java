package it.uniba.ventricellisardone.itss.csv;

import it.uniba.ventricellisardone.itss.log.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.*;

public class CSVFile {

    public static final Map<String, Integer> HEADER_FILE;
    private static final String TAG = "CSVFile.class";
    private static final Map<String, Integer> ANALYSIS_HEADER;
    private static final String CSV_EXTENSION = ".csv";
    private static final String XML_EXTENSION = ".xml";

    static {
        Map<String, Integer> map = new HashMap<>();
        map.put("IdOrdine", 0);
        map.put("DataOrdine", 1);
        map.put("CodStatoFattura", 2);
        map.put("SexAcquirente", 3);
        map.put("Quantita", 4);
        map.put("PrezzoPagato", 5);
        map.put("Sconto", 6);
        map.put("Outlet", 7);
        map.put("NomeBrand", 8);
        map.put("Collezione", 9);
        map.put("Colore", 10);
        map.put("SexArticolo", 11);
        map.put("PagamentoOrdine", 12);
        map.put("ValoreTagliaEffettivo", 13);
        map.put("NomeCategoria", 14);
        map.put("MacroCategoria", 15);
        HEADER_FILE = Collections.unmodifiableMap(map);

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

    private Map<String, Integer> headerFile;
    private List<CSVRecord> csvRecordList;
    private Scanner fileScanner;
    private List<String> nullRecordList;
    private List<String> parseErrorList;

    public CSVFile(String dataPath){
        Log.i(TAG, "Data path: " + dataPath);
        try {
            fileScanner = new Scanner(new File(dataPath));
            setHeaderFile();
            setCsvRecordList();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Constructor exception: ", e);
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getHeaderFile() {
        return headerFile;
    }

    private void setHeaderFile() {
        String[] headerString = fileScanner.nextLine().split(";");
        headerFile = new HashMap<>();
        for (int i = 0; i < headerString.length; i++)
            headerFile.put(headerString[i], i);
    }

    public List<CSVRecord> getCsvRecordList() {
        return csvRecordList;
    }

    private void setCsvRecordList() {
        csvRecordList = new ArrayList<>();
        while(fileScanner.hasNextLine()){
            String fileLine = fileScanner.nextLine();
            String[] stringRecord = fileLine.split(";");
            try {
                CSVRecord csvRecord = new CSVRecord(stringRecord);
                csvRecordList.add(csvRecord);
            }catch (CSVNullFieldsException ex){
                if(nullRecordList == null)
                    nullRecordList = new ArrayList<>();
                nullRecordList.add(fileLine);
            }catch (ParseException | NumberFormatException ex){
                if(parseErrorList == null)
                    parseErrorList = new ArrayList<>();
                parseErrorList.add(fileLine);
            }
        }
    }

    public List<String> getNullRecordList() {
        return nullRecordList;
    }

    public List<String> getParseErrorList(){
        return parseErrorList;
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

    public void logNullRecord(String pathDirectory, String fileName){
        pathDirectory += "results";
        fileName = checkDirectoryAndFileName(pathDirectory, fileName, CSV_EXTENSION);
        try{
            if(nullRecordList != null)
                writeLog(pathDirectory, fileName, nullRecordList);
            else
                Log.i(TAG, "Nessun record da loggare");
        }catch (FileNotFoundException e){
            Log.e(TAG, "Exception log null record", e);
        }
    }

    public void logParseErrorRecord(String pathDirectory, String fileName){
        pathDirectory += "results";
        fileName = checkDirectoryAndFileName(pathDirectory, fileName, CSV_EXTENSION);
        try{
            if(parseErrorList != null)
                writeLog(pathDirectory, fileName, parseErrorList);
            else
                Log.i(TAG, "Nessun record da loggare");
        }catch (FileNotFoundException e){
            Log.e(TAG, "Exception log null record", e);
        }
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

    private static void writeLog(String pathDirectory, String fileName, List<String> recordList) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new FileOutputStream(pathDirectory + "/" + fileName, false));
        Log.i(TAG, "List size: " + recordList.size());
        for (String string : recordList) {
            writer.println(string);
            Log.i(TAG, "Writing log in: " + pathDirectory + "/" + fileName);
        }
        writer.close();
    }
}
