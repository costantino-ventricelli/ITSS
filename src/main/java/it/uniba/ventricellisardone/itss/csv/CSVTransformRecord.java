package it.uniba.ventricellisardone.itss.csv;

import it.uniba.ventricellisardone.itss.cloud.data.CloudData;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


    /*
        Questa classe costruisce il record completo
    */
public class CSVTransformRecord {

    private final static String TAG = "CSVTransformRecord.class";

    private final List<CSVRecord> csvRecordList;
    private final List<String> strings = new ArrayList<>();

    //Costruttore
    public CSVTransformRecord(List<CSVRecord> csvRecordList) {
            this.csvRecordList = csvRecordList;
    }

    public List<String> getBuildString() {
            return strings;
    }

    public void setRecord() {
        for (CSVRecord csvRecord : csvRecordList) {
            long idOrdine = csvRecord.getIdOrdine();
            Date dataOrdine = csvRecord.getDataOrdine();
            String codiceStatoFattura = csvRecord.getCodiceStatoFattura();
            char sessoAcquirente = csvRecord.getSessoAcquirente();
            int quantita = csvRecord.getQuantita();
            double prezzoPagato = csvRecord.getPrezzoPagato();
            int sconto = csvRecord.getSconto();
            boolean outlet = csvRecord.isOutlet();
            String nomeBrand = csvRecord.getNomeBrand();
            String collezione = csvRecord.getCollezione();
            String colore = csvRecord.getColore();
            String sessoArticolo = csvRecord.getSessoArticolo();
            String pagamentoOrdine = csvRecord.getPagamentoOrdine();
            String taglia = csvRecord.getTaglia();
            String categoria = csvRecord.getCategoria();
            String macroCategoria = csvRecord.getMacroCategoria();

            CloudData cloudData = takeDataFromCloudData(dataOrdine);

            String nomeGiorno = cloudData.getNomeGiorno();
            int numeroGiornoAnno = cloudData.getNumeroGiornoAnno();
            String nomeMese = cloudData.getNomeMese();
            int annoValore = cloudData.getAnnoValore();
            int meseValore = cloudData.getMeseValore();
            String trimestre = cloudData.getTrimestre();
            String periodo = cloudData.getPeriodo();
            String trimestreAnno = cloudData.getTrimestreAnno();
            String meseAnno = cloudData.getMeseAnno();
            String feriale = cloudData.getFeriale();
            String festivo = cloudData.getFestivo();


            strings.add(idOrdine + "," + dataOrdine + "," + nomeGiorno + "," + numeroGiornoAnno + "," + nomeMese + "," + annoValore + "," + meseValore + "," + trimestre + "," + periodo + "," + trimestreAnno + "," + meseAnno + "," + feriale + "," + festivo + "," +
                    codiceStatoFattura + "," + sessoAcquirente + "," + quantita + "," + prezzoPagato + "," + sconto + "," + outlet + "," + nomeBrand + "," + collezione + "," +
                    colore + "," + sessoArticolo + "," + pagamentoOrdine + "," + taglia + "," + categoria + "," + macroCategoria);

        }
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
}
