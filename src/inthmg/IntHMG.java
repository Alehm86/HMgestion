/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package inthmg;


import views.homeFrame;
import utils.themeConfig;
import views.homeFrame;

public class IntHMG {


    public static void main(String[] args) {
        
        themeConfig.applyTheme();

        java.awt.EventQueue.invokeLater(() -> {
            new homeFrame().setVisible(true);
        });
        
    }
    
    
    
}
