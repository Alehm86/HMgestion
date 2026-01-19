/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package forms;


import Old.frmProducts;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;


public class frmHome extends javax.swing.JFrame {
    
    fondoPanel background = new fondoPanel();
    Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
    
    public frmHome() {
        
        this.setContentPane(background); 
        initComponents();
        this.setExtendedState(6);
        FechaHora.iniciarFecha(jLabelFecha);
        FechaHora.iniciarReloj(jLabelHora);
        menuSuperior();
        paginaDeInicio();
        
        
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
    
    public void paginaDeInicio(){
        PageDashboard pDashboard = new PageDashboard();
        pDashboard.setSize(1700, 877);
        pDashboard.setLocation(0, 0);
        
        jPanelContent.removeAll();
        jPanelContent.add(pDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1712, 877));
        jPanelContent.revalidate();
        jPanelContent.repaint();
    }
    
    
    public class FechaHora {

        public static void iniciarFecha(JLabel label) {

            DateTimeFormatter formato = DateTimeFormatter.ofPattern(
                    "EEEE d 'de' MMMM",
                    new Locale("es", "ES")
            );

            Timer timer = new Timer(1000, e -> {
                LocalDateTime ahora = LocalDateTime.now();
                label.setText(ahora.format(formato));
            });

            timer.start();
        }
        public static void iniciarReloj(JLabel label) {

            DateTimeFormatter formato =
                    DateTimeFormatter.ofPattern("HH:mm:ss");

            Timer timer = new Timer(1000, e -> {
                LocalDateTime ahora = LocalDateTime.now();
                label.setText(ahora.format(formato));
            });

            timer.start();
        }
    }
    
    private void actionButtons(){
        
        btnProducts.addActionListener(e -> {
            frmProducts products = new frmProducts();
            products.setVisible(true);
            this.dispose();
        });
        
        btnExit.addActionListener(e -> {  
            
            int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Desea salir de la aplicación?",
                "",
                JOptionPane.YES_NO_OPTION
            );                   
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }
            
            this.dispose();
        });
        
        btnSales.addActionListener(e -> {
            frmSalesManagement sales = new frmSalesManagement();
            sales.setVisible(true);
            this.dispose();
        });
        
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

        jMenuNewCat.addActionListener(e -> {
//            frmCategoriesAndSubcategoriesMgmt dialogo = new frmCategoriesAndSubcategoriesMgmt(this, true);
//            dialogo.setVisible(true);
        });

        jMenuNewSupplier.addActionListener(e -> {
            frmSupplierNew fNsupplier = new frmSupplierNew(this, true);
            fNsupplier.setVisible(true);
        });
        
        jMenuIEditSupplier.addActionListener(e -> {
            frmSupplierEdit fEsupplier = new frmSupplierEdit(this, true);
            fEsupplier.setVisible(true);
        });

        jMenuNewProduct.addActionListener(e -> {
            frmProductNewDialog newProduct = new frmProductNewDialog(parent, true);
            newProduct.setLocationRelativeTo(parent);
            newProduct.setVisible(true);

        });

        jMenuAjusteDeStock.addActionListener(e -> {
            frmStockAdjustment stockAdj = new frmStockAdjustment();
            stockAdj.setVisible(true);
        });
        
        jMenuNewSubcat.addActionListener(e -> {
            frmSubategoriesNew fNSubcat = new frmSubategoriesNew(this, true);
            fNSubcat.setVisible(true);
        });
        
        jMenuEditSubcat.addActionListener(e -> {
            frmSubcategoriesEdit fESubcat = new frmSubcategoriesEdit(this, true);
            fESubcat.setVisible(true);
        });
        
        jMenuNewCat.addActionListener(e -> {
            frmCategoriesNew fNcat = new frmCategoriesNew(this, true);
            fNcat.setVisible(true);
        });
         
        jMenuEditCat.addActionListener(e -> {
            frmCategoriesEdit fEcat = new frmCategoriesEdit(this, true);
            fEcat.setVisible(true);
        });

    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnProducts = new javax.swing.JButton();
        btnSales = new javax.swing.JButton();
        btnCustomers = new javax.swing.JButton();
        btnCtaCte = new javax.swing.JButton();
        btnServiceTec = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabelUser = new javax.swing.JLabel();
        jLabelTitleDolar = new javax.swing.JLabel();
        jLabelValorDolar = new javax.swing.JLabel();
        jLabelLineaBlanca = new javax.swing.JLabel();
        btnExit = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabelHora = new javax.swing.JLabel();
        jLabelFecha = new javax.swing.JLabel();
        jPanelContent = new javax.swing.JPanel();
        jLabelTitle = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuNewCategory = new javax.swing.JMenu();
        jMenuNewProduct = new javax.swing.JMenuItem();
        jMenuCategorias = new javax.swing.JMenu();
        jMenuNewCat = new javax.swing.JMenuItem();
        jMenuEditCat = new javax.swing.JMenuItem();
        jMenuNewSubcat = new javax.swing.JMenuItem();
        jMenuEditSubcat = new javax.swing.JMenuItem();
        jMenuBrands = new javax.swing.JMenu();
        jMenuBrandEdit = new javax.swing.JMenuItem();
        jMenuNewBrand = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenu8 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenuNewSupplier = new javax.swing.JMenuItem();
        jMenuIEditSupplier = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenuAjusteDeStock = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(12, 83, 151));

        btnProducts.setBackground(new java.awt.Color(101, 129, 171));
        btnProducts.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        btnProducts.setForeground(new java.awt.Color(240, 240, 242));
        btnProducts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ecommerce_online_store_shop_market_icon_228274.png"))); // NOI18N
        btnProducts.setText("Productos");
        btnProducts.setBorder(null);
        btnProducts.setBorderPainted(false);
        btnProducts.setContentAreaFilled(false);
        btnProducts.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProducts.setFocusPainted(false);
        btnProducts.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnProducts.setMaximumSize(new java.awt.Dimension(103, 37));
        btnProducts.setMinimumSize(new java.awt.Dimension(103, 37));
        btnProducts.setPreferredSize(new java.awt.Dimension(103, 37));
        btnProducts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnProductsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnProductsMouseExited(evt);
            }
        });
        btnProducts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductsActionPerformed(evt);
            }
        });

        btnSales.setBackground(new java.awt.Color(101, 129, 171));
        btnSales.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        btnSales.setForeground(new java.awt.Color(240, 240, 242));
        btnSales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/shop_buy_add_cart_market_icon_228271.png"))); // NOI18N
        btnSales.setText("Ventas");
        btnSales.setBorder(null);
        btnSales.setBorderPainted(false);
        btnSales.setContentAreaFilled(false);
        btnSales.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSales.setFocusPainted(false);
        btnSales.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSales.setMaximumSize(new java.awt.Dimension(103, 37));
        btnSales.setMinimumSize(new java.awt.Dimension(103, 37));
        btnSales.setPreferredSize(new java.awt.Dimension(103, 37));
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

        btnCustomers.setBackground(new java.awt.Color(101, 129, 171));
        btnCustomers.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        btnCustomers.setForeground(new java.awt.Color(240, 240, 242));
        btnCustomers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/profile_account_user_icon_228272.png"))); // NOI18N
        btnCustomers.setText("Clientes");
        btnCustomers.setBorder(null);
        btnCustomers.setBorderPainted(false);
        btnCustomers.setContentAreaFilled(false);
        btnCustomers.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCustomers.setFocusPainted(false);
        btnCustomers.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCustomers.setPreferredSize(new java.awt.Dimension(103, 37));
        btnCustomers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCustomersMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCustomersMouseExited(evt);
            }
        });
        btnCustomers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomersActionPerformed(evt);
            }
        });

        btnCtaCte.setBackground(new java.awt.Color(101, 129, 171));
        btnCtaCte.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        btnCtaCte.setForeground(new java.awt.Color(240, 240, 242));
        btnCtaCte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/market_payment_cashier_icon_228280.png"))); // NOI18N
        btnCtaCte.setText("Cta. Cte.");
        btnCtaCte.setBorder(null);
        btnCtaCte.setBorderPainted(false);
        btnCtaCte.setContentAreaFilled(false);
        btnCtaCte.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCtaCte.setFocusPainted(false);
        btnCtaCte.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCtaCte.setMaximumSize(new java.awt.Dimension(103, 37));
        btnCtaCte.setMinimumSize(new java.awt.Dimension(103, 37));
        btnCtaCte.setPreferredSize(new java.awt.Dimension(103, 37));
        btnCtaCte.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCtaCteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCtaCteMouseExited(evt);
            }
        });
        btnCtaCte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCtaCteActionPerformed(evt);
            }
        });

        btnServiceTec.setBackground(new java.awt.Color(101, 129, 171));
        btnServiceTec.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        btnServiceTec.setForeground(new java.awt.Color(240, 240, 242));
        btnServiceTec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/sale_flash_icon_228290(1).png"))); // NOI18N
        btnServiceTec.setText("Servicio técnico");
        btnServiceTec.setBorder(null);
        btnServiceTec.setBorderPainted(false);
        btnServiceTec.setContentAreaFilled(false);
        btnServiceTec.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnServiceTec.setFocusPainted(false);
        btnServiceTec.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnServiceTec.setMaximumSize(new java.awt.Dimension(103, 37));
        btnServiceTec.setMinimumSize(new java.awt.Dimension(103, 37));
        btnServiceTec.setPreferredSize(new java.awt.Dimension(103, 37));
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

        jPanel4.setBackground(new java.awt.Color(12, 83, 151));

        jLabelUser.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabelUser.setForeground(new java.awt.Color(240, 240, 242));
        jLabelUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/User.png"))); // NOI18N
        jLabelUser.setText("Usuario");

        jLabelTitleDolar.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabelTitleDolar.setForeground(new java.awt.Color(240, 240, 242));
        jLabelTitleDolar.setText("Dolar:");

        jLabelValorDolar.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabelValorDolar.setForeground(new java.awt.Color(240, 240, 242));
        jLabelValorDolar.setText("$1500");

        jLabelLineaBlanca.setBackground(new java.awt.Color(255, 255, 255));
        jLabelLineaBlanca.setForeground(new java.awt.Color(240, 240, 242));
        jLabelLineaBlanca.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 242)));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabelTitleDolar, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelValorDolar, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                        .addGap(26, 26, 26))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabelUser)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelLineaBlanca, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTitleDolar)
                    .addComponent(jLabelValorDolar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addComponent(jLabelLineaBlanca))
        );

        btnExit.setBackground(new java.awt.Color(255, 255, 255));
        btnExit.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        btnExit.setForeground(new java.awt.Color(101, 129, 171));
        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/salir32.png"))); // NOI18N
        btnExit.setText("Salir");
        btnExit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExit.setFocusPainted(false);
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnServiceTec, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnProducts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCustomers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCtaCte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67)
                .addComponent(btnProducts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCustomers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCtaCte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnServiceTec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 554, Short.MAX_VALUE)
                .addComponent(btnExit)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(12, 83, 151));

        jLabelHora.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabelHora.setForeground(new java.awt.Color(240, 240, 242));
        jLabelHora.setText("Hora");

        jLabelFecha.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabelFecha.setForeground(new java.awt.Color(240, 240, 242));
        jLabelFecha.setText("Fecha");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelFecha)
                    .addComponent(jLabelHora))
                .addGap(14, 14, 14))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelFecha)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelHora)
                .addContainerGap())
        );

        jPanelContent.setBackground(new java.awt.Color(255, 255, 255));
        jPanelContent.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelTitle.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        jLabelTitle.setForeground(new java.awt.Color(105, 127, 155));
        jLabelTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/OnOff(1).png"))); // NOI18N
        jLabelTitle.setText("HmGestión");

        jMenuBar1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jMenuBar1.setForeground(new java.awt.Color(12, 83, 151));
        jMenuBar1.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jMenuBar1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N

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

        jMenuCategorias.setText("Gestionar categorías");

        jMenuNewCat.setText("Crear Categoría");
        jMenuNewCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuNewCatActionPerformed(evt);
            }
        });
        jMenuCategorias.add(jMenuNewCat);

        jMenuEditCat.setText("Editar Categoría");
        jMenuCategorias.add(jMenuEditCat);

        jMenuNewSubcat.setText("Crear Subcategoría");
        jMenuCategorias.add(jMenuNewSubcat);

        jMenuEditSubcat.setText("Editar Subcategoría");
        jMenuCategorias.add(jMenuEditSubcat);

        jMenuNewCategory.add(jMenuCategorias);

        jMenuBrands.setText("Gestionar marcas");

        jMenuBrandEdit.setText("Editar marca");
        jMenuBrandEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuBrandEditActionPerformed(evt);
            }
        });
        jMenuBrands.add(jMenuBrandEdit);

        jMenuNewBrand.setText("Crear marca");
        jMenuNewBrand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuNewBrandActionPerformed(evt);
            }
        });
        jMenuBrands.add(jMenuNewBrand);

        jMenuNewCategory.add(jMenuBrands);

        jMenuBar1.add(jMenuNewCategory);

        jMenu4.setText("Servicio técnico");
        jMenuBar1.add(jMenu4);

        jMenu8.setText("Ventas");
        jMenuBar1.add(jMenu8);

        jMenu5.setText("Proveedores");

        jMenuNewSupplier.setText("Crear proveedor");
        jMenuNewSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuNewSupplierActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuNewSupplier);

        jMenuIEditSupplier.setText("Editar proveedor");
        jMenu5.add(jMenuIEditSupplier);

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(183, 183, 183)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTitle, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelContent, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1256, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnProductsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductsMouseEntered
        btnProducts.setForeground(new Color(215,215,0));
    }//GEN-LAST:event_btnProductsMouseEntered

    private void btnProductsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductsMouseExited
        btnProducts.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnProductsMouseExited

    private void btnProductsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductsActionPerformed
        PageProductsHome p1 = new PageProductsHome();
        p1.setSize(1700, 877);
        p1.setLocation(0, 0);
        
        jPanelContent.removeAll();
        jPanelContent.add(p1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1712, 877));
        jPanelContent.revalidate();
        jPanelContent.repaint();
                
    }//GEN-LAST:event_btnProductsActionPerformed

    private void btnSalesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesMouseEntered
        btnSales.setForeground(new Color(215,215,0));
    }//GEN-LAST:event_btnSalesMouseEntered

    private void btnSalesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesMouseExited
        btnSales.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnSalesMouseExited

    private void btnSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesActionPerformed
        
    }//GEN-LAST:event_btnSalesActionPerformed

    private void btnCustomersMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCustomersMouseEntered
        btnCustomers.setForeground(new Color(215,215,0));
    }//GEN-LAST:event_btnCustomersMouseEntered

    private void btnCustomersMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCustomersMouseExited
        btnCustomers.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnCustomersMouseExited

    private void btnCustomersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomersActionPerformed

    }//GEN-LAST:event_btnCustomersActionPerformed

    private void btnCtaCteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCtaCteMouseEntered
        btnCtaCte.setForeground(new Color(215,215,0));
    }//GEN-LAST:event_btnCtaCteMouseEntered

    private void btnCtaCteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCtaCteMouseExited
        btnCtaCte.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnCtaCteMouseExited

    private void btnCtaCteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCtaCteActionPerformed

    }//GEN-LAST:event_btnCtaCteActionPerformed

    private void btnServiceTecMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnServiceTecMouseEntered
        btnServiceTec.setForeground(new Color(215,215,0));
    }//GEN-LAST:event_btnServiceTecMouseEntered

    private void btnServiceTecMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnServiceTecMouseExited
        btnServiceTec.setForeground(new Color(255,255,255));
    }//GEN-LAST:event_btnServiceTecMouseExited

    private void btnServiceTecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnServiceTecActionPerformed

    }//GEN-LAST:event_btnServiceTecActionPerformed

    private void btnExitMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseEntered
        btnExit.setBackground(new Color(238,238,238));
    }//GEN-LAST:event_btnExitMouseEntered

    private void btnExitMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseExited
        btnExit.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnExitMouseExited

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnExitActionPerformed

    private void jMenuAjusteDeStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuAjusteDeStockActionPerformed

    }//GEN-LAST:event_jMenuAjusteDeStockActionPerformed

    private void jMenuNewSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuNewSupplierActionPerformed

    }//GEN-LAST:event_jMenuNewSupplierActionPerformed

    private void jMenuNewCategoryMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuNewCategoryMouseExited
        jMenuNewCategory.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_jMenuNewCategoryMouseExited

    private void jMenuNewCategoryMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuNewCategoryMouseEntered
        jMenuNewCategory.setBackground(new Color(13,155,219));
    }//GEN-LAST:event_jMenuNewCategoryMouseEntered

    private void jMenuBrandEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuBrandEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuBrandEditActionPerformed

    private void jMenuNewBrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuNewBrandActionPerformed

    }//GEN-LAST:event_jMenuNewBrandActionPerformed

    private void jMenuNewCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuNewCatActionPerformed

    }//GEN-LAST:event_jMenuNewCatActionPerformed

    private void jMenuNewProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuNewProductActionPerformed

    }//GEN-LAST:event_jMenuNewProductActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmHome().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCtaCte;
    private javax.swing.JButton btnCustomers;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnProducts;
    private javax.swing.JButton btnSales;
    private javax.swing.JButton btnServiceTec;
    private javax.swing.JLabel jLabelFecha;
    private javax.swing.JLabel jLabelHora;
    private javax.swing.JLabel jLabelLineaBlanca;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JLabel jLabelTitleDolar;
    private javax.swing.JLabel jLabelUser;
    private javax.swing.JLabel jLabelValorDolar;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenuItem jMenuAjusteDeStock;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuBrandEdit;
    private javax.swing.JMenu jMenuBrands;
    private javax.swing.JMenu jMenuCategorias;
    private javax.swing.JMenuItem jMenuEditCat;
    private javax.swing.JMenuItem jMenuEditSubcat;
    private javax.swing.JMenuItem jMenuIEditSupplier;
    private javax.swing.JMenuItem jMenuNewBrand;
    private javax.swing.JMenuItem jMenuNewCat;
    private javax.swing.JMenu jMenuNewCategory;
    private javax.swing.JMenuItem jMenuNewProduct;
    private javax.swing.JMenuItem jMenuNewSubcat;
    private javax.swing.JMenuItem jMenuNewSupplier;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelContent;
    // End of variables declaration//GEN-END:variables
}
