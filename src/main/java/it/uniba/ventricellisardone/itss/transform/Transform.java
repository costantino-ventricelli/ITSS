package it.uniba.ventricellisardone.itss.transform;

import it.uniba.ventricellisardone.itss.cloud.data.CloudData;
import it.uniba.ventricellisardone.itss.csv.CSVRecord;
import it.uniba.ventricellisardone.itss.log.Log;

import java.io.*;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class Transform {

    private String header = "ordine_id_carrello,ordine_data,ordine_giorno_nome,ordine_giorno_dell_anno,ordine_mese_nome,ordine_anno_valore,ordine_mese_valore,ordine_trimestre,ordine_periodo,ordine_trimestre_anno,ordine_mese_anno,ordine_feriale_non,ordine_festivo_non,ordine_codice_stato,ordine_stato_nome,ordine_sesso_acquirente,ordine_quantita,ordine_prezzo_pagato,ordine_sconto,ordine_outlet,ordine_brand,ordine_collezione,ordine_colore,ordine_sesso_articolo,ordine_metodo_pagamento,ordine_taglia,ordine_categoria,ordine_macro_categoria";

    //variabili della classe CSV Record
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

    //Variabili della classe cloud data
    private String nomeGiorno;
    private int numeroGiornoAnno;
    private String nomeMese;
    private int annoValore;
    private int meseValore;
    private String trimestre;
    private String periodo;
    private String trimestreAnno;
    private String meseAnno;
    private String feriale;
    private String festivo;

    private static final String CSV_EXTENSION = ".csv";

    private static String TAG = "Transform.class";
    private List<CSVRecord> csvRecordList;
    private List<Object> list;
    private List<List<Object>> finalList;

    public Transform(List<CSVRecord> csvRecordList) {
        this.csvRecordList = csvRecordList;
    }



    public List<List<Object>> getFinalList() {return finalList;};

    public void setFinalRecord(int i) {
        list.add(this.idOrdine = csvRecordList.get(i).getIdOrdine());
        list.add(this.dataOrdine = csvRecordList.get(i).getDataOrdine());
        list.add(this.codiceStatoFattura = csvRecordList.get(i).getCodiceStatoFattura());
        list.add(this.sessoAcquirente = csvRecordList.get(i).getSessoAcquirente());
        list.add(this.quantita = csvRecordList.get(i).getQuantita());
        list.add(this.prezzoPagato = csvRecordList.get(i).getPrezzoPagato());
        list.add(this.sconto = csvRecordList.get(i).getSconto());
        list.add(this.outlet = csvRecordList.get(i).isOutlet());
        list.add(this.nomeBrand = csvRecordList.get(i).getNomeBrand());
        list.add(this.collezione = csvRecordList.get(i).getCollezione());
        list.add(this.colore = csvRecordList.get(i).getColore());
        list.add(this.sessoArticolo = csvRecordList.get(i).getSessoArticolo());
        list.add(this.pagamentoOrdine = csvRecordList.get(i).getPagamentoOrdine());
        list.add(this.taglia = csvRecordList.get(i).getTaglia());
        list.add(this.categoria = csvRecordList.get(i).getCategoria());
        list.add(this.macroCategoria = csvRecordList.get(i).getMacroCategoria());

        CloudData cloudData = takeDataFromCloudData(dataOrdine);

        list.add(this.nomeGiorno = cloudData.getNomeGiorno());
        list.add(this.numeroGiornoAnno = cloudData.getNumeroGiornoAnno());
        list.add(this.nomeMese = cloudData.getNomeMese());
        list.add(this.annoValore = cloudData.getAnnoValore());
        list.add(this.meseValore = cloudData.getMeseValore());
        list.add(this.trimestre = cloudData.getTrimestre());
        list.add(this.periodo = cloudData.getPeriodo());
        list.add(this.trimestreAnno = cloudData.getTrimestreAnno());
        list.add(this.meseAnno = cloudData.getMeseAnno());
        list.add(this.feriale = cloudData.getFeriale());
        list.add(this.festivo = cloudData.getFestivo());

        finalList.add(list);

    }

    public void writeOnFile(String savingPath, int i, String fileName) throws FileNotFoundException{
        PrintWriter writer;
        fileName = setNumberFileName(i, fileName);
        fileName.concat(CSV_EXTENSION);
        writer = new PrintWriter(new FileOutputStream(savingPath + "/" + fileName, true));
        writer.println(header);

        writer.println(idOrdine+","+dataOrdine+","+nomeGiorno+","+numeroGiornoAnno+","+nomeMese+","+annoValore+","+meseValore+","+trimestre+","+periodo+","+trimestreAnno+","+meseAnno+","+feriale+","+festivo+","+
                codiceStatoFattura+","+sessoAcquirente+","+quantita+","+prezzoPagato+","+sconto+","+outlet+","+nomeBrand+","+collezione+","+
                colore+","+sessoArticolo+","+pagamentoOrdine+","+taglia+","+categoria+","+macroCategoria);


        writer.close();
    }

    public String setNumberFileName(int i, String fileName) {
        int j;
        if(i % 500 == 0) {
            j = i / 500;
            fileName = fileName.concat("_" + j);
        }
        return fileName;
    }

    public CloudData takeDataFromCloudData(Date date) {
        CloudData cloudData = null;
        try {
            cloudData = new CloudData(date);
        } catch (ParseException ex) {
            System.err.println(ex.getMessage());
        }
        return cloudData;
    }

} //end class
