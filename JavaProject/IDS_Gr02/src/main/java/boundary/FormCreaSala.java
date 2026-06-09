package boundary;
import control.BibliotecarioStub;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormCreaSala extends JFrame{
    private JLabel lblTitoloCrea;
    private JTextField nomeSala;
    private JTextField numeroPersone;
    private JComboBox tipologiaSala;
    private JLabel lblNome;
    private JLabel lblNum;
    private JLabel lblTipo;
    private JButton btnSalva;
    private JPanel contentPane;
    private JButton btnAnnulla;

    private FormBibliotecario parent;

    public void StampaDati(BibliotecarioStub b){
        String nSala = nomeSala.getText();
        String nPersone = (String)numeroPersone.getText();
        String tipo = (String) tipologiaSala.getSelectedItem();

        System.out.println("Nome: " + nSala);
        System.out.println("Numero persone: " + nPersone);
        System.out.println("Tipo: " + tipo);
    }

    public FormCreaSala(FormBibliotecario parent, BibliotecarioStub bib) {
        this.parent = parent;

        setTitle("Crea Sala");
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);

        btnSalva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StampaDati(bib);
            }
        });

        btnAnnulla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.setVisible(true);
                dispose();
            }
        });
    }
}
