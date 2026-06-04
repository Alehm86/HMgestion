/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import dao.genericDAO;
import dao.productDAO;
import models.Brands;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import utils.utility;


public class productBrandEditDialog extends javax.swing.JDialog {

    productDAO qProduct = new productDAO();
    genericDAO qGeneric = new genericDAO();
    
    Brands classBrand = new Brands();
    
    utility utils = new utility();
    
    public String marcaSeleccionada = "";
    private String marcaCreada;

    public String getMarcaCreada() {
        return marcaCreada;
    }
    
    private TableRowSorter<DefaultTableModel> sorter;
        
    public productBrandEditDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
        actionButtons();
        inicializar();
        
        utils.agregarPlaceholderN(txtSerch, "Buscar...");
                   
        qProduct.listTableBrands(tableBrands);
        
        tableBrands.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent Mouse_evt){
                JTable tablaO =(JTable) Mouse_evt.getSource();
                Point point = Mouse_evt.getPoint();
                int row = tablaO.rowAtPoint(point);
                         
                if(Mouse_evt.getClickCount()==1){
                    marcaSeleccionada = String.valueOf(tableBrands.getValueAt(tableBrands.getSelectedRow(), 0).toString()); 
                    txtBrand.setText(marcaSeleccionada);
                    txtBrand.requestFocusInWindow();
                    txtBrand.setEnabled(true);
                    btnCancel.setEnabled(true);
                    btnConfirm.setEnabled(true);
                    tableBrands.setEnabled(false);
                    tableBrands.setBackground(new Color(153, 153, 153));
                    
                }
                else{
                }            
            }
        });
        
        activarBuscadorTabla();
        
        txtSerch.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            txtSerch.setText("");
            }
        });
    }
    
    private void inicializar(){
        txtBrand.setText("");
        txtBrand.setEnabled(false);
        btnCancel.setEnabled(false);
        btnConfirm.setEnabled(false);
    }
  
    private void activarBuscadorTabla() {
        DefaultTableModel modelo = (DefaultTableModel) tableBrands.getModel();
        sorter = new TableRowSorter<>(modelo);
        tableBrands.setRowSorter(sorter);

        txtSerch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filtrar();
            }

            public void removeUpdate(DocumentEvent e) {
                filtrar();
            }

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
            String opcion=labelTitle.getText();
            classBrand.name=txtBrand.getText().toUpperCase();


            if (!qGeneric.nameExists(classBrand.getName(),"product_brands")) {
                
                int confirmacion = JOptionPane.showConfirmDialog(
                    null,
                    "¿Deseás editar esta marca?",
                    "Confirmación",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirmacion != JOptionPane.YES_OPTION) {
                    return;
                }
                    
                qProduct.updateBrand(classBrand.getName(),marcaSeleccionada);
                marcaCreada = classBrand.getName();
                qProduct.listTableBrands(tableBrands);
                
                JOptionPane.showMessageDialog(null, "Marca editada correctamente.");
                
                int msjNuevo = JOptionPane.showConfirmDialog(
                    this,
                    "¿Deseas modificar el nombre de otra marca?",
                    "Confirmar registro",
                    JOptionPane.YES_NO_OPTION
                );

                if (msjNuevo == JOptionPane.YES_OPTION) {
                    tableBrands.setEnabled(true);
                    tableBrands.setBackground(new Color(255, 255, 255));
                }else{
                    this.dispose();
                }
            } else {
                JOptionPane.showMessageDialog(null, "La marca ya existe.");
            }
            
        }
        });
    
        btnCancel.addActionListener(e->{
           inicializar();
           tableBrands.setEnabled(true);
           tableBrands.setBackground(new Color(255, 255, 255));
        });
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jPanelBrand = new javax.swing.JPanel();
        labelBrand = new javax.swing.JLabel();
        txtBrand = new javax.swing.JTextField();
        btnCancel = new javax.swing.JButton();
        btnConfirm = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableBrands = new javax.swing.JTable();
        txtSerch = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        labelTitle = new javax.swing.JLabel();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión - Gestionar marcas");
        setBackground(new java.awt.Color(255, 255, 255));
        setFont(new java.awt.Font("Poppins", 0, 10)); // NOI18N
        setResizable(false);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setToolTipText("Editar marcas.");

        jPanelBrand.setBackground(new java.awt.Color(255, 255, 255));
        jPanelBrand.setEnabled(false);

        labelBrand.setBackground(new java.awt.Color(255, 255, 255));
        labelBrand.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        labelBrand.setForeground(new java.awt.Color(12, 83, 151));
        labelBrand.setText("Nombre:");

        txtBrand.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        btnCancel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/borrador32.png"))); // NOI18N

        btnConfirm.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnConfirm.setForeground(new java.awt.Color(12, 83, 151));
        btnConfirm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnConfirm.setText("Registrar");
        btnConfirm.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

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
                        .addComponent(txtBrand))
                    .addGroup(jPanelBrandLayout.createSequentialGroup()
                        .addGap(0, 184, Short.MAX_VALUE)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConfirm)))
                .addContainerGap())
        );
        jPanelBrandLayout.setVerticalGroup(
            jPanelBrandLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBrandLayout.createSequentialGroup()
                .addGroup(jPanelBrandLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelBrandLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 36, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setFocusTraversalPolicyProvider(true);

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
                .addGap(10, 10, 10)
                .addComponent(txtSerch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(245, 248, 255));

        labelTitle.setBackground(new java.awt.Color(255, 255, 255));
        labelTitle.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        labelTitle.setForeground(new java.awt.Color(12, 83, 151));
        labelTitle.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/registrarse64.png"))); // NOI18N
        labelTitle.setText("Editar nombre de marca.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(113, 113, 113))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 684, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelBrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelBrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 675, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

   
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                productBrandEditDialog dialog = new productBrandEditDialog(new javax.swing.JFrame(), true);
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
