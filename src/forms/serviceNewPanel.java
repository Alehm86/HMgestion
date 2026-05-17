/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package forms;

import classDAO.customerDAO;
import classDAO.genericDAO;
import classDAO.serviceDAO;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import models.modelDevice;
import models.modelService;

public class serviceNewPanel extends javax.swing.JPanel {

    customerDAO qCustomer = new customerDAO();
    serviceDAO qService = new serviceDAO();
    
    modelDevice device = new modelDevice();
    modelService service = new modelService();
    
    int id_service = -1;
    String cuitClient = "";
    String snDevice;

    JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
    
    public serviceNewPanel() {
        initComponents();

        inicializar();
        actions();
        leyendaBotones();
        
    }
    
    private void leyendaBotones(){
        
        btnNewCustomer.setToolTipText("Alta cliente nuevo");
        btnCancel.setToolTipText("Borrar");
        btnSearch.setToolTipText("Buscar por número de serie");
        bntSearchByCustomer.setToolTipText("Seleccioná un dispositivo de la lista");
        btnNewDevice.setToolTipText("Registrar dispositivo");
    }
    
    private void inicializar(){
        
        txtName.setEditable(false);
        txtPhone.setEditable(false);
        
        lbl_id.setText("");       
        lblDevice.setText("");
        lblBrand.setText("");
        lblModel.setText("");
        lblDescription.setText("");
        lbl_idDevice.setText("");
        lblSerialNumber.setText("");
        
        jLabel14.setEnabled(false);
        txtParteA.setEnabled(false);
        txtParteB.setEnabled(false);
        btnSearchServiceOrder.setEnabled(false);
    }
    
    private void actions(){
                
        btnBuscar.addActionListener(e->{
                       
            customerSearchDialog pSearch = new customerSearchDialog(parent, true);           
            pSearch.setVisible(true);
            cuitClient = pSearch.getCustomerSerch();
            
            if(!cuitClient.isEmpty()){
                limpiar();
                qCustomer.selectCustomerSimplified(cuitClient, lbl_id, txtName, txtPhone);
            }
        });
        
        bntSearchByCustomer.addActionListener(e->{
            
            JTable tabla = new JTable();
            boolean dato = false;
            
            
            customerDevicesDialog pCustDev = new customerDevicesDialog(parent, true);         
            if(!lbl_id.getText().isEmpty()){
                
                int idClient = Integer.parseInt(lbl_id.getText().trim());
                dato = dato = qCustomer.listCustomerDevices(tabla, idClient);
                
                if(dato){
                    pCustDev.setIdCustomer(idClient);
                    pCustDev.setVisible(true); 
                }else{
                    JOptionPane.showMessageDialog(null, "No hay dispositivos vinculados");
                }

                snDevice = pCustDev.getSerialNumberDevice();
                if(!snDevice.isEmpty()){
                    buscarDevice();
                }
                          
            }else{
                JOptionPane.showMessageDialog(null, "Debe seleccionar un cliente.");
            }
        });
        
        btnNewCustomer.addActionListener(e->{
            
            customerNewDialog pNewClient = new customerNewDialog(parent, true);           
            pNewClient.setVisible(true);
            cuitClient = pNewClient.getCuitClient();
            
            qCustomer.selectCustomerSimplified(cuitClient, lbl_id, txtName, txtPhone);
        });
        
        btnCancel.addActionListener(e->{
            
            limpiar();
        });
        
        btnNewDevice.addActionListener(e->{
            
            int idClient = Integer.parseInt(lbl_id.getText().trim());
            
            serviceNewDeviceDialog pNewDevice = new serviceNewDeviceDialog(parent, true);   
            pNewDevice.setIdClient(idClient);
            pNewDevice.setVisible(true);
            snDevice = pNewDevice.getSNDevice();
            
            buscarDevice();
        });
        
        btnSearch.addActionListener(e->{
            
            snDevice = txtSerialNumber.getText().toString().trim();
            buscarDevice();  
        });
        
        btnRegistrar.addActionListener(e->{
            
            registrarServicio();                
            servicePrintDialog pPrintServiceOrder = new servicePrintDialog(parent, true);
            pPrintServiceOrder.dialogoId_service(id_service);
            pPrintServiceOrder.setVisible(true);
        });
        
        chkboxGarantia.addActionListener(e->{
            
            limpiar();
            boolean seleccionado = chkboxGarantia.isSelected();
            
            if(seleccionado){          
                jLabel14.setEnabled(true);
                txtParteA.setEnabled(true);
                txtParteB.setEnabled(true);
                btnSearchServiceOrder.setEnabled(true);
                
                btnBuscar.setEnabled(false);
                btnNewCustomer.setEnabled(false);
                txtSerialNumber.setEnabled(false);
                btnSearch.setEnabled(false);
                bntSearchByCustomer.setEnabled(false);
                btnNewDevice.setEnabled(false);
                
            }else{
                jLabel14.setEnabled(false);
                txtParteA.setEnabled(false);
                txtParteB.setEnabled(false);
                btnSearchServiceOrder.setEnabled(false);
                
                btnBuscar.setEnabled(true);
                btnNewCustomer.setEnabled(true);
                txtSerialNumber.setEnabled(true);
                btnSearch.setEnabled(true);
                bntSearchByCustomer.setEnabled(true);
                btnNewDevice.setEnabled(true);
            }
        });
        
        btnSearchServiceOrder.addActionListener(e->{
            
            String parteA = txtParteA.getText().trim();
            String parteB = txtParteB.getText().trim();
            String serviceNumber = "ST-"+parteA+"-"+parteB;
            
            if(!parteA.isEmpty() && !parteB.isEmpty()){
                qService.searchServiceOrder(
                        serviceNumber,
                        lbl_id,
                        txtName,
                        txtPhone,
                        lbl_idDevice,
                        lblDevice,
                        lblSerialNumber,
                        lblBrand,
                        lblModel,
                        lblDescription,
                        textAreaProblem
                );
                
            }
        });
    }
    
    private void buscarDevice(){
        qService.selectDevice(snDevice, lbl_idDevice, lblSerialNumber, lblDevice, lblBrand, lblModel, lblDescription);
    }
    
    private void limpiar(){
        
        txtName.setText("");
        txtPhone.setText("");
        lblDevice.setText("");
        lblBrand.setText("");
        lblModel.setText("");
        txtSerialNumber.setText("");
        textAreaProblem.setText("");
        lbl_id.setText("");       
        lblDevice.setText("");
        lblBrand.setText("");
        lblModel.setText("");
        lblDescription.setText("");
        lbl_idDevice.setText("");
        lblSerialNumber.setText("");
    }
    
    private void registrarServicio(){
        
        int garantia = 0;
        
        boolean valido = true;
        
        int estado = 1;
        service.setId_status(estado);
        
        if(chkboxGarantia.isSelected()){
            garantia = 1;
        }
        
        if(!txtName.getText().isEmpty()){
            service.setId_customer(Integer.parseInt(lbl_id.getText().trim()));
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un cliente.");
            valido = false;
        }
        
        if(!lbl_idDevice.getText().equals("-")){
            service.setId_device(Integer.parseInt(lbl_idDevice.getText().trim()));
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un dispositivo.");
            valido = false;
        }
        
        if(!textAreaProblem.getText().isEmpty()){
            service.setReported_problem(textAreaProblem.getText().toString().toUpperCase());
        }else{
            JOptionPane.showMessageDialog(null, "Debe describir la falla.");
            valido = false;
        }
        
        if (!valido) {
            return;
        }else{
            int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Confirma el registro?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
            );                   
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }      
        }

        id_service = qService.insertService(
            service.getId_customer(), 
            service.getId_device(), 
            service.getReported_problem(), 
            service.getId_status(),
            garantia
        );     
        
        if (id_service > 0){
            qService.insertService_order_status_history(id_service, service.getId_status());
        }
        
        limpiar();    
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jPanelSeparador2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtSerialNumber = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaProblem = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        btnNewCustomer = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        lbl_id = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        btnNewDevice = new javax.swing.JButton();
        bntSearchByCustomer = new javax.swing.JButton();
        lblDevice = new javax.swing.JLabel();
        lblBrand = new javax.swing.JLabel();
        lblModel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblDescription = new javax.swing.JLabel();
        jPanelSeparador4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        lblSerialNumber = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lbl_idDevice = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        chkboxGarantia = new javax.swing.JCheckBox();
        txtParteA = new javax.swing.JTextField();
        btnSearchServiceOrder = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtParteB = new javax.swing.JTextField();

        jPanel1.setBackground(new java.awt.Color(35, 35, 38));

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ordenador-personal64.png"))); // NOI18N
        jLabel1.setText("Recepción de Equipo para Servicio Técnico");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 819, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        txtName.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtName.setForeground(new java.awt.Color(35, 35, 38));
        txtName.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtName.setBorder(null);

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(35, 35, 38));
        jLabel2.setText("Nombre y apellido:");

        btnBuscar.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(0, 153, 255));
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/lupa32.png"))); // NOI18N
        btnBuscar.setText("Buscar cliente");
        btnBuscar.setBorder(null);
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscar.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuscarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBuscarMouseExited(evt);
            }
        });
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(35, 35, 38));
        jLabel3.setText("Teléfono:");

        txtPhone.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtPhone.setForeground(new java.awt.Color(35, 35, 38));
        txtPhone.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtPhone.setBorder(null);
        txtPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPhoneKeyPressed(evt);
            }
        });

        jPanelSeparador2.setBackground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout jPanelSeparador2Layout = new javax.swing.GroupLayout(jPanelSeparador2);
        jPanelSeparador2.setLayout(jPanelSeparador2Layout);
        jPanelSeparador2Layout.setHorizontalGroup(
            jPanelSeparador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 666, Short.MAX_VALUE)
        );
        jPanelSeparador2Layout.setVerticalGroup(
            jPanelSeparador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 6, Short.MAX_VALUE)
        );

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(35, 35, 38));
        jLabel4.setText("Equipo:");

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(35, 35, 38));
        jLabel5.setText("Marca:");

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(35, 35, 38));
        jLabel6.setText("Modelo:");

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(35, 35, 38));
        jLabel7.setText("Nº de serie:");

        txtSerialNumber.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtSerialNumber.setForeground(new java.awt.Color(65, 65, 63));
        txtSerialNumber.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSerialNumber.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(65, 65, 63)));

        textAreaProblem.setColumns(20);
        textAreaProblem.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        textAreaProblem.setForeground(new java.awt.Color(35, 35, 38));
        textAreaProblem.setRows(5);
        textAreaProblem.setBorder(null);
        jScrollPane1.setViewportView(textAreaProblem);

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(35, 35, 38));
        jLabel8.setText("Descripción del problema:");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

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

        btnRegistrar.setBackground(new java.awt.Color(255, 255, 255));
        btnRegistrar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(101, 129, 171));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/product32.png"))); // NOI18N
        btnRegistrar.setText("Registrar");
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(217, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(217, 217, 217))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        btnNewCustomer.setBackground(new java.awt.Color(255, 255, 255));
        btnNewCustomer.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnNewCustomer.setForeground(new java.awt.Color(255, 153, 0));
        btnNewCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/clientNew32.png"))); // NOI18N
        btnNewCustomer.setBorder(null);
        btnNewCustomer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNewCustomer.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnNewCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNewCustomerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNewCustomerMouseExited(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(35, 35, 38));
        jLabel9.setText("Id cliente:");

        lbl_id.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_id.setForeground(new java.awt.Color(35, 35, 38));
        lbl_id.setText("xxx");

        btnSearch.setBackground(new java.awt.Color(255, 255, 255));
        btnSearch.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnSearch.setForeground(new java.awt.Color(0, 153, 255));
        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/lupa32.png"))); // NOI18N
        btnSearch.setBorder(null);
        btnSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearch.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSearchMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSearchMouseExited(evt);
            }
        });

        btnNewDevice.setBackground(new java.awt.Color(255, 255, 255));
        btnNewDevice.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnNewDevice.setForeground(new java.awt.Color(255, 153, 0));
        btnNewDevice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/newDevice32.png"))); // NOI18N
        btnNewDevice.setBorder(null);
        btnNewDevice.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNewDevice.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnNewDevice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNewDeviceMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNewDeviceMouseExited(evt);
            }
        });

        bntSearchByCustomer.setBackground(new java.awt.Color(255, 255, 255));
        bntSearchByCustomer.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        bntSearchByCustomer.setForeground(new java.awt.Color(0, 153, 255));
        bntSearchByCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/equipClient.png"))); // NOI18N
        bntSearchByCustomer.setBorder(null);
        bntSearchByCustomer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bntSearchByCustomer.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        lblDevice.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblDevice.setForeground(new java.awt.Color(35, 35, 38));
        lblDevice.setText("xxx");

        lblBrand.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblBrand.setForeground(new java.awt.Color(35, 35, 38));
        lblBrand.setText("xxx");

        lblModel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblModel.setForeground(new java.awt.Color(35, 35, 38));
        lblModel.setText("xxx");

        jLabel10.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(35, 35, 38));
        jLabel10.setText("Descripción:");

        lblDescription.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblDescription.setForeground(new java.awt.Color(35, 35, 38));
        lblDescription.setText("xxx");

        jPanelSeparador4.setBackground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout jPanelSeparador4Layout = new javax.swing.GroupLayout(jPanelSeparador4);
        jPanelSeparador4.setLayout(jPanelSeparador4Layout);
        jPanelSeparador4Layout.setHorizontalGroup(
            jPanelSeparador4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelSeparador4Layout.setVerticalGroup(
            jPanelSeparador4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 6, Short.MAX_VALUE)
        );

        jLabel11.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(35, 35, 38));
        jLabel11.setText("S/N:");

        lblSerialNumber.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblSerialNumber.setForeground(new java.awt.Color(35, 35, 38));
        lblSerialNumber.setText("xxx");

        jLabel12.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(35, 35, 38));
        jLabel12.setText("Id dispositivo:");

        lbl_idDevice.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lbl_idDevice.setForeground(new java.awt.Color(35, 35, 38));
        lbl_idDevice.setText("xxx");

        jPanel4.setBackground(new java.awt.Color(102, 102, 102));

        chkboxGarantia.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        chkboxGarantia.setForeground(new java.awt.Color(255, 255, 255));
        chkboxGarantia.setText("En garantia");

        txtParteA.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtParteA.setForeground(new java.awt.Color(35, 35, 38));
        txtParteA.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        btnSearchServiceOrder.setBackground(new java.awt.Color(255, 255, 255));
        btnSearchServiceOrder.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnSearchServiceOrder.setForeground(new java.awt.Color(0, 153, 255));
        btnSearchServiceOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/lupa32.png"))); // NOI18N
        btnSearchServiceOrder.setBorder(null);
        btnSearchServiceOrder.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearchServiceOrder.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnSearchServiceOrder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSearchServiceOrderMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSearchServiceOrderMouseExited(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("-");

        jLabel14.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("ST-");

        txtParteB.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtParteB.setForeground(new java.awt.Color(35, 35, 38));
        txtParteB.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chkboxGarantia)
                .addGap(54, 54, 54)
                .addComponent(jLabel14)
                .addGap(0, 0, 0)
                .addComponent(txtParteA, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel13)
                .addGap(0, 0, 0)
                .addComponent(txtParteB, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearchServiceOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnSearchServiceOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtParteB, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(txtParteA, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(chkboxGarantia))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanelSeparador2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel9)
                                        .addComponent(jLabel3))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lbl_id)
                                        .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnNewCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 5, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblBrand, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblDevice, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblModel, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblSerialNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSerialNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bntSearchByCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnNewDevice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDescription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_idDevice, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8)
                            .addComponent(jScrollPane1)
                            .addComponent(jPanelSeparador4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNewCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(lbl_id))
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelSeparador2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnNewDevice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSerialNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(bntSearchByCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(lbl_idDevice))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblDevice)
                    .addComponent(jLabel11)
                    .addComponent(lblSerialNumber))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(lblBrand)
                    .addComponent(lblModel))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(lblDescription))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelSeparador4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 677, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseEntered
        btnCancel.setBackground(new Color(255,127,39));
    }//GEN-LAST:event_btnCancelMouseEntered

    private void btnCancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseExited
        btnCancel.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCancelMouseExited

    private void btnRegistrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseEntered
        btnRegistrar.setBackground(new Color(255,215,0));
    }//GEN-LAST:event_btnRegistrarMouseEntered

    private void btnRegistrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseExited
        btnRegistrar.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnRegistrarMouseExited

    private void txtPhoneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneKeyPressed
        txtPhone.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtPhone.getText().length() >= 15) {
                    e.consume();
                }
            }
        });        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhoneKeyPressed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnBuscarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarMouseEntered
        btnBuscar.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnBuscarMouseEntered

    private void btnBuscarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarMouseExited
        btnBuscar.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnBuscarMouseExited

    private void btnNewCustomerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewCustomerMouseEntered
        btnNewCustomer.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnNewCustomerMouseEntered

    private void btnNewCustomerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewCustomerMouseExited
        btnNewCustomer.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnNewCustomerMouseExited

    private void btnSearchMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchMouseEntered
        btnSearch.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnSearchMouseEntered

    private void btnSearchMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchMouseExited
        btnSearch.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnSearchMouseExited

    private void btnNewDeviceMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewDeviceMouseEntered
        btnNewDevice.setBackground(new Color(180,180,180));
    }//GEN-LAST:event_btnNewDeviceMouseEntered

    private void btnNewDeviceMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewDeviceMouseExited
        btnNewDevice.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnNewDeviceMouseExited

    private void btnSearchServiceOrderMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchServiceOrderMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSearchServiceOrderMouseEntered

    private void btnSearchServiceOrderMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchServiceOrderMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSearchServiceOrderMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bntSearchByCustomer;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNewCustomer;
    private javax.swing.JButton btnNewDevice;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSearchServiceOrder;
    private javax.swing.JCheckBox chkboxGarantia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelSeparador2;
    private javax.swing.JPanel jPanelSeparador4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBrand;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblDevice;
    private javax.swing.JLabel lblModel;
    private javax.swing.JLabel lblSerialNumber;
    private javax.swing.JLabel lbl_id;
    private javax.swing.JLabel lbl_idDevice;
    private javax.swing.JTextArea textAreaProblem;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtParteA;
    private javax.swing.JTextField txtParteB;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtSerialNumber;
    // End of variables declaration//GEN-END:variables
}
