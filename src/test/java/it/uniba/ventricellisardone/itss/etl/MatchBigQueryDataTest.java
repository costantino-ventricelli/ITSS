package it.uniba.ventricellisardone.itss.etl;

import it.uniba.ventricellisardone.itss.csv.CSVRecord;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MatchBigQueryDataTest {

    @Test
    public void initTest() throws SQLException, InterruptedException {
        List<CSVRecord> csvRecord = new Extraction(Objects.requireNonNull(MatchBigQueryDataTest.class.getClassLoader()
                .getResource("etl/matching/matching_data.csv")).getPath()).getCsvRecordList();
        MatchBigQueryData matchBigQueryData = new MatchBigQueryData(csvRecord, "test_tabella");
        assert (matchBigQueryData.getBigQueryDate().equals(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(csvRecord.get(0).getDataOrdine()))) : "Le date non coincidono";
    }

    @Test
    public void matchTest() throws SQLException, InterruptedException, ParseException {
        List<CSVRecord> csvRecord = new Extraction(Objects.requireNonNull(MatchBigQueryDataTest.class.getClassLoader()
                .getResource("etl/matching/matching_data.csv")).getPath()).getCsvRecordList();
        MatchBigQueryData matchBigQueryData = new MatchBigQueryData(csvRecord, "test_tabella");
        assert (matchBigQueryData.isMatching()) : "Le date nel cloud non coincidono con quelle estratte";
        assert (matchBigQueryData.getConflictRowNumber() == 4) : "Il conteggio delle righe non ha funzionato: " + matchBigQueryData.getConflictRowNumber();
    }

    @Test
    public void notMatchTest() throws SQLException, InterruptedException, ParseException {
        List<CSVRecord> csvRecord = new Extraction(Objects.requireNonNull(MatchBigQueryDataTest.class.getClassLoader()
                .getResource("etl/matching/not_matching_data.csv")).getPath()).getCsvRecordList();
        MatchBigQueryData matchBigQueryData = new MatchBigQueryData(csvRecord, "test_tabella");
        assert (matchBigQueryData.isMatching()) : "Le date nel cloud non coincidono con quelle estratte";
        assert (matchBigQueryData.getConflictRowNumber() == 0) : "Il conteggio delle righe non ha funzionato: " + matchBigQueryData.getConflictRowNumber();
    }
}
