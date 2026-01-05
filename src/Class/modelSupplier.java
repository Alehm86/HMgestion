/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

/**
 *
 * @author Ale
 */
public class modelSupplier {
    public String name;
    public String cuit;
    public String telefono;
    public String email;
    public String web;
    public String user;
    public String pass;

    public modelSupplier(String name, String cuit, String telefono, String email, String web, String user, String pass) {
        this.name = name;
        this.cuit = cuit;
        this.telefono = telefono;
        this.email = email;
        this.web = web;
        this.user = user;
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getTelephone() {
        return telefono;
    }

    public void setTelephone(String telephone) {
        this.telefono = telephone;
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
    
    public modelSupplier(){
        
    }
}
