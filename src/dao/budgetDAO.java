/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import ConnectionDB.connectionDB;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import utils.tableStyleUtil;

public class budgetDAO {
    
    genericDAO qGeneric = new genericDAO();
    connectionDAO Connection = new connectionDAO();   
    
    private DefaultTableModel crearModeloNoEditable() {
        return new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }    
       
    public int insertBudget(
            Integer id_service,
            LocalDate date,
            LocalDate expiration_date,
            String customer_name,
            String customer_phone,
            double total,
            int state,
            String observations
        ){    

        int idGenerado  = 0;

        String sqlInsert = "INSERT INTO `budget`(`id_service`, `date`, `expiration_date`, `customer_name`, `customer_phone`, `total`, `id_state`, `observations`) " +
                           "VALUES (?,?,?,?,?,?,?,?)";

        String sqlUpdate = "UPDATE `budget` SET `nro_budget`=? WHERE `id_budget`=?";

        Connection conexion = Connection.getConnection();

        try{
            conexion.setAutoCommit(false);
            PreparedStatement pstmtInsert = conexion.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);

            if (id_service == null) {
                pstmtInsert.setNull(1, java.sql.Types.INTEGER);
            } else {
                pstmtInsert.setInt(1, id_service);
            }
            pstmtInsert.setDate(2, java.sql.Date.valueOf(date));
            pstmtInsert.setDate(3, java.sql.Date.valueOf(expiration_date));
            pstmtInsert.setString(4, customer_name);
            pstmtInsert.setString(5, customer_phone);
            pstmtInsert.setDouble(6, total);
            pstmtInsert.setInt(7, state);
            pstmtInsert.setString(8, observations);

            pstmtInsert.executeUpdate();            

            ResultSet rs = pstmtInsert.getGeneratedKeys();

            if(rs.next()){
                idGenerado = rs.getInt(1);
            }

            rs.close();
            pstmtInsert.close();

            if(idGenerado > 0){
                
                String puntoVenta = "PPTO-01";
                String nro_budget = String.format("%s-%06d", puntoVenta, idGenerado);            

                PreparedStatement pstmtUpdate = conexion.prepareStatement(sqlUpdate);

                pstmtUpdate.setString(1, nro_budget);
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
            JOptionPane.showMessageDialog(null,"ERROR AL REGISTRAR PRESUPUESTO: " + e.getMessage());
        }

        return idGenerado;
    } 
    
    public boolean insertBudgetDetail(
            int id_budget,
            String description,
            String type,
            int idProd,
            int quantity,
            double price,
            String iva,
            double subtotal    
        ){
        
        boolean status = false;
        
        String sql ="INSERT INTO `budget_detail`(`id_budget`, `description`, `type`, `id_product`, `quantity`, `price`, `iva`, `subtotal`) " +
                    "VALUES (?,?,?,?,?,?,?,?)";
        
        Connection conexion = Connection.getConnection();
        
        try{
            PreparedStatement pstmt = (PreparedStatement) conexion.prepareStatement(sql);  
            
            pstmt.setInt(1, id_budget);
            pstmt.setString(2, description);
            pstmt.setString(3, type);
            
            if (idProd > 0) {
                pstmt.setInt(4, idProd);
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }
            
            pstmt.setInt(5, quantity);
            pstmt.setDouble(6, price);
            pstmt.setString(7, iva);
            pstmt.setDouble(8, subtotal);
            
            pstmt.executeUpdate();   
            
            status = true;
            
            conexion.close(); 
            pstmt.close();
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR AL REGISTRAR DETALLE: " + e.getMessage());
        }
        return status;
    }
    
    public void selectBudget(      
            int id_budget,
            JLabel lbl_budget,
            JLabel lbl_date,
            JLabel lbl_vencimiento,
            JLabel lbl_name,
            JLabel lbl_phone,
            JLabel lbl_cuit,
            JTable jTableItems,
            JTextArea jTextAreaObservation
        ){

        String sqlBudget =  "SELECT `nro_budget`, `date`, `expiration_date`, `customer_name`, `customer_phone`, `observations` " +
                            "FROM `budget` " +
                            "WHERE `id_budget` =?";

        String sqlDetail = "SELECT `description`, `quantity`, `price`, `iva`, `subtotal` FROM `budget_detail` WHERE `id_budget`=?";

        String sqlCUIT = "SELECT `cuit` FROM `customer` WHERE `name` = ?";

        DefaultTableModel dtm = crearModeloNoEditable();

        String nameCustomer = "";

        Connection conexion = Connection.getConnection();

        String[] titulo = new String[]{"Items", "Cant", "P. Unit. IVA Inc.", "IVA","Total"};
        dtm.setColumnIdentifiers(titulo);

        try{

            PreparedStatement pstmt1 = conexion.prepareStatement(sqlBudget);
            pstmt1.setInt(1, id_budget);
            ResultSet rs1 = pstmt1.executeQuery();

            if (rs1.next()) {
                lbl_budget.setText(rs1.getString("nro_budget"));
                lbl_date.setText(rs1.getString("date"));               
                lbl_vencimiento.setText(rs1.getString("expiration_date"));
                lbl_name.setText(rs1.getString("customer_name"));
                nameCustomer = rs1.getString("customer_name");
                lbl_phone.setText(rs1.getString("customer_phone"));
                jTextAreaObservation.setText(rs1.getString("observations"));
            }

            rs1.close();
            pstmt1.close();

            PreparedStatement pstmt2 = conexion.prepareStatement(sqlDetail);
            pstmt2.setInt(1, id_budget);
            ResultSet rs2 = pstmt2.executeQuery();

            while (rs2.next()) {
                Object[] row = {
                    rs2.getString("description"),
                    rs2.getInt("quantity"),
                    rs2.getDouble("price"),
                    rs2.getString("iva"),
                    rs2.getDouble("subtotal"),
                };
                dtm.addRow(row);
            }

            jTableItems.setModel(dtm);
            
            jTableItems.getTableHeader().setFont(new Font("Poppins", Font.PLAIN, 14));
            jTableItems.getTableHeader().setResizingAllowed(false);

            DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) jTableItems.getTableHeader().getDefaultRenderer();

            headerRenderer.setHorizontalAlignment(JLabel.CENTER);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            jTableItems.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); 
            jTableItems.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); 
            jTableItems.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            jTableItems.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

            jTableItems.getColumnModel().getColumn(0).setPreferredWidth(350);
            jTableItems.getColumnModel().getColumn(1).setPreferredWidth(40); 
            jTableItems.getColumnModel().getColumn(2).setPreferredWidth(100); 
            jTableItems.getColumnModel().getColumn(3).setPreferredWidth(100);
            jTableItems.getColumnModel().getColumn(4).setPreferredWidth(100);

            rs2.close();
            pstmt2.close();

            PreparedStatement pstmt3 = conexion.prepareStatement(sqlCUIT);
            pstmt3.setString(1, nameCustomer);
            ResultSet rs3 = pstmt3.executeQuery();

            if (rs3.next()) {
                lbl_cuit.setText(rs3.getString("cuit"));
            }

            rs3.close();
            pstmt3.close();

        } catch(SQLException e){
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static int selectIdBudget(int id_service){
        
        String sql="SELECT `id_budget` FROM `budget` WHERE `id_service`= ?";
               
        int Id = -1;   
        
        connectionDB con = new connectionDB();
        Connection conexion = (Connection) con.establecerConexion();
        
        try{
           PreparedStatement pstmt = conexion.prepareStatement(sql);
           pstmt.setInt(1, id_service);
           ResultSet rs = pstmt.executeQuery();   
           
            if(rs.next()){
                Id =(rs.getInt("id_budget"));            
            }  
           
            rs.close();
            pstmt.close();
            conexion.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());
        }
        return Id; 
    }  
    
    public boolean existsBudget(int id_service) {

        String sql = "SELECT EXISTS(SELECT 1 FROM budget WHERE id_service = ? AND (id_state = '1' OR id_state = '2' OR id_state = '4'))";

        connectionDB con = new connectionDB();

        try (Connection conexion = con.establecerConexion();
            PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, id_service);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean(1);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar existencia de presupuesto");
        }

        return false;
    }
    
    public void listItemsBudgetInService(int id_service, JTable jTableItems){
        
        String sql ="SELECT " +
                    "b.id_budget, " +
                    "bd.description, " +
                    "bd.quantity, " +
                    "bd.price, " +
                    "bd.iva, " +
                    "bd.subtotal " +
                    "FROM budget b " +
                    "INNER JOIN budget_detail bd ON b.id_budget = bd.id_budget " +
                    "WHERE b.id_service = ? " +
                    "AND b.id_state IN (1, 2, 3, 4);";
        
        DefaultTableModel dtm = crearModeloNoEditable();

        Connection conexion = Connection.getConnection();

        String[] titulo = new String[]{"Items", "Cant", "Precio Unit", "IVA","Total"};
        dtm.setColumnIdentifiers(titulo);        
 

        try{
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setInt(1, id_service);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("description"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getString("iva"),
                    rs.getDouble("subtotal"),
                };
                dtm.addRow(row);
            }

            jTableItems.setModel(dtm);

            jTableItems.getTableHeader().setFont(new Font("Poppins", Font.PLAIN, 14));
            jTableItems.getTableHeader().setResizingAllowed(false);

            DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) jTableItems.getTableHeader().getDefaultRenderer();

            headerRenderer.setHorizontalAlignment(JLabel.CENTER);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            jTableItems.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); 
            jTableItems.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); 
            jTableItems.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            jTableItems.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

            jTableItems.getColumnModel().getColumn(0).setPreferredWidth(350);
            jTableItems.getColumnModel().getColumn(1).setPreferredWidth(40); 
            jTableItems.getColumnModel().getColumn(2).setPreferredWidth(100); 
            jTableItems.getColumnModel().getColumn(3).setPreferredWidth(100);
            jTableItems.getColumnModel().getColumn(4).setPreferredWidth(100);

            rs.close();
            pstmt.close();
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    public boolean cancelBudget(int id_service){

        String sqlService = "UPDATE service_orders SET id_status = 1, cost = NULL, repair_date = NULL WHERE id_service = ?";
        String sqlBudget = "UPDATE budget SET id_state = 5 WHERE id_service = ?";

        boolean estado = false;

        Connection conexion = Connection.getConnection();

        try{
            conexion.setAutoCommit(false);

            PreparedStatement pstmt1 = conexion.prepareStatement(sqlService);           
            pstmt1.setInt(1, id_service);
            pstmt1.executeUpdate();
            pstmt1.close();

            PreparedStatement pstmt2 = conexion.prepareStatement(sqlBudget);           
            pstmt2.setInt(1, id_service);
            pstmt2.executeUpdate();
            pstmt2.close();

            conexion.commit();
            estado = true;

        }
        catch(SQLException e){
            try {
                conexion.rollback(); 
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "ERROR -> " + e.getMessage());
        } 
        finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return estado;
    }     
    
    public void listBudgets(int type, JTable tableItems, String filtroFecha, String filtroEstado){

        String sqlBase = "";
        
        String op1 =
            "SELECT " +
            "b.id_budget, " +
            "b.date, " +
            "b.nro_budget, " +
            "b.customer_name, " +
            "COALESCE(s.service_number, ' ') AS service_number, " +
            "s.id_service, " +
            "b.total, " +
            "b.expiration_date, " +
            "bt.name AS state_name " +
            "FROM budget b " +
            "LEFT JOIN service_orders s ON b.id_service = s.id_service " +
            "INNER JOIN budget_states bt ON b.id_state = bt.id_budget_state " +
            "WHERE b.date >= CURDATE() - INTERVAL 1 YEAR ";

        String op2 =
            "SELECT " +
            "b.id_budget, " +
            "b.date, " +
            "b.nro_budget, " +
            "b.customer_name, " +
            "COALESCE(s.service_number, ' ') AS service_number, " +
            "s.id_service, " +
            "b.total, " +
            "b.expiration_date, " +
            "bt.name AS state_name " +
            "FROM budget b " +
            "LEFT JOIN service_orders s ON b.id_service = s.id_service " +
            "INNER JOIN budget_states bt ON b.id_state = bt.id_budget_state " +
            "WHERE b.id_service IS NULL " +
            "AND b.id_state <> 6 " +
            "AND b.date >= CURDATE() - INTERVAL 1 YEAR ";
        
        if(type == 1){
            sqlBase =  op2;
        }else{
            sqlBase =  op1;
        }

        String condicionFecha = "";
        String condicionEstado = "";

        switch (filtroFecha) {
            case "Hoy":
                condicionFecha = "AND b.date = CURDATE() ";
                break;
            case "7 días":
                condicionFecha = "AND b.date >= CURDATE() - INTERVAL 7 DAY ";
                break;
            case "30 días":
                condicionFecha = "AND b.date >= CURDATE() - INTERVAL 30 DAY ";
                break;
            case "Último año":
            case "Todo":
                condicionFecha = "";
                break;
        }

        if (!filtroEstado.equals("Todos")) {
            condicionEstado = "AND bt.name = '" + filtroEstado + "' ";
        }

        String sql = sqlBase + condicionFecha + condicionEstado + "ORDER BY b.date DESC";

        DefaultTableModel dtm = crearModeloNoEditable();

        String[] titulo = {
            "Fecha", "Presup. Nº", "Cliente", "Nº de servicio","Total", "Vencimiento", "Estado", "id_budget", "id_service"
        };
        dtm.setColumnIdentifiers(titulo);

        Connection conexion = Connection.getConnection();

        try{
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {

                String total = "$ " + String.format("%.2f", rs.getDouble("total"));

                Object idServiceObj = rs.getObject("id_service");
                Integer idService = (idServiceObj != null) ? (Integer) idServiceObj : null;

                Object[] row = {
                    rs.getDate("date").toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    rs.getString("nro_budget"),
                    rs.getString("customer_name"),
                    rs.getString("service_number"),
                    total,
                    rs.getDate("expiration_date").toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    rs.getString("state_name"),
                    rs.getInt("id_budget"),
                    idService
                };

                dtm.addRow(row);
            }

            tableItems.setModel(dtm);
            
            tableStyleUtil.applyPoppinsHeader(tableItems);

            tableItems.getTableHeader().setFont(new Font("Poppins", Font.PLAIN, 14));
            tableItems.getTableHeader().setResizingAllowed(false);

            DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tableItems.getTableHeader().getDefaultRenderer();
            headerRenderer.setHorizontalAlignment(JLabel.CENTER);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            for (int i = 0; i < tableItems.getColumnCount(); i++) {
                tableItems.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                tableItems.getColumnModel().getColumn(i).setPreferredWidth(100);
            }

            int COL_ID_BUDGET = 7;
            int COL_ID_SERVICE = 8;

            tableItems.getColumnModel().getColumn(COL_ID_BUDGET).setMinWidth(0);
            tableItems.getColumnModel().getColumn(COL_ID_BUDGET).setMaxWidth(0);
            tableItems.getColumnModel().getColumn(COL_ID_BUDGET).setWidth(0);
            tableItems.getTableHeader().getColumnModel().getColumn(COL_ID_BUDGET).setMinWidth(0);
            tableItems.getTableHeader().getColumnModel().getColumn(COL_ID_BUDGET).setMaxWidth(0);

            tableItems.getColumnModel().getColumn(COL_ID_SERVICE).setMinWidth(0);
            tableItems.getColumnModel().getColumn(COL_ID_SERVICE).setMaxWidth(0);
            tableItems.getColumnModel().getColumn(COL_ID_SERVICE).setWidth(0);
            tableItems.getTableHeader().getColumnModel().getColumn(COL_ID_SERVICE).setMinWidth(0);
            tableItems.getTableHeader().getColumnModel().getColumn(COL_ID_SERVICE).setMaxWidth(0);

            rs.close();
            stmt.close();
            conexion.close();

        }catch(SQLException e){
            qGeneric.mensajeError();
            System.out.println("ERROR EN: budgetDAO: listBudgets. " + e.getMessage());
        }
    }
    
    public void actualizarPresupuestosVencidos(){
        
        String sql= "UPDATE budget SET id_state  = 4 WHERE expiration_date < CURDATE() AND id_state  = 1";
        
        Connection conexion = Connection.getConnection();
        
        try{
            PreparedStatement pstmt = conexion.prepareStatement(sql);           
            pstmt.executeUpdate();
            
            //aca tenemos que agregarlo al dashboard informativo

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
       
    }

    public boolean updateIdInBudgetDetail(Connection conn,int id_product, int idBudgDetail){
        
        String sql = "UPDATE `budget_detail` SET `id_product`= ? WHERE id_budget_detail = ?";
              
        boolean estado = false;
        
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);           
            pstmt.setInt(1, id_product);
            pstmt.setInt(2, idBudgDetail);
            pstmt.executeUpdate();
            
            estado = true;            
            
        }catch(SQLException e){
            qGeneric.mensajeError();
            System.out.println("ERROR EN: budgetDAO: updateIdInBudgetDetail. " + e.getMessage());
        }
        return estado;
    }
    
}
