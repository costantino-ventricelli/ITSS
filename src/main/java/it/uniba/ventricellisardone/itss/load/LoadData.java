package it.uniba.ventricellisardone.itss.load;

import com.google.cloud.bigquery.*;
import it.uniba.ventricellisardone.itss.log.Log;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class LoadData {

    private final static String TAG = "LoadData.class";
    private final static String DATASET = "dataset";
    private final static String LOCATION = "eu";

    private final String dataDirectory;
    private int actualLoad;
    private final BigQuery bigQuery;
    private final WriteChannelConfiguration writeChannelConfiguration;
    private final JobId jobId;

    public LoadData(String dataDirectory, String tableName) throws IOException {
        File directory = new File(dataDirectory);
        if (!directory.exists())
            throw new IOException("Directory not found");
        else {
            this.dataDirectory = dataDirectory;
            bigQuery = BigQueryOptions.getDefaultInstance().getService();
            TableId tableId = TableId.of(DATASET, tableName);
            writeChannelConfiguration = WriteChannelConfiguration.newBuilder(tableId).setFormatOptions(FormatOptions.csv()).setAutodetect(true).build();
            jobId = JobId.newBuilder().setLocation(LOCATION).setProject("biproject-itss").build();
        }
    }

    public void startLoad(int startFrom, int endTo) throws IOException, InterruptedException {
        this.actualLoad = startFrom;
        saveOperation();
        Thread thread = null;
        for (int i = startFrom; i < endTo; i++) {
            String file = dataDirectory + "/data_load_" + i + ".csv";
            System.out.println("[EXECUTING] File directory: " + file);
            System.out.println("[EXECUTING] CARICO IL FILE CSV N°: " + i);
            TableDataWriteChannel writer = bigQuery.writer(jobId, writeChannelConfiguration);
            try (OutputStream stream = Channels.newOutputStream(writer)) {
                Files.copy(Paths.get(file), stream);
            } catch (IOException | NullPointerException e) {
                System.out.println("[EXECUTING] ERRORE: " + e.getMessage());
                Log.e(TAG, "ERRORE OUTPUT STREAM", e);
            }
            if(thread != null)
                thread.join();
            thread = new Thread(new UploadCSVFile(writer));
            thread.start();
            System.out.println("[EXECUTING] ESEGUO UPLOAD");
            this.actualLoad = i;
            saveOperation();
        }
    }

    private void saveOperation() throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(Calendar.getInstance().getTime())));
        outputStream.writeInt(actualLoad);
        outputStream.close();
    }

    public int getLastLoad(Date date) throws IOException {
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(date)));
        int readInt = inputStream.readInt();
        inputStream.close();
        return readInt;
    }

    private static class UploadCSVFile implements Runnable{

        private final TableDataWriteChannel writer;

        public UploadCSVFile(TableDataWriteChannel writer) {
            this.writer = writer;
        }

        @Override
        public void run() {
            Job job = writer.getJob();
            try {
                job = job.waitFor();
                if (job.getStatus().getError() != null) {
                    System.err.println("[EXECUTING] ERRORE: " + job.getStatus().getError());
                    JobStatistics.LoadStatistics statistics = job.getStatistics();
                    System.out.println("[EXECUTING] CARICATI: " + statistics.getOutputRows() + " RECORDS");
                }
            } catch (InterruptedException e) {
                Log.e(TAG, "ERRORE IN RUNNABLE", e);
                System.err.println("[EXECUTING] ERRORE IN RUNNABLE: " + e.getMessage());
            }
        }
    }
}
