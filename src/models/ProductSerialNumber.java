/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDate;
import java.util.Date;


public class ProductSerialNumber {
    
    int id_product;
    int id_purchase;
    String serialNumber;
    LocalDate fechaRegistro;
    String status;
    String observations;

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public int getId_purchase() {
        return id_purchase;
    }

    public void setId_purchase(int id_purchase) {
        this.id_purchase = id_purchase;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public ProductSerialNumber(int id_product, int id_purchase, String serialNumber, LocalDate fechaRegistro, String status, String observations) {
        this.id_product = id_product;
        this.id_purchase = id_purchase;
        this.serialNumber = serialNumber;
        this.fechaRegistro = fechaRegistro;
        this.status = status;
        this.observations = observations;
    }


    
    
}
