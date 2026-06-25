/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import ConnectionDB.connectionDB;
import java.awt.Color;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.function.Consumer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import utils.tableStyleUtil;



public class productDAO { 
    
//********************************************** BRANDS ***************************************************************************    
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
    //LLENAR LISTA EN frmNewBrand.java
    public void listTableBrands(JTable jtable){
        
        String sql="SELECT * FROM product_brands ORDER BY name ASC";
        
        Statement stmt;
        
        DefaultTableModel dtm = crearModeloNoEditable();
 
        Connection conexion = getConnection();
        
        String[] titleTBrands = new String[]{"Marca"};
        dtm.setColumnIdentifiers(titleTBrands);
        
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
            JOptionPane.showMessageDialog(null, "ERROR" + e.getMessage());
        }
    }
    
    //REGISTRAR NUEVA MARCA EN frmNewBrand.java
    public void insertBrand(String name){
        
        String sql="INSERT INTO product_brands(name) VALUE(?)";
        
        Connection conexion = getConnection();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);            
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            
            pstmt.close();
            conexion.close();

        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }    
    }
    
    //EDITAR MARCA REGISTRADA EN frmNewBrand.java
    public void updateBrand(String newName, String oldName){
        
        String sql = "UPDATE product_brands SET `name` = ? WHERE `name` = ?";
        
        Connection conexion = getConnection();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
            pstmt.setString(1, newName);
            pstmt.setString(2, oldName);
            pstmt.executeUpdate();
            
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
       
    
//------------------------------ END BRANDS || COMIENZA --> CATEGORIES ----------------------
  

    public void insertCategory(String name, int state){
        
        String sql="INSERT INTO product_categories(name, state) VALUES (?,?)";
        
        Connection conexion = getConnection();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);            
            pstmt.setString(1, name);
            pstmt.setInt(2, state);
            pstmt.executeUpdate();
            
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }    
    }
    
    public void listTableCategory(JTable jtable){
        
        String sql="SELECT * FROM product_categories ORDER BY name ASC";
        
        Statement stmt;
        
        DefaultTableModel dtm = crearModeloNoEditable();
        
        String estado;
 
        Connection conexion = getConnection();
        
        String[] titleCategoriy = new String[]{"Nombre", "Estado"};
        dtm.setColumnIdentifiers(titleCategoriy);
        
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
            
            tableStyleUtil.applyPoppinsHeader(jtable);
            
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }           

//*************************** END: CATEGORIES || COMIENZA --> SUBCATEGORIES ******************

    public void insertSubcategory(int idCat, String name, int state){
        
        String sql="INSERT INTO `product_subcategories`(`id_category`, `name`, `state`) VALUES (?,?,?)";
        
        Connection conexion = getConnection();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, idCat);
            pstmt.setString(2, name);
            pstmt.setInt(3, state);
            pstmt.executeUpdate();
            
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR SUBCATEGORIA");
        }    
    }
    
    public static int selectIdCategoria(String name){
        
        String sql="SELECT `id_category` FROM `product_categories` WHERE `name` = ?";
         
        int idCat=0;
        
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
           
            while(rs.next()){
                idCat=(rs.getInt("id_category"));            
            }
           
           rs.close();
           pstmt.close();
           conexion.close();
           
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        
       return idCat; 
    }
    
    public void listTableSubcategories(JTable jtable, int categoria){
        
        String sql="SELECT * FROM `product_subcategories` WHERE `id_category` = "+categoria+" ORDER BY name ASC"; 
        
        Statement stmt;
        
        DefaultTableModel dtm = crearModeloNoEditable();
        
        String estado;
 
        Connection conexion = getConnection();
        
        String[] titleSubcategory = new String[]{"Nombre", "Estado"};
        dtm.setColumnIdentifiers(titleSubcategory);
        
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
            
            tableStyleUtil.applyPoppinsHeader(jtable);
            
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    //ACTUALIZA LA CATEGORIA PADRE DE UNA SUBCATEGORIA
    public void updateCatPadre(String name, int idCat){
        
        String sql = "UPDATE `product_subcategories` SET `id_category`= ? WHERE `name`=?";
        
        Connection conexion = getConnection();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
            pstmt.setInt(1, idCat);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
            
            pstmt.close();
            conexion.close();
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL ACTUALIZAR ESTADO" + e.getMessage());
        }
    }
    
    public void llenarCombosSubcategories(JComboBox combo, int idCat){
        
        String sql="SELECT * FROM `product_subcategories` WHERE `id_category`="+idCat+" AND `state`=1 ORDER BY name ASC";
        
        Statement stmt;
        
        combo.addItem("Seleccione una subcategoria");
        
        Connection conexion = getConnection();
      
        try{
           stmt=conexion.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
           
           while(rs.next()){
               combo.addItem(rs.getString("name"));
           } 
           
           rs.close();
           stmt.close();
           conexion.close();
           
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
               
        Connection conexion = getConnection();
       
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
        
        int id = -1;

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
            pstmt.close();
            conexion.close();
            
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
            JLabel lbl_state,
            JLabel lblPromotion,
            JLabel lbl_discount_percentage
        ) {    
        
        String sql = 
                "SELECT " + 
                "pc.name AS category, " +
                "ps.name AS subcategory, " +
                "b.name AS brand, " + // 👈 también corregido
                "p.model, " +
                "p.color, " +
                "p.product_code, " +
                "COALESCE(pp.name, 'Sin promoción') AS promotion, " +
                "COALESCE(pp.discount_percentage, '0') AS discount, " +
                "p.state " +
                "FROM products p " +
                "INNER JOIN product_subcategories ps ON p.id_subcategory = ps.id_subcategory " +
                "INNER JOIN product_categories pc ON ps.id_category = pc.id_category " +
                "INNER JOIN product_brands b ON p.id_brand = b.id_brand " +
                "LEFT JOIN product_promotions pp ON p.id_promotion = pp.id_promotion " +
                "WHERE p.id_product = ?";

        Connection conexion = getConnection();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conexion.prepareStatement(sql);
            pstmt.setInt(1, idProducto);
            
            int state = -1;

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
                lbl_discount_percentage.setText(rs.getString("discount"));
                state = Integer.parseInt(rs.getString("state"));
                
                if(state == 0){
                    lbl_state.setText("Inactivo");
                    lbl_state.setForeground(Color.RED);
                }else{
                    lbl_state.setText("Activo");
                    lbl_state.setForeground(new Color(0, 128, 0));
                }
                
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
        
        String sql="SELECT sc.name, b.name, model, color, product_code "
                + "FROM products "
                + "INNER JOIN product_subcategories sc ON products.id_subcategory = sc.id_subcategory "
                + "INNER JOIN product_brands b ON products.id_brand = b.id_brand "
                + "WHERE id_product = " +idProducto;        
        
        Statement stmt;

        String marca;
        String subcat;
        String modelo;
        String color;
        
        Connection conexion = getConnection();
        
        try{
           stmt=conexion.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
           
           while(rs.next()){
               
                subcat=(rs.getString("sc.name"));
                marca=(rs.getString("b.name"));
                modelo=(rs.getString("model"));
                color=(rs.getString("color"));
                producto.setText(subcat+" "+marca+" "+modelo+" COLOR "+color+"."); 
                codigoProducto.setText(rs.getString("product_code"));
           } 
           
           rs.close();
           stmt.close();
           conexion.close();
           
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL VERIFICAR ESTADO " + e.getMessage());
        } 
    }
    
    //EDITAR PRODUCTO
    public void updateProduct(int idProduct, int id_subcategory, int id_brand , String model, String color, String product_code, int state){
        
        String sql = "UPDATE `products` SET `id_subcategory`=?,`id_brand`=?,`model`=?,`color`=?,`product_code`=? ,`state`=? WHERE `id_product`=?";
               
        Connection conexion = getConnection();
       
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
            
            pstmt.close();
            conexion.close();
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }    
    }
    
    public boolean updateStateProduct(int idProduct, int estado){
          
        boolean state = false;

        
        String sql = "UPDATE `products` SET `state`= "+ estado +" WHERE `id_product` = ?";
               
        Connection conexion = getConnection();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, idProduct);
            pstmt.executeUpdate();
            
            state = true;
            
            pstmt.close();
            conexion.close();         
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return state;
    }      
    
    // LLENAR LISTA EN frmProduct
    public void listProductCode(String productCode, JTable jtable){
        
        String sql="SELECT * FROM product_brands ORDER BY name ASC";
        
        Statement stmt;
        
        DefaultTableModel dtm = crearModeloNoEditable();
 
        Connection conexion = getConnection();
        
        String[] titleTBrands = new String[]{"Marca"};
        dtm.setColumnIdentifiers(titleTBrands);
        
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
            JOptionPane.showMessageDialog(null, "ERROR" + e.getMessage());
        }
    }
    
    public void listTableProducts(JTable jtable, String productCode) {
        
        String sql = "SELECT p.product_code, " +
                     "CONCAT(sc.name, ' ', b.name, ' ', p.model, ' Color ', p.color) AS descripcion, " +
                     "st.quantity, pr.iva, pr.salePrice " +
                     "FROM products p " +
                     "INNER JOIN product_subcategories sc ON p.id_subcategory = sc.id_subcategory " +
                     "INNER JOIN product_brands b ON p.id_brand = b.id_brand " +
                     "INNER JOIN product_price pr ON p.id_product = pr.id_product " +
                     "INNER JOIN product_stock st ON p.id_product = st.id_product " +
                     "WHERE p.product_code = ? ";

        DefaultTableModel dtm = crearModeloNoEditable();
        
        String[] titleTable = {"Código de producto", "Descripción", "Stock", "IVA", "Precio"};
        dtm.setColumnIdentifiers(titleTable);

        Connection conexion = getConnection();

        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setString(1, productCode);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("product_code"),
                    rs.getString("descripcion"),
                    rs.getInt("quantity"),
                    rs.getDouble("iva"),
                    rs.getDouble("salePrice")
                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
            
            tableStyleUtil.applyPoppinsHeader(jtable);
                    
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
        
        String sql="INSERT INTO `product_price`(`id_product`, `price`, `benefit`, `iva`, `salePrice`) VALUES (?,?,?,?,?)";     
        
        Connection conexion = getConnection();
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, id_product);
            pstmt.setDouble(2, price);
            pstmt.setDouble(3, benefit);
            pstmt.setDouble(4, iva);
            pstmt.setDouble(5, salePrice);
            pstmt.executeUpdate();
            
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR PRECIO DE PRODUCTO " + e.getMessage());
        }    
    }
    
    //EDITAR PRECIO DE PRODUCTO
    public static boolean updatePriceProduct(int idProduct, double price, double benefit, double salePrice, double iva){
        
        String sql = "UPDATE `product_price` SET `price`=?, `benefit`=?, `salePrice`=?, `iva`=? WHERE `id_product`=?";
        
        boolean status = false;

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
            
            status = true;
        
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR AL EDITAR PRECIO" + e.getMessage());
        } 
        return status;
    }
    
    public void updatePriceInStocks(int idProduct, double price){
        
        String sql = "UPDATE `product_price` SET `price`=? WHERE `id_product`=?";

        Connection conexion = getConnection();

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
    
        Connection conexion = getConnection();
    
        try {
            PreparedStatement stmt = (PreparedStatement) conexion.prepareStatement(sql);
            stmt.setInt(1, id_Product);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                txtPrice.setText(rs.getString("price"));              
                txtBenefit.setText(rs.getString("benefit"));
                labelIva.setText(rs.getString("iva"));
                txtFinalPrice.setText(rs.getString("salePrice"));
            } 
//            else {
//                JOptionPane.showMessageDialog(null, "No se encontró el producto");
//            }

            rs.close();
            stmt.close();
            conexion.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
    }
        
    public void selectPriceAndIva(int id_Product, JTextField txtPrice, JLabel labelIva) {
        
        String sql = "SELECT `price`, `iva` FROM `product_price` WHERE `id_product`= ?";
    
        Connection conexion = getConnection();
    
        try {
            PreparedStatement stmt = (PreparedStatement) conexion.prepareStatement(sql);
            stmt.setInt(1, id_Product);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                txtPrice.setText(rs.getString("price"));
                String iva = rs.getString("iva");
                labelIva.setText(iva + "%");
                
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
    
    public void selectSalePriceAndIva(int id_Product, JTextField txtPrice, JLabel labelIva) {
        
        String sql = "SELECT `salePrice`, `iva` FROM `product_price` WHERE `id_product`= ?";
    
        Connection conexion = getConnection();
    
        try {
            PreparedStatement stmt = (PreparedStatement) conexion.prepareStatement(sql);
            stmt.setInt(1, id_Product);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                txtPrice.setText(rs.getString("salePrice"));
                String iva = rs.getString("iva");
                labelIva.setText(iva + "%");
                
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
    public int selectStockActual(int id_product) {
        
        int stock = 0;

        String sql = "SELECT quantity FROM product_stock WHERE id_product = ?";

        Connection conexion = getConnection();

        try {
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, id_product);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                stock = rs.getInt("quantity");
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
        
        String sql = "SELECT `quantity`, `min` FROM `product_stock` WHERE `id_product` = ?";
        
        Connection conexion = getConnection();             

        try{            
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, idproduct);
            ResultSet rs = pstmt.executeQuery();
           
           if (rs.next()) {
               
                lblStock.setText(rs.getString("quantity"));
                txtMin.setText(rs.getString("min"));          
            } 
           
            rs.close();
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR " + e.getMessage());
        } 
        
    }
    
    //inserta el stock = 0 al igual que la configuracion básica al dar de alta un producto nuevo
    public void insertInitialStock(int id_product){
        
        String sql="INSERT INTO `product_stock`(`id_product`, `quantity`, `min`) VALUES (?,?,?)";

        int min = 0;
        int quantity = 0;
        
        Connection conexion = getConnection();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, id_product);
            pstmt.setInt(2, quantity);
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
        double benefit = 50;
        double iva = 0;
        double salePrice = 0;
        
        Connection conexion = getConnection();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, id_product);
            pstmt.setDouble(2, price);
            pstmt.setDouble(3, benefit);
            pstmt.setDouble(4, iva);
            pstmt.setDouble(5, salePrice);            
            pstmt.executeUpdate();
            
            pstmt.close();
            conexion.close();
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR PRECIO "+ e.getMessage());
        }    
    }
    
    public boolean updateStockProduct(int idProduct, double stock){
        
        String sql = "UPDATE product_stock SET quantity = ? WHERE id_product = ?";
        
        boolean valido = false;

        Connection conexion = getConnection();

        try {
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setDouble(1, stock);
            pstmt.setInt(2, idProduct);

            pstmt.executeUpdate();
            
            valido= true;
            
            pstmt.close();
            conexion.close();
            
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR AL EDITAR PRECIO" + e.getMessage());
        } 
        return valido;
    }
    
    //***************************************** COMBOS DE AJUSTE DE STOCK ***************************************
    
    public void llenarCombosAjusteStock(JComboBox combo, double tipe){
        
        String sql="SELECT * FROM `motivoajustestock` WHERE `tipe`= "+tipe+ "ORDER BY name ASC";    
        
        Connection conexion = getConnection();
        
        combo.addItem("Seleccione una categoría");
              
        try{
            Statement stmt;
            stmt=conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                combo.addItem(rs.getString("name"));
            }    
            
            rs.close();
            stmt.close();
            conexion.close();
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LOS COMBOBOX" + e.getMessage());
        }
    } 
    
    //ACTUALIZAR ALARMA DE STOCK MINIMO
    public void updateAlarmStockMin(int id_prod, int min){
        
        String sql="UPDATE `product_stock` SET `min`=? WHERE `id_product`=?";
        
        Connection conexion = getConnection();
        
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
    
    //----------------------- PROMO ------------------------
    
    //ACTIVA UNA PROMO

    public void updateProductPromotion(int id_product, Integer id_promotion){

        String sql = "UPDATE products SET id_promotion = ? WHERE id_product = ?";

        Connection conexion = getConnection();

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
    
    //******************************** LISTAR ************************************

    private void configurarTablaProductos(JTable jtable){

        tableStyleUtil.applyPoppinsHeader(jtable);

        jtable.getColumnModel().getColumn(0).setPreferredWidth(150);
        jtable.getColumnModel().getColumn(1).setPreferredWidth(500);
        jtable.getColumnModel().getColumn(2).setPreferredWidth(50);
        jtable.getColumnModel().getColumn(3).setPreferredWidth(200);
        jtable.getColumnModel().getColumn(4).setPreferredWidth(50);
        jtable.getColumnModel().getColumn(5).setPreferredWidth(200);

        jtable.getTableHeader().setReorderingAllowed(false);
    }
    
    private void listarProductosGenerico(JTable jtable, String sql, Consumer<PreparedStatement> binder) {

        DefaultTableModel dtm = crearModeloNoEditable();

        String[] columns = {"Código de producto", "Descripción", "Stock", "Promoción", "IVA", "Precio"};
        dtm.setColumnIdentifiers(columns);

        try (Connection conexion = getConnection();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            if (binder != null) {
                binder.accept(pstmt);
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                double salePrice = rs.getDouble("salePrice");
                boolean hasPrice = !rs.wasNull();
                double discount = rs.getDouble("promotion_discount");

                double finalPrice = salePrice;

                if (hasPrice && salePrice > 0 && discount > 0) {
                    finalPrice = salePrice * (1 - discount / 100.0);
                }

                Object precioAMostrar;
                if (finalPrice > 0) {
                    precioAMostrar = "$" + finalPrice;
                } else {
                    precioAMostrar = (!hasPrice || salePrice == 0) ? "SIN PRECIO" : finalPrice;
                }

                Object[] row = {
                    rs.getString("product_code"),
                    rs.getString("descripcion"),
                    rs.getInt("quantity"),
                    rs.getString("promotion_name"),
                    rs.getDouble("iva"),
                    precioAMostrar
                };

                dtm.addRow(row);
            }

            jtable.setModel(dtm);
            configurarTablaProductos(jtable);

            rs.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar productos: " + e.getMessage());
        }
    }
    
    public void listAllProduct(JTable jtable, int state) {

        String sql= "SELECT  " +
                    "p.product_code, " +
                    "CONCAT( " +
                    "sc.name, ' ', " +
                    "b.name, ' ', " +
                    "p.model, " +
                    "IF(p.color IS NOT NULL AND p.color != '', CONCAT(' Color ', p.color), '')\n" +
                    ") AS descripcion, " +
                    "st.quantity, " +
                    "pr.iva, " +
                    "pr.salePrice, " +
                    "COALESCE(pp.name, ' ') AS promotion_name, " +
                    "COALESCE(pp.discount_percentage, 0) AS promotion_discount " +
                    "FROM products p " +
                    "INNER JOIN product_subcategories sc ON p.id_subcategory = sc.id_subcategory " +
                    "INNER JOIN product_brands b ON p.id_brand = b.id_brand " +
                    "INNER JOIN product_price pr ON p.id_product = pr.id_product " +
                    "LEFT JOIN product_promotions pp ON p.id_promotion = pp.id_promotion " +
                    "INNER JOIN product_stock st ON p.id_product = st.id_product " +
                    "WHERE p.state = ?";

        listarProductosGenerico(jtable, sql, ps -> {
            try { ps.setInt(1, state); } catch (SQLException e) {}
        });
    }
    
    public void listAllProdForBrand(JTable jtable, int brand, int state) {

        String sql= "SELECT  " +
                    "p.product_code, " +
                    "CONCAT( " +
                    "sc.name, ' ', " +
                    "b.name, ' ', " +
                    "p.model, " +
                    "IF(p.color IS NOT NULL AND p.color != '', CONCAT(' Color ', p.color), '')\n" +
                    ") AS descripcion, " +
                    "st.quantity, " +
                    "pr.iva, " +
                    "pr.salePrice, " +
                    "COALESCE(pp.name, 'Sin promo') AS promotion_name, " +
                    "COALESCE(pp.discount_percentage, 0) AS promotion_discount " +
                    "FROM products p " +
                    "INNER JOIN product_subcategories sc ON p.id_subcategory = sc.id_subcategory " +
                    "INNER JOIN product_brands b ON p.id_brand = b.id_brand " +
                    "INNER JOIN product_price pr ON p.id_product = pr.id_product " +
                    "LEFT JOIN product_promotions pp ON p.id_promotion = pp.id_promotion " +
                    "INNER JOIN product_stock st ON p.id_product = st.id_product " +
                    "WHERE p.id_brand = ? AND p.state = ?";

        listarProductosGenerico(jtable, sql, ps -> {
            try { 
                ps.setInt(1, brand);
                ps.setInt(2, state);
            } catch (SQLException e) {}
        });
    }
    
    public void listProdFSubcategory(JTable jtable, int idSubcat, int state) {

        String sql= "SELECT " +
                    "p.product_code, " +
                    "CONCAT( " +
                    "sc.name, ' ', " +
                    "b.name, ' ', " +
                    "p.model, " +
                    "IF(p.color IS NOT NULL AND p.color != '', CONCAT(' Color ', p.color), '')\n" +
                    ") AS descripcion, " +
                    "st.quantity, " +
                    "pr.iva, " +
                    "pr.salePrice, " +
                    "COALESCE(pp.name, 'Sin promo') AS promotion_name, " +
                    "COALESCE(pp.discount_percentage, 0) AS promotion_discount " +
                    "FROM products p " +
                    "INNER JOIN product_subcategories sc ON p.id_subcategory = sc.id_subcategory " +
                    "INNER JOIN product_brands b ON p.id_brand = b.id_brand " +
                    "INNER JOIN product_price pr ON p.id_product = pr.id_product " +
                    "LEFT JOIN product_promotions pp ON p.id_promotion = pp.id_promotion " +
                    "INNER JOIN product_stock st ON p.id_product = st.id_product " +
                    "WHERE sc.id_subcategory = ? AND p.state = 1";

        listarProductosGenerico(jtable, sql, ps -> {
            try { 
                ps.setInt(1, idSubcat); 
                ps.setInt(2, state);
            } catch (SQLException e) {}
        });
    }
    
    public void listProdForBrandAndSubCat(JTable jtable, int brand, int subcat, int state) {

        String sql= "SELECT " +
                    "p.product_code, " +
                    "CONCAT( " +
                    "sc.name, ' ', " +
                    "b.name, ' ', " +
                    "p.model, " +
                    "IF(p.color IS NOT NULL AND p.color != '', CONCAT(' Color ', p.color), '')\n" +
                    ") AS descripcion, " +
                    "st.quantity, " +
                    "pr.iva, " +
                    "pr.salePrice, " +
                    "COALESCE(pp.name, 'Sin promo') AS promotion_name, " +
                    "COALESCE(pp.discount_percentage, 0) AS promotion_discount " +
                    "FROM products p " +
                    "INNER JOIN product_subcategories sc ON p.id_subcategory = sc.id_subcategory " +
                    "INNER JOIN product_brands b ON p.id_brand = b.id_brand " +
                    "INNER JOIN product_price pr ON p.id_product = pr.id_product " +
                    "LEFT JOIN product_promotions pp ON p.id_promotion = pp.id_promotion " +
                    "INNER JOIN product_stock st ON p.id_product = st.id_product " +
                    "WHERE p.id_brand = ? AND sc.id_subcategory = ? AND p.state = ?";

        listarProductosGenerico(jtable, sql, ps -> {
            try {
                ps.setInt(1, brand);
                ps.setInt(2, subcat);
                ps.setInt(3, state);
            } catch (SQLException e) {}
        });
    }
    
    public void listProdFCategory(JTable jtable, int id_category, int state) {

        String sql= "SELECT " +
                    "p.product_code, " +
                    "CONCAT( " +
                    "sc.name, ' ', " +
                    "b.name, ' ', " +
                    "p.model, " +
                    "IF(p.color IS NOT NULL AND p.color != '', CONCAT(' Color ', p.color), '')\n" +
                    ") AS descripcion, " +
                    "st.quantity, " +
                    "pr.iva, " +
                    "pr.salePrice, " +
                    "COALESCE(pp.name, 'Sin promo') AS promotion_name, " +
                    "COALESCE(pp.discount_percentage, 0) AS promotion_discount " +
                    "FROM products p " +
                    "INNER JOIN product_subcategories sc ON p.id_subcategory = sc.id_subcategory " +
                    "INNER JOIN product_brands b ON p.id_brand = b.id_brand " +
                    "INNER JOIN product_price pr ON p.id_product = pr.id_product " +
                    "LEFT JOIN product_promotions pp ON p.id_promotion = pp.id_promotion " +
                    "INNER JOIN product_stock st ON p.id_product = st.id_product " +
                    "WHERE sc.id_category = ? AND p.state = ?";

        listarProductosGenerico(jtable, sql, ps -> {
            try { 
                ps.setInt(1, id_category); 
                ps.setInt(2, state);
            } catch (SQLException e) {}
        });
    }
    
    public void listProdForBrandAndCat(JTable jtable, int brand, int id_category, int state) {

        String sql= "SELECT " +
                    "p.product_code, " +
                    "CONCAT( " +
                    "sc.name, ' ', " +
                    "b.name, ' ', " +
                    "p.model, " +
                    "IF(p.color IS NOT NULL AND p.color != '', CONCAT(' Color ', p.color), '')\n" +
                    ") AS descripcion, " +
                    "st.quantity, " +
                    "pr.iva, " +
                    "pr.salePrice, " +
                    "COALESCE(pp.name, 'Sin promo') AS promotion_name, " +
                    "COALESCE(pp.discount_percentage, 0) AS promotion_discount " +
                    "FROM products p " +
                    "INNER JOIN product_subcategories sc ON p.id_subcategory = sc.id_subcategory " +
                    "INNER JOIN product_brands b ON p.id_brand = b.id_brand " +
                    "INNER JOIN product_price pr ON p.id_product = pr.id_product " +
                    "LEFT JOIN product_promotions pp ON p.id_promotion = pp.id_promotion " +
                    "INNER JOIN product_stock st ON p.id_product = st.id_product " +
                    "WHERE p.id_brand = ? AND sc.id_category = ? AND p.state = ?";

        listarProductosGenerico(jtable, sql, ps -> {
            try {
                ps.setInt(1, brand);
                ps.setInt(2, id_category);
                ps.setInt(3, state);
            } catch (SQLException e) {}
        });
    }
    
    public void insertProductHistory(int id_product, String action, String information){    
    
        String sql = "INSERT INTO `product_history`(`id_product`, `date`, `action`, `information`) VALUES (?,?,?,?)";
        
        Date fecha = new Date();
                
        Connection conexion = getConnection();

        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            
            pstmt.setInt(1, id_product);
            pstmt.setTimestamp(2, new java.sql.Timestamp(fecha.getTime()));
            pstmt.setString(3, action);
            pstmt.setString(4, information);
            
            pstmt.executeUpdate();            
            conexion.close(); 
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }

    }    

    public void listProductHistory(JTable jtable, int id_product){

        String sql = "SELECT date, action, information FROM product_history WHERE id_product = ?";

        DefaultTableModel dtm = crearModeloNoEditable();

        Connection conexion = getConnection();

        String[] titleTable = {"Fecha", "Operación", "Motivo"};
        dtm.setColumnIdentifiers(titleTable);

        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, id_product);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("date"),
                    rs.getString("action"),
                    rs.getString("information")
                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
            
            tableStyleUtil.applyPoppinsHeader(jtable);

            jtable.getColumnModel().getColumn(0).setPreferredWidth(200);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(300);           
            jtable.getColumnModel().getColumn(2).setPreferredWidth(700);

            jtable.getTableHeader().setReorderingAllowed(false);

            rs.close();
            conexion.close();

        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }  
    
    public void llenarComboProducto(JComboBox combo, int factura){

        String sql =
                "SELECT " +
                "p.id_product, " +
                "ps.name AS scat, " +
                "pb.name AS brand, " +
                "p.model AS model, " +
                "COALESCE(p.color, '') AS color " +
                "FROM purchase_invoice_detail AS pid " +
                "INNER JOIN products p ON pid.id_product = p.id_product " +
                "INNER JOIN product_subcategories ps ON p.id_subcategory = ps.id_subcategory " +
                "INNER JOIN product_brands pb ON p.id_brand = pb.id_brand " +
                "WHERE id_purchase_invoice = ?";

        Connection conexion = getConnection();

        combo.removeAllItems();

        try {

            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setInt(1, factura);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){

                int idProduct = rs.getInt("id_product");

                String subcat = rs.getString("scat");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                String color = rs.getString("color");

                String item = subcat + " " + brand + " " + model + " " + color;

                combo.addItem(new ComboProducto(idProduct, item));
            }

            conexion.close();

        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
    }
    
    public class ComboProducto {

        private int idProduct;
        private String descripcion;

        public ComboProducto(int idProduct, String descripcion) {
            this.idProduct = idProduct;
            this.descripcion = descripcion;
        }

        public int getIdProduct() {
            return idProduct;
        }

        @Override
        public String toString() {
            return descripcion;
        }
    }
    
    public void listProductSN(int id_product,int factura,DefaultTableModel dtm){

        String sql= "SELECT " +
                    "ps.id_product, " +
                    "subc.name AS subcat, " +
                    "pb.name AS brand, " +
                    "p.model AS model, " +
                    "COALESCE(p.color, '') AS color, " +
                    "ps.serial_number AS serialNumber " +
                    "FROM product_serials AS ps " +
                    "INNER JOIN products p ON ps.id_product = p.id_product " +
                    "INNER JOIN product_subcategories subc ON p.id_subcategory = subc.id_subcategory " +
                    "INNER JOIN product_brands pb ON p.id_brand = pb.id_brand " +
                    "WHERE ps.id_purchase_invoice = ? " +
                    "AND ps.id_product = ?";

        Connection conexion = getConnection();

        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setInt(1, factura);
            pstmt.setInt(2, id_product);
            ResultSet rs = pstmt.executeQuery();

            dtm.setRowCount(0);

            while(rs.next()){

                int idProduct = rs.getInt("id_product");

                String subcat = rs.getString("subcat");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                String color = rs.getString("color");

                String item = subcat + " " + brand + " " + model + " " + color;

                Object[] row = {idProduct, item, rs.getString("serialNumber")};
                dtm.addRow(row);
            }

            conexion.close();

        } catch(SQLException e)
            {JOptionPane.showMessageDialog(null,"ERROR: " + e.getMessage());
        }
    }    
    
    public int obtenerStockDeCompra(int factura, int producto){
        
        String sql = "SELECT quantity FROM purchase_invoice_detail WHERE id_purchase_invoice = ? AND id_product = ?";
         
        Connection conexion = getConnection();
        
        int stock = 0;
        
        try {

            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setInt(1, factura);
            pstmt.setInt(2, producto);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                stock = rs.getInt("quantity");
            }

            conexion.close();

        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
        
        return stock;
    }
    
    public boolean insertProductSerialNumber(int id_product, int id_purchase, String serialNumber, LocalDate fecha, String status, String observation){
        
        String sql= "INSERT INTO `product_serials`" +
                    "(`id_product`, `id_purchase_invoice`, `serial_number`, `fecha_de_registro`, `status`, `observations`) " +
                    "VALUES (?,?,?,?,?,?)";
        
        boolean valido = false;
        
        Connection conexion = getConnection();
        
        try {

            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setInt(1, id_product);
            pstmt.setInt(2, id_purchase);
            pstmt.setString(3, serialNumber);
            pstmt.setDate(4, java.sql.Date.valueOf(fecha));
            pstmt.setString(5, status);
            pstmt.setString(6, observation);
            
            int resultado = pstmt.executeUpdate();
            
            if(resultado > 0){
                valido = true;
            }

            conexion.close();
            pstmt.close();

        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
        return valido;             
    }
    
    public void selectProductSN(
            String serialNumber,
            JLabel lbl_status,
            JLabel lbl_product,
            JLabel lbl_color,
            JLabel lbl_warranty,
            JLabel lbl_purchase_date,
            JLabel lbl_supplier,
            JLabel lbl_purchase_invoice,                   
            JLabel lbl_purchase_price,
            JLabel lbl_iva,
            JLabel lbl_sale_date,
            JLabel lbl_sale_price,
            JTextArea textAreaObservation         
    ){
        
        String sql= "SELECT " +
                    "psubcat.name AS subcategory, " +
                    "pbrand.name AS brand, " +
                    "p.model AS model, " +
                    "p.color AS color, " +
                    "pi.number AS purchase, " +
                    "pi.fecha_compra AS purchaseDate, " +
                    "s.name AS supplier, " +
                    "ps.status AS estado, " +
                    "pid.price AS purchasePrice, " +
                    "pid.iva AS iva, " +
                    "ps.observations " +
                    "FROM product_serials ps " +
                    "INNER JOIN products p ON ps.id_product = p.id_product " +
                    "INNER JOIN product_subcategories psubcat ON p.id_subcategory = psubcat.id_subcategory " +
                    "INNER JOIN product_brands pbrand ON p.id_brand = pbrand.id_brand " +
                    "INNER JOIN purchase_invoice pi ON ps.id_purchase_invoice = pi.id_purchase_invoice " +
                    "INNER JOIN suppliers s ON pi.id_supplier = s.id_supplier " +
                    "INNER JOIN purchase_invoice_detail pid " +
                    "ON ps.id_purchase_invoice = pid.id_purchase_invoice " +
                    "AND ps.id_product = pid.id_product " +
                    "WHERE ps.serial_number = ?";
        
        Connection conexion = getConnection();
        
        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setString(1, serialNumber);
            
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                
                String subcat = rs.getString("subcategory");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                String product = subcat + " " + brand + " " + model;
                            
                lbl_status.setText(rs.getString("estado"));
                lbl_product.setText(product);
                lbl_color.setText(rs.getString("color"));
                lbl_purchase_date.setText(rs.getString("purchaseDate"));
                lbl_supplier.setText(rs.getString("supplier"));
                lbl_purchase_invoice.setText(rs.getString("purchase"));
                lbl_warranty.setText("-");
                lbl_sale_date.setText("-");
                lbl_sale_price.setText("-");
                lbl_iva.setText(rs.getString("iva"));
                lbl_purchase_price.setText(rs.getString("purchasePrice"));
                textAreaObservation.setText(rs.getString("observations"));
                
                
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el producto");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar producto: " + e.getMessage());
        }

        
    }
    

    
    
    


}
