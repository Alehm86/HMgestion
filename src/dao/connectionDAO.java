/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import ConnectionDB.connectionDB;
import java.sql.Connection;


public class connectionDAO {
   
        public Connection getConnection() {
        connectionDB con = new connectionDB();
        return con.establecerConexion();
    } 
        
}
