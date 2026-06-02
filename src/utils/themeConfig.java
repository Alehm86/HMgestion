/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;
import java.awt.Insets;

public class themeConfig {
    
    public static void applyTheme() {

        FlatLightLaf.setup();
        
        UIManager.put("Button.arc", 5);
        UIManager.put("TextComponent.arc", 5);          
        UIManager.put("Component.arc", 5);
        
        UIManager.put("defaultFont",new Font("Poppins", Font.PLAIN, 14));
        
        UIManager.put("Button.background",new Color(255, 255, 255));
        UIManager.put("Button.foreground",new Color(32, 82, 149));
        UIManager.put("Button.borderColor",new Color(210, 210, 210));
        UIManager.put("Button.focusedBorderColor",new Color(101,129,171));
        UIManager.put("Button.pressedBackground",new Color(230, 235, 245));
        
        UIManager.put("ComboBox.padding",new Insets(4,8,4,8));
        UIManager.put("ComboBox.borderWidth", 1);
        UIManager.put("ComboBox.buttonSeparatorWidth", 0);
        UIManager.put("ComboBox.selectionBackground",new Color(101,129,171));
        UIManager.put("ComboBox.selectionForeground",Color.WHITE);

    }
}
