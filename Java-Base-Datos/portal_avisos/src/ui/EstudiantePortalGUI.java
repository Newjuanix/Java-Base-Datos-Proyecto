package ui;

import dao.AvisoDAO;
import models.Aviso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EstudiantePortalGUI extends JFrame {
    private final JTable tabla;
    private final DefaultTableModel modelo;
    private final AvisoDAO dao = new AvisoDAO();
    private final String usuarioNombre;

    public EstudiantePortalGUI(String nombre) {
        this.usuarioNombre = nombre;

        setTitle("Portal de Avisos - Estudiante: " + nombre);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        // --- Panel Superior (Encabezado) con Gradiente ---
        JPanel panelHeader = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(103, 58, 183), getWidth(), getHeight(), new Color(63, 81, 181));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelHeader.setOpaque(false);
        panelHeader.setPreferredSize(new Dimension(0, 100));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel lblBienvenida = new JLabel("Bienvenid@, " + nombre);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblBienvenida.setForeground(Color.WHITE);

        JLabel lblDescripcion = new JLabel("Consulta los avisos y comunicados de tus profesores");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDescripcion.setForeground(new Color(200, 255, 200));

        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setOpaque(false);
        panelTexto.add(lblBienvenida);
        panelTexto.add(Box.createVerticalStrut(8));
        panelTexto.add(lblDescripcion);

        panelHeader.add(panelTexto, BorderLayout.WEST);

        JButton btnSalir = crearBotonSalir();
        panelHeader.add(btnSalir, BorderLayout.EAST);

        add(panelHeader, BorderLayout.NORTH);

        // --- Panel Central (Tabla de Avisos) ---
        modelo = new DefaultTableModel(new Object[]{"ID", "Profesor", "Título", "Contenido", "Fecha"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(32);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(new Color(103, 58, 183));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setSelectionBackground(new Color(126, 103, 195));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setGridColor(new Color(230, 230, 235));
        tabla.setBackground(Color.WHITE);

        // Ancho de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(350);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(150);

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(scrollTabla, BorderLayout.CENTER);

        // --- Panel Inferior (Botones de Acción) ---
        JPanel panelBotones = crearPanelBotones();
        add(panelBotones, BorderLayout.SOUTH);

        cargarAvisos();
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(new Color(248, 249, 252));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        JButton btnActualizar = new JButton("Actualizar Avisos");
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnActualizar.setBackground(new Color(103, 58, 183));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnActualizar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnActualizar.setPreferredSize(new Dimension(160, 40));
        btnActualizar.addActionListener(e -> cargarAvisos());
        panel.add(btnActualizar);

        JButton btnVerDetalles = new JButton("Ver Detalles");
        btnVerDetalles.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnVerDetalles.setBackground(new Color(63, 81, 181));
        btnVerDetalles.setForeground(Color.WHITE);
        btnVerDetalles.setFocusPainted(false);
        btnVerDetalles.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnVerDetalles.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVerDetalles.setPreferredSize(new Dimension(160, 40));
        btnVerDetalles.addActionListener(e -> verDetallesAviso());
        panel.add(btnVerDetalles);

        return panel;
    }

    private JButton crearBotonSalir() {
        JButton btn = new JButton("Salir");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setBackground(new Color(200, 200, 200));
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
        });
        return btn;
    }

    private void cargarAvisos() {
        modelo.setRowCount(0);
        List<Aviso> avisos = dao.listarAvisos();
        for (Aviso a : avisos) {
            modelo.addRow(new Object[]{
                a.getId(),
                a.getNombreProfesor(),
                a.getTitulo(),
                truncarTexto(a.getContenido(), 80),
                a.getFechaPublicacion()
            });
        }
    }

    private String truncarTexto(String texto, int longitud) {
        if (texto == null) return "";
        return texto.length() > longitud ? texto.substring(0, longitud) + "..." : texto;
    }

    private void verDetallesAviso() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un aviso para ver detalles.", 
                "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = (int) modelo.getValueAt(fila, 0);
            String profesor = (String) modelo.getValueAt(fila, 1);
            String titulo = (String) modelo.getValueAt(fila, 2);
            String fecha = (String) modelo.getValueAt(fila, 4);

            // Obtener contenido completo
            List<Aviso> avisos = dao.listarAvisos();
            String contenidoCompleto = "";
            for (Aviso a : avisos) {
                if (a.getId() == id) {
                    contenidoCompleto = a.getContenido();
                    break;
                }
            }

            // Crear diálogo de detalles
            JDialog dialogo = new JDialog(this, "Detalles del Aviso", true);
            dialogo.setSize(600, 400);
            dialogo.setLocationRelativeTo(this);
            dialogo.setLayout(new BorderLayout(10, 10));

            // Encabezado
            JPanel panelEncabezado = new JPanel();
            panelEncabezado.setLayout(new BoxLayout(panelEncabezado, BoxLayout.Y_AXIS));
            panelEncabezado.setBackground(new Color(76, 175, 80));
            panelEncabezado.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            JLabel lblTitulo = new JLabel(titulo);
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblTitulo.setForeground(Color.WHITE);

            JLabel lblProfesor = new JLabel("Profesor: " + profesor);
            lblProfesor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblProfesor.setForeground(new Color(220, 255, 220));

            JLabel lblFecha = new JLabel("Publicado: " + fecha);
            lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblFecha.setForeground(new Color(220, 255, 220));

            panelEncabezado.add(lblTitulo);
            panelEncabezado.add(Box.createVerticalStrut(8));
            panelEncabezado.add(lblProfesor);
            panelEncabezado.add(Box.createVerticalStrut(3));
            panelEncabezado.add(lblFecha);

            dialogo.add(panelEncabezado, BorderLayout.NORTH);

            // Contenido
            JTextArea areaContenido = new JTextArea(contenidoCompleto);
            areaContenido.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            areaContenido.setLineWrap(true);
            areaContenido.setWrapStyleWord(true);
            areaContenido.setEditable(false);
            areaContenido.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            areaContenido.setBackground(Color.WHITE);

            JScrollPane scroll = new JScrollPane(areaContenido);
            dialogo.add(scroll, BorderLayout.CENTER);

            // Botón Cerrar
            JButton btnCerrar = new JButton("Cerrar");
            btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnCerrar.setBackground(new Color(200, 200, 200));
            btnCerrar.setForeground(Color.BLACK);
            btnCerrar.setFocusPainted(false);
            btnCerrar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnCerrar.addActionListener(e -> dialogo.dispose());

            JPanel panelBotones = new JPanel();
            panelBotones.setBackground(new Color(245, 245, 245));
            panelBotones.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));
            panelBotones.add(btnCerrar);
            dialogo.add(panelBotones, BorderLayout.SOUTH);

            dialogo.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EstudiantePortalGUI("Ana López").setVisible(true));
    }
}
