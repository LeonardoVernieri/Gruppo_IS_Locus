package main;

import boundary.FormSplash;
import boundary.ServerMailEmbedded;

import database.JpaUtil;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.swing.*;


public class Main extends JFrame {
    public static void main(String[] args) {

        JpaUtil.getInstance();

        System.out.println("Avvio di Hibernate completato.");

        ServerMailEmbedded.avvia();
        SwingUtilities.invokeLater(() -> new FormSplash());
    }


}

