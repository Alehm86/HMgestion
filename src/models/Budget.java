/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDate;


public class Budget {
    
    int id_budget;
    String nro_budget;
    LocalDate date;
    LocalDate expiration_date;
    String customer_name;
    String customer_phone;
    int state;
    String observations;
    Integer id_service;
    double total;

    public int getId_budget() {
        return id_budget;
    }

    public void setId_budget(int id_budget) {
        this.id_budget = id_budget;
    }

    public String getNro_budget() {
        return nro_budget;
    }

    public void setNro_budget(String nro_budget) {
        this.nro_budget = nro_budget;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(LocalDate expiration_date) {
        this.expiration_date = expiration_date;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Integer getId_service() {
        return id_service;
    }

    public void setId_service(Integer id_service) {
        this.id_service = id_service;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Budget(int id_budget, String nro_budget, LocalDate date, LocalDate expiration_date, String customer_name, String customer_phone, int state, String observations, Integer id_service, double total) {
        this.id_budget = id_budget;
        this.nro_budget = nro_budget;
        this.date = date;
        this.expiration_date = expiration_date;
        this.customer_name = customer_name;
        this.customer_phone = customer_phone;
        this.state = state;
        this.observations = observations;
        this.id_service = id_service;
        this.total = total;
    }


    
    public Budget(){}
}
