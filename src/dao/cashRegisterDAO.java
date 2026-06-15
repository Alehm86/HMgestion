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
    
    public void listServiceOperation(
            String serviceNumber,
            DefaultTableModel dtmOperation,
            DefaultTableModel dtmProduct,
            DefaultTableModel dtmService
    ) {

        String sql = "SELECT " +
                     "so.entry_date AS fecha, " +
                     "so.service_number AS comprobante, " +
                     "c.name AS client, " +
                     "so.cost AS total " +
                     "FROM service_orders AS so " +
                     "INNER JOIN customer AS c " +
                     "ON so.id_customer = c.id_customer " +
                     "WHERE so.service_number = ?";

        String sqlService = "SELECT " +
                            "bd.description AS descripcion, " +
                            "bd.type AS tipo, " +
                            "bd.id_product AS idProduct, " +
                            "bd.quantity AS cantidad, " +
                            "bd.price AS precioUnitario, " +
                            "bd.iva AS iva, " +
                            "bd.subtotal AS total " +
                            "FROM budget_detail AS bd " +
                            "INNER JOIN budget b ON bd.id_budget = b.id_budget " +
                            "INNER JOIN service_orders so ON b.id_service = so.id_service " +
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
                dtmOperation.addRow(row);
            }

            try {
                PreparedStatement pstmt2 = conexion.prepareStatement(sqlService);
                pstmt2.setString(1, serviceNumber);
                ResultSet rs2 = pstmt2.executeQuery();

                while (rs2.next()) {

                    String tipo = rs2.getString("tipo");

                    if ("product".equals(tipo)) {

                        Integer idProduct = (Integer) rs2.getObject("idProduct");

                        Object[] rowProd = {
                            idProduct,
                            serviceNumber,
                            rs2.getString("descripcion"),
                            rs2.getInt("cantidad"),
                            rs2.getDouble("precioUnitario"),
                            rs2.getString("iva"),
                            rs2.getDouble("total")
                        };

                        dtmProduct.addRow(rowProd);

                    } else if ("service".equals(tipo)) {

                        Object[] rowServ = {
                            serviceNumber,
                            rs2.getString("descripcion"),
                            rs2.getInt("cantidad"),
                            rs2.getDouble("precioUnitario"),
                            rs2.getString("iva"),
                            rs2.getDouble("total")
                        };

                        dtmService.addRow(rowServ);
                    }
                }

                rs2.close();
                pstmt2.close();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
            }

            rs.close();
            pstmt.close();
            conexion.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
    }
    
//        public void listServiceOperation(String serviceNumber, DefaultTableModel dtmOperation){
//
//        String sql = "SELECT " +
//                     "so.entry_date AS fecha, " +
//                     "so.service_number AS comprobante, " +
//                     "c.name AS client, " +
//                     "so.cost AS total " +
//                     "FROM service_orders AS so " +
//                     "INNER JOIN customer AS c " +
//                     "ON so.id_customer = c.id_customer " +
//                     "WHERE so.service_number = ?";
//
//        Connection conexion = getConnection();
//
//        try {
//            PreparedStatement pstmt = conexion.prepareStatement(sql);
//            pstmt.setString(1, serviceNumber);
//
//            ResultSet rs = pstmt.executeQuery();
//
//            while (rs.next()) {
//
//                Object[] row = {
//                    rs.getString("fecha"),
//                    "Servicio técnico",
//                    rs.getString("comprobante"),
//                    rs.getString("client"),
//                    rs.getDouble("total")
//                };
//
//                dtmOperation.addRow(row);
//            }
//
//            rs.close();
//            pstmt.close();
//            conexion.close();
//
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null,"ERROR: " + e.getMessage());
//        }
//    }
}
