package ui;

import database.Conexion;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class LoginGUI extends JFrame {
    private final JTextField txtCorreo;
    private final JButton btnEntrar;

    public LoginGUI() {
        setTitle("Portal de Avisos Universitario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);

        // Panel gradiente
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(103, 58, 183),
                    getWidth(), getHeight(), new Color(57, 73, 171)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelFondo.setLayout(new GridBagLayout());
        add(panelFondo);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 30, 15, 30);

        // Logo/Título principal
        JLabel lblTitulo = new JLabel("Portal de Avisos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(40, 30, 10, 30);
        panelFondo.add(lblTitulo, gbc);

        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Bienvenido a tu portal universitario");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(220, 220, 255));
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 30, 40, 30);
        panelFondo.add(lblSubtitulo, gbc);

        // Panel blanco para el formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(30, 25, 30, 25));
        panelFormulario.setPreferredSize(new Dimension(380, 220));
        
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.gridx = 0;
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        gbcForm.insets = new Insets(12, 0, 12, 0);

        // Etiqueta
        JLabel lblCorreo = new JLabel("Correo Institucional");
        lblCorreo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCorreo.setForeground(new Color(50, 50, 50));
        gbcForm.gridy = 0;
        gbcForm.weightx = 1;
        panelFormulario.add(lblCorreo, gbcForm);

        // Campo de correo
        txtCorreo = new JTextField();
        txtCorreo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCorreo.setPreferredSize(new Dimension(300, 45));
        txtCorreo.setBackground(new Color(245, 245, 245));
        txtCorreo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        gbcForm.gridy = 1;
        panelFormulario.add(txtCorreo, gbcForm);

        // Botón
        btnEntrar = new JButton("INGRESAR") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(103, 58, 183),
                    getWidth(), getHeight(), new Color(63, 81, 181)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                
                super.paintComponent(g);
            }
        };
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setBackground(new Color(103, 58, 183));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        btnEntrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEntrar.setOpaque(false);
        btnEntrar.setBorderPainted(false);
        btnEntrar.setContentAreaFilled(false);
        gbcForm.gridy = 2;
        gbcForm.insets = new Insets(25, 0, 0, 0);
        panelFormulario.add(btnEntrar, gbcForm);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 30, 30, 30);
        gbc.weightx = 1;
        panelFondo.add(panelFormulario, gbc);

        // Acciones
        btnEntrar.addActionListener(e -> autenticar());
        txtCorreo.addActionListener(e -> autenticar());
    }

    private void autenticar() {
        String correo = txtCorreo.getText().trim();
        if (correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa tu correo institucional.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = Conexion.getConnection()) {
            // 1️⃣ Buscar en tabla profesor
            String sqlProfesor = "SELECT CONCAT(nombre_profesor, ' ', apellido_profesor) AS nombre FROM profesor WHERE correo_profesor = ?";
            PreparedStatement stmtP = conn.prepareStatement(sqlProfesor);
            stmtP.setString(1, correo);
            ResultSet rsP = stmtP.executeQuery();

            if (rsP.next()) {
                String nombre = rsP.getString("nombre");
                dispose();
                SwingUtilities.invokeLater(() -> new ProfesorPortalGUI(nombre).setVisible(true));
                return;
            }

            // 2️⃣ Buscar en tabla estudiante
            String sqlEstudiante = "SELECT CONCAT(nombre_estudiante, ' ', apellido_estudiante) AS nombre FROM estudiante WHERE correo_estudiante = ?";
            PreparedStatement stmtE = conn.prepareStatement(sqlEstudiante);
            stmtE.setString(1, correo);
            ResultSet rsE = stmtE.executeQuery();

            if (rsE.next()) {
                String nombre = rsE.getString("nombre");
                dispose();
                SwingUtilities.invokeLater(() -> new EstudiantePortalGUI(nombre).setVisible(true));
                return;
            }

            // 3️⃣ Si no está en ninguna tabla
            JOptionPane.showMessageDialog(this, "Correo no registrado como profesor ni estudiante.", "Error", JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al autenticar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }
}

