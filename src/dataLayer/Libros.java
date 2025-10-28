package dataLayer;

public class Libros {

    private int id_Libro, anio_pubicacion;
    private String titulo, autor, disponibilidad;

    public Libros(int id_Libro, String titulo, String autor, int anio_publicacion, String disponibilidad){
        this.id_Libro = id_Libro;
        this.titulo = titulo;
        this.autor = autor;
        this.anio_pubicacion = anio_publicacion;
        this.disponibilidad = disponibilidad;
    }

    public int getId_Libro() {
        return id_Libro;
    }

    public void setId_Libro(int id_Libro) {
        this.id_Libro = id_Libro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnio_pubicacion() {
        return anio_pubicacion;
    }

    public void setAnio_pubicacion(int anio_pubicacion) {
        this.anio_pubicacion = anio_pubicacion;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

}
