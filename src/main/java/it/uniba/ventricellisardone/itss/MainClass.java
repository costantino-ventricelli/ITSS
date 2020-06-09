package it.uniba.ventricellisardone.itss;

import it.uniba.ventricellisardone.itss.csv.CSVFile;
import it.uniba.ventricellisardone.itss.csv.DataAnalysis;

public class MainClass {

    public static void main(String[] args) {
        CSVFile csvFile = new CSVFile(args[0]);
        DataAnalysis dataAnalysis = new DataAnalysis(csvFile.getCsvRecordList());
        dataAnalysis.logDataAnalysis(dataAnalysis.performDataAnalysis(), args[1], args[2]);
    }
}
