package views;

import dao.serviceDAO;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import utils.utility;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

public class serviceListPanel extends javax.swing.JPanel {

    serviceDAO qServices = new serviceDAO();
    
    utility utils = new utility();
    
    String serviceSelected = "";
    
    Map<String, Integer> estadosMap = new HashMap<>();    
    
    public serviceListPanel() {
        initComponents();
        
        btnEntregar.setEnabled(false);
        
        llenarCombos();
        listar(); 
        actions();
        selectService();
        verificarEstadoServicio();
        
    }
    
    private void actions(){
        
        btnServiceView.addActionListener(e->{
            viewService();
        });
       
        cboStates.addActionListener(e->{
            listar();
        });
        
        btnEntregar.addActionListener(e->{           
            int confirmacion = JOptionPane.showConfirmDialog(null,"¿Confirma entregar el equipo?","Confirmación",JOptionPane.YES_NO_OPTION);
            
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }              
            qServices.updateServiceDespachar(serviceSelected);   
            listar();
            
        });
        
    }
    
    private void viewService(){
        boolean estado = false;

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        serviceViewPanel pServiceView = new serviceViewPanel(parent, true);

        if(!serviceSelected.isEmpty()){
             pServiceView.setService(serviceSelected);
             pServiceView.setVisible(true);

             estado = pServiceView.dialogoServiceActualizado();
             
             if(estado){
                 qServices.listServices(tableServices);
             }

        }else{
            JOptionPane.showMessageDialog(null, "Seleccione un servicio.");
        }
    }
    
    private void listar(){
        
        int id_status = -1;
        String selected = (String) cboStates.getSelectedItem();

        if (selected != null && estadosMap.containsKey(selected)) {
            id_status = estadosMap.get(selected);
        }

        if(selected.equals("Todos")){
            qServices.listServices(tableServices); 
        }else{
            qServices.listServicesForState(tableServices,id_status);
        }        
    }
    
    private void selectService(){

        tableServices.addMouseListener(new MouseAdapter(){
            
            public void mousePressed(MouseEvent Mouse_evt){
                JTable tablaO =(JTable) Mouse_evt.getSource();
                Point point = Mouse_evt.getPoint();
                int row = tablaO.rowAtPoint(point);
                         
                if(Mouse_evt.getClickCount()==1){
                    serviceSelected = String.valueOf(tableServices.getValueAt(tableServices.getSelectedRow(), 1).toString()); 
                }         
            }
        });
    }
    
    private void llenarCombos(){
        
        cboStates.removeAllItems();
        estadosMap.clear();

        cboStates.addItem("Todos");
        
        cboStates.addItem("Ingresado");
        estadosMap.put("Ingresado", 1);        

        cboStates.addItem("Diagnosticado");
        estadosMap.put("Diagnosticado", 2);

        cboStates.addItem("Esperando aprobación");
        estadosMap.put("Esperando aprobación", 3);
        
        cboStates.addItem("Presupuesto aprobado");
        estadosMap.put("Presupuesto aprobado", 4);

        cboStates.addItem("No reparado");
        estadosMap.put("No reparado", 6);

        cboStates.addItem("Reparado");
        estadosMap.put("Reparado", 7);
        
        cboStates.addItem("Entregado");
        estadosMap.put("Entregado", 8);        
    }
    
    private void verificarEstadoServicio(){

        int fila = tableServices.getSelectedRow();

        if(fila == -1){
            btnEntregar.setEnabled(false);
            return;
        }

        String estado = tableServices.getValueAt(fila, 8).toString().trim();

        btnEntregar.setEnabled(estado.equalsIgnoreCase("No reparado"));
    }  

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        btnServiceView = new javax.swing.JButton();
        cboStates = new javax.swing.JComboBox<>();
        btnEntregar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableServices = new javax.swing.JTable();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(35, 35, 38));
        jLabel3.setText("Filtrar por estado");

        btnServiceView.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnServiceView.setForeground(new java.awt.Color(35, 35, 38));
        btnServiceView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/revisar32.png"))); // NOI18N
        btnServiceView.setText("Ver servicio");

        btnEntregar.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnEntregar.setForeground(new java.awt.Color(35, 35, 38));
        btnEntregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/entrega-de-paquetes.png"))); // NOI18N
        btnEntregar.setText("Entregar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnServiceView)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEntregar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 535, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboStates, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cboStates, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnServiceView)
                            .addComponent(btnEntregar))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        tableServices.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        tableServices.setForeground(new java.awt.Color(65, 65, 63));
        tableServices.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableServices.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tableServices.setFillsViewportHeight(true);
        tableServices.setRowHeight(28);
        tableServices.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableServicesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableServices);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tableServicesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableServicesMouseClicked
        
        verificarEstadoServicio();
        
        if (evt.getClickCount() == 2) {

            int fila = tableServices.getSelectedRow();

            if (fila != -1) {
                viewService();      
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una fila");
            }
        } 
    }//GEN-LAST:event_tableServicesMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEntregar;
    private javax.swing.JButton btnServiceView;
    private javax.swing.JComboBox<String> cboStates;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableServices;
    // End of variables declaration//GEN-END:variables
}
