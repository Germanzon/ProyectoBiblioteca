package dataLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
        String Select = "UPDATE Usuarios SET Nombre = ?, Apellido = ?," + "Correo = ?, Telefono = ? where ID_Usuario = ?";
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
        String select = "DELETE FROM Usuarios WHERE ID_Usuario = ?";
        PreparedStatement ps = con.prepareStatement(select);
        ps.setInt(1, id_Usuario);
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    //Metodo auxiliar Buscar Usuario por Nombre
    public static Usuarios BuscarUsuarioPorNombre(String nombre, String apellido) throws Exception {
        Connection con = GetConexion();
        String select = "SELECT ID_Usuario FROM Usuarios WHERE nombre = ? AND apellido = ?";
        PreparedStatement ps = con.prepareStatement(select);
        ps.setString(1, nombre);
        ps.setString(2, apellido);
        ResultSet rs = ps.executeQuery();

        Usuarios usuario = null;
        if (rs.next()) {
            usuario = new Usuarios();
            usuario.setId_Usuario(rs.getInt("ID_Usuario"));
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
        }

        rs.close();
        ps.close();
        con.close();
        return usuario;
    }
}
