package entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Bibliotecario{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String codiceInterno;

    private String nome;
    private String cognome;
    private String email;
    private String password;

    @OneToMany
    private List<SalaStudio> saleStudio;

    public Bibliotecario(){

    }
}