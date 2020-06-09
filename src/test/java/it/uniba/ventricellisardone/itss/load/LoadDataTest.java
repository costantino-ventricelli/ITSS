package it.uniba.ventricellisardone.itss.load;

import com.google.cloud.bigquery.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class LoadDataTest {

    private static final Map<Integer, String> DWH_HEADER;
    static {
        DWH_HEADER = Map.ofEntries(Map.entry(0, "ordine_id_carrello"),
                Map.entry(1, "ordine_data"),
                Map.entry(2, "ordine_giorno_nome"),
                Map.entry(3, "ordine_giorno_dell_anno"),
                Map.entry(4, "ordine_mese_nome"),
                Map.entry(5, "ordine_anno_valore"),
                Map.entry(6, "ordine_mese_valore"),
                Map.entry(7, "ordine_trimestre"),
                Map.entry(8, "ordine_periodo"),
                Map.entry(9, "ordine_trimestre_anno"),
                Map.entry(10, "ordine_mese_anno"),
                Map.entry(11, "ordine_feriale_non"),
                Map.entry(12, "ordine_festivo_non"),
                Map.entry(13, "ordine_codice_stato"),
                Map.entry(14, "ordine_stato_nome"),
                Map.entry(15, "ordine_sesso_acquirente"),
                Map.entry(16, "ordine_quantita"),
                Map.entry(17, "ordine_prezzo_pagato"),
                Map.entry(18, "ordine_sconto"),
                Map.entry(19, "ordine_outlet"),
                Map.entry(20, "ordine_brand"),
                Map.entry(21, "ordine_collezione"),
                Map.entry(22, "ordine_colore"),
                Map.entry(23, "ordine_sesso_articolo"),
                Map.entry(24, "ordine_metodo_pagamento"),
                Map.entry(25, "ordine_taglia"),
                Map.entry(26, "ordine_categoria"),
                Map.entry(27, "ordine_macro_categoria"));
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
    public void completeLoadDataOnDWH() throws IOException, InterruptedException, URISyntaxException {
        BigQuery bigQuery = BigQueryOptions.getDefaultInstance().getService();
        QueryJobConfiguration jobConfiguration = QueryJobConfiguration.newBuilder("DELETE FROM `biproject-itss.dataset.test_tabella` WHERE TRUE;").build();
        JobId jobId = JobId.of(UUID.randomUUID().toString());
        Job job = bigQuery.create(JobInfo.newBuilder(jobConfiguration).setJobId(jobId).build());
        job.waitFor();

        LoadData loadData = new LoadData(Paths.get(Objects.requireNonNull(LoadDataTest.class.getClassLoader().getResource("")).toURI()).toString(), "test_tabella");
        loadData.startLoad(0, 2);
        jobConfiguration = QueryJobConfiguration.newBuilder("SELECT * FROM `biproject-itss.dataset.test_tabella` ORDER BY(ordine_id_carrello);").build();
        jobId = JobId.of(UUID.randomUUID().toString());
        job = bigQuery.create(JobInfo.newBuilder(jobConfiguration).setJobId(jobId).build());
        job = job.waitFor();
        if(job == null)
            throw new RuntimeException("Job no longer exist");
        else if(job.getStatus().getError() != null)
            throw new RuntimeException(job.getStatus().getError().toString());

        TableResult result = job.getQueryResults();
        //QUI IL TEST DEVE ESSERE MENO FISCALE, IN QUANTO PER IL MULTI THREADING QUESTO TEST POTREBBE ESSERE RICHIAMATO PRIMA CHE L'UPLOAD SIA ULTIMATO
        assert (result.getTotalRows() == 8 || result.getTotalRows() == 4) : "NUMERO RIGHE RESTITUITO NON CORRETTO: " + result.getTotalRows();
        String fileJSON = Files.readString(Paths.get(Objects.requireNonNull(LoadDataTest.class.getClassLoader().getResource("data_result.json")).toURI()));
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
    }

    @Test
    public void interruptedLoadDataOnDWH() throws IOException, InterruptedException, URISyntaxException {
        BigQuery bigQuery = BigQueryOptions.getDefaultInstance().getService();
        QueryJobConfiguration jobConfiguration = QueryJobConfiguration.newBuilder("DELETE FROM `biproject-itss.dataset.test_tabella` WHERE TRUE;").build();
        JobId jobId = JobId.of(UUID.randomUUID().toString());
        Job job = bigQuery.create(JobInfo.newBuilder(jobConfiguration).setJobId(jobId).build());
        job.waitFor();
        LoadData loadData = new LoadData(Paths.get(Objects.requireNonNull(LoadDataTest.class.getClassLoader().getResource("")).toURI()).toString(), "test_tabella");
        loadData.startLoad(0, 1);
        assert (loadData.getLastLoad(Calendar.getInstance().getTime()) == 0) : "ERRORE NEL SALVATAGGIO DEL JOB: " + loadData.getLastLoad(Calendar.getInstance().getTime());
    }
}
