/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

/**
 *
 * @author Ale
 */
public class modelProducts {
    public int id_subcat;
    public int id_brand;
    public int id_supplier;
    public int id_category;
    public String model;
    public String color;
    public String productCode;
    public int state;


    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }

    public int getId_subcat() {
        return id_subcat;
    }

    public void setId_subcat(int id_subcat) {
        this.id_subcat = id_subcat;
    }

    public int getId_brand() {
        return id_brand;
    }

    public void setId_brand(int id_brand) {
        this.id_brand = id_brand;
    }

    public int getId_supplier() {
        return id_supplier;
    }

    public void setId_supplier(int id_supplier) {
        this.id_supplier = id_supplier;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public modelProducts(int id_subcat, int id_brand, int id_supplier, int id_category, String model, String color, String productCode, int state) {
        this.id_subcat = id_subcat;
        this.id_brand = id_brand;
        this.id_supplier = id_supplier;
        this.id_category = id_category;
        this.model = model;
        this.color = color;
        this.productCode = productCode;
        this.state = state;
    }
   
    public modelProducts() {

    }
    
}
