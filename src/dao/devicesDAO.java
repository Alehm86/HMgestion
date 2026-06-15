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
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import utils.tableStyleUtil;


public class devicesDAO {
    
    
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
    
    public boolean deviceExist(int id_customer){
        
        boolean exist = false;
        
        String sql = "SELECT `id_device`FROM `devices` WHERE `id_customer`= ?";
        
        Connection conexion = getConnection();
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, id_customer);
            
            ResultSet rs = pstmt.executeQuery(); 

            while (rs.next()) {
                exist = true;                
            }

            rs.close();
            pstmt.close();
            conexion.close();

        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
        return exist;
        
    }
    
    public boolean listCustomerDevices(JTable jtable, int id_customer){

        String sql= "SELECT " +
                    "d.device_type, " +
                    "COALESCE(d.brand, 'S/D') AS brand, " +
                    "COALESCE(d.model, 'S/D') AS model, " +
                    "d.serial_number " +
                    "FROM devices AS d " +
                    "WHERE d.id_customer = ?";
        
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

                Object[] row = {
                    rs.getString("device_type"),
                    rs.getString("brand"),
                    rs.getString("model"),
                    rs.getString("serial_number"),
                };
                dtm.addRow(row);
                dato = true;
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
