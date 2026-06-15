/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package views;

import dao.customerDAO;
import dao.devicesDAO;
import dao.serviceDAO;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import models.Device;
import models.Service;

public class serviceNewPanel extends javax.swing.JPanel {

    customerDAO qCustomer = new customerDAO();
    devicesDAO qDevices = new devicesDAO();
    serviceDAO qService = new serviceDAO();
    
    Device device = new Device();
    Service service = new Service();
    
    int id_service = -1;
    String cuitClient = "";
    String snDevice = "";

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
        btnSearchDeviceSN.setToolTipText("Buscar por número de serie");
        bntDeviceCustomer.setToolTipText("Seleccioná un dispositivo de la lista");
        btnNewDevice.setToolTipText("Registrar dispositivo");
    }
    
    private void inicializar(){
        
        lbl_id.setVisible(false);
        
        txtName.setEditable(false);
        txtPhone.setEditable(false);
        
        lbl_id.setText("0");       
        lblDevice.setText("");
        lblBrand.setText("");
        lblModel.setText("");
        lblDescription.setText("");
        lbl_idDevice.setText("");
        lblSerialNumber.setText("");
        lbl_address.setText("");
        
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
                qCustomer.selectCustomerSimplified(cuitClient, lbl_id,txtName, txtPhone, lbl_address);
            }
        });
        
        bntDeviceCustomer.addActionListener(e->{
            
//            JTable tabla = new JTable();
            boolean deviceExist = false;
            
            int idClient = Integer.parseInt(lbl_id.getText().trim());   
            
            if(idClient > 0){   
                         
                deviceExist = qDevices.deviceExist(idClient);
                
                if(deviceExist){
                    
                    customerDevicesDialog pCustDev = new customerDevicesDialog(parent, true);   
                    
                    pCustDev.setIdCustomer(idClient);
                    
                    pCustDev.setVisible(true); 
                    
                    snDevice = pCustDev.getSerialNumberDevice();
                
                    if(!snDevice.isEmpty()){
                        buscarDevice();
                    }
                    
                }else{
                    JOptionPane.showMessageDialog(null, "No hay dispositivos vinculados");
                }
                
            }else{
                JOptionPane.showMessageDialog(null, "Debe seleccionar un cliente.");
            }
        });
        
        btnNewCustomer.addActionListener(e->{
            
            customerNewDialog pNewClient = new customerNewDialog(parent, true);           
            pNewClient.setVisible(true);
            cuitClient = pNewClient.getCuitClient();
            
            qCustomer.selectCustomerSimplified(cuitClient, lbl_id, txtName, txtPhone, lbl_address);
        });
        
        btnCancel.addActionListener(e->{
            
            limpiar();
        });
        
        btnNewDevice.addActionListener(e->{
            
            int idClient = Integer.parseInt(lbl_id.getText().trim());
            
            if(idClient > 0){
                serviceNewDeviceDialog pNewDevice = new serviceNewDeviceDialog(parent, true);   
                
                pNewDevice.setIdClient(idClient);
                
                pNewDevice.setVisible(true);
                
                snDevice = pNewDevice.getSNDevice();
                
                if(!snDevice.isEmpty()){
                    buscarDevice();
                }
                
            }else{
                JOptionPane.showMessageDialog(null, "Debe seleccionar un cliente.");
            }                  
        });
        
        btnSearchDeviceSN.addActionListener(e->{
            
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
                btnSearchDeviceSN.setEnabled(false);
                bntDeviceCustomer.setEnabled(false);
                btnNewDevice.setEnabled(false);
                
            }else{
                jLabel14.setEnabled(false);
                txtParteA.setEnabled(false);
                txtParteB.setEnabled(false);
                btnSearchServiceOrder.setEnabled(false);
                
                btnBuscar.setEnabled(true);
                btnNewCustomer.setEnabled(true);
                txtSerialNumber.setEnabled(true);
                btnSearchDeviceSN.setEnabled(true);
                bntDeviceCustomer.setEnabled(true);
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

        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jPanelSeparador2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnRegistrar = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lbl_id = new javax.swing.JLabel();
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
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        chkboxGarantia = new javax.swing.JCheckBox();
        txtParteA = new javax.swing.JTextField();
        txtParteB = new javax.swing.JTextField();
        btnSearchServiceOrder = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        lbl_address = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        btnNewCustomer = new javax.swing.JButton();
        txtSerialNumber = new javax.swing.JTextField();
        btnSearchDeviceSN = new javax.swing.JButton();
        bntDeviceCustomer = new javax.swing.JButton();
        btnNewDevice = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        textAreaProblem = new javax.swing.JTextArea();

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(35, 35, 38));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ordenador-personal64.png"))); // NOI18N
        jLabel1.setText("Recepción de Equipo para Servicio Técnico");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(78, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        txtName.setEditable(false);
        txtName.setBackground(new java.awt.Color(255, 255, 255));
        txtName.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtName.setForeground(new java.awt.Color(35, 35, 38));
        txtName.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtName.setBorder(null);

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(35, 35, 38));
        jLabel2.setText("Nombre y apellido:");

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(35, 35, 38));
        jLabel3.setText("Teléfono:");

        txtPhone.setEditable(false);
        txtPhone.setBackground(new java.awt.Color(255, 255, 255));
        txtPhone.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtPhone.setForeground(new java.awt.Color(35, 35, 38));
        txtPhone.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtPhone.setBorder(null);
        txtPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPhoneKeyPressed(evt);
            }
        });

        jPanelSeparador2.setBackground(new java.awt.Color(245, 248, 255));

        javax.swing.GroupLayout jPanelSeparador2Layout = new javax.swing.GroupLayout(jPanelSeparador2);
        jPanelSeparador2.setLayout(jPanelSeparador2Layout);
        jPanelSeparador2Layout.setHorizontalGroup(
            jPanelSeparador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 666, Short.MAX_VALUE)
        );
        jPanelSeparador2Layout.setVerticalGroup(
            jPanelSeparador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
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

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(35, 35, 38));
        jLabel8.setText("Descripción del problema:");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        btnRegistrar.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(12, 83, 151));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseExited(evt);
            }
        });

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/borrador32.png"))); // NOI18N
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        lbl_id.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_id.setForeground(new java.awt.Color(35, 35, 38));
        lbl_id.setText("xxx");

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

        jPanelSeparador4.setBackground(new java.awt.Color(245, 248, 255));

        javax.swing.GroupLayout jPanelSeparador4Layout = new javax.swing.GroupLayout(jPanelSeparador4);
        jPanelSeparador4.setLayout(jPanelSeparador4Layout);
        jPanelSeparador4Layout.setHorizontalGroup(
            jPanelSeparador4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 666, Short.MAX_VALUE)
        );
        jPanelSeparador4Layout.setVerticalGroup(
            jPanelSeparador4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
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

        jPanel4.setBackground(new java.awt.Color(245, 248, 255));

        jLabel13.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(12, 83, 151));
        jLabel13.setText("-");

        jLabel14.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(12, 83, 151));
        jLabel14.setText("ST-");

        chkboxGarantia.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        chkboxGarantia.setForeground(new java.awt.Color(12, 83, 151));
        chkboxGarantia.setText("En garantia");

        txtParteA.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtParteA.setForeground(new java.awt.Color(35, 35, 38));

        txtParteB.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtParteB.setForeground(new java.awt.Color(35, 35, 38));

        btnSearchServiceOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/search32.png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chkboxGarantia)
                .addGap(99, 99, 99)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtParteA, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel13)
                .addGap(4, 4, 4)
                .addComponent(txtParteB, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearchServiceOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(chkboxGarantia)
                    .addComponent(txtParteA, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtParteB, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchServiceOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel15.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(35, 35, 38));
        jLabel15.setText("Dirección:");

        lbl_address.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        lbl_address.setForeground(new java.awt.Color(35, 35, 38));
        lbl_address.setText("xxx");

        btnBuscar.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(12, 83, 151));
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/clientView32.png"))); // NOI18N
        btnBuscar.setText("Buscar cliente");

        btnNewCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/clientNew32.png"))); // NOI18N

        txtSerialNumber.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtSerialNumber.setForeground(new java.awt.Color(35, 35, 38));
        txtSerialNumber.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        btnSearchDeviceSN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/searchBarCode32.png"))); // NOI18N

        bntDeviceCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/device32_1.png"))); // NOI18N

        btnNewDevice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/deviceAdd32.png"))); // NOI18N

        textAreaProblem.setColumns(20);
        textAreaProblem.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        textAreaProblem.setForeground(new java.awt.Color(35, 35, 38));
        textAreaProblem.setRows(5);
        jScrollPane2.setViewportView(textAreaProblem);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                                .addComponent(txtSerialNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSearchDeviceSN, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bntDeviceCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelSeparador4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jPanelSeparador2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel15)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lbl_address)
                                    .addGap(530, 530, 530))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                    .addComponent(btnBuscar)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnNewCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lbl_id)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addGap(8, 8, 8)
                                .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 6, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_id)
                    .addComponent(btnNewCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(lbl_address))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelSeparador2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel7)
                    .addComponent(txtSerialNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchDeviceSN, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bntDeviceCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNewDevice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 64, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 707, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtPhoneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneKeyPressed
        txtPhone.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || txtPhone.getText().length() >= 15) {
                    e.consume();
                }
            }
        });  
    }//GEN-LAST:event_txtPhoneKeyPressed

    private void btnRegistrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseEntered
        btnRegistrar.setBackground(new Color(255,215,0));
        btnRegistrar.setForeground(new Color(35, 35, 38));
    }//GEN-LAST:event_btnRegistrarMouseEntered

    private void btnRegistrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseExited
        btnRegistrar.setBackground(new Color(255,255,255));
        btnRegistrar.setForeground(new Color(12,83,151));
    }//GEN-LAST:event_btnRegistrarMouseExited

    private void btnCancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseEntered
        btnCancel.setBackground(new Color(255,127,39));
    }//GEN-LAST:event_btnCancelMouseEntered

    private void btnCancelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseExited
        btnCancel.setBackground(new Color(255,255,255));
    }//GEN-LAST:event_btnCancelMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bntDeviceCustomer;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNewCustomer;
    private javax.swing.JButton btnNewDevice;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnSearchDeviceSN;
    private javax.swing.JButton btnSearchServiceOrder;
    private javax.swing.JCheckBox chkboxGarantia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelSeparador2;
    private javax.swing.JPanel jPanelSeparador4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblBrand;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblDevice;
    private javax.swing.JLabel lblModel;
    private javax.swing.JLabel lblSerialNumber;
    private javax.swing.JLabel lbl_address;
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
