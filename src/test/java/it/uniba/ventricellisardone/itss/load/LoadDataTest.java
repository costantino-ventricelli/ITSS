package it.uniba.ventricellisardone.itss.load;

import com.google.cloud.bigquery.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LoadDataTest {

    private static final Map<Integer, String> DWH_HEADER;
    static {
        Map<Integer, String> dwhHeader = new HashMap<>();
        dwhHeader.put(0, "ordine_id_carrello");
        dwhHeader.put(1, "ordine_data");
        dwhHeader.put(2, "ordine_giorno_nome");
        dwhHeader.put(3, "ordine_giorno_dell_anno");
        dwhHeader.put(4, "ordine_mese_nome");
        dwhHeader.put(5, "ordine_anno_valore");
        dwhHeader.put(6, "ordine_mese_valore");
        dwhHeader.put(7, "ordine_trimestre");
        dwhHeader.put(8, "ordine_periodo");
        dwhHeader.put(9, "ordine_trimestre_anno");
        dwhHeader.put(10, "ordine_mese_anno");
        dwhHeader.put(11, "ordine_feriale_non");
        dwhHeader.put(12, "ordine_festivo_non");
        dwhHeader.put(13, "ordine_codice_stato");
        dwhHeader.put(14, "ordine_stato_nome");
        dwhHeader.put(15, "ordine_sesso_acquirente");
        dwhHeader.put(16, "ordine_quantita");
        dwhHeader.put(17, "ordine_prezzo_pagato");
        dwhHeader.put(18, "ordine_sconto");
        dwhHeader.put(19, "ordine_outlet");
        dwhHeader.put(20, "ordine_brand");
        dwhHeader.put(21, "ordine_collezione");
        dwhHeader.put(22, "ordine_colore");
        dwhHeader.put(23, "ordine_sesso_articolo");
        dwhHeader.put(24, "ordine_metodo_pagamento");
        dwhHeader.put(25, "ordine_taglia");
        dwhHeader.put(26, "ordine_categoria");
        dwhHeader.put(27, "ordine_macro_categoria");
        DWH_HEADER = Collections.unmodifiableMap(dwhHeader);
    }

    @Test
    public void rightConstructorTest() throws IOException {
        new LoadData(Objects.requireNonNull(LoadDataTest.class.getClassLoader().getResource("load_data_0.csv")).getPath(), "test_tabella");
    }

    @Test
    public void wrongConstructorTest(){
        Assertions.assertThrows(IOException.class, () -> new LoadData("file_not_found.csv", "test_tabella"), "File trovato inspiegabilmente");
    }

    @Test
    public void completeLoadDataOnDWH() throws IOException, InterruptedException {
        System.out.println("[EXECUTING] Test completo");
        LoadData loadData = new LoadData(Objects.requireNonNull(LoadDataTest.class.getClassLoader().getResource("")).getPath(), "test_tabella");
        loadData.startLoad(0, 1);
        BigQuery bigQuery = BigQueryOptions.getDefaultInstance().getService();
        QueryJobConfiguration jobConfiguration = QueryJobConfiguration.newBuilder("SELECT * FROM `biproject-itss.dataset.test_tabella`;").build();
        JobId jobId = JobId.of(UUID.randomUUID().toString());
        Job job = bigQuery.create(JobInfo.newBuilder(jobConfiguration).setJobId(jobId).build());
        job = job.waitFor();
        if(job == null)
            throw new RuntimeException("Job no longer exist");
        else if(job.getStatus().getError() != null)
            throw new RuntimeException(job.getStatus().getError().toString());

        TableResult result = job.getQueryResults();
        assert (result.getTotalRows() == 8) : "NUMERO RIGHE RESTITUITO NON CORRETTO";
        Scanner scanner = new Scanner(new File(Objects.requireNonNull(LoadDataTest.class.getClassLoader().getResource("data_result.json")).getPath()));
        String fileJSON = scanner.nextLine();
        JsonParser jsonParser = new JsonParser();
        JsonObject object = (JsonObject) jsonParser.parse(fileJSON);
        JsonArray array = (JsonArray) object.get("result");
        int i = 0;

        for(FieldValueList row : result.iterateAll()){
            JsonObject element = (JsonObject) array.get(i);
            for (int j = 0; j < element.size(); j++) {
                assert (row.get(j).getStringValue().equals(element.get(DWH_HEADER.get(j)).getAsString())) :
                        "ERRORE ALL'ELEMENTO: " + i + " CAMPO: " + DWH_HEADER.get(j) + " DWH: " + row.get(j).getStringValue() + " JSON: " + element.get(DWH_HEADER.get(j)).getAsString();
            }
            i++;
        }

        jobConfiguration = QueryJobConfiguration.newBuilder("DELETE * FROM `biproject-itss.dataset.test_tabella` WHERE TRUE;").build();
        job = bigQuery.create(JobInfo.newBuilder(jobConfiguration).setJobId(jobId).build());
        job.waitFor();
    }

    /*
    @Test
    public void interruptedLoadDataOnDWH() throws IOException, InterruptedException {
        System.out.println("[EXECUTING] Test interrotto");
        LoadData loadData = new LoadData(Objects.requireNonNull(LoadDataTest.class.getClassLoader().getResource("")).getPath(), "test_table");
        loadData.startLoad(0, 0);
        assert (loadData.getLastLoad(Calendar.getInstance().getTime()) == 3) : "ERRORE NEL SALVATAGGIO DEL JOB";
    }*/
}
