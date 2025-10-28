package dataLayer;

public class Prestamos {

    private int id_Prestamo;
    private String fechaPrestamo, fechaDevolucion, estado;
    private Usuarios ID_Usuario;
    private Libros ID_Libro;

    public Prestamos(int id_Prestamo, String fechaPrestamo, String fechaDevolucion, String estado) {
        this.id_Prestamo = id_Prestamo;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
    }

    public Prestamos(){
        this(0, "", "", "");
    }

    public int getId_Prestamo() {
        return id_Prestamo;
    }

    public void setId_Prestamo(int id_Prestamo) {
        this.id_Prestamo = id_Prestamo;
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
