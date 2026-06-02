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
import java.sql.Statement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import utils.tableStyleUtil;


public class supplierDAO {
    
    private Connection getConnection() {
        connectionDB con = new connectionDB();
        return con.establecerConexion();
    }
    
    private DefaultTableModel crearModeloNoEditable() {
        return new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
    
    //****************** END: BRANDS || COMIENZA ---> SUPPLIERS ****************

    //LLENAR LISTA EN frmNewSupplier.java
    public void listTableSupplier(JTable jtable){
        
        String sql="SELECT * FROM suppliers ORDER BY name ASC";
        
        Statement stmt;
        
        DefaultTableModel dtm = crearModeloNoEditable();
 
        Connection conexion = getConnection();
        
        String[] titleTSuppliers = new String[]{"Nombre"};
        dtm.setColumnIdentifiers(titleTSuppliers);
        
        try{
           stmt=conexion.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
           
            while(rs.next()){
               
               Object [] lista = {rs.getString(2)};
               dtm.addRow(lista);             
            } 
            jtable.setModel(dtm);
            
            tableStyleUtil.applyPoppinsHeader(jtable);
           
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }  
    
    //BUSCAR DATOS PROVEEDOR PARA LLENAR CAJAS DE TEXTO A EDITAR   
    public void selectSupplier(
            String name,
            JTextField txtName,
            JTextField txtCondicion,
            JTextField txtCuit,
            JTextField txtTelefono,
            JTextField txtEmail,
            JTextField txtUrl,
            JTextField txtUser,
            JTextField txtPassword
        ){

        String sql = "SELECT * FROM suppliers WHERE name = ?";

        Connection conexion = getConnection();

        try {

            PreparedStatement pstm = conexion.prepareStatement(sql);
            pstm.setString(1, name);

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {

                txtName.setText(name);
                txtCondicion.setText(rs.getString("condicion") == null ? "" : rs.getString("condicion"));
                txtCuit.setText(rs.getString("cuit") == null ? "" : rs.getString("cuit"));
                txtTelefono.setText(rs.getString("telefono") == null ? "" : rs.getString("telefono"));
                txtEmail.setText(rs.getString("mail") == null ? "" : rs.getString("mail"));
                txtUrl.setText(rs.getString("url") == null ? "" : rs.getString("url"));
                txtUser.setText(rs.getString("user") == null ? "" : rs.getString("user"));
                txtPassword.setText(rs.getString("pass") == null ? "" : rs.getString("pass"));

            } else {
                JOptionPane.showMessageDialog(null, "No se encontró la entidad con nombre: " + name);
            }

            rs.close();
            pstm.close();
            conexion.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar proveedor: " + e.getMessage());
        }
    }

    public void select_supplier(
            String name,
            JLabel lbl_id,
            JLabel lbl_Iva,
            JLabel lbl_cuit_supplier,
            JLabel lbl_phone_supplier
        ){

        String sql = "SELECT * FROM suppliers WHERE name = ?";

        Connection conexion = getConnection();

        try {

            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                lbl_id.setText(rs.getString("id_supplier"));
                lbl_Iva.setText(rs.getString("condicion") == null ? "" : rs.getString("condicion"));
                lbl_cuit_supplier.setText(rs.getString("cuit") == null ? "" : rs.getString("cuit"));
                lbl_phone_supplier.setText(rs.getString("telefono") == null ? "" : rs.getString("telefono"));

            } else {
                JOptionPane.showMessageDialog(null, "No se encontró la entidad con nombre: " + name);
            }

            rs.close();
            ps.close();
            conexion.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar proveedor: " + e.getMessage());
        }
    }    

   
    //VERIFICA SI EL NOMBRE, CUIT O EMAIL YA ESTAN REGISTRADOS.
    public boolean supplierExists(String nombre, String cuit, String email) {
        
        String sql = "SELECT * FROM suppliers WHERE name = ? OR cuit = ? OR mail = ?";
        
        Connection conexion = getConnection();
        
        try {
            
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, cuit);
            pstmt.setString(3, email);
        
            ResultSet rs = pstmt.executeQuery();
            boolean existe = rs.next();
        
            rs.close();
            pstmt.close();
            conexion.close();
        
            return existe;

        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
    
    //EDITAR PROVEEDOR REGISTRADA EN frmNewSupplier.java
    public void updateSupplier(String oldName,String newName, String condicion, String cuit,String tel,String mail,String url,String user,String pass){
        
        String sql = "UPDATE `suppliers` " +
                    "SET `name`=?, `condicion`=?, `cuit`=?,`telefono`=?,`mail`=?,`url`=?,`user`=?,`pass`=? " + 
                    "WHERE `name`=?";
                  
        Connection conexion = getConnection();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
            pstmt.setString(1, newName);
            pstmt.setString(2, condicion);
            pstmt.setString(3, cuit);
            pstmt.setString(4, tel);
            pstmt.setString(5, mail);
            pstmt.setString(6, url);
            pstmt.setString(7, user);
            pstmt.setString(8, pass);
            pstmt.setString(9, oldName);
            pstmt.executeUpdate();
            
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    //REGISTRAR NUEVO PROVEEDOR EN frmNewSupplier.java
    public void insertSupplier(String name, String condicion, String cuit, String tel, String mail, String url, String user, String pass){
        
        String sql="INSERT INTO `suppliers`(`name`, `condicion`,`cuit`, `telefono`, `mail`, `url`, `user`, `pass`, state) VALUES (?,?,?,?,?,?,?,?,?)";
        
        int state = 1;
        
        Connection conexion = getConnection();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);            
            pstmt.setString(1, name);
            pstmt.setString(2, condicion);
            pstmt.setString(3, cuit);
            pstmt.setString(4, tel);
            pstmt.setString(5, mail);
            pstmt.setString(6, url);
            pstmt.setString(7, user);
            pstmt.setString(8, pass);
            pstmt.setInt(9, state);
            pstmt.executeUpdate();          
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR PROVEEDOR" + e.getMessage());
        }    
    }
      
    public int getIdSupplier(String name){
        
        String sql = "SELECT `id_supplier` FROM `suppliers` WHERE `name` = ?";
        
        int idSupplier = -1;
        
        Connection conexion = getConnection();
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            
            
            
            while(rs.next()){
                idSupplier = (rs.getInt("id_supplier"));            
            }
            
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR" + e.getMessage());
        }
        return idSupplier;
    }
    
}
