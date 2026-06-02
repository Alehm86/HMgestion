/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package inthmg;

import com.formdev.flatlaf.FlatLightLaf;
import views.homeFrame;
import utils.themeConfig;

public class IntHMG {


    public static void main(String[] args) {
        
        themeConfig.applyTheme();

        java.awt.EventQueue.invokeLater(() -> {
            new homeFrame().setVisible(true);
        });
        
    }
    
    
    
}
