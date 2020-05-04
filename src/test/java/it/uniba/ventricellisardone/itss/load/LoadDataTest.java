package it.uniba.ventricellisardone.itss.load;

import com.google.cloud.bigquery.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.nashorn.internal.parser.JSONParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

public class LoadDataTest {

    @Test
    public void rightConstructorTest() throws FileNotFoundException {
        LoadData loadData = new LoadData(Objects.requireNonNull(LoadDataTest.class.getClassLoader().getResource("load_data.sql")).getPath());
    }

    @Test
    public void wrongConstructorTest(){
        Assertions.assertThrows(FileNotFoundException.class, () -> new LoadData("file_not_found.sql"), "File trovato inspiegabilmente");
    }

    @Test
    public void completeLoadDataOnDWH() throws FileNotFoundException, InterruptedException {
        LoadData loadData = new LoadData(Objects.requireNonNull(LoadDataTest.class.getClassLoader().getResource("load_data.sql")).getPath());
        loadData.startLoad();
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
        assert (result.getTotalRows() == 8) : "Numero di righe restitutito non corretto";
        Scanner scanner = new Scanner(new File(Objects.requireNonNull(LoadDataTest.class.getClassLoader().getResource("data_result.json")).getPath()));
        String fileJSON = scanner.nextLine();
        JsonParser jsonParser = new JsonParser();
        JsonObject object = (JsonObject) jsonParser.parse(fileJSON);
        JsonArray array = (JsonArray) object.get("result");
        int i = 0;
        for(FieldValueList row : result.iterateAll()){
            JsonObject element = (JsonObject) array.get(i);
        }
    }

    @Test
    public void interruptedLoadDataOnDWH(){

    }
}
