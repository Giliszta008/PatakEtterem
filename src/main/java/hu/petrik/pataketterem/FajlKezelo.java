package hu.petrik.pataketterem;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FajlKezelo {

    public static List<Etel> fajlBeolvas(File fajl){
        List<Etel> etelLista = new ArrayList<>();
        try {
            FileReader fr = new FileReader(fajl.toString());
            BufferedReader br = new BufferedReader(fr);

            br.readLine();

            String sor = br.readLine();
            while (sor != null){
                etelLista.add(new Etel(sor));

                sor = br.readLine();
            }

            br.close();
            fr.close();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }catch (IOException e){
            System.err.println(e.getMessage());
        }

        return etelLista;
    }

    public static boolean FajlBaIr(List<Etel> etelLista){
        try {
            FileWriter fw = new FileWriter("rendeles.csv", Charset.forName("UTF-8"));
            PrintWriter pw = new PrintWriter(fw);

            pw.println("azonosito;nev;kategoria_id;egysegar;eladott_mennyiseg");
            for (Etel e: etelLista) {
                pw.println(e);
            }

            pw.close();
            fw.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

}
