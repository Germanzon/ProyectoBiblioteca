package dataLayer;

public class Prestamos {

    private int id_Prestamo;
    private String fechaPrestamo, fechaDevolucion, estado;
    private Usuarios ID_Usuario;
    private Libros ID_Libro;

    public Prestamos(int id_Prestamo, Usuarios ID_Usuario, Libros ID_Libro, String fechaPrestamo, String fechaDevolucion, String estado) {
        this.id_Prestamo = id_Prestamo;
        this.ID_Usuario = ID_Usuario;
        this.ID_Libro = ID_Libro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
    }

    public int getId_Prestamo() {
        return id_Prestamo;
    }

    public void setId_Prestamo(int id_Prestamo) {
        this.id_Prestamo = id_Prestamo;
    }

    public int getID_Usuario() {
        return ID_Usuario.getId_Usuario();
    }

    public void setID_Usuario(Usuarios ID_Usuario) {
        this.ID_Usuario = ID_Usuario;
    }

    public int getID_Libro() {
        return ID_Libro.getId_Libro();
    }

    public void setID_Libro(Libros ID_Libro) {
        this.ID_Libro = ID_Libro;
    }

    public String getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(String fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public String getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(String fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
