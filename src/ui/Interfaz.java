package ui;

import interprete.Interprete;
import interprete.Token;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class Interfaz extends JFrame {

    private JTextPane         editorArea;
    private JTextArea         numerosArea;
    private JTextPane         consolaArea;
    private JPanel            panelTabla;
    private boolean           tablaVisible = true;
    private DefaultTableModel modeloTabla;

    private Interprete interprete;

    private static final Color BG_OSCURO   = new Color(13, 13, 23);
    private static final Color BG_PANEL    = new Color(20, 20, 34);
    private static final Color BG_EDITOR   = new Color(16, 16, 28);
    private static final Color BG_CONSOLA  = new Color(10, 16, 10);
    private static final Color BG_HEADER   = new Color(16, 16, 26);
    private static final Color AMARILLO    = new Color(255, 200, 0);
    private static final Color AZUL        = new Color(90, 170, 255);
    private static final Color TEXTO_CLARO = new Color(210, 210, 228);
    private static final Color VERDE       = new Color(70, 210, 110);
    private static final Color ROJO        = new Color(255, 75, 75);
    private static final Color ROJO_SUAVE  = new Color(230, 120, 120);
    private static final Color ROJO_BG     = new Color(55, 18, 18);
    private static final Color BORDE       = new Color(42, 42, 62);
    private static final Color VIOLETA     = new Color(160, 100, 255);
    private static final Color NARANJA     = new Color(255, 160, 60);

    public Interfaz() {
        interprete = new Interprete();
        initUI();
    }

    private void initUI() {
        setTitle("✦ Lenguaje MSH");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 700);
        setMinimumSize(new Dimension(900, 550));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_OSCURO);
        setLayout(new BorderLayout(0, 0));

        add(crearHeader(),       BorderLayout.NORTH);
        add(crearCuerpo(),       BorderLayout.CENTER);
        add(crearBarraBotones(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // ── HEADER ──────────────────────────────────────────────────────────────

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_HEADER);
        header.setBorder(new MatteBorder(0, 0, 2, 0, AMARILLO));

        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 10));
        izq.setOpaque(false);

        JLabel titulo = new JLabel("⚡ Lenguaje MSH");
        titulo.setFont(new Font("Monospaced", Font.BOLD, 17));
        titulo.setForeground(AMARILLO);

        JLabel subtitulo = new JLabel("obi = int   |   anaki = double   |   padme = String");
        subtitulo.setFont(new Font("Monospaced", Font.PLAIN, 11));
        subtitulo.setForeground(AZUL);

        izq.add(titulo);
        izq.add(subtitulo);

        JPanel der = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        der.setOpaque(false);

        JButton btnToggleTabla = crearBoton("⊞ Tokens", new Color(50, 40, 90), VIOLETA);
        btnToggleTabla.setFont(new Font("Monospaced", Font.BOLD, 12));
        btnToggleTabla.addActionListener(e -> toggleTabla());
        der.add(btnToggleTabla);

        header.add(izq, BorderLayout.WEST);
        header.add(der, BorderLayout.EAST);
        return header;
    }

    // ── CUERPO ──────────────────────────────────────────────────────────────

    private JSplitPane crearCuerpo() {
        JSplitPane splitVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                crearPanelEditor(), crearPanelConsola());
        splitVertical.setDividerLocation(390);
        splitVertical.setResizeWeight(0.6);
        splitVertical.setBorder(null);
        splitVertical.setBackground(BG_OSCURO);

        panelTabla = crearPanelTabla();

        JSplitPane splitHorizontal = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, splitVertical, panelTabla);
        splitHorizontal.setDividerLocation(750);
        splitHorizontal.setResizeWeight(0.65);
        splitHorizontal.setBorder(null);
        splitHorizontal.setBackground(BG_OSCURO);

        return splitHorizontal;
    }

    // ── BARRA DE BOTONES ────────────────────────────────────────────────────

    private JPanel crearBarraBotones() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        barra.setBackground(BG_PANEL);
        barra.setBorder(new MatteBorder(1, 0, 0, 0, BORDE));

        JButton btnEjemplo  = crearBoton("📄 Ejemplo",  new Color(45, 75, 145), Color.WHITE);
        JButton btnLimpiar  = crearBoton("🗑  Limpiar",  new Color(60, 60, 85),  TEXTO_CLARO);
        JButton btnEjecutar = crearBoton("▶  Ejecutar", new Color(30, 125, 55), Color.WHITE);

        btnEjemplo .addActionListener(e -> cargarEjemplo());
        btnLimpiar .addActionListener(e -> limpiarTodo());
        btnEjecutar.addActionListener(e -> ejecutarCodigo());

        barra.add(btnEjemplo);
        barra.add(btnLimpiar);
        barra.add(btnEjecutar);
        return barra;
    }

    // ── PANEL EDITOR ────────────────────────────────────────────────────────

    private JPanel crearPanelEditor() {
        editorArea = new JTextPane();
        editorArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        editorArea.setBackground(BG_EDITOR);
        editorArea.setForeground(TEXTO_CLARO);
        editorArea.setCaretColor(AMARILLO);
        editorArea.setSelectionColor(new Color(80, 80, 150));
        editorArea.setMargin(new Insets(8, 10, 8, 10));

        numerosArea = new JTextArea("1");
        numerosArea.setFont(editorArea.getFont());
        numerosArea.setBackground(new Color(22, 22, 36));
        numerosArea.setForeground(new Color(110, 110, 140));
        numerosArea.setEditable(false);
        numerosArea.setMargin(new Insets(8, 8, 8, 8));

        editorArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { actualizarLineas(); limpiarEstilosEditor(); }
            public void removeUpdate(DocumentEvent e)  { actualizarLineas(); limpiarEstilosEditor(); }
            public void changedUpdate(DocumentEvent e) {}
        });

        JScrollPane scroll = new JScrollPane(editorArea);
        scroll.setRowHeaderView(numerosArea);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_EDITOR);

        return crearPanel("📝  Editor de código", scroll, AMARILLO);
    }

    // ── PANEL CONSOLA ───────────────────────────────────────────────────────

    private JPanel crearPanelConsola() {
        consolaArea = new JTextPane();
        consolaArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        consolaArea.setBackground(BG_CONSOLA);
        consolaArea.setForeground(VERDE);
        consolaArea.setEditable(false);
        consolaArea.setMargin(new Insets(10, 12, 10, 12));

        JScrollPane scroll = new JScrollPane(consolaArea);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_CONSOLA);

        return crearPanel("🖥  Consola de salida", scroll, VERDE);
    }

    // ── PANEL TABLA ─────────────────────────────────────────────────────────

    private JPanel crearPanelTabla() {
        String[] columnas = { "Token", "Lexema", "Patrón", "¿Pal. reservada?" };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("Monospaced", Font.PLAIN, 12));
        tabla.setBackground(new Color(16, 16, 30));
        tabla.setForeground(TEXTO_CLARO);
        tabla.setGridColor(new Color(38, 38, 58));
        tabla.setRowHeight(24);
        tabla.setSelectionBackground(new Color(50, 50, 100));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setShowVerticalLines(true);
        tabla.setShowHorizontalLines(true);
        tabla.getTableHeader().setReorderingAllowed(false);

        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Monospaced", Font.BOLD, 12));
        header.setBackground(new Color(28, 28, 48));
        header.setForeground(VIOLETA);
        header.setBorder(new MatteBorder(0, 0, 1, 0, VIOLETA));

        // Renderer columna 3 — ¿Pal. reservada?
        tabla.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setHorizontalAlignment(CENTER);
                boolean esRes = "Sí".equals(val);
                setForeground(esRes ? VERDE : new Color(150, 150, 170));
                setBackground(sel ? new Color(50, 50, 100) : new Color(16, 16, 30));
                return this;
            }
        });

        // Renderer columna 0 — tipo token en naranja
        tabla.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setForeground(NARANJA);
                setBackground(sel ? new Color(50, 50, 100) : new Color(16, 16, 30));
                return this;
            }
        });

        int[] anchos = { 140, 100, 175, 120 };
        for (int i = 0; i < anchos.length; i++)
            tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(16, 16, 30));

        return crearPanel("⊞  Tabla de Tokens", scroll, VIOLETA);
    }

    // ── LÓGICA DE EJECUCIÓN ─────────────────────────────────────────────────

    private void ejecutarCodigo() {
        limpiarConsola();
        limpiarEstilosEditor();
        modeloTabla.setRowCount(0);
        interprete = new Interprete();

        String codigo = editorArea.getText();
        if (codigo.trim().isEmpty()) {
            mostrarEnConsola("(editor vacío — escribe algo primero)\n", ROJO);
            return;
        }

        mostrarEnConsola("── Ejecutando ──────────────────────\n", AZUL);

        String resultado = interprete.ejecutar(codigo);

        boolean esError = resultado != null && resultado.contains("⚠");

        // Tokens en consola y tabla (solo hasta antes del error si lo hay)
        mostrarTokensEnConsola(esError);
        actualizarTablaTokens(esError);

        if (resultado == null || resultado.trim().isEmpty()) {
            mostrarEnConsola("(sin salida)\n", new Color(140, 140, 160));
        } else {
            if (esError) {
                int indiceSeparador = resultado.lastIndexOf("\n⚠");
                if (indiceSeparador > 0) {
                    String salidaPrevia = resultado.substring(0, indiceSeparador).trim();
                    if (!salidaPrevia.isEmpty()) {
                        mostrarEnConsola(salidaPrevia + "\n", VERDE);
                        mostrarEnConsola("\n", VERDE);
                    }
                }

                String mensajeError = resultado.substring(resultado.lastIndexOf("⚠")).trim();
                int lineaError = extraerNumeroLinea(mensajeError);

                String encabezado = lineaError > 0
                        ? "⚠  Error — línea " + lineaError + "\n"
                        : "⚠  Error\n";
                mostrarEnConsola(encabezado, ROJO);

                String descripcion = mensajeError.replaceFirst("^⚠\\s*", "").trim();
                mostrarEnConsola("   " + descripcion + "\n", ROJO_SUAVE);

                if (lineaError > 0) {
                    String fragmento = obtenerLinea(codigo, lineaError);
                    if (fragmento != null)
                        mostrarEnConsola("\n   > " + fragmento.stripLeading() + "\n",
                                new Color(190, 90, 90));
                    pintarLineaError(lineaError);
                }

            } else {
                mostrarEnConsola(resultado, VERDE);
            }
        }

        mostrarEnConsola("\n── Fin ─────────────────────────────\n", AZUL);
    }

    // ── TOKENS EN CONSOLA ────────────────────────────────────────────────────

    private void mostrarTokensEnConsola(boolean esError) {
        List<Token> tokens = interprete.getTokens();
        int limite = esError ? interprete.getTokenHastaError() : tokens.size() - 1;

        mostrarEnConsola("── Tokens reconocidos ──────────────\n", AZUL);

        boolean hayTokens = false;
        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            if (t.tipo == Token.Tipo.EOF)           continue;
            if (esError && i > limite)               continue;

            hayTokens = true;

            Color colorLexema = colorLexema(t.tipo);
            String descripcion = describirToken(t);

            mostrarEnConsola("  ", TEXTO_CLARO);
            mostrarEnConsola(t.valor,      colorLexema);
            mostrarEnConsola("  →  ",      new Color(80, 80, 110));
            mostrarEnConsola(descripcion,  esReservada(t.tipo) ? VIOLETA : AZUL);
            mostrarEnConsola("\n",         TEXTO_CLARO);
        }

        if (!hayTokens)
            mostrarEnConsola("  (ninguno)\n", new Color(100, 100, 120));

        mostrarEnConsola("────────────────────────────────────\n", AZUL);
    }

    private String describirToken(Token t) {
        return switch (t.tipo) {
            case OBI            -> "palabra reservada  (obi = int)";
            case ANAKI          -> "palabra reservada  (anaki = double)";
            case PADME          -> "palabra reservada  (padme = String)";
            case IMPRIMIR       -> "palabra reservada  (imprimir)";
            case IDENTIFICADOR  -> "identificador";
            case NUMERO_ENTERO  -> "número entero";
            case NUMERO_DECIMAL -> "número decimal";
            case CADENA         -> "cadena de texto";
            case ASIGNAR        -> "asignación  ($)";
            case SUMA           -> "operador suma";
            case RESTA          -> "operador resta";
            case MULT           -> "operador multiplicación";
            case DIV            -> "operador división";
            case CONCAT         -> "operador concatenación  (%)";
            case LPAREN         -> "paréntesis abre";
            case RPAREN         -> "paréntesis cierra";
            case PUNTO_COMA     -> "fin de instrucción  (;)";
            case COMENTARIO         -> "comentario  (@)";
            case TEXTO_COMENTARIO   -> "texto de comentario";
            default             -> t.tipo.name().toLowerCase();
        };
    }

    private Color colorLexema(Token.Tipo tipo) {
        return switch (tipo) {
            case OBI, ANAKI, PADME, IMPRIMIR               -> VIOLETA;
            case IDENTIFICADOR                             -> TEXTO_CLARO;
            case NUMERO_ENTERO, NUMERO_DECIMAL             -> new Color(100, 210, 255);
            case CADENA                                    -> new Color(200, 160, 90);
            case ASIGNAR, SUMA, RESTA, MULT, DIV,
                 CONCAT, LPAREN, RPAREN, PUNTO_COMA        -> NARANJA;
            case COMENTARIO                                -> new Color(150, 100, 200);
            case TEXTO_COMENTARIO                          -> new Color(100, 100, 100);
            default                                        -> TEXTO_CLARO;
        };
    }

    // ── TABLA DINÁMICA ──────────────────────────────────────────────────────

    private void actualizarTablaTokens(boolean esError) {
        List<Token> tokens = interprete.getTokens();
        int limite = esError ? interprete.getTokenHastaError() : tokens.size() - 1;

        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            if (t.tipo == Token.Tipo.EOF)  continue;
            if (esError && i > limite)      continue;

            modeloTabla.addRow(new Object[]{
                t.tipo.name(),
                t.valor,
                obtenerPatron(t.tipo),
                esReservada(t.tipo) ? "Sí" : "No"
            });
        }
    }

    private String obtenerPatron(Token.Tipo tipo) {
        return switch (tipo) {
            case OBI            -> "obi";
            case ANAKI          -> "anaki";
            case PADME          -> "padme";
            case IMPRIMIR       -> "imprimir";
            case IDENTIFICADOR  -> "[a-zA-Z_][a-zA-Z0-9_]*";
            case NUMERO_ENTERO  -> "-?[0-9]+";
            case NUMERO_DECIMAL -> "-?[0-9]+\\.[0-9]+";
            case CADENA         -> "\"[^\"]*\"";
            case ASIGNAR        -> "\\$";
            case SUMA           -> "\\+";
            case RESTA          -> "-";
            case MULT           -> "\\*";
            case DIV            -> "/";
            case CONCAT         -> "%";
            case LPAREN         -> "\\(";
            case RPAREN         -> "\\)";
            case PUNTO_COMA     -> ";";
            case COMENTARIO         -> "@";
            case TEXTO_COMENTARIO   -> ".*";
            default             -> "-";
        };
    }

    private boolean esReservada(Token.Tipo tipo) {
        return switch (tipo) {
            case OBI, ANAKI, PADME, IMPRIMIR,ASIGNAR,SUMA,RESTA,MULT,DIV,CONCAT,LPAREN,RPAREN, PUNTO_COMA, COMENTARIO  -> true;
            default -> false;
        };
    }

    // ── UTILIDADES DEL EDITOR ───────────────────────────────────────────────

    private void limpiarEstilosEditor() {
        SwingUtilities.invokeLater(() -> {
            StyledDocument doc = editorArea.getStyledDocument();
            Style def = StyleContext.getDefaultStyleContext()
                    .getStyle(StyleContext.DEFAULT_STYLE);
            doc.setCharacterAttributes(0, doc.getLength(), def, true);
        });
    }

    private void pintarLineaError(int numeroLinea) {
        if (numeroLinea < 1) return;

        StyledDocument doc = editorArea.getStyledDocument();
        Element root = doc.getDefaultRootElement();
        if (numeroLinea > root.getElementCount()) return;

        Element elem  = root.getElement(numeroLinea - 1);
        int start = elem.getStartOffset();
        int end   = elem.getEndOffset();

        Style style = editorArea.addStyle("ErrorLine", null);
        StyleConstants.setForeground(style, ROJO);
        StyleConstants.setBackground(style, ROJO_BG);
        StyleConstants.setUnderline(style, true);

        doc.setCharacterAttributes(start, end - start, style, true);

        editorArea.setCaretPosition(start);
        SwingUtilities.invokeLater(() -> {
            try {
                Rectangle rect = editorArea.modelToView(start);
                if (rect != null) editorArea.scrollRectToVisible(rect);
            } catch (BadLocationException ignored) {}
        });
    }

    private String obtenerLinea(String codigo, int numeroLinea) {
        String[] lineas = codigo.split("\n", -1);
        if (numeroLinea < 1 || numeroLinea > lineas.length) return null;
        return lineas[numeroLinea - 1];
    }

    private int extraerNumeroLinea(String mensajeError) {
        if (mensajeError == null) return -1;
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                "línea\\s+(\\d+)|Línea\\s+(\\d+)|linea\\s+(\\d+)",
                java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher matcher = pattern.matcher(mensajeError);
        if (matcher.find()) {
            for (int g = 1; g <= matcher.groupCount(); g++) {
                if (matcher.group(g) != null) {
                    try { return Integer.parseInt(matcher.group(g)); }
                    catch (NumberFormatException ignored) {}
                }
            }
        }
        return -1;
    }

    private void actualizarLineas() {
        int total = editorArea.getDocument().getDefaultRootElement().getElementCount();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= total; i++) sb.append(i).append("\n");
        numerosArea.setText(sb.toString());
    }

    // ── CONSOLA ─────────────────────────────────────────────────────────────

    private void mostrarEnConsola(String texto, Color color) {
        StyledDocument doc = consolaArea.getStyledDocument();
        Style style = consolaArea.addStyle("s", null);
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), texto, style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void limpiarConsola() {
        consolaArea.setText("");
    }

    private void limpiarTodo() {
        limpiarConsola();
        limpiarEstilosEditor();
        modeloTabla.setRowCount(0);
    }

    // ── TOGGLE TABLA ────────────────────────────────────────────────────────

    private void toggleTabla() {
        tablaVisible = !tablaVisible;
        panelTabla.setVisible(tablaVisible);
        SwingUtilities.invokeLater(() -> {
            Container parent = panelTabla.getParent();
            if (parent instanceof JSplitPane sp) {
                sp.setDividerLocation(tablaVisible ? 750 : sp.getWidth());
            }
        });
    }

    // ── EJEMPLO ─────────────────────────────────────────────────────────────

    private void cargarEjemplo() {
        limpiarEstilosEditor();
        limpiarConsola();
        modeloTabla.setRowCount(0);
        editorArea.setText(
            "@ Ejemplo del lenguaje MSH\n" +
            "\n" +
            "obi x $ 10;\n" +
            "obi y $ 5;\n" +
            "imprimir(x + y);\n" +
            "imprimir(x - y);\n" +
            "imprimir(x * y);\n" +
            "\n" +
            "anaki pi $ 3.14;\n" +
            "anaki radio $ 2.0;\n" +
            "imprimir(pi * radio * radio);\n" +
            "\n" +
            "padme nombre $ \"Ana\";\n" +
            "padme saludo $ \"Hola, \";\n" +
            "imprimir(saludo % nombre);"
        );
    }

    // ── HELPERS DE UI ───────────────────────────────────────────────────────

    private JPanel crearPanel(String titulo, JComponent contenido, Color colorTitulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_PANEL);
        panel.setBorder(new CompoundBorder(
                new EmptyBorder(5, 5, 5, 5),
                new LineBorder(BORDE, 1, true)
        ));

        JLabel lbl = new JLabel("  " + titulo);
        lbl.setForeground(colorTitulo);
        lbl.setFont(new Font("Monospaced", Font.BOLD, 12));
        lbl.setBorder(new EmptyBorder(6, 6, 6, 6));
        lbl.setOpaque(true);
        lbl.setBackground(new Color(14, 14, 24));

        panel.add(lbl,       BorderLayout.NORTH);
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
        btn.setOpaque(true);
        btn.setBorder(new CompoundBorder(
                new LineBorder(bg.brighter(), 1, true),
                new EmptyBorder(6, 16, 6, 16)
        ));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(bg.brighter()); }
            @Override public void mouseExited (MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }
}