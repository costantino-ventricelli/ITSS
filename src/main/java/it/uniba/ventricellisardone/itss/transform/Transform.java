package it.uniba.ventricellisardone.itss.transform;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

public class Transform {
    private static final String CSV_EXTENSION = ".csv";
    private static String TAG = "Transform.class";

    private String savingPath;
    private String fileName;
    List<String> list;

    private String header = "ordine_id_carrello,ordine_data,ordine_giorno_nome,ordine_giorno_dell_anno,ordine_mese_nome,ordine_anno_valore,ordine_mese_valore,ordine_trimestre,ordine_periodo,ordine_trimestre_anno,ordine_mese_anno,ordine_feriale_non,ordine_festivo_non,ordine_codice_stato,ordine_stato_nome,ordine_sesso_acquirente,ordine_quantita,ordine_prezzo_pagato,ordine_sconto,ordine_outlet,ordine_brand,ordine_collezione,ordine_colore,ordine_sesso_articolo,ordine_metodo_pagamento,ordine_taglia,ordine_categoria,ordine_macro_categoria";

    public Transform(String savingPath, String fileName, List<String> list) {
        this.savingPath = savingPath;
        this.fileName = fileName;
        this.list = list;
    }


    public void writeOnFile() throws FileNotFoundException{
        PrintWriter writer;
        savingPath += "results";

        fileName.concat(CSV_EXTENSION);

        for(int i = 0; i < list.size(); i++) {
            if(i % 500 == 0 && i != 0) {
                fileName = setNumberFileName(i, fileName);
                for(int j = 0; j <= i; j++) {
                    writer = new PrintWriter(new FileOutputStream(savingPath + "/" + fileName, false));
                    writer.println(header);
                    writer.println(list);
                    writer.close();
                }
           } else {
                for(int j = 0; j <= i; j++) {
                    writer = new PrintWriter(new FileOutputStream(savingPath + "/" + fileName, false));
                    writer.println(header);
                    writer.println(list);
                    writer.close();
                }
           }

        }
    }


    public String setNumberFileName(int i, String fileName) {
        int j = i / 500;
        String fileName1;
        fileName1 = fileName.concat("_" + j);
        return fileName1;
    }



} //end class
