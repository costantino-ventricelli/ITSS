package it.uniba.ventricellisardone.itss.ui;

import it.uniba.ventricellisardone.itss.csv.CSVRecord;
import it.uniba.ventricellisardone.itss.etl.Extraction;
import it.uniba.ventricellisardone.itss.etl.Transforming;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ETLForm {
    private JPanel etlPanel;
    private JButton chooseButton;
    private JTextArea console;
    private JProgressBar uploadProgress;
    private JLabel progressValue;

    public ETLForm() {
        JTextAreaOutputStream outputStream = new JTextAreaOutputStream(console);
        System.setOut(new PrintStream(outputStream));
        System.setErr(new PrintStream(outputStream));

        chooseButton.addActionListener(e -> {
            console.setText("");
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Seleziona file da analizzare");
            int filePath = chooser.showOpenDialog(etlPanel);
            if (filePath == JFileChooser.APPROVE_OPTION) {
                approvedOption(chooser);
            } else {
                System.out.println("Non è stato selezionato alcun file");
            }
        });
    }

    private void approvedOption(JFileChooser fileSource) {
        JFileChooser destinationChooser = new JFileChooser();
        destinationChooser.setDialogTitle("Seleziona cartella di destinazione");
        destinationChooser.setAcceptAllFileFilterUsed(false);
        destinationChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int directoryPath = destinationChooser.showOpenDialog(etlPanel);
        if (directoryPath == JFileChooser.APPROVE_OPTION) {
            transformMethod(destinationChooser, fileSource);
        } else {
            System.out.println("Non hai selezionato nessun file da analizzare");
        }
    }

    private void transformMethod(JFileChooser destinationChooser, JFileChooser sourceChooser) {
        System.out.println("Hai selezionato il file: " + sourceChooser.getSelectedFile().getPath());
        String destinationPath = destinationChooser.getSelectedFile().getPath() + "/"
                + new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime())
                + "/Transformed";
        System.out.println("Carico i dati in: " + destinationPath);
        Extraction extraction = new Extraction(sourceChooser.getSelectedFile().getPath());
        List<CSVRecord> csvRecordList = extraction.getCsvRecordList();
        ExecuteTransform executeTransform = new ExecuteTransform(csvRecordList, destinationPath);
        executeTransform.execute();
        if(!executeTransform.isDone()){
            uploadProgress.setValue(executeTransform.getProgress());
            progressValue.setText((executeTransform.getProgress() / 100 * csvRecordList.size() + 1) + "/" + csvRecordList.size() / 1000);
        }
    }

    public JPanel getEtlPanel() {
        return etlPanel;
    }

    private static class ExecuteTransform extends SwingWorker<Boolean, Void>{

        private final List<CSVRecord> csvRecordList;
        private final String destinationPath;


        public ExecuteTransform(List<CSVRecord> csvRecordList, String destinationPath) {
            this.csvRecordList = csvRecordList;
            this.destinationPath = destinationPath;
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            float transformedBlock;
            float transformBlock = csvRecordList.size() / 1000.00F;
            ArrayList<CSVRecord> subList = new ArrayList<>();
            try {
                Transforming transforming = new Transforming(destinationPath);
                for (int i = 0; i < csvRecordList.size(); i++) {
                    subList.add(csvRecordList.get(i));
                    if ((i % 1000) == 0 && (i != 0)) {
                        transformedBlock = (i % 1000 + 1);
                        setProgress((int) Math.round(transformedBlock / transformBlock * 100.00));
                        transforming.transformData(subList);
                        subList = new ArrayList<>();
                    }
                }
            } catch (IOException e) {
                System.err.println("Errore: " + e.getMessage());
            }
            return true;
        }

        @Override
        protected void done() {
            super.done();
        }
    }
}
