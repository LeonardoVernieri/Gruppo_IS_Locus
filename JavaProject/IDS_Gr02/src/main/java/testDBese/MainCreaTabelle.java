package testDBese;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class MainCreaTabelle {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bibliotecaSys");
        emf.close();
        System.out.println("Avvio di Hibernate completato.");
    }
}