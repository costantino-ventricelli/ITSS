package it.uniba.ventricellisardone.itss.transform;

import it.uniba.ventricellisardone.itss.csv.CSVFile;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;
import java.util.Scanner;

public class TransformTest {

    @Test
    public void firstTest() throws IOException, ParseException {
        CSVFile csvFile = new CSVFile(Objects.requireNonNull(TransformTest.class.getClassLoader().getResource("transform_data.csv")).getPath());
        Transform transform = new Transform(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getPath()+"/Desktop/");
        transform.transformData(csvFile.getCsvRecordList());
        Scanner transformedFile = new Scanner(new File(Objects.requireNonNull(TransformTest.class.getClassLoader().getResource("load_data_0.csv")).getPath()));
        Scanner testFile = new Scanner(new File(Objects.requireNonNull(TransformTest.class.getClassLoader().getResource("transformed_data.csv")).getPath()));
        while(transformedFile.hasNextLine()) {
            String testFileString = testFile.nextLine().trim();
            String transformedFileString = transformedFile.nextLine().trim();
            System.out.println("[INFO] " + testFileString);
            System.out.println("[INFO] " + transformedFileString);
            assert (testFileString.equals(transformedFileString)) : "LA LINEA NON COMBACIA";
        }
    }
}
