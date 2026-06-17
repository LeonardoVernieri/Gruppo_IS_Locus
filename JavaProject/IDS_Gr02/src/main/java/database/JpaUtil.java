package database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JpaUtil {

    private static JpaUtil instance;
    private EntityManagerFactory emf;

    private JpaUtil() {
        // Carica database.properties dal classpath
        Properties fileProps = new Properties();
        try (InputStream is = JpaUtil.class
                .getClassLoader()
                .getResourceAsStream("database.properties")) {

            if (is == null) {
                throw new RuntimeException(
                        "database.properties non trovato nel classpath. " +
                                "Configura le tue credenziali in database.properties"
                );
            }

            fileProps.load(is);

        } catch (IOException e) {
            throw new RuntimeException("Errore nel caricamento di database.properties", e);
        }

        // Sovrascrive solo user e password (url e driver restano nel persistence.xml)
        Map<String, Object> overrides = new HashMap<>();
        overrides.put("jakarta.persistence.jdbc.driver",   fileProps.getProperty("db.driver"));
        overrides.put("jakarta.persistence.jdbc.url",      fileProps.getProperty("db.url"));
        overrides.put("jakarta.persistence.jdbc.user",     fileProps.getProperty("db.username"));
        overrides.put("jakarta.persistence.jdbc.password", fileProps.getProperty("db.password"));

        emf = Persistence.createEntityManagerFactory("biblioteca_db", overrides);
    }

    public static JpaUtil getInstance() {
        if (instance == null) {
            instance = new JpaUtil();
        }
        return instance;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void chiudi() {
        emf.close();
    }
}