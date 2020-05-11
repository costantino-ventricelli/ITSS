package it.uniba.ventricellisardone.itss.Transform;

import it.uniba.ventricellisardone.itss.cloud.data.CloudData;
import it.uniba.ventricellisardone.itss.csv.CSVRecord;
import it.uniba.ventricellisardone.itss.log.Log;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Transform {
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

    private static String TAG = "Transform.class";
    private List<CSVRecord> csvRecordList;
    private PrintWriter writer;
    private String savingPath;
    private String fileName;

    public Transform(List<CSVRecord> csvRecordList, String savingPath, String fileName) {
        this.csvRecordList = csvRecordList;
        this.savingPath = savingPath;
        this.fileName = fileName;
    }


    public void buildFinalRecord() throws FileNotFoundException {
            for (int i = 0; i < csvRecordList.size(); i++) {
                this.idOrdine = csvRecordList.get(i).getIdOrdine();
                this.dataOrdine = csvRecordList.get(i).getDataOrdine();
                this.codiceStatoFattura = csvRecordList.get(i).getCodiceStatoFattura();
                this.sessoAcquirente = csvRecordList.get(i).getSessoAcquirente();
                this.quantita = csvRecordList.get(i).getQuantita();
                this.prezzoPagato = csvRecordList.get(i).getPrezzoPagato();
                this.sconto = csvRecordList.get(i).getSconto();
                this.outlet = csvRecordList.get(i).isOutlet();
                this.nomeBrand = csvRecordList.get(i).getNomeBrand();
                this.collezione = csvRecordList.get(i).getCollezione();
                this.colore = csvRecordList.get(i).getColore();
                this.sessoArticolo = csvRecordList.get(i).getSessoArticolo();
                this.pagamentoOrdine = csvRecordList.get(i).getPagamentoOrdine();
                this.taglia = csvRecordList.get(i).getTaglia();
                this.categoria = csvRecordList.get(i).getCategoria();
                this.macroCategoria = csvRecordList.get(i).getMacroCategoria();

                CloudData cloudData = takeDataFromCloudData(dataOrdine);

                this.nomeGiorno = cloudData.getNomeGiorno();
                this.numeroGiornoAnno = cloudData.getNumeroGiornoAnno();
                this.nomeMese = cloudData.getNomeMese();
                this.annoValore = cloudData.getAnnoValore();
                this.meseValore = cloudData.getMeseValore();
                this.trimestre = cloudData.getTrimestre();
                this.periodo = cloudData.getPeriodo();
                this.trimestreAnno = cloudData.getTrimestreAnno();
                this.meseAnno = cloudData.getMeseAnno();
                this.feriale = cloudData.getFeriale();
                this.feriale = cloudData.getFeriale();

               setNumberFileName(i);
               writeOnFile();
            }
    }


    public void setNumberFileName(int i) {
        int j;
        if(i == 500 || i % 500 == 0) {
            j = i / 500;
            this.fileName.concat("_"+j);
        }
    }

    public void writeOnFile() throws FileNotFoundException{
        writer = new PrintWriter(new FileOutputStream(savingPath + "/" + fileName, false));
        writer.print(idOrdine+","+dataOrdine+","+nomeGiorno+","+numeroGiornoAnno+","+nomeMese+","+annoValore+","+meseValore+","+trimestre+","+periodo+","+trimestreAnno+","+meseAnno+","+feriale+","+festivo+","+
                codiceStatoFattura+","+sessoAcquirente+","+quantita+","+prezzoPagato+","+sconto+","+outlet+","+nomeBrand+","+collezione+","+
                colore+","+sessoArticolo+","+pagamentoOrdine+","+taglia+","+categoria+","+macroCategoria);
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
