/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package OldNotUsed;

import Class.CustomerDAO;
import Class.GenericDAO;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Pattern;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;


public class PageCustomerListForm extends javax.swing.JPanel {

    GenericDAO queriesGeneric = new GenericDAO();
    CustomerDAO queriesCustomer= new CustomerDAO();
    
    private String filaSeleccionada = "";
   
    public String getFilaSeleccionada() {
        return filaSeleccionada;
    }
    
    private String cuitFound;  
    private TableRowSorter<DefaultTableModel> sorter;
    DefaultTableModel tableProducts = new DefaultTableModel();

    
    public PageCustomerListForm() {
        initComponents();
        queriesGeneric.llenarCombos(cboIVA, "iva");
        
        queriesCustomer.listAllCustomerComplete(tableCustomer);
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
            int idIva = queriesCustomer.selectIdIva(cboIVA.getSelectedItem().toString());

            if (cboIVA.getSelectedIndex() > 0) {
                queriesCustomer.listCustomerForIvaComplete(tableCustomer, idIva);
            } else {
                queriesCustomer.listAllCustomerComplete(tableCustomer);
            }
            
            activarBuscadorTabla();
        });
        

        
    }    

    private void activarBuscadorTabla() {
        DefaultTableModel modelo = (DefaultTableModel) tableCustomer.getModel();
        sorter = new TableRowSorter<>(modelo);
        tableCustomer.setRowSorter(sorter);

        txtSerch.getDocument().addDocumentListener(new DocumentListener() {
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
                String texto = txtSerch.getText();
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

        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCustomer = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtSerch = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cboIVA = new javax.swing.JComboBox<>();

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        tableCustomer.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
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
        tableCustomer.setRowHeight(25);
        jScrollPane1.setViewportView(tableCustomer);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1051, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 52, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1051, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel3.setBackground(new java.awt.Color(12, 83, 151));

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Cliente");

        txtSerch.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtSerch.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel2.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Condición frente al I.V.A.");

        cboIVA.setBackground(new java.awt.Color(255, 255, 255));
        cboIVA.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboIVA.setBorder(null);
        cboIVA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboIVAActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSerch, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(273, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtSerch, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cboIVAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboIVAActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboIVAActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cboIVA;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableCustomer;
    private javax.swing.JTextField txtSerch;
    // End of variables declaration//GEN-END:variables
}
