/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.util.Date;


public class mCurrentAccount {
    
    int id_customer;
    Date date;
    int id_operation;
    String description;
    Double debit;
    Double credit;
    int id_user;

    public int getId_customer() {
        return id_customer;
    }

    public void setId_customer(int id_customer) {
        this.id_customer = id_customer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId_operation() {
        return id_operation;
    }

    public void setId_operation(int id_operation) {
        this.id_operation = id_operation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDebit() {
        return debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public mCurrentAccount(int id_customer, Date date, int id_operation, String description, Double debit, Double credit, int id_user) {
        this.id_customer = id_customer;
        this.date = date;
        this.id_operation = id_operation;
        this.description = description;
        this.debit = debit;
        this.credit = credit;
        this.id_user = id_user;
    }

    public mCurrentAccount() {
    }
    
    
}
