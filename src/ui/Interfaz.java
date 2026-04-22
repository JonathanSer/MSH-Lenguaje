package ui;

import interprete.Interprete;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class Interfaz extends JFrame {

    private JTextArea editorArea;
    private JTextArea numerosArea;
    private JTextArea consolaArea;
    private final Interprete interprete = new Interprete();

    public Interfaz() {
        initUI();
    }

    private void initUI() {
        setTitle("✦ Lenguaje MSH");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);

        // 🎨 Colores estilo Interfaz 2
        Color bgOscuro   = new Color(18, 18, 28);
        Color bgPanel    = new Color(26, 26, 40);
        Color bgEditor   = new Color(22, 22, 35);
        Color amarillo   = new Color(255, 210, 0);
        Color azul       = new Color(100, 180, 255);
        Color textoClaro = new Color(220, 220, 235);
        Color verde      = new Color(80, 220, 120);
        Color rojo       = new Color(255, 80, 80);

        getContentPane().setBackground(bgOscuro);
        setLayout(new BorderLayout(10, 10));

        // ── HEADER ──
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 10));
        header.setBackground(bgPanel);
        header.setBorder(new MatteBorder(0, 0, 1, 0, amarillo));

        JLabel titulo = new JLabel("⚡ Lenguaje MSH");
        titulo.setFont(new Font("Monospaced", Font.BOLD, 18));
        titulo.setForeground(amarillo);

        JLabel subtitulo = new JLabel("obi=int | anaki=double | padme=String");
        subtitulo.setFont(new Font("Monospaced", Font.PLAIN, 12));
        subtitulo.setForeground(azul);

        header.add(titulo);
        header.add(subtitulo);
        add(header, BorderLayout.NORTH);

        // ── EDITOR CON NÚMEROS ──
        editorArea = new JTextArea();
        editorArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        editorArea.setBackground(bgEditor);
        editorArea.setForeground(textoClaro);
        editorArea.setCaretColor(amarillo);
        editorArea.setMargin(new Insets(8, 8, 8, 8));

        numerosArea = new JTextArea("1");
        numerosArea.setFont(editorArea.getFont());
        numerosArea.setBackground(new Color(30,30,45));
        numerosArea.setForeground(new Color(140,140,160));
        numerosArea.setEditable(false);
        numerosArea.setMargin(new Insets(8, 6, 8, 6));

        editorArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarLineas(); }
            public void removeUpdate(DocumentEvent e) { actualizarLineas(); }
            public void changedUpdate(DocumentEvent e) {}
        });

        JScrollPane scrollEditor = new JScrollPane(editorArea);
        scrollEditor.setRowHeaderView(numerosArea);
        scrollEditor.setBorder(null);

        JPanel panelEditor = crearPanel("📝 Editor de código", scrollEditor, bgPanel, amarillo);

        // ── CONSOLA ──
        consolaArea = new JTextArea();
        consolaArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        consolaArea.setBackground(new Color(14, 20, 14));
        consolaArea.setForeground(verde);
        consolaArea.setEditable(false);
        consolaArea.setMargin(new Insets(10,10,10,10));

        JScrollPane scrollConsola = new JScrollPane(consolaArea);
        scrollConsola.setBorder(null);

        JPanel panelConsola = crearPanel("🖥 Consola", scrollConsola, bgPanel, verde);

        // ── SPLIT ──
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelEditor, panelConsola);
        split.setDividerLocation(500);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);

        // ── BOTONES ──
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        barra.setBackground(bgPanel);
        barra.setBorder(new MatteBorder(1,0,0,0,new Color(50,50,70)));

        JButton btnEjecutar = crearBoton("▶ Ejecutar", new Color(40,140,60), Color.WHITE);
        JButton btnLimpiar  = crearBoton("🗑 Limpiar", new Color(70,70,90), textoClaro);
        JButton btnEjemplo  = crearBoton("📄 Ejemplo", new Color(60,90,160), Color.WHITE);

        btnEjecutar.addActionListener(e -> ejecutarCodigo(verde));
        btnLimpiar.addActionListener(e -> consolaArea.setText(""));
        btnEjemplo.addActionListener(e -> cargarEjemplo());

        barra.add(btnEjemplo);
        barra.add(btnLimpiar);
        barra.add(btnEjecutar);

        add(barra, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ── FUNCIONALIDAD ORIGINAL ──

    private void actualizarLineas() {
        int total = editorArea.getDocument().getDefaultRootElement().getElementCount();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= total; i++) sb.append(i).append("\n");
        numerosArea.setText(sb.toString());
    }

    private void ejecutarCodigo(Color verde) {
        consolaArea.setText("");
        
        consolaArea.append("── Ejecutando ──\n");
        String resultado = interprete.ejecutar(editorArea.getText());
        consolaArea.append(resultado.isEmpty() ? "(sin salida)\n" : resultado);
        consolaArea.append("\n── Fin ──\n\n");
    }

    private void cargarEjemplo() {
        editorArea.setText(
            "@ Ejemplo MSH\n" +
            "obi x $ 5;\n" +
            "obi y $ 10;\n" +
            "imprimir(x + y);\n"
        );
    }

    // ── COMPONENTES VISUALES ──

    private JPanel crearPanel(String titulo, JComponent contenido, Color bg, Color colorTitulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bg);
        panel.setBorder(new CompoundBorder(
                new EmptyBorder(6,6,6,6),
                new LineBorder(new Color(50,50,70),1,true)
        ));

        JLabel lbl = new JLabel("  " + titulo);
        lbl.setForeground(colorTitulo);
        lbl.setFont(new Font("Monospaced", Font.BOLD, 12));
        lbl.setBorder(new EmptyBorder(6,4,6,4));
        lbl.setOpaque(true);
        lbl.setBackground(new Color(20,20,32));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(contenido, BorderLayout.CENTER);

        return panel;
    }

    private JButton crearBoton(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Monospaced", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new CompoundBorder(
                new LineBorder(bg.brighter(),1,true),
                new EmptyBorder(6,18,6,18)
        ));
        return btn;
    }
}