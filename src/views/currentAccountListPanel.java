/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package views;

import dao.currentAccountDAO;
import java.awt.Dialog;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import utils.tableStyleUtil;
import utils.utility;


public class currentAccountListPanel extends javax.swing.JPanel {

    currentAccountDAO qCA = new currentAccountDAO();
    utility utils = new utility();
    
    private int idCA = -1;
    private int id_customer = -1;
    private String customerName;
    private String CUIT;
    
    private TableRowSorter<DefaultTableModel> sorter;
    
    DefaultTableModel dtmCurrentAccounts = new DefaultTableModel();
    
    public currentAccountListPanel() {
        
        initComponents();
        
        utils.agregarPlaceholderN(txtBuscar, "Buscar...");
        
           
        tableCurrentAccounts();
        actions();
        activarBuscadorTabla();
    }
    
    private void tableCurrentAccounts(){

        String[] titulo = {"id", "Numero de cuenta", "Cliente","Id cliente","DNI/CUIT","Teléfono", "Mail", "Fecha de ultimo movimiento", "Saldo"};
        dtmCurrentAccounts.setColumnIdentifiers(titulo);        
        tableCurrentAccount.setModel(dtmCurrentAccounts);
        
        qCA.listCurrentAccount(dtmCurrentAccounts);
        
        sorter = new TableRowSorter<>(dtmCurrentAccounts);

        tableCurrentAccount.setRowSorter(sorter);

        
        tableStyleUtil.applyPoppinsHeader(tableCurrentAccount);

        tableCurrentAccount.getColumnModel().getColumn(0).setMinWidth(0);
        tableCurrentAccount.getColumnModel().getColumn(0).setMaxWidth(0);
        tableCurrentAccount.getColumnModel().getColumn(0).setPreferredWidth(0);

        tableCurrentAccount.getColumnModel().getColumn(1).setPreferredWidth(40);
        tableCurrentAccount.getColumnModel().getColumn(2).setPreferredWidth(250);
        tableCurrentAccount.getColumnModel().getColumn(3).setPreferredWidth(40);
        tableCurrentAccount.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableCurrentAccount.getColumnModel().getColumn(5).setPreferredWidth(150); 
        tableCurrentAccount.getColumnModel().getColumn(6).setPreferredWidth(150);
        tableCurrentAccount.getColumnModel().getColumn(7).setPreferredWidth(150);
        tableCurrentAccount.getColumnModel().getColumn(8).setPreferredWidth(100);
        
        
        tableCurrentAccount.getTableHeader().setReorderingAllowed(false);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        tableCurrentAccount.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tableCurrentAccount.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tableCurrentAccount.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tableCurrentAccount.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
        tableCurrentAccount.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
    }
    
    private void actions(){

        tableCurrentAccount.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e){

                int filaVista = tableCurrentAccount.getSelectedRow();

                if(filaVista >= 0){

                    int filaModelo = tableCurrentAccount.convertRowIndexToModel(filaVista);

                    idCA = Integer.parseInt(dtmCurrentAccounts.getValueAt(filaModelo, 0).toString());
                    id_customer = Integer.parseInt(dtmCurrentAccounts.getValueAt(filaModelo, 3).toString());
                    customerName = String.valueOf(dtmCurrentAccounts.getValueAt(filaModelo, 2).toString());
                    CUIT = String.valueOf(dtmCurrentAccounts.getValueAt(filaModelo, 4).toString());                                      
                    
                }
            }
        });
        
        btnVer.addActionListener(e->{
            
            currentAccountViewForm pView = new currentAccountViewForm(idCA);
  
            
            if(idCA > 0){
                pView.setCurrentAccount(id_customer, customerName, CUIT);
                pView.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(null, "Seleccione una cuenta corriente de la lista...");
            }
        });
    }
    
    private void activarBuscadorTabla(){

        txtBuscar.getDocument().addDocumentListener(new DocumentListener(){

            private void filtrar(){

                String texto = txtBuscar.getText().trim();

                if(texto.equals("Buscar...") || texto.isEmpty()){

                    sorter.setRowFilter(null);

                }else{

                    sorter.setRowFilter(
                        RowFilter.regexFilter(
                            "(?i)" + Pattern.quote(texto)
                        )
                    );
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e){
                filtrar();
            }


            @Override
            public void removeUpdate(DocumentEvent e){
                filtrar();
            }


            @Override
            public void changedUpdate(DocumentEvent e){
                filtrar();
            }

        });
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btnVer = new javax.swing.JButton();
        txtBuscar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCurrentAccount = new javax.swing.JTable();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(245, 248, 255));

        btnVer.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnVer.setForeground(new java.awt.Color(12, 83, 151));
        btnVer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/see32.png"))); // NOI18N
        btnVer.setText("Ver");

        txtBuscar.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnVer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtBuscar)
                    .addComponent(btnVer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tableCurrentAccount.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        tableCurrentAccount.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableCurrentAccount.setRowHeight(35);
        jScrollPane1.setViewportView(tableCurrentAccount);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 979, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnVer;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableCurrentAccount;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
