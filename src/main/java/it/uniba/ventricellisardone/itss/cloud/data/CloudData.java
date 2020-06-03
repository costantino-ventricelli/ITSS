package it.uniba.ventricellisardone.itss.cloud.data;

import com.google.cloud.Date;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.uniba.ventricellisardone.itss.log.Log;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CloudData{

    private static final String TAG = "Cloud data";

    private final Date googleData;

    private final String nomeGiorno;
    private final int numeroGiornoAnno;
    private final String nomeMese;
    private final int annoValore;
    private final int meseValore;
    private final String trimestre;
    private final String periodo;
    private final String trimestreAnno;
    private final String meseAnno;
    private final String feriale;
    private final String festivo;
    private final String dataString;

    public CloudData(java.util.Date date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.dataString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
        this.googleData = Date.fromJavaUtilDate(date);
        this.nomeGiorno = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()).toUpperCase();
        this.numeroGiornoAnno = calendar.get(Calendar.DAY_OF_YEAR);
        this.nomeMese = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()).toUpperCase();
        this.annoValore = calendar.get(Calendar.YEAR);
        //calendar numera i mesi da 0, aggiungo 1 per evitare ambiguità
        this.meseValore = calendar.get(Calendar.MONTH) + 1;
        this.trimestre = setTrimestre();
        this.periodo = setPeriodo(date);
        this.trimestreAnno = setTrimestre() + "-" + this.annoValore;
        this.meseAnno = this.meseValore + "-" + this.annoValore;
        this.feriale = setFeriale();
        this.festivo = setFestivo();
    }

    public String getDataString() {
        return dataString;
    }

    public Date getGoogleData() {
        return googleData;
    }

    public String getNomeGiorno() {
        return nomeGiorno;
    }

    public int getNumeroGiornoAnno() {
        return numeroGiornoAnno;
    }

    public String getNomeMese() {
        return nomeMese;
    }

    public int getAnnoValore() {
        return annoValore;
    }

    public int getMeseValore() {
        return meseValore;
    }

    public String getTrimestre() {
        return trimestre;
    }

    public String getPeriodo() {
        return periodo;
    }

    public String getTrimestreAnno() {
        return trimestreAnno;
    }

    public String getMeseAnno() {
        return meseAnno;
    }

    public String getFeriale() {
        return feriale;
    }

    public String getFestivo() {
        return festivo;
    }

    private String setTrimestre() {
        if (this.meseValore >= 1 && this.meseValore <= 3)
            return "T1";
        else if (this.meseValore >= 4 && this.meseValore <= 6)
            return "T2";
        else if (this.meseValore >= 7 && this.meseValore <= 9)
            return "T3";
        else
            return "T4";
    }

    private String setPeriodo(java.util.Date date) {
        String stringResponse = "NESSUNO";
        try {
            System.out.println("Richiesta api");
            CloseableHttpClient client = HttpClients.custom().build();
            HttpUriRequest request = RequestBuilder.get()
                    .setUri("https://public-holiday.p.rapidapi.com/" + this.annoValore + "/IT")
                    .addHeader("x-rapidapi-host", "public-holiday.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "2ffe94d15fmsh77f096ee6ae83e2p1e6163jsn8d8d6c79a2c2")
                    .build();
            CloseableHttpResponse response = client.execute(request);
            Reader reader = new InputStreamReader(response.getEntity().getContent());
            stringResponse = CharStreams.toString(reader);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<HolidaysDate>>() {}.getType();
            List<HolidaysDate> holidaysDateList = gson.fromJson(stringResponse, listType);
            stringResponse = "NESSUNO";
            for (HolidaysDate holidaysDate : holidaysDateList) {
                Log.i(TAG, "Holiday: " + holidaysDate.getDate().toString() + ": " + holidaysDate.getLocalName());
                if (holidaysDate.getDate().equals(date))
                    stringResponse = holidaysDate.getLocalName().toUpperCase();
            }
            response.close();
            request.abort();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringResponse;
    }

    private String setFeriale() {
        if (this.periodo.equals("NESSUNO") && !this.nomeGiorno.equals("DOMENICA"))
            return "FERIALE";
        else
            return "NON FERIALE";
    }

    private String setFestivo() {
        if (this.periodo.equals("NESSUNO"))
            return "NON FESTIVO";
        else
            return "FESTIVO";
    }

    private static class HolidaysDate {
        private java.util.Date date;
        private String localName;

        public HolidaysDate() {
        }

        public java.util.Date getDate() {
            return date;
        }

        public String getLocalName() {
            return localName;
        }
    }
}
