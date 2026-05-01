/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codigo;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class InterfazGUI extends JFrame {

    private JTextArea editorArea;
    private JTextArea consolaArea;
    private JButton btnEjecutar, btnLimpiar;

    public InterfazGUI() {
        initUI();
    }

    private void initUI() {
        setTitle("✦ Lenguaje MSH");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        // Colores del tema oscuro Star Wars
        Color bgOscuro   = new Color(18, 18, 28);
        Color bgPanel    = new Color(26, 26, 40);
        Color bgEditor   = new Color(22, 22, 35);
        Color amarilloSW = new Color(255, 210, 0);
        Color azulClaro  = new Color(100, 180, 255);
        Color textoClaro = new Color(220, 220, 235);
        Color verde      = new Color(80, 220, 120);
        Color rojo       = new Color(255, 80, 80);

        getContentPane().setBackground(bgOscuro);
        setLayout(new BorderLayout(10, 10));

        // ── Header ──
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 10));
        header.setBackground(bgPanel);
        header.setBorder(new MatteBorder(0, 0, 1, 0, amarilloSW));
        JLabel titulo = new JLabel("⚡ MSH");
        titulo.setFont(new Font("Monospaced", Font.BOLD, 18));
        titulo.setForeground(amarilloSW);
        JLabel subtitulo = new JLabel("abi = int  |  anaki = double  |  padme = String | igual = ana");
        subtitulo.setFont(new Font("Monospaced", Font.PLAIN, 12));
        subtitulo.setForeground(azulClaro);
        header.add(titulo);
        header.add(subtitulo);
        add(header, BorderLayout.NORTH);

        // ── Panel central con editor y consola ──
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(480);
        split.setBackground(bgOscuro);
        split.setBorder(null);

        // Editor
        editorArea = new JTextArea();
        editorArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        editorArea.setBackground(bgEditor);
        editorArea.setForeground(textoClaro);
        editorArea.setCaretColor(amarilloSW);
        editorArea.setSelectionColor(new Color(60, 80, 140));
        editorArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        editorArea.setText(codigoEjemplo());

        JScrollPane scrollEditor = new JScrollPane(editorArea);
        scrollEditor.setBorder(null);
        JPanel panelEditor = crearPanel("📝  Editor de código", scrollEditor, bgPanel, amarilloSW);
        split.setLeftComponent(panelEditor);

        // Consola
        consolaArea = new JTextArea();
        consolaArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        consolaArea.setBackground(new Color(14, 20, 14));
        consolaArea.setForeground(verde);
        consolaArea.setEditable(false);
        consolaArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        consolaArea.setText("// Salida aparecerá aquí...\n");

        JScrollPane scrollConsola = new JScrollPane(consolaArea);
        scrollConsola.setBorder(null);
        JPanel panelConsola = crearPanel("🖥  Consola de salida", scrollConsola, bgPanel, verde);
        split.setRightComponent(panelConsola);

        add(split, BorderLayout.CENTER);

        // ── Barra de botones ──
        JPanel barraBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        barraBtn.setBackground(bgPanel);
        barraBtn.setBorder(new MatteBorder(1, 0, 0, 0, new Color(50, 50, 70)));

        btnLimpiar = crearBoton("🗑  Limpiar", new Color(70, 70, 90), textoClaro);
        btnLimpiar.addActionListener(e -> {
            editorArea.setText("");
            consolaArea.setText("// Listo.\n");
            editorArea.requestFocus();
        });

        btnEjecutar = crearBoton("▶  Ejecutar", new Color(40, 140, 60), Color.WHITE);
        btnEjecutar.addActionListener(e -> ejecutarCodigo(verde, rojo));

        // Atajo teclado Ctrl+Enter
        editorArea.getInputMap().put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "ejecutar");
        editorArea.getActionMap().put("ejecutar", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { ejecutarCodigo(verde, rojo); }
        });

        barraBtn.add(new JLabel("Ctrl+Enter para ejecutar  "));
        ((JLabel)barraBtn.getComponent(0)).setForeground(new Color(120,120,150));
        ((JLabel)barraBtn.getComponent(0)).setFont(new Font("Monospaced", Font.PLAIN, 11));
        barraBtn.add(btnLimpiar);
        barraBtn.add(btnEjecutar);
        add(barraBtn, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void ejecutarCodigo(Color verde, Color rojo) {
        String codigo = editorArea.getText().trim();
        if (codigo.isEmpty()) {
            consolaArea.setForeground(rojo);
            consolaArea.setText("⚠ El editor está vacío.\n");
            return;
        }
        String resultado = Main.ejecutar(codigo);
        if (resultado.startsWith("❌")) {
            consolaArea.setForeground(rojo);
        } else {
            consolaArea.setForeground(verde);
        }
        consolaArea.setText(resultado.isEmpty() ? "// Ejecución completada (sin salida).\n" : resultado);
    }

    private JPanel crearPanel(String titulo, JComponent contenido, Color bg, Color colorTitulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bg);
        panel.setBorder(new CompoundBorder(
            new EmptyBorder(6, 6, 6, 6),
            new LineBorder(new Color(50, 50, 70), 1, true)
        ));
        JLabel lbl = new JLabel("  " + titulo);
        lbl.setFont(new Font("Monospaced", Font.BOLD, 12));
        lbl.setForeground(colorTitulo);
        lbl.setBorder(new EmptyBorder(6, 4, 6, 4));
        lbl.setBackground(new Color(20, 20, 32));
        lbl.setOpaque(true);
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
        btn.setBorder(new CompoundBorder(
            new LineBorder(bg.brighter(), 1, true),
            new EmptyBorder(6, 18, 6, 18)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private String codigoEjemplo() {
        return 
            "@ Ejemplo del lenguaje MSH\n\n" +
            "abi x ana 10;\n" +
            "abi y ana 5;\n" +
            "imprimir(x + y);\n" +
            "imprimir(x - y);\n" +
            "imprimir(x * y);\n\n" +
            "anaki pi ana 3.14;\n" +
            "anaki radio ana 2;\n" +
            "imprimir(pi * radio * radio);\n\n" +
            "padme nombre ana \"Ana\";\n" +
            "padme saludo ana \"Hola, \";\n" +
            "imprimir(saludo ++ nombre);\n";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InterfazGUI::new);
    }
}