/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package forms;

import classDAO.serviceDAO;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

public class serviceSearchListDialog extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(serviceSearchListDialog.class.getName());

    serviceDAO qServices = new serviceDAO();
    
    String serviceSelected = "";
    
    public String getService(){       
        return serviceSelected;
    }
    
    public serviceSearchListDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        selectService();
        
        qServices.listServicesForState(jtableServices,7);
        
        btnView.addActionListener(e->{
            view();
        });
        
        btnSelect.addActionListener(e->{
            if(!serviceSelected.isEmpty()){
                this.dispose();
            }else{
                JOptionPane.showMessageDialog(null, "¡Debe seleccionar un producto de la lista!"); 
            }
        });
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
            }
        });
    }
    
    private void view(){       
        
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        serviceViewPanel pServiceView = new serviceViewPanel(parent, true);

        if(!serviceSelected.isEmpty()){
             pServiceView.viewService(serviceSelected);
             pServiceView.setVisible(true);

        }else{
            JOptionPane.showMessageDialog(null, "Seleccione un servicio.");
        }
    }   


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtableServices = new javax.swing.JTable();
        btnSelect = new javax.swing.JButton();
        btnView = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jtableServices.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jtableServices.setForeground(new java.awt.Color(65, 65, 63));
        jtableServices.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jtableServices.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jtableServices.setFillsViewportHeight(true);
        jtableServices.setRowHeight(30);
        jtableServices.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtableServicesMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jtableServicesMousePressed(evt);
            }
        });
        jtableServices.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtableServicesKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jtableServices);

        btnSelect.setBackground(new java.awt.Color(255, 255, 255));
        btnSelect.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnSelect.setForeground(new java.awt.Color(12, 83, 151));
        btnSelect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/search_32.png"))); // NOI18N
        btnSelect.setText("Seleccionar");
        btnSelect.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btnSelect.setBorderPainted(false);
        btnSelect.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSelect.setFocusable(false);
        btnSelect.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnSelect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSelectMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSelectMouseExited(evt);
            }
        });
        btnSelect.addActionListener(this::btnSelectActionPerformed);

        btnView.setBackground(new java.awt.Color(255, 255, 255));
        btnView.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnView.setForeground(new java.awt.Color(12, 83, 151));
        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/see32.png"))); // NOI18N
        btnView.setBorder(null);
        btnView.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnViewMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnViewMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 992, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnView, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addComponent(btnView, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jtableServicesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtableServicesMouseClicked
        if (evt.getClickCount() == 2) {

            int fila = jtableServices.getSelectedRow();

            if (fila != -1) {
//                viewService();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una fila");
            }
        }
    }//GEN-LAST:event_jtableServicesMouseClicked

    private void btnSelectMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSelectMouseEntered
        btnSelect.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnSelectMouseEntered

    private void btnSelectMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSelectMouseExited
        btnSelect.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnSelectMouseExited

    private void btnSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectActionPerformed

    }//GEN-LAST:event_btnSelectActionPerformed

    private void btnViewMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewMouseEntered
        btnView.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnViewMouseEntered

    private void btnViewMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewMouseExited
        btnView.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnViewMouseExited

    private void jtableServicesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtableServicesKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            view();
        }
    }//GEN-LAST:event_jtableServicesKeyPressed

    private void jtableServicesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtableServicesMousePressed
        if (evt.getClickCount() == 2) {

            int fila = jtableServices.getSelectedRow();

            if (fila != -1) {
                view();      
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una fila");
            }
        } 
    }//GEN-LAST:event_jtableServicesMousePressed


    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                serviceSearchListDialog dialog = new serviceSearchListDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSelect;
    private javax.swing.JButton btnView;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtableServices;
    // End of variables declaration//GEN-END:variables
}
