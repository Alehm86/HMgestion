/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;


public class modelService {
    
    int id_customer;
    Integer id_device;
    String fecha;
    String reported_problem;
    String status;
    String diagnosis;
    String repair_description;
    double  final_cost;
    String entry_date;
    String repair_date;
    String delivery_date;
    String technician;
    String observations;

    public int getId_customer() {
        return id_customer;
    }

    public void setId_customer(int id_customer) {
        this.id_customer = id_customer;
    }

    public Integer getId_device() {
        return id_device;
    }

    public void setId_device(Integer id_device) {
        this.id_device = id_device;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getReported_problem() {
        return reported_problem;
    }

    public void setReported_problem(String reported_problem) {
        this.reported_problem = reported_problem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getRepair_description() {
        return repair_description;
    }

    public void setRepair_description(String repair_description) {
        this.repair_description = repair_description;
    }

    public double getFinal_cost() {
        return final_cost;
    }

    public void setFinal_cost(double final_cost) {
        this.final_cost = final_cost;
    }

    public String getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(String entry_date) {
        this.entry_date = entry_date;
    }

    public String getRepair_date() {
        return repair_date;
    }

    public void setRepair_date(String repair_date) {
        this.repair_date = repair_date;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getTechnician() {
        return technician;
    }

    public void setTechnician(String technician) {
        this.technician = technician;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public modelService(int id_customer, Integer id_device, String fecha, String reported_problem, String status, String diagnosis, String repair_description, double final_cost, String entry_date, String repair_date, String delivery_date, String technician, String observations) {
        this.id_customer = id_customer;
        this.id_device = id_device;
        this.fecha = fecha;
        this.reported_problem = reported_problem;
        this.status = status;
        this.diagnosis = diagnosis;
        this.repair_description = repair_description;
        this.final_cost = final_cost;
        this.entry_date = entry_date;
        this.repair_date = repair_date;
        this.delivery_date = delivery_date;
        this.technician = technician;
        this.observations = observations;
    }
   
    public modelService(){
        
    }
    
}
