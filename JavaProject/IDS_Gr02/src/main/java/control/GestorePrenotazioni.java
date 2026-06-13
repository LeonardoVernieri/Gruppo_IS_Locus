package control;

import database.GestorePersistenza;
import entity.CatalogoPrenotazioni;
import entity.Prenotazione;
import entity.StatoPrenotazioneEnum;
import entity.Studente;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public  class GestorePrenotazioni {


    public List <Prenotazione> cercaPrenotazioniAttive(Studente studente) {

        CatalogoPrenotazioni catalogoPrenotazioni = new CatalogoPrenotazioni();
        return catalogoPrenotazioni.getPrenotazioniAttiveOggi(studente);

    }






    public void effettuaCheckIn(Prenotazione prenotazione) {
        CatalogoPrenotazioni catalogoPrenotazioni = new CatalogoPrenotazioni();

        prenotazione.conferma();
        catalogoPrenotazioni.Aggiorna(prenotazione);
    }

}
