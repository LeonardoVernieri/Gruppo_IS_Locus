package testDBese;

import boundary.FormStudente;
import database.GestorePersistenza;
import entity.Bibliotecario;
import dto.FasciaOraria;
import entity.Postazione;
import entity.SalaStudio;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalTime;

public class MainCreaTabelle {

    public static void inizializzaDb(){

        GestorePersistenza gp = new  GestorePersistenza();

        LocalTime orarioApertura = LocalTime.of(8, 0);
        LocalTime orarioChiusura = LocalTime.of(18, 0);


        SalaStudio s = new SalaStudio("Sala1", "descrizione scritta...", 10, orarioApertura, orarioChiusura);
        gp.salva(s);

        for (int i=0; i<10; i++){
            Postazione p = new Postazione(s);
            gp.salva(p);
        }
    }

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bibliotecaSys");
        emf.close();
        System.out.println("Avvio di Hibernate completato.");

        new FormStudente();

        MainCreaTabelle.inizializzaDb();
    }

    // prova

}

