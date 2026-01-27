/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package OldNotUsed;

import Class.GenericDAO;
import OldNotUsed.frmMenu;
import javax.swing.table.DefaultTableModel;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import Class.ProductDAO;
import forms.frmBrandEdit;
import forms.frmBrandNew;
import forms.frmEditPrice;
import forms.frmSalesManagement;
import forms.frmStockAdjustment;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class frmProducts extends javax.swing.JFrame {
    ProductDAO queries = new ProductDAO();
    GenericDAO queriesGeneric = new GenericDAO();
    
    fondoPanel background = new fondoPanel();

    DefaultTableModel tableProducts = new DefaultTableModel();
    DefaultTableModel tableFinancy = new DefaultTableModel();
    
    private String filaSeleccionada = "";
    //⚠️
    //✅
    
    public frmProducts() {

        this.setContentPane(background);
        initComponents();
        this.setExtendedState(6);
        
        actionButtons();
        agregarPlaceholder(txtCodProduct, "Ingrese código de producto...");       
        queriesGeneric.llenarCombosActivos(cboCategories,"categories");
        queriesGeneric.llenarCombos(cboBrand,"brands");
        titlesButtons();
        llenarSubcategorias();
        menuSuperior(); 
        
        
    }
   
    class fondoPanel extends JPanel{
         private Image imagen;      
         @Override
         public void paint(Graphics g)
         {
             imagen = new ImageIcon(getClass().getResource("/Wallpapers/fondoMenu.jpg")).getImage();          
             g.drawImage(imagen,0, 0, getWidth(), getHeight(),this);         
             setOpaque(false);      
             super.paint(g);
         }
     }
    
    private void titlesButtons(){
        btnRerturnProduct.setToolTipText("Devoluciones de productos");
        btnSerchCode.setToolTipText("Buscar");
        btnSerchCode2.setToolTipText("Buscar");
        btnAddStock.setToolTipText("Ingrsar stock de productos");
        btnMenu.setToolTipText("Menu principal");
        btnSales.setToolTipText("Ventas");
        btnNewProduct.setToolTipText("Registrar nuevo producto");
        btnEdit.setToolTipText("Editar datos de producto");
        btnEditPrice.setToolTipText("Editar precio de producto");
        btnServiceTec.setToolTipText("Modulo Servicio Técnico");
    }
   
    private void addTableFinancy(String entidad, String financy, double ctf, double cuota){
         tableFinancy.addRow(new Object[]{entidad,financy,ctf,cuota});
    }
   
    private void menuSuperior(){

        jMenuNewBrand.addActionListener(e -> {
            frmBrandNew dialogo = new frmBrandNew(this, true);
            dialogo.setVisible(true);
        });
        
        jMenuBrandEdit.addActionListener(e -> {
            frmBrandEdit dialogo = new frmBrandEdit(this, true);
            dialogo.setVisible(true);
        });

        jMenuCategory.addActionListener(e -> {
//            frmCategoriesAndSubcategoriesMgmt dialogo = new frmCategoriesAndSubcategoriesMgmt(this, true);
//            dialogo.setVisible(true);
        });

//        jMenuNewSupplier.addActionListener(e -> {
//            frmSupplierManagement dialogo = new frmSupplierManagement(this, true);
//            dialogo.setVisible(true);
//        });

        jMenuNewProduct.addActionListener(e -> {
            frmNewProduct_1 edit = new frmNewProduct_1();
            edit.setVisible(true);
            this.dispose();

        });

        jMenuAjusteDeStock.addActionListener(e -> {
            frmStockAdjustment stockAdj = new frmStockAdjustment();
            stockAdj.setVisible(true);
        });
         

    }
    
    void agregarPlaceholder(JTextField campo, String placeholder) {
        campo.setForeground(Color.GRAY);
        campo.setText(placeholder);

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setForeground(Color.GRAY);
                    campo.setText(placeholder);
                }
            }
        });
    }
    
    void llenarSubcategorias(){
        cboCategories.addActionListener(e -> {
            String categoria = (String) cboCategories.getSelectedItem();

            if (categoria != null && !categoria.equals("Seleccione una categoría")) {
                int idCat = queries.selectIdCategoria(categoria);
                cboSubcategories.removeAllItems();
                queries.llenarCombosSubcategories(cboSubcategories, idCat);          
            }
        });
    }
    
    void filtrarPorCombos(){
        
        String categoria = (String) cboCategories.getSelectedItem();
        String subcategoria = (String) cboSubcategories.getSelectedItem();
        String brand = (String) cboBrand.getSelectedItem();
        
        int idCat = queries.selectIdCategoria(categoria); 
        int idSubcat = queriesGeneric.selectId("id_subcategory","subcategories",subcategoria);
        int idBrand = queriesGeneric.selectId("id_brand","brands",brand);
        
        if(cboBrand.getSelectedIndex() != 0){
            if(cboCategories.getSelectedIndex() != 0){               
                if(cboSubcategories.getSelectedIndex() != 0){
                    queries.listProdForBrandAndSubCat(jtablePrducts, idBrand, idSubcat);
                }else{
                    queries.listProdForBrandAndCat(jtablePrducts, idBrand, idCat);
                }
            }else{
                queries.listAllProdForBrand(jtablePrducts, idBrand);
            }          
            
        }else{
            if(cboCategories.getSelectedIndex() != 0){
                if(cboSubcategories.getSelectedIndex() != 0){
                    queries.listAllProdForSubcategory(jtablePrducts,idSubcat);
                }else{
                    queries.listAllProdForIDCategory(jtablePrducts,idCat);
                }               
            }else{
                queries.listAllProdForCategory(jtablePrducts);
            }
        }
        
    }
    
    private void actionButtons(){
        
        btnExit.addActionListener(e-> {
            this.dispose();
        });
        
        btnRerturnProduct.addActionListener(e-> {
            frmReturnProducts returnProducts = new frmReturnProducts();
            returnProducts.setVisible(true);
            this.dispose();
        });
        
        btnSerchCode.addActionListener(e -> { 
            tableProducts.setRowCount(0);
            String productCode = txtCodProduct.getText().trim();

            if (!productCode.isEmpty() && !productCode.equals("Ingrese código de producto...")) {
                queries.listTableProducts(jtablePrducts, productCode);
                txtCodProduct.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "¡Debe ingresar un código de producto!");
                txtCodProduct.requestFocus();
            }
        });
        
        btnSerchCode2.addActionListener(e -> {
            tableProducts.setRowCount(0);
            filtrarPorCombos(); 
        });
        
        btnAddStock.addActionListener(e -> {
            frmStockManagement stockMgmt = new frmStockManagement();
            stockMgmt.setVisible(true);
            this.dispose();
        });
    
        btnMenu.addActionListener(e->{
            frmMenu menu = new frmMenu();
            menu.setVisible(true);
            this.dispose();
        });
        
        btnSales.addActionListener(e->{
            frmSalesManagement sales = new frmSalesManagement();
            sales.setVisible(true);
            this.dispose();
        });       
        
        btnNewProduct.addActionListener(e->{
            frmNewProduct_1 edit = new frmNewProduct_1();
            edit.setVisible(true);
            this.dispose();
        });
        
        btnEdit.addActionListener(e->{
            int id = queries.selectIdProduct(filaSeleccionada);

            frmEditProduct edit = new frmEditProduct();
            edit.dialogoEdit(id);
            edit.setVisible(true);
            this.dispose();  
        });
        
        btnEditPrice.addActionListener(e->{
            int id = queries.selectIdProduct(filaSeleccionada);
            
            frmEditPrice editPrice = new frmEditPrice();
            editPrice.dialogoEdit(id);
            editPrice.setVisible(true);
        });
        
        jtablePrducts.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent Mouse_evt){
                JTable tablaO =(JTable) Mouse_evt.getSource();
                Point point = Mouse_evt.getPoint();
                int row = tablaO.rowAtPoint(point);
                         
                if(Mouse_evt.getClickCount()==1){
                    filaSeleccionada = String.valueOf(jtablePrducts.getValueAt(jtablePrducts.getSelectedRow(), 0).toString()); 
                }
                else{
                }            
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jMenu1 = new javax.swing.JMenu();
        jPopupMenu3 = new javax.swing.JPopupMenu();
        jPopupMenu4 = new javax.swing.JPopupMenu();
        jPopupMenu5 = new javax.swing.JPopupMenu();
        jPopupMenu6 = new javax.swing.JPopupMenu();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btnMenu = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        btnSales = new javax.swing.JButton();
        btnServiceTec = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        txtCodProduct = new javax.swing.JTextField();
        btnSerchCode = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cboCategories = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cboSubcategories = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cboBrand = new javax.swing.JComboBox<>();
        btnSerchCode2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtableFinancy = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtablePrducts = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        btnNewProduct = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnEditPrice = new javax.swing.JButton();
        btnRerturnProduct = new javax.swing.JButton();
        btnAddStock = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuNewCategory = new javax.swing.JMenu();
        jMenuNewProduct = new javax.swing.JMenuItem();
        jMenuCategory = new javax.swing.JMenuItem();
        jMenuNewBrand = new javax.swing.JMenuItem();
        jMenuBrandEdit = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenu8 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenuNewSupplier = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenuAjusteDeStock = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión.");
        setUndecorated(true);

        jPanel2.setBackground(new java.awt.Color(0, 0, 0, 10));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jPanel5.setBackground(new java.awt.Color(0, 0, 0, 0));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Wallpapers/logo.jpg"))); // NOI18N
        jLabel5.setText("jLabel5");
        jLabel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(13, 155, 219)));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 109, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnMenu.setBackground(new java.awt.Color(255, 255, 255));
        btnMenu.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/3puntos32.png"))); // NOI18N
        btnMenu.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMenu.setFocusPainted(false);
        btnMenu.setHideActionText(true);
        btnMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMenuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMenuMouseExited(evt);
            }
        });
        btnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(255, 255, 255));
        jButton7.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/3puntos32.png"))); // NOI18N
        jButton7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.setEnabled(false);
        jButton7.setHideActionText(true);

        btnSales.setBackground(new java.awt.Color(255, 255, 255));
        btnSales.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnSales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/3puntos32.png"))); // NOI18N
        btnSales.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnSales.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSales.setHideActionText(true);
        btnSales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSalesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSalesMouseExited(evt);
            }
        });
        btnSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalesActionPerformed(evt);
            }
        });

        btnServiceTec.setBackground(new java.awt.Color(255, 255, 255));
        btnServiceTec.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnServiceTec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/3puntos32.png"))); // NOI18N
        btnServiceTec.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnServiceTec.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnServiceTec.setHideActionText(true);
        btnServiceTec.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnServiceTecMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnServiceTecMouseExited(evt);
            }
        });
        btnServiceTec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnServiceTecActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSales, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnServiceTec, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1193, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnSales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnMenu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnServiceTec, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setPreferredSize(new java.awt.Dimension(900, 957));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        txtCodProduct.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        txtCodProduct.setText("Ingrese codigo de producto...");
        txtCodProduct.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(101, 129, 171), 2, true));

        btnSerchCode.setBackground(new java.awt.Color(242, 242, 242));
        btnSerchCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Serch32.png"))); // NOI18N
        btnSerchCode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171), 2));
        btnSerchCode.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSerchCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSerchCodeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSerchCodeMouseExited(evt);
            }
        });
        btnSerchCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSerchCodeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtCodProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSerchCode, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSerchCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Raleway", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(101, 129, 171));
        jLabel1.setText("Categoría:");

        cboCategories.setBackground(new java.awt.Color(255, 255, 255));
        cboCategories.setFont(new java.awt.Font("Raleway", 1, 14)); // NOI18N
        cboCategories.setForeground(new java.awt.Color(102, 102, 102));
        cboCategories.setToolTipText("");
        cboCategories.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)));
        cboCategories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCategoriesActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Raleway", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(101, 129, 171));
        jLabel4.setText("Subcategoría:");

        cboSubcategories.setBackground(new java.awt.Color(255, 255, 255));
        cboSubcategories.setFont(new java.awt.Font("Raleway", 1, 14)); // NOI18N
        cboSubcategories.setForeground(new java.awt.Color(102, 102, 102));
        cboSubcategories.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)));
        cboSubcategories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSubcategoriesActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Raleway", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(101, 129, 171));
        jLabel2.setText("Marca:");

        cboBrand.setBackground(new java.awt.Color(255, 255, 255));
        cboBrand.setFont(new java.awt.Font("Raleway", 1, 14)); // NOI18N
        cboBrand.setForeground(new java.awt.Color(102, 102, 102));
        cboBrand.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)));

        btnSerchCode2.setBackground(new java.awt.Color(242, 242, 242));
        btnSerchCode2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Serch32.png"))); // NOI18N
        btnSerchCode2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171), 2));
        btnSerchCode2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSerchCode2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSerchCode2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSerchCode2MouseExited(evt);
            }
        });
        btnSerchCode2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSerchCode2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboCategories, 0, 197, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboSubcategories, 0, 197, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSerchCode2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(114, 114, 114))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(cboBrand, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboSubcategories, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboCategories, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSerchCode2, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(101, 129, 171));
        jLabel3.setText("Buscar");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(4, 4, 4))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jtableFinancy.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jtableFinancy.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(jtableFinancy);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jtablePrducts.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jtablePrducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jtablePrducts.setRowHeight(25);
        jtablePrducts.setSelectionBackground(new java.awt.Color(224, 242, 255));
        jScrollPane1.setViewportView(jtablePrducts);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1238, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        btnNewProduct.setBackground(new java.awt.Color(255,255,255));
        btnNewProduct.setForeground(new java.awt.Color(153, 255, 153));
        btnNewProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/3puntos32.png"))); // NOI18N
        btnNewProduct.setBorder(null);
        btnNewProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNewProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNewProductMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNewProductMouseExited(evt);
            }
        });
        btnNewProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewProductActionPerformed(evt);
            }
        });

        btnEdit.setBackground(new java.awt.Color(255, 255, 255));
        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/3puntos32.png"))); // NOI18N
        btnEdit.setBorder(null);
        btnEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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

        btnEditPrice.setBackground(new java.awt.Color(255, 255, 255));
        btnEditPrice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/3puntos32.png"))); // NOI18N
        btnEditPrice.setBorder(null);
        btnEditPrice.setBorderPainted(false);
        btnEditPrice.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditPrice.setFocusable(false);
        btnEditPrice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditPriceMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditPriceMouseExited(evt);
            }
        });
        btnEditPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditPriceActionPerformed(evt);
            }
        });

        btnRerturnProduct.setBackground(new java.awt.Color(255, 255, 255));
        btnRerturnProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/outside64.png"))); // NOI18N
        btnRerturnProduct.setBorder(null);
        btnRerturnProduct.setBorderPainted(false);
        btnRerturnProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRerturnProduct.setFocusable(false);
        btnRerturnProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRerturnProductMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRerturnProductMouseExited(evt);
            }
        });
        btnRerturnProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRerturnProductActionPerformed(evt);
            }
        });

        btnAddStock.setBackground(new java.awt.Color(255, 255, 255));
        btnAddStock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/3puntos32.png"))); // NOI18N
        btnAddStock.setBorder(null);
        btnAddStock.setBorderPainted(false);
        btnAddStock.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddStock.setFocusable(false);
        btnAddStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAddStockMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAddStockMouseExited(evt);
            }
        });
        btnAddStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddStockActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNewProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRerturnProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddStock, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNewProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEditPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddStock, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRerturnProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(415, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );

        btnExit.setBackground(new java.awt.Color(255, 255, 255));
        btnExit.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        btnExit.setForeground(new java.awt.Color(101, 129, 171));
        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit2_64.png"))); // NOI18N
        btnExit.setText("Salir");
        btnExit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnExitMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnExitMouseExited(evt);
            }
        });

        jMenuBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jMenuBar1.setForeground(new java.awt.Color(102, 102, 102));
        jMenuBar1.setFont(new java.awt.Font("Raleway", 0, 12)); // NOI18N

        jMenuNewCategory.setText("Productos");
        jMenuNewCategory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuNewCategoryMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jMenuNewCategoryMouseExited(evt);
            }
        });

        jMenuNewProduct.setText("Nuevo producto");
        jMenuNewProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuNewProductActionPerformed(evt);
            }
        });
        jMenuNewCategory.add(jMenuNewProduct);

        jMenuCategory.setText("Crear categoria");
        jMenuCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuCategoryActionPerformed(evt);
            }
        });
        jMenuNewCategory.add(jMenuCategory);

        jMenuNewBrand.setText("Crear marca");
        jMenuNewBrand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuNewBrandActionPerformed(evt);
            }
        });
        jMenuNewCategory.add(jMenuNewBrand);

        jMenuBrandEdit.setText("Editar marca");
        jMenuBrandEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuBrandEditActionPerformed(evt);
            }
        });
        jMenuNewCategory.add(jMenuBrandEdit);

        jMenuBar1.add(jMenuNewCategory);

        jMenu4.setText("Servicio técnico");
        jMenuBar1.add(jMenu4);

        jMenu8.setText("Ventas");
        jMenuBar1.add(jMenu8);

        jMenu5.setText("Proveedores");

        jMenuNewSupplier.setText("Crear/Editar proveedor");
        jMenuNewSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuNewSupplierActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuNewSupplier);

        jMenuBar1.add(jMenu5);

        jMenu3.setText("Clientes");
        jMenuBar1.add(jMenu3);

        jMenu6.setText("Administracion");

        jMenuAjusteDeStock.setText("Ajuste de Stock");
        jMenuAjusteDeStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuAjusteDeStockActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuAjusteDeStock);

        jMenuBar1.add(jMenu6);

        jMenu7.setText("Seguridad");
        jMenuBar1.add(jMenu7);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 2159, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExit)
                .addGap(117, 117, 117))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 780, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                .addComponent(btnExit)
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuAjusteDeStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuAjusteDeStockActionPerformed

    }//GEN-LAST:event_jMenuAjusteDeStockActionPerformed

    private void btnMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuActionPerformed
        
    }//GEN-LAST:event_btnMenuActionPerformed

    private void btnSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesActionPerformed
        
    }//GEN-LAST:event_btnSalesActionPerformed

    private void jMenuNewProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuNewProductActionPerformed
            
    }//GEN-LAST:event_jMenuNewProductActionPerformed

    private void btnServiceTecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnServiceTecActionPerformed
      
    }//GEN-LAST:event_btnServiceTecActionPerformed

    private void jMenuCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuCategoryActionPerformed
      
    }//GEN-LAST:event_jMenuCategoryActionPerformed

    private void jMenuNewBrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuNewBrandActionPerformed
   
    }//GEN-LAST:event_jMenuNewBrandActionPerformed

    private void jMenuNewSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuNewSupplierActionPerformed

    }//GEN-LAST:event_jMenuNewSupplierActionPerformed

    private void btnSerchCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSerchCodeActionPerformed

    }//GEN-LAST:event_btnSerchCodeActionPerformed

    private void cboCategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCategoriesActionPerformed
      
    }//GEN-LAST:event_cboCategoriesActionPerformed

    private void cboSubcategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSubcategoriesActionPerformed
        
    }//GEN-LAST:event_cboSubcategoriesActionPerformed

    private void btnNewProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewProductActionPerformed
        
    }//GEN-LAST:event_btnNewProductActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnEditPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditPriceActionPerformed
        
    }//GEN-LAST:event_btnEditPriceActionPerformed

    private void btnRerturnProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRerturnProductActionPerformed
    
    }//GEN-LAST:event_btnRerturnProductActionPerformed

    private void btnAddStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddStockActionPerformed
 
    }//GEN-LAST:event_btnAddStockActionPerformed

    private void btnSerchCodeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSerchCodeMouseEntered
        btnSerchCode.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnSerchCodeMouseEntered

    private void btnSerchCodeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSerchCodeMouseExited
        btnSerchCode.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnSerchCodeMouseExited

    private void btnMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuMouseEntered
        btnMenu.setBackground(new Color(13,155,219));
    }//GEN-LAST:event_btnMenuMouseEntered

    private void btnMenuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMenuMouseExited
        btnMenu.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnMenuMouseExited

    private void btnSalesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesMouseEntered
        btnSales.setBackground(new Color(13,155,219));
    }//GEN-LAST:event_btnSalesMouseEntered

    private void btnSalesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesMouseExited
        btnSales.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnSalesMouseExited

    private void btnSerchCode2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSerchCode2MouseEntered
        btnSerchCode2.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnSerchCode2MouseEntered

    private void btnSerchCode2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSerchCode2MouseExited
        btnSerchCode2.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnSerchCode2MouseExited

    private void btnSerchCode2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSerchCode2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSerchCode2ActionPerformed

    private void btnServiceTecMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnServiceTecMouseEntered
        btnServiceTec.setBackground(new Color(13,155,219));
    }//GEN-LAST:event_btnServiceTecMouseEntered

    private void btnServiceTecMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnServiceTecMouseExited
        btnServiceTec.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnServiceTecMouseExited

    private void jMenuNewCategoryMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuNewCategoryMouseEntered
        jMenuNewCategory.setBackground(new Color(13,155,219));
    }//GEN-LAST:event_jMenuNewCategoryMouseEntered

    private void jMenuNewCategoryMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuNewCategoryMouseExited
        jMenuNewCategory.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_jMenuNewCategoryMouseExited

    private void btnNewProductMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewProductMouseEntered
        btnNewProduct.setBackground(new Color(153,217,234));
    }//GEN-LAST:event_btnNewProductMouseEntered

    private void btnNewProductMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewProductMouseExited
        btnNewProduct.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnNewProductMouseExited

    private void btnEditMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseEntered
        btnEdit.setBackground(new Color(153,217,234));
    }//GEN-LAST:event_btnEditMouseEntered

    private void btnEditMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseExited
        btnEdit.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnEditMouseExited

    private void btnEditPriceMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditPriceMouseEntered
        btnEditPrice.setBackground(new Color(153,217,234));
    }//GEN-LAST:event_btnEditPriceMouseEntered

    private void btnEditPriceMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditPriceMouseExited
        btnEditPrice.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnEditPriceMouseExited

    private void btnAddStockMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddStockMouseEntered
        btnAddStock.setBackground(new Color(153,217,234));
    }//GEN-LAST:event_btnAddStockMouseEntered

    private void btnAddStockMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddStockMouseExited
        btnAddStock.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnAddStockMouseExited

    private void btnRerturnProductMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRerturnProductMouseEntered
        btnRerturnProduct.setBackground(new Color(153,217,234));
    }//GEN-LAST:event_btnRerturnProductMouseEntered

    private void btnRerturnProductMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRerturnProductMouseExited
        btnRerturnProduct.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnRerturnProductMouseExited

    private void btnExitMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseEntered
        btnExit.setBackground(new Color(238,238,238));
    }//GEN-LAST:event_btnExitMouseEntered

    private void btnExitMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseExited
        btnExit.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnExitMouseExited

    private void jMenuBrandEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuBrandEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuBrandEditActionPerformed

    public static void main(String args[]) { 
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmProducts().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddStock;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnEditPrice;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnNewProduct;
    private javax.swing.JButton btnRerturnProduct;
    private javax.swing.JButton btnSales;
    private javax.swing.JButton btnSerchCode;
    private javax.swing.JButton btnSerchCode2;
    private javax.swing.JButton btnServiceTec;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboBrand;
    private javax.swing.JComboBox<String> cboCategories;
    private javax.swing.JComboBox<String> cboSubcategories;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenuItem jMenuAjusteDeStock;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuBrandEdit;
    private javax.swing.JMenuItem jMenuCategory;
    private javax.swing.JMenuItem jMenuNewBrand;
    private javax.swing.JMenu jMenuNewCategory;
    private javax.swing.JMenuItem jMenuNewProduct;
    private javax.swing.JMenuItem jMenuNewSupplier;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JPopupMenu jPopupMenu3;
    private javax.swing.JPopupMenu jPopupMenu4;
    private javax.swing.JPopupMenu jPopupMenu5;
    private javax.swing.JPopupMenu jPopupMenu6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jtableFinancy;
    private javax.swing.JTable jtablePrducts;
    private javax.swing.JTextField txtCodProduct;
    // End of variables declaration//GEN-END:variables
}
