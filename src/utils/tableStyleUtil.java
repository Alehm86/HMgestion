/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class tableStyleUtil {
   
        private static final Font POPPINS_HEADER =
                new Font("Poppins", Font.PLAIN, 14);

        public static void applyPoppinsHeader(JTable table) {

            JTableHeader header = table.getTableHeader();

            header.setUI(new BasicTableHeaderUI());
            header.setFont(POPPINS_HEADER);
            header.setReorderingAllowed(false);
            header.setResizingAllowed(false);
            header.setDefaultRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(
                        JTable table,
                        Object value,
                        boolean isSelected,
                        boolean hasFocus,
                        int row,
                        int column) {

                    JLabel label = (JLabel)
                            super.getTableCellRendererComponent(
                                    table,
                                    value,
                                    false,
                                    false,
                                    row,
                                    column);

                    label.setOpaque(true);
                    label.setBackground(new Color(101,129,171));
                    label.setForeground(Color.WHITE);
                    label.setHorizontalAlignment(JLabel.CENTER);
                    label.setFont(POPPINS_HEADER);
                    label.setBorder(UIManager.getBorder("TableHeader.cellBorder"));

                    return label;
                }
            });
            header.setOpaque(true);
        }   
}
