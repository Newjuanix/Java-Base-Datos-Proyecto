package dao;

import database.Conexion;
import models.Aviso;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvisoDAO {

    // 🔹 Crear (INSERT)
    public boolean agregarAviso(Aviso aviso) { // ← cambiado el nombre para coincidir con Main.java
        String sql = "INSERT INTO avisos (titulo, contenido, id_profesor) VALUES (?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, aviso.getTitulo());
            ps.setString(2, aviso.getContenido());
            ps.setInt(3, aviso.getIdProfesor());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Leer (SELECT con JOIN)
    public List<Aviso> listarAvisos() {
        List<Aviso> lista = new ArrayList<>();

        String sql = "SELECT a.id_aviso, a.titulo, a.contenido, a.fecha_publicacion, " +
                     "p.id_profesor, CONCAT(p.nombre_profesor, ' ', p.apellido_profesor) AS nombre_profesor " +
                     "FROM avisos a " +
                     "JOIN profesor p ON a.id_profesor = p.id_profesor " +
                     "ORDER BY a.fecha_publicacion DESC";

        try (Connection con = Conexion.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Aviso aviso = new Aviso();
                aviso.setId(rs.getInt("id_aviso"));
                aviso.setTitulo(rs.getString("titulo"));
                aviso.setContenido(rs.getString("contenido"));
                aviso.setFechaPublicacion(rs.getString("fecha_publicacion"));
                aviso.setIdProfesor(rs.getInt("id_profesor"));
                aviso.setNombreProfesor(rs.getString("nombre_profesor"));
                lista.add(aviso);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // 🔹 Leer por ID (para editar)
    public Aviso obtenerAvisoPorId(int id) {
        Aviso aviso = null;
        String sql = "SELECT a.id_aviso, a.titulo, a.contenido, a.fecha_publicacion, " +
                     "p.id_profesor, CONCAT(p.nombre_profesor, ' ', p.apellido_profesor) AS nombre_profesor " +
                     "FROM avisos a " +
                     "JOIN profesor p ON a.id_profesor = p.id_profesor " +
                     "WHERE a.id_aviso = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                aviso = new Aviso();
                aviso.setId(rs.getInt("id_aviso"));
                aviso.setTitulo(rs.getString("titulo"));
                aviso.setContenido(rs.getString("contenido"));
                aviso.setFechaPublicacion(rs.getString("fecha_publicacion"));
                aviso.setIdProfesor(rs.getInt("id_profesor"));
                aviso.setNombreProfesor(rs.getString("nombre_profesor"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return aviso;
    }

    // 🔹 Actualizar (UPDATE)
    public boolean actualizarAviso(Aviso aviso) {
        String sql = "UPDATE avisos SET titulo = ?, contenido = ?, id_profesor = ? WHERE id_aviso = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, aviso.getTitulo());
            ps.setString(2, aviso.getContenido());
            ps.setInt(3, aviso.getIdProfesor());
            ps.setInt(4, aviso.getId());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Eliminar (DELETE)
    public boolean eliminarAviso(int id) {
        String sql = "DELETE FROM avisos WHERE id_aviso = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
