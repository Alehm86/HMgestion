/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import ConnectionDB.connectionDB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import session.session;
import java.sql.Connection;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;


public class currentAccountDAO {
    
    
    connectionDAO Connection = new connectionDAO();
    genericDAO qGeneric = new genericDAO();
    
    public boolean insertMovCtaCte(
            Connection conn,
            int id_ca,
            String operation,
            Integer id_product,
            Integer id_service,
            String descripcion,
            Integer quantity,
            Double price,
            String iva,
            Double debito,
            Double credito
    ){
        
        String sql = "INSERT INTO `current_account_detail`" +
                     "(`id_ca`, `date`, `operation`, `id_product`, `id_service`, `description`, `quantity`, `price`, `iva`, `debit`, `credit`, `id_user`) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        
        boolean estado = false;
        
        LocalDate fechaLocal = LocalDate.now();
        
        int id_user = session.getCurrentUser().getId();

        try{
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setInt(1, id_ca);
            pstmt.setDate(2, java.sql.Date.valueOf(fechaLocal));           
            pstmt.setString(3, operation);       
            
            if (id_product != null && id_product > 0) {
                pstmt.setInt(4, id_product);
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            } 
            
            if (id_service != null && id_service > 0) {
                pstmt.setInt(5, id_service);
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            } 
            
            pstmt.setString(6, descripcion);

            if (quantity != null && quantity > 0) {
                pstmt.setInt(7, quantity);
            } else {
                pstmt.setNull(7, java.sql.Types.INTEGER);
            }      
            
            if (price != null && price > 0) {
                pstmt.setDouble(8, price);
            } else {
                pstmt.setNull(8, java.sql.Types.DOUBLE);
            } 
                      
            pstmt.setString(9, iva);
     
            if (debito != null && debito > 0) {
                pstmt.setDouble(10, debito);
            } else {
                pstmt.setNull(10, java.sql.Types.DOUBLE);
            }          
            
            if (credito != null && credito > 0) {
                pstmt.setDouble(11, credito);
            } else {
                pstmt.setNull(11, java.sql.Types.DOUBLE);
            }       
            pstmt.setInt(12, id_user);
          
            pstmt.executeUpdate();
            estado = true;
            
            pstmt.close();
                      
        }
        catch(SQLException e){
            qGeneric.mensajeError();
            System.out.println("ERROR EN: currentAcountDAO: insertMovCtaCte. " + e.getMessage());
        } 
                
        return estado;
        
    }

    public static int selectIdService(String serviceNumber){
        
        String sql = "SELECT `id_service` FROM `service_orders` WHERE `service_number` = ?";
              
        int id = 0;     
        
        connectionDB con = new connectionDB();
        java.sql.Connection conexion = (java.sql.Connection) con.establecerConexion();
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
            pstmt.setString(1, serviceNumber);
            ResultSet rs = pstmt.executeQuery();   

            while(rs.next()){
                id =(rs.getInt("id_service"));            
            }
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR" + e.getMessage());
        }
       return id; 
    }
    
//YA NO LO USO!!!!  --->   09/07/2026    
//    public boolean currentAccountExist(int id_customer){
//        
//        boolean exist = false;
//        
//        String sql = "SELECT 1 FROM current_account WHERE id_customer = ? LIMIT 1";
//        
//        connectionDB con = new connectionDB();
//        java.sql.Connection conexion = (java.sql.Connection) con.establecerConexion();
//        
//        try{
//            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
//            pstmt.setInt(1, id_customer);
//            ResultSet rs = pstmt.executeQuery();
//            
//            exist = rs.next();
//            
//            rs.close();
//            pstmt.close();
//            conexion.close();
//            
//        }catch(SQLException e){
//            JOptionPane.showMessageDialog(null, "ERROR" + e.getMessage());
//        }
//        
//        
//        return exist;
//    }
    
    public int selectCurrentAccount(int id_customer){
        
        String sql = "SELECT `id_ca` FROM `current_account` WHERE `id_customer` =?";
        
        int id_ca = -1;
        
        connectionDB con = new connectionDB();
        java.sql.Connection conexion = (java.sql.Connection) con.establecerConexion();
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
            pstmt.setInt(1, id_customer);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next()){
                id_ca =(rs.getInt("id_ca"));            
            }
            
            rs.close();
            pstmt.close();
            conexion.close();
            
        }catch(SQLException e){
            qGeneric.mensajeError();
            System.out.println("ERROR EN: currentAccountDAO: selectCurrentAccount. " + e.getMessage());
        }
        
        
        return id_ca;
    }
    
    public void listCurrentAccount(DefaultTableModel dtmCurrentAccounts){
        
        String sql = "SELECT " +
                     "ca.id_ca AS idCA, " +
                     "ca.ca_number AS caNumber, " +
                     "c.name AS name, " +
                     "c.id_customer AS idCustomer, " +
                     "c.cuit AS cuit, " +
                     "c.phone AS phone, " +
                     "c.email AS email " +
                     "FROM current_account AS ca " +
                     "INNER JOIN customer c ON ca.id_customer = c.id_customer " +
                     "WHERE `status` = ?";
        
        Connection conexion = Connection.getConnection();
               
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
            pstmt.setInt(1, 1);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                
                int idCA = rs.getInt("idCA");
                String ultimoMov = selectLastMovement(idCA);
                double valSaldo = calcularSaldoCA(idCA);
                String saldo = "$" + valSaldo;
            
                Object[] row = {
                    idCA,
                    rs.getString("caNumber"),
                    rs.getString("name"),
                    rs.getString("idCustomer"),
                    formatCUIT(rs.getString("cuit")),
                    rs.getString("phone"),
                    rs.getString("email"),
                    ultimoMov,
                    saldo
                };
                dtmCurrentAccounts.addRow(row);
            }         
            
            rs.close();
            pstmt.close();
            conexion.close();
            
        }catch(SQLException e){
            qGeneric.mensajeError();
            System.out.println("ERROR EN: currentAccountDAO: listCurrentAccount. " + e.getMessage());
        }
        
    }
    
    public void listCurrentAccountSimplified(DefaultTableModel dtmCurrentAccounts){
        
        String sql = "SELECT " +
                     "ca.id_ca AS idCA, " +
                     "ca.ca_number AS caNumber, " +
                     "c.name AS name, " +
                     "c.id_customer AS idCustomer " +
                     "FROM current_account AS ca " +
                     "INNER JOIN customer c ON ca.id_customer = c.id_customer " +
                     "WHERE `status` = ?";
        
        Connection conexion = Connection.getConnection();
               
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
            pstmt.setInt(1, 1);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                            
                Object[] row = {
                    rs.getInt("idCA"),
                    rs.getString("caNumber"),
                    rs.getString("name"),
                    rs.getString("idCustomer")
                };
                dtmCurrentAccounts.addRow(row);
            }         
            
            rs.close();
            pstmt.close();
            conexion.close();
            
        }catch(SQLException e){
            qGeneric.mensajeError();
            System.out.println("ERROR EN: currentAccountDAO: listCurrentAccountSimplified. " + e.getMessage());
        }
        
    }
    
    private String formatCUIT(String cuit) {

        if (cuit.length() == 8) {
            return cuit;
        }

        if (cuit.length() == 11) {
            return cuit.substring(0, 2) + "-" + cuit.substring(2, 10) + "-" + cuit.substring(10);
        }

        return cuit;
    }
    
    public double calcularSaldoCA(int id_ca){

        double credito = 0;
        double debito = 0;
        double saldo = 0;

        String sql = "SELECT `debit`, `credit` FROM `current_account_detail` WHERE `id_ca` = ?";

        Connection conexion = Connection.getConnection();

        try{
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setInt(1, id_ca);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){

                credito += rs.getDouble("credit");
                debito += rs.getDouble("debit");

            }

            saldo = credito - debito;

            rs.close();
            pstmt.close();
            conexion.close();

        }catch(SQLException e){
            qGeneric.mensajeError();
            System.out.println("ERROR EN: currentAccountDAO: calcularSaldoCA. " + e.getMessage());
        }

        return saldo;
    }
    
    public int insertCurrentAccount(int id_customer){

        String sqlInsert = "INSERT INTO current_account (id_customer, status, date, id_user) "
                   + "VALUES (?, ?, ?, ?)";
        
        String sqlUpdate = "UPDATE `current_account` SET `ca_number`=? WHERE `id_ca` = ?";

        LocalDate fechaLocal = LocalDate.now();
        int idGenerado = 0;
        int id_user = session.getCurrentUser().getId();

        connectionDB con = new connectionDB();
        Connection conexion = con.establecerConexion();

        try{
            conexion.setAutoCommit(false);
            PreparedStatement pstmtInsert = conexion.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);

            pstmtInsert.setInt(1, id_customer);
            pstmtInsert.setInt(2, 1);
            pstmtInsert.setDate(3, java.sql.Date.valueOf(fechaLocal));
            pstmtInsert.setInt(4, id_user);

            pstmtInsert.executeUpdate();

            ResultSet rs = pstmtInsert.getGeneratedKeys();

            if(rs.next()){
                idGenerado = rs.getInt(1);
            }

            rs.close();
            pstmtInsert.close();
            
            if(idGenerado > 0){

                String serviceNumber = "CC-" + String.format("%06d", idGenerado);

                PreparedStatement pstmtUpdate = conexion.prepareStatement(sqlUpdate);

                pstmtUpdate.setString(1, serviceNumber);
                pstmtUpdate.setInt(2, idGenerado);

                pstmtUpdate.executeUpdate();
                pstmtUpdate.close();
            }

            conexion.commit();
            conexion.close();

        }catch(SQLException e){
            try {
                conexion.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            qGeneric.mensajeError();
            System.out.println("ERROR EN: currentAccountDAO: insertCurrentAccount. " + e.getMessage());
        }

        return idGenerado;
    }
    
    public String selectLastMovement(int idCA){

        String ultimoMov = null;

        String sql = "SELECT MAX(`date`) AS lastDate FROM current_account_detail WHERE id_ca = ?";
        
        Connection conexion = Connection.getConnection();

        try{
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setInt(1, idCA);
            ResultSet rs = pstmt.executeQuery();
                  
            if(rs.next()){
                ultimoMov = rs.getString("lastDate");
            }
            
            rs.close();
            pstmt.close();
            conexion.close();
            

        }catch(SQLException e){
            qGeneric.mensajeError();
            System.out.println("ERROR EN currentAccountDAO: selectLastMovement(): " + e.getMessage());
        }

        return ultimoMov;
    }
    
    public void listCAMovements(int idCA, DefaultTableModel dtmCAMovements){
        
        dtmCAMovements.setRowCount(0);
        
        String sql = "SELECT `date`,`description`, `quantity`, `price`, `iva`, COALESCE(debit, '') AS debit, COALESCE(credit, '') AS credit " +
                     "FROM `current_account_detail` WHERE `id_ca` = ?";
        
        Connection conexion = Connection.getConnection();
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
            pstmt.setInt(1, idCA);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                            
                Object[] row = {
                    rs.getString("date"),
                    rs.getString("description"),
                    rs.getString("quantity"),
                    rs.getString("price"),
                    rs.getString("iva"),
                    rs.getString("credit"),
                    rs.getString("debit")

                };
                dtmCAMovements.addRow(row);
            }         
            
            rs.close();
            pstmt.close();
            conexion.close();
            
        }catch(SQLException e){
            qGeneric.mensajeError();
            System.out.println("ERROR EN: currentAccountDAO: listCAMovements. " + e.getMessage());
        }           
    }
        
}
