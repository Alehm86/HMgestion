/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import ConnectionDB.connectionDB;
import dao.budgetDAO;
import dao.cashRegisterDAO;
import dao.productDAO;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import utils.tableStyleUtil;


public class productAssocieateDialog extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(productAssocieateDialog.class.getName());

    cashRegisterDAO qCashReg = new cashRegisterDAO();
    productDAO qProduct = new productDAO();
    budgetDAO qBudget = new budgetDAO();
    
    private String budgetNumber;
    private int id_product = -1;
    
    boolean dialog = false;
    boolean modoEdicion = false;
    int filaEditable = -1;
    
    DefaultTableModel dtmProduct = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return modoEdicion && row == filaEditable && (column == 3);
        }
    };  
    
    public boolean dialogoBudgetNumber(String budgetNumber){
        this.budgetNumber = budgetNumber;
         
        if(!budgetNumber.isEmpty()){
            qCashReg.listProductsForBudget(dtmProduct, budgetNumber);
        }
        return dialog;
    }
    
    public productAssocieateDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        lbl_product.setVisible(false);
        txtProductCode.setVisible(false);
        
        tablaProducts();
        
        qCashReg.listProductsForBudget(dtmProduct, budgetNumber);
        
        actionButtons();
    }    
    
    private void tablaProducts(){
        
        String[] titulo = new String[]{"Producto presupuestado","Producto asociado", "id.", "idBudgetDetail",""};
        dtmProduct.setColumnIdentifiers(titulo);
        tableProducts.setModel(dtmProduct);
        
        tableStyleUtil.applyPoppinsHeader(tableProducts);
        
        tableProducts.getColumnModel().getColumn(2).setMinWidth(0);
        tableProducts.getColumnModel().getColumn(2).setMaxWidth(0);
        tableProducts.getColumnModel().getColumn(2).setPreferredWidth(0);
        
        tableProducts.getColumnModel().getColumn(3).setMinWidth(0);
        tableProducts.getColumnModel().getColumn(3).setMaxWidth(0);
        tableProducts.getColumnModel().getColumn(3).setPreferredWidth(0);
        
        tableProducts.getColumnModel().getColumn(0).setPreferredWidth(300);
        tableProducts.getColumnModel().getColumn(1).setPreferredWidth(300);
        tableProducts.getColumnModel().getColumn(2).setPreferredWidth(40); 
        tableProducts.getColumnModel().getColumn(3).setPreferredWidth(40); 
        tableProducts.getColumnModel().getColumn(4).setPreferredWidth(40);
        
        tableProducts.getColumnModel().getColumn(4).setCellRenderer(new productAssocieateDialog.ButtonCellRenderer());
        
        tableProducts.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                int fila = tableProducts.rowAtPoint(e.getPoint());
                int columna = tableProducts.columnAtPoint(e.getPoint());

                if(fila == -1){
                    return;
                }

                if(columna == 4){
                  
                    productSearchDialog dialogo = new productSearchDialog((java.awt.Frame) getOwner(), true);   
                    dialogo.setLocationRelativeTo(null);
                    dialogo.setVisible(true);  

                    id_product = dialogo.getProduct();

                    if(id_product > 0){
                        qProduct.selectProduct(id_product, lbl_product, txtProductCode);
                        
                        dtmProduct.setValueAt(lbl_product.getText(), fila, 1);
                        dtmProduct.setValueAt(id_product, fila, 2);
                    }
                }

            }
        });
    }
    
    public class ButtonCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            JLabel label = new JLabel(value != null ? value.toString() : "");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setOpaque(true);

            label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

            if (column == 3){
                label.setBackground(new Color(144, 238, 144));
                label.setForeground(new Color(12,83,151));
            } 
            else {
                label.setBackground(Color.WHITE);
            }
            
            if (isSelected) {
                label.setBackground(new Color(200, 220, 255));
            }
            return label;
        }
    }
    
    private Connection getConnection() {
        connectionDB conn = new connectionDB();
        return conn.establecerConexion();
    }
    
    private void actionButtons(){
        
        btnConfirm.addActionListener(e -> {

            Connection conn = getConnection();

            if (tableProducts.getRowCount() == 0) {
                return;
            }

            try {

                conn.setAutoCommit(false);

                for (int i = 0; i < tableProducts.getRowCount(); i++) {

                    int idProd = Integer.parseInt(tableProducts.getValueAt(i, 2).toString());
                    int idBudgDet = Integer.parseInt(tableProducts.getValueAt(i, 3).toString());

                    boolean estado = qBudget.updateIdInBudgetDetail(conn, idProd, idBudgDet);

                    if (!estado) {
                        throw new Exception("No se pudo actualizar el detalle del presupuesto.");
                    }
                }

                conn.commit();

                JOptionPane.showMessageDialog(null, "Productos asociados correctamente.");

                dialog = true;
                dispose();

            } catch (Exception ex) {

                try {
                    if (conn != null) {
                        conn.rollback();
                    }
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }

                JOptionPane.showMessageDialog(null, ex.getMessage());
                ex.printStackTrace();

            } finally {

                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }            

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanelSeparador4 = new javax.swing.JPanel();
        lbl_product = new javax.swing.JLabel();
        txtProductCode = new javax.swing.JTextField();
        lbl_IdBudgetDetail = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableProducts = new javax.swing.JTable();
        btnConfirm = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setForeground(new java.awt.Color(12, 83, 151));

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 36)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(12, 83, 151));
        jLabel8.setText("Asociación de productos");

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelSeparador4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(115, 115, 115)
                        .addComponent(lbl_product)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_IdBudgetDetail)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lbl_product)
                    .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_IdBudgetDetail))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelSeparador4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tableProducts.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        tableProducts.setForeground(new java.awt.Color(65, 65, 63));
        tableProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableProducts.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tableProducts.setFillsViewportHeight(true);
        tableProducts.setRowHeight(30);
        tableProducts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableProductsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableProducts);

        btnConfirm.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnConfirm.setForeground(new java.awt.Color(12, 83, 151));
        btnConfirm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnConfirm.setText("Confirmar");
        btnConfirm.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit-32.png"))); // NOI18N
        btnCancel.addActionListener(this::btnCancelActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 897, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tableProductsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableProductsMouseClicked

    }//GEN-LAST:event_tableProductsMouseClicked

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed

    }//GEN-LAST:event_btnCancelActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                productAssocieateDialog dialog = new productAssocieateDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnConfirm;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelSeparador4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_IdBudgetDetail;
    private javax.swing.JLabel lbl_product;
    private javax.swing.JTable tableProducts;
    private javax.swing.JTextField txtProductCode;
    // End of variables declaration//GEN-END:variables
}
