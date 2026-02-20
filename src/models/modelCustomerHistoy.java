/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

public class modelCustomerHistoy {
    
    public int id_client;
    public String date;
    public String action;
    public String information;

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public modelCustomerHistoy(int id_client, String date, String action, String information) {
        this.id_client = id_client;
        this.date = date;
        this.action = action;
        this.information = information;
    }
        
    public modelCustomerHistoy() {
        
    }
    
    
    
    
}
