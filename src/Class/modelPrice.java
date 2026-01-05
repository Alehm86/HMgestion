/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

/**
 *
 * @author Ale
 */
public class modelPrice {
    public int id_product;
    public double price;
    public double benefit;
    public double iva;
    public double salesPrice;

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getBenefit() {
        return benefit;
    }

    public void setBenefit(double benefit) {
        this.benefit = benefit;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public modelPrice(int id_product, double price, double benefit, double iva, double salesPrice) {
        this.id_product = id_product;
        this.price = price;
        this.benefit = benefit;
        this.iva = iva;
        this.salesPrice = salesPrice;
    }

    public modelPrice() {

    }
}
