package it.uniba.ventricellisardone.itss.cloud.data;

import com.google.cloud.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

public class CloudDataTest {

    @Test
    public void festivo() throws ParseException {
        CloudData cloudData = new CloudData("01/05/20");
        assert (cloudData.getDataString().equals("2020-05-01")) : "Errore data string: " + cloudData.getDataString();
        assert (cloudData.getGoogleData().equals(
                Date.fromYearMonthDay(2020, 5, 1))) : "Data errata: " + cloudData.getGoogleData().toString();
        assert (cloudData.getNomeGiorno().equals("VENERDÌ")) : "Errore nome giorno: " + cloudData.getNomeGiorno();
        assert (cloudData.getNumeroGiornoAnno() == 122) : "Errato giorno dell'anno: " + cloudData.getNumeroGiornoAnno();
        assert (cloudData.getNomeMese().equals("MAGGIO")) : "Errato nome mese: " + cloudData.getNomeMese();
        assert (cloudData.getAnnoValore() == 2020) : "Errato anno: " + cloudData.getAnnoValore();
        assert (cloudData.getMeseValore() == 5) : "Errato mese valore: " + cloudData.getMeseValore();
        assert (cloudData.getTrimestre().equals("T2")) : "Errato trimestre: " + cloudData.getTrimestre();
        assert (cloudData.getPeriodo().equals("FESTA DEL LAVORO")) : "Errato periodo: " + cloudData.getPeriodo();
        assert (cloudData.getTrimestreAnno().equals("T2-2020")) : "Errato trimestre anno: " + cloudData.getTrimestreAnno();
        assert (cloudData.getMeseAnno().equals("5-2020")) : "Errato mese-anno: " + cloudData.getMeseAnno();
        assert (cloudData.getFeriale().equals("NON FERIALE")) : "Errato feriale: " + cloudData.getFeriale();
        assert (cloudData.getFestivo().equals("FESTIVO")) : "Errato festivo: " + cloudData.getFestivo();
    }

    @Test
    public void nonFestivo() throws ParseException{
        CloudData cloudData = new CloudData("02/05/20");
        assert (cloudData.getDataString().equals("2020-05-02")) : "Errore data string: " + cloudData.getDataString();
        assert (cloudData.getGoogleData().equals(
                Date.fromYearMonthDay(2020, 5, 2))) : "Data errata: " + cloudData.getGoogleData().toString();
        assert (cloudData.getNomeGiorno().equals("SABATO")) : "Errore nome giorno: " + cloudData.getNomeGiorno();
        assert (cloudData.getNumeroGiornoAnno() == 123) : "Errato giorno dell'anno: " + cloudData.getNumeroGiornoAnno();
        assert (cloudData.getNomeMese().equals("MAGGIO")) : "Errato nome mese: " + cloudData.getNomeMese();
        assert (cloudData.getAnnoValore() == 2020) : "Errato anno: " + cloudData.getAnnoValore();
        assert (cloudData.getMeseValore() == 5) : "Errato mese valore: " + cloudData.getMeseValore();
        assert (cloudData.getTrimestre().equals("T2")) : "Errato trimestre: " + cloudData.getTrimestre();
        assert (cloudData.getPeriodo().equals("NESSUNO")) : "Errato periodo: " + cloudData.getPeriodo();
        assert (cloudData.getTrimestreAnno().equals("T2-2020")) : "Errato trimestre anno: " + cloudData.getTrimestreAnno();
        assert (cloudData.getMeseAnno().equals("5-2020")) : "Errato mese-anno: " + cloudData.getMeseAnno();
        assert (cloudData.getFeriale().equals("FERIALE")) : "Errato feriale: " + cloudData.getFeriale();
        assert (cloudData.getFestivo().equals("NON FESTIVO")) : "Errato festivo: " + cloudData.getFestivo();
    }

    @Test
    public void domenica() throws ParseException{
        CloudData cloudData = new CloudData("03/05/20");
        assert (cloudData.getDataString().equals("2020-05-03")) : "Errore data string: " + cloudData.getDataString();
        assert (cloudData.getGoogleData().equals(
                Date.fromYearMonthDay(2020, 5, 3))) : "Data errata: " + cloudData.getGoogleData().toString();
        assert (cloudData.getNomeGiorno().equals("DOMENICA")) : "Errore nome giorno: " + cloudData.getNomeGiorno();
        assert (cloudData.getNumeroGiornoAnno() == 124) : "Errato giorno dell'anno: " + cloudData.getNumeroGiornoAnno();
        assert (cloudData.getNomeMese().equals("MAGGIO")) : "Errato nome mese: " + cloudData.getNomeMese();
        assert (cloudData.getAnnoValore() == 2020) : "Errato anno: " + cloudData.getAnnoValore();
        assert (cloudData.getMeseValore() == 5) : "Errato mese valore: " + cloudData.getMeseValore();
        assert (cloudData.getTrimestre().equals("T2")) : "Errato trimestre: " + cloudData.getTrimestre();
        assert (cloudData.getPeriodo().equals("NESSUNO")) : "Errato periodo: " + cloudData.getPeriodo();
        assert (cloudData.getTrimestreAnno().equals("T2-2020")) : "Errato trimestre anno: " + cloudData.getTrimestreAnno();
        assert (cloudData.getMeseAnno().equals("5-2020")) : "Errato mese-anno: " + cloudData.getMeseAnno();
        assert (cloudData.getFeriale().equals("NON FERIALE")) : "Errato feriale: " + cloudData.getFeriale();
        assert (cloudData.getFestivo().equals("NON FESTIVO")) : "Errato festivo: " + cloudData.getFestivo();
    }

    @Test
    public void wrongConstructorTest(){
        Assertions.assertThrows(ParseException.class, () -> new CloudData(" ciccio/05/20"));
    }
}
