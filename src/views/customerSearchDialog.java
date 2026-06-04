/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;


import dao.customerDAO;
import dao.genericDAO;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import utils.utility;

public class customerSearchDialog extends javax.swing.JDialog {

    genericDAO qGeneric = new genericDAO();
    customerDAO qCustomer= new customerDAO();
    
    utility utils = new utility();
      
    private TableRowSorter<DefaultTableModel> sorter;
    
    DefaultTableModel tableProducts = new DefaultTableModel();
    
    private String cuitFound;
    private String filaSeleccionada = "";
    

    public String getCustomerSerch() {
        return cuitFound;
    }
    
    public customerSearchDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        tableCustomer.requestFocus();
        
        qGeneric.llenarCombos(cboIVA, "customer_iva");
        utils.agregarPlaceholderN(txtSearch, "Ingrese el nombre a buscar");
        qCustomer.listAllCustomer(tableCustomer);

        activarBuscadorTabla();     
        
        actions();  
        
        
    }

    private void actions(){
        
        tableCustomer.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent Mouse_evt){
                JTable tablaO =(JTable) Mouse_evt.getSource();
                Point point = Mouse_evt.getPoint();
                int row = tablaO.rowAtPoint(point);
                         
                if(Mouse_evt.getClickCount()==1){
                    filaSeleccionada = String.valueOf(tableCustomer.getValueAt(tableCustomer.getSelectedRow(), 0).toString()); 
                }
                else{
                }            
            }
        });
        
        cboIVA.addActionListener(e->{
            int idIva = qCustomer.selectIdIva(cboIVA.getSelectedItem().toString());

            if (cboIVA.getSelectedIndex() > 0) {
                qCustomer.listCustomerForIva(tableCustomer, idIva);
            } else {
                qCustomer.listAllCustomer(tableCustomer);
            }
            
            activarBuscadorTabla();
        });
        
        btnSelect.addActionListener(e->{
            cuitFound = qCustomer.selectCuit("name", filaSeleccionada);            
            this.dispose();
        });
        
        btnCancel.addActionListener(e->{          
            this.dispose();
        });
        
        
    }
    
    private void activarBuscadorTabla() {
        
        DefaultTableModel modelo = (DefaultTableModel) tableCustomer.getModel();
        sorter = new TableRowSorter<>(modelo);
        tableCustomer.setRowSorter(sorter);  

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrar();
            }

            private void filtrar() {
                String texto = txtSearch.getText();
                if (texto.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
                }
            }
        });
    } 
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCustomer = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        cboIVA = new javax.swing.JComboBox<>();
        btnCancel = new javax.swing.JButton();
        btnSelect = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tableCustomer.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        tableCustomer.setForeground(new java.awt.Color(65, 65, 63));
        tableCustomer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableCustomer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tableCustomer.setRowHeight(30);
        tableCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCustomerMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableCustomer);

        jButton1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jButton2.setBorder(null);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(12, 83, 151));
        jLabel1.setText("Cliente");

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(12, 83, 151));
        jLabel2.setText("I.V.A.");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cliente128.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSearch)
                    .addComponent(cboIVA, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(cboIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit-32.png"))); // NOI18N

        btnSelect.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnSelect.setForeground(new java.awt.Color(12, 83, 151));
        btnSelect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnSelect.setText("Seleccionar");
        btnSelect.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(222, 222, 222))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 527, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tableCustomerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCustomerMouseClicked
        if (evt.getClickCount() == 2) {

            int fila = tableCustomer.getSelectedRow();

            if (fila != -1) {
                cuitFound = qCustomer.selectCuit("name", filaSeleccionada);            
                this.dispose();      
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una fila");
            }
        } 
    }//GEN-LAST:event_tableCustomerMouseClicked

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                customerSearchDialog dialog = new customerSearchDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnSelect;
    private javax.swing.JComboBox<String> cboIVA;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableCustomer;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
