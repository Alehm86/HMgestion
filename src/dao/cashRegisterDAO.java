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
import java.time.LocalDate;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import session.session;


public class cashRegisterDAO {
    
    connectionDAO Connection = new connectionDAO();
    genericDAO qGeneric = new genericDAO();
       
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
                     "c.name AS cliente, " +
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

        Connection conexion = Connection.getConnection();

        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setString(1, serviceNumber);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                Object[] row = {
                    rs.getString("fecha"),
                    "Servicio técnico",
                    rs.getString("comprobante"),
                    rs.getString("cliente"),
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
                qGeneric.mensajeError();
                System.out.println("ERROR EN: cashRegisterDAO: listServiceOperation. " + e.getMessage());
            }

            rs.close();
            pstmt.close();
            conexion.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
    }
    
    public void listBudgetOperation(
            int idBudget,
            DefaultTableModel dtmOperation,
            DefaultTableModel dtmProduct,
            DefaultTableModel dtmService){     
        
        String sqlBudget= "SELECT " +
                          "b.nro_budget AS comprobante, " +
                          "b.date AS fecha, " +
                          "b.customer_name AS cliente, " +
                          "b.total AS total " +
                          "FROM budget AS b " +
                          "WHERE b.id_budget = ?";
        
        String sqlBudgetItem = "SELECT " +
                               "b.nro_budget AS nroBudget, " +
                               "bd.description AS descripcion, " +
                               "bd.type AS tipo, " +
                               "COALESCE(bd.id_product, 0) AS idProduct, " +
                               "bd.quantity AS cantidad, " +
                               "bd.price AS precioUnitario, " +
                               "bd.iva AS iva, " +
                               "bd.subtotal AS total " +
                               "FROM budget_detail AS bd " +
                               "INNER JOIN budget b ON bd.id_budget = b.id_budget " +
                               "WHERE bd.id_budget = ?";
            
        Connection conexion = Connection.getConnection();
        
        try {
            PreparedStatement pstmt = conexion.prepareStatement(sqlBudget);
            pstmt.setInt(1, idBudget);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                Object[] row = {
                    rs.getString("fecha"),
                    "Presupuesto",
                    rs.getString("comprobante"),
                    rs.getString("cliente"),
                    rs.getDouble("total")
                };
                dtmOperation.addRow(row);
            }

            try {
                PreparedStatement pstmt2 = conexion.prepareStatement(sqlBudgetItem);
                pstmt2.setInt(1, idBudget);
                ResultSet rs2 = pstmt2.executeQuery();

                while (rs2.next()) {

                    String tipo = rs2.getString("tipo");

                    if ("product".equals(tipo)) {

                        Integer idProduct = (Integer) rs2.getObject("idProduct");

                        Object[] rowProd = {
                            rs2.getString("idProduct"),
                            rs2.getString("nroBudget"),
                            rs2.getString("descripcion"),
                            rs2.getInt("cantidad"),
                            rs2.getDouble("precioUnitario"),
                            rs2.getString("iva"),
                            rs2.getDouble("total")
                        };

                        dtmProduct.addRow(rowProd);

                    } else if ("service".equals(tipo)) {

                        Object[] rowServ = {
                            rs2.getString("nroBudget"),
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
                qGeneric.mensajeError();
                System.out.println("ERROR EN: cashRegisterDAO: listBudgetOperation. " + e.getMessage());
            }

            rs.close();
            pstmt.close();
            conexion.close();

        } catch (SQLException e) {
            qGeneric.mensajeError();
            System.out.println("ERROR EN: cashRegisterDAO: listBudgetOperation. " + e.getMessage());
        }
        
    }
    
    public int cashRegister(
            Connection conn,
            Integer id_customer,    
            double total,
            double discount,
            double totalFinal,
            String observations
    ){
        
        String sqlInsert = "INSERT INTO `sales`" +
                           "(id_customer, id_user, total, discount, total_final, observations) " +
                           "VALUES (?,?,?,?,?,?)";
        
        String sqlUpdate = "UPDATE sales SET sale_number = ? WHERE `id_sale` = ?";
          
        LocalDate fechaLocal = LocalDate.now();
        
        int idGenerado = 0;
        int id_user = session.getCurrentUser().getId();       
        
        try{
            conn.setAutoCommit(false);
            PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            
            if (id_customer == null) {
                pstmtInsert.setNull(1, java.sql.Types.INTEGER);
            } else {
                pstmtInsert.setInt(1, id_customer);
            }
            
            pstmtInsert.setInt(2, id_user);
            pstmtInsert.setDouble(3, total);
            pstmtInsert.setDouble(4, discount);
            pstmtInsert.setDouble(5,totalFinal);
            pstmtInsert.setString(6,observations);
            
            pstmtInsert.executeUpdate();            
            ResultSet rs = pstmtInsert.getGeneratedKeys();

            if(rs.next()){
                idGenerado = rs.getInt(1);
            }

            rs.close();
            pstmtInsert.close();
            
            if(idGenerado > 0){

                String sale_number = "RV-" + String.format("%06d", idGenerado);

                PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate);
                pstmtUpdate.setString(1, sale_number);
                pstmtUpdate.setInt(2, idGenerado);

                pstmtUpdate.executeUpdate();
                pstmtUpdate.close();
            }         
            
        }catch(SQLException e){
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("ERROR EN: cashRegisterDAO: cashRegister. " + e.getMessage());          
        }
        return idGenerado;
    }
    
    public boolean insertMethodPayments(Connection conn, int id_sale, String method, double total){
        
        String sql = "INSERT INTO `sales_payments`(`id_sale`, `method`, `total`) VALUES (?,?,?)";
        
        boolean status = false;
        
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);         
            pstmt.setInt(1, id_sale);
            pstmt.setString(2, method);
            pstmt.setDouble(3, total);
            
            pstmt.executeUpdate();
            status = true;
            
            pstmt.close();
            
        }catch(SQLException e){
            System.out.println("ERROR EN: cashRegisterDAO: insertMethodPayments. " + e.getMessage()); 
        }
        return status;
    }
    
    public boolean insertCashRegDetail(
            Connection conn,
            int id_CashReg,
            String operation,
            String description,
            String type,
            Integer idProd,
            int quantity,
            double price,
            String iva,
            double subtotal    
        ){
        
        boolean status = false;
        
        String sql ="INSERT INTO `sales_detail`(`id_sale`, `operation`, `description`, `type`, `id_product`, `quantity`, `price`, `iva`, `subtotal`) " +
                    "VALUES (?,?,?,?,?,?,?,?,?)";
        
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);             
            pstmt.setInt(1, id_CashReg);
            pstmt.setString(2, operation);
            pstmt.setString(3, description);
            pstmt.setString(4, type);
            
            if (idProd != null && idProd > 0) {
                pstmt.setInt(5, idProd);
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }
            
            pstmt.setInt(6, quantity);
            pstmt.setDouble(7, price);
            pstmt.setString(8, iva);
            pstmt.setDouble(9, subtotal);
            
            pstmt.executeUpdate();   
            
            status = true;

            pstmt.close();
            
        }
        catch(SQLException e){
            System.out.println("ERROR EN: cashRegisterDAO: insertMethodPayments. " + e.getMessage());
        }
        return status;
    }
    
        public boolean serviceDespachar(Connection conn,String serviceNumber){
        
        String sql = "UPDATE `service_orders` SET `id_status`= 8 ,`delivery_date`= ? WHERE `service_number` = ?";
        
        boolean estado = false;
        LocalDate fechaLocal = LocalDate.now();           
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);         
            pstmt.setDate(1, java.sql.Date.valueOf(fechaLocal));
            pstmt.setString(2, serviceNumber);
            pstmt.executeUpdate();
              
            pstmt.close();
            estado = true;

        }
        catch(SQLException e){
            qGeneric.mensajeError();
            System.out.println("ERROR EN: cashRegisterDAO: serviceDespachar. " + e.getMessage());
        } 
        
        if(estado){
            JOptionPane.showMessageDialog(null, "Equipo entregado!.");
        }
        
        return estado;
    } 
        
    public boolean updateStateBudget(Connection conn, String budgetNumber){
        
        String sql = "UPDATE `budget` SET `id_state`= 6 WHERE `nro_budget`=?";
        
        boolean estado = false;
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);        
            pstmt.setString(1, budgetNumber);
            pstmt.executeUpdate();
              
            pstmt.close();
            estado = true;

        }
        catch(SQLException e){
            qGeneric.mensajeError();
            System.out.println("ERROR EN: cashRegisterDAO: updateStateBudget. " + e.getMessage());
        }
        return estado;
    }    

}
