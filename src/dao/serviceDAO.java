/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import ConnectionDB.connectionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ale
 */
public class serviceDAO {
    
    public void insertDevice(String deviceType, String brand, String model, String serialNumber, String description){
        
        String sql="INSERT INTO `devices`(`device_type`, `brand`, `model`, `serial_number`, `description`) VALUES (?,?,?,?,?)";
               
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
            
            pstmt.setString(1, deviceType);
            pstmt.setString(2, brand);
            pstmt.setString(3, model);
            pstmt.setString(4, serialNumber);
            pstmt.setString(5, description);
            
            pstmt.executeUpdate();   
            
            conexion.close(); 
            pstmt.close();
            
            JOptionPane.showMessageDialog(null, "Producto registrado!");
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR DISPOSITIVO" + e.getMessage());
        }    
    }
    
    //VERIFICA SI EL NUMERO DE SERIE EXISTE.
    public boolean serialNumberExists(String serialNumber) {
        
        String sql = "SELECT * FROM devices WHERE serial_number = ?";
        
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        try {         
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            
            pstmt.setString(1, serialNumber);
        
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
    
    public void selectDevice(
        String serialNumber,
        JLabel lbl_idDevice,
        JLabel lblSerialNumber,
        JLabel lblDevice,
        JLabel lblBrand,
        JLabel lblModel,
        JLabel lblDescription
        ){  
        
        
        String sql = 
                "SELECT " +
                "id_device, " +
                "device_type, " +
                "COALESCE(brand, 'Sin dato') AS brand, " +
                "COALESCE(model, 'Sin dato') AS model, " +
                "COALESCE(description, 'Sin dato') AS description " +
                "FROM `devices` " +
                "WHERE `serial_number`=?";
                    
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
                  
        try{            
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setString(1, serialNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                lblSerialNumber.setText(serialNumber); 
                lbl_idDevice.setText(rs.getString("id_device"));
                lblDevice.setText(rs.getString("device_type"));               
                lblBrand.setText(rs.getString("brand"));
                lblModel.setText(rs.getString("model"));
                lblDescription.setText(rs.getString("description"));


            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el dispositivo");
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR " + e.getMessage());
        } 
    }  

    public int insertService(
            int id_customer,
            int id_device,
            String reported_problem,
            String status,
            String entry_date
    ){    

        int idGenerado = 0;

        String sql = "INSERT INTO service_orders " +
                     "(id_customer, id_device, reported_problem, status, entry_date) " +
                     "VALUES (?,?,?,?,?)";       

        connectionDB con = new connectionDB();
        Connection conexion = con.establecerConexion();

        try{
            PreparedStatement pstmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, id_customer);
            pstmt.setInt(2, id_device);
            pstmt.setString(3, reported_problem);
            pstmt.setString(4, status);
            pstmt.setString(5, entry_date);

            pstmt.executeUpdate();            

            ResultSet rs = pstmt.getGeneratedKeys();

            if(rs.next()){
                idGenerado = rs.getInt(1);
            }

            rs.close();
            pstmt.close();
            conexion.close();

        } catch(SQLException e){
            System.out.println("ERROR AL REGISTRAR ORDEN: " + e.getMessage());
        }

        return idGenerado;
    }    
    
    public void insertService_order_status_history(
            int id_service,
            String status,
            String entry_date
        ){    
    
        String sql = "INSERT INTO `service_order_status_history`(`id_service`, `status`, `date`) VALUES (?,?,?)";       
        
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();

        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            
            pstmt.setInt(1, id_service);
            pstmt.setString(2, status);
            pstmt.setString(3, entry_date);
            
            pstmt.executeUpdate();            
            conexion.close(); 

        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR Service_order_status_history" + e.getMessage());
        }
    }
    
    public void listCustomerDevices(JTable jtable, int id_customer){

        String sql = 
            "SELECT d.device_type, " +
            "COALESCE(d.brand, 'Sin dato') AS brand, " +
            "COALESCE(d.model, 'Sin dato') AS model, " +
            "d.serial_number, " +
            "COALESCE(d.description, 'Sin dato') AS description " +
            "FROM service_orders s " +
            "INNER JOIN devices d ON s.id_device = d.id_device " +
            "WHERE s.id_customer = ?";

        DefaultTableModel dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        connectionDB con = new connectionDB();
        Connection conexion = con.establecerConexion();

        String[] titleTable = {"Tipo de dispositivo","Marca","Modelo","Nº de serie","Descripción"};
        dtm.setColumnIdentifiers(titleTable);

        try{
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setInt(1, id_customer);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                Object[] row = {
                    rs.getString("device_type"),
                    rs.getString("brand"),
                    rs.getString("model"),
                    rs.getString("serial_number"),
                    rs.getString("description")
                };

                dtm.addRow(row);
            }

            jtable.setModel(dtm);
             
            jtable.getColumnModel().getColumn(4).setPreferredWidth(300);

            jtable.getTableHeader().setReorderingAllowed(false);

            rs.close();
            pstmt.close();
            conexion.close();

        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
    }
}
