/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import dao.genericDAO;
import dao.productDAO;
import java.awt.Color;
import javax.swing.JOptionPane;

public class productSubategoriesNewDialog extends javax.swing.JDialog {
    
    productDAO qProduct = new productDAO();
    genericDAO qGeneric = new genericDAO();
    
    private String subcategoriaCreada;
    
    public int state = 1;
    String catPadre;

    public void setCategoriaPadre(String categoria){
        catPadre = categoria;
        infoCombo();
    }
    
    public String getSubcategoriaCreada() {       
        return subcategoriaCreada;
    }
    
    public productSubategoriesNewDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents(); 
        
        txtName.setText("");
        checkState.setSelected(true);
        
        llenarCombos();
        actionButtons();
              
    }
    
    void llenarCombos(){
        qGeneric.llenarCombos(cboCatPNew,"product_categories");
        
    }
 
    void newSubcategory(){

        int confirmacion = JOptionPane.showConfirmDialog(
            null,
            "¿Deseás registrar subcategoria " + txtName.getText().toUpperCase() + "?",
            "Confirmación",
            JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        if (!qGeneric.nameExists(txtName.getText().toUpperCase(),"product_subcategories")) {
            qProduct.insertSubcategory(
                    qProduct.selectIdCategoria(cboCatPNew.getSelectedItem().toString()),
                    txtName.getText().toUpperCase(),
                    state);
            subcategoriaCreada = txtName.getText().toUpperCase();
            JOptionPane.showMessageDialog(null, "Subategoria creada!");
        } else {
            JOptionPane.showMessageDialog(null, "El nombre ya existe!");
        }
    }
 
    void actionButtons(){
        
        checkState.addActionListener(e -> {
            boolean activo = checkState.isSelected();

            if (activo) {
                state = 1;
            } else {
                state = 0;
            }
        });
       
        btnRegistrar.addActionListener(e -> {

            if (cboCatPNew.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(null, "Por favor, seleccione una categoria padre.");
                cboCatPNew.requestFocusInWindow();
                return;
            }

            if (txtName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "¡Ingrese un nombre!..");
                txtName.requestFocusInWindow();
                return;
            }

            newSubcategory();
            this.dispose();
        });           
    }
    
    public void infoCombo(){

        for (int i = 0; i < cboCatPNew.getItemCount(); i++) {
            
            if (cboCatPNew.getItemAt(i).toString().equalsIgnoreCase(catPadre)) {
                cboCatPNew.setSelectedIndex(i);
                break;
            }
        }        
    } 
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        labelCatPNew1 = new javax.swing.JLabel();
        labelState = new javax.swing.JLabel();
        labelTitleNameNew = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        cboCatPNew = new javax.swing.JComboBox<>();
        checkState = new javax.swing.JCheckBox();
        txtName = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión - Editar categorias.");
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        labelCatPNew1.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        labelCatPNew1.setForeground(new java.awt.Color(35, 35, 38));
        labelCatPNew1.setText("Categoría padre:");

        labelState.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelState.setForeground(new java.awt.Color(35, 35, 38));
        labelState.setText("Estado:");

        labelTitleNameNew.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        labelTitleNameNew.setForeground(new java.awt.Color(35, 35, 38));
        labelTitleNameNew.setText("Nombre:");

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit-32.png"))); // NOI18N

        btnRegistrar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(12, 83, 151));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        cboCatPNew.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        checkState.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        checkState.setForeground(new java.awt.Color(12, 83, 151));
        checkState.setText("Activa");

        txtName.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labelState, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelTitleNameNew, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(checkState)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(txtName))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelCatPNew1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCatPNew, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCatPNew1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCatPNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelState, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkState))
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelTitleNameNew, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(245, 248, 255));

        jLabel1.setBackground(new java.awt.Color(245, 248, 255));
        jLabel1.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(12, 83, 151));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/registro64.png"))); // NOI18N
        jLabel1.setText("Crear Subcategorías.");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                productSubategoriesNewDialog dialog = new productSubategoriesNewDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JComboBox<String> cboCatPNew;
    private javax.swing.JCheckBox checkState;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JLabel labelCatPNew1;
    private javax.swing.JLabel labelState;
    private javax.swing.JLabel labelTitleNameNew;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}
