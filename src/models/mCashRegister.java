/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;


public class mCashRegister {
    
    Integer id_customer;
    int id_user;
    double total;
    double discount;
    double totalFinal;
    String observation;

    public Integer getId_customer() {
        return id_customer;
    }

    public void setId_customer(Integer id_customer) {
        this.id_customer = id_customer;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotalFinal() {
        return totalFinal;
    }

    public void setTotalFinal(double totalFinal) {
        this.totalFinal = totalFinal;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public mCashRegister(Integer id_customer, int id_user, double total, double discount, double totalFinal, String observation) {
        this.id_customer = id_customer;
        this.id_user = id_user;
        this.total = total;
        this.discount = discount;
        this.totalFinal = totalFinal;
        this.observation = observation;
    }

    public mCashRegister() {
    }


}
