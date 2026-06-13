package control;
import entity.GestoreBibliotecari;
import entity.GestoreStudenti;
import entity.Bibliotecario;
import entity.Studente;
import javax.swing.*;
public class GestoreAccesso {
    private static final int MAX_TENTATIVI = 3;
    private int contatore = 0;

    /**
     * Autentica un utente a partire dalle credenziali fornite.
     * Cerca prima tra gli studenti, poi tra i bibliotecari; incrementa il
     * contatore dei tentativi falliti e lo azzera in caso di successo.
     *
     * @param email    email dell'utente
     * @param password password da verificare
     * @return lo Studente o il Bibliotecario autenticato, oppure null se le
     *         credenziali sono errate o l'accesso è bloccato
     */
    public Object loginUtente(String email, String password) {
        if (contatore >= MAX_TENTATIVI)
            return null;
        GestoreStudenti gestoreStudenti = new GestoreStudenti();
        Studente studente = gestoreStudenti.cercaStudente(email);
        if (studente == null) {
            GestoreBibliotecari gestoreBibliotecari = new GestoreBibliotecari();
            Bibliotecario bibliotecario = gestoreBibliotecari.cercaBibliotecario(email);
            if (bibliotecario == null || !bibliotecario.getPassword().equals(password)) {
                contatore++;
                return null;
            }
            contatore = 0;
            return bibliotecario;
        }
        if (!studente.getPassword().equals(password)) {
            contatore++;
            return null;
        }
        contatore = 0;
        return studente;
    }

    /**
     * Indica se l'accesso è bloccato per aver raggiunto il numero massimo
     * di tentativi di login falliti.
     *
     * @return true se i tentativi hanno raggiunto MAX_TENTATIVI
     */
    public boolean isBloccato() {return contatore >= MAX_TENTATIVI;}

    /**
     * Registra un nuovo studente nel sistema. Valida i dati anagrafici,
     * verifica l'obbligatorietà della matricola e l'unicità di email e
     * matricola prima di procedere alla creazione.
     *
     * @param matricola matricola dello studente (obbligatoria)
     * @param nome      nome dello studente
     * @param cognome   cognome dello studente
     * @param email     email dello studente (deve essere univoca)
     * @param password  password dello studente
     * @return true se la registrazione va a buon fine, false altrimenti
     */
    public static boolean registraStudente(Long matricola, String nome, String cognome, String email, String password){
        if (!validaDati(nome, cognome, email, password)) {
            return false;
        }
        if (matricola == null) {
            JOptionPane.showMessageDialog(null, "Inserire matricola");
            return false;
        }
        GestoreStudenti gestoreStudenti = new GestoreStudenti();
        if (gestoreStudenti.cercaStudente(email) != null) {
            JOptionPane.showMessageDialog(null, "Email già associata ad un altro studente");
            return false;
        }
        if (gestoreStudenti.cercaPerMatricola(matricola) != null) {
            JOptionPane.showMessageDialog(null, "Matricola già registrata");
            return false;
        }
        return gestoreStudenti.creaStudente(matricola, nome, cognome, email, password);
    }

    /**
     * Registra un nuovo bibliotecario nel sistema. Valida i dati anagrafici,
     * verifica l'obbligatorietà del codice interno e l'unicità dell'email
     * prima di procedere alla creazione.
     *
     * @param codiceInterno codice identificativo del bibliotecario (obbligatorio)
     * @param nome          nome del bibliotecario
     * @param cognome       cognome del bibliotecario
     * @param email         email del bibliotecario (deve essere univoca)
     * @param password      password del bibliotecario
     * @return true se la registrazione va a buon fine, false altrimenti
     */
    public static boolean registraBibliotecario(Long codiceInterno, String nome, String cognome, String email, String password){
        if (!validaDati(nome, cognome, email, password)) {
            return false;
        }
        if (codiceInterno == null){
            JOptionPane.showMessageDialog(null, "Codice identificativo obbligatorio.");
            return false;
        }
        GestoreBibliotecari gestoreBibliotecari = new GestoreBibliotecari();
        if (gestoreBibliotecari.cercaBibliotecario(email) != null){
            JOptionPane.showMessageDialog(null, "Email già registrata.");
            return false;
        }
        return gestoreBibliotecari.creaBibliotecario(codiceInterno, nome, cognome, email, password);
    }

    /**
     * Registra un utente generico delegando al metodo appropriato in base
     * al ruolo specificato (studente o bibliotecario).
     *
     * @param ruolo    ruolo dell'utente ("STUDENTE", altrimenti bibliotecario)
     * @param nome     nome dell'utente
     * @param cognome  cognome dell'utente
     * @param email    email dell'utente
     * @param password password dell'utente
     * @param extra    matricola (studente) o codice interno (bibliotecario)
     * @throws Exception in caso di errore durante la registrazione
     */
    public void registraUtente(String ruolo, String nome, String cognome, String email, String password, Long extra)
            throws Exception
    {
        if ("STUDENTE".equals(ruolo)) {registraStudente(extra, nome, cognome, email, password);}
        else {registraBibliotecario(extra, nome, cognome, email, password);}
    }

    /**
     * Valida i dati anagrafici comuni a studenti e bibliotecari, verificando
     * che nome e cognome non siano vuoti e che l'email contenga il carattere "@".
     *
     * @param nome     nome da validare
     * @param cognome  cognome da validare
     * @param email    email da validare
     * @param password password (presente per uniformità di firma, non validata)
     * @return true se i dati sono validi, false altrimenti
     */
    public static boolean validaDati(String nome, String cognome, String email, String password) {
        if (nome == null || nome.isBlank()) {
            JOptionPane.showMessageDialog(null, "Nome obbligatorio");
            return false;
        }
        if (cognome == null || cognome.isBlank()) {
            JOptionPane.showMessageDialog(null, "Cognome obbligatorio");
            return false;
        }
        if (email == null || !email.contains("@")) {
            JOptionPane.showMessageDialog(null, "Email non valida");
            return false;
        }
        return true;
    }
}