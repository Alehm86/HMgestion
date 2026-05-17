/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classDAO;

import ConnectionDB.connectionDB;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import utils.config;

public class purchaseInvoiceDAO {
       
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
    
    public int insertPurchaseInvoice(
            String type, 
            String number, 
            int idSupplier, 
            Date fecha_compra, 
            double subtotal, 
            Double discount,
            Double iva_10_5,
            Double iva_21,
            Double percepciones,
            Double imp_interno,
            double total,
            String notes)
    {
        
        String sql= "INSERT INTO `purchase_invoice`(" +
                    "`type`, `number`, `id_supplier`, `fecha_compra`, `subtotal`, " +
                    "`discount`, `iva_10_5`, `iva_21`, `percepciones`, `imp_interno`, `total`, " +
                    "`status`, `notes`, `fecha_de_registro`, `stock_cargado`) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        int idGenerado = 0;
        int stockCargado = 0;
        String status = "Activo";
        LocalDate fechaLocal = LocalDate.now();
               
        Connection conexion = getConnection();
       
        try{
            PreparedStatement pstmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, type);
            pstmt.setString(2, number);
            pstmt.setInt(3, idSupplier);
            pstmt.setDate(4, new java.sql.Date(fecha_compra.getTime()));
            pstmt.setDouble(5, subtotal);
            
            if (discount != null) {
                pstmt.setDouble(6, discount);
            } else {
                pstmt.setNull(6, java.sql.Types.DECIMAL);
            }
            
            if (iva_10_5 != null) {
                pstmt.setDouble(7, iva_10_5);
            } else {
                pstmt.setNull(7, java.sql.Types.DECIMAL);
            }
            
            if (iva_21 != null) {
                pstmt.setDouble(8, iva_21);
            } else {
                pstmt.setNull(8, java.sql.Types.DECIMAL);
            }
            
            if (percepciones != null) {
                pstmt.setDouble(9, percepciones);
            } else {
                pstmt.setNull(9, java.sql.Types.DECIMAL);
            }
            
            if (imp_interno != null) {
                pstmt.setDouble(10, imp_interno);
            } else {
                pstmt.setNull(10, java.sql.Types.DECIMAL);
            }
            
            pstmt.setDouble(11, total);
            pstmt.setString(12, status);
            
            if (notes != null && !notes.trim().isEmpty()) {
                pstmt.setString(13, notes.trim());
            } else {
                pstmt.setNull(13, java.sql.Types.VARCHAR);
            }
            
            pstmt.setDate(14, java.sql.Date.valueOf(fechaLocal));
            pstmt.setInt(15, stockCargado);
            
            pstmt.executeUpdate(); 
            
            ResultSet rs = pstmt.getGeneratedKeys();

            if(rs.next()){
                idGenerado = rs.getInt(1);
            }
            
            rs.close();
            conexion.close(); 
            pstmt.close();
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }  
        
        return idGenerado;
    }
    
    public boolean insertPurchaceInvoiceDetail(
            int id_Purchase_invoice,
            int id_product,
            int amount,
            double price,
            String iva,
            double total
        ){
        
        boolean status = false;
        
        String sql ="INSERT INTO `purchase_invoice_detail`" +
                    "(`id_purchase_invoice`, `id_product`, `amount`, `price`, `iva`, `total`) " +
                    "VALUES (?,?,?,?,?,?)";
        
        Connection conexion = getConnection();
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
            
            pstmt.setInt(1, id_Purchase_invoice);
            pstmt.setInt(2, id_product);
            pstmt.setInt(3, amount);
            pstmt.setDouble(4, price);
            pstmt.setString(5, iva);
            pstmt.setDouble(6, total);
            
            pstmt.executeUpdate();   
            
            status = true;
            conexion.close(); 
            pstmt.close();
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
        return status;
    }
    
    public void listPurchaseInvoice(JTable tabla){  

        String sql= "SELECT " +
                    "pi.id_purchase_invoice, " +
                    "pi.type AS tipo, " +
                    "pi.number AS numero, " +
                    "s.name AS proveedor, " +
                    "pi.fecha_compra AS fecha, " +
                    "pi.total, " +
                    "pi.status AS estado " +
                    "FROM purchase_invoice pi " +
                    "INNER JOIN suppliers s ON pi.id_supplier = s.id_supplier " +
                    "WHERE pi.stock_cargado = 0";

        DefaultTableModel dtm = crearModeloNoEditable();
        
        Statement stmt;
        
        Connection conexion = getConnection();
        
        String[] titleTable = {"id","Fecha","Tipo","Numero","Proveedor","Total","Estado"};
        dtm.setColumnIdentifiers(titleTable);

        try{            
            stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);    

            while (rs.next()) {

                double total = rs.getDouble("total");
                String totalFormateado = "$" + total;

                Object[] row = {
                    rs.getString("id_purchase_invoice"),
                    rs.getString("fecha"),
                    rs.getString("tipo"),
                    rs.getString("numero"),
                    rs.getString("proveedor"),
                    totalFormateado,
                    rs.getString("estado") 
                };

                dtm.addRow(row);
            }
            tabla.setModel(dtm);
            
            config.TableStyleUtil.applyPoppinsHeader(tabla);    
            
            tabla.getColumnModel().getColumn(0).setMinWidth(0);
            tabla.getColumnModel().getColumn(0).setMaxWidth(0);
            tabla.getColumnModel().getColumn(0).setPreferredWidth(0);

            tabla.getColumnModel().getColumn(0).setPreferredWidth(0);
            tabla.getColumnModel().getColumn(1).setPreferredWidth(200);
            tabla.getColumnModel().getColumn(2).setPreferredWidth(200);
            tabla.getColumnModel().getColumn(3).setPreferredWidth(250);
            tabla.getColumnModel().getColumn(4).setPreferredWidth(500);
            tabla.getColumnModel().getColumn(5).setPreferredWidth(200);
            tabla.getColumnModel().getColumn(6).setPreferredWidth(200);

            tabla.getTableHeader().setReorderingAllowed(false);

            rs.close();
            stmt.close();
            conexion.close();

        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR " + e.getMessage());
        } 
    }  
    
    public void listAllPurchase(JTable tabla){  

        String sql= "SELECT " +
                    "pi.id_purchase_invoice, " +
                    "pi.type AS tipo, " +
                    "pi.number AS numero, " +
                    "s.name AS proveedor, " +
                    "pi.fecha_compra AS fecha, " +
                    "pi.total, " +
                    "pi.status AS estado " +
                    "FROM purchase_invoice pi " +
                    "INNER JOIN suppliers s ON pi.id_supplier = s.id_supplier ";

        DefaultTableModel dtm = crearModeloNoEditable();
        
        Statement stmt;
        
        Connection conexion = getConnection();
        
        String[] titleTable = {"id","Fecha","Tipo","Numero","Proveedor","Total","Estado"};
        dtm.setColumnIdentifiers(titleTable);

        try{            
            stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);    

            while (rs.next()) {

                double total = rs.getDouble("total");
                String totalFormateado = "$" + total;

                Object[] row = {
                    rs.getString("id_purchase_invoice"),
                    rs.getString("fecha"),
                    rs.getString("tipo"),
                    rs.getString("numero"),
                    rs.getString("proveedor"),
                    totalFormateado,
                    rs.getString("estado") 
                };

                dtm.addRow(row);
            }
            tabla.setModel(dtm);
            
            config.TableStyleUtil.applyPoppinsHeader(tabla);    
            
            tabla.getColumnModel().getColumn(0).setMinWidth(0);
            tabla.getColumnModel().getColumn(0).setMaxWidth(0);
            tabla.getColumnModel().getColumn(0).setPreferredWidth(0);

            tabla.getColumnModel().getColumn(0).setPreferredWidth(0);
            tabla.getColumnModel().getColumn(1).setPreferredWidth(200);
            tabla.getColumnModel().getColumn(2).setPreferredWidth(200);
            tabla.getColumnModel().getColumn(3).setPreferredWidth(250);
            tabla.getColumnModel().getColumn(4).setPreferredWidth(500);
            tabla.getColumnModel().getColumn(5).setPreferredWidth(200);
            tabla.getColumnModel().getColumn(6).setPreferredWidth(200);

            tabla.getTableHeader().setReorderingAllowed(false);

            rs.close();
            stmt.close();
            conexion.close();

        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR " + e.getMessage());
        } 
    }
    
    public boolean selectPuchaseInvoice(
        int idPurchase,
        JLabel labelType,
        JLabel labelNumber,
        JLabel labelSupplier,
        JLabel labelFecha,
        JLabel labelSubtotal,
        JLabel labelPersepciones,
        JLabel labelOtherTaxes,
        JLabel lblDiscount,
        JLabel lbl_iva_105,
        JLabel lbl_iva_21,
        JLabel lblTotal,
        JTable jTableItems
        ){  
          
        String sql_purchase_date = 
                "SELECT " +
                "pi.type AS tipo, " +
                "pi.number AS numero, " +
                "s.name AS supplier, " +
                "pi.fecha_compra AS fecha, " +
                "pi.subtotal AS subtotal, " +
                "COALESCE(pi.discount, '$0,00') AS descuento, " +
                "COALESCE(pi.iva_10_5, '$0,00') AS iva_10_5, " +
                "COALESCE(pi.iva_21, '$0,00') AS iva_21, " +
                "COALESCE(pi.percepciones, '$0,00') AS percepciones, " +
                "COALESCE(pi.imp_interno, '$0,00') AS imp_interno, " +
                "pi.total " +
                "FROM `purchase_invoice` AS pi " +
                "INNER JOIN suppliers s ON pi.id_supplier = s.id_supplier " +
                "WHERE id_purchase_invoice = ?";
        
        String sql_detail = 
                "SELECT " +
                "ps.name AS subcat, " +
                "pb.name AS brand, " +
                "p.model AS model, " +
                "COALESCE(p.color, '') AS color, " +
                "pid.id_product AS idProduct, " +
                "pid.amount AS cantidad " +              
                "FROM purchase_invoice_detail AS pid " +
                "INNER JOIN products p ON pid.id_product = p.id_product " +
                "INNER JOIN product_subcategories ps ON p.id_subcategory = ps.id_subcategory " +
                "INNER JOIN product_brands pb ON p.id_brand = pb.id_brand " +
                "WHERE pid.id_purchase_invoice = ?";
        
        boolean state = false;
                    
        Connection conexion = getConnection();
        
        DefaultTableModel dtm = crearModeloNoEditable();
        
        String[] titulo = new String[]{"id_Product", "Cant", "Producto"};
        dtm.setColumnIdentifiers(titulo);        
                  
        try{            
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql_purchase_date);
            pstmt.setInt(1, idPurchase);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                labelType.setText(rs.getString("tipo"));
                labelNumber.setText(rs.getString("numero"));
                labelSupplier.setText(rs.getString("supplier"));               
                labelFecha.setText(rs.getString("fecha"));
                labelSubtotal.setText("$"+rs.getString("subtotal"));
                labelPersepciones.setText(rs.getString("percepciones"));
                labelOtherTaxes.setText(rs.getString("imp_interno"));
                lblDiscount.setText(rs.getString("descuento"));
                lbl_iva_105.setText("$"+rs.getString("iva_10_5"));
                lbl_iva_21.setText("$"+rs.getString("iva_21"));
                lblTotal.setText("$"+rs.getString("total"));

            } else {
                JOptionPane.showMessageDialog(null, "No se encontró factura");
            }
            rs.close();
            pstmt.close();
            
            PreparedStatement pstmt2 = conexion.prepareStatement(sql_detail);
            pstmt2.setInt(1, idPurchase);
            ResultSet rs2 = pstmt2.executeQuery();

            while (rs2.next()) {
                
                String subCat = rs2.getString("subcat");
                String brand = rs2.getString("brand");        
                String model = rs2.getString("model");        
                String color = rs2.getString("color");
                String producto = subCat + " " + brand + " " + model + " " + color;
                
                Object[] row = {  
                    rs2.getInt("idProduct"),
                    rs2.getInt("cantidad"),
                    producto
                };
                dtm.addRow(row);
                state = true;
            }
            
            jTableItems.setModel(dtm);
            
            config.TableStyleUtil.applyPoppinsHeader(jTableItems);
            
            jTableItems.getTableHeader().setFont(new Font("Poppins", Font.PLAIN, 14));
            jTableItems.getTableHeader().setResizingAllowed(false);

            DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) jTableItems.getTableHeader().getDefaultRenderer();

            headerRenderer.setHorizontalAlignment(JLabel.CENTER);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            
            jTableItems.getColumnModel().getColumn(0).setMinWidth(0);
            jTableItems.getColumnModel().getColumn(0).setMaxWidth(0);
            jTableItems.getColumnModel().getColumn(0).setPreferredWidth(0);

            jTableItems.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); 
            jTableItems.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); 

            
            jTableItems.getColumnModel().getColumn(1).setPreferredWidth(50);
            jTableItems.getColumnModel().getColumn(2).setPreferredWidth(600); 

            rs2.close();
            pstmt2.close();                 
        }
        catch(SQLException e){
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return state;
    } 
    
    public void selectPurchaseInvoice2(
            int IdPurchase,
            JLabel labelFecha,
            JLabel labelSupplier,
            JLabel labelType,
            JLabel labelNumber
    ){
        
        String sql = 
                "SELECT " +
                "pi.type AS tipo, " +
                "pi.number AS numero, " +
                "s.name AS supplier, " +
                "pi.fecha_compra AS fecha " +
                "FROM purchase_invoice AS pi " +
                "INNER JOIN suppliers s ON pi.id_supplier = s.id_supplier " +
                "WHERE pi.id_purchase_invoice = ?";
        
        Connection conexion = getConnection();
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, IdPurchase);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                labelType.setText(rs.getString("tipo"));
                labelNumber.setText(rs.getString("numero"));
                labelSupplier.setText(rs.getString("supplier"));               
                labelFecha.setText(rs.getString("fecha"));
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró factura");
            }
            rs.close();
            pstmt.close();
        
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
        
    }
    
    public static int selectIdPurchase(String number){
        
        String sql = "SELECT `id_purchase_invoice` FROM `purchase_invoice` WHERE `number` = ?";
        
        int id = -1;

        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();

        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setString(1, number);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id_purchase_invoice");
            }
            
            rs.close();
            pstmt.close();
            conexion.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return id;
    }    
    
    public void updateStockCargadoInPurchase(int id_purchase_invoice){
        
        String sql = "UPDATE `purchase_invoice` SET `stock_cargado`= 1 WHERE `id_purchase_invoice`=?";
        
        Connection conexion = getConnection();
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, id_purchase_invoice);
            pstmt.executeUpdate();
            
            pstmt.close();
            conexion.close();       
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
    }
    
    
}
