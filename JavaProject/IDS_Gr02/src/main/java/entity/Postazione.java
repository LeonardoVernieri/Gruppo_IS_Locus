package entity;

import database.GestorePersistenza;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Postazione {

    @Transient
    GestorePersistenza gestorePersistenza;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Associazione con postazione
    @OneToMany(mappedBy = "postazione")
    private List<Prenotazione> prenotazioni = new ArrayList<>();

    // Associazione con sala studio
    @ManyToOne
    @JoinColumn(name = "salaStudio_id")
    private SalaStudio salaStudio;

    public Postazione(){
        gestorePersistenza = new GestorePersistenza();
    }

    public Postazione(SalaStudio salaStudio){
        this.salaStudio = salaStudio;
    }

    public List<Prenotazione> getPrenotazioni() {
        return(gestorePersistenza.cercaPerCampo(Prenotazione.class, "postazione", this));
    }

    public boolean isDisponibile(FasciaOraria fascia) {
        for (Prenotazione p : getPrenotazioni()) {
            if ((p.isAttiva() || p.isConfermata()) && p.overlaps(fascia)) {
                return true;
            }
        }
        return false;
    }
}
