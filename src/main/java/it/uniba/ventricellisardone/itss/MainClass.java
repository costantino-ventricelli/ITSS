package it.uniba.ventricellisardone.itss;

import it.uniba.ventricellisardone.itss.etl.Extraction;
import it.uniba.ventricellisardone.itss.csv.DataAnalysis;

public class MainClass {

    public static void main(String[] args) {
        Extraction extraction = new Extraction(args[0]);
        DataAnalysis dataAnalysis = new DataAnalysis(extraction.getCsvRecordList());
        dataAnalysis.logDataAnalysis(dataAnalysis.performDataAnalysis(), args[1], args[2]);
    }
}
