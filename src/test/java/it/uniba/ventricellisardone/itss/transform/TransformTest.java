package it.uniba.ventricellisardone.itss.transform;

import it.uniba.ventricellisardone.itss.csv.CSVFile;
import it.uniba.ventricellisardone.itss.log.Log;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Objects;
import java.util.Scanner;

public class TransformTest {

    @Test
    public void firstTest() throws IOException, ParseException {
        CSVFile csvFile = new CSVFile(Objects.requireNonNull(TransformTest.class.getClassLoader().getResource("transform_data.csv")).getPath());
        Transform transform = new Transform("/Users/costantinoventricelli/Desktop/2020-06-03");
        transform.transformData(csvFile.getCsvRecordList());
        Scanner transformedFile = new Scanner(new File("/Users/costantinoventricelli/Desktop/2020-06-03/load_data_0.csv"));
        Scanner testFile = new Scanner(new File(Objects.requireNonNull(TransformTest.class.getClassLoader().getResource("transformed_data.csv")).getPath()));
        int i = 0;
        while(transformedFile.hasNextLine()) {
            String lineaTest = testFile.nextLine().trim();
            String transforedLine = transformedFile.nextLine().trim();
            assert (lineaTest.equals(transforedLine)) : "Errore alla linea: " + i + lineaTest + "    " + transforedLine;
            i++;
        }
    }
}
