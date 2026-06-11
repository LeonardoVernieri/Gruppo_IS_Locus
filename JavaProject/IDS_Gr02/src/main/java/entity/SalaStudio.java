package entity;

import database.GestorePersistenza;
import dto.FasciaOraria;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Entity
public class SalaStudio{

    @Transient
    private GestorePersistenza gestorePersistenza;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;
    private String descrizione;
    private int numeroPostazioni;
    private LocalTime orarioApertura;
    private LocalTime orarioChiusura;

    // Associazione con bibliotecari
    @ManyToMany
    private Set<Bibliotecario> bibliotecari = new HashSet<>(); // Set evita duplicati

    @OneToMany(mappedBy = "salaStudio")
    private List<Postazione> postazioni = new ArrayList<>();

    @PostLoad
    public void init(){
        gestorePersistenza = new GestorePersistenza();
    }

    public SalaStudio(){}

    public SalaStudio(String nome, String descrizione, int numeroPostazioni, LocalTime orarioApertura, LocalTime orarioChiusura) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.numeroPostazioni = numeroPostazioni;
        this.orarioApertura = orarioApertura;
        this.orarioChiusura = orarioChiusura;
        init();
    }

    public String getNome() {
        return nome;
    }

    public List<Postazione> getPostazioni() {
        return gestorePersistenza.cercaPerCampo(Postazione.class, "salaStudio", this);
    }

    public List<FasciaOraria>  getFasceOrarie() {
        List<FasciaOraria> fasce = new ArrayList<>();
        LocalTime corrente = this.orarioApertura;

        while (corrente.isBefore(this.orarioChiusura)) {
            fasce.add(new FasciaOraria(corrente, corrente.plusHours(1)));
            corrente = corrente.plusHours(1);
        }
        return fasce;
    }

    // Ritorna il numero di postiLiberi per una data e una fascia oraria
    public int getPostiLiberi(FasciaOraria f, LocalDate data) {
        int count = 0;
        for (Postazione p : getPostazioni()) {
            if (p.isDisponibile(f, data)) {
                count++;
            }
        }
        return count;
    }
}
