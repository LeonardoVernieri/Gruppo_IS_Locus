package control;

import entity.*;

import java.time.LocalTime;
import java.util.List;

public class GestoreSalaStudio {

    public GestoreSalaStudio(){

    }

    public static boolean aggiungiSalaStudio(String nome,
                                             String descrizione,
                                             int numeroPostazioniTotali,
                                             LocalTime orarioApertura,
                                             LocalTime orarioChiusura,
                                             boolean presenzaAree) {

        Bibliotecario bibliotecario = new Bibliotecario();
        return bibliotecario.creaSalaStudio(nome, descrizione, numeroPostazioniTotali, orarioApertura, orarioChiusura, presenzaAree);
    }

    public static boolean aggiungiArea(List<String> str, List<Integer> num){
        Area a = new Area();
        boolean bs=false;
        boolean bn=false;
        for(String s : str){
            bs=false;
            bn=true;
            for(Integer n : num){
                a.creaArea(s, n);
            }
            bs=true;
            bn=true;
        }
        if(bs&&bn){
            return true;
        }
        else{
            return false;
        }

    }
}
