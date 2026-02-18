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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


public class CustomerDAO {
    
//    REGISTRAR CLIENTE NUEVO
    public void insertCustomer(
            String name,
            int id_iva,
            String cuit,
            String email,
            String phone,
            String street,
            int height,
            String city,
            int id_province,
            String registration_date,
            int id_state
        ){    
    
        String sql = "INSERT INTO client " +
                     "(name, id_iva, cuit, email, phone, street, height, city, id_province, registration_date, id_state) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,?,?)";        
        
        ConnectionDB con = new ConnectionDB();
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
            pstmt.setString(10, registration_date);
            pstmt.setInt(11, id_state);
            
            pstmt.executeUpdate();            
            conexion.close(); 
            
            JOptionPane.showMessageDialog(null, "Cliente registrado con exito.");
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR CLIENTE");
        }

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
        
        ConnectionDB con = new ConnectionDB();
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
    
    public void updateState(String cuit, int state){    
    
        String sql = "UPDATE client SET id_state=? WHERE cuit=?";      
        
        ConnectionDB con = new ConnectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
               
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
            pstmt.setInt(1, state);
            pstmt.setString(2, cuit);     
            
            pstmt.executeUpdate();            
            conexion.close(); 
            
            JOptionPane.showMessageDialog(null, "CLIENTE DADO DE BAJA.");
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR");
        }

    }    
    
    public boolean existsByCuit(String cuit) {
        String sql = "SELECT COUNT(*) FROM `client` WHERE `cuit` = ?";
    
        ConnectionDB con = new ConnectionDB();
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
            "client.email, " +
            "client.phone, " +
            "client.street, " +
            "client.height, " +
            "client.city, " +
            "provinces.name AS province_name, " +
            "stateclient.name AS stateclient_name " +
            "FROM client " +
            "INNER JOIN iva ON client.id_iva = iva.id_iva " +
            "INNER JOIN provinces ON client.id_province = provinces.id_province " +
            "INNER JOIN stateclient ON client.id_state = stateclient.id_state " +
            "WHERE client.cuit ="+cuit;
        
        Statement stmt;               
        ConnectionDB con = new ConnectionDB();
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
                txtHeight.setText(String.valueOf(rs.getInt("height")));
                txtCity.setText(rs.getString("city"));
                txtIva.setText(rs.getString("iva_name"));
                txtProvince.setText(rs.getString("province_name"));
                txtState.setText(rs.getString("stateclient_name"));

            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el cliente");
            }
            rs.close();
            stmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR X");
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

        ConnectionDB con = new ConnectionDB();
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
                "stateclient.name AS stateclient_name " +
                "FROM client " +
                "INNER JOIN iva ON client.id_iva = iva.id_iva " +
                "INNER JOIN stateclient ON client.id_state = stateclient.id_state";
        
        Statement stmt;
        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ConnectionDB con = new ConnectionDB();
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
                    rs.getString("stateclient_name"),

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
                "stateclient.name AS stateclient_name " +
                "FROM client " +
                "INNER JOIN iva ON client.id_iva = iva.id_iva " +
                "INNER JOIN stateclient ON client.id_state = stateclient.id_state " +
                "WHERE client.id_state ="+state;
        
        Statement stmt;
        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ConnectionDB con = new ConnectionDB();
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
                    rs.getString("stateclient_name"),

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

        ConnectionDB con = new ConnectionDB();
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
                "stateclient.name AS stateclient_name " +
                "FROM client " +
                "INNER JOIN iva ON client.id_iva = iva.id_iva " +
                "INNER JOIN stateclient ON client.id_state = stateclient.id_state " +
                "WHERE client.id_iva = " + iva;

        
        Statement stmt;
        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ConnectionDB con = new ConnectionDB();
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
                    rs.getString("stateclient_name"),

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
                "stateclient.name AS stateclient_name " +
                "FROM client " +
                "INNER JOIN iva ON client.id_iva = iva.id_iva " +
                "INNER JOIN stateclient ON client.id_state = stateclient.id_state " +
                "WHERE client.id_state = "+ state +" AND client.id_iva = " + iva;

        
        Statement stmt;
        
        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ConnectionDB con = new ConnectionDB();
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
                    rs.getString("stateclient_name"),

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

        ConnectionDB con = new ConnectionDB();
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

        ConnectionDB con = new ConnectionDB();
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

    public static int selectIdState(String name){
        String sql = "SELECT id_state FROM stateclient WHERE name = ?";
        int id_state = -1;

        ConnectionDB con = new ConnectionDB();
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
        String sql="SELECT `name` FROM `stateclient`";
        Statement stmt;

        ConnectionDB con = new ConnectionDB();
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

    
    
}
