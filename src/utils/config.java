/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class config {
    
    public void configMensajes(){
        UIManager.put("OptionPane.messageFont", new Font("Poppins", Font.PLAIN, 14));
    }
    
    public class TableStyleUtil {

        private static final Font POPPINS_HEADER = new Font("Poppins", Font.PLAIN, 14);

        public static void applyPoppinsHeader(JTable table) {

            table.getTableHeader().setFont(POPPINS_HEADER);

            table.getTableHeader().setResizingAllowed(false);

            DefaultTableCellRenderer headerRenderer =
                    (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();

            headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        }
    }
}
