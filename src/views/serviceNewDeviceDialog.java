/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;

import dao.serviceDAO;
import java.awt.Color;
import java.security.SecureRandom;
import javax.swing.JOptionPane;
import models.mDevice;

public class serviceNewDeviceDialog extends javax.swing.JDialog {
    
    serviceDAO queriesService = new serviceDAO();

    mDevice device = new mDevice();
    
    String id_SerialNumber;
    int id_customer = -1;
    
    public String getSNDevice(){
        return id_SerialNumber;
    }
    
    public void setIdClient(int id_client){
        this.id_customer = id_client;   
    }
    
    public serviceNewDeviceDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        inicializar();
        actions();
    }
    
    private void inicializar(){
        
        txtEquipo.setVisible(false);

        cboEquipo.removeAllItems();
        
        cboEquipo.addItem("Seleccionar");
        cboEquipo.addItem("PC");
        cboEquipo.addItem("Notebook");
        cboEquipo.addItem("Celular");
        cboEquipo.addItem("Tablet");
        cboEquipo.addItem("Impresora");
        cboEquipo.addItem("Monitor");
        cboEquipo.addItem("Otro");   
        
        cboEquipo.setSelectedIndex(0);
        
        btnSerialNumber.setToolTipText("Generar número aleatorio.");
    }
    
    private void actions(){
        
        cboEquipo.addActionListener(e->{
            
            txtEquipo.setText(cboEquipo.getSelectedItem().toString());
            
            if(cboEquipo.getSelectedItem().equals("Otro")){
                txtEquipo.setVisible(true);
                txtEquipo.setText("");
            }else{
                txtEquipo.setVisible(false);
            }
        });
        
        btnExit.addActionListener(e->{
            this.dispose();
        });
        
        btnRegistrar.addActionListener(e->{
            registrarDevice();
        });
        
        btnSerialNumber.addActionListener(e->{
            txtSerialNumber.setText(GeneradorCodigo.generarCodigo(10));
        });
        
    }
    
    public class GeneradorCodigo {

        private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        private static final SecureRandom random = new SecureRandom();

        public static String generarCodigo(int longitud) {
            StringBuilder codigo = new StringBuilder(longitud);

            for (int i = 0; i < longitud; i++) {
                int index = random.nextInt(CARACTERES.length());
                codigo.append(CARACTERES.charAt(index));
            }

            return codigo.toString();
        }
    }

    private void registrarDevice(){
    
        boolean valido = true;
        
        device.setId_customer(id_customer);
                
        if(!txtEquipo.getText().isEmpty()){
            device.setDevice_type(txtEquipo.getText().toString().toUpperCase());
        }else{
            JOptionPane.showMessageDialog(null, "Debe ingresar el tipo de equipo");
            valido = false;
        }
            
        if(!txtBrand.getText().isEmpty()){
            device.setBrand(txtBrand.getText().toString().trim().toUpperCase());
        }else{
            device.setBrand(null);
        }

        if(!txtModel.getText().isEmpty()){
            device.setModel(txtModel.getText().toString().trim().toUpperCase());
        }else{
            device.setModel(null);
        }

        if(!txtSerialNumber.getText().isEmpty()){
            
            String serialNumber = txtSerialNumber.getText().toString().trim().toUpperCase();
            boolean existe = queriesService.serialNumberExists(serialNumber);

            if(!existe){
                device.setSerial_number(serialNumber);
            }else{
                JOptionPane.showMessageDialog(null, "El número de serie ya está registrado");
                valido = false;
            }
            
        }else{
            JOptionPane.showMessageDialog(null, "Debe ingresar un número de serie");
            valido = false;
        }

        if(!textAreaDescripcion.getText().isEmpty()){
            device.setDescription(textAreaDescripcion.getText().toString().toUpperCase());
        }else{
            device.setDescription(null);
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
        
        queriesService.insertDevice(
                device.getId_customer(),
                device.getDevice_type(), 
                device.getBrand(), 
                device.getModel(), 
                device.getSerial_number(), 
                device.getDescription()
        );
        
        id_SerialNumber = device.getSerial_number();
        
        this.dispose();           
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtEquipo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaDescripcion = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        cboEquipo = new javax.swing.JComboBox<>();
        txtBrand = new javax.swing.JTextField();
        txtModel = new javax.swing.JTextField();
        txtSerialNumber = new javax.swing.JTextField();
        btnSerialNumber = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión - Registrar dispositivo");

        jPanel1.setBackground(new java.awt.Color(245, 248, 255));

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(12, 83, 151));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/dispositivos64.png"))); // NOI18N
        jLabel8.setText("Registrar dispositivo.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(35, 35, 38));
        jLabel7.setText("Nº de serie:");

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(35, 35, 38));
        jLabel4.setText("Equipo:");

        txtEquipo.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txtEquipo.setForeground(new java.awt.Color(35, 35, 38));
        txtEquipo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtEquipo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(35, 35, 38));
        jLabel5.setText("Marca:");

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(35, 35, 38));
        jLabel6.setText("Modelo:");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit-32.png"))); // NOI18N

        btnRegistrar.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(12, 83, 151));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ok32.png"))); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        textAreaDescripcion.setColumns(20);
        textAreaDescripcion.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        textAreaDescripcion.setForeground(new java.awt.Color(65, 65, 63));
        textAreaDescripcion.setRows(5);
        jScrollPane1.setViewportView(textAreaDescripcion);

        jLabel9.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(35, 35, 38));
        jLabel9.setText("Descripción:");

        cboEquipo.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        cboEquipo.setForeground(new java.awt.Color(35, 35, 38));
        cboEquipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        txtBrand.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtBrand.setForeground(new java.awt.Color(35, 35, 38));

        txtModel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtModel.setForeground(new java.awt.Color(35, 35, 38));

        txtSerialNumber.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtSerialNumber.setForeground(new java.awt.Color(35, 35, 38));

        btnSerialNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/dado32.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane1)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5)
                                .addComponent(jLabel4))
                            .addGap(34, 34, 34)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(cboEquipo, 0, 210, Short.MAX_VALUE)
                                .addComponent(txtBrand))
                            .addGap(30, 30, 30)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtModel, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(txtEquipo, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSerialNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSerialNumber))
                    .addComponent(jLabel9))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4)
                    .addComponent(cboEquipo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEquipo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5)
                    .addComponent(txtBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtModel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(txtSerialNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnSerialNumber))
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                customerCambioEstadoDialog dialog = new customerCambioEstadoDialog(new javax.swing.JFrame(),true);
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
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnSerialNumber;
    private javax.swing.JComboBox<String> cboEquipo;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea textAreaDescripcion;
    private javax.swing.JTextField txtBrand;
    private javax.swing.JTextField txtEquipo;
    private javax.swing.JTextField txtModel;
    private javax.swing.JTextField txtSerialNumber;
    // End of variables declaration//GEN-END:variables
}
