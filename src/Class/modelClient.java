/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.time.LocalDateTime;


public class modelClient {
    
    public String name;
    public int tipeClient;
    public int CUIT;
    public String email;
    public int phone;
    public String street;
    public int height;
    public int idProvince;
    public LocalDateTime fechaRegistro;
    public int state;
    public int iva;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTipeClient() {
        return tipeClient;
    }

    public void setTipeClient(int tipeClient) {
        this.tipeClient = tipeClient;
    }

    public int getCUIT() {
        return CUIT;
    }

    public void setCUIT(int CUIT) {
        this.CUIT = CUIT;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getIdProvince() {
        return idProvince;
    }

    public void setIdProvince(int idProvince) {
        this.idProvince = idProvince;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getIva() {
        return iva;
    }

    public void setIva(int iva) {
        this.iva = iva;
    }

    public modelClient(String name, int tipeClient, int CUIT, String email, int phone, String street, int height, int idProvince, LocalDateTime fechaRegistro, int state, int iva) {
        this.name = name;
        this.tipeClient = tipeClient;
        this.CUIT = CUIT;
        this.email = email;
        this.phone = phone;
        this.street = street;
        this.height = height;
        this.idProvince = idProvince;
        this.fechaRegistro = fechaRegistro;
        this.state = state;
        this.iva = iva;
    }


    public modelClient() {

    }
}
