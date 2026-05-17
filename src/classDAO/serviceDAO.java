/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classDAO;

import ConnectionDB.connectionDB;
import java.awt.Color;
import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import utils.config;

public class serviceDAO {
    
    private Connection getConnection() {
        connectionDB con = new connectionDB();
        return con.establecerConexion();
    }
    
    private DefaultTableModel crearModeloNoEditable() {
        return new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
    
    public void insertDevice(int id_customer, String deviceType, String brand, String model, String serialNumber, String description){
        
        String sql="INSERT INTO `devices`(`id_customer`, `device_type`, `brand`, `model`, `serial_number`, `description`) VALUES (?,?,?,?,?,?)";
               
        Connection conexion = getConnection();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);           
            pstmt.setInt(1, id_customer);
            pstmt.setString(2, deviceType);
            pstmt.setString(3, brand);
            pstmt.setString(4, model);
            pstmt.setString(5, serialNumber);
            pstmt.setString(6, description);           
            pstmt.executeUpdate();   
            
            conexion.close(); 
            pstmt.close();          
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR DISPOSITIVO" + e.getMessage());
        }    
    }
    
    //VERIFICA SI EL NUMERO DE SERIE EXISTE.
    public boolean serialNumberExists(String serialNumber) {
        
        String sql = "SELECT * FROM devices WHERE serial_number = ?";
        
        Connection conexion = getConnection();
        
        try {         
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);          
            pstmt.setString(1, serialNumber);       
            ResultSet rs = pstmt.executeQuery();
            
            boolean existe = rs.next();
        
            rs.close();
            pstmt.close();
            conexion.close();
        
            return existe;

        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
    
    public void selectDevice(
        String serialNumber,
        JLabel lbl_idDevice,
        JLabel lblSerialNumber,
        JLabel lblDevice,
        JLabel lblBrand,
        JLabel lblModel,
        JLabel lblDescription
        ){  
          
        String sql = 
                "SELECT " +
                "id_device, " +
                "device_type, " +
                "COALESCE(brand, 'Sin dato') AS brand, " +
                "COALESCE(model, 'Sin dato') AS model, " +
                "COALESCE(description, 'Sin dato') AS description " +
                "FROM `devices` " +
                "WHERE `serial_number`=?";
                    
        Connection conexion = getConnection();
                  
        try{            
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setString(1, serialNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                lblSerialNumber.setText(serialNumber); 
                lbl_idDevice.setText(rs.getString("id_device"));
                lblDevice.setText(rs.getString("device_type"));               
                lblBrand.setText(rs.getString("brand"));
                lblModel.setText(rs.getString("model"));
                lblDescription.setText(rs.getString("description"));


            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el dispositivo");
            }
            rs.close();
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR " + e.getMessage());
        } 
    }  

    public int insertService(
            int id_customer,
            int id_device,
            String reported_problem,
            int id_status,
            int garantia
    ){    

        int idGenerado = 0;
        LocalDate fechaLocal = LocalDate.now();

        String sqlInsert = "INSERT INTO service_orders " +
                           "(id_customer, id_device, reported_problem, id_status, entry_date, garantia) " +
                           "VALUES (?,?,?,?,?,?)";

        String sqlUpdate = "UPDATE service_orders SET service_number = ? WHERE id_service = ?";

        Connection conexion = getConnection();

        try{
            conexion.setAutoCommit(false);
            PreparedStatement pstmtInsert = conexion.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);

            pstmtInsert.setInt(1, id_customer);
            pstmtInsert.setInt(2, id_device);
            pstmtInsert.setString(3, reported_problem);
            pstmtInsert.setInt(4, id_status);
            pstmtInsert.setDate(5, java.sql.Date.valueOf(fechaLocal));
            pstmtInsert.setInt(6,garantia);
            
            pstmtInsert.executeUpdate();            

            ResultSet rs = pstmtInsert.getGeneratedKeys();

            if(rs.next()){
                idGenerado = rs.getInt(1);
            }

            rs.close();
            pstmtInsert.close();

            if(idGenerado > 0){

                int year = java.time.Year.now().getValue();

                String serviceNumber = "ST-" + year + "-" + String.format("%06d", idGenerado);

                PreparedStatement pstmtUpdate = conexion.prepareStatement(sqlUpdate);

                pstmtUpdate.setString(1, serviceNumber);
                pstmtUpdate.setInt(2, idGenerado);

                pstmtUpdate.executeUpdate();
                pstmtUpdate.close();
            }

            conexion.commit();
            conexion.close();

        } catch(SQLException e){
            try {
                conexion.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("ERROR AL REGISTRAR ORDEN: " + e.getMessage());
        }

        return idGenerado;
    }
    
    public void insertService_order_status_history(
            int id_service,
            int id_status
        ){    
    
        String sql = "INSERT INTO `service_order_status_history`(`id_service`, `id_status`, `date`) VALUES (?,?,?)";       
        
        LocalDate fechaLocal = LocalDate.now();
        
        Connection conexion = getConnection();

        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);            
            pstmt.setInt(1, id_service);
            pstmt.setInt(2, id_status);
            pstmt.setDate(3, java.sql.Date.valueOf(fechaLocal));            
            pstmt.executeUpdate();  
            
            conexion.close(); 
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR Service_order_status_history" + e.getMessage());
        }
    }   
    
    public void listServices(JTable jtable){

        String sql = "SELECT " +
                "so.entry_date, " +
                "so.service_number, " +
                "COALESCE(so.repair_date, '') AS repair_date, " +
                "c.name AS customer_name, " +
                "d.device_type, " +
                "COALESCE(d.brand, 'Sin dato') AS brand, " +
                "COALESCE(d.model, 'Sin dato') AS model, " +
                "COALESCE(d.description, 'Sin dato') AS description, " +
                "d.serial_number, " +
                "s.name AS status_name " +
                "FROM service_orders so " +
                "INNER JOIN customer c ON so.id_customer = c.id_customer " +
                "INNER JOIN devices d ON so.id_device = d.id_device " +
                "INNER JOIN services_states s ON so.id_status = s.id_states " +
                "WHERE so.id_status BETWEEN 1 AND 7 " +
                "ORDER BY so.entry_date DESC;";
        
        Statement stmt;

        DefaultTableModel dtm = crearModeloNoEditable();

        Connection conexion = getConnection();

        String[] titleTable = {"Fecha de ingreso","Nº de servicio","Cliente","Dispositivo","Marca","Modelo","Nº de serie","Descripcion","Estado","Fecha de reparación"};
        dtm.setColumnIdentifiers(titleTable);

        try{
            stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {

                Object[] row = {
                    rs.getString("entry_date"),
                    rs.getString("service_number"),
                    rs.getString("customer_name"),
                    rs.getString("device_type"),
                    rs.getString("brand"),       
                    rs.getString("model"),       
                    rs.getString("serial_number"),
                    rs.getString("description"),
                    rs.getString("status_name"),
                    rs.getString("repair_date")   
                };

                dtm.addRow(row);
            }
            jtable.setModel(dtm);
            
            config.TableStyleUtil.applyPoppinsHeader(jtable);
            
            jtable.setDefaultRenderer(Object.class, new StatusColorRenderer());

            rs.close();
            stmt.close();
            conexion.close();

        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
    }

    public void listServicesForState(JTable jtable, int id_status){

        String sql = "SELECT " +
                "so.entry_date, " +
                "so.service_number, " +
                "COALESCE(so.repair_date, '') AS repair_date, " +
                "c.name AS customer_name, " +
                "d.device_type, " +
                "COALESCE(d.brand, 'Sin dato') AS brand, " +
                "COALESCE(d.model, 'Sin dato') AS model, " +
                "COALESCE(d.description, 'Sin dato') AS description, " +
                "d.serial_number, " +
                "s.name AS status_name " +
                "FROM service_orders so " +
                "INNER JOIN customer c ON so.id_customer = c.id_customer " +
                "INNER JOIN devices d ON so.id_device = d.id_device " +
                "INNER JOIN services_states s ON so.id_status = s.id_states " +
                "WHERE so.id_status = ?" +
                "ORDER BY so.entry_date DESC;";

        DefaultTableModel dtm = crearModeloNoEditable();

        Connection conexion = getConnection();

        String[] titleTable = {"Fecha de ingreso","Nº de servicio","Cliente","Dispositivo","Marca","Modelo","Nº de serie","Descripcion","Estado","Fecha de reparación"};
        dtm.setColumnIdentifiers(titleTable);

        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            pstmt.setInt(1, id_status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                Object[] row = {
                    rs.getString("entry_date"),
                    rs.getString("service_number"),
                    rs.getString("customer_name"),
                    rs.getString("device_type"),
                    rs.getString("brand"),       
                    rs.getString("model"),       
                    rs.getString("serial_number"),
                    rs.getString("description"),
                    rs.getString("status_name"),
                    rs.getString("repair_date")   
                };

                dtm.addRow(row);
            }
            jtable.setModel(dtm);
            
            config.TableStyleUtil.applyPoppinsHeader(jtable);
            
            jtable.setDefaultRenderer(Object.class, new StatusColorRenderer());

            rs.close();
            conexion.close();

        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
    }    
    
    public class StatusColorRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String status = table.getValueAt(row, 8).toString().toUpperCase();

            if (!isSelected) {

            switch (status) {

                case "INGRESADO":
                    c.setBackground(Color.decode("#F5F5F5")); // gris claro (neutro)
                    break;

                case "DIAGNOSTICADO":
                    c.setBackground(Color.decode("#BBDEFB")); // azul claro
                    break;

                case "ESPERANDO APROBACION":
                    c.setBackground(Color.decode("#FFE0B2")); // naranja suave
                    break;

                case "PRESUPUESTO APROBADO":
                    c.setBackground(Color.decode("#C8E6C9")); // verde claro
                    break;

                case "PRESUPUESTO RECHAZADO":
                    c.setBackground(Color.decode("#FFCDD2")); // rojo claro
                    break;

                case "NO REPARADO":
                    c.setBackground(Color.decode("#EF9A9A")); // rojo más fuerte
                    break;

                case "REPARADO":
                    c.setBackground(Color.decode("#A5D6A7")); // verde
                    break;

                case "ENTREGADO":
                    c.setBackground(Color.decode("#D1C4E9")); // violeta suave (cerrado)
                    break;

                default:
                    c.setBackground(Color.WHITE);
            }

            c.setForeground(Color.BLACK);
        }

            return c;
        }
    }
    
    public void selectService(
            String serviceNumber,
            JLabel lbl_state,
            JLabel lbl_id_service,
            JLabel lbl_serviceNumber,
            JLabel lbl_date,
            JLabel lbl_name,
            JLabel lbl_phone,
            JLabel lbl_deviceType,
            JLabel lbl_brand,  
            JLabel lbl_Model,
            JLabel lbl_sn,
            JTextArea jTextAreaDescription,
            JTextArea jTextAreaFalla,
            JTextArea jTextAreaDiagnostico,
            JTextField txtCost
        ){  

        String sql = "SELECT " +
                "c.name AS customer_name, " +
                "c.phone, " +
                "d.device_type, " +
                "COALESCE(d.brand, 'Sin dato') AS brand, " +
                "COALESCE(d.model, 'Sin dato') AS model, " +
                "d.serial_number, " +
                "COALESCE(d.description, 'Sin dato') AS description, " +
                "so.id_service, " +
                "so.reported_problem, " +
                "so.entry_date, " +
                "COALESCE(so.diagnosis, '') AS diagnosis, " +
                "COALESCE(so.cost, '') AS cost, " +
                "s.name AS status_name " +
                "FROM service_orders so " +
                "INNER JOIN customer c ON so.id_customer = c.id_customer " +
                "INNER JOIN devices d ON so.id_device = d.id_device " +
                "INNER JOIN services_states s ON so.id_status = s.id_states " +
                "WHERE so.service_number = ?";

        Connection conexion = getConnection();

        try{            
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setString(1, serviceNumber);

            ResultSet rs = pstmt.executeQuery();     

            if (rs.next()) {

                lbl_state.setText(rs.getString("status_name"));
                lbl_id_service.setText(rs.getString("id_service"));
                lbl_serviceNumber.setText(serviceNumber);               
                lbl_date.setText(rs.getString("entry_date"));
                lbl_name.setText(rs.getString("customer_name"));
                lbl_phone.setText(rs.getString("phone"));
                lbl_deviceType.setText(rs.getString("device_type"));
                lbl_brand.setText(rs.getString("brand"));
                lbl_Model.setText(rs.getString("model"));
                lbl_sn.setText(rs.getString("serial_number"));
                jTextAreaDescription.setText(rs.getString("description"));
                jTextAreaFalla.setText(rs.getString("reported_problem"));
                jTextAreaDiagnostico.setText(rs.getString("diagnosis"));
                txtCost.setText(rs.getString("cost"));

            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el servicio");
            }

            rs.close();
            pstmt.close();
            conexion.close();

        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR " + e.getMessage());
        } 
    } 
    
    public boolean updateService(String serviceNumber, int id_status, String diagnosis , Double cost){
        
        String sql = "UPDATE service_orders SET diagnosis = ?, id_status = ?, cost = ?, repair_date = ? WHERE service_number = ?";
        
        boolean estado = false;
        
        LocalDate fechaLocal = LocalDate.now();
               
        Connection conexion = getConnection();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            
            pstmt.setString(1, diagnosis);
            pstmt.setInt(2, id_status);
            
            if (cost == null) {
                pstmt.setNull(3, java.sql.Types.DOUBLE);
            } else {
                pstmt.setDouble(3, cost);
            }
            
            pstmt.setDate(4, java.sql.Date.valueOf(fechaLocal));
            pstmt.setString(5, serviceNumber);
            
            pstmt.executeUpdate();
            
            conexion.close();
                
            estado = true;
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL ACTUALIZAR ESTADO DE SERVICIO TÉCNICO. " + e.getMessage());
        } 
        return estado;
    } 
    
    public boolean updateServiceDespachar(String serviceNumber, String delivery_date){
        
        String sql = "UPDATE `service_orders` SET `id_status`=?,`delivery_date`=? WHERE `service_number`=?";
        
        boolean estado = false;
        int id_state = 7;
               
        Connection conexion = getConnection();
       
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);
            
            pstmt.setInt(1, id_state);
            pstmt.setString(2, delivery_date);
            pstmt.setString(3, serviceNumber);
            
            pstmt.executeUpdate();
            
            conexion.close();
            
            JOptionPane.showMessageDialog(null, "Equipo entregado!.");
            
            estado = true;
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR -> " + e.getMessage());
        } 
        return estado;
    } 
    
    public void selectPrintServiceOrder(
            int id_service,
            JLabel lbl_serviceNumber,
            JLabel lbl_date,
            JLabel lbl_customerId,
            JLabel lbl_name,
            JLabel lbl_phone,
            JLabel lbl_deviceType,
            JLabel lbl_brand,  
            JLabel lbl_Model,
            JLabel lbl_sn,
            JLabel lblDeviceDescription,
            JTextArea jTextAreaReportedProblem
    ){
        String sql = "SELECT " +
                    "so.service_number, " +
                    "c.name AS customer_name, " +
                    "c.id_customer, " +
                    "c.phone, " +
                    "d.device_type, " +
                    "COALESCE(d.brand, 'Sin dato') AS brand, " +
                    "COALESCE(d.model, 'Sin dato') AS model, " +
                    "d.serial_number, " +
                    "COALESCE(d.description, 'Sin dato') AS description, " +
                    "so.reported_problem, " +
                    "so.entry_date " +
                    "FROM service_orders so " +
                    "INNER JOIN customer c ON so.id_customer = c.id_customer " +
                    "INNER JOIN devices d ON so.id_device = d.id_device " +
                    "WHERE so.id_service = ?";
        
        Connection conexion = getConnection();
        
        try{            
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setInt(1, id_service);

            ResultSet rs = pstmt.executeQuery();     

            if (rs.next()) {

                lbl_serviceNumber.setText(rs.getString("service_number"));
                lbl_date.setText(rs.getString("entry_date"));
                lbl_customerId.setText(rs.getString("id_customer"));                
                lbl_name.setText(rs.getString("customer_name"));
                lbl_phone.setText(rs.getString("phone"));
                lbl_deviceType.setText(rs.getString("device_type"));
                lbl_brand.setText(rs.getString("brand"));
                lbl_Model.setText(rs.getString("model"));
                lbl_sn.setText(rs.getString("serial_number"));
                lblDeviceDescription.setText(rs.getString("description"));
                jTextAreaReportedProblem.setText(rs.getString("reported_problem"));

            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el servicio");
            }

            rs.close();
            pstmt.close();
            conexion.close();

        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR " + e.getMessage());
        }
        
    }
    
    public void mostrarDashboard(
            JLabel lbl_ingrsado,
            JLabel lbl_diagnosticado,
            JLabel lbl_esperando,
            JLabel lbl_aprobado,
            JLabel lbl_rechazado,
            JLabel lbl_no_reparado,
            JLabel lbl_reparado
            ){
        
        String sql = "SELECT " +
                    "SUM(CASE WHEN id_status = 1 THEN 1 ELSE 0 END) AS ingresado, " +
                    "SUM(CASE WHEN id_status = 2 THEN 1 ELSE 0 END) AS diagnosticado, " +
                    "SUM(CASE WHEN id_status = 3 THEN 1 ELSE 0 END) AS esperando_aprobacion, " +
                    "SUM(CASE WHEN id_status = 4 THEN 1 ELSE 0 END) AS aprobado, " +
                    "SUM(CASE WHEN id_status = 5 THEN 1 ELSE 0 END) AS rechazado, " +
                    "SUM(CASE WHEN id_status = 6 THEN 1 ELSE 0 END) AS no_reparado, " +
                    "SUM(CASE WHEN id_status = 7 THEN 1 ELSE 0 END) AS reparado " +
                    "FROM service_orders";
        
        Connection conexion = getConnection();
        
        try{
            PreparedStatement pstm = conexion.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
           
            if (rs.next()) {

                lbl_ingrsado.setText(rs.getString("ingresado"));
                lbl_diagnosticado.setText(rs.getString("diagnosticado"));
                lbl_esperando.setText(rs.getString("esperando_aprobacion"));
                lbl_aprobado.setText(rs.getString("aprobado"));
                lbl_rechazado.setText(rs.getString("rechazado"));
                lbl_no_reparado.setText(rs.getString("no_reparado"));
                lbl_reparado.setText(rs.getString("reparado"));

            } else {
                JOptionPane.showMessageDialog(null, "Error");
            } 
          
            
            rs.close();
            pstm.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR" + e.getMessage());
        }
    }
    
    public void searchServiceOrder(
            String serviceNumber,
            JLabel lbl_id,
            JTextField txtName,
            JTextField txtPhone,
            JLabel lbl_idDevice,
            JLabel lblDevice,
            JLabel lblSerialNumber,
            JLabel lblBrand,
            JLabel lblModel,
            JLabel lblDescription,
            JTextArea textAreaProblem
            ){
        
        String sql= "SELECT " +
                    "so.id_customer AS id_customer, " +
                    "c.name AS client, " +
                    "c.phone AS phone, " +
                    "so.id_device AS idDev, " +
                    "d.device_type AS type, " +
                    "d.brand AS brand, " +
                    "d.serial_number AS serNum, " +
                    "COALESCE(d.model, 'Sin dato') AS model, " +
                    "d.description AS description, " +
                    "so.reported_problem AS reported_problem, " +
                    "COALESCE(so.diagnosis, 'SIN DATO') AS diagnosis " +
                    "FROM service_orders AS so " +
                    "INNER JOIN customer c ON so.id_customer = c.id_customer " +
                    "INNER JOIN devices d ON so.id_device = d.id_device " +
                    "WHERE so.service_number = ? AND so.id_status = 8";
        
        
        Connection conexion = getConnection();
            
        try{
            PreparedStatement pstm = conexion.prepareStatement(sql);
            pstm.setString(1, serviceNumber);

            ResultSet rs = pstm.executeQuery();
            
            if (rs.next()) {
                
                String problema = rs.getString("reported_problem");
                String diagnostico = rs.getString("diagnosis");
                String texto = "GARANTIA! - PROBLEMA REPORTADO: " + problema + " - DIAGNOSTICO: " + diagnostico;

                lbl_id.setText(rs.getString("id_customer"));
                txtName.setText(rs.getString("client"));
                txtPhone.setText(rs.getString("phone"));
                lbl_idDevice.setText(rs.getString("idDev"));
                lblDevice.setText(rs.getString("type"));
                lblSerialNumber.setText(rs.getString("serNum"));
                lblBrand.setText(rs.getString("brand"));
                lblModel.setText(rs.getString("model"));
                lblDescription.setText(rs.getString("description"));
                textAreaProblem.setText(texto);


            } else {
                JOptionPane.showMessageDialog(null, "No se encontró servicio técnico");
            }

            rs.close();
            pstm.close();
            conexion.close();
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR" + e.getMessage());
        }
        
    }
    
    public boolean serviceHistory(int id_customer, JTable tabla){
        
        String sql= "SELECT " +
                    "so.id_service AS idService, " +
                    "so.service_number AS service, " +
                    "d.device_type AS dType, " +
                    "COALESCE(d.brand, 'Sin dato') AS brand, " +
                    "COALESCE(d.model, 'Sin dato') AS model, " +
                    "d.serial_number AS serialNumber, " +
                    "ss.name AS estado, " +
                    "so.entry_date AS fechaIngreso, " +
                    "so.delivery_date AS fechaEgreso " +
                    "FROM service_orders so " +
                    "INNER JOIN devices d ON so.id_device = d.id_device " +
                    "INNER JOIN services_states ss ON so.id_status = ss.id_states " +
                    "WHERE so.id_customer = ? " +
                    "ORDER BY so.entry_date DESC";
        
        boolean status = false;
        
        Connection conexion = getConnection();
        
        DefaultTableModel dtm = crearModeloNoEditable();
        
        String[] titulo = {"idService","Servicio","Estado","Dispositivo", "S/N", "Ingrso", "Egreso"};
        dtm.setColumnIdentifiers(titulo);
        
        try{
            PreparedStatement pstm = conexion.prepareStatement(sql);
            pstm.setInt(1, id_customer);
            ResultSet rs = pstm.executeQuery();
            
            while (rs.next()) {
                
                String tipo = rs.getString("dType");
                String marca = rs.getString("brand");
                String modelo = rs.getString("model");              
                String dispositivo = tipo + " " + marca + " " + modelo;
                
                Object[] row = {
                    rs.getString("idService"),
                    rs.getString("service"),
                    rs.getString("estado"),
                    dispositivo,
                    rs.getString("serialNumber"),
                    rs.getString("fechaIngreso"),
                    rs.getString("fechaEgreso")

                };
                dtm.addRow(row); 
                status = true;
            }
            tabla.setModel(dtm);
            
            config.TableStyleUtil.applyPoppinsHeader(tabla);
            
            tabla.getColumnModel().getColumn(0).setMinWidth(0);
            tabla.getColumnModel().getColumn(0).setMaxWidth(0);
            tabla.getColumnModel().getColumn(0).setWidth(0);
            
            tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
            tabla.getColumnModel().getColumn(1).setPreferredWidth(150);
            tabla.getColumnModel().getColumn(2).setPreferredWidth(150);
            tabla.getColumnModel().getColumn(3).setPreferredWidth(400);
            tabla.getColumnModel().getColumn(4).setPreferredWidth(150); 
            tabla.getColumnModel().getColumn(5).setPreferredWidth(150);
            tabla.getColumnModel().getColumn(6).setPreferredWidth(150); 

            tabla.getTableHeader().setReorderingAllowed(false);
            
            rs.close();
            pstm.close();
            conexion.close();
            
            status = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
        return status;
    }
        
        
        
      
    
}
