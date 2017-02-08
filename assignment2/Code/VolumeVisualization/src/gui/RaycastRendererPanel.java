/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import volvis.RaycastRenderer;

/**
 *
 * @author michel
 */
public class RaycastRendererPanel extends javax.swing.JPanel {

    RaycastRenderer renderer;
    TransferFunctionEditor tfEditor = null;
    TransferFunction2DEditor tfEditor2D = null;
	private JLabel increaseSizeLabel;
	private JLabel fpsLabel;
	private JLabel fpsTextLabel;
	private JCheckBox slowModeCheckbox;
    
    /**
     * Creates new form RaycastRendererPanel
     */
    public RaycastRendererPanel(RaycastRenderer renderer) {
        initComponents();
        this.renderer = renderer;
    }

    public void setSpeedLabel(String text) {
        renderingSpeedLabel.setText(text);
    }
    
    public void setFpsLabel(String text) {
        fpsLabel.setText(text);
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        renderingSpeedLabel = new javax.swing.JLabel();
        fpsTextLabel = new javax.swing.JLabel();
        fpsLabel = new javax.swing.JLabel();
        slicerButton = new javax.swing.JRadioButton();
        mipButton = new javax.swing.JRadioButton();
        compositingButton = new javax.swing.JRadioButton();
        tf2dButton = new javax.swing.JRadioButton();
        shadingCheckbox = new javax.swing.JCheckBox();
        slicerNNButton = new javax.swing.JRadioButton();
        slicerTransferButton = new javax.swing.JRadioButton();
        slicerTrilinearButton = new javax.swing.JRadioButton();
        buttonGroup2 = new javax.swing.ButtonGroup();
        slowModeCheckbox = new javax.swing.JCheckBox();
        
        SpinnerNumberModel model1 = new SpinnerNumberModel(2, 1, null, 1);
        increaseSizeLabel = new javax.swing.JLabel();
        increaseSizeLabel.setText("# skipped pixels while interacting");
        increaseSelector = new javax.swing.JSpinner(model1);
        
        jLabel1.setText("Rendering time (ms):");
        renderingSpeedLabel.setText("jLabel2");

        fpsTextLabel.setText("FPS per second: ");
        fpsTextLabel.setToolTipText("Please note, fps is only calculate when actively rotating object"
            + ", therefore this cannot be used as a true measurement.");
        fpsLabel.setText("0");
        
        buttonGroup1.add(slicerButton);
        slicerButton.setSelected(true);
        slicerButton.setText("Slicer");
        slicerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                slicerButtonActionPerformed(evt);
            }
        });
        
        buttonGroup1.add(mipButton);
        mipButton.setText("MIP");
        mipButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mipButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(compositingButton);
        compositingButton.setText("Compositing");
        compositingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compositingButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(tf2dButton);
        tf2dButton.setText("2D Transfer function");
        tf2dButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf2dButtonActionPerformed(evt);
            }
        });

        shadingCheckbox.setText("Volume shading");
        shadingCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shadingCheckboxActionPerformed(evt);
            }
        });

        slowModeCheckbox.setText("Skip frames");
        slowModeCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                slowModeCheckboxActionPerformed(evt);
            }
        });

        //Ugly but science gui and dont want to waste time
        //Add select box for colour selection, ONLY SELECT ONE
        buttonGroup2.add(slicerNNButton);
        slicerNNButton.setText("Slicer Nearest Neighbour");
        slicerNNButton.setSelected(true);
        slicerNNButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                slicerNNButtonActionPerformed(evt);
            }
        });
        buttonGroup2.add(slicerTransferButton);
        slicerTransferButton.setText("Slicer transfer");
        slicerTransferButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                slicerTransferButtonActionPerformed(evt);
            }
        });
        buttonGroup2.add(slicerTrilinearButton);
        slicerTrilinearButton.setText("Slicer trilinear");
        slicerTrilinearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                slicerTrilinearButtonActionPerformed(evt);
            }
        });
        
        increaseSelector.setValue(2);
        increaseSelector.setMaximumSize(new Dimension(50, 40));
        increaseSelector.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                increaseSelectorActionPerformed(e);
             }
          });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                		.addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(renderingSpeedLabel))
                		.addGroup(layout.createSequentialGroup()
                                .addComponent(fpsTextLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fpsLabel))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(compositingButton)
                        .addComponent(tf2dButton)
                        .addComponent(mipButton)
                        .addComponent(slicerButton)
                        .addComponent(shadingCheckbox)
                        .addComponent(slicerNNButton)
                        .addComponent(slicerTransferButton)
                        .addComponent(slicerTrilinearButton)
                        .addComponent(increaseSizeLabel)
                        .addComponent(increaseSelector)
                        .addComponent(slowModeCheckbox)))
                .addContainerGap(339, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(renderingSpeedLabel))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(fpsTextLabel)
                        .addComponent(fpsLabel))
                .addGap(49, 49, 49)
                .addComponent(slicerButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mipButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(compositingButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tf2dButton)
                .addGap(18, 18, 18)
                .addComponent(shadingCheckbox)
                .addComponent(slicerNNButton)
                .addComponent(slicerTransferButton)
                .addComponent(slicerTrilinearButton)
                .addComponent(increaseSizeLabel)
                .addComponent(increaseSelector)
                .addComponent(slowModeCheckbox)
                .addContainerGap(137, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void mipButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mipButtonActionPerformed
        renderer.setMIPMode();
    }//GEN-LAST:event_mipButtonActionPerformed

    private void slicerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_slicerButtonActionPerformed
        renderer.setSlicerMode();
    }//GEN-LAST:event_slicerButtonActionPerformed

    private void compositingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compositingButtonActionPerformed
        renderer.setCompositingMode();
    }//GEN-LAST:event_compositingButtonActionPerformed

    private void tf2dButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf2dButtonActionPerformed
        renderer.setTF2DMode();
    }//GEN-LAST:event_tf2dButtonActionPerformed

    private void shadingCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shadingCheckboxActionPerformed
        renderer.setShadingMode(shadingCheckbox.isSelected());
    }//GEN-LAST:event_shadingCheckboxActionPerformed
    private void slicerTransferButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shadingButtonActionPerformed
        renderer.SetSlicerTransfer(slicerTransferButton.isSelected());
    }
    private void slicerNNButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shadingButtonActionPerformed
        renderer.SetSlicerNN(slicerNNButton.isSelected());
    }
    private void slicerTrilinearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shadingButtonActionPerformed
        renderer.SetSlicerTrilinear(slicerTrilinearButton.isSelected());
    }
    private void increaseSelectorActionPerformed(ChangeEvent evt) {//GEN-FIRST:event_shadingButtonActionPerformed
    	
    	try{ 
    		int val = (int) increaseSelector.getValue();
            renderer.setIncrementSize(val);
    	} catch (Exception e) {
			System.out.println("Incorrect increasement");
		}
    }


    private void slowModeCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shadingCheckboxActionPerformed
        renderer.setSlowMode(slowModeCheckbox.isSelected());
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton compositingButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JRadioButton mipButton;
    private javax.swing.JLabel renderingSpeedLabel;
    private javax.swing.JCheckBox shadingCheckbox;
    private javax.swing.JRadioButton slicerButton;
    private javax.swing.JRadioButton tf2dButton;
    // End of variables declaration//GEN-END:variables
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JRadioButton slicerTransferButton;
    private javax.swing.JRadioButton slicerNNButton;
    private javax.swing.JRadioButton slicerTrilinearButton;
    private javax.swing.JSpinner increaseSelector;
    
}
