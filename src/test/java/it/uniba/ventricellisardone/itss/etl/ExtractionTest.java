package it.uniba.ventricellisardone.itss.etl;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class ExtractionTest {

    private static final String TAG = "CSVFileTest.class";

    @Test
    public void firstTest() throws FileNotFoundException {
        Extraction extraction = new Extraction(Objects.requireNonNull(ExtractionTest.class.getClassLoader().getResource("right_data.csv")).getPath());
        assert (extraction.getHeaderFile().equals(Extraction.HEADER_FILE)) : "[ERROR] Incorrect header file";
        assert (extraction.getCsvRecordList().equals(ETLStaticTestModel.getTestList())) : "[ERROR] Incorrect list of record";
    }

    @Test
    public void secondTest() throws IOException {
        Extraction extraction = new Extraction(Objects.requireNonNull(ExtractionTest.class.getClassLoader().getResource("etl/extraction/missing_fields_data.csv")).getPath());
        assert (extraction.getNullRecordList().equals(ETLStaticTestModel.getNullFieldsList())) : "[ERROR] Null list record incorrect";
        extraction.logNullRecord(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "/TEST", "test_result_null_fields.csv");
        Scanner testFile = new Scanner(new File(Objects.requireNonNull(ExtractionTest.class.getClassLoader().getResource("etl/extraction/null_data_fields_test.csv")).getPath()));
        Scanner resultFile = new Scanner(new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "/TEST/Results/test_result_null_fields.csv"));
        int i = 0;
        while (resultFile.hasNextLine()){
            assert (resultFile.nextLine().equals(testFile.nextLine())) : "[ERROR] Null field line " + i + " not match";
            i++;
        }
    }

    @Test
    public void thirdTest() throws IOException {
        Extraction extraction = new Extraction(Objects.requireNonNull(ExtractionTest.class.getClassLoader().getResource("etl/extraction/parsing_error_data.csv")).getPath());
        assert (extraction.getParseErrorList().equals(ETLStaticTestModel.getParsingErrorList())) : "[ERROR] Parsing error list incorrect";
        extraction.logParseErrorRecord(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "/TEST", "parsing_error_result.csv");
        Scanner testFile = new Scanner(new File(Objects.requireNonNull(ExtractionTest.class.getClassLoader().getResource("etl/extraction/parsing_error_test.csv")).getPath()));
        Scanner resultFile = new Scanner(new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "/TEST/Results/parsing_error_result.csv"));
        int i = 0;
        while (resultFile.hasNextLine()) {
            assert (resultFile.nextLine().equals(testFile.nextLine())) : "[ERROR] Parsing error line " + i + " not match";
            i++;
        }
    }
}
