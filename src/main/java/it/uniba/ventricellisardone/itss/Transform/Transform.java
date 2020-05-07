package it.uniba.ventricellisardone.itss.Transform;


import it.uniba.ventricellisardone.itss.cloud.data.CloudData;
import it.uniba.ventricellisardone.itss.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Transform {

    private List<CSVRecord> csvRecordList;
    private List<Object> listaTotale; //ogni elemento di questa lista dovrebbe contenere un record sia con i campi di csv record si di cloudd data
    private PrintWriter writer;
    private CloudData cloudData;

    public Transform(List<CSVRecord> csvRecordList) {
        this.csvRecordList = csvRecordList;
    }

    public void takeDataFromClouData() {

    }


} //end class
