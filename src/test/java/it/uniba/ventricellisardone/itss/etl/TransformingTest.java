package it.uniba.ventricellisardone.itss.etl;

import it.uniba.ventricellisardone.itss.csv.ecxception.CSVParsingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;
import java.util.Scanner;

public class TransformingTest {

    @Test
    public void firstTest() throws IOException, ParseException {
        Extraction extraction = new Extraction(Objects.requireNonNull(TransformingTest.class.getClassLoader().getResource("etl/transforming/transform_data.csv")).getPath());
        Transforming transforming = new Transforming(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "/Desktop/TEST");
        transforming.transformData(extraction.getCsvRecordList());
        Scanner transformedFile = new Scanner(new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "/Desktop/TEST/load_data_0.csv"));
        Scanner testFile = new Scanner(new File(Objects.requireNonNull(TransformingTest.class.getClassLoader().getResource("etl/transforming/transformed_data.csv")).getPath()));
        int i = 0;
        while(transformedFile.hasNextLine()) {
            assert (testFile.nextLine().trim().equals(transformedFile.nextLine().trim())) : "Errore alla linea: " + i;
            i++;
        }
    }

    @Test
    public void secondTest() throws IOException, CSVParsingException {
        Extraction extraction = new Extraction(Objects.requireNonNull(TransformingTest.class.getClassLoader().getResource("etl/transforming/transform_data_error.csv")).getPath());
        Transforming transforming = new Transforming(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "/Desktop/TEST");
        Assertions.assertThrows(ParseException.class, () -> transforming.transformData(extraction.getCsvRecordList()), "Eccezione non sollevata");
    }

    @Test
    public void thirdTest() throws IOException, ParseException {
        Extraction extraction = new Extraction(Objects.requireNonNull(TransformingTest.class.getClassLoader().getResource("etl/transforming/transform_data.csv")).getPath());
        Transforming transforming = new Transforming(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "/Desktop/TEST");
        transforming.transformData(extraction.getCsvRecordList());
        extraction = new Extraction(Objects.requireNonNull(TransformingTest.class.getClassLoader().getResource("etl/transforming/transform_data_1.csv")).getPath());
        transforming.transformData(extraction.getCsvRecordList());
        Assertions.assertDoesNotThrow(() -> new Scanner(new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "/Desktop/TEST/load_data_0.csv")) );
        Assertions.assertDoesNotThrow(() -> new Scanner(new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "/Desktop/TEST/load_data_1.csv")) );
    }
}
