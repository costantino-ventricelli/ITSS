package it.uniba.ventricellisardone.itss;

import it.uniba.ventricellisardone.itss.transform.Transform;
import it.uniba.ventricellisardone.itss.csv.CSVFile;
import it.uniba.ventricellisardone.itss.csv.DataAnalisys;
import it.uniba.ventricellisardone.itss.csv.FileFormatException;

import java.io.FileNotFoundException;
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
            CSVFile csvFile = new CSVFile(data);
            //costruzione file della classe Transform
            Transform transform = new Transform(csvFile.getCsvRecordList(), savingPath, "TransformFile");
            try {
                transform.buildFinalRecord();
            } catch (FileNotFoundException ex) {
                System.err.println(ex.getMessage());
            }
        }else {
            throw new FileFormatException("Serve un file in formato .csv");
        }
    }
}
