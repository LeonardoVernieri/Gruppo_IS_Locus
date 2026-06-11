package control;

import entity.Bibliotecario;
import dto.FasciaOraria;
import entity.CatalogoSalaStudio;
import entity.SalaStudio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GestoreSaleStudio {

    public Map<FasciaOraria, Integer> getFascieOrarieDisponibili(String nomeSala, LocalDate data) {
        CatalogoSalaStudio catalogoSala = new CatalogoSalaStudio();
        return catalogoSala.getDisponibilitaFasciaOrariaSalaPerData(nomeSala, data);
    }

    public List<String> getNomiSale() {
        CatalogoSalaStudio catalogoSala = new CatalogoSalaStudio();

        List<String> nomiSale = new ArrayList<>();

        for (SalaStudio s : catalogoSala.getSale()) {
            nomiSale.add(s.getNome());
        }

        return nomiSale;
    }

    public int getNumPostazioni(FasciaOraria fascia, LocalDate date, String nomeSala) {
        CatalogoSalaStudio catalogoSala = new CatalogoSalaStudio();
        return catalogoSala.getPostazioniLibere(fascia, date, nomeSala);
    }
}
