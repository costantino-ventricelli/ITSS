package it.uniba.ventricellisardone.itss.etl;

import it.uniba.ventricellisardone.itss.csv.CSVRecord;
import it.uniba.ventricellisardone.itss.csv.ecxception.CSVNullFieldsException;
import it.uniba.ventricellisardone.itss.log.Log;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;

public class Extraction {

    public static final Map<String, Integer> HEADER_FILE;
    private static final String TAG = "CSVFile.class";
    private static final String CSV_EXTENSION = ".csv";
    private static final String RESULTS_DIR = "/Results/";

    static {
        HEADER_FILE = Map.ofEntries(Map.entry("IdOrdine", 0),
                Map.entry("DataOrdine", 1),
                Map.entry("CodStatoFattura", 2),
                Map.entry("SexAcquirente", 3),
                Map.entry("Quantita", 4),
                Map.entry("PrezzoPagato", 5),
                Map.entry("Sconto", 6),
                Map.entry("Outlet", 7),
                Map.entry("NomeBrand", 8),
                Map.entry("Collezione", 9),
                Map.entry("Colore", 10),
                Map.entry("SexArticolo", 11),
                Map.entry("PagamentoOrdine", 12),
                Map.entry("ValoreTagliaEffettivo", 13),
                Map.entry("NomeCategoria", 14),
                Map.entry("MacroCategoria", 15));

    }

    private Map<String, Integer> headerFile;
    private List<CSVRecord> csvRecordList;
    private Scanner fileScanner;
    private List<String> nullRecordList;
    private List<String> parseErrorList;

    public Extraction(String dataPath){
        Log.i(TAG, "Data path: " + dataPath);
        try {
            dataPath = Paths.get(dataPath).toString();
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

    public void logNullRecord(String pathDirectory, String fileName) throws IOException {
        pathDirectory += RESULTS_DIR;
        pathDirectory = Paths.get(pathDirectory).toString();
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

    public void logParseErrorRecord(String pathDirectory, String fileName) throws IOException {
        pathDirectory += RESULTS_DIR;
        pathDirectory = Paths.get(pathDirectory).toString();
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

    private String checkDirectoryAndFileName(String pathDirectory , String fileName, String extension) throws IOException {
        Log.i(TAG, "Directory: " + pathDirectory);
        Log.i(TAG, "File name: " + fileName);
        FileUtils.forceMkdir(new File(pathDirectory));
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
