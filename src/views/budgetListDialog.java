/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import dao.budgetDAO;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import utils.utility;


public class budgetListDialog extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(budgetListDialog.class.getName());
    
    budgetDAO qBudget = new budgetDAO();
    utility utils = new utility();
    
    private String budgetNumber;
    private int id_budget;
    private Integer id_service;
    private int status = 0;
    
    private String filtroFecha;
    private String filtroEstado;   

    private TableRowSorter<DefaultTableModel> sorter;
    
    public void setConfigPage(int status){      
        this.status = status;
        if(this.status == 1){
            btnSelectProduct.setVisible(true);
            btnCancel.setVisible(true);
            btnCancelBudget.setVisible(false);
        }           
    }
    
    public int getIdBudget(){
        return id_budget;
    }
    
    public String getBudgetNumber(){
        return budgetNumber;
    }
    
    public budgetListDialog(java.awt.Frame parent, boolean modal, int status) {
        super(parent, modal);
        
        this.status = status;
        
        initComponents();
        
        btnSelectProduct.setVisible(false);
        btnCancel.setVisible(false);
        
        combo();
        cboFiltroFecha.setSelectedIndex(2);
        
        leyendaBotones();       
        actions();
        
        listadoInicial();   
             
        utils.agregarPlaceholderN(txtBuscar, "Filtrar...");      

    }    

    private void listadoInicial(){
        qBudget.listBudgets(status,tableBudgets, "30 días", "Todos");
        tableBudgets.getColumnModel().getColumn(5).setCellRenderer(new vencimientoColorRenderer());
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
        
        DefaultTableModel dtm = (DefaultTableModel) tableBudgets.getModel();

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
                qBudget.listBudgets(status,tableBudgets, fecha, estado);
                filtrarPresupuestos();             
            }else{
                qBudget.listBudgets(status,tableBudgets, fecha, estado);
            }
            tableBudgets.getColumnModel().getColumn(5).setCellRenderer(new vencimientoColorRenderer());
            tableBudgets.getColumnModel().getColumn(6).setCellRenderer(new colorRenderEstado());
        };

        cboFiltroFecha.addActionListener(filtroListener);
        cboFiltroEstado.addActionListener(filtroListener);

        tableBudgets.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent evt){

                JTable tabla = (JTable) evt.getSource();
                int row = tabla.rowAtPoint(evt.getPoint());

                if (evt.getClickCount() == 1 && row != -1){

                    int modelRow = tabla.convertRowIndexToModel(row);

                    budgetNumber = String.valueOf(tableBudgets.getValueAt(tableBudgets.getSelectedRow(), 1).toString()); 
                    id_budget = (int) tabla.getModel().getValueAt(modelRow, 7);

                    Object idServiceObj = tabla.getModel().getValueAt(modelRow, 8);
                    id_service = (idServiceObj != null) ? (Integer) idServiceObj : null;

                }
            }
        });
        
        btnViewBudget.addActionListener(e->{
            if(id_budget > 0){
               viewBudget(); 
            }else{
                JOptionPane.showMessageDialog(null, "Seleccione un presupuesto.");
            }
            
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
        
        btnBuscarFiltro.addActionListener(e->{
            buscadorTabla();
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
    
    private void buscadorTabla() {
        
        DefaultTableModel modelo = (DefaultTableModel) tableBudgets.getModel();
        sorter = new TableRowSorter<>(modelo);
        tableBudgets.setRowSorter(sorter);
     
        String texto = txtBuscar.getText();
        if (texto.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
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
        jLabel6 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnBuscarFiltro = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableBudgets = new javax.swing.JTable();
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

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(12, 83, 151));
        jLabel6.setText("|");

        txtBuscar.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtBuscar.setForeground(new java.awt.Color(35, 35, 38));
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarKeyPressed(evt);
            }
        });

        btnBuscarFiltro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/search32.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnViewBudget, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelBudget, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscarFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(cboFiltroEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
        );

        tableBudgets.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        tableBudgets.setForeground(new java.awt.Color(65, 65, 63));
        tableBudgets.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableBudgets.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tableBudgets.setFillsViewportHeight(true);
        tableBudgets.setRowHeight(30);
        tableBudgets.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableBudgetsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableBudgets);

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
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSelectProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1057, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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

    private void tableBudgetsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableBudgetsMouseClicked
        if (evt.getClickCount() == 2) {

            int fila = tableBudgets.getSelectedRow();

            if (fila != -1) {
                viewBudget();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una fila");
            }
        }
    }//GEN-LAST:event_tableBudgetsMouseClicked

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            buscadorTabla();
        }
    }//GEN-LAST:event_txtBuscarKeyPressed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                budgetListDialog dialog = new budgetListDialog(new javax.swing.JFrame(), true, 1);
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
    private javax.swing.JButton btnBuscarFiltro;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCancelBudget;
    private javax.swing.JButton btnSelectProduct;
    private javax.swing.JButton btnViewBudget;
    private javax.swing.JComboBox<String> cboFiltroEstado;
    private javax.swing.JComboBox<String> cboFiltroFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelSeparador4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableBudgets;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
