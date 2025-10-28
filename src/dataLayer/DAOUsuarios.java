package dataLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DAOUsuarios extends DBConexion{

    //Insertar nuevos usuarios
    public static void Insertar(Usuarios reg) throws Exception {
        Connection con = GetConexion();
        String Select = "INSERT INTO Usuarios (nombre,apellido,correo,telefono)" + "VALUES (?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(Select);

        ps.setString(1, reg.getNombre());
        ps.setString(2, reg.getApellido());
        ps.setString(3, reg.getCorreo());
        ps.setString(4, reg.getTelefono());
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    //Actualizar un usuario
    public static void Actualizar(Usuarios reg) throws Exception {
        Connection con = GetConexion();
        String Select = "UPDATE Usuarios SET Nombre = ?, Apellido = ?," + "Correo = ?, Telefono = ? where ID = ?";
        PreparedStatement ps = con.prepareStatement(Select);
        ps.setString(1, reg.getNombre());
        ps.setString(2, reg.getApellido());
        ps.setString(3, reg.getCorreo());
        ps.setString(4, reg.getTelefono());
        ps.setInt(5, reg.getId_Usuario());
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    //Eliminar usuarios
    public static void Eliminar(int id_Usuario) throws Exception {
        Connection con = GetConexion();
        String select = "DELETE FROM Usuarios WHERE ID = ?";
        PreparedStatement ps = con.prepareStatement(select);
        ps.setInt(1, id_Usuario);
        ps.executeUpdate();
        ps.close();
        con.close();
    }
}
