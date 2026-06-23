/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

public class mCashRegDetail {
    
    String operation;
    String description;
    String type;
    Integer id_product;
    int quantity;
    double price;
    String iva;
    double subtotal;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId_product() {
        return id_product;
    }

    public void setId_product(Integer id_product) {
        this.id_product = id_product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public mCashRegDetail(String operation, String description, String type, Integer id_product, int quantity, double price, String iva, double subtotal) {
        this.operation = operation;
        this.description = description;
        this.type = type;
        this.id_product = id_product;
        this.quantity = quantity;
        this.price = price;
        this.iva = iva;
        this.subtotal = subtotal;
    }

    public mCashRegDetail() {
    }
    
    
}
