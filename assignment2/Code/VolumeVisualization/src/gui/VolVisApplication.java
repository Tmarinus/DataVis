/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.jogamp.opengl.awt.GLJPanel;
import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import volume.Volume;
import volvis.RaycastRenderer;
import volvis.Visualization;

/**
 *
 * @author michel
 */
public class VolVisApplication extends javax.swing.JFrame {

    Visualization visualization;
    Volume volume;
    RaycastRenderer raycastRenderer;
    File lastDirectory = null;
    
    /**
     * Creates new form VolVisApplication
     */
    public VolVisApplication() {
        initComponents();
        this.setTitle("2IMV20 Volume Visualization");
        
        //GLCanvas glPanel = new GLCanvas();
        GLJPanel glPanel = new GLJPanel();
        renderPanel.setLayout(new BorderLayout());
        renderPanel.add(glPanel, BorderLayout.CENTER);
        // Create a new visualization for the OpenGL panel
        visualization = new Visualization(glPanel);
        glPanel.addGLEventListener(visualization);
    
        raycastRenderer = new RaycastRenderer();
        visualization.addRenderer(raycastRenderer);
        raycastRenderer.addTFChangeListener(visualization);
        tabbedPanel.addTab("Raycaster", raycastRenderer.getPanel());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splitPane = new javax.swing.JSplitPane();
        tabbedPanel = new javax.swing.JTabbedPane();
        loadVolume = new javax.swing.JPanel();
        loadButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        infoTextPane = new javax.swing.JTextPane();
        renderPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        splitPane.setDividerLocation(600);

        loadButton.setText("Load volume");
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });

        infoTextPane.setEditable(false);
        jScrollPane1.setViewportView(infoTextPane);

        javax.swing.GroupLayout loadVolumeLayout = new javax.swing.GroupLayout(loadVolume);
        loadVolume.setLayout(loadVolumeLayout);
        loadVolumeLayout.setHorizontalGroup(
            loadVolumeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loadVolumeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(loadVolumeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(loadVolumeLayout.createSequentialGroup()
                        .addComponent(loadButton)
                        .addGap(0, 308, Short.MAX_VALUE)))
                .addContainerGap())
        );
        loadVolumeLayout.setVerticalGroup(
            loadVolumeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loadVolumeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(loadButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPanel.addTab("Load", loadVolume);

        splitPane.setRightComponent(tabbedPanel);

        javax.swing.GroupLayout renderPanelLayout = new javax.swing.GroupLayout(renderPanel);
        renderPanel.setLayout(renderPanelLayout);
        renderPanelLayout.setHorizontalGroup(
            renderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 598, Short.MAX_VALUE)
        );
        renderPanelLayout.setVerticalGroup(
            renderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 619, Short.MAX_VALUE)
        );

        splitPane.setLeftComponent(renderPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1076, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser(lastDirectory);
        fc.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isFile()) {
                    if (f.getName().toLowerCase().endsWith(".fld")) {
                        return true;
                    }
                }
                if (f.isDirectory()) {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "AVS files";
            }
        });
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        		lastDirectory = fc.getCurrentDirectory();
                File file = fc.getSelectedFile();
                volume = new Volume(file);
                
                String infoText = new String("Volume data info:\n");
                infoText = infoText.concat(file.getName() + "\n");
                infoText = infoText.concat("dimensions:\t\t" + volume.getDimX() + " x " + volume.getDimY() + " x " + volume.getDimZ() + "\n");
                infoText = infoText.concat("voxel value range:\t" + volume.getMinimum() + " - " + volume.getMaximum());
                infoTextPane.setText(infoText);
                tabbedPanel.remove(raycastRenderer.getTFPanel());
                tabbedPanel.remove(raycastRenderer.getTF2DPanel());
                raycastRenderer.setVolume(volume);
                tabbedPanel.addTab("Transfer function", raycastRenderer.getTFPanel());
                tabbedPanel.addTab("2D transfer function", raycastRenderer.getTF2DPanel());
                visualization.update();


        }
    }//GEN-LAST:event_loadButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VolVisApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VolVisApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VolVisApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VolVisApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VolVisApplication().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane infoTextPane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton loadButton;
    private javax.swing.JPanel loadVolume;
    private javax.swing.JPanel renderPanel;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTabbedPane tabbedPanel;
    // End of variables declaration//GEN-END:variables
}
