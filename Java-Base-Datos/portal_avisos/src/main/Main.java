package main;
import dao.AvisoDAO;
import models.Aviso;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        AvisoDAO dao = new AvisoDAO();

        try {
            // Crear aviso
            dao.agregarAviso(new Aviso(0, "Recordatorio de examen", "El parcial es el lunes", null, 1));
            System.out.println("✅ Aviso agregado correctamente.");

            // Listar avisos
            List<Aviso> avisos = dao.listarAvisos();
            System.out.println("📋 Avisos actuales:");
            for (Aviso a : avisos) {
                System.out.println(a.getTitulo() + " → " + a.getContenido());
            }

            // Actualizar aviso (opcional)
            if (!avisos.isEmpty()) {
                Aviso a = avisos.get(0);
                a.setTitulo("Recordatorio de examen (actualizado)");
                a.setContenido("Fecha cambiada al martes");
                dao.actualizarAviso(a);
                System.out.println("♻️ Aviso actualizado.");
            }

            // Eliminar aviso (opcional)
            // dao.eliminarAviso(1);
            // System.out.println("🗑 Aviso eliminado.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

