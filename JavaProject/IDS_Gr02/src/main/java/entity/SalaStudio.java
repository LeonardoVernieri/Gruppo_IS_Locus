package entity;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class SalaStudio{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;
    private String descrizione;
    private int numeroPostazioni;
    private LocalTime orarioApertura;
    private LocalTime orarioChiusura;

    public SalaStudio(){

    }
}
