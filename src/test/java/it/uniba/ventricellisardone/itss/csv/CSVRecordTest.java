package it.uniba.ventricellisardone.itss.csv;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CSVRecordTest {

    @Test
    public void firstTestConstructor() throws ParseException, CSVNullFieldsException, CSVParsingException {
        String[] strings = {"11", "07/09/10", "IT", "M", "1", "89", "0", "0","FRECCE TRICOLORI", "Primavera - Estate 2011",
                "Celeste", "Uomo", "PayPal", "UNICA", "Orologi", "Gioielli Moda"};
        CSVRecord csvRecord = new CSVRecord(strings);
        assert (csvRecord.getIdOrdine() == 11);
        assert (csvRecord.getDataOrdine().equals(new SimpleDateFormat("dd/MM/yy",
                Locale.getDefault()).parse("07/09/10")));
        assert (csvRecord.getCodiceStatoFattura().equals("IT"));
        assert (csvRecord.getSessoAcquirente() == 'M');
        assert (csvRecord.getQuantita() == 1);
        assert (csvRecord.getPrezzoPagato() == 89);
        assert (csvRecord.getSconto() == 0);
        assert (!csvRecord.isOutlet());
        assert (csvRecord.getNomeBrand().equals("FRECCE TRICOLORI"));
        assert (csvRecord.getCollezione().equals("Primavera - Estate 2011"));
        assert (csvRecord.getColore().equals("Celeste"));
        assert (csvRecord.getSessoArticolo().equals("Uomo"));
        assert (csvRecord.getPagamentoOrdine().equals("PayPal"));
        assert (csvRecord.getTaglia().equals("UNICA"));
        assert (csvRecord.getCategoria().equals("Orologi"));
        assert (csvRecord.getMacroCategoria().equals("Gioielli Moda"));
    }

    @Test
    public void secondTestConstructor(){
        String[] strings = {"ciccio", "07/09/10", "IT", "M", "1", "89", "0", "0","FRECCE TRICOLORI", "Primavera - Estate 2011",
                "Celeste", "Uomo", "PayPal", "UNICA", "Orologi", "Gioielli Moda"};
        Assertions.assertThrows(NumberFormatException.class, () -> new CSVRecord(strings));
    }

    @Test
    public void thirdTestConstructor(){
        String[] strings = {"11", "ciccio", "IT", "M", "1", "89", "0", "0","FRECCE TRICOLORI", "Primavera - Estate 2011",
                "Celeste", "Uomo", "PayPal", "UNICA", "Orologi", "Gioielli Moda"};
        Assertions.assertThrows(ParseException.class, () -> new CSVRecord(strings));
    }

    @Test
    public void fourthTestConstructor(){
        String[] strings = {"11", "07/09/10", "IT", "G", "1", "89", "0", "0","FRECCE TRICOLORI", "Primavera - Estate 2011",
                "Celeste", "Uomo", "PayPal", "UNICA", "Orologi", "Gioielli Moda"};
        Assertions.assertThrows(CSVParsingException.class, () -> new CSVRecord(strings));
    }

    @Test
    public void fifthTestConstructor(){
        String[] strings = {"11", "07/09/10", "IT", "M", "1", "ciccio", "0", "0","FRECCE TRICOLORI", "Primavera - Estate 2011",
                "Celeste", "Uomo", "PayPal", "UNICA", "Orologi", "Gioielli Moda"};
        Assertions.assertThrows(NumberFormatException.class, () -> new  CSVRecord(strings));
    }

    @Test
    public void sixthTestConstructor(){
        String[] strings = {"11", "07/09/10", "IT", "M", "1", "-89", "0", "0","FRECCE TRICOLORI", "Primavera - Estate 2011",
                "Celeste", "Uomo", "PayPal", "UNICA", "Orologi", "Gioielli Moda"};
        Assertions.assertThrows(IllegalArgumentException.class, () -> new CSVRecord(strings));
    }

    @Test
    public void seventhTestConstructor(){
        String[] strings = {"11", "07/09/10", "IT", "M", "1", "89", "0", "ciccio","FRECCE TRICOLORI", "Primavera - Estate 2011",
                "Celeste", "Uomo", "PayPal", "UNICA", "Orologi"};
        Assertions.assertThrows(CSVNullFieldsException.class, () -> new CSVRecord(strings));
    }
}
