/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package views;


public class productAddSerialNumberDialog extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(productAddSerialNumberDialog.class.getName());


    public productAddSerialNumberDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        inicializar();
    }

    private void inicializar(){
        
        productAddSerialNumberPanel pProductSN = new productAddSerialNumberPanel();
        pProductSN.setSize(1700, 877);
        pProductSN.setLocation(0, 0);       

        panelContent.removeAll();
        panelContent.add(pProductSN, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1712, 877));
        panelContent.revalidate();
        panelContent.repaint();
          
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelContent = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HM Gestión - Cargar numeros de serie.");
        setResizable(false);

        panelContent.setBackground(new java.awt.Color(255, 255, 255));
        panelContent.setPreferredSize(new java.awt.Dimension(700, 750));

        javax.swing.GroupLayout panelContentLayout = new javax.swing.GroupLayout(panelContent);
        panelContent.setLayout(panelContentLayout);
        panelContentLayout.setHorizontalGroup(
            panelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 603, Short.MAX_VALUE)
        );
        panelContentLayout.setVerticalGroup(
            panelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 743, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelContent, javax.swing.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelContent, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
                .addGap(0, 0, 0))
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
//        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
//            logger.log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                productAddSerialNumberDialog dialog = new productAddSerialNumberDialog(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
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
    private javax.swing.JPanel panelContent;
    // End of variables declaration//GEN-END:variables
}
