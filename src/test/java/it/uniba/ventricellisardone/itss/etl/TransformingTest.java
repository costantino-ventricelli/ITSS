package it.uniba.ventricellisardone.itss.etl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigquery.*;
import com.google.common.collect.Lists;
import it.uniba.ventricellisardone.itss.csv.ecxception.CSVParsingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

public class TransformingTest {

    private static final String TABLE_NAME = "test_tabella";

    @Test
    public void correctTest() throws IOException, ParseException, InterruptedException {
        System.out.println("[INFO] CorrectTest");
        Extraction extraction = new Extraction(Objects.requireNonNull(TransformingTest.class.getClassLoader().getResource("etl" + File.separator + "transforming" + File.separator + "transform_data.csv")).getPath());
        Transforming transforming = new Transforming(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + File.separator + "Desktop" + File.separator + "TEST", TABLE_NAME);
        transforming.transformData(extraction.getCsvRecordList());
        Scanner transformedFile = new Scanner(new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + File.separator + "Desktop" + File.separator + "TEST" + File.separator + "load_data_0.csv"));
        Scanner testFile = new Scanner(new File(Objects.requireNonNull(TransformingTest.class.getClassLoader().getResource("etl" + File.separator + "transforming" + File.separator + "transformed_data.csv")).getPath()));
        int i = 0;
        while (transformedFile.hasNextLine()) {
            String testLine = testFile.nextLine().trim();
            String transformedLine = transformedFile.nextLine().trim();
            System.out.println("TEST LINE: " + testLine);
            System.out.println("TRANSFORMED LINE: " + transformedLine);
            assert (testLine.equals(transformedLine)) : "Errore alla linea: " + i;
            i++;
        }
    }

    @Test
    public void parseErrorTest() throws IOException, CSVParsingException {
        System.out.println("[INFO] ParseErrorTest");
        Extraction extraction = new Extraction(Objects.requireNonNull(TransformingTest.class.getClassLoader().getResource("etl" + File.separator + "transforming" + File.separator + "transform_data_error.csv")).getPath());
        Transforming transforming = new Transforming(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + File.separator + "Desktop" + File.separator + "TEST", TABLE_NAME);
        Assertions.assertThrows(ParseException.class, () -> transforming.transformData(extraction.getCsvRecordList()), "Eccezione non sollevata");
    }

    @Test
    public void chooseFileTest() throws IOException, ParseException {
        System.out.println("[INFO] ChooseFileTest");
        Extraction extraction = new Extraction(Objects.requireNonNull(TransformingTest.class.getClassLoader().getResource("etl" + File.separator + "transforming" + File.separator + "transform_data.csv")).getPath());
        Transforming transforming = new Transforming(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + File.separator + "Desktop" + File.separator + "TEST", TABLE_NAME);
        transforming.transformData(extraction.getCsvRecordList());
        extraction = new Extraction(Objects.requireNonNull(TransformingTest.class.getClassLoader().getResource("etl" + File.separator + "transforming" + File.separator + "transform_data_1.csv")).getPath());
        transforming.transformData(extraction.getCsvRecordList());
        Assertions.assertDoesNotThrow(() -> new Scanner(new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + File.separator + "Desktop" + File.separator + "TEST" + File.separator + "load_data_0.csv")));
        Assertions.assertDoesNotThrow(() -> new Scanner(new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + File.separator + "Desktop" + File.separator + "TEST" + File.separator + "load_data_1.csv")));
    }

    @Test
    public void conflictLoadTest() throws ParseException, IOException {
        System.out.println("[INFO] ConflictLoadTest");
        Extraction extraction = new Extraction(Objects.requireNonNull(TransformingTest.class.getClassLoader().getResource("etl" + File.separator + "transforming" + File.separator + "transform_data_conflict.csv")).getPath());
        Transforming transforming = new Transforming(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + File.separator + "Desktop" + File.separator + "TEST", TABLE_NAME);
        transforming.transformData(extraction.getCsvRecordList());
        Scanner duplicateData = new Scanner(new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + File.separator + "Desktop" + File.separator + "TEST" + File.separator + "duplicate_data.csv"));
        Scanner testFileData = new Scanner(new File(Objects.requireNonNull(TransformingTest.class.getClassLoader().getResource("etl" + File.separator + "loading" + File.separator + "load_data_0.csv")).getPath()));
        //Salto l'header file del file di test
        testFileData.nextLine();
        while(testFileData.hasNextLine())
            assert (testFileData.nextLine().equals(duplicateData.nextLine()));
    }
}
