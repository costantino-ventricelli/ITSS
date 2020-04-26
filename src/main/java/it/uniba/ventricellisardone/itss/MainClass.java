package it.uniba.ventricellisardone.itss;

import it.uniba.ventricellisardone.itss.csv.CSVFile;
import it.uniba.ventricellisardone.itss.csv.FileFormatException;

import java.io.File;

public class MainClass {

    public static void main(String[] args){
        try {
            if (args.length == 0) {
                System.err.println("Inserire il percorso del file da analizzare");
            } else if (args.length == 1) {
                checkFile(args[0], null);
            } else if (args.length == 2) {
                checkFile(args[0], args[1]);
            } else {
                System.err.println("Ci sono troppi argomenti per il programma");
            }
        }catch (FileFormatException ex){
            System.err.println(ex.getMessage());
        }
    }

    private static void checkFile(String data, String savingPath) throws FileFormatException{
        if(data.contains(".csv")){
            File directory = new File(savingPath);
            if(directory.mkdir())
                System.out.println("Directory creata");
            CSVFile csvFile = new CSVFile(data);
            csvFile.logParseErrorRecord(savingPath, "Parse error");
            csvFile.logNullRecord(savingPath, "Null field");
            csvFile.logDataAnalysis(csvFile.performDataAnalysis(), savingPath, "Data analysis");
        }else {
            throw new FileFormatException("Serve un file in formato .csv");
        }
    }
}
