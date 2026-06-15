/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import dao.budgetDAO;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class budgetListDialog extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(budgetListDialog.class.getName());
    
    budgetDAO qBudget = new budgetDAO();
    
    int id_budget;
    int id_service;
    int status = 0;
    
    String filtroFecha;
    String filtroEstado;
    
    
    public void setConfigPage(int status){      
        this.status = status;
        if(status == 1){
            btnSelectProduct.setVisible(true);
            btnCancel.setVisible(true);
            btnCancelBudget.setVisible(false);
        }           
    }
    
    public int getIdBudget(){
        return id_budget;
    }
    
    public budgetListDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        btnSelectProduct.setVisible(false);
        btnCancel.setVisible(false);
        
        combo();
        cboFiltroFecha.setSelectedIndex(2);
        
        leyendaBotones();       
        actions();
        listadoInicial();   
        

        
    }

    private void listadoInicial(){
        qBudget.listBudgets(jTableBudgets, "30 días", "Todos");
        jTableBudgets.getColumnModel().getColumn(5).setCellRenderer(new vencimientoColorRenderer());
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
    
    private void filtrarPresupuestos(){
        
        DefaultTableModel dtm = (DefaultTableModel) jTableBudgets.getModel();

        for (int i = dtm.getRowCount() - 1; i >= 0; i--) {
            Object valor = dtm.getValueAt(i, 3);
            if (valor != null && !valor.toString().trim().isEmpty()) {
                dtm.removeRow(i);
            }
        }
    }
    
    private void actions(){
           
        ActionListener filtroListener = e -> {
            
            String fecha = cboFiltroFecha.getSelectedItem().toString();
            String estado = cboFiltroEstado.getSelectedItem().toString();
 
            if(status > 0){
                qBudget.listBudgets(jTableBudgets, fecha, estado);
                filtrarPresupuestos();             
            }else{
                qBudget.listBudgets(jTableBudgets, fecha, estado);
            }
            jTableBudgets.getColumnModel().getColumn(5).setCellRenderer(new vencimientoColorRenderer());
            jTableBudgets.getColumnModel().getColumn(6).setCellRenderer(new colorRenderEstado());
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
            
            qBudget.cancelBudget(id_service);
            listadoInicial();
          
        });
        
        btnSelectProduct.addActionListener(e->{
            this.dispose();
        });
        
        btnCancel.addActionListener(e->{
            id_budget = -1;          
            this.dispose();
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
    
    public class vencimientoColorRenderer extends DefaultTableCellRenderer {

        private final DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {

            setHorizontalAlignment(SwingConstants.CENTER);
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            c.setForeground(new Color(65,65,63));

            try {

                LocalDate hoy = LocalDate.now();
                LocalDate vencimiento = LocalDate.parse(value.toString(), formato);

                if (vencimiento.isBefore(hoy)) {
                    c.setForeground(Color.RED);
                } else {
                    c.setForeground(new Color(65,65,63));
                }

            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }

            return c;
        }
    }
    
    public class colorRenderEstado extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {

            setHorizontalAlignment(SwingConstants.CENTER);
            Component estado = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value != null && "Vencido".equals(value.toString())) {
                estado.setForeground(Color.RED);
            } else {
                estado.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            }

            return estado;
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanelSeparador4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnViewBudget = new javax.swing.JButton();
        btnCancelBudget = new javax.swing.JButton();
        cboFiltroFecha = new javax.swing.JComboBox<>();
        cboFiltroEstado = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableBudgets = new javax.swing.JTable();
        btnSelectProduct = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión - Historial presupuestos");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(12, 83, 151));

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 36)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(12, 83, 151));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/budget64_1.png"))); // NOI18N
        jLabel8.setText("Presupuestos");

        jPanelSeparador4.setBackground(new java.awt.Color(12, 83, 151));

        javax.swing.GroupLayout jPanelSeparador4Layout = new javax.swing.GroupLayout(jPanelSeparador4);
        jPanelSeparador4.setLayout(jPanelSeparador4Layout);
        jPanelSeparador4Layout.setHorizontalGroup(
            jPanelSeparador4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelSeparador4Layout.setVerticalGroup(
            jPanelSeparador4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelSeparador4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 748, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelSeparador4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/calendar32.png"))); // NOI18N
        jLabel1.setFocusable(false);
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/gear24.png"))); // NOI18N
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        btnViewBudget.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchList32_1.png"))); // NOI18N

        btnCancelBudget.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cancelBudget32.png"))); // NOI18N

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
                .addComponent(cboFiltroEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnViewBudget, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelBudget, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboFiltroFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboFiltroEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        btnSelectProduct.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnSelectProduct.setForeground(new java.awt.Color(12, 83, 151));
        btnSelectProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnSelectProduct.setText("Seleccionar");
        btnSelectProduct.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit-32.png"))); // NOI18N
        btnCancel.addActionListener(this::btnCancelActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1069, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSelectProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnSelectProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                budgetListDialog dialog = new budgetListDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCancelBudget;
    private javax.swing.JButton btnSelectProduct;
    private javax.swing.JButton btnViewBudget;
    private javax.swing.JComboBox<String> cboFiltroEstado;
    private javax.swing.JComboBox<String> cboFiltroFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelSeparador4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableBudgets;
    // End of variables declaration//GEN-END:variables
}
