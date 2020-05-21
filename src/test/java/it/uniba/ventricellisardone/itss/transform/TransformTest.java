package it.uniba.ventricellisardone.itss.transform;

import it.uniba.ventricellisardone.itss.csv.CSVFile;
import it.uniba.ventricellisardone.itss.csv.CSVFileTest;
import it.uniba.ventricellisardone.itss.csv.StaticTestModel;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class TransformTest {

    @Test
    public void firstTest() throws FileNotFoundException {
        CSVFile csvFile = new CSVFile(Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("data_transform_record_fonte.csv")).getPath());
        Transform transform = new Transform(csvFile.getCsvRecordList());

        List<List<Object>> list = transform.getFinalList();
            for(int i = 0; i < list.size(); i++) {
                transform.setFinalRecord(i);
                transform.writeOnFile(Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("")).getPath(), i, "test_result_transform_record.csv");
            }
        Scanner testFile = new Scanner(new File(Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("data_transform_final_record.csv")).getPath()));
        String resultPath = Objects.requireNonNull(CSVFileTest.class.getClassLoader().getResource("")).getPath() + "/results/test_result_transform_record.csv";
        Scanner resultFile = new Scanner(new File(resultPath));
        int i = 0;
        while (resultFile.hasNextLine()){
            assert (resultFile.nextLine().equals(testFile.nextLine())) : "[ERROR] Incorrect record line " + i + " not match";
            i++;
        }


    }
}
