package it.uniba.ventricellisardone.itss.csv;

import it.uniba.ventricellisardone.itss.log.Log;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class CSVFileTest {

    private static final String TAG = "CSVFileTest.class";

    @Test
    public void firstTest() throws FileNotFoundException {
        CSVFile csvFile = new CSVFile(Objects.requireNonNull(
                CSVFileTest.class.getClassLoader().getResource("right_data.csv")).getPath());
        assert (csvFile.getHeaderFile().equals(CSVFile.HEADER_FILE)) : "[ERROR] Incorrect header file";
        assert (csvFile.getCsvRecordList().equals(StaticTestModel.getTestList())) : "[ERROR] Incorrect list of record";
        Map<String, Map<String, Integer>> map = csvFile.performDataAnalysis();
        Log.i(TAG, "Mappa di test");
        Log.i(TAG, StaticTestModel.getTestMap().toString());
        assert (map.equals(StaticTestModel.getTestMap())) : "[ERROR] Incorrect data analysis";
        csvFile.logDataAnalysis(map, Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("")).getPath(), "test_result_analysis.xml");
        Scanner testFile = new Scanner(new File(Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("data_analysis_test.xml")).getPath()));
        String resultPath = Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("")).getPath() + "/results/test_result_analysis.xml";
        Scanner resultFile = new Scanner(new File(resultPath));
        int i = 0;
        while (resultFile.hasNextLine()){
            assert (resultFile.nextLine().equals(testFile.nextLine())) : "[ERROR] Data analysis line " + i + " not match";
            i++;
        }
    }

    @Test
    public void secondTest() throws FileNotFoundException {
        CSVFile csvFile = new CSVFile(Objects.requireNonNull(
                CSVFileTest.class.getClassLoader().getResource("missing_fields_data.csv")).getPath());
        assert (csvFile.getNullRecordList().equals(StaticTestModel.getNullFieldsList())) : "[ERROR] Null list record incorrect";
        csvFile.logNullRecord(Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("")).getPath(), "test_result_null_fields.csv");
        Scanner testFile = new Scanner(new File(Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("null_data_fields_test.csv")).getPath()));
        String resultPath = Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("")).getPath() + "/results/test_result_null_fields.csv";
        Scanner resultFile = new Scanner(new File(resultPath));
        int i = 0;
        while (resultFile.hasNextLine()){
            assert (resultFile.nextLine().equals(testFile.nextLine())) : "[ERROR] Null field line " + i + " not match";
            i++;
        }
    }

    @Test
    public void thirdTest() {
        CSVFile csvFile = new CSVFile(Objects.requireNonNull(
                CSVFileTest.class.getClassLoader().getResource("parsing_error_data.csv")).getPath());
        assert (csvFile.getParseErrorList().equals(StaticTestModel.getParsingErrorList())) : "[ERROR] Parsing error list incorrect";
        csvFile.logParseErrorRecord(Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("")).getPath(), "parsing_error_result.csv");
        Scanner testFile = new Scanner(Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("parsing_error_test.csv")).getPath());
        String resultPath = Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("")).getPath() + "/result/test_result_parsing_error.csv";
        Scanner resultFile = new Scanner(resultPath);
        int i = 0;
        while (resultFile.hasNextLine()) {
            assert (resultFile.nextLine().equals(testFile.nextLine())) : "[ERROR] Parsing error line " + i + " not match";
            i++;
        }
    }
}
