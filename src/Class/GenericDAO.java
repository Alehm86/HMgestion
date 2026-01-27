/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import ConnectionDB.ConnectionDB;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;


public class GenericDAO {
    
     //VERIFICA SI EXISTE EL NOMBRE
    public boolean nameExists(String name , String tabla) {
        String sql = "SELECT COUNT(*) FROM `" + tabla + "` WHERE `name` = ?";
    
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
    
        try {
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
        
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        
            rs.close();
            conexion.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR AL VERIFICAR EXISTENCIA DE NOMBRE");
        }
        return false;
    }  
    
    //DEVUELVE EL VALOR CONTRARIO AL DEL ESTADO DE UNA CATEGORIA O SUBCATEGORIA
    public static int verificarState(String tabla, String name){
        String sql="SELECT `state` FROM `"+tabla+"` WHERE `name`= '"+ name +"'";
        Statement stmt;
        int estado = 0;
        
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        try{
           stmt=conexion.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
           
           while(rs.next()){
               
               if (rs.getString("state").equals("0")) {
                    estado = 1;
                } else {
                    estado = 0;
                }
           }               
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL VERIFICAR ESTADO");
        }
       return estado; 
    }
    
    //ACTUALIZA EL ESTADO DE UNA CATEGORIA O SUBCATEGORIA
    public void updateState(String tabla, String name, int state){
        String sql = "UPDATE `"+tabla+"` SET `state`=? WHERE `name`=?";
        
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
            pstmt.setInt(1, state);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
            
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL ACTUALIZAR ESTADO" + e.getMessage());
        }
    }
    
    //ACTUALIZA EL NOMBRE DE UNA CATEGORIA O SUBCATEGORIA
    public void updateName(String tabla, String oldName, String newName){
        String sql = "UPDATE `"+tabla+"` SET `name`=? WHERE `name`=?";
        
        
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
            pstmt.setString(1, newName);
            pstmt.setString(2, oldName);
            pstmt.executeUpdate();
            
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL ACTUALIZAR ESTADO" + e.getMessage());
        }
    }
    
    public void llenarCombos(JComboBox combo, String table){
        String sql="SELECT * FROM `"+table+"`";
        Statement stmt;
        
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        combo.addItem("Seleccione una categoría");
        
        
        try{
           stmt=conexion.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
           
           while(rs.next()){
               combo.addItem(rs.getString("name"));
           }                  
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR");
        }
    }
    
    public void llenarCombosActivos(JComboBox combo, String table){
        String sql="SELECT * FROM `"+table+"` WHERE `state`='1'";
        Statement stmt;
        
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        combo.addItem("Seleccione una categoría");
        
        
        try{
           stmt=conexion.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
           
           while(rs.next()){
               combo.addItem(rs.getString("name"));
           }                  
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR CARGAR "+table);
        }
    }
    
    public static int selectId(String nameId, String tabla, String name){
        String sql="SELECT `"+ nameId +"` FROM `"+ tabla +"` WHERE `name` = '"+ name +"'";
        Statement stmt;
        String nId=nameId;
        int id=0;       
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        try{
           stmt=conexion.createStatement();
           ResultSet rs = stmt.executeQuery(sql);         
           while(rs.next()){
               id=(rs.getInt(nId));            
           }                 
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR");
        }
       return id; 
    }
}
