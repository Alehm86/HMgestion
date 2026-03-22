/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import ConnectionDB.connectionDB;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;


public class productDAO { 
    
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
 
        connectionDB con = new connectionDB();
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
    public void insertBrand(String name){
        
        String sql="INSERT INTO brands(name) VALUE(?)";
        
        connectionDB con = new connectionDB();
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
    public void updateBrand(String newName, String oldName){
        String sql = "UPDATE `brands` SET `name` = ? WHERE `name` = ?";
        
        connectionDB con = new connectionDB();
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
 
        connectionDB con = new connectionDB();
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
            JTextField txtCondicion,
            JTextField txtCuit,
            JTextField txtTelefono,
            JTextField txtEmail,
            JTextField txtUrl,
            JTextField txtUser,
            JTextField txtPassword
        ){

        String sql = "SELECT * FROM suppliers WHERE name = ?";

        connectionDB con = new connectionDB();
        Connection conexion = con.establecerConexion();

        try {

            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();

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
            ps.close();
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

        connectionDB con = new connectionDB();
        Connection conexion = con.establecerConexion();

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
        String sql = "SELECT * FROM proveedores WHERE nombre = ? OR cuit = ? OR email = ?";
        connectionDB con = new connectionDB();
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
    public void updateSupplier(String oldName,String newName, String condicion, String cuit,String tel,String mail,String url,String user,String pass){
        
        String sql = "UPDATE `suppliers` "
                + "SET `name`=?, `condicion`=?, `cuit`=?,`telefono`=?,`mail`=?,`url`=?,`user`=?,`pass`=? "
                + "WHERE `name`=?";
                  
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
       
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
            
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Ya existe otro proveedor con ese nombre.");
        }
    }
    
    //REGISTRAR NUEVO PROVEEDOR EN frmNewSupplier.java
    public void insertSupplier(String name, String condicion, String cuit,String tel,String mail,String url,String user,String pass){
        
        String sql="INSERT INTO `suppliers`(`name`, `condicion`,`cuit`, `telefono`, `mail`, `url`, `user`, `pass`) VALUES (?,?,?,?,?,?,?,?)";
        
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
       
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
            pstmt.executeUpdate();          
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR PROVEEDOR" + e.getMessage() );
        }    
    }
      
//********************************************** END: SUPPLIERS ***************************************************************************     
    
    
//********************************************** CATEGORIES ****************************************************************************
  
    //REGISTRAR CATEGORIA NUEVA
    public void insertCategory(String name, int state){
        
        String sql="INSERT INTO `categories`(`name`, `state`) VALUES (?,?)";
        
        connectionDB con = new connectionDB();
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
 
        connectionDB con = new connectionDB();
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
       

    public void listProdFCategory(JTable jtable, int id_category){
        String sql=  "SELECT p.product_code, " +
                     "CONCAT(sc.name, ' ', b.name, ' ', p.model, ' Color ', p.color) AS descripcion, " +
                     "st.amount, pr.iva, pr.salePrice " +
                     "FROM products p " +
                     "INNER JOIN subcategories sc ON p.id_subcategory = sc.id_subcategory " +
                     "INNER JOIN brands b ON p.id_brand = b.id_brand " +
                     "INNER JOIN product_price pr ON p.id_product = pr.id_product " +
                     "INNER JOIN product_stock st ON p.id_product = st.id_product " +
                     "WHERE p.id_category = ?";
        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        String[] titleTable = {"Código de producto", "Descripción", "Stock", "IVA", "Precio"};
        dtm.setColumnIdentifiers(titleTable);
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, id_category);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("product_code"),
                    rs.getString("descripcion"),
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
            jtable.getColumnModel().getColumn(2).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(3).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(4).setPreferredWidth(100);
            
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            
            jtable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);  

            jtable.getTableHeader().setReorderingAllowed(false);
            
            rs.close();
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR");
        }
    }
    
    public void listAllProduct(JTable jtable){
        String sql=  "SELECT p.product_code, " +
                     "CONCAT(sc.name, ' ', b.name, ' ', p.model, ' Color ', p.color) AS descripcion, " +
                     "st.amount, pr.iva, pr.salePrice " +
                     "FROM products p " +
                     "INNER JOIN subcategories sc ON p.id_subcategory = sc.id_subcategory " +
                     "INNER JOIN brands b ON p.id_brand = b.id_brand " +
                     "INNER JOIN product_price pr ON p.id_product = pr.id_product " +
                     "INNER JOIN product_stock st ON p.id_product = st.id_product ";
        Statement stmt;
        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        String[] titleTable = {"Código de producto", "Descripción", "Stock", "IVA", "Precio"};
        dtm.setColumnIdentifiers(titleTable);
        
        try{
           stmt=conexion.createStatement();
           ResultSet rs = stmt.executeQuery(sql);


            while (rs.next()) {
                Object[] row = {
                    rs.getString("product_code"),
                    rs.getString("descripcion"),
                    rs.getInt("amount"),
                    rs.getDouble("iva"),
                    rs.getDouble("salePrice")
                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(150);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(500);
            jtable.getColumnModel().getColumn(2).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(3).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(4).setPreferredWidth(100);
            
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            
            jtable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);  

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
                     "st.amount, pr.iva, pr.salePrice " +
                     "FROM products p " +
                     "INNER JOIN subcategories sc ON p.id_subcategory = sc.id_subcategory " +
                     "INNER JOIN brands b ON p.id_brand = b.id_brand " +                     
                     "INNER JOIN product_price pr ON p.id_product = pr.id_product " +
                     "INNER JOIN product_stock st ON p.id_product = st.id_product " +
                     "WHERE p.id_brand = ?";
        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        String[] titleTable = {"Código de producto", "Descripción", "Stock", "IVA", "Precio"};
        dtm.setColumnIdentifiers(titleTable);
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, brand);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("product_code"),
                    rs.getString("descripcion"),
                    rs.getInt("amount"),
                    rs.getDouble("iva"),
                    rs.getDouble("salePrice"),
                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
            
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(150);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(500);
            jtable.getColumnModel().getColumn(2).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(3).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(4).setPreferredWidth(100);
            
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            
            jtable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); 

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
                     "INNER JOIN product_price pr ON p.id_product = pr.id_product " +
                     "INNER JOIN product_stock st ON p.id_product = st.id_product " +
                     "WHERE p.id_brand = ? AND p.id_category = ?";
        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        connectionDB con = new connectionDB();
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
                    rs.getInt("amount"),
                    rs.getDouble("iva"),
                    rs.getDouble("salePrice")
                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);            
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(150);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(500);
            jtable.getColumnModel().getColumn(2).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(3).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(4).setPreferredWidth(100);
            
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            
            jtable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);  

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
    public void listProdFSubcategory(JTable jtable, int idSubcat){
        String sql=  "SELECT p.product_code, " +
                     "CONCAT(sc.name, ' ', b.name, ' ', p.model, ' Color ', p.color) AS descripcion, " +
                     "st.amount, pr.iva, pr.salePrice " +
                     "FROM products p " +
                     "INNER JOIN subcategories sc ON p.id_subcategory = sc.id_subcategory " +
                     "INNER JOIN brands b ON p.id_brand = b.id_brand " +
                     "INNER JOIN product_price pr ON p.id_product = pr.id_product " +
                     "INNER JOIN product_stock st ON p.id_product = st.id_product " +
                     "WHERE p.id_subcategory = ?";

        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        String[] titleTable = {"Código de producto", "Descripción", "Stock", "IVA", "Precio"};
        dtm.setColumnIdentifiers(titleTable);
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, idSubcat);
            ResultSet rs = pstmt.executeQuery();


            while (rs.next()) {
                Object[] row = {
                    rs.getString("product_code"),
                    rs.getString("descripcion"),
                    rs.getInt("amount"),
                    rs.getDouble("iva"),
                    rs.getDouble("salePrice")
                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
                       
            jtable.getColumnModel().getColumn(0).setPreferredWidth(150);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(500);
            jtable.getColumnModel().getColumn(2).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(3).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(4).setPreferredWidth(100);
            
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            
            jtable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); 

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
                     "st.amount, pr.iva, pr.salePrice " +
                     "FROM products p " +
                     "INNER JOIN subcategories sc ON p.id_subcategory = sc.id_subcategory " +
                     "INNER JOIN brands b ON p.id_brand = b.id_brand " +
                     "INNER JOIN product_price pr ON p.id_product = pr.id_product " +
                     "INNER JOIN product_stock st ON p.id_product = st.id_product " +
                     "WHERE p.id_brand = ? AND p.id_subcategory = ?";
        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        connectionDB con = new connectionDB();
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
                    rs.getInt("amount"),
                    rs.getDouble("iva"),
                    rs.getDouble("salePrice")
                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);            
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(150);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(500);
            jtable.getColumnModel().getColumn(2).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(3).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(4).setPreferredWidth(100);
            
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            
            jtable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); 

            jtable.getTableHeader().setReorderingAllowed(false);
            
            rs.close();
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al listar productos: " + e.getMessage());
        }
    }    
    
    public void insertSubcategory(int idCat, String name, int state){
        
        String sql="INSERT INTO `subcategories`(`id_category`, `name`, `state`) VALUES (?,?,?)";
        
        connectionDB con = new connectionDB();
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
    public static int selectIdCategoria(String name){
        String sql="SELECT `id_category` FROM `categories` WHERE `name` = '"+ name +"'";
        Statement stmt;
        int idCat=0;
        
        connectionDB con = new connectionDB();
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
 
        connectionDB con = new connectionDB();
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
    public void updateCatPadre(String name, int idCat){
        String sql = "UPDATE `subcategories` SET `id_category`= ? WHERE `name`=?";
        //UPDATE `subcategories` SET `id_category`= 1 WHERE `name`='TECLADOS'; 
        
        connectionDB con = new connectionDB();
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
        
        connectionDB con = new connectionDB();
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
    
    public int insertProduct(int id_subcategory, int id_brand, String model, String color, String product_code, int state){
        
        String sql="INSERT INTO `products`(`id_subcategory`, `id_brand`,  `model`, `color`, `product_code`, `state`) VALUES (?,?,?,?,?,?)";
        
        int idGenerado = 0;
               
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
       
        try{
            PreparedStatement pstmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, id_subcategory);
            pstmt.setInt(2, id_brand);
            pstmt.setString(3, model);
            pstmt.setString(4, color);
            pstmt.setString(5, product_code);
            pstmt.setInt(6, state);
            
            pstmt.executeUpdate(); 
            
            ResultSet rs = pstmt.getGeneratedKeys();

            if(rs.next()){
                idGenerado = rs.getInt(1);
            }
            
            rs.close();
            conexion.close(); 
            pstmt.close();
            
            JOptionPane.showMessageDialog(null, "Producto registrado!");
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR PRODUCTO" + e.getMessage());
        }  
        
        return idGenerado;
    }
         
    public static int selectIdProduct(String product_code){
        String sql = "SELECT id_product FROM products WHERE product_code = ?";
        int id = 0;

        connectionDB con = new connectionDB();
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
            JLabel lbl_id,
            JTextField txtCategories,
            JTextField txtSubcategories,
            JTextField txtBrand,
            JTextField txtModel,
            JTextField txtColor,  
            JTextField txtProductCode,
            JLabel lblPromotion
        ) {    
        
        String sql = 
                "SELECT " + 
                "categories.name AS category, " +
                "subcategories.name AS subcategory, " +
                "brands.name AS brand, " +
                "products.model, " +
                "products.color, " +
                "products.product_code, " +
                "COALESCE(product_promotions.name, 'Sin promoción') AS promotion " +
                "FROM products " +
                "INNER JOIN subcategories ON products.id_subcategory = subcategories.id_subcategory " +
                "INNER JOIN categories ON subcategories.id_category = categories.id_category " +
                "INNER JOIN brands ON products.id_brand = brands.id_brand " +
                "LEFT JOIN product_promotions ON products.id_promotion = product_promotions.id_promotion " +
                "WHERE products.id_product = ?";

        connectionDB con = new connectionDB();
        Connection conexion = con.establecerConexion();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conexion.prepareStatement(sql);
            pstmt.setInt(1, idProducto);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                lbl_id.setText(String.valueOf(idProducto));
                txtBrand.setText(rs.getString("brand"));
                txtModel.setText(rs.getString("model"));
                txtColor.setText(rs.getString("color"));
                txtProductCode.setText(rs.getString("product_code"));
                txtSubcategories.setText(rs.getString("subcategory"));
                txtCategories.setText(rs.getString("category"));
                lblPromotion.setText(rs.getString("promotion"));
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el producto");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar producto: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                conexion.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al cerrar conexión: " + ex.getMessage());
            }
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
        
        connectionDB con = new connectionDB();
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
            JOptionPane.showMessageDialog(null, "ERROR AL VERIFICAR ESTADO " + e.getMessage());
        } 
    }
    
    //EDITAR PRODUCTO
    public void updateProduct(int idProduct, int id_subcategory, int id_brand , String model, String color, String product_code, int state){
        
        String sql = "UPDATE `products` SET `id_subcategory`=?,`id_brand`=?,`model`=?,`color`=?,`product_code`=? ,`state`=? WHERE `id_product`=?";
               
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, id_subcategory);
            pstmt.setInt(2, id_brand);
            pstmt.setString(3, model);
            pstmt.setString(4, color);
            pstmt.setString(5, product_code);
            pstmt.setInt(6, state);
            pstmt.setInt(7, idProduct);
            pstmt.executeUpdate();
            
            conexion.close();
            
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
 
        connectionDB con = new connectionDB();
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
            JOptionPane.showMessageDialog(null, "ERROR" + e.getMessage());
        }
    }
    
    public void listTableProducts(JTable jtable, String productCode) {
        String sql = "SELECT p.product_code, " +
                     "CONCAT(sc.name, ' ', b.name, ' ', p.model, ' Color ', p.color) AS descripcion, " +
                     "st.amount, pr.iva, pr.salePrice " +
                     "FROM products p " +
                     "INNER JOIN subcategories sc ON p.id_subcategory = sc.id_subcategory " +
                     "INNER JOIN brands b ON p.id_brand = b.id_brand " +
                     "INNER JOIN product_price pr ON p.id_product = pr.id_product " +
                     "INNER JOIN product_stock st ON p.id_product = st.id_product " +
                     "WHERE p.product_code = ?";

        DefaultTableModel dtm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        String[] titleTable = {"Código de producto", "Descripción", "Stock", "IVA", "Precio"};
        dtm.setColumnIdentifiers(titleTable);

        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();

        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setString(1, productCode);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("product_code"),
                    rs.getString("descripcion"),
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
            jtable.getColumnModel().getColumn(2).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(3).setPreferredWidth(50);
            jtable.getColumnModel().getColumn(4).setPreferredWidth(100);
            
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            
            jtable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            jtable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); 

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
        connectionDB con = new connectionDB();
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
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR PRECIO DE PRODUCTO " + e.getMessage());
        }    
    }
    
    //EDITAR PRECIO DE PRODUCTO
    public void updatePriceProduct(int idProduct, double price, double benefit, double salePrice, double iva){
        String sql = "UPDATE `product_price` SET `price`=?, `benefit`=?, `salePrice`=?, `iva`=? WHERE `id_product`=?";

        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();

        try {
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setDouble(1, price);
            pstmt.setDouble(2, benefit);
            pstmt.setDouble(3, salePrice);
            pstmt.setDouble(4, iva);
            pstmt.setInt(5, idProduct); 

            pstmt.executeUpdate();
            pstmt.close();
            conexion.close();
        
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR AL EDITAR PRECIO" + e.getMessage());
        } 
    }
    
    public void updatePriceInStocks(int idProduct, double price){
        String sql = "UPDATE `prices` SET `price`=? WHERE `id_product`=?";

        connectionDB con = new connectionDB();
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
     
    public void selectProductPriceEdit(int id_Product, JTextField txtPrice, JTextField txtBenefit , JLabel labelIva, JTextField txtFinalPrice) {
        
        String sql = "SELECT `price`, `benefit`, `iva`, `salePrice` FROM `product_price` WHERE `id_product`= ?";
    
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
    
        try {
            PreparedStatement stmt = (PreparedStatement) conexion.prepareStatement(sql);
            stmt.setInt(1, id_Product);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                txtPrice.setText(rs.getString("price"));              
                txtBenefit.setText(rs.getString("benefit"));
                labelIva.setText(rs.getString("iva"));
                txtFinalPrice.setText(rs.getString("salePrice"));
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el producto");
            }

            rs.close();
            stmt.close();
            conexion.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
    }
        
    public void selectPriceAndIva(int id_Product, JTextField txtPrice, JLabel labelIva) {
        
        String sql = "SELECT `price`, `iva` FROM `product_price` WHERE `id_product`= ?";
    
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
    
        try {
            PreparedStatement stmt = (PreparedStatement) conexion.prepareStatement(sql);
            stmt.setInt(1, id_Product);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                txtPrice.setText(rs.getString("price"));
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
    public static int selectStock(int id_product) {
        int stock = 0;

        String sql = "SELECT amount FROM product_stock WHERE id_product = ?";

        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();

        try {
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, id_product);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                stock = rs.getInt("amount");
            }

            rs.close();
            pstmt.close();
            conexion.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR al obtener stock: " + e.getMessage());
        }

        return stock;
    }
    
    public void selectAllTableStock(int idproduct, JLabel lblStock, JTextField txtMin){
        
        String sql = "SELECT `amount`, `min` FROM `product_stock` WHERE `id_product` = " + idproduct;
        
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        Statement stmt;               

        try{            
           stmt=conexion.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
           
           if (rs.next()) {
               
                lblStock.setText(rs.getString("amount"));
                txtMin.setText(rs.getString("min"));          
            } 
           
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR " + e.getMessage());
        } 
        
    }
    
    //inserta el stock = 0 al igual que la configuracion básica al dar de alta un producto nuevo
    public void insertInitialStock(int id_product){
        
        String sql="INSERT INTO `product_stock`(`id_product`, `amount`, `min`) VALUES (?,?,?)";

        int min = 0;
        int amount = 0;
        
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, id_product);
            pstmt.setInt(2, amount);
            pstmt.setInt(3, min);
            pstmt.executeUpdate();
            
            conexion.close();
            pstmt.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR STOCK "+ e.getMessage());
        }    
    }
    
    //inserta el precio = 0 al igual que la configuracion básica al dar de alta un producto nuevo   
    public void insertInitialPrice(int id_product){
        
        String sql="INSERT INTO `product_price`(`id_product`, `price`, `benefit`, `iva`, `salePrice`) VALUES (?,?,?,?,?)";
        double price = 0;
        double benefit = 0;
        double iva = 0;
        double salePrice = 0;
        
        connectionDB con = new connectionDB();
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
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR PRECIO "+ e.getMessage());
        }    
    }
    
    public void updateStockProduct(int idProduct, double stock){
        String sql = "UPDATE `product_stock` SET `amount`=? WHERE `id_product`=?";

        connectionDB con = new connectionDB();
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
        
        connectionDB con = new connectionDB();
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
    
    //ACTUALIZAR ALARMA DE STOCK MINIMO
    public void updateAlarmStockMin(int id_prod, int min){
        
        String sql="UPDATE `product_stock` SET `min`=? WHERE `id_product`=?";
        
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        try {
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, min);
            pstmt.setInt(2, id_prod);

            pstmt.executeUpdate();
            pstmt.close();
            conexion.close();
            
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR AL EDITAR ALARMA" + e.getMessage());
        }         
        
    }
    
    public String calcularPrecioSugerido(String pCosto, String pIva, String pGanancia){

        try {
            double precioCosto = Double.parseDouble(pCosto);
            double iva = Double.parseDouble(pIva);
            double beneficio = Double.parseDouble(pGanancia);

            double precioSugerido = precioCosto * (1 + iva / 100) * (1 + beneficio / 100);
            long precioEntero = Math.round(precioSugerido);

            return "$" + precioEntero;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                "ERROR AL CALCULAR PRECIO SUGERIDO\nVerifique los valores ingresados. "+ e.getMessage());
            return "$0";
        }
    }
    
    //----------------------- PROMO ------------------------
    
    //ACTIVA UNA PROMO

    public void updateProductPromotion(int id_product, Integer id_promotion){

        String sql = "UPDATE products SET id_promotion = ? WHERE id_product = ?";

        connectionDB con = new connectionDB();
        Connection conexion = con.establecerConexion();

        try{
            PreparedStatement pstmt = conexion.prepareStatement(sql);

            if(id_promotion == null){
                pstmt.setNull(1, java.sql.Types.INTEGER);
            }else{
                pstmt.setInt(1, id_promotion);
            }

            pstmt.setInt(2, id_product);

            pstmt.executeUpdate();

            pstmt.close();
            conexion.close();

        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR " + e.getMessage());
        }         
    }           

}
