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

public class currentAccountDAO {
    
    
    connectionDAO Connection = new connectionDAO();
    genericDAO qGeneric = new genericDAO();
    
    public boolean insertMovCtaCte(
            Connection conn,
            int idCustomer,
            String operation,
            int id_sale,
            Integer id_product,
            Integer id_service,
            String descripcion,
            Integer quantity,
            Double price,
            String iva,
            Double debito,
            Double credito,
            String status
    ){
        
        String sql = "INSERT INTO `current_account`" +
                     "(`id_customer`, `date`, `operation`, `id_sale`, `id_product`, `id_service`, `description`, " +
                     "`quantity`, `price`, `iva`, `debit`, `credit`, `status`, `id_user`)" +
                     "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        boolean estado = false;
        
        LocalDate fechaLocal = LocalDate.now();
        
        int id_user = session.getCurrentUser().getId();

        try{
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setInt(1, idCustomer);
            pstmt.setDate(2, java.sql.Date.valueOf(fechaLocal));           
            pstmt.setString(3, operation);       
            pstmt.setInt(4, id_sale); 
            
            if (id_product != null && id_product > 0) {
                pstmt.setInt(5, id_product);
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            } 
            
            if (id_service != null && id_service > 0) {
                pstmt.setInt(6, id_service);
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            } 
            
            pstmt.setString(7, descripcion);

            if (quantity != null && quantity > 0) {
                pstmt.setInt(8, quantity);
            } else {
                pstmt.setNull(8, java.sql.Types.INTEGER);
            }      
            
            if (price != null && price > 0) {
                pstmt.setDouble(9, price);
            } else {
                pstmt.setNull(9, java.sql.Types.DOUBLE);
            } 
                       
//            pstmt.setInt(8, quantity);
//            pstmt.setDouble(9, price);
            pstmt.setString(10, iva);
     
            if (debito != null && debito > 0) {
                pstmt.setDouble(11, debito);
            } else {
                pstmt.setNull(11, java.sql.Types.DOUBLE);
            }          
            
            if (credito != null && credito > 0) {
                pstmt.setDouble(12, credito);
            } else {
                pstmt.setNull(12, java.sql.Types.DOUBLE);
            }       
            pstmt.setString(13, status);
            pstmt.setInt(14, id_user);
          
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
        
}
