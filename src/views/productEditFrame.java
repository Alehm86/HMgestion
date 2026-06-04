/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views;

import java.awt.BorderLayout;

public class productEditFrame extends javax.swing.JFrame {

    private int idProduct;
    
    public void dialogoEdit(int idProducto){
        this.idProduct = idProducto;           
        
        if(idProducto != -1){
            start();
        }else{
            
        }    
    }
 
    public productEditFrame() {

        initComponents();
        
        
    }
    
    private void start(){
        productViewPanel pEdit = new productViewPanel();
        pEdit.setSize(1700, 800);
        pEdit.setLocation(0, 0);
        
        pEdit.dialogoEditSearch(idProduct);
        
        jPanelEdit.removeAll();
        jPanelEdit.add(pEdit, BorderLayout.CENTER);
        jPanelEdit.revalidate();
        jPanelEdit.repaint();
    }    
    
    


    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelEdit = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión - Editar producto");
        setLocationByPlatform(true);

        javax.swing.GroupLayout jPanelEditLayout = new javax.swing.GroupLayout(jPanelEdit);
        jPanelEdit.setLayout(jPanelEditLayout);
        jPanelEditLayout.setHorizontalGroup(
            jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1269, Short.MAX_VALUE)
        );
        jPanelEditLayout.setVerticalGroup(
            jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 782, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(productEditFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(productEditFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(productEditFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(productEditFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new productEditFrame().setVisible(true);
//            }
//        });
//    }

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
    private javax.swing.JPanel jPanelEdit;
    // End of variables declaration//GEN-END:variables
}
