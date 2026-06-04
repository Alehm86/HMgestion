/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import dao.genericDAO;
import dao.productDAO;
import models.Brands;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;


public class productBrandNewDialog extends javax.swing.JDialog {

    productDAO qProduct = new productDAO();
    genericDAO qGeneric = new genericDAO();
    
    Brands classBrand = new Brands();
    
    public String marcaSeleccionada = "";
    private String marcaCreada;

    public String getMarcaCreada() {
        return marcaCreada;
    }
    
    private TableRowSorter<DefaultTableModel> sorter;
        
    public productBrandNewDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        start();
        setLocationRelativeTo(parent);
        actionButtons();
        txtBrand.requestFocusInWindow();
                
        qProduct.listTableBrands(tableBrands);
            
        activarBuscadorTabla();
        
        txtSerch.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            txtSerch.setText("");
            }
        });
        
        
        txtBrand.getDocument().addDocumentListener(new DocumentListener() {

            private void validar() {
                String texto = txtBrand.getText().trim();
                btnConfirm.setEnabled(true);
                btnCancel.setEnabled(true);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                validar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validar();
            }
        });

    }
    
    private void start(){
        btnCancel.setEnabled(false);
        btnConfirm.setEnabled(false);
    }

    private void activarBuscadorTabla() {
        DefaultTableModel modelo = (DefaultTableModel) tableBrands.getModel();
        sorter = new TableRowSorter<>(modelo);
        tableBrands.setRowSorter(sorter);

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
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
                }
            }
        });
    }
    
    private void actionButtons(){
        
        btnConfirm.addActionListener(e->{
            if(!txtBrand.getText().isEmpty()){
//            String opcion=labelTitle.getText();
            classBrand.name=txtBrand.getText().toUpperCase();

            
            if (!qGeneric.nameExists(classBrand.getName(),"product_brands")) {
                    
                qProduct.insertBrand(classBrand.getName());
                marcaCreada = classBrand.getName();
                qProduct.listTableBrands(tableBrands);
                
                JOptionPane.showMessageDialog(null, "Marca registrada correctamente.");
                
                int msjNuevo = JOptionPane.showConfirmDialog(
                    this,
                    "¿Deseas registrar otra marca?",
                    "Confirmar registro",
                    JOptionPane.YES_NO_OPTION
                );

                if (msjNuevo == JOptionPane.YES_OPTION) {
                    txtBrand.setText("");
                    txtBrand.requestFocusInWindow(); 
                }else{
                    this.dispose();
                }
                
            } else {
                JOptionPane.showMessageDialog(null, "La marca ya existe.");
            }
             
        }
        });

        btnCancel.addActionListener(e->{
           txtBrand.setText("");
           txtBrand.requestFocusInWindow(); 
           start();
        });
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox<>();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPanel4 = new javax.swing.JPanel();
        jPanelBrand = new javax.swing.JPanel();
        labelBrand = new javax.swing.JLabel();
        txtBrand = new javax.swing.JTextField();
        btnConfirm = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableBrands = new javax.swing.JTable();
        txtSerch = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        labelTitle = new javax.swing.JLabel();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión - Registrar marca");
        setBackground(new java.awt.Color(255, 255, 255));
        setFont(new java.awt.Font("Poppins", 0, 10)); // NOI18N
        setResizable(false);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setToolTipText("HM Gestión - Gestion Marcas.");

        jPanelBrand.setBackground(new java.awt.Color(255, 255, 255));
        jPanelBrand.setEnabled(false);

        labelBrand.setBackground(new java.awt.Color(255, 255, 255));
        labelBrand.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        labelBrand.setForeground(new java.awt.Color(12, 83, 151));
        labelBrand.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelBrand.setText("Nombre:");

        txtBrand.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        btnConfirm.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnConfirm.setForeground(new java.awt.Color(12, 83, 151));
        btnConfirm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnConfirm.setText("Registrar");
        btnConfirm.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        btnCancel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/borrador32.png"))); // NOI18N

        javax.swing.GroupLayout jPanelBrandLayout = new javax.swing.GroupLayout(jPanelBrand);
        jPanelBrand.setLayout(jPanelBrandLayout);
        jPanelBrandLayout.setHorizontalGroup(
            jPanelBrandLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBrandLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBrandLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelBrandLayout.createSequentialGroup()
                        .addComponent(labelBrand)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBrand, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelBrandLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConfirm)))
                .addContainerGap())
        );
        jPanelBrandLayout.setVerticalGroup(
            jPanelBrandLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBrandLayout.createSequentialGroup()
                .addGroup(jPanelBrandLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelBrand, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(txtBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelBrandLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setForeground(new java.awt.Color(65, 65, 63));

        tableBrands.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        tableBrands.setForeground(new java.awt.Color(65, 65, 63));
        tableBrands.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableBrands.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tableBrands.setFillsViewportHeight(true);
        tableBrands.setFocusable(false);
        tableBrands.setRowHeight(30);
        jScrollPane2.setViewportView(tableBrands);

        txtSerch.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                    .addComponent(txtSerch))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtSerch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelBrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelBrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(245, 248, 255));

        labelTitle.setBackground(new java.awt.Color(255, 255, 255));
        labelTitle.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        labelTitle.setForeground(new java.awt.Color(12, 83, 151));
        labelTitle.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/registro64.png"))); // NOI18N
        labelTitle.setText("Datos de marca nueva.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 623, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

   
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                productBrandNewDialog dialog = new productBrandNewDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelBrand;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelBrand;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JTable tableBrands;
    private javax.swing.JTextField txtBrand;
    private javax.swing.JTextField txtSerch;
    // End of variables declaration//GEN-END:variables
}
