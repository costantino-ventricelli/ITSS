package it.uniba.ventricellisardone.itss.csv;

import java.text.ParseException;
import java.util.*;

public class CSVFile {

    public static final Map<String, Integer> HEADER_FILE;

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
    private final Scanner fileScanner;
    private List<String> nullRecordList;
    private List<String> parseErrorList;

    public CSVFile(String dataPath){
        fileScanner = new Scanner(dataPath);
        setHeaderFile();
        setCsvRecordList();
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
            }catch (ParseException | CSVParsingException ex){
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
        return null;
    }

    public void logNullRecord(String directory, String fileName){

    }

    public void logDataAnalysis(Map<String, Map<String, Integer>> analysisReport, String directory, String fileName){

    }

    public void logParseErrorRecord(String directory, String fileName){

    }
}
