package it.uniba.ventricellisardone.itss.csv;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticTestModel {

    protected static Map<String, Map<String, Integer>> getTestMap() {
        Map<String, Map<String, Integer>> map = new HashMap<>();
        Map<String, Integer> innerMap = new HashMap<>();
        innerMap.put("Celeste", 1);
        innerMap.put("Blu", 2);
        innerMap.put("Bianco", 2);
        innerMap.put("Verde muschio", 1);
        innerMap.put("Bluette", 1);
        innerMap.put("Panna", 1);
        innerMap.put("Blu scuro", 2);
        innerMap.put("Verde acido", 1);
        innerMap.put("Testa di moro", 2);
        innerMap.put("Beige", 1);
        innerMap.put("Azzurro", 1);
        innerMap.put("Blu notte", 1);
        innerMap.put("Blu medio", 4);
        map.put("Colore", innerMap);
        innerMap = new HashMap<>();
        innerMap.put("IT", 11);
        innerMap.put("FR", 2);
        innerMap.put("GB", 2);
        innerMap.put("RU", 1);
        innerMap.put("DE", 2);
        innerMap.put("ES", 1);
        innerMap.put("LU", 1);
        map.put("CodStatoFattura", innerMap);
        innerMap = new HashMap<>();
        innerMap.put("FRECCE TRICOLORI", 2);
        innerMap.put("AERONAUTICA MILITARE", 8);
        innerMap.put("FIX DESIGN", 3);
        innerMap.put("FAY", 3);
        innerMap.put("DIADORA", 1);
        innerMap.put("HOGAN", 2);
        innerMap.put("SANTONI", 1);
        map.put("NomeBrand", innerMap);
        innerMap = new HashMap<>();
        innerMap.put("Primavera - Estate 2011", 5);
        innerMap.put("Autunno - Inverno 2011", 5);
        innerMap.put("Primavera - Estate 2012", 5);
        innerMap.put("Autunno - Inverno 2012", 5);
        map.put("Collezione", innerMap);
        innerMap = new HashMap<>();
        innerMap.put("PayPal", 6);
        innerMap.put("Contrassegno", 5);
        innerMap.put("Carta di Credito", 9);
        map.put("PagamentoOrdine", innerMap);
        innerMap = new HashMap<>();
        innerMap.put("Orologi", 1);
        innerMap.put("Maglieria", 10);
        innerMap.put("Shopper", 4);
        innerMap.put("Sneakers", 1);
        innerMap.put("Capispalla", 2);
        innerMap.put("Polacco", 1);
        innerMap.put("Cappelli", 1);
        map.put("NomeCategoria", innerMap);
        innerMap = new HashMap<>();
        innerMap.put("Gioielli Moda", 1);
        innerMap.put("Abbigliamento", 12);
        innerMap.put("Borse", 4);
        innerMap.put("Calzature", 2);
        innerMap.put("Accessori", 1);
        map.put("MacroCategoria", innerMap);
        return map;
    }

    protected static List<CSVRecord> getTestList() {
        List<CSVRecord> list = new ArrayList<>();
        try {
            String[] strings = {"11", "07/09/10", "IT", "M", "1", "89", "0", "0", "FRECCE TRICOLORI", "Primavera - Estate 2011", "Celeste", "Uomo", "PayPal", "UNICA", "Orologi", "Gioielli Moda"};
            CSVRecord csvRecord = new CSVRecord(strings);
            list.add(csvRecord);
            strings = new String[]{"11", "07/09/10", "IT", "M", "1", "62", "50", "1", "AERONAUTICA MILITARE", "Primavera - Estate 2011", "Blu medio", "Uomo", "PayPal", "L", "Maglieria", "Abbigliamento"};
            csvRecord = new CSVRecord(strings);
            list.add(csvRecord);
            strings = new String[]{"15", "07/09/10", "IT", "F", "1", "49", "50", "1", "AERONAUTICA MILITARE", "Primavera - Estate 2011", "Blu", "Uomo", "Contrassegno", "XL", "Maglieria", "Abbigliamento"};
            csvRecord = new CSVRecord(strings);
            list.add(csvRecord);
            strings = new String[]{"16", "07/09/10", "IT", "M", "1", "62", "50", "1", "AERONAUTICA MILITARE", "Primavera - Estate 2011", "Blu medio", "Uomo", "PayPal", "XL", "Maglieria", "Abbigliamento"};
            csvRecord = new CSVRecord(strings);
            list.add(csvRecord);
            strings = new String[]{"16", "07/09/10", "IT", "M", "1", "62", "50", "1", "AERONAUTICA MILITARE", "Primavera - Estate 2011", "Bianco", "Uomo", "PayPal", "S", "Maglieria", "Abbigliamento"};
            csvRecord = new CSVRecord(strings);
            list.add(csvRecord);
            strings = new String[]{"46", "10/09/10", "IT", "F", "1", "60", "50", "1", "FIX DESIGN", "Autunno - Inverno 2011", "Bianco", "Donna", "Carta di Credito", "M", "Maglieria", "Abbigliamento"};
            csvRecord = new CSVRecord(strings);
            list.add(csvRecord);
            strings = new String[]{"46", "10/09/10", "IT", "F", "1", "325", "50", "0", "FAY", "Autunno - Inverno 2011", "Verde muschio", "Donna", "Carta di Credito", "UNICA", "Shopper", "Borse"};
            csvRecord = new CSVRecord(strings);
            list.add(csvRecord);
            strings = new String[]{"46", "10/09/10", "FR", "F", "1", "65", "50", "1", "FIX DESIGN", "Autunno - Inverno 2011", "Bluette", "Donna", "Carta di Credito", "L", "Maglieria", "Abbigliamento"};
            csvRecord = new CSVRecord(strings);
            list.add(csvRecord);
            strings = new String[]{"46", "10/09/10", "GB", "F", "1", "43", "50", "1", "FIX DESIGN", "Autunno - Inverno 2011", "Panna", "Donna", "Carta di Credito", "S", "Maglieria", "Abbigliamento"};
            csvRecord = new CSVRecord(strings);
            list.add(csvRecord);
            strings = new String[]{"48", "12/09/10", "RU", "M", "1", "65", "50", "1", "DIADORA", "Autunno - Inverno 2011", "Blu scuro", "Uomo", "Contrassegno", "41", "Sneakers", "Calzature"};
            csvRecord = new CSVRecord(strings);
            list.add(csvRecord);
            strings = new String[]{"50", "14/09/10", "DE", "M", "1", "275", "50", "0", "FAY", "Primavera - Estate 2012", "Verde acido", "Donna", "Carta di Credito", "UNICA", "Shopper", "Borse"};
            csvRecord = new CSVRecord(strings);
            list.add(csvRecord);
            strings = new String[]{"51", "15/09/10", "IT", "M", "1", "107", "50", "1", "AERONAUTICA MILITARE", "Primavera - Estate 2012", "Blu medio", "Uomo", "PayPal", "46", "Capispalla", "Abbigliamento"};
            csvRecord = new CSVRecord(strings);
            list.add(csvRecord);
            strings = new String[]{"67", "16/09/10", "ES", "F", "1", "195", "50", "0", "HOGAN", "Primavera - Estate 2012", "Testa di moro", "Donna", "Contrassegno", "UNICA", "Shopper", "Borse"};
            csvRecord = new CSVRecord(strings);
            list.add(csvRecord);
            strings = new String[]{"68", "16/09/10", "IT", "M", "1", "143", "50", "1", "SANTONI", "Primavera - Estate 2012", "Beige", "Uomo", "Contrassegno", "7.5", "Polacco", "Calzature"};
            csvRecord = new CSVRecord(strings);
            list.add(csvRecord);
            strings = new String[]{"72", "16/09/10", "IT", "M", "1", "13", "50", "0", "FRECCE TRICOLORI", "Primavera - Estate 2012", "Azzurro", "Uomo", "Carta di Credito", "UNICA", "Cappelli", "Accessori"};
            csvRecord = new CSVRecord(strings);
            list.add(csvRecord);
            strings = new String[]{"79", "20/09/10", "IT", "M", "1", "95", "50", "1", "AERONAUTICA MILITARE", "Autunno - Inverno 2012", "Blu notte", "Uomo", "Carta di Credito", "S", "Maglieria", "Abbigliamento"};
            csvRecord = new CSVRecord(strings);
            list.add(csvRecord);
            strings = new String[]{"82", "21/09/10", "LU", "M", "1", "77", "50", "1", "AERONAUTICA MILITARE", "Autunno - Inverno 2012", "Blu", "Uomo", "PayPal", "S", "Maglieria", "Abbigliamento"};
            csvRecord = new CSVRecord(strings);
            list.add(csvRecord);
            strings = new String[]{"88", "23/09/10", "GB", "F", "12", "195.0", "50", "0", "HOGAN", "Autunno - Inverno 2012", "Testa di moro", "Donna", "Carta di Credito", "UNICA", "Shopper", "Borse"};
            csvRecord = new CSVRecord(strings);
            list.add(csvRecord);
            strings = new String[]{"89", "23/09/10", "FR", "F", "1", "145", "50", "1", "FAY", "Autunno - Inverno 2012", "Blu scuro", "Kids Girl", "Carta di Credito", "4", "Capispalla", "Abbigliamento"};
            csvRecord = new CSVRecord(strings);
            list.add(csvRecord);
            strings = new String[]{"90", "24/09/10", "DE", "F", "1", "48", "50", "1", "AERONAUTICA MILITARE", "Autunno - Inverno 2012", "Blu medio", "Kids Boy", "Contrassegno", "6", "Maglieria", "Abbigliamento"};
            csvRecord = new CSVRecord(strings);
            list.add(csvRecord);
            return list;
        } catch (ParseException | CSVNullFieldsException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static List<String> getNullFieldsList(){
        List<String> list = new ArrayList<>();
        String string = "15;07/09/10;IT;F;1;49;50;1;AERONAUTICA MILITARE;Blu;Uomo;Contrassegno;XL;Maglieria;Abbigliamento";
        list.add(string);
        string = "46;10/09/10;GB;F;1;43;50;1;FIX DESIGN;Autunno - Inverno 2011;Donna;Carta di Credito;S;Maglieria;Abbigliamento";
        list.add(string);
        string = "68;16/09/10;IT;M;1;143;50;1;Primavera - Estate 2012;Beige;Uomo;Contrassegno;7.5;Polacco;Calzature";
        list.add(string);
        string = "90;DE;F;1;48;50;1;AERONAUTICA MILITARE;Autunno - Inverno 2012;Blu medio;Kids Boy;Contrassegno;6;Maglieria;Abbigliamento";
        list.add(string);
        return list;
    }

    protected static List<String> getParsingErrorList(){
        List<String> list = new ArrayList<>();
        String string = "ciccio;07/09/10;IT;M;1;89;0;0;FRECCE TRICOLORI;Primavera - Estate 2011;Celeste;Uomo;PayPal;UNICA;Orologi;Gioielli Moda";
        list.add(string);
        string = "11;ciccio;IT;M;1;62;50;1;AERONAUTICA MILITARE;Primavera - Estate 2011;Blu medio;Uomo;PayPal;L;Maglieria;Abbigliamento";
        list.add(string);
        string = "15;07/09/10;IT;F;ciccio;49;50;1;AERONAUTICA MILITARE;Primavera - Estate 2011;Blu;Uomo;Contrassegno;XL;Maglieria;Abbigliamento";
        list.add(string);
        string = "16;07/09/10;IT;M;1;ciccio;50;1;AERONAUTICA MILITARE;Primavera - Estate 2011;Blu medio;Uomo;PayPal;XL;Maglieria;Abbigliamento";
        list.add(string);
        string = "16;07/09/10;IT;M;1;62;ciccio;1;AERONAUTICA MILITARE;Primavera - Estate 2011;Bianco;Uomo;PayPal;S;Maglieria;Abbigliamento";
        list.add(string);
        string = "46;10/09/10;IT;F;1;60;50;ciccio;FIX DESIGN;Autunno - Inverno 2011;Bianco;Donna;Carta di Credito;M;Maglieria;Abbigliamento";
        list.add(string);
        string = "46;10/09/10;IT;U;1;325;50;0;FAY;Autunno - Inverno 2011;Verde muschio;Donna;Carta di Credito;UNICA;Shopper;Borse";
        list.add(string);
        return list;
    }

}
