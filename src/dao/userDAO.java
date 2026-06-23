/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import ConnectionDB.connectionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.table.DefaultTableModel;
import models.mUser;
import java.sql.ResultSet;
import java.sql.SQLException;


public class userDAO {
    
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
    
    public mUser getUserById(int id_user){

        mUser usuario = null;

        String sql = "SELECT * FROM users WHERE id_user = ?";

        Connection conexion = getConnection();
        
        try{
            PreparedStatement pstmt = conexion.prepareStatement(sql);          
            pstmt.setInt(1, id_user);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){

                usuario = new mUser();

                usuario.setId(rs.getInt("id_user"));
                usuario.setUsername(rs.getString("user"));
                usuario.setName(rs.getString("full_name"));
                usuario.setActive(rs.getInt("active"));

            }

        }catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return usuario;
    }
}
