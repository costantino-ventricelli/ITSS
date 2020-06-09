package it.uniba.ventricellisardone.itss.ui;

import it.uniba.ventricellisardone.itss.csv.CSVDataAnalysis;
import it.uniba.ventricellisardone.itss.etl.Extraction;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class DataAnalysis {
    private JPanel dataAnalysis;
    private JButton chooseButton;
    private JTextArea console;

    public DataAnalysis(){
        
        JTextAreaOutputStream out = new JTextAreaOutputStream (console);
        System.setOut (new PrintStream(out));
        System.setErr(new PrintStream(out));
        
        chooseButton.addActionListener(e -> {
            console.setText("");
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Seleziona file da analizzare");
            int filePath = chooser.showOpenDialog(dataAnalysis);
            if(filePath == JFileChooser.APPROVE_OPTION) {
                approvedOption(chooser);
            }else{
                System.out.println("Non è stato selezionato alcun file");
            }
        });
    }

    private void approvedOption(JFileChooser chooser) {
        System.out.println("File selezionato: " + chooser.getSelectedFile().getName() + "");
        System.out.println("Estraggo i record dal file");
        Extraction extraction = new Extraction(chooser.getSelectedFile().getPath());
        System.out.println("Avvio analisi dati...");
        CSVDataAnalysis csvDataAnalysis = new CSVDataAnalysis(extraction.getCsvRecordList());
        Map<String, Map<String, Integer>> dataMap = csvDataAnalysis.performDataAnalysis();
        System.out.println("Salvo analisi...");
        try {
            savingAnalysis(csvDataAnalysis, dataMap, extraction);
        }catch (IOException ex){
            System.err.println(ex.getMessage());
        }
    }

    private void savingAnalysis(CSVDataAnalysis csvDataAnalysis, Map<String, Map<String, Integer>> dataMap, Extraction extraction) throws IOException {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleziona cartella salvataggio");
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int savingPath = chooser.showOpenDialog(dataAnalysis);
        if (savingPath == JFileChooser.APPROVE_OPTION) {
            String pathDirectory = chooser.getSelectedFile().getPath() + "/" + new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
            System.out.println("Cartella selezionata: " + pathDirectory);
            csvDataAnalysis.logDataAnalysis(dataMap, pathDirectory,"Data analysis");
            extraction.logNullRecord(pathDirectory, "Null record");
            extraction.logParseErrorRecord(pathDirectory, "Parsing error");
        }else{
            System.out.println("Selezionare una cartella per salvare l'analisi dei file");
        }
    }

    public JPanel getDataAnalysis() {
        return dataAnalysis;
    }
}
