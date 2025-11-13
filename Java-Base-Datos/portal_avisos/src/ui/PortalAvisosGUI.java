package ui;

import com.formdev.flatlaf.FlatDarkLaf;
import dao.AvisoDAO;
import database.Conexion;
import java.awt.*;
import java.sql.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Aviso;

public class PortalAvisosGUI extends JFrame {

    private JTextField txtProfesor, txtTitulo;
    private JTextArea txtContenido;
    private JTable tabla;
    private DefaultTableModel modelo;
    private AvisoDAO dao = new AvisoDAO();

    private String usuarioNombre;
    private String usuarioRol;

    // 🔹 Constructor
    public PortalAvisosGUI(String nombre, String rol) {
        this.usuarioNombre = nombre;
        this.usuarioRol = rol;

        setTitle("📢 Portal de Avisos Universitario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // --- Estilos generales ---
        Font fuente = new Font("Segoe UI", Font.PLAIN, 15);
        UIManager.put("Label.font", fuente);
        UIManager.put("Button.font", fuente);
        UIManager.put("Table.font", fuente);
        UIManager.put("TableHeader.font", new Font("Segoe UI Semibold", Font.BOLD, 14));

        // --- Tabla ---
        modelo = new DefaultTableModel(new Object[]{"ID", "Profesor", "Título", "Contenido", "Fecha"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.getTableHeader().setBackground(new Color(25, 118, 210));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setSelectionBackground(new Color(33, 150, 243));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setGridColor(new Color(60, 63, 65));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(new Color(43, 43, 43));
        add(scroll, BorderLayout.CENTER);

        // Si es profesor
        if (rol.equalsIgnoreCase("profesor")) {
            JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 10));
            panelForm.setBorder(BorderFactory.createTitledBorder("📝 Nuevo aviso"));
            panelForm.setBackground(new Color(43, 43, 43));

            panelForm.add(etiqueta("Profesor:"));
            txtProfesor = new JTextField(nombre);
            estiloCampo(txtProfesor);
            panelForm.add(txtProfesor);

            panelForm.add(etiqueta("Título:"));
            txtTitulo = new JTextField();
            estiloCampo(txtTitulo);
            panelForm.add(txtTitulo);

            panelForm.add(etiqueta("Contenido:"));
            txtContenido = new JTextArea(3, 20);
            estiloArea(txtContenido);
            panelForm.add(new JScrollPane(txtContenido));

            // --- Botones ---
            JButton btnAgregar = botonAzul("Agregar");
            JButton btnActualizar = botonAzul("Actualizar");
            JButton btnEliminar = botonRojo("Eliminar");

            JPanel panelBotones = new JPanel();
            panelBotones.setBackground(new Color(43, 43, 43));
            panelBotones.add(btnAgregar);
            panelBotones.add(btnActualizar);
            panelBotones.add(btnEliminar);

            add(panelForm, BorderLayout.NORTH);
            add(panelBotones, BorderLayout.SOUTH);

            // Acciones
            btnAgregar.addActionListener(e -> agregarAviso());
            btnActualizar.addActionListener(e -> actualizarAviso());
            btnEliminar.addActionListener(e -> eliminarAviso());

            setTitle("Portal de Avisos - Profesor: " + nombre);

        } else {
            // Vista para estudiante
            setTitle("Portal de Avisos - Estudiante: " + nombre);
        }

        cargarAvisos();
    }

    // --- Métodos de estilo ---
    private JLabel etiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    private void estiloCampo(JTextField campo) {
        campo.setBackground(new Color(60, 63, 65));
        campo.setForeground(Color.WHITE);
        campo.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
    }

    private void estiloArea(JTextArea area) {
        area.setBackground(new Color(60, 63, 65));
        area.setForeground(Color.WHITE);
        area.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
    }

    private JButton botonAzul(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(new Color(33, 150, 243));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(130, 40));
        return b;
    }

    private JButton botonRojo(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(new Color(229, 57, 53));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(130, 40));
        return b;
    }

    // --- Métodos funcionales ---
    private void cargarAvisos() {
        modelo.setRowCount(0);
        List<Aviso> avisos = dao.listarAvisos();
        for (Aviso a : avisos) {
            modelo.addRow(new Object[]{
                    a.getId(),
                    a.getNombreProfesor(),
                    a.getTitulo(),
                    a.getContenido(),
                    a.getFechaPublicacion()
            });
        }
    }

    private int obtenerIdProfesor(String nombreCompleto) throws SQLException {
        String sql = "SELECT id_profesor FROM profesor WHERE CONCAT(nombre_profesor, ' ', apellido_profesor) = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreCompleto);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id_profesor");
            throw new SQLException("No se encontró el profesor con nombre: " + nombreCompleto);
        }
    }

    private void agregarAviso() {
        try {
            String profesor = txtProfesor.getText();
            String titulo = txtTitulo.getText();
            String contenido = txtContenido.getText();

            if (profesor.isEmpty() || titulo.isEmpty() || contenido.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor completa todos los campos.");
                return;
            }

            int idProfesor = obtenerIdProfesor(profesor);
            Aviso aviso = new Aviso(0, titulo, contenido, null, idProfesor, profesor);
            if (dao.agregarAviso(aviso)) {
                JOptionPane.showMessageDialog(this, "✅ Aviso agregado correctamente.");
                cargarAvisos();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ No se pudo agregar el aviso.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void actualizarAviso() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un aviso para actualizar.");
            return;
        }
        try {
            int id = (int) modelo.getValueAt(fila, 0);
            String profesor = txtProfesor.getText();
            String titulo = txtTitulo.getText();
            String contenido = txtContenido.getText();
            int idProfesor = obtenerIdProfesor(profesor);

            Aviso aviso = new Aviso(id, titulo, contenido, null, idProfesor, profesor);
            if (dao.actualizarAviso(aviso)) {
                JOptionPane.showMessageDialog(this, "♻️ Aviso actualizado correctamente.");
                cargarAvisos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
        }
    }

    private void eliminarAviso() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un aviso para eliminar.");
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este aviso?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (dao.eliminarAviso(id)) {
                    JOptionPane.showMessageDialog(this, "🗑 Aviso eliminado correctamente.");
                    cargarAvisos();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void limpiarCampos() {
        txtTitulo.setText("");
        txtContenido.setText("");
    }

    public static void main(String[] args) {
        try {
            FlatDarkLaf.setup(); // Tema oscuro moderno
        } catch (Exception e) {
            System.err.println("Error al aplicar FlatLaf: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() ->
                new PortalAvisosGUI("Juan Pérez", "profesor").setVisible(true)
        );
    }
}
