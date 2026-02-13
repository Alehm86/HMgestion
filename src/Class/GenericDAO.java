/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import ConnectionDB.ConnectionDB;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class GenericDAO {
    
    //AGREGA PLACEHOLDER A CAJAS DE TEXTO ---- COLOR NEGRO.
    public void agregarPlaceholderN(JTextField campo, String placeholder) {
        campo.setForeground(Color.BLACK);
        campo.setText(placeholder);

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setForeground(Color.GRAY);
                    campo.setText(placeholder);
                }
            }
        });
    }    
    
    //AGREGA PLACEHOLDER A CAJAS DE TEXTO ---- COLOR NEGRO.
    public void agregarPlaceholderR(JTextField campo, String placeholder) {
        campo.setForeground(new Color(255, 102, 51));
        campo.setText(placeholder);

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setForeground(Color.GRAY);
                    campo.setText(placeholder);
                }
            }
        });
    }     
    
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
    
    //OBTIENE LA FECHA 
    public static String fecha(){
        LocalDate fecha = LocalDate.now();
        String fechaString = fecha.format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
        );
        return fechaString;
    }
    
    public void clearMsjErrorTxt(JTextField txt,JLabel label){
                txt.getDocument().addDocumentListener(new DocumentListener() {

            private void limpiar() {
                label.setText("");
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                limpiar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                limpiar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                limpiar();
            }
        });
        
    }
    
    public void clearMsjErrorCombo(JComboBox combo, JLabel label){
        combo.addActionListener(e-> {
            if (combo.getSelectedIndex() > 0) { 
                label.setText("");
            }
        });
    }
    
}
