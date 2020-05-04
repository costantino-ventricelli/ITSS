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
    private static final String CSV_EXTENSION = ".csv";

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
