/*
 * Copyright 2010, 2011  Service de logiciel et developpement R.J. Potter (Robert J Potter)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SectionHeaderImpl.java
 *
 * Created on Oct 4, 2010, 6:39:28 PM
 */

package com.sldrjp.wonderland.modules.cardwall.client;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * @author bob
 */
public class SectionHeaderImpl extends javax.swing.JPanel implements SectionHeader {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(
            "com/sldrjp/wonderland/modules/cardwall/client/resources/Bundle");

    /**
     * Creates new form SectionHeaderImpl
     */
    public SectionHeaderImpl() {
        initComponents();
    }

    public void setSection(int section) {
        this.section = section;
    }

    public Component getAsComponent() {
        return this;
    }

    public void setMasterPanel(MasterPanel masterPanel, CardWallManager cardWallManager) {

        this.masterPanel = masterPanel;
        this.cardWallManager = cardWallManager;

    }

    private MasterPanel masterPanel = null;
    private CardWallManager cardWallManager = null;
    private int section = 0;

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
//        jButton2 = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(200, 50));
        setPreferredSize(new java.awt.Dimension(200, 50));

        jLabel1.setText("jLabel1");

        jButton1.setFont(new java.awt.Font("DejaVu Sans", 0, 8)); // NOI18N
        jButton1.setText(BUNDLE.getString("sectionHeader.button.addNew"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
//        jButton2.setFont(new java.awt.Font("DejaVu Sans", 0, 8)); // NOI18N
//        jButton2.setText("More ...");
//        jButton2.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jButton2ActionPerformed(evt);
//            }
//        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                .addComponent(jButton2)
                                .addComponent(jButton1)
                                .addGap(51, 51, 51))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                        .addComponent(jButton2)
                                        .addComponent(jButton1))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        cardWallManager.requestNewCard(section);
    }//GEN-LAST:event_jButton1ActionPerformed


//    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
//
//        SelectCardImpl sc = new SelectCardImpl();
//        sc.setVisible(true);
//        masterPanel.add(sc);
//
//    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
//    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

    public void setText(String text) {
        jLabel1.setText(text);
        jLabel1.setVisible(true);

    }

    public void setNewText(String text) {
        jLabel1.setText(text);
        jLabel1.setVisible(true);
        final JPanel panel = this;


        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                panel.repaint();
            }
        });

    }

}
