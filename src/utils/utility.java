/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class utility {
    
        //AGREGA PLACEHOLDER A CAJAS DE TEXTO ---- COLOR NEGRO.
    public void agregarPlaceholderN(JTextField campo, String placeholder) {
        
        campo.setForeground(Color.BLACK);
        campo.setText(placeholder);

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setForeground(Color.GRAY);
                    campo.setText(placeholder);
                }
            }
        });
    }    
    
    //AGREGA PLACEHOLDER A CAJAS DE TEXTO ---- COLOR NEGRO.
    public void agregarPlaceholderR(JTextField campo, String placeholder) {
        
        campo.setForeground(new Color(255, 102, 51));
        campo.setText(placeholder);

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setForeground(Color.GRAY);
                    campo.setText(placeholder);
                }
            }
        });
    }
    
    //OBTIENE LA FECHA 
    public static String fecha(){
        LocalDate fecha = LocalDate.now();
        String fechaString = fecha.format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
        );
        return fechaString;
    }
    
    public void clearMsjErrorTxt(JTextField txt,JLabel label){
        
            txt.getDocument().addDocumentListener(new DocumentListener() {

            private void limpiar() {
                label.setText("");
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                limpiar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                limpiar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                limpiar();
            }
        });
        
    }
    
    public void clearMsjErrorCombo(JComboBox combo, JLabel label){
        
        combo.addActionListener(e-> {
            if (combo.getSelectedIndex() > 0) { 
                label.setText("");
            }
        });
    }
}
