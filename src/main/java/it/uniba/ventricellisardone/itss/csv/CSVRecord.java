package it.uniba.ventricellisardone.itss.csv;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CSVRecord {

    private long idOrdine;
    private Date dataOrdine;
    private String codiceStatoFattura;
    private char sessoAcquirente;
    private int quantita;
    private double prezzoPagato;
    private int sconto;
    private boolean outlet;
    private String nomeBrand;
    private String collezione;
    private String colore;
    private String sessoArticolo;
    private String pagamentoOrdine;
    private String taglia;
    private String categoria;
    private String macroCategoria;

    public CSVRecord(String[] strings) throws ParseException, CSVNullFieldsException {
        if (strings.length == 16) {
            for (int i = 0; i < 16; i++) {
                if(strings[i].isEmpty())
                    throw new CSVParsingException("Valore non impostato correttamente", i);
            }
            init(strings);
        } else
            throw new CSVNullFieldsException("Il numero dei campi non corrisponde a quanto stabilito: " + strings.length);
    }

    private void init(String[] strings) throws ParseException {
        this.idOrdine = Long.parseLong(strings[0]);
        this.dataOrdine = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).parse(strings[1]);
        this.codiceStatoFattura = strings[2];
        if (strings[3].charAt(0) == 'M')
            this.sessoAcquirente = 'M';
        else if (strings[3].charAt(0) == 'F')
            this.sessoAcquirente = 'F';
        else
            throw new CSVParsingException("Il sesso inserito non esiste", 3);
        this.quantita = Integer.parseInt(strings[4]);
        this.prezzoPagato = Double.parseDouble(strings[5]);
        if (this.prezzoPagato < 0)
            throw new IllegalArgumentException("Il prezzo non può essere negativo");
        this.sconto = Integer.parseInt(strings[6]);
        if (strings[7].equals("0")) {
            this.outlet = false;
        } else if (strings[7].equals("1")) {
            this.outlet = true;
        } else
            throw new CSVParsingException("Il valore non è un booleano", 7);
        this.nomeBrand = strings[8];
        this.collezione = strings[9];
        this.colore = strings[10];
        this.sessoArticolo = strings[11];
        this.pagamentoOrdine = strings[12];
        this.taglia = strings[13];
        this.categoria = strings[14];
        this.macroCategoria = strings[15];
    }

    public long getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(long idOrdine) {
        this.idOrdine = idOrdine;
    }

    public Date getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(Date dataOrdine) {
        this.dataOrdine = dataOrdine;
    }

    public String getCodiceStatoFattura() {
        return codiceStatoFattura;
    }

    public void setCodiceStatoFattura(String codiceStatoFattura) {
        this.codiceStatoFattura = codiceStatoFattura;
    }

    public char getSessoAcquirente() {
        return sessoAcquirente;
    }

    public void setSessoAcquirente(char sessoAcquirente) {
        this.sessoAcquirente = sessoAcquirente;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public double getPrezzoPagato() {
        return prezzoPagato;
    }

    public void setPrezzoPagato(double prezzoPagato) {
        this.prezzoPagato = prezzoPagato;
    }

    public int getSconto() {
        return sconto;
    }

    public void setSconto(int sconto) {
        this.sconto = sconto;
    }

    public boolean isOutlet() {
        return outlet;
    }

    public void setOutlet(boolean outlet) {
        this.outlet = outlet;
    }

    public String getNomeBrand() {
        return nomeBrand;
    }

    public void setNomeBrand(String nomeBrand) {
        this.nomeBrand = nomeBrand;
    }

    public String getCollezione() {
        return collezione;
    }

    public void setCollezione(String collezione) {
        this.collezione = collezione;
    }

    public String getColore() {
        return colore;
    }

    public void setColore(String colore) {

    }

    public String getSessoArticolo() {
        return sessoArticolo;
    }

    public void setSessoArticolo(String sessoArticolo) {
        this.sessoArticolo = sessoArticolo;
    }

    public String getPagamentoOrdine() {
        return pagamentoOrdine;
    }

    public void setPagamentoOrdine(String pagamentoOrdine) {
        this.pagamentoOrdine = pagamentoOrdine;
    }

    public String getTaglia() {
        return taglia;
    }

    public void setTaglia(String taglia) {
        this.taglia = taglia;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMacroCategoria() {
        return macroCategoria;
    }

    public void setMacroCategoria(String macroCategoria) {
        this.macroCategoria = macroCategoria;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CSVRecord) {
            CSVRecord record = (CSVRecord) obj;
            String[] otherRecord = CSVRecord.reverseCSVRecord(record);
            String[] thisRecord = CSVRecord.reverseCSVRecord(this);
            boolean result = false;
            for (int i = 0; i < thisRecord.length; i++)
                result = otherRecord[i].equals(thisRecord[i]);
            return result;
        } else
            return false;
    }

    public static String[] reverseCSVRecordForAnalysis(CSVRecord record) {
        String[] strings = new String[7];
        strings[0] = record.getColore();
        strings[1] = record.getCodiceStatoFattura();
        strings[2] = record.getNomeBrand();
        strings[3] = record.getCollezione();
        strings[4] = record.getPagamentoOrdine();
        strings[5] = record.getCategoria();
        strings[6] = record.getMacroCategoria();
        return strings;
    }

    public static String[] reverseCSVRecord(CSVRecord record) {
        String[] strings = new String[16];
        strings[0] = Long.toString(record.getIdOrdine());
        strings[1] = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(record.getDataOrdine());
        strings[2] = record.getCodiceStatoFattura();
        strings[3] = String.valueOf(record.getSessoAcquirente());
        strings[4] = Integer.toString(record.getQuantita());
        strings[5] = Double.toString(record.getPrezzoPagato());
        strings[6] = Integer.toString(record.getSconto());
        strings[7] = Boolean.toString(record.isOutlet());
        strings[8] = record.getNomeBrand();
        strings[9] = record.getCollezione();
        strings[10] = record.getColore();
        strings[11] = record.getSessoArticolo();
        strings[12] = record.getPagamentoOrdine();
        strings[13] = record.getTaglia();
        strings[14] = record.getCategoria();
        strings[15] = record.getMacroCategoria();
        return strings;
    }

    @Override
    public String toString() {
        return Arrays.toString(CSVRecord.reverseCSVRecord(this)) + "\n";
    }
}
