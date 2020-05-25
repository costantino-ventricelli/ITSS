package it.uniba.ventricellisardone.itss.transform;

import it.uniba.ventricellisardone.itss.cloud.data.CloudData;
import it.uniba.ventricellisardone.itss.csv.CSVRecord;
import it.uniba.ventricellisardone.itss.log.Log;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
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
    private final List<CSVRecord> csvRecordList;

    private final List<List<Object>> finalList = new ArrayList<>();
    public Transform(List<CSVRecord> csvRecordList) {
        this.csvRecordList = csvRecordList;
    }



    public List<List<Object>> getFinalList() {return finalList;};

    public void setFinalRecord(int i) {
        List<Object> list = new ArrayList<>();
        this.idOrdine = csvRecordList.get(i).getIdOrdine();
        list.add(idOrdine);
        this.dataOrdine = csvRecordList.get(i).getDataOrdine();
        list.add(dataOrdine);
        this.codiceStatoFattura = csvRecordList.get(i).getCodiceStatoFattura();
        list.add(codiceStatoFattura);
        this.sessoAcquirente = csvRecordList.get(i).getSessoAcquirente();
        list.add(sessoAcquirente);
        this.quantita = csvRecordList.get(i).getQuantita();
        list.add(quantita);
        this.prezzoPagato = csvRecordList.get(i).getPrezzoPagato();
        list.add(prezzoPagato);
        this.sconto = csvRecordList.get(i).getSconto();
        list.add(sconto);
        this.outlet = csvRecordList.get(i).isOutlet();
        list.add(outlet);
        this.nomeBrand = csvRecordList.get(i).getNomeBrand();
        list.add(nomeBrand);
        this.collezione = csvRecordList.get(i).getCollezione();
        list.add(collezione);
        this.colore = csvRecordList.get(i).getColore();
        list.add(colore);
        this.sessoArticolo = csvRecordList.get(i).getSessoArticolo();
        list.add(sessoArticolo);
        this.pagamentoOrdine = csvRecordList.get(i).getPagamentoOrdine();
        list.add(pagamentoOrdine);
        this.taglia = csvRecordList.get(i).getTaglia();
        list.add(taglia);
        this.categoria = csvRecordList.get(i).getCategoria();
        list.add(categoria);
        this.macroCategoria = csvRecordList.get(i).getMacroCategoria();
        list.add(macroCategoria);

        CloudData cloudData = takeDataFromCloudData(dataOrdine);

        this.nomeGiorno = cloudData.getNomeGiorno();
        list.add(nomeGiorno);
        this.numeroGiornoAnno = cloudData.getNumeroGiornoAnno();
        list.add(numeroGiornoAnno);
        this.nomeMese = cloudData.getNomeMese();
        list.add(nomeMese);
        this.annoValore = cloudData.getAnnoValore();
        list.add(annoValore);
        this.meseValore = cloudData.getMeseValore();
        list.add(meseValore);
        this.trimestre = cloudData.getTrimestre();
        list.add(trimestre);
        this.periodo = cloudData.getPeriodo();
        list.add(periodo);
        this.trimestreAnno = cloudData.getTrimestreAnno();
        list.add(trimestreAnno);
        this.meseAnno = cloudData.getMeseAnno();
        list.add(meseAnno);
        this.feriale = cloudData.getFeriale();
        list.add(feriale);
        this.festivo = cloudData.getFestivo();
        list.add(festivo);

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
