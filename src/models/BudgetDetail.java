/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;


public class BudgetDetail {
    
    String description;
    int quantity;
    double price;
    String iva;
    double subtotal;
    String type;
    Integer idProduct;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public BudgetDetail(String description, int quantity, double price, String iva, double subtotal, String type, Integer idProduct) {
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.iva = iva;
        this.subtotal = subtotal;
        this.type = type;
        this.idProduct = idProduct;
    }
 
    public BudgetDetail(){}
    
}
