/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import dao.genericDAO;
import dao.productDAO;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class productSubcategoriesEditDialog extends javax.swing.JDialog {
    
    productDAO queriesProduct = new productDAO();
    genericDAO queriesGeneric = new genericDAO();    

    private String subcategoriaCreada;
    private int state;
    private String filaSeleccionada = "";
    private String comboSeleccionado= "";
    boolean activo;
    
    private String catPadre;
    
    public void setCategoriaPadre(String categoria){
        catPadre = categoria;
        infoCombo();    
    }

    public String getSubcategoriaCreada() {       
        return subcategoriaCreada;
    }
       
    public productSubcategoriesEditDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();    
        llenarCombos();        
        actionEdit();
        inicializar();
     
    }
  
    void inicializar(){

        cboCatPEdit.setSelectedIndex(0);
        cboChaCatP.setSelectedIndex(0);

        limpiarJtable();
        
        txtNameEdit.setText("");
        txtNameEdit.setEnabled(false);
        
        btnActDes.setEnabled(false);
        btnConfirmEdit.setEnabled(false);
        btnEdit.setEnabled(false);
        btnCancelEdit.setEnabled(false);
  
        txtNameEdit.setText("");
        
        cboChaCatP.setEnabled(false);
        btnConfirmChaCatP.setEnabled(false);

    }
    
    void llenarCombos(){
        queriesGeneric.llenarCombos(cboCatPEdit,"product_categories");
        queriesGeneric.llenarCombos(cboChaCatP,"product_categories");
    }

    void limpiarJtable(){
        DefaultTableModel modelo = (DefaultTableModel) tableCategory.getModel();
        modelo.setRowCount(0); 
    }
    
    void actDesactSubcat(){       
        queriesGeneric.updateState("product_subcategories",filaSeleccionada, queriesGeneric.verificarState("product_subcategories",filaSeleccionada));
        limpiarJtable();
        llenarComboCatPadreInEdit();
        btnActDes.setEnabled(false);
        btnEdit.setEnabled(false);
     
    }
    
    void editName(){
        
        if (!queriesGeneric.nameExists(txtNameEdit.getText(),"product_subcategories")) {
            queriesGeneric.updateName("product_subcategories", filaSeleccionada, txtNameEdit.getText().toUpperCase());
            subcategoriaCreada = txtNameEdit.getText().toUpperCase();
            limpiarJtable();   
            cboCatPEdit.setSelectedItem(comboSeleccionado);
            queriesProduct.listTableSubcategories(tableCategory, queriesProduct.selectIdCategoria(cboCatPEdit.getSelectedItem().toString()));                
            txtNameEdit.setText("");
            txtNameEdit.setEnabled(false);
            btnActDes.setEnabled(false);
            btnEdit.setEnabled(false);                
        } else {
            JOptionPane.showMessageDialog(null, "La subcategoria ya existe.");
        }
    }
 
    void actionEdit(){
       
        tableCategory.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent Mouse_evt){
                JTable tablaO =(JTable) Mouse_evt.getSource();
                Point point = Mouse_evt.getPoint();
                int row = tablaO.rowAtPoint(point);
                         
                if(Mouse_evt.getClickCount()==1){
                    filaSeleccionada = String.valueOf(tableCategory.getValueAt(tableCategory.getSelectedRow(), 0).toString()); 
                    btnActDes.setEnabled(true);
                    btnEdit.setEnabled(true);            
                }else{
                    
                }            
            }
        });
         
        btnActDes.addActionListener(e -> {
            if(!activo){
                actDesactSubcat();
            }else{
                JOptionPane.showMessageDialog(null, "La casilla de verificación está marcada. Desmárcala para habilitar esta operación.");
                
            }
            
        });
        
        btnEdit.addActionListener(e -> {
            
            if(!activo){
                txtNameEdit.setEnabled(true);
                txtNameEdit.requestFocusInWindow(); 
                txtNameEdit.setText(filaSeleccionada);
                btnConfirmEdit.setEnabled(true);
                btnCancelEdit.setEnabled(true);
            }else{
                JOptionPane.showMessageDialog(null, "La casilla de verificación está marcada. Desmárcala para habilitar esta operación.");
                
            }
            
        });
        
        btnConfirmEdit.addActionListener(e -> {
            editName();
        });
        
        btnCancelEdit.addActionListener(e -> {
            inicializar();
        });
        
        checkBoxCambiarCatP.addActionListener(e -> {
            
            activo = checkBoxCambiarCatP.isSelected();
            labelTitleChaCatP.setEnabled(activo);
            cboChaCatP.setEnabled(activo);
        });
        
        cboChaCatP.addActionListener(e -> {
            String categoria = (String) cboChaCatP.getSelectedItem();

            if (categoria != null && !categoria.equals("Seleccione una categoría")) {
                btnConfirmChaCatP.setEnabled(true);
            }
        });
        
        btnConfirmChaCatP.addActionListener(e -> {
            cambiarCatPadre();
        });
        
        cboCatPEdit.addActionListener(e -> {
            int id = queriesProduct.selectIdCategoria(cboCatPEdit.getSelectedItem().toString());
            queriesProduct.listTableSubcategories(tableCategory, id);
        });
      
    }
    
    void llenarComboCatPadreInEdit(){
        String categoria = (String) cboCatPEdit.getSelectedItem();
        comboSeleccionado=cboCatPEdit.getSelectedItem().toString();
            
        if (categoria != null && !categoria.equals("Seleccione una categoría")) {
            int idCat = queriesProduct.selectIdCategoria(categoria);
            queriesProduct.listTableSubcategories(tableCategory, idCat);
        }
    }
    
    void cambiarCatPadre(){
        
        int fileSel = tableCategory.getSelectedRow();
        
        int confirmacion = JOptionPane.showConfirmDialog(
            null,
            "¿Deseás cambiar la categoria padre de "+filaSeleccionada+" a " + cboChaCatP.getSelectedItem().toString() + "?",
            "Confirmación",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        int id = queriesProduct.selectIdCategoria(cboChaCatP.getSelectedItem().toString());
        queriesProduct.updateCatPadre(filaSeleccionada,id);
        limpiarJtable(); 
        
        queriesProduct.listTableSubcategories(tableCategory, queriesProduct.selectIdCategoria(cboCatPEdit.getSelectedItem().toString()));       
        checkBoxCambiarCatP.setSelected(false);
        cboChaCatP.setEnabled(false);
        btnConfirmChaCatP.setEnabled(false);    

    }
    
    public void infoCombo(){

        for (int i = 0; i < cboCatPEdit.getItemCount(); i++) {
            
            if (cboCatPEdit.getItemAt(i).toString().equalsIgnoreCase(catPadre)) {
                cboCatPEdit.setSelectedIndex(i);
                break;
            }
        }        
    } 
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCategory = new javax.swing.JTable();
        btnActDes = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        txtNameEdit = new javax.swing.JTextField();
        btnCancelEdit = new javax.swing.JButton();
        btnConfirmEdit = new javax.swing.JButton();
        labelTitleEdit2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        checkBoxCambiarCatP = new javax.swing.JCheckBox();
        labelTitleChaCatP = new javax.swing.JLabel();
        cboChaCatP = new javax.swing.JComboBox<>();
        btnConfirmChaCatP = new javax.swing.JButton();
        cboCatPEdit = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión - Editar categorias.");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        jPanel7.setBackground(new java.awt.Color(245, 248, 255));

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(12, 83, 151));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/registrarse64.png"))); // NOI18N
        jLabel1.setText("Editar Subcategorías.");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        tableCategory.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        tableCategory.setForeground(new java.awt.Color(65, 65, 63));
        tableCategory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableCategory.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tableCategory.setRowHeight(30);
        jScrollPane1.setViewportView(tableCategory);

        btnActDes.setBackground(new java.awt.Color(255, 255, 204));
        btnActDes.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        btnActDes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/switch32.png"))); // NOI18N
        btnActDes.setText("Activar/Desactivar");

        btnEdit.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/editar-32.png"))); // NOI18N

        txtNameEdit.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        btnCancelEdit.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnCancelEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/borrador32.png"))); // NOI18N

        btnConfirmEdit.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnConfirmEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnActDes, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNameEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConfirmEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnConfirmEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNameEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnActDes, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                .addContainerGap())
        );

        labelTitleEdit2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelTitleEdit2.setForeground(new java.awt.Color(35, 35, 38));
        labelTitleEdit2.setText("Categoría padre:");

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        checkBoxCambiarCatP.setBackground(new java.awt.Color(255, 255, 255));
        checkBoxCambiarCatP.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        checkBoxCambiarCatP.setForeground(new java.awt.Color(35, 35, 38));
        checkBoxCambiarCatP.setText("¿Cambiar categoría padre?");
        checkBoxCambiarCatP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        checkBoxCambiarCatP.setFocusPainted(false);

        labelTitleChaCatP.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelTitleChaCatP.setForeground(new java.awt.Color(35, 35, 38));
        labelTitleChaCatP.setText("Categoría padre:");

        cboChaCatP.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        btnConfirmChaCatP.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btnConfirmChaCatP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBoxCambiarCatP)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(labelTitleChaCatP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboChaCatP, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConfirmChaCatP, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(168, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(checkBoxCambiarCatP)
                .addGap(6, 6, 6)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnConfirmChaCatP, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboChaCatP, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelTitleChaCatP, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(labelTitleEdit2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboCatPEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelTitleEdit2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCatPEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                productSubcategoriesEditDialog dialog = new productSubcategoriesEditDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnActDes;
    private javax.swing.JButton btnCancelEdit;
    private javax.swing.JButton btnConfirmChaCatP;
    private javax.swing.JButton btnConfirmEdit;
    private javax.swing.JButton btnEdit;
    private javax.swing.JComboBox<String> cboCatPEdit;
    private javax.swing.JComboBox<String> cboChaCatP;
    private javax.swing.JCheckBox checkBoxCambiarCatP;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelTitleChaCatP;
    private javax.swing.JLabel labelTitleEdit2;
    private javax.swing.JTable tableCategory;
    private javax.swing.JTextField txtNameEdit;
    // End of variables declaration//GEN-END:variables
}
