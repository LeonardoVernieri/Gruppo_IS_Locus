package entity;

import database.GestorePersistenza;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    private String tipologia;
    private int numeroPostazioni;

    @ManyToOne
    @JoinColumn(name = "sala_id")
    private SalaStudio salaStudio; //riferimento alla sala che la contiene

    @OneToMany(mappedBy = "area")
    private List<Postazione> postazione = new ArrayList<>();
    public void setSala(SalaStudio sala) {
        this.salaStudio = sala;
    }

    public Area(String tipologia, int numeroPostazioni, SalaStudio sala) {
        this.tipologia = tipologia;
        this.numeroPostazioni = numeroPostazioni;
        this.salaStudio = sala;
    }

    public Area() {

    }

    public static final GestorePersistenza gp = new GestorePersistenza();

    //getter
    public String getTipologia() { return tipologia; }
    public int getNumeroPostazioni() { return numeroPostazioni; }
    public SalaStudio getSala() { return salaStudio; }
    public long getId() { return Id; }

    //setter
    public void setNumeroPostazioni(int numeroPostazioni) {
        this.numeroPostazioni = numeroPostazioni;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

}