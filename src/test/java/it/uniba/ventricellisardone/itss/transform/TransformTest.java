package it.uniba.ventricellisardone.itss.transform;

import it.uniba.ventricellisardone.itss.csv.CSVFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;
import java.util.Scanner;

public class TransformTest {

    @Test
    public void firstTest() throws IOException, ParseException {
        CSVFile csvFile = new CSVFile(Objects.requireNonNull(TransformTest.class.getClassLoader().getResource("transform_data.csv")).getPath());
        Transform transform = new Transform("/Users/costantinoventricelli/Desktop/TEST/");
        transform.transformData(csvFile.getCsvRecordList());
        Scanner transformedFile = new Scanner(new File("/Users/costantinoventricelli/Desktop/TEST/load_data_0.csv"));
        Scanner testFile = new Scanner(new File(Objects.requireNonNull(TransformTest.class.getClassLoader().getResource("transformed_data.csv")).getPath()));
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
        CSVFile csvFile = new CSVFile(Objects.requireNonNull(TransformTest.class.getClassLoader().getResource("transform_data_error.csv")).getPath());
        Transform transform = new Transform("/Users/costantinoventricelli/Desktop/TEST");
        Assertions.assertThrows(ParseException.class, () -> transform.transformData(csvFile.getCsvRecordList()), "Eccezione non sollevata");
    }
}
