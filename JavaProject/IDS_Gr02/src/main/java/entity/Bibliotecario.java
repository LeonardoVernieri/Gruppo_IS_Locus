package entity;

import database.GestorePersistenza;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.*;

@Entity
public class Bibliotecario{


    @Transient
    private GestorePersistenza gestorePersistenza;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long codiceInterno;

    private String nome;
    private String cognome;
    private String email;
    private String password;


    @ManyToMany
    private Set<SalaStudio> saleStudio = new HashSet<>(); // Set evita duplicati

    public Bibliotecario() {
        gestorePersistenza = new GestorePersistenza();
    }

}