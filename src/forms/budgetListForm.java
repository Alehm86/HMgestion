/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package forms;

import classDAO.budgetDAO;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JTable;

public class budgetListForm extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(budgetListForm.class.getName());
    
    budgetDAO qService = new budgetDAO();
    
    int id_budget;
    int id_service;
    
    String filtroFecha;
    String filtroEstado;

    public budgetListForm() {
        initComponents();
        
        combo();
        cboFiltroFecha.setSelectedIndex(2);
        
        leyendaBotones();       
        actions();
        listadoInicial();
   
    }
    
    private void listadoInicial(){
        qService.listBudgets(jTableBudgets, "30 días", "Todos");
    }
    
    private void combo(){
        
        cboFiltroFecha.addItem("Hoy");
        cboFiltroFecha.addItem("7 días");
        cboFiltroFecha.addItem("30 días");
        cboFiltroFecha.addItem("Último año");
        cboFiltroFecha.addItem("Todo");
        
        cboFiltroEstado.addItem("Todos");
        cboFiltroEstado.addItem("Pendiente");
        cboFiltroEstado.addItem("Aprobado");
        cboFiltroEstado.addItem("Rechazado");
        cboFiltroEstado.addItem("Anulado");
        cboFiltroEstado.addItem("Vencido");
    }
    
    private void leyendaBotones(){
        btnViewBudget.setToolTipText("Ver presupuesto");
        btnCancelBudget.setToolTipText("Cancelar presupuesto");
    }
    
    private void actions(){
           
        ActionListener filtroListener = e -> {
            String fecha = cboFiltroFecha.getSelectedItem().toString();
            String estado = cboFiltroEstado.getSelectedItem().toString();

            qService.listBudgets(jTableBudgets, fecha, estado);
        };

        cboFiltroFecha.addActionListener(filtroListener);
        cboFiltroEstado.addActionListener(filtroListener);

        jTableBudgets.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent evt){

                JTable tabla = (JTable) evt.getSource();
                int row = tabla.rowAtPoint(evt.getPoint());

                if (evt.getClickCount() == 1 && row != -1){

                    int modelRow = tabla.convertRowIndexToModel(row);

                    id_budget = (int) tabla.getModel().getValueAt(modelRow, 7);

                    Object idServiceObj = tabla.getModel().getValueAt(modelRow, 8);
                    id_service = (idServiceObj != null) ? (Integer) idServiceObj : null;

                }
            }
        });
        
        btnViewBudget.addActionListener(e->{
            viewBudget();
        });         
        
        btnCancelBudget.addActionListener(e->{
            
            boolean cancelado = false;
            
            int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Está seguro de anular el presupuesto?\n\n" +
                "Esta acción no se puede deshacer.",
                "Confirmación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );                   
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }
            
            qService.cancelBudget(id_service);
            listadoInicial();
          
        });
        
    }
    
    public void viewBudget(){
        this.setVisible(false);

        budgetPrintDialog fViewBudget = new budgetPrintDialog(null, true);

        fViewBudget.dialogoIdBudget(id_budget);

        fViewBudget.setLocationRelativeTo(null);
        fViewBudget.setVisible(true);

        this.setVisible(true);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btnViewBudget = new javax.swing.JButton();
        btnCancelBudget = new javax.swing.JButton();
        cboFiltroFecha = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        cboFiltroEstado = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableBudgets = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(101, 129, 171));

        jLabel8.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/bugdet128.png"))); // NOI18N
        jLabel8.setText("Presupuestos");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        btnViewBudget.setBackground(new java.awt.Color(255, 255, 255));
        btnViewBudget.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchList32_1.png"))); // NOI18N
        btnViewBudget.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnViewBudget.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnViewBudgetMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnViewBudgetMouseExited(evt);
            }
        });

        btnCancelBudget.setBackground(new java.awt.Color(255, 255, 255));
        btnCancelBudget.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cancelBudget32.png"))); // NOI18N
        btnCancelBudget.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelBudget.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelBudgetMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelBudgetMouseExited(evt);
            }
        });

        cboFiltroFecha.setBackground(new java.awt.Color(255, 255, 255));
        cboFiltroFecha.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboFiltroFecha.setBorder(null);
        cboFiltroFecha.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboFiltroFecha.setFocusable(false);
        cboFiltroFecha.addActionListener(this::cboFiltroFechaActionPerformed);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/calendar32.png"))); // NOI18N
        jLabel1.setLabelFor(cboFiltroFecha);
        jLabel1.setFocusable(false);
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        cboFiltroEstado.setBackground(new java.awt.Color(255, 255, 255));
        cboFiltroEstado.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboFiltroEstado.setBorder(null);
        cboFiltroEstado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboFiltroEstado.setFocusable(false);
        cboFiltroEstado.addActionListener(this::cboFiltroEstadoActionPerformed);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/gear24.png"))); // NOI18N
        jLabel2.setLabelFor(cboFiltroEstado);
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnViewBudget, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelBudget, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboFiltroFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboFiltroEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnCancelBudget, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnViewBudget, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboFiltroEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboFiltroFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
        );

        jTableBudgets.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jTableBudgets.setForeground(new java.awt.Color(65, 65, 63));
        jTableBudgets.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTableBudgets.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTableBudgets.setFillsViewportHeight(true);
        jTableBudgets.setRowHeight(30);
        jTableBudgets.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableBudgetsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableBudgets);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1069, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewBudgetMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewBudgetMouseEntered
        btnViewBudget.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnViewBudgetMouseEntered

    private void btnViewBudgetMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewBudgetMouseExited
        btnViewBudget.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnViewBudgetMouseExited

    private void btnCancelBudgetMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelBudgetMouseEntered
        btnCancelBudget.setBackground(new Color(255,127,39));
    }//GEN-LAST:event_btnCancelBudgetMouseEntered

    private void btnCancelBudgetMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelBudgetMouseExited
        btnCancelBudget.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCancelBudgetMouseExited

    private void cboFiltroFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFiltroFechaActionPerformed

    }//GEN-LAST:event_cboFiltroFechaActionPerformed

    private void cboFiltroEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFiltroEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboFiltroEstadoActionPerformed

    private void jTableBudgetsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableBudgetsMouseClicked
        if (evt.getClickCount() == 2) {

            int fila = jTableBudgets.getSelectedRow();

            if (fila != -1) {
                viewBudget();      
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una fila");
            }
        } 
    }//GEN-LAST:event_jTableBudgetsMouseClicked


    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> new budgetListForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelBudget;
    private javax.swing.JButton btnViewBudget;
    private javax.swing.JComboBox<String> cboFiltroEstado;
    private javax.swing.JComboBox<String> cboFiltroFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableBudgets;
    // End of variables declaration//GEN-END:variables
}
