/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.time.LocalDateTime;


public class modelClient {
    
    public int ID;
    public String name;
    public String CUIT;
    public String email;
    public String phone;
    public String street;
    public int height;
    public int idProvince;
    public String city;
    public String fechaRegistro;
    public int state;
    public int iva;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCUIT() {
        return CUIT;
    }

    public void setCUIT(String CUIT) {
        this.CUIT = CUIT;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
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

    public modelClient(int ID, String name, String CUIT, String email, String phone, String street, int height, int idProvince, String city, String fechaRegistro, int state, int iva) {
        this.ID = ID;
        this.name = name;
        this.CUIT = CUIT;
        this.email = email;
        this.phone = phone;
        this.street = street;
        this.height = height;
        this.idProvince = idProvince;
        this.city = city;
        this.fechaRegistro = fechaRegistro;
        this.state = state;
        this.iva = iva;
    }
    
    public modelClient() {

    }
}
