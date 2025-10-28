package dataLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConexion {

    public static Connection GetConexion() throws SQLException {
        Connection Conn = null;
        String cadena = "jdbc:sqlserver://localhost:1433;database=Biblioteca" +
                ";user=sa;password=hola;loginTimeout=30;"+
                "trustServerCertificate=true";
        try{
            Conn = DriverManager.getConnection(cadena);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally{
            return Conn;
        }
    }
}
