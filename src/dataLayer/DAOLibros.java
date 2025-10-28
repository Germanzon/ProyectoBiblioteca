package dataLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DAOLibros extends DBConexion {

    //Insertar nuevos libros
    public static void Insertar(Libros reg) throws Exception {
        Connection con = GetConexion();
        String Select = "INSERT INTO Libros (titulo,autor,anio_publicacion,disponibilidad)" +
                "VALUES (?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(Select);

        ps.setString(1, reg.getTitulo());
        ps.setString(2, reg.getAutor());
        ps.setInt(3, reg.getAnio_pubicacion());
        ps.setString(4, reg.getDisponibilidad());
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    //Actualizar un usuario
    public static void Actualizar(Libros reg) throws Exception {
        Connection con = GetConexion();
        String Select = "UPDATE Libros SET Titulo = ?, Autor = ?," +
                "Anio_publicacion = ?, Disponibilidad = ? where id_Libro = ?";
        PreparedStatement ps = con.prepareStatement(Select);
        ps.setString(1, reg.getTitulo());
        ps.setString(2, reg.getAutor());
        ps.setInt(3, reg.getAnio_pubicacion());
        ps.setString(4, reg.getDisponibilidad());
        ps.setInt(5, reg.getId_Libro());
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    //Eliminar usuarios
    public static void Eliminar(int id_Libro) throws Exception {
        Connection con = GetConexion();
        String select = "DELETE FROM Libros WHERE id_Libro = ?";
        PreparedStatement ps = con.prepareStatement(select);
        ps.setInt(1, id_Libro);
        ps.executeUpdate();
        ps.close();
        con.close();
    }
}
