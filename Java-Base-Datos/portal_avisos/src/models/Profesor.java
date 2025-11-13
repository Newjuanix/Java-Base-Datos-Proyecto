package models;

public class Profesor {
    private int idProfesor;
    private String nombreProfesor;
    private String apellidoProfesor;
    private String correoProfesor;

    // Getters y Setters
    public int getIdProfesor() { return idProfesor; }
    public void setIdProfesor(int idProfesor) { this.idProfesor = idProfesor; }

    public String getNombreProfesor() { return nombreProfesor; }
    public void setNombreProfesor(String nombreProfesor) { this.nombreProfesor = nombreProfesor; }

    public String getApellidoProfesor() { return apellidoProfesor; }
    public void setApellidoProfesor(String apellidoProfesor) { this.apellidoProfesor = apellidoProfesor; }

    public String getCorreoProfesor() { return correoProfesor; }
    public void setCorreoProfesor(String correoProfesor) { this.correoProfesor = correoProfesor; }
}
