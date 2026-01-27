/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import ConnectionDB.ConnectionDB;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.security.Timestamp;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;


public class ClientDAO {
    
//    REGISTRAR CLIENTE NUEVO
    public void insertClient(String name,int tipeClient, int CUIT, String email,int phone, String street, int height, int idProvince, String fechaRegistro, int state,int iva){
        
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        String sql="INSERT INTO `client`(`name`, `tipeClient`, `CUIT`, `email`, `phone`, `street`, `height`, `idProvince`, `fechaRegistro`, `state`, `iva`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, tipeClient);
            pstmt.setInt(3, CUIT);
            pstmt.setString(4, email);
            pstmt.setInt(5, phone);
            pstmt.setString(6, street);
            pstmt.setInt(7, height);
            pstmt.setInt(8, idProvince);
            pstmt.setString(9, fechaRegistro);
            pstmt.setInt(10, state);
            pstmt.setInt(11, iva);
            pstmt.executeUpdate();            
            conexion.close();           
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR CLIENTE");
        }

    }
    
}
