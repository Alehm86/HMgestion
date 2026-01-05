/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

/**
 *
 * @author Ale
 */
public class modelStocks {
    public int idProduct;
    public int stock;
    public int max;
    public int min;

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public modelStocks(int idProduct, int stock, int max, int min) {
        this.idProduct = idProduct;
        this.stock = stock;
        this.max = max;
        this.min = min;
    }
    
    public modelStocks(){
        
    }
}
