package main;
import database.Conexion;
import java.sql.Connection;
import java.sql.SQLException;

public class TestConexion {
    public static void main(String[] args) {
        try (Connection conn = Conexion.getConnection()) {
            System.out.println("✅ Conectado correctamente a MySQL");
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}
