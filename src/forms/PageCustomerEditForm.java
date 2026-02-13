/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package forms;

import OldNotUsed.frmCustomerSerch;
import Class.modelClient;
import Class.ProductDAO;
import Class.GenericDAO;
import Class.CustomerDAO;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class PageCustomerEditForm extends javax.swing.JPanel {

    ProductDAO queriesProduct = new ProductDAO();
    GenericDAO queriesGeneric = new GenericDAO();
    CustomerDAO queriesClient = new CustomerDAO();
    
    modelClient mClient = new modelClient();
    String fecha=queriesGeneric.fecha();
    
    public PageCustomerEditForm() {
        initComponents();
        
        queriesGeneric.agregarPlaceholderN(txtCodeSearch, "Ingrese numero de DNI o CUIT..."); 
        
        txtIVA.setEnabled(false);
        txtIVA.setVisible(false);
        txtProvince.setEnabled(false);
        txtProvince.setVisible(false);
        
        startMsjs();
        llenarCombos();
        actions();
    }
    

    private void clear(){
        cboIVA.setSelectedIndex(0);
        cboProvinces.setSelectedIndex(0);
        txtName.setText("");
        txtCUIT.setText("");
        txtTel.setText("");
        txtEmail.setText("");
        txtStreet.setText("");
        txtHeight.setText("");
        txtCity.setText("");
    }
    
    private void startMsjs(){
        lblErrorName.setText("");
        lblErrorDni.setText("");
        lblErrorTel.setText("");
        lblErrorEmail.setText("");
    }

    private void llenarCombos(){       
        queriesGeneric.llenarCombos(cboIVA, "iva");
        queriesGeneric.llenarCombosActivos(cboProvinces, "provinces");
    }
    
    private void offObjets(){
        cboIVA.setEnabled(false);
        txtName.setEnabled(false);
        txtCUIT.setEnabled(false);
        txtTel.setEnabled(false);
        txtEmail.setEnabled(false);
        txtStreet.setEnabled(false);
        txtHeight.setEnabled(false);
        txtCity.setEnabled(false);
        cboProvinces.setEnabled(false);
    }    
    
    private void onObjets(){
        cboIVA.setEnabled(true);
        txtName.setEnabled(true);
        txtCUIT.setEnabled(true);
        txtTel.setEnabled(true);
        txtEmail.setEnabled(true);
        txtStreet.setEnabled(true);
        txtHeight.setEnabled(true);
        txtCity.setEnabled(true);
        cboProvinces.setEnabled(true);
    } 

    
    public void infoCombo(){
        String iva = txtIVA.getText().trim();
        String province = txtProvince.getText().trim();

        for (int i = 0; i < cboIVA.getItemCount(); i++) {
            if (cboIVA.getItemAt(i).toString().equalsIgnoreCase(iva)) {
                cboIVA.setSelectedIndex(i);
                break;
            }
        } 
        for (int i = 0; i < cboProvinces.getItemCount(); i++) {
            if (cboProvinces.getItemAt(i).toString().equalsIgnoreCase(province)) {
                cboProvinces.setSelectedIndex(i);
                break;
            }
        }         
    }
    
    public void buscar(){
        clear();
        queriesClient.selectClientEdit(txtCodeSearch.getText().trim(), txtID, txtName, txtCUIT, txtTel, txtEmail, txtStreet, txtHeight, txtCity, txtIVA, txtProvince);
        infoCombo();                      
    }
    
    private void actions(){
        
        queriesGeneric.clearMsjErrorTxt(txtName, lblErrorName);
        queriesGeneric.clearMsjErrorTxt(txtCUIT, lblErrorDni);
        queriesGeneric.clearMsjErrorTxt(txtTel, lblErrorTel);  
        
        btnSerchByList.addActionListener(e->{
            txtCodeSearch.setText("");
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            DlgCustomerSearchForm formListCustomer = new DlgCustomerSearchForm(parent, true);
            formListCustomer.setVisible(true);

            String resultCuit = formListCustomer.getCustomerSerch();
            queriesClient.selectClientEdit(resultCuit, txtID, txtName, txtCUIT, txtTel, txtEmail, txtStreet, txtHeight, txtCity, txtIVA, txtProvince);
            
            infoCombo();
                      
        });
        
        btnSerch.addActionListener(e -> {
            String texto = txtCodeSearch.getText().trim();

            if (!texto.isEmpty()) {
                buscar();
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese el DNI o CUIT");
                txtCodeSearch.requestFocus();
            }
        });
        
        btnCancel.addActionListener(e-> {
            limpiar();
        });
        
        btnRegistrar.addActionListener(e-> {           
            updateCustomer();
            clear();
        });
        
        queriesGeneric.clearMsjErrorTxt(txtName, lblErrorName);
        queriesGeneric.clearMsjErrorTxt(txtCUIT, lblErrorDni);
        queriesGeneric.clearMsjErrorTxt(txtTel, lblErrorTel);

        //OBLIGA A VALIDAR MAIL CON '@'       
        txtEmail.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String email = txtEmail.getText();

                if (!email.contains("@")) {
                    lblErrorEmail.setText("El correo debe contener '@'");
                }else
                    lblErrorEmail.setText("");
            }
        });  
        
        //VALIDA QUE TENGA AL MENOS 8 CARACTERES
        txtCUIT.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String texto = txtCUIT.getText();

                if (!texto.isEmpty() && texto.length() < 8) {
                    JOptionPane.showMessageDialog(
                        null,
                        "El DNI / CUIT debe tener al menos 8 dígitos",
                        "Dato inválido",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });               
        
        txtTel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String texto = txtTel.getText();

                if (texto.length() >= 10) {
                    lblErrorTel.setText("");   
                }
            }
        }); 
        
        txtName.addActionListener(e->{
            lblErrorName.setText("");
        });
        
    }
    
    private void limpiar(){
        txtName.setText("");
        txtEmail.setText("");
        txtTel.setText("");
        txtCUIT.setText("");
        txtStreet.setText("");
        txtHeight.setText("");
        txtCity.setText("");
        cboProvinces.setSelectedIndex(0);
        cboIVA.setSelectedIndex(0);
        startMsjs();
    }
    
    private void updateCustomer(){
        String msjError ="Complete el campo!";
        
        boolean valido = true;
        
        String cuitText = txtCUIT.getText().trim();
        mClient.state=1;
        mClient.fechaRegistro=fecha;
        
        if (cboIVA.getSelectedIndex() > 0) { 
            mClient.iva = queriesGeneric.selectId(
                    "id_iva",
                    "iva",
                    cboIVA.getSelectedItem().toString()
            );
        } else {
            valido = false;
        }

        if(!txtName.getText().isEmpty()){           
            if (txtName.getText().trim().length() < 5) {
                lblErrorName.setText("El nombre debe contener al menos 5 caracteres.");
            } else {
                mClient.name = txtName.getText().trim().toUpperCase();
            }              
        } else {
            lblErrorName.setText(msjError);
            valido = false;
        }       
        
        if(!txtEmail.getText().isEmpty()){
            mClient.email = txtEmail.getText();
        } else {
            mClient.email="S/D";
        } 

        if(!txtTel.getText().isEmpty()){
            if (txtTel.getText().length() < 10) {
                lblErrorTel.setText("El número de telefono debe tener al menos 10 numeros");
            }else{
                mClient.phone = txtTel.getText().trim();
            }        
        } else {
            lblErrorTel.setText(msjError);
            valido = false;
        }  

        if(!cuitText.isEmpty()){
//      VALIDA QUE EL DNI/CUIT NO SE REPITA          
            if (queriesClient.existsByCuit(cuitText)) {
                JOptionPane.showMessageDialog(null,
                    "El DNI/CUIT corresponde a otro cliente.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }else{
                mClient.CUIT= cuitText;  
            }
        } else {
            lblErrorDni.setText(msjError);
            valido = false;
        } 
        
        if(!txtStreet.getText().isEmpty()){
            mClient.street = txtStreet.getText().trim().toUpperCase();
        } else {
            mClient.street = "S/D";
        }          

        if(!txtHeight.getText().isEmpty()){
            mClient.height = Integer.parseInt(txtHeight.getText());
        } else {
            mClient.height = 0;
        }  

        if(!txtCity.getText().isEmpty()){
            mClient.city = txtCity.getText().trim().toUpperCase();
        } else {
            mClient.city = "S/D";
        } 

        if (cboProvinces.getSelectedIndex() > 0) { 
            mClient.idProvince=queriesGeneric.selectId(
                    "id_province",
                    "provinces",
                    cboProvinces.getSelectedItem().toString()
            );
        } else {
            mClient.idProvince=25;
        } 
        
        if(!txtID.getText().isEmpty()){
            mClient.ID = Integer.parseInt(txtID.getText());
        } else {
            valido = false;
        }          
        
        if (!valido) {
            return;
        }else{
            int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Confirma el registro del Cliente?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
            );                   
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }      
        }
                   
        queriesClient.updateCustomer(
                mClient.getID(),        
                mClient.getName(),
                mClient.getIva(),
                mClient.getCUIT(),
                mClient.getEmail(),
                mClient.getPhone(),
                mClient.getStreet(),
                mClient.getHeight(),
                mClient.getCity(),
                mClient.getIdProvince()  
        );        
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtTel = new javax.swing.JTextField();
        txtCUIT = new javax.swing.JTextField();
        lblErrorDni = new javax.swing.JLabel();
        lblErrorTel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblErrorEmail = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cboIVA = new javax.swing.JComboBox<>();
        lblErrorName = new javax.swing.JLabel();
        txtIVA = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        btnRegistrar = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        txtStreet = new javax.swing.JTextField();
        txtCity = new javax.swing.JTextField();
        txtHeight = new javax.swing.JTextField();
        cboProvinces = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtProvince = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        btnSerch = new javax.swing.JButton();
        txtCodeSearch = new javax.swing.JTextField();
        btnSerchByList = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)), "Datos de cliente.", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 1, 18), new java.awt.Color(101, 129, 171))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel1.setText("Nombre");

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel3.setText("DNI / CUIT");

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel5.setText("Teléfono");

        txtName.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        txtTel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtTel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        txtTel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelKeyPressed(evt);
            }
        });

        txtCUIT.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtCUIT.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        txtCUIT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCUITActionPerformed(evt);
            }
        });
        txtCUIT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCUITKeyPressed(evt);
            }
        });

        lblErrorDni.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorDni.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorDni.setText("mensaje error!");

        lblErrorTel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorTel.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorTel.setText("mensaje error!");

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel4.setText("Email");

        txtEmail.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtEmail.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        lblErrorEmail.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorEmail.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorEmail.setText("mensaje error!");

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel2.setText("Tipo de cliente");

        cboIVA.setBackground(new java.awt.Color(255, 255, 255));
        cboIVA.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        lblErrorName.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblErrorName.setForeground(new java.awt.Color(255, 102, 51));
        lblErrorName.setText("mensaje error!");

        txtIVA.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtIVA.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel7.setText("ID:");

        txtID.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtID.setBorder(null);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(txtName)
                            .addComponent(cboIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCUIT)
                            .addComponent(txtTel)
                            .addComponent(txtEmail))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblErrorName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblErrorDni, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblErrorTel, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblErrorEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtID)
                        .addGap(388, 388, 388)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblErrorName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cboIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCUIT, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblErrorDni, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblErrorTel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblErrorEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        btnRegistrar.setBackground(new java.awt.Color(255, 255, 255));
        btnRegistrar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(101, 129, 171));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/product32.png"))); // NOI18N
        btnRegistrar.setText("Editar");
        btnRegistrar.setBorder(null);
        btnRegistrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistrar.setMaximumSize(new java.awt.Dimension(120, 52));
        btnRegistrar.setMinimumSize(new java.awt.Dimension(120, 52));
        btnRegistrar.setPreferredSize(new java.awt.Dimension(120, 52));
        btnRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseExited(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(255, 255, 255));
        btnCancel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/borrador32.png"))); // NOI18N
        btnCancel.setBorder(null);
        btnCancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(291, 291, 291)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)), "Dirección", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 1, 14), new java.awt.Color(101, 129, 171))); // NOI18N

        txtStreet.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtStreet.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Calle", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 0, 14))); // NOI18N

        txtCity.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtCity.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Calle", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 0, 14))); // NOI18N

        txtHeight.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtHeight.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Calle", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 0, 14))); // NOI18N
        txtHeight.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtHeightKeyPressed(evt);
            }
        });

        cboProvinces.setBackground(new java.awt.Color(255, 255, 255));
        cboProvinces.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel6.setText("Provincia");

        txtProvince.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtProvince.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtStreet, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCity))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(cboProvinces, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtProvince, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtStreet, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cboProvinces, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProvince, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        btnSerch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Serch32.png"))); // NOI18N
        btnSerch.setBorder(null);
        btnSerch.setBorderPainted(false);
        btnSerch.setContentAreaFilled(false);
        btnSerch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSerch.setDefaultCapable(false);
        btnSerch.setFocusPainted(false);
        btnSerch.setFocusable(false);
        btnSerch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSerchActionPerformed(evt);
            }
        });

        txtCodeSearch.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        txtCodeSearch.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCodeSearch.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(101, 129, 171)));
        txtCodeSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodeSearchKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodeSearchKeyTyped(evt);
            }
        });

        btnSerchByList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/serchList32.png"))); // NOI18N
        btnSerchByList.setBorder(null);
        btnSerchByList.setBorderPainted(false);
        btnSerchByList.setContentAreaFilled(false);
        btnSerchByList.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSerchByList.setDefaultCapable(false);
        btnSerchByList.setFocusPainted(false);
        btnSerchByList.setFocusable(false);
        btnSerchByList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSerchByListActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtCodeSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(btnSerch, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSerchByList, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(343, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnSerch, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSerchByList, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodeSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(131, Short.MAX_VALUE))
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

    private void txtTelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelKeyPressed
        txtTel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtTel.getText().length() >= 15) {
                    e.consume();
                }
            }
        });
    }//GEN-LAST:event_txtTelKeyPressed

    private void txtCUITActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCUITActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCUITActionPerformed

    private void txtCUITKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCUITKeyPressed
        txtCUIT.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtCUIT.getText().length() >= 11) {
                    e.consume();
                }
            }
        });
    }//GEN-LAST:event_txtCUITKeyPressed

    private void btnRegistrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseEntered
        btnRegistrar.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnRegistrarMouseEntered

    private void btnRegistrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseExited
        btnRegistrar.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnRegistrarMouseExited

    private void btnCancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseEntered
        btnCancel.setBackground(new Color(255,127,39));
    }//GEN-LAST:event_btnCancelMouseEntered

    private void btnCancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseExited
        btnCancel.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCancelMouseExited

    private void txtHeightKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHeightKeyPressed
        txtHeight.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtHeight.getText().length() >= 5) {
                    e.consume();
                }
            }
        });
    }//GEN-LAST:event_txtHeightKeyPressed

    private void btnSerchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSerchActionPerformed

    }//GEN-LAST:event_btnSerchActionPerformed

    private void btnSerchByListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSerchByListActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSerchByListActionPerformed

    private void txtCodeSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodeSearchKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodeSearchKeyTyped

    private void txtCodeSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodeSearchKeyPressed
        txtCodeSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtCodeSearch.getText().length() >= 11) {
                    e.consume();
                }
            }
        });
    }//GEN-LAST:event_txtCodeSearchKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnSerch;
    private javax.swing.JButton btnSerchByList;
    private javax.swing.JComboBox<String> cboIVA;
    private javax.swing.JComboBox<String> cboProvinces;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lblErrorDni;
    private javax.swing.JLabel lblErrorEmail;
    private javax.swing.JLabel lblErrorName;
    private javax.swing.JLabel lblErrorTel;
    private javax.swing.JTextField txtCUIT;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtCodeSearch;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtHeight;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtIVA;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtProvince;
    private javax.swing.JTextField txtStreet;
    private javax.swing.JTextField txtTel;
    // End of variables declaration//GEN-END:variables
}
