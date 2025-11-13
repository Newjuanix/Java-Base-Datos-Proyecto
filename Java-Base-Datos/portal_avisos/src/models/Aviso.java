package models;

public class Aviso {
    private int id;
    private String titulo;
    private String contenido;
    private String fechaPublicacion;
    private int idProfesor;
    private String nombreProfesor;

    // 🔹 Constructor para insertar avisos (sin nombreProfesor)
    public Aviso(int id, String titulo, String contenido, String fechaPublicacion, int idProfesor) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fechaPublicacion = fechaPublicacion;
        this.idProfesor = idProfesor;
    }

    // 🔹 Constructor extendido (para listar con nombre de profesor)
    public Aviso(int id, String titulo, String contenido, String fechaPublicacion, int idProfesor, String nombreProfesor) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fechaPublicacion = fechaPublicacion;
        this.idProfesor = idProfesor;
        this.nombreProfesor = nombreProfesor;
    }

    // 🔹 Constructor vacío
    public Aviso() {}

    // Getters y Setters (sin guiones bajos)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }
}
