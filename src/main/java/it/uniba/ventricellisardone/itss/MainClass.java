package it.uniba.ventricellisardone.itss;

import it.uniba.ventricellisardone.itss.cloud.data.CloudData;

import java.text.ParseException;
import java.util.Calendar;

public class MainClass {

    public static void main(String[] args){
        try {
            CloudData cloudData = new CloudData(Calendar.getInstance().getTime());
            System.out.println(cloudData.getFestivo());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
