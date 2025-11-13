package ui;

import dao.AvisoDAO;
import database.Conexion;
import models.Aviso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.List;

public class ProfesorPortalGUI extends JFrame {
    private JTextField txtTitulo;
    private JTextArea txtContenido;
    private final JTable tabla;
    private final DefaultTableModel modelo;
    private final AvisoDAO dao = new AvisoDAO();
    private final String usuarioNombre;

    public ProfesorPortalGUI(String nombre) {
        this.usuarioNombre = nombre;

        setTitle("👨‍🏫 Portal de Avisos - Profesor: " + nombre);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
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
        panelHeader.setPreferredSize(new Dimension(0, 90));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));

        JLabel lblBienvenida = new JLabel("Bienvenid@, " + nombre);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblBienvenida.setForeground(Color.WHITE);

        JLabel lblDescripcion = new JLabel("Gestiona tus avisos académicos fácilmente");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDescripcion.setForeground(new Color(200, 200, 200));

        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setOpaque(false);
        panelTexto.add(lblBienvenida);
        panelTexto.add(Box.createVerticalStrut(5));
        panelTexto.add(lblDescripcion);

        panelHeader.add(panelTexto, BorderLayout.WEST);

        JButton btnSalir = crearBotonSalir();
        panelHeader.add(btnSalir, BorderLayout.EAST);

        add(panelHeader, BorderLayout.NORTH);

        // --- Panel Central (Tabla de Avisos) ---
        modelo = new DefaultTableModel(new Object[]{"ID", "Título", "Contenido", "Profesor", "Fecha"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(new Color(103, 58, 183));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setSelectionBackground(new Color(126, 103, 195));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setGridColor(new Color(230, 230, 235));
        tabla.setBackground(Color.WHITE);

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollTabla, BorderLayout.CENTER);

        // --- Panel Izquierdo (Formulario) ---
        JPanel panelFormulario = crearPanelFormulario();
        add(panelFormulario, BorderLayout.WEST);

        // --- Panel Inferior (Botones de Acción) ---
        JPanel panelBotones = crearPanelBotones();
        add(panelBotones, BorderLayout.SOUTH);

        cargarAvisos();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(248, 249, 252));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(103, 58, 183)),
            BorderFactory.createEmptyBorder(25, 22, 25, 22)
        ));
        panel.setPreferredSize(new Dimension(350, 0));

        // Título del formulario con icono
        JLabel lblFormulario = new JLabel("Crear Nuevo Aviso");
        lblFormulario.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormulario.setForeground(new Color(103, 58, 183));
        panel.add(lblFormulario);
        panel.add(Box.createVerticalStrut(18));

        // Línea divisora
        JSeparator sep1 = new JSeparator();
        sep1.setForeground(new Color(200, 200, 200));
        panel.add(sep1);
        panel.add(Box.createVerticalStrut(15));

        // Campo: Título
        panel.add(crearEtiqueta("Título del Aviso"));
        txtTitulo = new JTextField();
        txtTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtTitulo.setPreferredSize(new Dimension(280, 38));
        txtTitulo.setMaximumSize(new Dimension(280, 38));
        txtTitulo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 230), 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtTitulo.setBackground(Color.WHITE);
        panel.add(txtTitulo);
        panel.add(Box.createVerticalStrut(15));

        // Campo: Contenido
        panel.add(crearEtiqueta("Contenido del Aviso"));
        txtContenido = new JTextArea(6, 24);
        txtContenido.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtContenido.setLineWrap(true);
        txtContenido.setWrapStyleWord(true);
        txtContenido.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 230), 2),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        txtContenido.setBackground(Color.WHITE);
        JScrollPane scrollContenido = new JScrollPane(txtContenido);
        scrollContenido.setPreferredSize(new Dimension(280, 140));
        scrollContenido.setMaximumSize(new Dimension(280, 140));
        scrollContenido.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 230), 2));
        panel.add(scrollContenido);
        panel.add(Box.createVerticalStrut(18));

        // Botón Agregar
        JButton btnAgregar = new JButton("Agregar Aviso");
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAgregar.setBackground(new Color(76, 175, 80));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        btnAgregar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAgregar.setMaximumSize(new Dimension(280, 45));
        btnAgregar.addActionListener(e -> agregarAviso());
        btnAgregar.setOpaque(true);
        panel.add(btnAgregar);

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 18));
        panel.setBackground(new Color(248, 249, 252));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnActualizar.setBackground(new Color(63, 81, 181));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        btnActualizar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnActualizar.setPreferredSize(new Dimension(140, 42));
        btnActualizar.addActionListener(e -> actualizarAviso());
        panel.add(btnActualizar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEliminar.setBackground(new Color(229, 57, 53));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        btnEliminar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEliminar.setPreferredSize(new Dimension(140, 42));
        btnEliminar.addActionListener(e -> eliminarAviso());
        panel.add(btnEliminar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpiar.setBackground(new Color(158, 158, 158));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        btnLimpiar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLimpiar.setPreferredSize(new Dimension(140, 42));
        btnLimpiar.addActionListener(e -> limpiarCampos());
        panel.add(btnLimpiar);

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

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(60, 70, 90));
        return lbl;
    }

    private void cargarAvisos() {
        modelo.setRowCount(0);
        List<Aviso> avisos = dao.listarAvisos();
        for (Aviso a : avisos) {
            modelo.addRow(new Object[]{
                a.getId(),
                a.getTitulo(),
                truncarTexto(a.getContenido(), 50),
                a.getNombreProfesor(),
                a.getFechaPublicacion()
            });
        }
    }

    private String truncarTexto(String texto, int longitud) {
        if (texto == null) return "";
        return texto.length() > longitud ? texto.substring(0, longitud) + "..." : texto;
    }

    private int obtenerIdProfesor(String nombreCompleto) throws SQLException {
        String sql = "SELECT id_profesor FROM profesor WHERE CONCAT(nombre_profesor, ' ', apellido_profesor) = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreCompleto);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id_profesor");
            throw new SQLException("Profesor no encontrado: " + nombreCompleto);
        }
    }

    private void agregarAviso() {
        try {
            String titulo = txtTitulo.getText().trim();
            String contenido = txtContenido.getText().trim();

            if (titulo.isEmpty() || contenido.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor completa todos los campos.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idProfesor = obtenerIdProfesor(usuarioNombre);
            Aviso aviso = new Aviso(0, titulo, contenido, null, idProfesor, usuarioNombre);
            
            if (dao.agregarAviso(aviso)) {
                JOptionPane.showMessageDialog(this, "✅ Aviso agregado correctamente.", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarAvisos();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo agregar el aviso.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarAviso() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un aviso para actualizar.", 
                "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = (int) modelo.getValueAt(fila, 0);
            String titulo = txtTitulo.getText().trim();
            String contenido = txtContenido.getText().trim();

            if (titulo.isEmpty() || contenido.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor completa todos los campos.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idProfesor = obtenerIdProfesor(usuarioNombre);
            Aviso aviso = new Aviso(id, titulo, contenido, null, idProfesor, usuarioNombre);
            
            if (dao.actualizarAviso(aviso)) {
                JOptionPane.showMessageDialog(this, "✅ Aviso actualizado correctamente.", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarAvisos();
                limpiarCampos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarAviso() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un aviso para eliminar.", 
                "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar este aviso?", 
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (dao.eliminarAviso(id)) {
                    JOptionPane.showMessageDialog(this, "✅ Aviso eliminado correctamente.", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarAvisos();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarCampos() {
        txtTitulo.setText("");
        txtContenido.setText("");
        tabla.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProfesorPortalGUI("Juan Pérez").setVisible(true));
    }
}
