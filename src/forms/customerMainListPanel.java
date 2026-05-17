/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package forms;

import classDAO.customerDAO;
import classDAO.genericDAO;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JOptionPane;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class customerMainListPanel extends javax.swing.JPanel {

    genericDAO qGeneric = new genericDAO();
    customerDAO qCustomer= new customerDAO();
    
    private String filaSeleccionada = "";
    
    private String cuitFound;  
    
    private TableRowSorter<DefaultTableModel> sorter;
    DefaultTableModel tableProducts = new DefaultTableModel();
    
    public customerMainListPanel() {
        initComponents();
        
        qGeneric.llenarCombos(cboIVA, "customer_iva"); 
        qCustomer.ComboIdState(cboStates);
        cboStates.setSelectedIndex(1);
        qCustomer.listAllCustomerForState(tableCustomer,1);
        
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
            filtrarClientes();
        });
        cboStates.addActionListener(e->{
            filtrarClientes();
        });
          
        btnAltaCustomer.addActionListener(e-> {
 
            boolean estado = false;            
            
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            customerNewDialog fNewCustomer = new customerNewDialog(parent, true);
            
            fNewCustomer.setVisible(true);           
            if(estado=true){
                filtrarClientes();
            }                                  
        });  
        
        btnCustomerView.addActionListener(e-> {
            viewCustomer();  
        });            
    }
    
    private void viewCustomer(){
        boolean estado = false;
        String CUIT;

        if(!filaSeleccionada.isEmpty()){

            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            customerViewDialog fView = new customerViewDialog(parent, true);

            CUIT = qCustomer.selectCuit("name", filaSeleccionada);
            fView.dialogoEdit(CUIT); 

            fView.setVisible(true);

            estado = fView.dialogoClienteActualizado();                             
            if(estado=true){
                filtrarClientes();
            }                             
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione un cliente");
        } 
    }
    
    private void filtrarClientes(){

        boolean filtrarPorEstado = cboStates.getSelectedIndex() > 0;
        boolean filtrarPorIva = cboIVA.getSelectedIndex() > 0;

        if (filtrarPorEstado && filtrarPorIva) {

            int state = qCustomer.selectIdState(cboStates.getSelectedItem().toString());
            int idIva = qCustomer.selectIdIva(cboIVA.getSelectedItem().toString());

            qCustomer.listCustomerForIvaAndState(tableCustomer, idIva, state);

        } else if (filtrarPorEstado) {

            int state = qCustomer.selectIdState(cboStates.getSelectedItem().toString());
            qCustomer.listAllCustomerForState(tableCustomer, state);

        } else if (filtrarPorIva) {

            int idIva = qCustomer.selectIdIva(cboIVA.getSelectedItem().toString());
            qCustomer.listCustomerForIvaComplete(tableCustomer, idIva);

        } else {

            qCustomer.listAllCustomerComplete(tableCustomer);
        }

        activarBuscadorTabla();
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

        jPanel10 = new javax.swing.JPanel();
        btnAltaCustomer = new javax.swing.JButton();
        btnCustomerView = new javax.swing.JButton();
        jPanelClientContent = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtSerch = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cboIVA = new javax.swing.JComboBox<>();
        cboStates = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCustomer = new javax.swing.JTable();

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        btnAltaCustomer.setBackground(new java.awt.Color(255,255,255));
        btnAltaCustomer.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnAltaCustomer.setForeground(new java.awt.Color(12, 83, 151));
        btnAltaCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/clientNew32.png"))); // NOI18N
        btnAltaCustomer.setText("Alta");
        btnAltaCustomer.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btnAltaCustomer.setBorderPainted(false);
        btnAltaCustomer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAltaCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAltaCustomerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAltaCustomerMouseExited(evt);
            }
        });
        btnAltaCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAltaCustomerActionPerformed(evt);
            }
        });

        btnCustomerView.setBackground(new java.awt.Color(255, 255, 255));
        btnCustomerView.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnCustomerView.setForeground(new java.awt.Color(12, 83, 151));
        btnCustomerView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/clientView32.png"))); // NOI18N
        btnCustomerView.setText("Ver Cliente");
        btnCustomerView.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        btnCustomerView.setBorderPainted(false);
        btnCustomerView.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCustomerView.setFocusable(false);
        btnCustomerView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCustomerViewMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCustomerViewMouseExited(evt);
            }
        });
        btnCustomerView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerViewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCustomerView, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAltaCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(871, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnAltaCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCustomerView, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelClientContent.setBackground(new java.awt.Color(255, 255, 255));
        jPanelClientContent.setPreferredSize(new java.awt.Dimension(1600, 800));

        jPanel3.setBackground(new java.awt.Color(35, 35, 38));

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Cliente");

        txtSerch.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtSerch.setForeground(new java.awt.Color(65, 65, 63));
        txtSerch.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSerch.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel2.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Condición frente al I.V.A.");

        cboIVA.setBackground(new java.awt.Color(255, 255, 255));
        cboIVA.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        cboIVA.setForeground(new java.awt.Color(65, 65, 63));
        cboIVA.setBorder(null);
        cboIVA.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboIVA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboIVAActionPerformed(evt);
            }
        });

        cboStates.setBackground(new java.awt.Color(255, 255, 255));
        cboStates.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        cboStates.setForeground(new java.awt.Color(65, 65, 63));
        cboStates.setBorder(null);
        cboStates.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel3.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Estado");

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
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(cboStates, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtSerch, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(cboStates, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

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
        tableCustomer.setFillsViewportHeight(true);
        tableCustomer.setRowHeight(25);
        tableCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCustomerMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableCustomer);

        javax.swing.GroupLayout jPanelClientContentLayout = new javax.swing.GroupLayout(jPanelClientContent);
        jPanelClientContent.setLayout(jPanelClientContentLayout);
        jPanelClientContentLayout.setHorizontalGroup(
            jPanelClientContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        jPanelClientContentLayout.setVerticalGroup(
            jPanelClientContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelClientContentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE))
        );

        jScrollPane1.getAccessibleContext().setAccessibleParent(this);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelClientContent, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1175, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanelClientContent, javax.swing.GroupLayout.PREFERRED_SIZE, 797, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAltaCustomerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAltaCustomerMouseEntered
        btnAltaCustomer.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnAltaCustomerMouseEntered

    private void btnAltaCustomerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAltaCustomerMouseExited
        btnAltaCustomer.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnAltaCustomerMouseExited

    private void btnAltaCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltaCustomerActionPerformed

    }//GEN-LAST:event_btnAltaCustomerActionPerformed

    private void btnCustomerViewMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCustomerViewMouseEntered
        btnCustomerView.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnCustomerViewMouseEntered

    private void btnCustomerViewMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCustomerViewMouseExited
        btnCustomerView.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCustomerViewMouseExited

    private void btnCustomerViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerViewActionPerformed

    }//GEN-LAST:event_btnCustomerViewActionPerformed

    private void cboIVAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboIVAActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboIVAActionPerformed

    private void tableCustomerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCustomerMouseClicked
        if (evt.getClickCount() == 2) {

            int fila = tableCustomer.getSelectedRow();

            if (fila != -1) {
                viewCustomer();      
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione una fila");
            }
        }      
    }//GEN-LAST:event_tableCustomerMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAltaCustomer;
    private javax.swing.JButton btnCustomerView;
    private javax.swing.JComboBox<String> cboIVA;
    private javax.swing.JComboBox<String> cboStates;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelClientContent;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableCustomer;
    private javax.swing.JTextField txtSerch;
    // End of variables declaration//GEN-END:variables
}
