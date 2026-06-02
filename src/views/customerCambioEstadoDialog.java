/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import dao.customerDAO;
import java.awt.Color;
import javax.swing.JOptionPane;
import utils.utility;

public class customerCambioEstadoDialog extends javax.swing.JDialog {

    customerDAO queriesCustomer= new customerDAO();
    
    utility utils = new utility();    
    
    int id_client;
    String title;
    boolean estado=false;
    
    public void dialogoId_client(int id_client){
        
        this.id_client = id_client;
        
        if (id_client < 0) {
            throw new IllegalArgumentException("ID de cliente inválido");
        }
          
    } 
    
    public void dialogoGetTitle(String title){
        this.title = title;
        
        if(!title.isEmpty()){
            lblTitle.setText(title);
        }    
    } 
    
    public boolean dialogoEstadoActualizado(){
        return estado;
    }
    
    public customerCambioEstadoDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        actions();
    }

    public void actions(){

        btnCambioEstado.addActionListener(e -> {

            int state = -1;
            String action;
            String detalle = "S/D";
            
            if(!textAreaMotivo.getText().isEmpty()){
                detalle = textAreaMotivo.getText().trim();
            }

            String opcion = title;

            switch (opcion) {

                case "Dar de baja":
                    state = 2;
                    action = "Baja de cliente"; 
                    break;

                case "Suspender":
                    state = 3;
                    action = "Suspensión de cliente";                     
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Estado desconocido");
                    return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(
                    null,
                    "¿Confirma la acción sobre el cliente?",
                    "Confirmación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            queriesCustomer.updateState(id_client, state);
            queriesCustomer.insertCustomerHistory(id_client, action, detalle);
            
            estado=true;
            dispose();
        });

    }

    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaMotivo = new javax.swing.JTextArea();
        btnCambioEstado = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        textAreaMotivo.setColumns(20);
        textAreaMotivo.setRows(5);
        textAreaMotivo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)));
        jScrollPane1.setViewportView(textAreaMotivo);

        btnCambioEstado.setBackground(new java.awt.Color(255, 255, 255));
        btnCambioEstado.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnCambioEstado.setForeground(new java.awt.Color(101, 129, 171));
        btnCambioEstado.setText("Confirmar");
        btnCambioEstado.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnCambioEstado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCambioEstado.setFocusable(false);
        btnCambioEstado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCambioEstadoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCambioEstadoMouseExited(evt);
            }
        });
        btnCambioEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambioEstadoActionPerformed(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(255, 255, 255));
        btnCancel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cancelar_32.png"))); // NOI18N
        btnCancel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnCancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelMouseExited(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(35, 35, 38));
        jLabel1.setText("Motivo");

        jLabel2.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(35, 35, 38));
        jLabel2.setText("Tramite: ");

        lblTitle.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(35, 35, 38));
        lblTitle.setText("xxx");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTitle))
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnCambioEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblTitle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCambioEstado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCambioEstadoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCambioEstadoMouseEntered
        btnCambioEstado.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnCambioEstadoMouseEntered

    private void btnCambioEstadoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCambioEstadoMouseExited
        btnCambioEstado.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCambioEstadoMouseExited

    private void btnCambioEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambioEstadoActionPerformed

    }//GEN-LAST:event_btnCambioEstadoActionPerformed

    private void btnCancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseEntered
        btnCancel.setBackground(new Color(255,127,39));
    }//GEN-LAST:event_btnCancelMouseEntered

    private void btnCancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseExited
        btnCancel.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCancelMouseExited

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                customerCambioEstadoDialog dialog = new customerCambioEstadoDialog(new javax.swing.JFrame(),true);
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
    private javax.swing.JButton btnCambioEstado;
    private javax.swing.JButton btnCancel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextArea textAreaMotivo;
    // End of variables declaration//GEN-END:variables
}
