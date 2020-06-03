package it.uniba.ventricellisardone.itss;

import it.uniba.ventricellisardone.itss.cloud.data.CloudData;
import it.uniba.ventricellisardone.itss.csv.CSVFile;
import it.uniba.ventricellisardone.itss.csv.CSVRecord;
import it.uniba.ventricellisardone.itss.log.Log;
import it.uniba.ventricellisardone.itss.transform.Transform;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.*;

public class MainClass {

    public static void main(String[] args){
        CSVFile csvFile = new CSVFile("/Users/costantinoventricelli/Desktop/dev/JAVA/ITSS/src/main/resources/right_data.csv");
        Transform transform = new Transform("/Users/costantinoventricelli/Desktop/2020-06-03/");
        List<CSVRecord> csvRecordList = csvFile.getCsvRecordList();
        ArrayList<CSVRecord> transformList = new ArrayList<>();
        for(CSVRecord record : csvRecordList){
            System.out.println("Leggo record");
            if(transformList.size() <= 1000){
                transformList.add(record);
            }else{
                transform(transform, transformList);
                break;
            }
        }
        transform(transform, transformList);
    }

    private static void transform(Transform transform, ArrayList<CSVRecord> transformList) {
        try {
            transform.transformData(transformList);
        } catch (ParseException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
