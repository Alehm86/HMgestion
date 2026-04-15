package forms;


import dao.genericDAO;
import dao.serviceDAO;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

public class serviceListPanel extends javax.swing.JPanel {

    genericDAO queriesGeneric = new genericDAO();
    serviceDAO queriesServices = new serviceDAO();
    
    String serviceSelected = "";
    Map<String, Integer> estadosMap = new HashMap<>();    
    
    public serviceListPanel() {
        initComponents();
        llenarCombos();
        listar();
        
        actions();
        selectService();
    }
    
    private void actions(){
        
        btnServiceView.addActionListener(e->{

            boolean estado = false;

            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            serviceViewPanel pServiceView = new serviceViewPanel(parent, true);

            if(!serviceSelected.isEmpty()){
                 pServiceView.setService(serviceSelected);
                 pServiceView.setVisible(true);

                 estado = pServiceView.dialogoServiceActualizado();
                 if(estado){
                     queriesServices.listServices(jtableServices);
                 }

            }else{
                JOptionPane.showMessageDialog(null, "Seleccione un servicio.");
            }
        });
       
        cboStates.addActionListener(e->{
            listar();
        });
        
        btnEntregar.addActionListener(e->{
            
            String delivery_date = queriesGeneric.fecha();
            boolean estado = false;
            
            int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Confirma la edición?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
            );                   
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            } 

            estado = queriesServices.updateServiceDespachar(serviceSelected, delivery_date);
            
            if(estado){
                listar();
            }
        });
    }
    
    private void listar(){
        
        int id_status = -1;
        String selected = (String) cboStates.getSelectedItem();

        if (selected != null && estadosMap.containsKey(selected)) {
            id_status = estadosMap.get(selected);
        }

        if(selected.equals("Todos")){
            queriesServices.listServices(jtableServices); 
        }else{
            queriesServices.listServicesForState(jtableServices,id_status);
        }        
    }
    
    private void selectService(){

        jtableServices.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent Mouse_evt){
                JTable tablaO =(JTable) Mouse_evt.getSource();
                Point point = Mouse_evt.getPoint();
                int row = tablaO.rowAtPoint(point);
                         
                if(Mouse_evt.getClickCount()==1){
                    serviceSelected = String.valueOf(jtableServices.getValueAt(jtableServices.getSelectedRow(), 1).toString()); 
                }
//                else{
//                }            
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
        estadosMap.put("No reparado", 5);

        cboStates.addItem("Reparado");
        estadosMap.put("Reparado", 6);
        
        cboStates.addItem("Entregado");
        estadosMap.put("Entregado", 7);        
    }
    


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnServiceView = new javax.swing.JButton();
        btnEntregar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cboStates = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtableServices = new javax.swing.JTable();

        jPanel1.setBackground(new java.awt.Color(101, 129, 171));

        btnServiceView.setBackground(new java.awt.Color(255, 255, 255));
        btnServiceView.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnServiceView.setForeground(new java.awt.Color(12, 83, 151));
        btnServiceView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/revisar32.png"))); // NOI18N
        btnServiceView.setText("Revisar");
        btnServiceView.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btnServiceView.setBorderPainted(false);
        btnServiceView.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnServiceView.setFocusable(false);
        btnServiceView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnServiceViewMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnServiceViewMouseExited(evt);
            }
        });
        btnServiceView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnServiceViewActionPerformed(evt);
            }
        });

        btnEntregar.setBackground(new java.awt.Color(255, 255, 255));
        btnEntregar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnEntregar.setForeground(new java.awt.Color(12, 83, 151));
        btnEntregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/entrega-de-paquetes.png"))); // NOI18N
        btnEntregar.setText("Entregar");
        btnEntregar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btnEntregar.setBorderPainted(false);
        btnEntregar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnEntregar.setFocusable(false);
        btnEntregar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEntregarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEntregarMouseExited(evt);
            }
        });
        btnEntregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntregarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Filtrar por estado");

        cboStates.setBackground(new java.awt.Color(255, 255, 255));
        cboStates.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboStates.setForeground(new java.awt.Color(65, 65, 63));
        cboStates.setBorder(null);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnServiceView, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEntregar, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 510, Short.MAX_VALUE)
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
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboStates, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnServiceView, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnEntregar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jtableServices.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jtableServices.setForeground(new java.awt.Color(65, 65, 63));
        jtableServices.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtableServices.setRowHeight(25);
        jScrollPane1.setViewportView(jtableServices);

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnServiceViewMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnServiceViewMouseEntered
        btnServiceView.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnServiceViewMouseEntered

    private void btnServiceViewMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnServiceViewMouseExited
        btnServiceView.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnServiceViewMouseExited

    private void btnServiceViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnServiceViewActionPerformed

    }//GEN-LAST:event_btnServiceViewActionPerformed

    private void btnEntregarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEntregarMouseEntered
        btnEntregar.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnEntregarMouseEntered

    private void btnEntregarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEntregarMouseExited
        btnEntregar.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnEntregarMouseExited

    private void btnEntregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntregarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEntregarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEntregar;
    private javax.swing.JButton btnServiceView;
    private javax.swing.JComboBox<String> cboStates;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtableServices;
    // End of variables declaration//GEN-END:variables
}
