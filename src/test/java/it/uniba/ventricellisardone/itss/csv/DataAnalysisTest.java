package it.uniba.ventricellisardone.itss.csv;

import it.uniba.ventricellisardone.itss.log.Log;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class DataAnalysisTest {
    private static final String TAG = "DataAnalisysTest.class";

    @Test
    public void firstTest() throws FileNotFoundException {
        CSVFile csvFile = new CSVFile(Objects.requireNonNull(DataAnalysisTest.class.getClassLoader().getResource("right_data.csv")).getPath());
        DataAnalysis dataAnalysis = new DataAnalysis(csvFile.getCsvRecordList());

        Map<String, Map<String, Integer>> map = dataAnalysis.performDataAnalysis();
        Log.i(TAG, "Mappa di test");
        Log.i(TAG, StaticTestModel.getTestMap().toString());
        assert (map.equals(StaticTestModel.getTestMap())) : "[ERROR] Incorrect data analysis";
        dataAnalysis.logDataAnalysis(map, Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("")).getPath(), "test_result_analysis.xml");

        Scanner testFile = new Scanner(new File(Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("data_analysis_test.xml")).getPath()));
        String resultPath = Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("Analysis")).getPath() + "/test_result_analysis.xml";
        Scanner resultFile = new Scanner(new File(resultPath));
        int i = 0;
        while (resultFile.hasNextLine()){
            assert (resultFile.nextLine().trim().equals(testFile.nextLine().trim())) : "[ERROR] Data analysis line " + i + " not match";
            i++;
        }
    }

}
