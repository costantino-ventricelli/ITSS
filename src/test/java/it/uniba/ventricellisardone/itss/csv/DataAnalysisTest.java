package it.uniba.ventricellisardone.itss.csv;

import it.uniba.ventricellisardone.itss.etl.Extraction;
import it.uniba.ventricellisardone.itss.etl.ExtractionTest;
import it.uniba.ventricellisardone.itss.log.Log;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class DataAnalysisTest {
    private static final String TAG = "DataAnalysisTest.class";

    @Test
    public void firstTest() throws FileNotFoundException {
        Extraction extraction = new Extraction(Objects.requireNonNull(DataAnalysisTest.class.getClassLoader().getResource("data_analysis/right_data.csv")).getPath());
        DataAnalysis dataAnalysis = new DataAnalysis(extraction.getCsvRecordList());

        Map<String, Map<String, Integer>> map = dataAnalysis.performDataAnalysis();
        Log.i(TAG, "Mappa di test");
        Log.i(TAG, CSVStaticTestModel.getTestMap().toString());
        assert (map.equals(CSVStaticTestModel.getTestMap())) : "[ERROR] Incorrect data analysis";
        dataAnalysis.logDataAnalysis(map, javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "/TEST", "test_result_analysis.xml");

        Scanner testFile = new Scanner(new File(Objects.requireNonNull(ExtractionTest.class.getClassLoader().getResource("data_analysis/data_analysis_test.xml")).getPath()));
        String resultPath =  javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "/TEST/Analysis/test_result_analysis.xml";
        Scanner resultFile = new Scanner(new File(resultPath));
        int i = 0;
        while (resultFile.hasNextLine()){
            assert (resultFile.nextLine().trim().equals(testFile.nextLine().trim())) : "[ERROR] Data analysis line " + i + " not match";
            i++;
        }
    }

}