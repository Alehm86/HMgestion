/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package forms;

import Class.GenericDAO;
import Class.ProductDAO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class frmSubcategoriesEdit extends javax.swing.JDialog {
    
    ProductDAO queriesProduct = new ProductDAO();
    GenericDAO queriesGeneric = new GenericDAO();    

    private String subcategoriaCreada;
    private int state;
    private String filaSeleccionada = "";
    private String comboSeleccionado= "";
    boolean activo;

    public String getSubcategoriaCreada() {       
        return subcategoriaCreada;
    }
    
    public frmSubcategoriesEdit(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        llenarCombos();        
        inicializar();
        actionEdit();
     
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
        queriesGeneric.llenarCombos(cboCatPEdit,"categories");
        queriesGeneric.llenarCombos(cboChaCatP,"categories");
    }

    void limpiarJtable(){
        DefaultTableModel modelo = (DefaultTableModel) tableCategory.getModel();
        modelo.setRowCount(0); 
    }
    
    void actDesactSubcat(){       
        queriesGeneric.updateState("subcategories",filaSeleccionada, queriesGeneric.verificarState("subcategories",filaSeleccionada));
        limpiarJtable();
        llenarComboCatPadreInEdit();
        btnActDes.setEnabled(false);
        btnEdit.setEnabled(false);
     
    }
    
    void editName(){
        
        if (!queriesGeneric.nameExists(txtNameEdit.getText(),"subcategories")) {
            queriesGeneric.updateName("subcategories", filaSeleccionada, txtNameEdit.getText().toUpperCase());
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
        
        btnExit.addActionListener(e->{
            this.dispose();
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
        txtNameEdit = new javax.swing.JTextField();
        btnConfirmEdit = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnCancelEdit = new javax.swing.JButton();
        cboCatPEdit = new javax.swing.JComboBox<>();
        labelTitleEdit2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        checkBoxCambiarCatP = new javax.swing.JCheckBox();
        labelTitleChaCatP = new javax.swing.JLabel();
        cboChaCatP = new javax.swing.JComboBox<>();
        btnConfirmChaCatP = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editar categorias.");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        jPanel7.setBackground(new java.awt.Color(12, 83, 151));

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Editar Subcategorías.");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        tableCategory.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
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
        tableCategory.setRowHeight(25);
        tableCategory.setSelectionBackground(new java.awt.Color(255, 204, 153));
        jScrollPane1.setViewportView(tableCategory);

        btnActDes.setBackground(new java.awt.Color(255, 255, 204));
        btnActDes.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        btnActDes.setForeground(new java.awt.Color(102, 102, 102));
        btnActDes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/switch32.png"))); // NOI18N
        btnActDes.setText("Activar/Desactivar");
        btnActDes.setBorder(null);
        btnActDes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActDes.setFocusPainted(false);
        btnActDes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnActDesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnActDesMouseExited(evt);
            }
        });
        btnActDes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActDesActionPerformed(evt);
            }
        });

        txtNameEdit.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtNameEdit.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNameEdit.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 0, 12), new java.awt.Color(13, 155, 219))); // NOI18N

        btnConfirmEdit.setBackground(new java.awt.Color(255, 255, 255));
        btnConfirmEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnConfirmEdit.setBorder(null);
        btnConfirmEdit.setBorderPainted(false);
        btnConfirmEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConfirmEdit.setFocusPainted(false);
        btnConfirmEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnConfirmEditMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnConfirmEditMouseExited(evt);
            }
        });
        btnConfirmEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmEditActionPerformed(evt);
            }
        });

        btnEdit.setBackground(new java.awt.Color(255, 255, 255));
        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/edit_121852.png"))); // NOI18N
        btnEdit.setBorder(null);
        btnEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEdit.setPreferredSize(new java.awt.Dimension(35, 39));
        btnEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditMouseExited(evt);
            }
        });
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnCancelEdit.setBackground(new java.awt.Color(255, 255, 255));
        btnCancelEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cancelar_32.png"))); // NOI18N
        btnCancelEdit.setBorder(null);
        btnCancelEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelEdit.setFocusPainted(false);
        btnCancelEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelEditMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelEditMouseExited(evt);
            }
        });
        btnCancelEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(btnActDes, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNameEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(btnConfirmEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnConfirmEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNameEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnActDes, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(btnCancelEdit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                .addContainerGap())
        );

        cboCatPEdit.setBackground(new java.awt.Color(255, 255, 255));
        cboCatPEdit.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboCatPEdit.setToolTipText("");
        cboCatPEdit.setBorder(null);
        cboCatPEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        labelTitleEdit2.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        labelTitleEdit2.setForeground(new java.awt.Color(105, 127, 155));
        labelTitleEdit2.setText("Categoría padre:");

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        checkBoxCambiarCatP.setBackground(new java.awt.Color(255, 255, 255));
        checkBoxCambiarCatP.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        checkBoxCambiarCatP.setForeground(new java.awt.Color(105, 127, 155));
        checkBoxCambiarCatP.setText("¿Cambiar categoría padre?");
        checkBoxCambiarCatP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        checkBoxCambiarCatP.setFocusPainted(false);

        labelTitleChaCatP.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        labelTitleChaCatP.setForeground(new java.awt.Color(105, 127, 155));
        labelTitleChaCatP.setText("Categoría padre:");

        cboChaCatP.setBackground(new java.awt.Color(255, 255, 255));
        cboChaCatP.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboChaCatP.setToolTipText("");
        cboChaCatP.setBorder(null);
        cboChaCatP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnConfirmChaCatP.setBackground(new java.awt.Color(255, 255, 255));
        btnConfirmChaCatP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnConfirmChaCatP.setBorder(null);
        btnConfirmChaCatP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConfirmChaCatP.setFocusPainted(false);
        btnConfirmChaCatP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnConfirmChaCatPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnConfirmChaCatPMouseExited(evt);
            }
        });
        btnConfirmChaCatP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmChaCatPActionPerformed(evt);
            }
        });

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
                        .addComponent(btnConfirmChaCatP, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(122, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(checkBoxCambiarCatP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(labelTitleChaCatP, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboChaCatP, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnConfirmChaCatP, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        btnExit.setBackground(new java.awt.Color(255, 255, 255));
        btnExit.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        btnExit.setForeground(new java.awt.Color(101, 129, 171));
        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit2_64.png"))); // NOI18N
        btnExit.setText("Salir");
        btnExit.setBorder(null);
        btnExit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnExitMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnExitMouseExited(evt);
            }
        });
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(labelTitleEdit2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboCatPEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelTitleEdit2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCatPEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed

    }//GEN-LAST:event_btnEditActionPerformed

    private void btnConfirmEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmEditActionPerformed

    }//GEN-LAST:event_btnConfirmEditActionPerformed

    private void btnActDesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActDesActionPerformed

    }//GEN-LAST:event_btnActDesActionPerformed

    private void btnActDesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActDesMouseEntered
        if (btnActDes.isEnabled()) {
            btnActDes.setBackground(new Color(238,238,150));
        } else {
            btnActDes.setBackground(new Color(255,255,204));
        }
    }//GEN-LAST:event_btnActDesMouseEntered

    private void btnActDesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActDesMouseExited
        btnActDes.setBackground(new Color(255,255,204));
    }//GEN-LAST:event_btnActDesMouseExited

    private void btnEditMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseEntered
        if (btnEdit.isEnabled()) {
            btnEdit.setBackground(new Color(255,215,0));
        } else {
            btnEdit.setBackground(new Color(255,255,255));
        }
        
    }//GEN-LAST:event_btnEditMouseEntered

    private void btnEditMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseExited
        btnEdit.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnEditMouseExited

    private void btnConfirmEditMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmEditMouseEntered
        if (btnConfirmEdit.isEnabled()) {
            btnConfirmEdit.setBackground(new Color(255,215,0));
        } else {
            btnConfirmEdit.setBackground(new Color(255,255,255));
        }
    }//GEN-LAST:event_btnConfirmEditMouseEntered

    private void btnConfirmEditMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmEditMouseExited
        btnConfirmEdit.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnConfirmEditMouseExited

    private void btnCancelEditMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelEditMouseEntered
        if (btnCancelEdit.isEnabled()) {
            btnCancelEdit.setBackground(new Color(255,127,39));
        } else {
            btnCancelEdit.setBackground(new Color(255,255,255));
        }
    }//GEN-LAST:event_btnCancelEditMouseEntered

    private void btnCancelEditMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelEditMouseExited
        btnCancelEdit.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCancelEditMouseExited

    private void btnCancelEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelEditActionPerformed

    private void btnExitMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseEntered
        btnExit.setBackground(new Color(238,238,238));
    }//GEN-LAST:event_btnExitMouseEntered

    private void btnExitMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseExited
        btnExit.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnExitMouseExited

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed

    }//GEN-LAST:event_btnExitActionPerformed

    private void btnConfirmChaCatPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmChaCatPActionPerformed

    }//GEN-LAST:event_btnConfirmChaCatPActionPerformed

    private void btnConfirmChaCatPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmChaCatPMouseEntered
        if (btnConfirmChaCatP.isEnabled()) {
            btnConfirmChaCatP.setBackground(new Color(255,215,0));
        } else {
            btnConfirmChaCatP.setBackground(new Color(255,255,255));
        }
    }//GEN-LAST:event_btnConfirmChaCatPMouseEntered

    private void btnConfirmChaCatPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmChaCatPMouseExited
        btnConfirmChaCatP.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnConfirmChaCatPMouseExited


    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frmSubcategoriesEdit dialog = new frmSubcategoriesEdit(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnExit;
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
