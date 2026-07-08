/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import ConnectionDB.connectionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class genericDAO {
    
    connectionDAO Connection = new connectionDAO();
    
    public void mensajeError(){
        JOptionPane.showMessageDialog(null, "Error comunicarse con el administrador!");
    }
    
    public DefaultTableModel crearModeloNoEditable() {
        return new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
    
    //VERIFICA SI EXISTE EL NOMBRE
    public boolean nameExists(String name , String tabla) {
        
        String sql = "SELECT COUNT(*) FROM "+ tabla +" WHERE name = ?";
    
        Connection conexion = Connection.getConnection();
    
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
            JOptionPane.showMessageDialog(null, "ERROR" + e.getMessage());
        }
        return false;
    }  
    
    //DEVUELVE EL VALOR CONTRARIO AL DEL ESTADO DE UNA CATEGORIA O SUBCATEGORIA
    public static int verificarState(String tabla, String name){
        
        String sql = "SELECT `state` FROM "+ tabla +" WHERE `name`= ?";
        int estado = 0;
        
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
           
           while(rs.next()){
               
               if (rs.getString("state").equals("0")) {
                    estado = 1;
                } else {
                    estado = 0;
                }
           } 
           
           conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR" + e.getMessage());
        }
       return estado; 
    }
    
    //ACTUALIZA EL ESTADO DE UNA CATEGORIA O SUBCATEGORIA
    public void updateState(String tabla, String name, int state){
        
        String sql = "UPDATE "+ tabla +" SET state = "+ state +" WHERE name = ?";
        
        Connection conexion = Connection.getConnection();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);         
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR" + e.getMessage());
        }
    }
    
    //ACTUALIZA EL NOMBRE DE UNA CATEGORIA O SUBCATEGORIA
    public void updateName(String tabla, String oldName, String newName){

        String sql = "UPDATE " + tabla + " SET `name` = ? WHERE `name` = ?";

        Connection conexion = Connection.getConnection();

        try{
            PreparedStatement pstmt = conexion.prepareStatement(sql); 

            pstmt.setString(1, newName);
            pstmt.setString(2, oldName);

            pstmt.executeUpdate();

            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR " + e.getMessage());
        }
    }
    
    public void llenarCombos(JComboBox combo, String tableDB){

        String sql = "SELECT * FROM " + tableDB + " ORDER BY name ASC";

        Connection conexion = Connection.getConnection();

        combo.removeAllItems();
        combo.addItem("Seleccione una opción");

        try{
            PreparedStatement pstmt = conexion.prepareStatement(sql);  
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                combo.addItem(rs.getString("name"));
            }       

            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
    }
    
    public void llenarCombosActivos(JComboBox combo, String table){

        String sql = "SELECT * FROM " + table + " WHERE state = 1 ORDER BY name ASC";

        Connection conexion = Connection.getConnection();

        combo.addItem("Seleccione una opción");

        try{
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                combo.addItem(rs.getString("name"));
            }             

            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR " + e.getMessage());
        }
    }
    
    public static int selectId(String nameId, String tabla, String name){
        
        String sql="SELECT "+ nameId +" FROM "+ tabla +" WHERE name = ?";
              
        String nId = nameId;
        int id=0;     
        
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();   

            while(rs.next()){
                id =(rs.getInt(nId));            
            }
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR" + e.getMessage());
        }
       return id; 
    }
    

    

    
}
