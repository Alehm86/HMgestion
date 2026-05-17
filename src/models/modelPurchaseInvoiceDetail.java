/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;


public class modelPurchaseInvoiceDetail {
    
    int id_product;
    int amount;
    double price;
    String iva;
    double total;

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public modelPurchaseInvoiceDetail(int id_product, int amount, double price, String iva, double total) {
        this.id_product = id_product;
        this.amount = amount;
        this.price = price;
        this.iva = iva;
        this.total = total;
    }

    public modelPurchaseInvoiceDetail(){}
}
