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
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import utils.tableStyleUtil;


public class customerDAO {
    
    
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
    
    public void nameCustomer(int id_customer, JLabel titulo){
        
        String sql = "SELECT `name` FROM `customer` WHERE `id_customer` = ?";
        
        Connection conexion = getConnection();
        
        try{
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setInt(1, id_customer);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("name");
                String title = "Dispositivos de " + nombre;
                
                titulo.setText(title);
            }

            rs.close();
            pstmt.close();
            conexion.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
        
    }
    
    
    public int insertCustomer(
            String name,
            int id_iva,
            String cuit,
            String email,
            String phone,
            String street,
            Integer height,
            String city,
            Integer id_province,
            String registration_date,
            int id_state
        ){    
    
        String sql = "INSERT INTO customer " +
                     "(name, id_iva, cuit, email, phone, street, height, city, id_province, registration_date, id_state) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        int idGenerado = 0;
        
        Connection conexion = getConnection();

        try{
            PreparedStatement pstmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, name);
            pstmt.setInt(2, id_iva);
            pstmt.setString(3, cuit);
            pstmt.setString(4, email);
            pstmt.setString(5, phone);
            pstmt.setString(6, street);
            if(height != null){
                pstmt.setInt(7, height);
            }else{
                pstmt.setNull(7, java.sql.Types.INTEGER);
            }
            pstmt.setString(8, city);
            if(id_province != null){
                pstmt.setInt(9, id_province);
            }else{
                pstmt.setNull(9, java.sql.Types.INTEGER);
            }
            pstmt.setString(10, registration_date);
            pstmt.setInt(11, id_state);
            
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();

            if(rs.next()){
                idGenerado = rs.getInt(1);
            }

            rs.close();  
            pstmt.close();
            conexion.close(); 
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return idGenerado;
    }
    
    public void updateCustomer(
            int id_customer,
            String name,
            int id_iva,
            String cuit,
            String email,
            String phone,
            String street,
            int height,
            String city,
            int id_province
        ){    
    
        String sql = "UPDATE customer SET " +
                "name=?, id_iva=?, cuit=?, email=?, phone=?, street=?, height=?, city=?, id_province=? " +
                "WHERE id_customer=?";      
        
        Connection conexion = getConnection();
             
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            
            pstmt.setString(1, name);
            pstmt.setInt(2, id_iva);
            pstmt.setString(3, cuit);
            pstmt.setString(4, email);
            pstmt.setString(5, phone);
            pstmt.setString(6, street);
            pstmt.setInt(7, height);
            pstmt.setString(8, city);
            pstmt.setInt(9, id_province);
            pstmt.setInt(10, id_customer);     
            
            pstmt.executeUpdate();            
            conexion.close(); 
            
            JOptionPane.showMessageDialog(null, "Datos de cliente actualizados.");
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }

    }
    
    public void updateState(int id_customer, int state){    
    
        String sql = "UPDATE customer SET id_state=? WHERE id_customer =?";      
        
        Connection conexion = getConnection();  
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
            pstmt.setInt(1, state);
            pstmt.setInt(2, id_customer);     
            
            pstmt.executeUpdate();            
            conexion.close(); 
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR " + e.getMessage());
        }

    }    
    
    public boolean existsByCuit(String cuit) {
        String sql = "SELECT COUNT(*) FROM `customer` WHERE `cuit` = ?";
    
        Connection conexion = getConnection();       
    
        try {
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
            pstmt.setString(1, cuit);
            ResultSet rs = pstmt.executeQuery();
        
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        
            rs.close();
            conexion.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return false;
    }   
    
    
    public void selectCustomerEdit(
            String cuit,
            JTextField txtID,
            JTextField txtName,
            JTextField txtCUIT,
            JTextField txtTel,
            JTextField txtEmail,
            JTextField txtStreet,
            JTextField txtHeight,  
            JTextField txtCity,
            JTextField txtIva,
            JTextField txtProvince,
            JTextField txtState
            ){  
        
        String sql =
            "SELECT " +
            "c.name AS customer_name, " +
            "c.id_customer, " +            
            "ci.name AS iva_name, " +
            "c.cuit, " +
            "COALESCE(c.email, 'Sin dato') AS email, " +
            "c.phone, " +
            "COALESCE(c.street, 'Sin dato') AS street, " +
            "COALESCE(c.height, '0') AS height, " +
            "COALESCE(c.city, 'Sin dato') AS city, " +
            "COALESCE(provinces.name, '25') AS province_name, " +
            "cs.name AS cs_name " +
            "FROM customer c " +
            "INNER JOIN customer_iva ci ON c.id_iva = ci.id_iva " +
            "INNER JOIN provinces ON c.id_province = provinces.id_province " +
            "INNER JOIN customer_state cs ON c.id_state = cs.id_state " +
            "WHERE c.cuit = ?";
                  
        Connection conexion = getConnection();
                  
        try{            
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setString(1, cuit);
            ResultSet rs = pstmt.executeQuery();
           

            if (rs.next()) {

                txtID.setText(rs.getString("id_customer"));                 
                txtName.setText(rs.getString("customer_name"));               
                txtCUIT.setText(rs.getString("cuit"));
                txtTel.setText(rs.getString("phone"));
                txtEmail.setText(rs.getString("email"));
                txtStreet.setText(rs.getString("street"));
                txtHeight.setText(rs.getString("height"));
                txtCity.setText(rs.getString("city"));
                txtIva.setText(rs.getString("iva_name"));
                txtProvince.setText(rs.getString("province_name"));
                txtState.setText(rs.getString("cs_name"));

            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el cliente");
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR " + e.getMessage());
        } 
    }
    
    public void selectCustomer(
            String cuit,
            JLabel lbl_ID,
            JTextField txtName,
            JTextField txtCUIT,
            JTextField txtTel,
            JLabel lbl_Iva
            ){  
           
        String sql =
                "SELECT " +
                "c.name AS customer_name, " +
                "c.id_customer AS idCustomer, " +
                "ci.name AS iva_name, " +
                "c.cuit, " +
                "c.phone " +
                "FROM customer AS c " +
                "INNER JOIN customer_iva ci ON c.id_iva = ci.id_iva " +
                "WHERE c.cuit =?";
                     
        Connection conexion = getConnection();
                  
        try{            
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql); 
            pstmt.setString(1, cuit);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                
                lbl_ID.setText(rs.getString("idCustomer"));                 
                txtName.setText(rs.getString("customer_name"));               
                txtCUIT.setText(rs.getString("cuit"));
                txtTel.setText(rs.getString("phone"));
                lbl_Iva.setText(rs.getString("iva_name"));

            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el cliente");
                               
                lbl_ID.setText("");
                txtName.setText("");
                txtCUIT.setText("");
                txtTel.setText("");
                lbl_Iva.setText("");
            
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR " + e.getMessage());
        } 
    }
    
    public void selectCustomerSimplified(
            String cuit,
            JLabel lbl_ID,
            JTextField txtName,
            JTextField txtTel,
            JLabel lbl_address
            ){  
           
        String sql =
                "SELECT " +
                "c.name AS customer_name, " +
                "c.id_customer, " +
                "c.phone, " +
                "COALESCE(c.street, '') AS street, " +
                "COALESCE(c.height, '') AS height, " +
                "COALESCE(c.city, '') AS city, " +
                "COALESCE(p.name, '') AS province " +
                "FROM customer c " +
                "INNER JOIN provinces p ON c.id_province = p.id_province " +
                "WHERE c.cuit =?";
                     
        Connection conexion = getConnection();
                  
        try{            
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql); 
            pstmt.setString(1, cuit);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                
                lbl_ID.setText(rs.getString("id_customer"));     
                txtName.setText(rs.getString("customer_name"));               
                txtTel.setText(rs.getString("phone"));
                
                String calle = rs.getString("street");
                String altura = rs.getString("height");
                String ciudad = rs.getString("city");
                String provincia = rs.getString("province");
                
                String direccion = "Calle " + calle + " Nº " + altura + ", " + ciudad + " provincia de " + provincia;
                
                lbl_address.setText(direccion); 
                
                

            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el cliente");
                               
                lbl_ID.setText("");
                txtName.setText("");
                txtTel.setText("");
            
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR " + e.getMessage());
        } 
    }           
    
    //no se usa
    public void listAllCustomer(JTable jtable){
        
        String sql = "SELECT * FROM `customer`";  
        Statement stmt;
        
        DefaultTableModel dtm = crearModeloNoEditable();

        Connection conexion = getConnection();
        
        String[] titulo = {"NOMBRE", "CUIT / DNI"};
        dtm.setColumnIdentifiers(titulo);
        
        try{
            stmt=conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Object[] row = {
                    rs.getString("name"),
                    rs.getString("cuit"),

                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
            
            tableStyleUtil.applyPoppinsHeader(jtable);
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(250);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(250);           

            jtable.getTableHeader().setReorderingAllowed(false);
            
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    public void listAllCustomerComplete(JTable jtable){
        
        String sql = 
                "SELECT customer.name AS customer_name, " +
                "ci.name AS iva_name, " +
                "customer.cuit, " +
                "customer.email, " +
                "customer.phone, " +
                "customer.city, " +
                "cs.name AS cs_name " +
                "FROM customer " +
                "INNER JOIN customer_iva ci ON customer.id_iva = ci.id_iva " +
                "INNER JOIN customer_state cs ON customer.id_state = cs.id_state";
        
        Statement stmt;
        
        DefaultTableModel dtm = crearModeloNoEditable();

        Connection conexion = getConnection();
        
        String[] titleTable = {"Nombre","CUIT / DNI","IVA","Teléfono","Email","Ciudad","Estado"};
        dtm.setColumnIdentifiers(titleTable);
        
        try{
            stmt=conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Object[] row = {
                    rs.getString("customer_name"),
                    rs.getString("cuit"),
                    rs.getString("iva_name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("city"),
                    rs.getString("cs_name"),

                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
            
            tableStyleUtil.applyPoppinsHeader(jtable);
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(100);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(30);           

            jtable.getTableHeader().setReorderingAllowed(false);
            
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }  
    
        public void listAllCustomerForState(JTable jtable, int state){
        
        String sql = 
                "SELECT c.name AS customer_name, " +
                "ci.name AS iva_name, " +
                "c.cuit, " +
                "c.email, " +
                "c.phone, " +
                "c.city, " +
                "cs.name AS cs_name " +
                "FROM customer c " +
                "INNER JOIN customer_iva ci ON c.id_iva = ci.id_iva " +
                "INNER JOIN customer_state cs ON c.id_state = cs.id_state " +
                "WHERE c.id_state = ?";
        
        
        DefaultTableModel dtm = crearModeloNoEditable();

        Connection conexion = getConnection();
        
        String[] titleTable = {"Nombre","CUIT / DNI","IVA","Teléfono","Email","Ciudad","Estado"};
        dtm.setColumnIdentifiers(titleTable);
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, state);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("customer_name"),
                    rs.getString("cuit"),
                    rs.getString("iva_name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("city"),
                    rs.getString("cs_name"),

                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
            
            tableStyleUtil.applyPoppinsHeader(jtable);
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(100);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(30);           

            jtable.getTableHeader().setReorderingAllowed(false);
            
            rs.close();
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR" + e.getMessage());
        }
    }
    
    public void listCustomerForIva(JTable jtable, int iva){        
        
        String sql="SELECT `name`, `cuit` FROM `customer` WHERE `id_state`= 1 AND `id_iva`= ?";
        
        DefaultTableModel dtm = crearModeloNoEditable();

        Connection conexion = getConnection();
        
        String[] titleTable = {"NOMBRE", "CUIT / DNI"};
        dtm.setColumnIdentifiers(titleTable);
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, iva);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("name"),
                    rs.getString("cuit"),

                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
            
            tableStyleUtil.applyPoppinsHeader(jtable);
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(250);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(250);           

            jtable.getTableHeader().setReorderingAllowed(false);
            
            rs.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    public void listCustomerForIvaComplete(JTable jtable, int iva){
        
        String sql =
                "SELECT c.name AS customer_name, " +
                "ci.name AS iva_name, " +
                "c.cuit, " +
                "c.email, " +
                "c.phone, " +
                "c.city, " +
                "cs.name AS cs_name " +
                "FROM customer c " +
                "INNER JOIN customer_iva ci ON c.id_iva = ci.id_iva " +
                "INNER JOIN customer_state cs ON c.id_state = cs.id_state " +
                "WHERE c.id_iva = ?";
        
        DefaultTableModel dtm = crearModeloNoEditable();

        Connection conexion = getConnection();
        
        String[] titleTable = {"Nombre","CUIT / DNI","IVA","Teléfono","Email","Ciudad","Estado"};
        dtm.setColumnIdentifiers(titleTable);
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, iva);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("customer_name"),
                    rs.getString("cuit"),
                    rs.getString("iva_name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("city"),
                    rs.getString("cs_name"),

                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
            
            tableStyleUtil.applyPoppinsHeader(jtable);
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(100);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(30);          

            jtable.getTableHeader().setReorderingAllowed(false);
            
            rs.close();
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }    
    
    public void listCustomerForIvaAndState(JTable jtable, int iva, int state){
        
        String sql =
                "SELECT c.name AS customer_name, " +
                "ci.name AS iva_name, " +
                "c.cuit, " +
                "c.email, " +
                "c.phone, " +
                "c.city, " +
                "cs.name AS cs_name " +
                "FROM customer c " +
                "INNER JOIN customer_iva ci ON c.id_iva = ci.id_iva " +
                "INNER JOIN customer_state cs ON c.id_state = cs.id_state " +
                "WHERE c.id_state = ? AND c.id_iva = ?";

        DefaultTableModel dtm = crearModeloNoEditable();

        Connection conexion = getConnection();
        
        String[] titleTable = {"Nombre","CUIT / DNI","IVA","Teléfono","Email","Ciudad","Estado"};
        dtm.setColumnIdentifiers(titleTable);
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, iva);
            pstmt.setInt(2, state);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("customer_name"),
                    rs.getString("cuit"),
                    rs.getString("iva_name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("city"),
                    rs.getString("cs_name"),

                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
            
            tableStyleUtil.applyPoppinsHeader(jtable);
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(100);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(30);          

            jtable.getTableHeader().setReorderingAllowed(false);
            
            rs.close();
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }        
    
    public static int selectIdIva(String valor){
        String sql = "SELECT id_iva FROM customer_iva WHERE name = ?";
        int id = 0;

        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();

        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setString(1, valor);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id_iva");
            }
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return id;
    }    
    
    public static String selectCuit(String column, Object value){

        String sql = "SELECT cuit FROM customer WHERE " + column + " = ?";
        String cuit = "";

        connectionDB con = new connectionDB();
        Connection conexion = con.establecerConexion();

        try{
            PreparedStatement pstmt = conexion.prepareStatement(sql);

            if (value instanceof String) {
                pstmt.setString(1, (String) value);
            } else if (value instanceof Integer) {
                pstmt.setInt(1, (Integer) value);
            }

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                cuit = rs.getString("cuit");
            }

            rs.close();
            pstmt.close();
            conexion.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }

        return cuit;
    }
    
    public static int selectId_customer(String cuit){
        String sql = "SELECT id_customer FROM customer WHERE cuit = ?";
        int id = -1;

        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();

        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setString(1, cuit);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id_customer");
            }
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return id;
    } 

    public static int selectIdState(String name){
        String sql = "SELECT id_state FROM customer_state WHERE name = ?";
        int id_state = -1;

        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();

        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                id_state = rs.getInt("id_state");
            }
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return id_state;
    }
        
    public void ComboIdState(JComboBox combo){
        
        String sql="SELECT `name` FROM `customer_state`";
        
        Statement stmt;

        Connection conexion = getConnection(); 
        
        combo.addItem("Todos");

        try{
            stmt=conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                combo.addItem(rs.getString("name"));
            }                  
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    public void insertCustomerHistory(int id_customer, String action, String information){    
    
        String sql = "INSERT INTO `customer_history`(`id_customer`, `date`, `action`, `information`) VALUES (?,?,?,?)";
        
        Connection conexion = getConnection();

        Date fecha = new Date();
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            
            pstmt.setInt(1, id_customer);
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

    public void listCustomerHistory(JTable jtable, int id_customer){

        String sql = "SELECT date, action, information FROM customer_history WHERE id_customer = ?";

        DefaultTableModel dtm = crearModeloNoEditable();

        Connection conexion = getConnection();

        String[] titleTable = {"Fecha", "Operación", "Motivo"};
        dtm.setColumnIdentifiers(titleTable);

        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, id_customer);
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
    
    public boolean listCustomerDevices(JTable jtable, int id_customer){

        String sql = "SELECT DISTINCT " +
                    "d.device_type, " +
                    "COALESCE(d.brand, 'S/D') AS brand, " +
                    "COALESCE(d.model, 'S/D') AS model, " +
                    "d.serial_number " +
                    "FROM service_orders s " +
                    "INNER JOIN devices d " +
                    "ON s.id_device = d.id_device " +
                    "WHERE s.id_customer = ?";
        
        boolean dato = false;

        DefaultTableModel dtm = crearModeloNoEditable();

        Connection conexion = getConnection();

        String[] titleTable = {"Tipo","Marca","Modelo","Numero de serie"};
        dtm.setColumnIdentifiers(titleTable);

        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, id_customer);
            
            ResultSet rs = pstmt.executeQuery(); 

            while (rs.next()) {

                dato = true;
                
                Object[] row = {
                    rs.getString("device_type"),
                    rs.getString("brand"),
                    rs.getString("model"),
                    rs.getString("serial_number"),
                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
            
            tableStyleUtil.applyPoppinsHeader(jtable);

            rs.close();
            pstmt.close();
            conexion.close();

        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
        return dato;
    }    
    
    
    
    
}
