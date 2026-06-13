package boundary;

import control.GestorePrenotazioni;
import control.Sessione;
import entity.Prenotazione;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FormCheckIn {
    private JList listaPrenotazioni;
    private JButton btnCheckIn;
    private JButton btnAnnulla;
    private JPanel contentPanel;

    private JFrame frame;
    private JFrame parent;
    private GestorePrenotazioni gestorePrenotazioni;

    public FormCheckIn(Sessione session) {

        // Inizializza i componenti
        contentPanel = new JPanel(new BorderLayout());
        listaPrenotazioni = new JList();
        btnCheckIn = new JButton("Effettua Check-In");
        btnAnnulla = new JButton("Annulla");

        contentPanel.add(new JScrollPane(listaPrenotazioni), BorderLayout.CENTER);
        contentPanel.add(btnCheckIn, BorderLayout.SOUTH);
        contentPanel.setPreferredSize(new Dimension(400, 300));
        contentPanel.add(btnAnnulla, BorderLayout.NORTH);


        this.parent = parent;
        this.gestorePrenotazioni = new GestorePrenotazioni();

        // Recupera le prenotazioni attive di oggi
        List<Prenotazione> prenotazioni = gestorePrenotazioni.cercaPrenotazioniAttive(session.getStudenteCorrente());

        // Popola la JList con le prenotazioni
        DefaultListModel<Prenotazione> model = new DefaultListModel<>();
        for (Prenotazione p : prenotazioni) {
            model.addElement(p);
        }
        listaPrenotazioni.setModel(model);

        frame = new JFrame("Check-In");
        frame.setContentPane(contentPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        btnAnnulla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        btnCheckIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Prenotazione selezionata = (Prenotazione) listaPrenotazioni.getSelectedValue();

                if (selezionata == null) {
                    JOptionPane.showMessageDialog(frame,
                            "Seleziona una prenotazione!",
                            "Attenzione",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (selezionata.isScaduta()) {
                    JOptionPane.showMessageDialog(frame,
                            "Impossibile effettuare il check-in: prenotazione scaduta!",
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int risposta = JOptionPane.showConfirmDialog(frame,
                        "Confermi il check-in?",
                        "Conferma",
                        JOptionPane.YES_NO_OPTION);

                if (risposta == JOptionPane.YES_OPTION) {
                    gestorePrenotazioni.effettuaCheckIn(selezionata);
                    JOptionPane.showMessageDialog(frame,
                            "Check-in effettuato con successo!",
                            "Successo",
                            JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                    parent.setVisible(true);
                }
            }
        });
    }
}