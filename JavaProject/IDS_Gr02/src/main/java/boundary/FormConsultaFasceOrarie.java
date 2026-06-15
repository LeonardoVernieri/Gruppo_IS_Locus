package boundary;

import control.Sessione;
import dto.FasciaOraria;
import control.GestoreSaleStudio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;

public class FormConsultaFasceOrarie extends JFrame {

    // ── Palette (protected così la sottoclasse può usarla) ────────────────────
// ── Palette ───────────────────────────────────────────────────────────────
    protected static final Color BG_PAGE        = new Color(0xF7F7F6);
    protected static final Color BG_CARD        = Color.WHITE;
    protected static final Color BORDER_LIGHT   = new Color(0xEBEBEA);
    protected static final Color BLUE_BG        = new Color(0xEEF2FF); // indaco chiaro
    protected static final Color BLUE_BORDER    = new Color(0xA5B4FC); // indaco medio
    protected static final Color BLUE_TEXT      = new Color(0x4338CA); // indaco scuro
    protected static final Color GREEN_BG       = new Color(0xECFDF5);
    protected static final Color GREEN_TEXT     = new Color(0x065F46);
    protected static final Color AMBER_BG       = new Color(0xFEF3C7);
    protected static final Color AMBER_TEXT     = new Color(0x92400E);
    protected static final Color RED_BG         = new Color(0xFEE2E2);
    protected static final Color RED_TEXT       = new Color(0x991B1B);
    protected static final Color TEXT_PRIMARY   = new Color(0x111110);
    protected static final Color TEXT_SECONDARY = new Color(0x52525B);
    protected static final Color TEXT_TERTIARY  = new Color(0xA1A1AA);
    protected static final double SOGLIA_QUASI_PIENO = 0.35;

    // ── Stato (protected per consentire l'override di buildSlotRow) ───────────
    protected final GestoreSaleStudio gestoreSaleStudio;
    protected JComboBox<String> comboSale;
    protected JPanel datePanelInner;
    protected JScrollPane dateScrollPane;
    protected JPanel fascePanelInner;
    protected JScrollPane fasceScrollPane;
    protected JLabel fasceHeaderLabel;
    protected JLabel hintLabel;
    protected JPanel panelConsultaFasceOrarie;
    protected JLabel labelTitolo;
    protected JComboBox comboArea;
    protected JPanel areaSection;

    protected final List<DateButton> dateButtons = new ArrayList<>();
    protected LocalDate dataSelezionata = null;
    protected String nomeSala = null;


    // Gestione font
    protected static Font FONT_REGULAR;
    protected static Font FONT_BOLD;

    static {
        try {
            FONT_REGULAR = Font.createFont(Font.TRUETYPE_FONT,
                            FormConsultaFasceOrarie.class.getResourceAsStream("/resources/Inter-Regular.ttf"))
                    .deriveFont(13f);
            FONT_BOLD = Font.createFont(Font.TRUETYPE_FONT,
                            FormConsultaFasceOrarie.class.getResourceAsStream("/resources/Inter-Bold.ttf"))
                    .deriveFont(Font.BOLD, 13f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(FONT_REGULAR);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(FONT_BOLD);
        } catch (Exception e) {
            // fallback sicuro se il file non si trova
            FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
            FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
        }
    }
    // ═════════════════════════════════════════════════════════════════════════
    public FormConsultaFasceOrarie() {
        gestoreSaleStudio = new GestoreSaleStudio();

        setTitle("Gestore sale studio");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(660, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        panelConsultaFasceOrarie = new JPanel();
        panelConsultaFasceOrarie.setLayout(new BoxLayout(panelConsultaFasceOrarie, BoxLayout.Y_AXIS));
        panelConsultaFasceOrarie.setBackground(BG_PAGE);
        panelConsultaFasceOrarie.setBorder(new EmptyBorder(24, 28, 24, 28));

        // Titolo
        labelTitolo = new JLabel("Consulta fasce orarie disponibili");
        labelTitolo.setFont(FONT_BOLD.deriveFont(18f));
        labelTitolo.setForeground(TEXT_PRIMARY);
        labelTitolo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelConsultaFasceOrarie.add(labelTitolo);
        panelConsultaFasceOrarie.add(Box.createVerticalStrut(20));

        // Bottone torna
        // root.add(Box.createVerticalStrut(8));
        RoundedButton btnTorna = new RoundedButton("← Torna al menu", true);
        btnTorna.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnTorna.addActionListener(e -> { new FormStudente(); dispose(); });
        panelConsultaFasceOrarie.add(btnTorna);
        panelConsultaFasceOrarie.add(Box.createVerticalStrut(20));


        // Sala
        panelConsultaFasceOrarie.add(buildSectionLabel("Sala"));
        panelConsultaFasceOrarie.add(Box.createVerticalStrut(8));
        panelConsultaFasceOrarie.add(buildComboSala());
        panelConsultaFasceOrarie.add(Box.createVerticalStrut(20));

        // Data
        panelConsultaFasceOrarie.add(buildSectionLabel("Data"));
        panelConsultaFasceOrarie.add(Box.createVerticalStrut(8));
        panelConsultaFasceOrarie.add(buildDateScrollPane());
        panelConsultaFasceOrarie.add(Box.createVerticalStrut(20));

        // Separatore
        panelConsultaFasceOrarie.add(buildDivider());
        panelConsultaFasceOrarie.add(Box.createVerticalStrut(20));

        // Aree
        panelConsultaFasceOrarie.add(buildAreaSection());
        panelConsultaFasceOrarie.add(Box.createVerticalStrut(5));


        // Fasce orarie
        fasceHeaderLabel = buildSectionLabel("Fasce orarie");
        fasceHeaderLabel.setVisible(false);
        panelConsultaFasceOrarie.add(fasceHeaderLabel);
        panelConsultaFasceOrarie.add(Box.createVerticalStrut(8));
        panelConsultaFasceOrarie.add(buildFasceScrollPane());

        // Hint iniziale
        hintLabel = new JLabel("Seleziona una sala per vedere le date disponibili.");
        hintLabel.setFont(FONT_REGULAR);
        hintLabel.setForeground(TEXT_TERTIARY);
        hintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelConsultaFasceOrarie.add(hintLabel);

        setContentPane(panelConsultaFasceOrarie);
        getContentPane().setBackground(BG_PAGE);
    }

    // ── Builder sezioni ───────────────────────────────────────────────────────

    private JComboBox<String> buildComboSala() {
        comboSale = new JComboBox<>();
        comboSale.addItem("Seleziona una sala...");
        for (String nome : gestoreSaleStudio.getNomiSale()) comboSale.addItem(nome);
        comboSale.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        comboSale.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboSale.setFont(FONT_REGULAR);
        comboSale.setBackground(BG_CARD);
        comboSale.addActionListener(e -> onSalaSelezionata());
        return comboSale;
    }

    private JScrollPane buildDateScrollPane() {
        datePanelInner = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        datePanelInner.setOpaque(false);

        dateScrollPane = new JScrollPane(datePanelInner,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        dateScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        dateScrollPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, 68));
        dateScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateScrollPane.setBorder(BorderFactory.createEmptyBorder());
        dateScrollPane.setOpaque(false);
        dateScrollPane.getViewport().setOpaque(false);
        dateScrollPane.setVisible(false);
        dateScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 3));
        return dateScrollPane;
    }

    private JPanel buildAreaSection(){

        // Costruisco sezione per la scelta dell'area
        areaSection = new JPanel();
        areaSection.setLayout(new BoxLayout(areaSection, BoxLayout.Y_AXIS));
        areaSection.setOpaque(false);
        areaSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        areaSection.setVisible(false);

        // Label per il titolo dell'area
        areaSection.add(buildSectionLabel("Area (opzionale)"));
        areaSection.add(Box.createVerticalStrut(8));

        // Creo il JComboBox
        comboArea = new JComboBox<>();
        comboArea.addItem("Nesssuna preferenza");
        comboArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        comboArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboArea.setFont(comboArea.getFont().deriveFont(13f));
        comboArea.setBackground(BG_CARD);

        comboArea.addActionListener(e ->
                {
                    if (dataSelezionata != null) aggiornaFasce();
                });

        areaSection.add(comboArea);
        areaSection.add(Box.createVerticalStrut(16));

        return areaSection;
    }

    private JScrollPane buildFasceScrollPane() {
        fascePanelInner = new JPanel();
        fascePanelInner.setLayout(new BoxLayout(fascePanelInner, BoxLayout.Y_AXIS));
        fascePanelInner.setOpaque(false);

        fasceScrollPane = new JScrollPane(fascePanelInner,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        fasceScrollPane.setBorder(BorderFactory.createEmptyBorder());
        fasceScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        fasceScrollPane.setOpaque(false);
        fasceScrollPane.getViewport().setOpaque(false);
        fasceScrollPane.setVisible(false);
        fasceScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        fasceScrollPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, 220));
        return fasceScrollPane;
    }

    // ── Logica eventi ─────────────────────────────────────────────────────────

    protected void onSalaSelezionata() {

        if (comboSale.getSelectedIndex() == 0) {
            dateScrollPane.setVisible(false);
            fasceScrollPane.setVisible(false);
            fasceHeaderLabel.setVisible(false);
            areaSection.setVisible(false);
            hintLabel.setVisible(true);
            revalidate(); repaint();
            return;
        }
        nomeSala = (String) comboSale.getSelectedItem();
        hintLabel.setVisible(false);
        buildDateButtons();
        dateScrollPane.setVisible(true);
        areaSection.setVisible(true);
        fasceScrollPane.setVisible(false);
        fasceHeaderLabel.setVisible(false);
        dataSelezionata = null;
        revalidate(); repaint();

        aggiornaComboAree();

    }

    protected void buildDateButtons() {
        datePanelInner.removeAll();
        dateButtons.clear();

        LocalDate oggi = LocalDate.now();
        for (int i = 0; i < 10; i++) {
            LocalDate data = oggi.plusDays(i);
            String giorno = data.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ITALIAN);
            DateButton btn = new DateButton(giorno, data.getDayOfMonth());

            LocalDate finalData = data;
            btn.addActionListener(e -> {
                dateButtons.forEach(b -> b.setSelected(false));
                btn.setSelected(true);
                dataSelezionata = finalData;
                aggiornaFasce();
            });

            dateButtons.add(btn);
            datePanelInner.add(btn);
        }

        datePanelInner.revalidate();
        datePanelInner.repaint();
    }

    protected void aggiornaFasce() {
        fascePanelInner.removeAll();


        // Leggo l'area selezionata (null = nessuna preferenza)
        String areaSelezionata = (comboArea != null && comboArea.getSelectedIndex() > 0)
                ? (String) comboArea.getSelectedItem()
                : null;

        Map<FasciaOraria, Integer> fasceDisponibili = new TreeMap<>(
                Comparator.comparing(FasciaOraria::getOraInizio)
        );
        fasceDisponibili.putAll(gestoreSaleStudio.getNumPostazioniDisponibili(nomeSala, dataSelezionata, areaSelezionata));

        // totale postazioni: dell'area se selezionata, altrimenti della sala
        int totale = (areaSelezionata != null)
                ? gestoreSaleStudio.getNumPostazioniArea(nomeSala, areaSelezionata)
                : gestoreSaleStudio.getNumPostazioniSala(nomeSala);

        if (fasceDisponibili.isEmpty()) {
            JLabel vuoto = new JLabel("Nessuna fascia oraria disponibile per questa data.");
            vuoto.setFont(vuoto.getFont().deriveFont(13f));
            vuoto.setForeground(TEXT_TERTIARY);
            vuoto.setAlignmentX(Component.LEFT_ALIGNMENT);
            fascePanelInner.add(vuoto);
        } else {
            RoundedCard card = new RoundedCard();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

            List<Map.Entry<FasciaOraria, Integer>> entries = new ArrayList<>(fasceDisponibili.entrySet());
            for (int i = 0; i < entries.size(); i++) {
                Map.Entry<FasciaOraria, Integer> entry = entries.get(i);
                card.add(buildSlotRow(entry.getKey(), entry.getValue(), totale));
                if (i < entries.size() - 1) card.add(buildInternalDivider());
            }

            fascePanelInner.add(card);
        }

        String giornoNome = dataSelezionata.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ITALIAN);
        String mese = dataSelezionata.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN);
        fasceHeaderLabel.setText("Fasce orarie — " + giornoNome + " " + dataSelezionata.getDayOfMonth() + " " + mese);
        fasceHeaderLabel.setVisible(true);
        fasceScrollPane.setVisible(true);

        fascePanelInner.revalidate();
        fascePanelInner.repaint();
        revalidate();
        repaint();
    }

    private void aggiornaComboAree() {
        if (comboArea == null) return;
        comboArea.removeAllItems();
        comboArea.addItem("Nessuna preferenza");
        List<String> aree = gestoreSaleStudio.getAreeSala(nomeSala);
        for (String area : aree) comboArea.addItem(area);
    }

    // ── Componenti UI ─────────────────────────────────────────────────────────

    /** Protected: la sottoclasse fa override per aggiungere il bottone di selezione. */
    protected JPanel buildSlotRow(FasciaOraria fascia, int liberi, int totale) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(12, 16, 12, 16));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));

        JLabel orario = new JLabel(fascia.toString());
        orario.setFont(FONT_REGULAR.deriveFont(14f));
        orario.setForeground(TEXT_PRIMARY);
        row.add(orario, BorderLayout.WEST);

        JPanel destra = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        destra.setOpaque(false);

        JLabel postiLabel = new JLabel(liberi + " / " + totale + " postazioni libere");
        postiLabel.setFont(FONT_REGULAR.deriveFont(12f));
        postiLabel.setForeground(TEXT_SECONDARY);
        destra.add(postiLabel);
        destra.add(buildBadgePill(liberi, totale));

        row.add(destra, BorderLayout.EAST);
        return row;
    }

    protected JLabel buildBadgePill(int liberi, int totale) {
        String testo;
        Color bg, fg;

        if (liberi == 0) {
            testo = "esaurito";    bg = RED_BG;   fg = RED_TEXT;
        } else if ((double) liberi / totale <= SOGLIA_QUASI_PIENO) {
            testo = "quasi pieno"; bg = AMBER_BG; fg = AMBER_TEXT;
        } else {
            testo = "disponibile"; bg = GREEN_BG; fg = GREEN_TEXT;
        }
        return new PillLabel(testo, bg, fg);
    }

    protected JSeparator buildInternalDivider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_LIGHT);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    protected JSeparator buildDivider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_LIGHT);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    protected JLabel buildSectionLabel(String testo) {
        JLabel lbl = new JLabel(testo.toUpperCase());
        lbl.setFont(FONT_REGULAR.deriveFont(10.5f));
        lbl.setForeground(TEXT_TERTIARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // Componenti custom (protected per riuso nella sottoclasse)
    // ═════════════════════════════════════════════════════════════════════════

    protected static class DateButton extends JButton {
        private boolean selected = false;
        private boolean hovered = false;
        private final String giorno;
        private final String numero;

        DateButton(String giorno, int numero) {
            super();
            this.giorno = giorno;
            this.numero = String.valueOf(numero);
            setPreferredSize(new Dimension(52, 60));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setFont(FONT_REGULAR);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    System.out.println("entered");
                    hovered = true;
                    repaint();
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    System.out.println("exited");
                    hovered = false;
                    repaint();
                }
            });
        }

        public void setSelected(boolean sel) {
            this.selected = sel;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            if (selected) {
                g2.setColor(BLUE_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(BLUE_BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 10, 10));
            } else {
                Color sfondo = hovered ? new Color(0xEEF2FF) : BG_CARD;
                Color bordo  = hovered ? new Color(0x6366F1) : BORDER_LIGHT;

                g2.setColor(sfondo);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(bordo);
                g2.setStroke(new BasicStroke(0.8f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 10, 10));
            }

            Color colTertiary = selected ? new Color(0x185FA5) : TEXT_TERTIARY;
            Color colPrimary  = selected ? BLUE_TEXT : TEXT_PRIMARY;
            int cx = getWidth() / 2;

            Font fontSmall = getFont().deriveFont(Font.PLAIN, 10.5f);
            Font fontBig   = getFont().deriveFont(Font.BOLD, 16f);
            FontMetrics fmSmall = g2.getFontMetrics(fontSmall);
            FontMetrics fmBig   = g2.getFontMetrics(fontBig);

            int totalH = fmSmall.getAscent() + 3 + fmBig.getAscent();
            int startY = (getHeight() - totalH) / 2 + fmSmall.getAscent();

            g2.setFont(fontSmall);
            g2.setColor(colTertiary);
            g2.drawString(giorno, cx - fmSmall.stringWidth(giorno) / 2, startY);

            g2.setFont(fontBig);
            g2.setColor(colPrimary);
            g2.drawString(numero, cx - fmBig.stringWidth(numero) / 2, startY + 3 + fmBig.getAscent());

            g2.dispose();
        }
    }

    protected static class RoundedCard extends JPanel {
        RoundedCard() {
            setOpaque(false);
            setBorder(new EmptyBorder(0, 0, 0, 0));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(BG_CARD);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
            g2.setColor(BORDER_LIGHT);
            g2.setStroke(new BasicStroke(0.8f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 12, 12));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    protected static class PillLabel extends JLabel {
        private final Color bg;

        PillLabel(String testo, Color bg, Color fg) {
            super(testo);
            this.bg = bg;
            setForeground(fg);
            setFont(FONT_REGULAR);
            setBorder(new EmptyBorder(3, 10, 3, 10));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    protected static class RoundedButton extends JButton {

        private boolean hovered = false;
        private final boolean filled; // ← nuovo

        RoundedButton(String testo) {
            this(testo, false); // default: stile outline
        }
        RoundedButton(String testo, boolean filled) {
            super(testo);
            this.filled = filled;
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setFont(FONT_REGULAR.deriveFont(11f));
            setBorder(new EmptyBorder(7, 16, 7, 16));
            if (filled) setForeground(Color.WHITE); // testo bianco

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    hovered = true; repaint();
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    hovered = false; repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color sfondo, bordo;
            if (filled) {
                sfondo = hovered ? new Color(0x4F46E5) : new Color(0x6366F1); // filled indaco
                bordo  = sfondo;
            } else {
                sfondo = hovered ? new Color(0xEEF2FF) : BG_CARD;             // outline originale
                bordo  = hovered ? new Color(0x6366F1) : BORDER_LIGHT;
            }

            g2.setColor(sfondo);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
            g2.setColor(bordo);
            g2.setStroke(new BasicStroke(0.8f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, getHeight(), getHeight()));
            g2.dispose();
            super.paintComponent(g);
        }
    }
}