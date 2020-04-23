package it.uniba.ventricellisardone.itss.csv;

import com.sun.tools.jdeprscan.scan.Scan;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CSVFile {

    private Map<String, Integer> headerFile;
    private List<CSVRecord> csvRecordList;
    private Scanner fileScanner;
    private List<String> nullRecordList;

    public CSVFile(String dataPath) {

    }

    public Map<String, Integer> getHeaderFile() {
        return headerFile;
    }

    private void setHeaderFile() {

    }

    public List<CSVRecord> getCsvRecordList() {
        return csvRecordList;
    }

    private void setCsvRecordList() {

    }

    public List<String> getNullRecordList() {
        return nullRecordList;
    }

    public void logNullRecord(){

    }

    private void logDataAnalysis(Map<String, Map<String, Integer>> analysisReport){

    }

    public Map<String, Map<String, Integer>> performDataAnalysis(){
        return null;
    }
}
