/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;


public class modelBudgetDetail {
    
    String description;
    int amount;
    double price;
    String iva;
    double subtotal;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public modelBudgetDetail(String description, int amount, double price, String iva, double subtotal) {
        this.description = description;
        this.amount = amount;
        this.price = price;
        this.iva = iva;
        this.subtotal = subtotal;
    }
    
    public modelBudgetDetail(){}
    
}
