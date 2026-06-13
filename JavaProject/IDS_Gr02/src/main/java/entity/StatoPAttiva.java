package entity;

public class StatoPAttiva implements StatoPrenotazione{
    @Override
    public void conferma(Prenotazione prenotazione) {
        prenotazione.setStato(StatoPrenotazioneEnum.CONFERMATA);
    }
}
