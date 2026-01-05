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


public class ConnectionDB {
    
    static final String driver = "com.mysql.cj.jdbc.Driver"; 
    static final String dataBase = "jdbc:mysql://localhost/hmgestion_data_base";
    
    
    public Connection establecerConexion() {
        
        Connection con = null;
        
        try{
            con = DriverManager.getConnection(dataBase, "usuario", "123456" );
            
        }
        catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error al conectarse con la base de datos.");
        }
        return con;
    }  

    public Statement createStatement() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
