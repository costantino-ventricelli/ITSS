package it.uniba.ventricellisardone.itss.csv;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class CSVFileTest {

    @Test
    public void firstTest(){
        CSVFile csvFile = new CSVFile(Objects.requireNonNull(
                CSVFileTest.class.getClassLoader().getResource("right_data.csv")).getPath());
        assert (csvFile.getHeaderFile().equals(CSVFile.HEADER_FILE)) : "[ERROR] Incorrect header file";
        assert (csvFile.getCsvRecordList().equals(StaticTestModel.getTestList())) : "[ERROR] Incorrect list of record";
        Map<String, Map<String, Integer>> map = csvFile.performDataAnalysis();
        assert (map.equals(StaticTestModel.getTestMap())) : "[ERROR] Incorrect data analysis";
        csvFile.logDataAnalysis(map, Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("")).getPath(), "test_result_analysis.xml");
        Scanner testFile = new Scanner(Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("data_analysis_test.xml")).getPath());
        Scanner resultFile = new Scanner(Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("test_result_analysis.xml")).getPath());
        int i = 0;
        while (resultFile.hasNextLine()){
            assert (resultFile.nextLine().equals(testFile.nextLine())) : "[ERROR] Data analysis line " + i + " not match";
            i++;
        }
    }

    @Test
    public void secondTest(){
        CSVFile csvFile = new CSVFile(Objects.requireNonNull(
                CSVFileTest.class.getClassLoader().getResource("missing_fields_data.csv")).getPath());
        assert (csvFile.getNullRecordList().equals(StaticTestModel.getNullFieldsList())) : "[ERROR] Null list record incorrect";
        csvFile.logNullRecord(Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("")).getPath(), "test_result_null_fields.csv");
        Scanner testFile = new Scanner(Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("null_data_fields_test.csv")).getPath());
        Scanner resultFile = new Scanner(Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("test_result_null_fields.csv")).getPath());
        int i = 0;
        while (resultFile.hasNextLine()){
            assert (resultFile.nextLine().equals(testFile.nextLine())) : "[ERROR] Null field line " + i + " not match";
            i++;
        }
    }

    @Test
    public void thirdTest(){
        CSVFile csvFile = new CSVFile(Objects.requireNonNull(
                CSVFileTest.class.getClassLoader().getResource("parsing_error_data.csv")).getPath());
        assert (csvFile.getParseErrorList().equals(StaticTestModel.getParsingErrorList())) : "[ERROR] Parsing error list incorrect";
        csvFile.logParseErrorRecord(Objects.requireNonNull(StaticTestModel.class.getClassLoader().getResource("")).getPath(), "parsing_error_result.csv");
        Scanner testFile = new Scanner(Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("parsing_error_test.csv")).getPath());
        Scanner resultFile = new Scanner(Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("test_result_parsing_error.csv")).getPath());
        int i = 0;
        while(resultFile.hasNextLine()){
            assert (resultFile.nextLine().equals(testFile.nextLine())) : "[ERROR] Parsing error line " + i + " not match";
            i++;
        }
    }
}
