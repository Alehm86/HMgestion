/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views;

import dao.currentAccountDAO;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import utils.tableStyleUtil;

public class currentAccountViewForm extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(currentAccountViewForm.class.getName());

    DefaultTableModel dtmCurrentAccountsMovements = new DefaultTableModel();
    currentAccountDAO qCA = new currentAccountDAO();
    
    private int id_customer;
    private int id_CA;
    private String CUIT;
    private String customerName;
    
    public void setCurrentAccount(int id_customer, String customerName, String CUIT){
           
        this.id_customer = id_customer;       
        this.customerName = customerName;
        this.CUIT = CUIT;
        
        lbl_nameCustomer.setText(customerName);
        lbl_idCustomer.setText(String.valueOf(id_customer));
        lbl_cuit.setText(CUIT);
        
    }
    
    public currentAccountViewForm() {
        initComponents();

        inicializar();
        
        
    }

    public currentAccountViewForm(int id_CA) {
        initComponents();
        this.id_CA = id_CA;
        
        tablaMovements();
        actions();
    }
    
    private void inicializar(){
        
        lbl_nameCustomer.setText("");
        lbl_saldo.setText("");
        lbl_idCustomer.setText("");
        lbl_cuit.setText("");
        
    }
    
    private void actions(){
        
        btnCustomer.addActionListener(e->{
            
            customerViewDialog fView = new customerViewDialog(this, true);

            String formatCUIT = CUIT.replace("-", "").trim();

            fView.dialogoEdit(formatCUIT);
            fView.setVisible(true);
            
        });
        
    }
    
    private void tablaMovements(){
        
        String[] titulo = {"Fecha","Descripcion","Cantidad","Precio","IVA","Credito","Debito"};
        dtmCurrentAccountsMovements.setColumnIdentifiers(titulo);     
        
        tableMovements.setModel(dtmCurrentAccountsMovements);
        
        qCA.listCAMovements(id_CA, dtmCurrentAccountsMovements);
        lbl_saldo.setText(String.valueOf(qCA.calcularSaldoCA(id_CA)));
        
        tableStyleUtil.applyPoppinsHeader(tableMovements); 

        tableMovements.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableMovements.getColumnModel().getColumn(1).setPreferredWidth(250); 
        tableMovements.getColumnModel().getColumn(2).setPreferredWidth(40);
        tableMovements.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableMovements.getColumnModel().getColumn(4).setPreferredWidth(40);
        tableMovements.getColumnModel().getColumn(5).setPreferredWidth(100); 
        tableMovements.getColumnModel().getColumn(6).setPreferredWidth(100);

        tableMovements.getTableHeader().setReorderingAllowed(false);
        
        tableMovements.getColumnModel().getColumn(5).setCellRenderer(rendererCredito);
        tableMovements.getColumnModel().getColumn(6).setCellRenderer(rendererCredito);
        
    }
    
    DefaultTableCellRenderer rendererCredito = new DefaultTableCellRenderer() {
        @Override
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if(!isSelected){
                if(column == 5){
                    c.setForeground(new Color(204, 153, 0));  
                }else if(column == 6){
                    c.setForeground(new Color(0, 128, 0));  
                }else{
                    c.setForeground(Color.BLACK);
                }
            }

            setHorizontalAlignment(SwingConstants.CENTER);
            return c;
        }
    };
    
   
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanelSeparador4 = new javax.swing.JPanel();
        lbl_nameCustomer = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnCustomer = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lbl_idCustomer = new javax.swing.JLabel();
        lbl_cuit = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableMovements = new javax.swing.JTable();
        lbl_nameCustomer1 = new javax.swing.JLabel();
        lbl_saldo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setForeground(new java.awt.Color(12, 83, 151));

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(12, 83, 151));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/budget64_1.png"))); // NOI18N
        jLabel8.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

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

        lbl_nameCustomer.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        lbl_nameCustomer.setForeground(new java.awt.Color(35, 35, 38));
        lbl_nameCustomer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_nameCustomer.setText("Name");
        lbl_nameCustomer.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        lbl_nameCustomer.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel9.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(12, 83, 151));
        jLabel9.setText("Cuenta corriente: ");
        jLabel9.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        btnCustomer.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnCustomer.setForeground(new java.awt.Color(12, 83, 151));
        btnCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/clientView32.png"))); // NOI18N
        btnCustomer.setText("Ver cliente");

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(12, 83, 151));
        jLabel1.setText("Id:");

        lbl_idCustomer.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lbl_idCustomer.setForeground(new java.awt.Color(12, 83, 151));
        lbl_idCustomer.setText("xxx");

        lbl_cuit.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        lbl_cuit.setText("CUIT");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(lbl_cuit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 151, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_idCustomer))
                    .addComponent(btnCustomer))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lbl_idCustomer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCustomer))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lbl_cuit))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_nameCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_nameCustomer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelSeparador4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        tableMovements.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableMovements.setRowHeight(25);
        jScrollPane1.setViewportView(tableMovements);

        lbl_nameCustomer1.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        lbl_nameCustomer1.setForeground(new java.awt.Color(12, 83, 151));
        lbl_nameCustomer1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_nameCustomer1.setText("Saldo:");
        lbl_nameCustomer1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lbl_saldo.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        lbl_saldo.setForeground(new java.awt.Color(12, 83, 151));
        lbl_saldo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_saldo.setText("xxx");
        lbl_saldo.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lbl_nameCustomer1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_saldo, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_nameCustomer1)
                    .addComponent(lbl_saldo))
                .addContainerGap(101, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> new currentAccountViewForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCustomer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelSeparador4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_cuit;
    private javax.swing.JLabel lbl_idCustomer;
    private javax.swing.JLabel lbl_nameCustomer;
    private javax.swing.JLabel lbl_nameCustomer1;
    private javax.swing.JLabel lbl_saldo;
    private javax.swing.JTable tableMovements;
    // End of variables declaration//GEN-END:variables
}
