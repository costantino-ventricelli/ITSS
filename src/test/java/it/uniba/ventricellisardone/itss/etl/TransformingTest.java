package it.uniba.ventricellisardone.itss.etl;

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
        Transforming transforming = new Transforming(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "/TEST");
        transforming.transformData(extraction.getCsvRecordList());
        Scanner transformedFile = new Scanner(new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "/TEST/load_data_0.csv"));
        Scanner testFile = new Scanner(new File(Objects.requireNonNull(TransformingTest.class.getClassLoader().getResource("etl/transforming/transformed_data.csv")).getPath()));
        int i = 0;
        while(transformedFile.hasNextLine()) {
            String testLine = testFile.nextLine().trim();
            String testingLine = transformedFile.nextLine().trim();
            char[] testLineArray = testLine.toCharArray();
            char[] testingLineArray = testingLine.toCharArray();
            for(int j = 0; j < testingLineArray.length; j++) {
                if(testingLineArray[j] != testLineArray[j])
                    System.out.println("TestLine: " + testLineArray[j] + " TestingLine: " + testingLineArray[j]);
            }
            assert (testLine.equals(testingLine)) : "Errore alla linea: " + i + " Testing Line: " + testingLine + " Test Line: " + testLine;
            i++;
        }
    }

    @Test
    public void secondTest(){
        Extraction extraction = new Extraction(Objects.requireNonNull(TransformingTest.class.getClassLoader().getResource("etl/transforming/transform_data_error.csv")).getPath());
        Transforming transforming = new Transforming("/Users/costantinoventricelli/Desktop/TEST");
        Assertions.assertThrows(ParseException.class, () -> transforming.transformData(extraction.getCsvRecordList()), "Eccezione non sollevata");
    }

    @Test
    public void thirdTest() throws IOException, ParseException {
        Extraction extraction = new Extraction(Objects.requireNonNull(TransformingTest.class.getClassLoader().getResource("etl/transforming/transform_data.csv")).getPath());
        Transforming transforming = new Transforming(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "/TEST");
        transforming.transformData(extraction.getCsvRecordList());
        extraction = new Extraction(Objects.requireNonNull(TransformingTest.class.getClassLoader().getResource("etl/transforming/transform_data_1.csv")).getPath());
        transforming.transformData(extraction.getCsvRecordList());
        Assertions.assertDoesNotThrow(() -> new Scanner(new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "/TEST/load_data_0.csv")) );
        Assertions.assertDoesNotThrow(() -> new Scanner(new File(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "/TEST/load_data_1.csv")) );
    }
}
