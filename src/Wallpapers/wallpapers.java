/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Wallpapers;

import forms.frmMenu;
import forms.frmProducts;
import forms.frmNewProduct;
import forms.frmEditPrice;
        
public class wallpapers {
    
    
    public static void main(String[] args) 
    {
        new frmMenu().setVisible(true);
        new frmProducts().setVisible(true);
        new frmNewProduct().setVisible(true);
        new frmEditPrice().setVisible(true);
       
    }
    
}
