package dataLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DAOPrestamos extends DBConexion{

    //Insertar nuevos Prestamos
    public static void Insertar(Usuarios regUsuarios, Libros regLibros) throws Exception {
        Connection con = GetConexion();
        String Select = "INSERT INTO Prestamos (ID_Usuario, ID_Libro)" + "VALUES (?,?)";
        PreparedStatement ps = con.prepareStatement(Select);
        ps.setInt(1, regUsuarios.getId_Usuario());
        ps.setInt(2, regLibros.getId_Libro());
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    //Actualizar Prestamos (Devoluciones)
    public static void Devolver(Prestamos reg) throws Exception {
        Connection con = GetConexion();
        String Select = "UPDATE Prestamos SET estado = 'Devuelto' WHERE ID_Prestamo = ? ";
        PreparedStatement ps = con.prepareStatement(Select);
        ps.setInt(1, reg.getId_Prestamo());
        ps.executeUpdate();
        ps.close();
        con.close();
    }

}
