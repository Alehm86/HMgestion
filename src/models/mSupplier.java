/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Ale
 */
public class mSupplier {
    public String name;
    public String condicion;
    public String cuit;
    public String telefono;
    public String email;
    public String web;
    public String user;
    public String pass;
    public int state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public mSupplier(String name, String condicion, String cuit, String telefono, String email, String web, String user, String pass, int state) {
        this.name = name;
        this.condicion = condicion;
        this.cuit = cuit;
        this.telefono = telefono;
        this.email = email;
        this.web = web;
        this.user = user;
        this.pass = pass;
        this.state = state;
    }


    
    public mSupplier(){
        
    }
}
