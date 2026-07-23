/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import dao.currentAccountDAO;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import utils.tableStyleUtil;
import utils.utility;


public class currentAccountSelectDialog extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(currentAccountSelectDialog.class.getName());

    currentAccountDAO qCA = new currentAccountDAO();
    utility utils = new utility();
    
    private int idCA = -1;
    private String CANumber;
    private String name;
    private Object[] rowOp;
    private Object[] rowItem;
    
    DefaultTableModel dtmCurrentAccounts = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    DefaultTableModel dtmCurrentAccountsMovements = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    
    public Object[] CAOperation(){        
        return rowOp;
    }
    public Object[] CAItem(){        
        return rowItem;
    }
    
        
    private TableRowSorter<DefaultTableModel> sorter;
    
    public currentAccountSelectDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        utils.agregarPlaceholderN(txtBuscar, "Buscar...");
        
        tableCurrentAccounts();
        tablaMovements();
        buscadorTabla();
        actions();
        
    }
    
    private void actions(){
        
        tableCurrentAccount.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e){

                int filaVista = tableCurrentAccount.getSelectedRow();

                if(filaVista >= 0){

                    int filaModelo = tableCurrentAccount.convertRowIndexToModel(filaVista);
                    idCA = Integer.parseInt(dtmCurrentAccounts.getValueAt(filaModelo, 0).toString());
                    CANumber = String.valueOf(dtmCurrentAccounts.getValueAt(filaModelo, 1).toString());
                    name = String.valueOf(dtmCurrentAccounts.getValueAt(filaModelo, 2).toString());
                            
                    qCA.listCAMovements(idCA, dtmCurrentAccountsMovements);
                    lbl_saldo.setText(String.valueOf("$"+qCA.calcularSaldoCA(idCA)));
                    
                }
            }
        });
        
        btnAll.addActionListener(e->{
        
            Double todo = Double.parseDouble(lbl_saldo.getText().trim().replace("$", "").replace(" ", "").replace(",", "."));   
            
            txtPrice.setText(String.valueOf(todo));
            
        });
        
        btnSelect.addActionListener(e -> {

            if (idCA == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una cuenta corriente.");
                return;
            }

            if (txtPrice.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un importe.");
                return;
            }

            double valorIngresado;
            double totalSaldo;

            try{
                valorIngresado = Double.parseDouble(txtPrice.getText().trim());
                totalSaldo = Double.parseDouble(lbl_saldo.getText().replace("$", "").replace(" ", "").replace(",", ".").trim());

            }catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Importe inválido.");
                return;
            }

            if(valorIngresado <= 0) {
                JOptionPane.showMessageDialog(this, "Ingrese un importe mayor a cero.");
                return;
            }

            if(valorIngresado > totalSaldo) {
                JOptionPane.showMessageDialog(this, "El valor no puede ser mayor al saldo.");
                return;
            }

            String item;

            if(valorIngresado < totalSaldo) {
                item = "Pago parcial de cuenta corriente";
            }else{
                item = "Pago total de cuenta corriente";
            }

            LocalDate fechaLocal = LocalDate.now();

            rowOp = new Object[]{
                java.sql.Date.valueOf(fechaLocal),
                "Cuenta corriente",
                CANumber,
                name,
                valorIngresado
            };

            rowItem = new Object[]{
                "",
                CANumber,
                item,
                "",
                "",
                "",
                valorIngresado
            };

            dispose();
        });        
        
        btnCancel.addActionListener(e->{    
            
            Arrays.fill(rowOp, null);
            this.dispose();           
        });        
    }   

    private void tableCurrentAccounts(){

        String[] titulo = {"id", "Nº Cta.", "Cliente","Id cliente"};
        dtmCurrentAccounts.setColumnIdentifiers(titulo);        
        tableCurrentAccount.setModel(dtmCurrentAccounts);
        
        qCA.listCurrentAccountSimplified(dtmCurrentAccounts);
        
        sorter = new TableRowSorter<>(dtmCurrentAccounts);
        tableCurrentAccount.setRowSorter(sorter);
        
        tableStyleUtil.applyPoppinsHeader(tableCurrentAccount);

        tableCurrentAccount.getColumnModel().getColumn(0).setMinWidth(0);
        tableCurrentAccount.getColumnModel().getColumn(0).setMaxWidth(0);
        tableCurrentAccount.getColumnModel().getColumn(0).setPreferredWidth(0);       

        tableCurrentAccount.getColumnModel().getColumn(1).setPreferredWidth(100);
        tableCurrentAccount.getColumnModel().getColumn(2).setPreferredWidth(250);

        tableCurrentAccount.getColumnModel().getColumn(3).setMinWidth(0);
        tableCurrentAccount.getColumnModel().getColumn(3).setMaxWidth(0);
        tableCurrentAccount.getColumnModel().getColumn(3).setPreferredWidth(0);
        
        tableCurrentAccount.getTableHeader().setReorderingAllowed(false);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

    }
    
        private void tablaMovements(){
        
        String[] titulo = {"Fecha","Descripcion","Cantidad","Precio","IVA","Credito","Debito"};
        dtmCurrentAccountsMovements.setColumnIdentifiers(titulo);     
        
        tableMovements.setModel(dtmCurrentAccountsMovements);
        

        
        tableStyleUtil.applyPoppinsHeader(tableMovements); 

        tableMovements.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableMovements.getColumnModel().getColumn(1).setPreferredWidth(250); 
        tableMovements.getColumnModel().getColumn(2).setPreferredWidth(40);
        tableMovements.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableMovements.getColumnModel().getColumn(4).setPreferredWidth(40);
        tableMovements.getColumnModel().getColumn(5).setPreferredWidth(100); 
        tableMovements.getColumnModel().getColumn(6).setPreferredWidth(100);

        tableMovements.getTableHeader().setReorderingAllowed(false);
        
        tableMovements.getColumnModel().getColumn(5).setCellRenderer(rendererCredito);
        tableMovements.getColumnModel().getColumn(6).setCellRenderer(rendererCredito);
        
    }
    
    private void buscadorTabla(){

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
    
    DefaultTableCellRenderer rendererCredito = new DefaultTableCellRenderer() {
        @Override
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if(!isSelected){
                if(column == 5){
                    c.setForeground(new Color(204, 153, 0));  
                }else if(column == 6){
                    c.setForeground(new Color(0, 128, 0));  
                }else{
                    c.setForeground(Color.BLACK);
                }
            }

            setHorizontalAlignment(SwingConstants.CENTER);
            return c;
        }
    };

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableMovements = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        lbl_nameCustomer1 = new javax.swing.JLabel();
        lbl_saldo = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        btnAll = new javax.swing.JButton();
        btnSelect = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCurrentAccount = new javax.swing.JTable();
        txtBuscar = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión - Seleccionar Cta\\Cte");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(245, 248, 255));

        jLabel45.setFont(new java.awt.Font("Poppins", 0, 36)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(12, 83, 151));
        jLabel45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/currentAccount64.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(12, 83, 151));
        jLabel1.setText("Seleccionar cuenta corriente");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 11, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        tableMovements.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tableMovements);

        jPanel6.setBackground(new java.awt.Color(245, 248, 255));

        lbl_nameCustomer1.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        lbl_nameCustomer1.setForeground(new java.awt.Color(12, 83, 151));
        lbl_nameCustomer1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_nameCustomer1.setText("Saldo:");
        lbl_nameCustomer1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lbl_saldo.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        lbl_saldo.setForeground(new java.awt.Color(12, 83, 151));
        lbl_saldo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_saldo.setText("xxx");
        lbl_saldo.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_nameCustomer1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_saldo, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_nameCustomer1)
                    .addComponent(lbl_saldo))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(12, 83, 151));
        jLabel3.setText("Ingresar el valor a pagar:");

        txtPrice.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPriceKeyTyped(evt);
            }
        });

        btnAll.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnAll.setForeground(new java.awt.Color(35, 35, 38));
        btnAll.setText("Todo");

        btnSelect.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnSelect.setForeground(new java.awt.Color(12, 83, 151));
        btnSelect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnSelect.setText("Seleccionar");
        btnSelect.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit-32.png"))); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

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
        jScrollPane1.setViewportView(tableCurrentAccount);

        txtBuscar.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
            .addComponent(txtBuscar)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceKeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c)
                && c != '.'
                && c != ','
                && c != KeyEvent.VK_BACK_SPACE
                && c != KeyEvent.VK_DELETE) {
            evt.consume();
        }

        if ((c == '.' || c == ',')
                && (txtPrice.getText().contains(".")
                || txtPrice.getText().contains(","))) {
            evt.consume();
        }

        if (txtPrice.getText().length() >= 10
                && Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPriceKeyTyped

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                currentAccountSelectDialog dialog = new currentAccountSelectDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAll;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSelect;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_nameCustomer1;
    private javax.swing.JLabel lbl_saldo;
    private javax.swing.JTable tableCurrentAccount;
    private javax.swing.JTable tableMovements;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtPrice;
    // End of variables declaration//GEN-END:variables
}
