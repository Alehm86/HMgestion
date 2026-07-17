/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views;

import dao.budgetDAO;
import dao.customerDAO;
import dao.userDAO;
import java.awt.Color;
import java.awt.Frame;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import models.mUser;
import session.session;
import utils.configMensajes;

public class homeFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(homeFrame.class.getName());

    Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
    
    configMensajes config = new configMensajes();
    customerDAO qCustomer = new customerDAO();
    budgetDAO qBudget = new budgetDAO();
    userDAO qUser = new userDAO();
    
    public homeFrame() {
        initComponents();
        
        loadUser();
        
        lbl_user.setText(session.getCurrentUser().getUsername());
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        this.setExtendedState(6);
        homeFrame.FechaHora.iniciarFecha(jLabelFecha);
        homeFrame.FechaHora.iniciarReloj(jLabelHora);
        menuSuperior();
        paginaDeInicio();
        actionButtons();
        
        qBudget.actualizarPresupuestosVencidos();

        
    }
    
    private void loadUser(){

        mUser user = qUser.getUserById(1);

        session.setCurrentUser(user);
    }

    public void paginaDeInicio(){
        dashboard();
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
    
    public void dashboard(){
        dashboardPanel pDashboard = new dashboardPanel();
        pDashboard.setSize(1700, 877);
        pDashboard.setLocation(0, 0);
        
        jPanelContent.removeAll();
        jPanelContent.add(pDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1712, 877));
        jPanelContent.revalidate();
        jPanelContent.repaint();
    }
    
    private void actionButtons(){
        
        btnHome.addActionListener(e->{
            dashboard();
        });
        
        btnProducts.addActionListener(e -> {
            productDashboardPanel pProduct = new productDashboardPanel();
            pProduct.setSize(1700, 877);
            pProduct.setLocation(0, 0);

            jPanelContent.removeAll();
            jPanelContent.add(pProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1712, 877));
            jPanelContent.revalidate();
            jPanelContent.repaint();
        });
        
        btnClient.addActionListener(e -> {
            customerListPanel pClient = new customerListPanel();
            pClient.setSize(1700, 877);
            pClient.setLocation(0, 0);

            jPanelContent.removeAll();
            jPanelContent.add(pClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1712, 877));
            jPanelContent.revalidate();
            jPanelContent.repaint();
        });
        
        btnServiceTec.addActionListener(e -> {
            serviceDashboard pService = new serviceDashboard();
            pService.setSize(1700, 877);
            pService.setLocation(0, 0);

            jPanelContent.removeAll();
            jPanelContent.add(pService, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1712, 877));
            jPanelContent.revalidate();
            jPanelContent.repaint();
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
        
        btnCashRegister.addActionListener(e -> {
            cashRegisterDashboard pCashR = new cashRegisterDashboard();
            pCashR.setSize(1700, 877);
            pCashR.setLocation(0, 0);

            jPanelContent.removeAll();
            jPanelContent.add(pCashR, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1712, 877));
            jPanelContent.revalidate();
            jPanelContent.repaint();
        });
        
        btnCtaCte.addActionListener(e -> {
            currentAccountDashboard pCuAcc = new currentAccountDashboard();
            pCuAcc.setSize(1700, 877);
            pCuAcc.setLocation(0, 0);

            jPanelContent.removeAll();
            jPanelContent.add(pCuAcc, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1712, 877));
            jPanelContent.revalidate();
            jPanelContent.repaint();
        });
        
    }
    
    private void menuSuperior(){

        jMenuNewBrand.addActionListener(e -> {
            productBrandNewDialog fBrandNew = new productBrandNewDialog(this, true);
            fBrandNew.setVisible(true);
        });
        
        jMenuBrandEdit.addActionListener(e -> {
            productBrandEditDialog fBrandEdit = new productBrandEditDialog(this, true);
            fBrandEdit.setVisible(true);
        });

        jMenuNewSupplier.addActionListener(e -> {
            supplierNewDialog fNsupplier = new supplierNewDialog(this, true);
            fNsupplier.setVisible(true);
        });
        
        jMenuIEditSupplier.addActionListener(e -> {
            supplierEditDialog fEsupplier = new supplierEditDialog(this, true);
            fEsupplier.setVisible(true);
        });

        jMenuNewProduct.addActionListener(e -> {
            productNewDialog newProduct = new productNewDialog(parent, true);
            newProduct.setLocationRelativeTo(parent);
            newProduct.setVisible(true);

        });

        jMenuAjusteDeStock.addActionListener(e -> {
            productStockAdjustmentFrame stockAdj = new productStockAdjustmentFrame();
            stockAdj.setVisible(true);
        });
        
        jMenuNewSubcat.addActionListener(e -> {
            productSubategoriesNewDialog fNSubcat = new productSubategoriesNewDialog(this, true);
            fNSubcat.setVisible(true);
        });
        
        jMenuEditSubcat.addActionListener(e -> {
            productSubcategoriesEditDialog fESubcat = new productSubcategoriesEditDialog(this, true);
            fESubcat.setVisible(true);
        });
        
        jMenuNewCat.addActionListener(e -> {
            productCategoriesNewDialog fNcat = new productCategoriesNewDialog(this, true);
            fNcat.setVisible(true);
        });
         
        jMenuEditCat.addActionListener(e -> {
            productCategoriesEditDialog fEcat = new productCategoriesEditDialog(this, true);
            fEcat.setVisible(true);
        });
        
        jMenuIPrice.addActionListener(e -> {
            productPriceEditDialog fPrice = new productPriceEditDialog(this, true);
            fPrice.setVisible(true);
        });
        
        jMenuICustomerInsert.addActionListener(e -> {
            customerNewDialog fCustNew = new customerNewDialog(this, true);
            fCustNew.setVisible(true);
        });
        
        jMenuNewPresupuesto.addActionListener(e -> {
            budgetDialog fPresupuesto = new budgetDialog(null, true);
            fPresupuesto.setLocationRelativeTo(null);
            fPresupuesto.setVisible(true);
        });
        
        jMenuListPresupuesto.addActionListener(e -> {
            budgetListDialog fListBudget = new budgetListDialog(null, true, 0);
            fListBudget.setLocationRelativeTo(null);
            fListBudget.setVisible(true);
        });
        
        jMenuNewService.addActionListener(e -> {
            serviceNewForm fNewServiceOrder = new serviceNewForm();
            fNewServiceOrder.setLocationRelativeTo(null);
            fNewServiceOrder.setVisible(true);
        });
        
        jMenuItemViewCustomer.addActionListener(e->{
                   
            String CUIT = JOptionPane.showInputDialog(null, "Ingrese el número de cliente:");

            if (!CUIT.trim().isEmpty()) {
                
                customerViewDialog fView = new customerViewDialog(homeFrame.this, true);
                fView.dialogoEdit(CUIT);
                fView.setVisible(true);
                
            } else {
                System.out.println("No ingresó nada o canceló");
            }
        });
        
        jMenuItemAddPurchaseInvoice.addActionListener(e -> {
            purchaseInvoiceDialog fPurchase = new purchaseInvoiceDialog(parent, true);
            fPurchase.setLocationRelativeTo(null);
            fPurchase.setVisible(true);
        });
        
        jMenuItemProductSN.addActionListener(e -> {
            productAddSerialNumberDialog fProductSN = new productAddSerialNumberDialog(parent, true);
            fProductSN.setLocationRelativeTo(null);
            fProductSN.setVisible(true);
        });
        
        jMenuItemViewProductSN.addActionListener(e -> {
            productSerialNumberHistoryForm fViewProductSN = new productSerialNumberHistoryForm();
            fViewProductSN.setLocationRelativeTo(null);
            fViewProductSN.setVisible(true);
        });

    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btnProducts = new javax.swing.JButton();
        btnCashRegister = new javax.swing.JButton();
        btnClient = new javax.swing.JButton();
        btnCtaCte = new javax.swing.JButton();
        btnServiceTec = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        lbl_user = new javax.swing.JLabel();
        jLabelLineaBlanca = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnHome = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabelHora = new javax.swing.JLabel();
        jLabelFecha = new javax.swing.JLabel();
        jLabelTitle = new javax.swing.JLabel();
        jPanelContent = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuViewProductSN = new javax.swing.JMenu();
        jMenuNewProduct = new javax.swing.JMenuItem();
        jMenuCategorias = new javax.swing.JMenu();
        jMenuNewCat = new javax.swing.JMenuItem();
        jMenuEditCat = new javax.swing.JMenuItem();
        jMenuNewSubcat = new javax.swing.JMenuItem();
        jMenuEditSubcat = new javax.swing.JMenuItem();
        jMenuBrands = new javax.swing.JMenu();
        jMenuBrandEdit = new javax.swing.JMenuItem();
        jMenuNewBrand = new javax.swing.JMenuItem();
        jMenuIPrice = new javax.swing.JMenuItem();
        jMenuItemProductSN = new javax.swing.JMenuItem();
        jMenuItemViewProductSN = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuNewService = new javax.swing.JMenuItem();
        jMenuPresupuesto = new javax.swing.JMenu();
        jMenuNewPresupuesto = new javax.swing.JMenuItem();
        jMenuListPresupuesto = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemAddPurchaseInvoice = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuNewSupplier = new javax.swing.JMenuItem();
        jMenuIEditSupplier = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuICustomerInsert = new javax.swing.JMenuItem();
        jMenuItemViewCustomer = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenuAjusteDeStock = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(35, 35, 38));

        btnProducts.setBackground(new java.awt.Color(101, 129, 171));
        btnProducts.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnProducts.setForeground(new java.awt.Color(210, 215, 223));
        btnProducts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/homeProduct32.png"))); // NOI18N
        btnProducts.setText("Productos");
        btnProducts.setBorder(null);
        btnProducts.setBorderPainted(false);
        btnProducts.setContentAreaFilled(false);
        btnProducts.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
        btnProducts.addActionListener(this::btnProductsActionPerformed);

        btnCashRegister.setBackground(new java.awt.Color(101, 129, 171));
        btnCashRegister.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnCashRegister.setForeground(new java.awt.Color(210, 215, 223));
        btnCashRegister.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/homeSales32.png"))); // NOI18N
        btnCashRegister.setText("Ventas");
        btnCashRegister.setBorder(null);
        btnCashRegister.setBorderPainted(false);
        btnCashRegister.setContentAreaFilled(false);
        btnCashRegister.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnCashRegister.setFocusPainted(false);
        btnCashRegister.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCashRegister.setMaximumSize(new java.awt.Dimension(103, 37));
        btnCashRegister.setMinimumSize(new java.awt.Dimension(103, 37));
        btnCashRegister.setPreferredSize(new java.awt.Dimension(103, 37));
        btnCashRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCashRegisterMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCashRegisterMouseExited(evt);
            }
        });
        btnCashRegister.addActionListener(this::btnCashRegisterActionPerformed);

        btnClient.setBackground(new java.awt.Color(101, 129, 171));
        btnClient.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnClient.setForeground(new java.awt.Color(210, 215, 223));
        btnClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/clientes32.png"))); // NOI18N
        btnClient.setText("Clientes");
        btnClient.setBorder(null);
        btnClient.setBorderPainted(false);
        btnClient.setContentAreaFilled(false);
        btnClient.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnClient.setFocusPainted(false);
        btnClient.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnClient.setPreferredSize(new java.awt.Dimension(103, 37));
        btnClient.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnClientMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnClientMouseExited(evt);
            }
        });
        btnClient.addActionListener(this::btnClientActionPerformed);

        btnCtaCte.setBackground(new java.awt.Color(101, 129, 171));
        btnCtaCte.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnCtaCte.setForeground(new java.awt.Color(210, 215, 223));
        btnCtaCte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/iconDashboard32-CtaCte.png"))); // NOI18N
        btnCtaCte.setText("Cta. Cte.");
        btnCtaCte.setBorder(null);
        btnCtaCte.setBorderPainted(false);
        btnCtaCte.setContentAreaFilled(false);
        btnCtaCte.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
        btnCtaCte.addActionListener(this::btnCtaCteActionPerformed);

        btnServiceTec.setBackground(new java.awt.Color(101, 129, 171));
        btnServiceTec.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnServiceTec.setForeground(new java.awt.Color(210, 215, 223));
        btnServiceTec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/homeService32.png"))); // NOI18N
        btnServiceTec.setText("Servicio");
        btnServiceTec.setBorder(null);
        btnServiceTec.setBorderPainted(false);
        btnServiceTec.setContentAreaFilled(false);
        btnServiceTec.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
        btnServiceTec.addActionListener(this::btnServiceTecActionPerformed);

        jPanel4.setBackground(new java.awt.Color(35, 35, 38));

        lbl_user.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lbl_user.setForeground(new java.awt.Color(210, 215, 223));
        lbl_user.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_user.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/user48.png"))); // NOI18N
        lbl_user.setText("Ale");
        lbl_user.setMaximumSize(new java.awt.Dimension(176, 48));
        lbl_user.setMinimumSize(new java.awt.Dimension(176, 48));

        jLabelLineaBlanca.setBackground(new java.awt.Color(255, 255, 255));
        jLabelLineaBlanca.setForeground(new java.awt.Color(240, 240, 242));
        jLabelLineaBlanca.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 242)));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelLineaBlanca, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                    .addComponent(lbl_user, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                .addComponent(jLabelLineaBlanca))
        );

        jPanel3.setBackground(new java.awt.Color(35, 35, 38));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 188, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 97, Short.MAX_VALUE)
        );

        btnHome.setBackground(new java.awt.Color(101, 129, 171));
        btnHome.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnHome.setForeground(new java.awt.Color(210, 215, 223));
        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/homeHome32.png"))); // NOI18N
        btnHome.setText("Home");
        btnHome.setBorder(null);
        btnHome.setBorderPainted(false);
        btnHome.setContentAreaFilled(false);
        btnHome.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnHome.setFocusPainted(false);
        btnHome.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnHome.setMaximumSize(new java.awt.Dimension(103, 37));
        btnHome.setMinimumSize(new java.awt.Dimension(103, 37));
        btnHome.setPreferredSize(new java.awt.Dimension(103, 37));
        btnHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnHomeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnHomeMouseExited(evt);
            }
        });
        btnHome.addActionListener(this::btnHomeActionPerformed);

        btnExit.setBackground(new java.awt.Color(35, 35, 38));
        btnExit.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        btnExit.setForeground(new java.awt.Color(255, 255, 255));
        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit-32.png"))); // NOI18N
        btnExit.setText("Salir");
        btnExit.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnExit.setBorderPainted(false);
        btnExit.setContentAreaFilled(false);
        btnExit.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnExit.setFocusPainted(false);
        btnExit.setFocusable(false);
        btnExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnExitMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnExitMouseExited(evt);
            }
        });
        btnExit.addActionListener(this::btnExitActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(6, 6, 6))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(btnCtaCte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnCashRegister, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnProducts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnServiceTec, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnHome, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(btnExit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnProducts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(btnCashRegister, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(btnClient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(btnCtaCte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(btnServiceTec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 450, Short.MAX_VALUE)
                .addComponent(btnExit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(35, 35, 38));

        jLabelHora.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabelHora.setForeground(new java.awt.Color(210, 215, 223));
        jLabelHora.setText("Hora");

        jLabelFecha.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabelFecha.setForeground(new java.awt.Color(210, 215, 223));
        jLabelFecha.setText("Fecha");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabelFecha)
                    .addComponent(jLabelHora))
                .addGap(14, 14, 14))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelFecha)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelHora)
                .addContainerGap())
        );

        jLabelTitle.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        jLabelTitle.setForeground(new java.awt.Color(12, 96, 197));
        jLabelTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/OnOFF_64_2.png"))); // NOI18N
        jLabelTitle.setText("HmGestión");

        jPanelContent.setBackground(new java.awt.Color(255, 255, 255));
        jPanelContent.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(183, 183, 183)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelContent, javax.swing.GroupLayout.DEFAULT_SIZE, 1268, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelTitle)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanelContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenuBar1.setBackground(new java.awt.Color(204, 204, 204));
        jMenuBar1.setBorder(null);
        jMenuBar1.setForeground(new java.awt.Color(35, 35, 38));
        jMenuBar1.setToolTipText("");
        jMenuBar1.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jMenuBar1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N

        jMenuViewProductSN.setText("Productos");
        jMenuViewProductSN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenuViewProductSNMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jMenuViewProductSNMouseExited(evt);
            }
        });

        jMenuNewProduct.setText("Crear producto nuevo");
        jMenuNewProduct.addActionListener(this::jMenuNewProductActionPerformed);
        jMenuViewProductSN.add(jMenuNewProduct);

        jMenuCategorias.setText("Gestionar categorías");

        jMenuNewCat.setText("Crear Categoría");
        jMenuNewCat.addActionListener(this::jMenuNewCatActionPerformed);
        jMenuCategorias.add(jMenuNewCat);

        jMenuEditCat.setText("Editar Categoría");
        jMenuCategorias.add(jMenuEditCat);

        jMenuNewSubcat.setText("Crear Subcategoría");
        jMenuCategorias.add(jMenuNewSubcat);

        jMenuEditSubcat.setText("Editar Subcategoría");
        jMenuCategorias.add(jMenuEditSubcat);

        jMenuViewProductSN.add(jMenuCategorias);

        jMenuBrands.setText("Gestionar marcas");

        jMenuBrandEdit.setText("Editar marca");
        jMenuBrandEdit.addActionListener(this::jMenuBrandEditActionPerformed);
        jMenuBrands.add(jMenuBrandEdit);

        jMenuNewBrand.setText("Crear marca");
        jMenuNewBrand.addActionListener(this::jMenuNewBrandActionPerformed);
        jMenuBrands.add(jMenuNewBrand);

        jMenuViewProductSN.add(jMenuBrands);

        jMenuIPrice.setText("Editar precio");
        jMenuViewProductSN.add(jMenuIPrice);

        jMenuItemProductSN.setText("Cargar numeros de serie");
        jMenuViewProductSN.add(jMenuItemProductSN);

        jMenuItemViewProductSN.setText("Ver producto por numero de serie");
        jMenuViewProductSN.add(jMenuItemViewProductSN);

        jMenuBar1.add(jMenuViewProductSN);

        jMenu4.setText("Servicio técnico");

        jMenuNewService.setText("Generar orden");
        jMenu4.add(jMenuNewService);

        jMenuBar1.add(jMenu4);

        jMenuPresupuesto.setText("Presupuesto");

        jMenuNewPresupuesto.setText("Nuevo");
        jMenuPresupuesto.add(jMenuNewPresupuesto);

        jMenuListPresupuesto.setText("Ver presupuestos");
        jMenuPresupuesto.add(jMenuListPresupuesto);

        jMenuBar1.add(jMenuPresupuesto);

        jMenu8.setText("Ventas");
        jMenuBar1.add(jMenu8);

        jMenu1.setText("Compras");

        jMenuItemAddPurchaseInvoice.setText("Registrar factura");
        jMenu1.add(jMenuItemAddPurchaseInvoice);

        jMenuBar1.add(jMenu1);

        jMenu5.setText("Proveedores");

        jMenuNewSupplier.setText("Crear proveedor");
        jMenuNewSupplier.addActionListener(this::jMenuNewSupplierActionPerformed);
        jMenu5.add(jMenuNewSupplier);

        jMenuIEditSupplier.setText("Editar proveedor");
        jMenu5.add(jMenuIEditSupplier);

        jMenuBar1.add(jMenu5);

        jMenu3.setText("Clientes");

        jMenuICustomerInsert.setText("Alta cliente");
        jMenu3.add(jMenuICustomerInsert);

        jMenuItemViewCustomer.setText("Ver cliente");
        jMenu3.add(jMenuItemViewCustomer);

        jMenuBar1.add(jMenu3);

        jMenu7.setBorder(null);
        jMenu7.setText("Seguridad");
        jMenu7.setFocusPainted(true);
        jMenuBar1.add(jMenu7);

        jMenu6.setText("Administracion");
        jMenu6.setContentAreaFilled(false);

        jMenuAjusteDeStock.setText("Ajuste de Stock");
        jMenuAjusteDeStock.addActionListener(this::jMenuAjusteDeStockActionPerformed);
        jMenu6.add(jMenuAjusteDeStock);

        jMenuBar1.add(jMenu6);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnProductsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductsMouseEntered
        btnProducts.setForeground(new Color(59,130,246));
    }//GEN-LAST:event_btnProductsMouseEntered

    private void btnProductsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductsMouseExited
        btnProducts.setForeground(new Color(210,215,223));
    }//GEN-LAST:event_btnProductsMouseExited

    private void btnProductsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductsActionPerformed

    }//GEN-LAST:event_btnProductsActionPerformed

    private void btnCashRegisterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCashRegisterMouseEntered
        btnCashRegister.setForeground(new Color(59,130,246));
    }//GEN-LAST:event_btnCashRegisterMouseEntered

    private void btnCashRegisterMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCashRegisterMouseExited
        btnCashRegister.setForeground(new Color(210,215,223));
    }//GEN-LAST:event_btnCashRegisterMouseExited

    private void btnCashRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCashRegisterActionPerformed

    }//GEN-LAST:event_btnCashRegisterActionPerformed

    private void btnClientMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClientMouseEntered
        btnClient.setForeground(new Color(59,130,246));
    }//GEN-LAST:event_btnClientMouseEntered

    private void btnClientMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClientMouseExited
        btnClient.setForeground(new Color(210,215,223));
    }//GEN-LAST:event_btnClientMouseExited

    private void btnClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientActionPerformed

    }//GEN-LAST:event_btnClientActionPerformed

    private void btnCtaCteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCtaCteMouseEntered
        btnCtaCte.setForeground(new Color(59,130,246));
    }//GEN-LAST:event_btnCtaCteMouseEntered

    private void btnCtaCteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCtaCteMouseExited
        btnCtaCte.setForeground(new Color(210,215,223));
    }//GEN-LAST:event_btnCtaCteMouseExited

    private void btnCtaCteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCtaCteActionPerformed

    }//GEN-LAST:event_btnCtaCteActionPerformed

    private void btnServiceTecMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnServiceTecMouseEntered
        btnServiceTec.setForeground(new Color(59,130,246));
    }//GEN-LAST:event_btnServiceTecMouseEntered

    private void btnServiceTecMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnServiceTecMouseExited
        btnServiceTec.setForeground(new Color(210,215,223));
    }//GEN-LAST:event_btnServiceTecMouseExited

    private void btnServiceTecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnServiceTecActionPerformed

    }//GEN-LAST:event_btnServiceTecActionPerformed

    private void btnExitMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseEntered
        btnExit.setForeground(new Color(59,130,246));
    }//GEN-LAST:event_btnExitMouseEntered

    private void btnExitMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseExited
        btnExit.setForeground(new Color(210,215,223));
    }//GEN-LAST:event_btnExitMouseExited

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed

    }//GEN-LAST:event_btnExitActionPerformed

    private void btnHomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseEntered
        btnHome.setForeground(new Color(59,130,246));
    }//GEN-LAST:event_btnHomeMouseEntered

    private void btnHomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseExited
        btnHome.setForeground(new Color(210,215,223));
    }//GEN-LAST:event_btnHomeMouseExited

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHomeActionPerformed

    private void jMenuNewProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuNewProductActionPerformed

    }//GEN-LAST:event_jMenuNewProductActionPerformed

    private void jMenuNewCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuNewCatActionPerformed

    }//GEN-LAST:event_jMenuNewCatActionPerformed

    private void jMenuBrandEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuBrandEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuBrandEditActionPerformed

    private void jMenuNewBrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuNewBrandActionPerformed

    }//GEN-LAST:event_jMenuNewBrandActionPerformed

    private void jMenuViewProductSNMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuViewProductSNMouseEntered

    }//GEN-LAST:event_jMenuViewProductSNMouseEntered

    private void jMenuViewProductSNMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuViewProductSNMouseExited

    }//GEN-LAST:event_jMenuViewProductSNMouseExited

    private void jMenuNewSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuNewSupplierActionPerformed

    }//GEN-LAST:event_jMenuNewSupplierActionPerformed

    private void jMenuAjusteDeStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuAjusteDeStockActionPerformed

    }//GEN-LAST:event_jMenuAjusteDeStockActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new homeFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCashRegister;
    private javax.swing.JButton btnClient;
    private javax.swing.JButton btnCtaCte;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnProducts;
    private javax.swing.JButton btnServiceTec;
    private javax.swing.JLabel jLabelFecha;
    private javax.swing.JLabel jLabelHora;
    private javax.swing.JLabel jLabelLineaBlanca;
    private javax.swing.JLabel jLabelTitle;
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
    private javax.swing.JMenu jMenuBrands;
    private javax.swing.JMenu jMenuCategorias;
    private javax.swing.JMenuItem jMenuEditCat;
    private javax.swing.JMenuItem jMenuEditSubcat;
    private javax.swing.JMenuItem jMenuICustomerInsert;
    private javax.swing.JMenuItem jMenuIEditSupplier;
    private javax.swing.JMenuItem jMenuIPrice;
    private javax.swing.JMenuItem jMenuItemAddPurchaseInvoice;
    private javax.swing.JMenuItem jMenuItemProductSN;
    private javax.swing.JMenuItem jMenuItemViewCustomer;
    private javax.swing.JMenuItem jMenuItemViewProductSN;
    private javax.swing.JMenuItem jMenuListPresupuesto;
    private javax.swing.JMenuItem jMenuNewBrand;
    private javax.swing.JMenuItem jMenuNewCat;
    private javax.swing.JMenuItem jMenuNewPresupuesto;
    private javax.swing.JMenuItem jMenuNewProduct;
    private javax.swing.JMenuItem jMenuNewService;
    private javax.swing.JMenuItem jMenuNewSubcat;
    private javax.swing.JMenuItem jMenuNewSupplier;
    private javax.swing.JMenu jMenuPresupuesto;
    private javax.swing.JMenu jMenuViewProductSN;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelContent;
    private javax.swing.JLabel lbl_user;
    // End of variables declaration//GEN-END:variables
}
