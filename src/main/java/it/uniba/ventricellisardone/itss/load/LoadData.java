package it.uniba.ventricellisardone.itss.load;

import com.google.cloud.bigquery.BigQuery;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class LoadData {

    private BigQuery bigQuery;
    private Scanner fileReader;
    private PrintWriter saveFile;

    public LoadData(String dataPath) throws FileNotFoundException{
        readFile(dataPath);
    }

    public void startLoad(){

    }

    private void executeQuery(String query){

    }

    private void readFile(String dataPath) throws FileNotFoundException {

    }

    private void saveOperation(){

    }
}
