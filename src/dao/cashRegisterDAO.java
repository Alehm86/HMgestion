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
import javax.swing.table.DefaultTableModel;


public class cashRegisterDAO {
    
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
    
    public void listServiceOperation(String serviceNumber, DefaultTableModel dtm){

        String sql = "SELECT " +
                     "so.entry_date AS fecha, " +
                     "so.service_number AS comprobante, " +
                     "c.name AS client, " +
                     "so.cost AS total " +
                     "FROM service_orders AS so " +
                     "INNER JOIN customer AS c " +
                     "ON so.id_customer = c.id_customer " +
                     "WHERE so.service_number = ?";

        Connection conexion = getConnection();

        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setString(1, serviceNumber);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                Object[] row = {
                    rs.getString("fecha"),
                    "Servicio técnico",
                    rs.getString("comprobante"),
                    rs.getString("client"),
                    rs.getDouble("total")
                };

                dtm.addRow(row);
            }

            rs.close();
            pstmt.close();
            conexion.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"ERROR: " + e.getMessage());
        }
    }
}
