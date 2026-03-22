/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConnectionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;


public class connectionDB {
    
//    static final String driver = "com.mysql.cj.jdbc.Driver"; 
//    static final String dataBase = "jdbc:mysql://localhost/hmgestion_data_base";
//
//    
//    
//    public Connection establecerConexion() {
//        
//        Connection con = null;
//        
//        try{
//            con = DriverManager.getConnection(dataBase, "hm_user", "G7k9R2mQ" );
//            
//        }
//        catch (SQLException e){
//            JOptionPane.showMessageDialog(null, "Error al conectarse con la base de datos.");
//        }
//        return con;
//    }  
//
//    public Statement createStatement() {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//    
    
    private static final String URL = "jdbc:mysql://localhost:3306/hmgestion_data_base?useSSL=false&serverTimezone=UTC";
    private static final String USER = "hm_user";
    private static final String PASSWORD = "G7k9R2mQ";

    public Connection establecerConexion() {

        try {
            // Cargar el driver explícitamente (más seguro en algunos entornos)
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Conexión exitosa a la base de datos.");
            return con;

        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, 
                "No se encontró el driver JDBC.\n" + e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error al conectarse con la base de datos.\n" + e.getMessage());
        }

        return null;
    }
}
