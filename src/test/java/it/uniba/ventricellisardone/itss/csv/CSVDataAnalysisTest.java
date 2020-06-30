package it.uniba.ventricellisardone.itss.csv;

import it.uniba.ventricellisardone.itss.csv.ecxception.CSVParsingException;
import it.uniba.ventricellisardone.itss.etl.Extraction;
import it.uniba.ventricellisardone.itss.etl.ExtractionTest;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class CSVDataAnalysisTest {

    @Test
    public void analysisTest() throws IOException, CSVParsingException {
        System.out.println("[INFO] AnalysisTest");
        Extraction extraction = new Extraction(Objects.requireNonNull(CSVDataAnalysisTest.class.getClassLoader().getResource("data_analysis" + File.separator + "right_data.csv")).getPath());
        CSVDataAnalysis CSVDataAnalysis = new CSVDataAnalysis(extraction.getCsvRecordList());

        Map<String, Map<String, Integer>> map = CSVDataAnalysis.performDataAnalysis();
        assert (map.equals(CSVStaticTestModel.getTestMap())) : "[ERROR] Incorrect data analysis";
        CSVDataAnalysis.logDataAnalysis(map, javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + File.separator + "Desktop" + File.separator + "TEST", "test_result_analysis.xml");

        Scanner testFile = new Scanner(new File(Objects.requireNonNull(ExtractionTest.class.getClassLoader().getResource("data_analysis" + File.separator + "data_analysis_test.xml")).getPath()));
        String resultPath =  javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + File.separator + "Desktop" + File.separator + "TEST" + File.separator + "Analysis" + File.separator + "test_result_analysis.xml";
        Scanner resultFile = new Scanner(new File(resultPath));
        int i = 0;
        while (resultFile.hasNextLine()){
            assert (resultFile.nextLine().trim().equals(testFile.nextLine().trim())) : "[ERROR] Data analysis line " + i + " not match";
            i++;
        }
    }

}
