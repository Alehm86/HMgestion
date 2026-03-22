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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


public class customerDAO {
    
//    REGISTRAR CLIENTE NUEVO
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
    
        String sql = "INSERT INTO client " +
                     "(name, id_iva, cuit, email, phone, street, height, city, id_province, registration_date, id_state) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        int idGenerado = 0;
        
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();

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
            
            JOptionPane.showMessageDialog(null, "Cliente registrado con exito.");
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR CLIENTE");
        }
        return idGenerado;
    }
    
    public void updateCustomer(
            int id_client,
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
    
        String sql = "UPDATE client SET " +
                "name=?, id_iva=?, cuit=?, email=?, phone=?, street=?, height=?, city=?, id_province=? " +
                "WHERE id_client=?";      
        
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
               
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
            pstmt.setInt(10, id_client);     
            
            pstmt.executeUpdate();            
            conexion.close(); 
            
            JOptionPane.showMessageDialog(null, "Datos de cliente actualizados.");
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL EDITAR CLIENTE");
        }

    }
    
    public void updateState(int id_client, int state){    
    
        String sql = "UPDATE client SET id_state=? WHERE id_client =?";      
        
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
               
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
            pstmt.setInt(1, state);
            pstmt.setInt(2, id_client);     
            
            pstmt.executeUpdate();            
            conexion.close(); 
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR " + e.getMessage());
        }

    }    
    
    public boolean existsByCuit(String cuit) {
        String sql = "SELECT COUNT(*) FROM `client` WHERE `cuit` = ?";
    
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();       
    
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
            JOptionPane.showMessageDialog(null, "Error al verificar existencia de cliente por CUIT");
        }
        return false;
    }   
    
    
    public void selectClientEdit(
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
            "client.name AS client_name, " +
            "client.id_client, " +            
            "iva.name AS iva_name, " +
            "client.cuit, " +
            "COALESCE(client.email, 'Sin dato') AS email, " +
            "client.phone, " +
            "COALESCE(client.street, 'Sin dato') AS street, " +
            "COALESCE(client.height, '0') AS height, " +
            "COALESCE(client.city, 'Sin dato') AS city, " +
            "COALESCE(provinces.name, '25') AS province_name, " +
            "customerState.name AS customerState_name " +
            "FROM client " +
            "INNER JOIN iva ON client.id_iva = iva.id_iva " +
            "INNER JOIN provinces ON client.id_province = provinces.id_province " +
            "INNER JOIN customerState ON client.id_state = customerState.id_state " +
            "WHERE client.cuit = '" + cuit + "'";
        
        Statement stmt;               
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
                  
        try{            
           stmt=conexion.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
           

            if (rs.next()) {

                txtID.setText(rs.getString("id_client"));                 
                txtName.setText(rs.getString("client_name"));               
                txtCUIT.setText(rs.getString("cuit"));
                txtTel.setText(rs.getString("phone"));
                txtEmail.setText(rs.getString("email"));
                txtStreet.setText(rs.getString("street"));
                txtHeight.setText(rs.getString("height"));
                txtCity.setText(rs.getString("city"));
                txtIva.setText(rs.getString("iva_name"));
                txtProvince.setText(rs.getString("province_name"));
                txtState.setText(rs.getString("customerState_name"));

            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el cliente");
            }
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR " + e.getMessage());
        } 
    }
    
    public void selectClient(
            String cuit,
            JLabel lbl_ID,
            JTextField txtName,
            JTextField txtCUIT,
            JTextField txtTel,
            JLabel lbl_Iva
            ){  
           
        String sql =
                "SELECT " +
                "client.name AS client_name, " +
                "client.id_client, " +
                "iva.name AS iva_name, " +
                "client.cuit, " +
                "client.phone " +
                "FROM client " +
                "INNER JOIN iva ON client.id_iva = iva.id_iva " +
                "WHERE client.cuit =?";
                     
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
                  
        try{            
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql); 
            pstmt.setString(1, cuit);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                
                lbl_ID.setText(rs.getString("id_client"));                 
                txtName.setText(rs.getString("client_name"));               
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
    
    public void selectSupplier(
            int id,
            JLabel lbl_ID,
            JLabel lbl_cuit,
            JLabel lbl_Iva,
            JLabel lbl_phone,
            JTextField txtName
            ){  
        
        
        String sql =
                "SELECT " +
                "client.name AS client_name, " +
                "client.id_client, " +
                "iva.name AS iva_name, " +
                "client.cuit, " +
                "client.phone " +
                "FROM client " +
                "INNER JOIN iva ON client.id_iva = iva.id_iva " +
                "WHERE client.cuit =?";
                     
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
                  
        try{            
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql); 
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                
//                lbl_ID.setText(rs.getString("id_client"));                 
//                txtName.setText(rs.getString("client_name"));               
//                txtCUIT.setText(rs.getString("cuit"));
//                txtTel.setText(rs.getString("phone"));
//                lbl_Iva.setText(rs.getString("iva_name"));

            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el cliente");
                               
                lbl_ID.setText("");
                txtName.setText("");
                lbl_cuit.setText("");
                lbl_phone.setText("");
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
    
    //no se usa
    public void listAllCustomer(JTable jtable){
        
        String sql = "SELECT * FROM `client`";  
        Statement stmt;
        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        String[] titleTable = {"NOMBRE", "CUIT / DNI"};
        dtm.setColumnIdentifiers(titleTable);
        
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
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(250);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(250);           

            jtable.getTableHeader().setReorderingAllowed(false);
            
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR");
        }
    }
    
    public void listAllCustomerComplete(JTable jtable){
        
        String sql = 
                "SELECT client.name AS client_name, " +
                "iva.name AS iva_name, " +
                "client.cuit, " +
                "client.email, " +
                "client.phone, " +
                "client.city, " +
                "customerState.name AS customerState_name " +
                "FROM client " +
                "INNER JOIN iva ON client.id_iva = iva.id_iva " +
                "INNER JOIN customerState ON client.id_state = customerState.id_state";
        
        Statement stmt;
        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        String[] titleTable = {"Nombre","CUIT / DNI","IVA","Teléfono","Email","Ciudad","Estado"};
        dtm.setColumnIdentifiers(titleTable);
        
        try{
            stmt=conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Object[] row = {
                    rs.getString("client_name"),
                    rs.getString("cuit"),
                    rs.getString("iva_name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("city"),
                    rs.getString("customerState_name"),

                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(100);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(30);           

            jtable.getTableHeader().setReorderingAllowed(false);
            
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR");
        }
    }  
    
        public void listAllCustomerForState(JTable jtable, int state){
        
        String sql = 
                "SELECT client.name AS client_name, " +
                "iva.name AS iva_name, " +
                "client.cuit, " +
                "client.email, " +
                "client.phone, " +
                "client.city, " +
                "customerState.name AS customerState_name " +
                "FROM client " +
                "INNER JOIN iva ON client.id_iva = iva.id_iva " +
                "INNER JOIN customerState ON client.id_state = customerState.id_state " +
                "WHERE client.id_state ="+state;
        
        Statement stmt;
        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        String[] titleTable = {"Nombre","CUIT / DNI","IVA","Teléfono","Email","Ciudad","Estado"};
        dtm.setColumnIdentifiers(titleTable);
        
        try{
            stmt=conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Object[] row = {
                    rs.getString("client_name"),
                    rs.getString("cuit"),
                    rs.getString("iva_name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("city"),
                    rs.getString("customerState_name"),

                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(100);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(30);           

            jtable.getTableHeader().setReorderingAllowed(false);
            
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR");
        }
    }
    
    public void listCustomerForIva(JTable jtable, int iva){                     //no lo estoy usando (revisar y borrar)
        
        String SQL="SELECT `name`, `cuit` FROM `client` WHERE `id_state`= 1 AND `id_iva`="+iva;
        
        Statement stmt;
        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        String[] titleTable = {"NOMBRE", "CUIT / DNI"};
        dtm.setColumnIdentifiers(titleTable);
        
        try{
            stmt=conexion.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                Object[] row = {
                    rs.getString("name"),
                    rs.getString("cuit"),

                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(250);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(250);           

            jtable.getTableHeader().setReorderingAllowed(false);
            
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR");
        }
    }
    
    public void listCustomerForIvaComplete(JTable jtable, int iva){
        
        String SQL =
                "SELECT client.name AS client_name, " +
                "iva.name AS iva_name, " +
                "client.cuit, " +
                "client.email, " +
                "client.phone, " +
                "client.city, " +
                "customerState.name AS customerState_name " +
                "FROM client " +
                "INNER JOIN iva ON client.id_iva = iva.id_iva " +
                "INNER JOIN customerState ON client.id_state = customerState.id_state " +
                "WHERE client.id_iva = " + iva;

        
        Statement stmt;
        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        String[] titleTable = {"Nombre","CUIT / DNI","IVA","Teléfono","Email","Ciudad","Estado"};
        dtm.setColumnIdentifiers(titleTable);
        
        try{
            stmt=conexion.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                Object[] row = {
                    rs.getString("client_name"),
                    rs.getString("cuit"),
                    rs.getString("iva_name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("city"),
                    rs.getString("customerState_name"),

                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(100);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(30);          

            jtable.getTableHeader().setReorderingAllowed(false);
            
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR");
        }
    }    
    
    public void listCustomerForIvaAndState(JTable jtable, int iva, int state){
        
        String SQL =
                "SELECT client.name AS client_name, " +
                "iva.name AS iva_name, " +
                "client.cuit, " +
                "client.email, " +
                "client.phone, " +
                "client.city, " +
                "customerState.name AS customerState_name " +
                "FROM client " +
                "INNER JOIN iva ON client.id_iva = iva.id_iva " +
                "INNER JOIN customerState ON client.id_state = customerState.id_state " +
                "WHERE client.id_state = "+ state +" AND client.id_iva = " + iva;

        
        Statement stmt;
        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        String[] titleTable = {"Nombre","CUIT / DNI","IVA","Teléfono","Email","Ciudad","Estado"};
        dtm.setColumnIdentifiers(titleTable);
        
        try{
            stmt=conexion.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                Object[] row = {
                    rs.getString("client_name"),
                    rs.getString("cuit"),
                    rs.getString("iva_name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("city"),
                    rs.getString("customerState_name"),

                };
                dtm.addRow(row);
            }

            jtable.setModel(dtm);
            
            jtable.getColumnModel().getColumn(0).setPreferredWidth(100);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(30);          

            jtable.getTableHeader().setReorderingAllowed(false);
            
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR");
        }
    }        
    
    public static int selectIdIva(String valor){
        String sql = "SELECT id_iva FROM iva WHERE name = ?";
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
            JOptionPane.showMessageDialog(null, "Error al obtener ID.");
        }
        return id;
    }  
    
    public static String selectCUIT(String name){
        String sql = "SELECT cuit FROM client WHERE name = ?";
        String cuit = "";

        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();

        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                cuit = rs.getString("cuit");
            }
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener ID.");
        }
        return cuit;
    }   
    
    public static int selectId_customer(String cuit){
        String sql = "SELECT id_client FROM client WHERE cuit = ?";
        int id = -1;

        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();

        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setString(1, cuit);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id_client");
            }
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener ID.");
        }
        return id;
    } 

    public static int selectIdState(String name){
        String sql = "SELECT id_state FROM customerState WHERE name = ?";
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
            JOptionPane.showMessageDialog(null, "Error al obtener ID.");
        }
        return id_state;
    }
        
    public void ComboIdState(JComboBox combo){
        String sql="SELECT `name` FROM `customerState`";
        Statement stmt;

        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        combo.addItem("Todos");

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
    
    public void insertCustomerHistory(int id_client,String date,String action,String information){    
    
        String sql = "INSERT INTO `customer_history`(`id_client`, `date`, `action`, `information`) VALUES (?,?,?,?)";
        
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();

        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            
            pstmt.setInt(1, id_client);
            pstmt.setString(2, date);
            pstmt.setString(3, action);
            pstmt.setString(4, information);
            
            pstmt.executeUpdate();            
            conexion.close(); 
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR HISTORIAL");
        }

    }    

    public void listCustomerHistory(JTable jtable, int id_client){

        String sql = "SELECT date, action, information FROM customer_history WHERE id_client = ?";

        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();

        String[] titleTable = {"Fecha", "Operación", "Motivo"};
        dtm.setColumnIdentifiers(titleTable);

        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, id_client);
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

            jtable.getColumnModel().getColumn(0).setPreferredWidth(200);
            jtable.getColumnModel().getColumn(1).setPreferredWidth(300);           
            jtable.getColumnModel().getColumn(2).setPreferredWidth(700);

            jtable.getTableHeader().setReorderingAllowed(false);

            rs.close();
            conexion.close();

        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al cargar historial del cliente");
        }
    }    
    
    
    
}
