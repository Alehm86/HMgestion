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
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class ProductDAO {
   //============================================ CONSULTAS USADAS EN MULTIPLES CASOS ==================================================================    
    
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
    public void editState(String tabla, String name, int state){
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
    public void editName(String tabla, String oldName, String newName){
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
    
    public static int obtenerId(String nameId, String tabla, String name){
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
    
//============================================ END: CONSULTAS USADAS EN MULTIPLES CASOS ==================================================================        
    
//********************************************** BRANDS ***************************************************************************    
    
    //LLENAR LISTA EN frmNewBrand.java
    public void listTableBrands(JTable jtable){
        String sql="SELECT * FROM brands";
        Statement stmt;
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
 
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        String[] titleTBrands = new String[]{"Nombre"};
        dtm.setColumnIdentifiers(titleTBrands);
        
        try{
           stmt=conexion.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
           
            while(rs.next()){
               
                Object [] lista = {rs.getString(2)};
                dtm.addRow(lista);             
            } 
            jtable.setModel(dtm);
            
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR");
        }
    }
    
    public void listComboBrands(JComboBox combo){
        
    }
    //REGISTRAR NUEVA MARCA EN frmNewBrand.java
    public void newBrand(String name){
        
        String sql="INSERT INTO brands(name) VALUE(?)";
        
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);            
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            
            conexion.close();

        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR MARCA");
        }    
    }
    
    //EDITAR MARCA REGISTRADA EN frmNewBrand.java
    public void editBrand(String newName, String oldName){
        String sql = "UPDATE `brands` SET `name` = ? WHERE `name` = ?";
        
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
            JOptionPane.showMessageDialog(null, "ERROR AL EDITAR NOMBRE DE MARCA" + e.getMessage());
        }
    }
       
    
//********************************************** END: BRANDS ***************************************************************************     
    
    
//********************************************** SUPPLIERS ****************************************************************************

//LLENAR LISTA EN frmNewSupplier.java
    public void listTableSupplier(JTable jtable){
        String sql="SELECT * FROM suppliers";
        Statement stmt;
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
 
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
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
           
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR");
        }
    }  
    
    //BUSCAR DATOS PROVEEDOR PARA LLENAR CAJAS DE TEXTO A EDITAR
    public void selectSupplier(
            String name,
            JTextField txtName,
            JTextField txtCuit,
            JTextField txtTelefono,
            JTextField txtEmail,
            JTextField txtUrl,
            JTextField txtUser,
            JTextField txtPassword
            ){
        
        String sql="SELECT * FROM `suppliers` WHERE `name`= '"+name+"'";
        Statement stmt;
        
        
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        try{            
           stmt=conexion.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
           
           if (rs.next()) {
                txtName.setText(name);
                txtCuit.setText(rs.getString("cuit"));
                txtTelefono.setText(rs.getString("telefono"));
                txtEmail.setText(rs.getString("mail"));
                txtUrl.setText(rs.getString("url"));
                txtUser.setText(rs.getString("user"));
                txtPassword.setText(rs.getString("pass"));
            } else {
            JOptionPane.showMessageDialog(null, "No se encontró la entidad con nombre: " + name);
            }
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR");
        } 
    }
   
    //VERIFICA SI EL NOMBRE, CUIT O EMAIL YA ESTAN REGISTRADOS.
    public boolean supplierExists(String nombre, String cuit, String email) {
        String sql = "SELECT * FROM proveedores WHERE nombre = ? OR cuit = ? OR email = ?";
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
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
    public void EditSupplier(String oldName,String newName,String cuit,String tel,String mail,String url,String user,String pass){
        
        String sql = "UPDATE `suppliers` "
                + "SET "
                + "`name`=?,"
                + "`cuit`=?,"
                + "`telefono`=?,"
                + "`mail`=?,"
                + "`url`=?,"
                + "`user`=?,"
                + "`pass`=? "
                + "WHERE "
                + "`name`=?";
                
                
                
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
            pstmt.setString(1, newName);
            pstmt.setString(2, cuit);
            pstmt.setString(3, tel);
            pstmt.setString(4, mail);
            pstmt.setString(5, url);
            pstmt.setString(6, user);
            pstmt.setString(7, pass);
            pstmt.setString(8, oldName);
            pstmt.executeUpdate();
            
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL EDITAR PROVEEDOR" + e.getMessage());
        }
    }
    
    //REGISTRAR NUEVO PROVEEDOR EN frmNewSupplier.java
    public void newSupplier(String name,String cuit,String tel,String mail,String url,String user,String pass){
        
        String sql="INSERT INTO `suppliers`(`name`, `cuit`, `telefono`, `mail`, `url`, `user`, `pass`) VALUES (?,?,?,?,?,?,?)";
        
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);            
            pstmt.setString(1, name);
            pstmt.setString(2, cuit);
            pstmt.setString(3, tel);
            pstmt.setString(4, mail);
            pstmt.setString(5, url);
            pstmt.setString(6, user);
            pstmt.setString(7, pass);
            pstmt.executeUpdate();          
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR PROVEEDOR");
        }    
    }
    
    public boolean nameExistsForOtherId(String name, int id) {
        boolean exists = false;
        String sql = "SELECT 1 FROM suppliers WHERE UPPER(name) = ? AND id_supplier != ? LIMIT 1";
        
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();

        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setString(1, name.toUpperCase());
            pstmt.setInt(2, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                exists = rs.next();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar proveedor duplicado: " + e.getMessage());
        }

        return exists;
    }
    
//********************************************** END: SUPPLIERS ***************************************************************************     
    
    
//********************************************** CATEGORIES ****************************************************************************
  
    //REGISTRAR CATEGORIA NUEVA
    public void newCategory(String name, int state){
        
        String sql="INSERT INTO `categories`(`name`, `state`) VALUES (?,?)";
        
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);            
            pstmt.setString(1, name);
            pstmt.setInt(2, state);
            pstmt.executeUpdate();
            
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR CATEGORIA");
        }    
    }
    
    //LLENAR LISTA EN frmCategories.java CON CATEGORIAS
    public void listTableCategory(JTable jtable){
        String sql="SELECT * FROM `categories`";
        Statement stmt;
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        String estado;
 
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        String[] titleTBrands = new String[]{"Nombre", "Estado"};
        dtm.setColumnIdentifiers(titleTBrands);
        
        try{
           stmt=conexion.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
           
            while(rs.next()){
                
                if (rs.getString("state").equals("0")) {
                    estado = "DESHABLITADO";
                } else {
                    estado = "HABILITADO";
                }
               
                Object [] lista = {rs.getString(2),estado};
                dtm.addRow(lista);             
            } 
            jtable.setModel(dtm);
            
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR");
        }
    }

    public void listAllProdForIDCategory(JTable jtable, int id_category){
        String sql=  "SELECT p.product_code, " +
                     "CONCAT(sc.name, ' ', b.name, ' ', p.model, ' Color ', p.color) AS descripcion, " +
                     "s.name AS proveedor, st.amount, pr.iva, pr.salePrice " +
                     "FROM products p " +
                     "INNER JOIN subcategories sc ON p.id_subcategory = sc.id_subcategory " +
                     "INNER JOIN brands b ON p.id_brand = b.id_brand " +
                     "INNER JOIN suppliers s ON p.id_supplier = s.id_supplier " +
                     "INNER JOIN prices pr ON p.id_product = pr.id_product " +
                     "INNER JOIN stocks st ON p.id_product = st.id_product " +
                     "WHERE p.id_category = ?";
        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        String[] titleTable = {"Código de producto", "Descripción", "Proveedor", "Stock", "IVA", "Precio"};
        dtm.setColumnIdentifiers(titleTable);
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, id_category);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("product_code"),
                    rs.getString("descripcion"),
                    rs.getString("proveedor"),
                    rs.getInt("amount"),
                    rs.getDouble("iva"),
                    rs.getDouble("salePrice")
                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
            
//            jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(150);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(500);
            jtable.getColumnModel().getColumn(2).setPreferredWidth(200);
            jtable.getColumnModel().getColumn(3).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(4).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(5).setPreferredWidth(100);
            
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            
            jtable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); 

            jtable.getTableHeader().setReorderingAllowed(false);
            
            rs.close();
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR");
        }
    }
    
    public void listAllProdForCategory(JTable jtable){
        String sql=  "SELECT p.product_code, " +
                     "CONCAT(sc.name, ' ', b.name, ' ', p.model, ' Color ', p.color) AS descripcion, " +
                     "s.name AS proveedor, st.amount, pr.iva, pr.salePrice " +
                     "FROM products p " +
                     "INNER JOIN subcategories sc ON p.id_subcategory = sc.id_subcategory " +
                     "INNER JOIN brands b ON p.id_brand = b.id_brand " +
                     "INNER JOIN suppliers s ON p.id_supplier = s.id_supplier " +
                     "INNER JOIN prices pr ON p.id_product = pr.id_product " +
                     "INNER JOIN stocks st ON p.id_product = st.id_product ";
        Statement stmt;
        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        String[] titleTable = {"Código de producto", "Descripción", "Proveedor", "Stock", "IVA", "Precio"};
        dtm.setColumnIdentifiers(titleTable);
        
        try{
            stmt=conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);


            while (rs.next()) {
                Object[] row = {
                    rs.getString("product_code"),
                    rs.getString("descripcion"),
                    rs.getString("proveedor"),
                    rs.getInt("amount"),
                    rs.getDouble("iva"),
                    rs.getDouble("salePrice")
                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(150);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(500);
            jtable.getColumnModel().getColumn(2).setPreferredWidth(200);
            jtable.getColumnModel().getColumn(3).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(4).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(5).setPreferredWidth(100);
            
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            
            jtable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); 

            jtable.getTableHeader().setReorderingAllowed(false);
            
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR");
        }
    }
    
    public void listAllProdForBrand(JTable jtable, int brand){
        String sql=  "SELECT p.product_code, " +
                     "CONCAT(sc.name, ' ', b.name, ' ', p.model, ' Color ', p.color) AS descripcion, " +
                     "s.name AS proveedor, st.amount, pr.iva, pr.salePrice " +
                     "FROM products p " +
                     "INNER JOIN subcategories sc ON p.id_subcategory = sc.id_subcategory " +
                     "INNER JOIN brands b ON p.id_brand = b.id_brand " +
                     "INNER JOIN suppliers s ON p.id_supplier = s.id_supplier " +
                     "INNER JOIN prices pr ON p.id_product = pr.id_product " +
                     "INNER JOIN stocks st ON p.id_product = st.id_product " +
                     "WHERE p.id_brand = ?";
        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        String[] titleTable = {"Código de producto", "Descripción", "Proveedor", "Stock", "IVA", "Precio"};
        dtm.setColumnIdentifiers(titleTable);
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, brand);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("product_code"),
                    rs.getString("descripcion"),
                    rs.getString("proveedor"),
                    rs.getInt("amount"),
                    rs.getDouble("iva"),
                    rs.getDouble("salePrice")
                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(150);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(500);
            jtable.getColumnModel().getColumn(2).setPreferredWidth(200);
            jtable.getColumnModel().getColumn(3).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(4).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(5).setPreferredWidth(100);
            
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            
            jtable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); 

            jtable.getTableHeader().setReorderingAllowed(false);
            
            rs.close();
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR");
        }
    }
    
    public void listProdForBrandAndCat(JTable jtable, int brand, int cat){
        String sql=  "SELECT p.product_code, " +
                     "CONCAT(sc.name, ' ', b.name, ' ', p.model, ' Color ', p.color) AS descripcion, " +
                     "s.name AS proveedor, st.amount, pr.iva, pr.salePrice " +
                     "FROM products p " +
                     "INNER JOIN subcategories sc ON p.id_subcategory = sc.id_subcategory " +
                     "INNER JOIN brands b ON p.id_brand = b.id_brand " +
                     "INNER JOIN suppliers s ON p.id_supplier = s.id_supplier " +
                     "INNER JOIN prices pr ON p.id_product = pr.id_product " +
                     "INNER JOIN stocks st ON p.id_product = st.id_product " +
                     "WHERE p.id_brand = ? AND p.id_category = ?";
        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        String[] titleTable = {"Código de producto", "Descripción", "Proveedor", "Stock", "IVA", "Precio"};
        dtm.setColumnIdentifiers(titleTable);
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, brand);
            pstmt.setInt(2, cat);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("product_code"),
                    rs.getString("descripcion"),
                    rs.getString("proveedor"),
                    rs.getInt("amount"),
                    rs.getDouble("iva"),
                    rs.getDouble("salePrice")
                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);            
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(150);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(500);
            jtable.getColumnModel().getColumn(2).setPreferredWidth(200);
            jtable.getColumnModel().getColumn(3).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(4).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(5).setPreferredWidth(100);
            
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            
            jtable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); 

            jtable.getTableHeader().setReorderingAllowed(false);
            
            rs.close();
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al listar productos: " + e.getMessage());
        }
    }
    
    
    

//********************************************** END: CATEGORIES ***************************************************************************     
    
    
//********************************************** SUBCATEGORIES ****************************************************************************    
    public void listAllProdForSubcategory(JTable jtable, int idSubcat){
        String sql=  "SELECT p.product_code, " +
                     "CONCAT(sc.name, ' ', b.name, ' ', p.model, ' Color ', p.color) AS descripcion, " +
                     "s.name AS proveedor, st.amount, pr.iva, pr.salePrice " +
                     "FROM products p " +
                     "INNER JOIN subcategories sc ON p.id_subcategory = sc.id_subcategory " +
                     "INNER JOIN brands b ON p.id_brand = b.id_brand " +
                     "INNER JOIN suppliers s ON p.id_supplier = s.id_supplier " +
                     "INNER JOIN prices pr ON p.id_product = pr.id_product " +
                     "INNER JOIN stocks st ON p.id_product = st.id_product " +
                     "WHERE p.id_subcategory = ?";

        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        String[] titleTable = {"Código de producto", "Descripción", "Proveedor", "Stock", "IVA", "Precio"};
        dtm.setColumnIdentifiers(titleTable);
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, idSubcat);
            ResultSet rs = pstmt.executeQuery();


            while (rs.next()) {
                Object[] row = {
                    rs.getString("product_code"),
                    rs.getString("descripcion"),
                    rs.getString("proveedor"),
                    rs.getInt("amount"),
                    rs.getDouble("iva"),
                    rs.getDouble("salePrice")
                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
                       
            jtable.getColumnModel().getColumn(0).setPreferredWidth(150);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(500);
            jtable.getColumnModel().getColumn(2).setPreferredWidth(200);
            jtable.getColumnModel().getColumn(3).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(4).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(5).setPreferredWidth(100);
            
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            
            jtable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); 

            jtable.getTableHeader().setReorderingAllowed(false);
            
            rs.close();
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR" + e.getMessage());
        }
    }
    
    public void listProdForBrandAndSubCat(JTable jtable, int brand, int subcat){
        String sql=  "SELECT p.product_code, " +
                     "CONCAT(sc.name, ' ', b.name, ' ', p.model, ' Color ', p.color) AS descripcion, " +
                     "s.name AS proveedor, st.amount, pr.iva, pr.salePrice " +
                     "FROM products p " +
                     "INNER JOIN subcategories sc ON p.id_subcategory = sc.id_subcategory " +
                     "INNER JOIN brands b ON p.id_brand = b.id_brand " +
                     "INNER JOIN suppliers s ON p.id_supplier = s.id_supplier " +
                     "INNER JOIN prices pr ON p.id_product = pr.id_product " +
                     "INNER JOIN stocks st ON p.id_product = st.id_product " +
                     "WHERE p.id_brand = ? AND p.id_subcategory = ?";
        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        String[] titleTable = {"Código de producto", "Descripción", "Proveedor", "Stock", "IVA", "Precio"};
        dtm.setColumnIdentifiers(titleTable);
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, brand);
            pstmt.setInt(2, subcat);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("product_code"),
                    rs.getString("descripcion"),
                    rs.getString("proveedor"),
                    rs.getInt("amount"),
                    rs.getDouble("iva"),
                    rs.getDouble("salePrice")
                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);            
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(150);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(500);
            jtable.getColumnModel().getColumn(2).setPreferredWidth(200);
            jtable.getColumnModel().getColumn(3).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(4).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(5).setPreferredWidth(100);
            
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            
            jtable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); 

            jtable.getTableHeader().setReorderingAllowed(false);
            
            rs.close();
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al listar productos: " + e.getMessage());
        }
    }    
    
    public void newSubcategory(int idCat, String name, int state){
        
        String sql="INSERT INTO `subcategories`(`id_category`, `name`, `state`) VALUES (?,?,?)";
        
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, idCat);
            pstmt.setString(2, name);
            pstmt.setInt(3, state);
            pstmt.executeUpdate();
            
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR SUBCATEGORIA");
        }    
    }
    
    //CARGA EL JLIST DE CATEGORIAS
    public static int idCategoria(String name){
        String sql="SELECT `id_category` FROM `categories` WHERE `name` = '"+ name +"'";
        Statement stmt;
        int idCat=0;
        
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        try{
           stmt=conexion.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
           
           while(rs.next()){
               idCat=(rs.getInt("id_category"));            
           }                 
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR");
        }
       return idCat; 
    }
    
    public void listTableSubcategories(JTable jtable, int categoria){
        String sql="SELECT * FROM `subcategories` WHERE `id_category` = "+categoria; 
        Statement stmt;
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String estado;
 
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        String[] titleTBrands = new String[]{"Nombre", "Estado"};
        dtm.setColumnIdentifiers(titleTBrands);
        
        try{
           stmt=conexion.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
           
            while(rs.next()){
                
                if (rs.getString("state").equals("0")) {
                    estado = "DESHABLITADO";
                } else {
                    estado = "HABILITADO";
                }
               
                Object [] lista = {rs.getString(3),estado};
                dtm.addRow(lista);             
            } 
            jtable.setModel(dtm);
            
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR");
        }
    }
    
    //ACTUALIZA LA CATEGORIA PADRE DE UNA SUBCATEGORIA
    public void editCatPadre(String name, int idCat){
        String sql = "UPDATE `subcategories` SET `id_category`= ? WHERE `name`=?";
        //UPDATE `subcategories` SET `id_category`= 1 WHERE `name`='TECLADOS'; 
        
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
            pstmt.setInt(1, idCat);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
            
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL ACTUALIZAR ESTADO" + e.getMessage());
        }
    }
    
    public void llenarCombosSubcategories(JComboBox combo, int idCat){
        String sql="SELECT * FROM `subcategories` WHERE `id_category`="+idCat+" AND `state`=1";
        Statement stmt;
        combo.addItem("Seleccione una subcategoria");
        
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
      
        try{
           stmt=conexion.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
           
           while(rs.next()){
               combo.addItem(rs.getString("name"));
           }                  
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR" + e.getMessage());
        }
    }

    
//********************************************** END: SUBCATEGORIES ***************************************************************************************    
    
//********************************************** PRODUCTS ************************************************************************************************* 
    
    public void newProduct(int id_subcategory, int id_brand , int id_supplier , int id_category, String model, String color, String product_code, int state){
        
        String sql="INSERT INTO `products`(`id_subcategory`, `id_brand`, `id_supplier`, `id_category`, `model`, `color`, `product_code`, `state`) VALUES (?,?,?,?,?,?,?,?)";
               
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, id_subcategory);
            pstmt.setInt(2, id_brand);
            pstmt.setInt(3, id_supplier);
            pstmt.setInt(4, id_category);
            pstmt.setString(5, model);
            pstmt.setString(6, color);
            pstmt.setString(7, product_code);
            pstmt.setInt(8, state);
            pstmt.executeUpdate();            
            conexion.close();           
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR PRODUCTO");
        }    
    }
      
    
    public static int obtenerIdProduct(String product_code){
        String sql = "SELECT id_product FROM products WHERE product_code = ?";
        int id = 0;

        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();

        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setString(1, product_code);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id_product");
            }
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener ID del producto: " + e.getMessage());
        }
        return id;
    }
    
    
    public void selectProductEdit(
            int idProducto,
            JTextField txtSubcategorie,
            JTextField txtBrand,
            JTextField txtSupplier,
            JTextField txtCategory,
            JTextField txtModel,
            JTextField txtColor,  
            JTextField txtProductCode   
            ){    
        
        String sql =
                "SELECT categories.name, subcategories.name, brands.name, suppliers.name, " +
                "products.model, products.color, products.product_code " +
                "FROM products " +
                "INNER JOIN subcategories ON products.id_subcategory = subcategories.id_subcategory " +
                "INNER JOIN brands ON products.id_brand = brands.id_brand " +
                "INNER JOIN suppliers ON products.id_supplier = suppliers.id_supplier " +
                "INNER JOIN categories ON products.id_category = categories.id_category " +
                "WHERE products.id_product = " + idProducto;   
        
        Statement stmt;               
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
                  
        try{            
           stmt=conexion.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
           
           if (rs.next()) {
               
                txtBrand.setText(rs.getString("brands.name"));
                txtModel.setText(rs.getString("model"));
                txtColor.setText(rs.getString("color"));
                txtProductCode.setText(rs.getString("product_code"));
                txtSupplier.setText(rs.getString("suppliers.name"));
                txtCategory.setText(rs.getString("categories.name"));
                txtSubcategorie.setText(rs.getString("subcategories.name"));
            
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el producto con ID: " + idProducto);
            }
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR X");
        } 
    }
    
    public void selectProduct(int idProducto, JLabel producto, JTextField codigoProducto){
        String sql="SELECT subcategories.name, brands.name, model, color, product_code "
                + "FROM products "
                + "INNER JOIN subcategories ON products.id_subcategory = subcategories.id_subcategory "
                + "INNER JOIN brands ON products.id_brand = brands.id_brand "
                + "WHERE id_product = " +idProducto;        
        
        Statement stmt;

        String marca;
        String subcat;
        String modelo;
        String color;
        
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        try{
           stmt=conexion.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
           
           while(rs.next()){
               
                subcat=(rs.getString("subcategories.name"));
                marca=(rs.getString("brands.name"));
                modelo=(rs.getString("model"));
                color=(rs.getString("color"));
                producto.setText(subcat+" "+marca+" "+modelo+" COLOR "+color+"."); 
                codigoProducto.setText(rs.getString("product_code"));
           }               
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL VERIFICAR ESTADO");
        } 
    }
    
    //EDITAR PRODUCTO
    public void editProduct(int idProduct, int id_subcategory, int id_brand , int id_supplier, int id_category, String model, String color, String product_code, int state){
        
        String sql = "UPDATE `products` SET `id_subcategory`=?,`id_brand`=?,`id_supplier`=?,`id_category`=?,`model`=?,`color`=?,`product_code`=? ,`state`=? WHERE `id_product`=?";
               
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, id_subcategory);
            pstmt.setInt(2, id_brand);
            pstmt.setInt(3, id_supplier);
            pstmt.setInt(4, id_category);
            pstmt.setString(5, model);
            pstmt.setString(6, color);
            pstmt.setString(7, product_code);
            pstmt.setInt(8, state);
            pstmt.setInt(9, idProduct);
            pstmt.executeUpdate();
            
            conexion.close();
            
            JOptionPane.showMessageDialog(null, "¡Producto editado con éxito!");
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL EDITAR PRODUCTO");
        }    
    }
    
    // LLENAR LISTA EN frmProduct
    public void listProductCode(String productCode, JTable jtable){
        String sql="SELECT * FROM brands";
        Statement stmt;
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
 
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        String[] titleTBrands = new String[]{"Nombre"};
        dtm.setColumnIdentifiers(titleTBrands);
        
        try{
           stmt=conexion.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
           
            while(rs.next()){
               
                Object [] lista = {rs.getString(2)};
                dtm.addRow(lista);             
            } 
            jtable.setModel(dtm);
            
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR");
        }
    }
    
    public void listTableProducts(JTable jtable, String productCode) {
        String sql = "SELECT p.product_code, " +
                     "CONCAT(sc.name, ' ', b.name, ' ', p.model, ' Color ', p.color) AS descripcion, " +
                     "s.name AS proveedor, st.amount, pr.iva, pr.salePrice " +
                     "FROM products p " +
                     "INNER JOIN subcategories sc ON p.id_subcategory = sc.id_subcategory " +
                     "INNER JOIN brands b ON p.id_brand = b.id_brand " +
                     "INNER JOIN suppliers s ON p.id_supplier = s.id_supplier " +
                     "INNER JOIN prices pr ON p.id_product = pr.id_product " +
                     "INNER JOIN stocks st ON p.id_product = st.id_product " +
                     "WHERE p.product_code = ?";

        DefaultTableModel dtm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        String[] titleTable = {"Código de producto", "Descripción", "Proveedor", "Stock", "IVA", "Precio"};
        dtm.setColumnIdentifiers(titleTable);

        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();

        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setString(1, productCode);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("product_code"),
                    rs.getString("descripcion"),
                    rs.getString("proveedor"),
                    rs.getInt("amount"),
                    rs.getDouble("iva"),
                    rs.getDouble("salePrice")
                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
            
//            jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(150);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(500);
            jtable.getColumnModel().getColumn(2).setPreferredWidth(200);
            jtable.getColumnModel().getColumn(3).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(4).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(5).setPreferredWidth(100);
            
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            
            jtable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); 

            jtable.getTableHeader().setReorderingAllowed(false);

            rs.close();
            pstmt.close();
            conexion.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
    }
  
//********************************************** END: PRODUCTS ****************************************************************************    
    
//********************************************** PRICE ************************************************************************************ 
    
    public void priceProduct(int id_product , double  price , double  benefit ,double  iva, double  salePrice){
        
        String sql="INSERT INTO `prices`(`id_product`, `price`, `benefit`, `iva`, `salePrice`) VALUES (?,?,?,?,?)";        
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, id_product);
            pstmt.setDouble(2, price);
            pstmt.setDouble(3, benefit);
            pstmt.setDouble(4, iva);
            pstmt.setDouble(5, salePrice);
            pstmt.executeUpdate();
            
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR PRECIO DE PRODUCTO");
        }    
    }
    
    //EDITAR PRECIO DE PRODUCTO
    public void editPriceProduct(int idProduct, double price, double benefit, double salePrice){
        String sql = "UPDATE `prices` SET `price`=?, `benefit`=?, `salePrice`=? WHERE `id_product`=?";

        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();

        try {
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setDouble(1, price);
            pstmt.setDouble(2, benefit);
            pstmt.setDouble(3, salePrice);
            pstmt.setInt(4, idProduct); 

            pstmt.executeUpdate();
            pstmt.close();
            conexion.close();

            JOptionPane.showMessageDialog(null, "¡Datos registrados con éxito!");
            
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR AL EDITAR PRECIO" + e.getMessage());
        } 
    }
    
    public void editPriceInStocks(int idProduct, double price){
        String sql = "UPDATE `prices` SET `price`=? WHERE `id_product`=?";

        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();

        try {
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setDouble(1, price);
            pstmt.setInt(2, idProduct); 

            pstmt.executeUpdate();
            pstmt.close();
            conexion.close();
           
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR AL EDITAR PRECIO" + e.getMessage());
        } 
    }
     
    public void selectProductPriceEdit1(int id_Product, JFormattedTextField txtPrice, JTextField txtBenefit , JLabel labelIva, JFormattedTextField txtFinalPrice) {
        
        String sql = "SELECT `price`, `benefit`, `iva`, `salePrice` FROM `prices` WHERE `id_product`= ?";
    
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
    
        try {
            PreparedStatement stmt = (PreparedStatement) conexion.prepareStatement(sql);
            stmt.setInt(1, id_Product);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                txtPrice.setValue(rs.getDouble("price"));
                txtBenefit.setText(rs.getString("benefit"));
                labelIva.setText(rs.getString("iva"));
                txtFinalPrice.setText(rs.getString("salePrice"));
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el id: " + id_Product);
            }

            rs.close();
            stmt.close();
            conexion.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
    }
        
    public void selectProductPriceEdit(int id_Product, JFormattedTextField txtPrice, JLabel labelIva) {
        
        String sql = "SELECT `price`, `iva` FROM `prices` WHERE `id_product`= ?";
    
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
    
        try {
            PreparedStatement stmt = (PreparedStatement) conexion.prepareStatement(sql);
            stmt.setInt(1, id_Product);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                txtPrice.setValue(rs.getDouble("price")); 
                labelIva.setText(rs.getString("iva"));
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el id: " + id_Product);
            }

            rs.close();
            stmt.close();
            conexion.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
    }
     
     
     
//********************************************** END: PRICE ****************************************************************************    
    
//********************************************** STOCKS ************************************************************************************      
    public static int obtenerStock(int id_product) {
        int stock = 0;

        String sql = "SELECT amount FROM stocks WHERE id_product = ?";

        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();

        try {
            PreparedStatement stmt = (PreparedStatement) conexion.prepareStatement(sql);
            stmt.setInt(1, id_product);

            ResultSet rs = stmt.executeQuery();  // CORREGIDO

            if (rs.next()) {
                stock = rs.getInt("amount");
            }

            rs.close();
            stmt.close();
            conexion.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR al obtener stock: " + e.getMessage());
        }

        return stock;
    }
    
    public void agregarStock(int id_product){
        
        String sql="INSERT INTO `stocks`(`id_product`, `amount`, `min`, `max`) VALUES (?,?,?,?)";
        int max = 0;
        int min = 0;
        int amount = 0;
        
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, id_product);
            pstmt.setInt(2, amount);
            pstmt.setInt(3, min);
            pstmt.setInt(4, max);
            pstmt.executeUpdate();
            
            conexion.close();
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR STOCK");
        }    
    }
    
    public void editStockProduct(int idProduct, double stock){
        String sql = "UPDATE `stocks` SET `amount`=? WHERE `id_product`=?";

        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();

        try {
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setDouble(1, stock);
            pstmt.setInt(2, idProduct);

            pstmt.executeUpdate();
            pstmt.close();
            conexion.close();
            
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR AL EDITAR PRECIO" + e.getMessage());
        } 
    }
    
    //***************************************** COMBOS DE AJUSTE DE STOCK ***************************************
    
    public void llenarCombosAjusteStock(JComboBox combo, double tipe){
        String sql="SELECT * FROM `motivoajustestock` WHERE `tipe`="+tipe;    
        
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        combo.addItem("Seleccione una categoría");
              
        try{
            Statement stmt;
            stmt=conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                combo.addItem(rs.getString("name"));
            }                  
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LOS COMBOBOX" + e.getMessage());
        }
    } 
}
