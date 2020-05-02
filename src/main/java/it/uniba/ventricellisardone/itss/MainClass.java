package it.uniba.ventricellisardone.itss;

import it.uniba.ventricellisardone.itss.csv.CSVFile;
import it.uniba.ventricellisardone.itss.csv.DataAnalisys;
import it.uniba.ventricellisardone.itss.csv.FileFormatException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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
            if(savingPath == null)
                savingPath = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(Calendar.getInstance().getTime());
            else
                savingPath = savingPath.concat(" ");
            CSVFile csvFile = new CSVFile(data);
            csvFile.logParseErrorRecord(savingPath, "Parse error");
            csvFile.logNullRecord(savingPath, "Null field");
            DataAnalisys dataAnalisys = new DataAnalisys(csvFile.getCsvRecordList());
            dataAnalisys.logDataAnalysis(dataAnalisys.performDataAnalysis(), savingPath, "Data analysis");
        }else {
            throw new FileFormatException("Serve un file in formato .csv");
        }
    }
}
