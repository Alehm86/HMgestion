/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.util.Date;

public class modelPurchaseInvoice {
    
    String type;
    String number;
    int supplier_id;
    Date fechaCompra;
    double subtotal;
    Double discount;
    Double iva_10_5;
    Double iva_21;
    Double percepciones;
    Double impInterno;
    double total;
    String status;
    String notes;
    Date created_at;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getIva_10_5() {
        return iva_10_5;
    }

    public void setIva_10_5(Double iva_10_5) {
        this.iva_10_5 = iva_10_5;
    }

    public Double getIva_21() {
        return iva_21;
    }

    public void setIva_21(Double iva_21) {
        this.iva_21 = iva_21;
    }

    public Double getPercepciones() {
        return percepciones;
    }

    public void setPercepciones(Double percepciones) {
        this.percepciones = percepciones;
    }

    public Double getImpInterno() {
        return impInterno;
    }

    public void setImpInterno(Double impInterno) {
        this.impInterno = impInterno;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public modelPurchaseInvoice(String type, String number, int supplier_id, Date fechaCompra, double subtotal, Double discount, Double iva_10_5, Double iva_21, Double percepciones, Double impInterno, double total, String status, String notes, Date created_at) {
        this.type = type;
        this.number = number;
        this.supplier_id = supplier_id;
        this.fechaCompra = fechaCompra;
        this.subtotal = subtotal;
        this.discount = discount;
        this.iva_10_5 = iva_10_5;
        this.iva_21 = iva_21;
        this.percepciones = percepciones;
        this.impInterno = impInterno;
        this.total = total;
        this.status = status;
        this.notes = notes;
        this.created_at = created_at;
    }

    public modelPurchaseInvoice(){}
    
    
}
