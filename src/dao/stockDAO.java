/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import dao.genericDAO;
import dao.connectionDAO;

public class stockDAO {
    
    connectionDAO Connection = new connectionDAO();
    genericDAO qGeneric = new genericDAO();
    
    public boolean updateStock(Connection conn, int id_product, int cantidad){

        String sql = "UPDATE product_stock " +
                     "SET quantity = quantity - ? " +
                     "WHERE id_product = ?";

        boolean status = false;

        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, cantidad);
            pstmt.setInt(2, id_product);

            int rows = pstmt.executeUpdate();
            System.out.println("Filas afectadas: " + rows);
            return rows > 0;

        }catch(SQLException e){
            System.out.println("ERROR EN: stockDAO: updateStock. " + e.getMessage());
            return false;
        }

    }
}
