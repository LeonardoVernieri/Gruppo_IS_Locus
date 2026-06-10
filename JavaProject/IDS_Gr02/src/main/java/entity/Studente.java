package entity;

import jakarta.persistence.*;

@Entity
public class Studente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long matricola;

    private String nome;
    private String cognome;
    private String email;
    private String password;
}
