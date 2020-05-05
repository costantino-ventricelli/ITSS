package it.uniba.ventricellisardone.itss.load;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableSchema;
import com.google.cloud.bigquery.*;

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
    private final static List<TableFieldSchema> SCHEMA;

    static {
        List<TableFieldSchema> arrayList = new ArrayList<>();
        arrayList.add(new TableFieldSchema().setName("ordine_id_carrello").setName("INTEGER"));
        arrayList.add(new TableFieldSchema().setName("ordine_data").setName("DATE"));
        arrayList.add(new TableFieldSchema().setName("ordine_giorno_nome").setName("STRING"));
        arrayList.add(new TableFieldSchema().setName("ordine_giorno_dell_anno").setName("INTEGER"));
        arrayList.add(new TableFieldSchema().setName("ordine_mese_nome").setName("STRING"));
        arrayList.add(new TableFieldSchema().setName("ordine_anno_valore").setName("INTEGER"));
        arrayList.add(new TableFieldSchema().setName("ordine_mese_valore").setName("INTEGER"));
        arrayList.add(new TableFieldSchema().setName("ordine_trimestre").setName("STRING"));
        arrayList.add(new TableFieldSchema().setName("ordine_periodo").setName("STRING"));
        arrayList.add(new TableFieldSchema().setName("ordine_trimestre_anno").setName("STRING"));
        arrayList.add(new TableFieldSchema().setName("ordine_mese_anno").setName("STRING"));
        arrayList.add(new TableFieldSchema().setName("ordine_feriale_non").setName("STRING"));
        arrayList.add(new TableFieldSchema().setName("ordine_festivo_non").setName("STRING"));
        arrayList.add(new TableFieldSchema().setName("ordine_codice_stato").setName("STRING"));
        arrayList.add(new TableFieldSchema().setName("ordine_stato_nome").setName("STRING"));
        arrayList.add(new TableFieldSchema().setName("ordine_sesso_acquirente").setName("STRING"));
        arrayList.add(new TableFieldSchema().setName("ordine_quantita").setName("INTEGER"));
        arrayList.add(new TableFieldSchema().setName("ordine_prezzo_pagato").setName("FLOAT"));
        arrayList.add(new TableFieldSchema().setName("ordine_sconto").setName("INTEGER"));
        arrayList.add(new TableFieldSchema().setName("ordine_outlet").setName("STRING"));
        arrayList.add(new TableFieldSchema().setName("ordine_brand").setName("STRING"));
        arrayList.add(new TableFieldSchema().setName("ordine_collezione").setName("STRING"));
        arrayList.add(new TableFieldSchema().setName("ordine_colore").setName("STRING"));
        arrayList.add(new TableFieldSchema().setName("ordine_sesso_articolo").setName("STRING"));
        arrayList.add(new TableFieldSchema().setName("ordine_metodo_pagamento").setName("STRING"));
        arrayList.add(new TableFieldSchema().setName("ordine_taglia").setName("STRING"));
        arrayList.add(new TableFieldSchema().setName("ordine_categoria").setName("STRING"));
        arrayList.add(new TableFieldSchema().setName("ordine_macro_categoria").setName("STRING"));
        SCHEMA = Collections.unmodifiableList(arrayList);
    }

    private final String dataDirectory;
    private int actualLoad;
    private final TableDataWriteChannel writer;

    public LoadData(String dataDirectory, String tableName) throws IOException {
        File directory = new File(dataDirectory);
        if(!directory.exists())
            throw new IOException("Directory not found");
        else {
            this.dataDirectory = dataDirectory;
            BigQuery bigQuery = BigQueryOptions.getDefaultInstance().getService();
            TableId tableId = TableId.of(DATASET, tableName);
            WriteChannelConfiguration writeChannelConfiguration = WriteChannelConfiguration.newBuilder(tableId).setFormatOptions(FormatOptions.csv()).setAutodetect(true).build();
            JobId jobId = JobId.newBuilder().setLocation(LOCATION).setProject("biproject-itss").build();
            this.writer = bigQuery.writer(jobId, writeChannelConfiguration);
        }
    }

    public void startLoad(int startFrom, int endTo) throws IOException, InterruptedException {
        this.actualLoad = startFrom;
        saveOperation();
        for(int i = startFrom; i < endTo; i++){
            String file = new File(dataDirectory + "/load_data_" + i + ".csv").getPath();
            System.out.println("[EXECUTING] File directory: " + file);
            try (OutputStream stream = Channels.newOutputStream(writer)){
                Files.copy(Paths.get(file), stream);
            }
            Job job = writer.getJob();
            job = job.waitFor();
            if(job.getStatus().getError() != null){
                System.err.println("[EXECUTING] ERRORE: " + job.getStatus().getError());
            }
            JobStatistics.LoadStatistics statistics = job.getStatistics();
            System.out.println("[EXECUTING] Output rows: " + statistics.getOutputRows());
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

    public int getActualLoad(){
        return actualLoad;
    }
}
