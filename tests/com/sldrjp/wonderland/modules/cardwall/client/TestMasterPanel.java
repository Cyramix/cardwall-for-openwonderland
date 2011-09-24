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
package com.sldrjp.wonderland.modules.cardwall.client;

import com.sldrjp.wonderland.modules.cardwall.client.cell.CardWallCell;
import com.sldrjp.wonderland.modules.cardwall.client.cell.CardWallCellForTests;
import com.sldrjp.wonderland.modules.cardwall.common.CardWallDefaultConfiguration;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientStateForTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A standalone program for the MasterPanelImpl (outside of Wonderland).
 */

public class TestMasterPanel extends JFrame {

    CardWallCellClientState state;

    public static void main(String[] args) {
        TestMasterPanel testMasterPanel = new TestMasterPanel();
    }


    public TestMasterPanel() {
        state = CardWallDefaultConfiguration.getDefaultState();
        Frame frame = new Frame();
        // center the frame
        frame.setLocationRelativeTo(null);
        frame.mainPanel.configurePanel(state);
        frame.cardWallManager.populateData(state);
        // show frame
        frame.mainPanel.setMinimumSize(new Dimension(state.getPreferredWidth(), state.getPreferredHeight()));
        frame.mainPanel.validate();
        frame.mainPanel.setVisible(true);
        frame.validate();
        frame.setVisible(true);

    }

    class Frame extends JFrame {

        JPanel contentPane;
        CardWallCell cell = new CardWallCellForTests();
        MasterPanelImpl mainPanel = new MasterPanelImpl(cell, state);
        CardWallManager cardWallManager = new CardWallManager(cell, state, mainPanel);

        // Construct the frame

        public Frame() {

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    dispose();
                    System.exit(0);
                }
            });


            contentPane = (JPanel) this.getContentPane();
            contentPane.setLayout(new BorderLayout());
            contentPane.add(button, BorderLayout.SOUTH);
            button.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonActionPerformed(evt);
                }
            });
            setTitle("TestMasterPanel");
            contentPane.setSize(state.getPreferredWidth(), state.getPreferredHeight() + button.getHeight());
            contentPane.add(mainPanel, BorderLayout.NORTH);
            setSize(state.getPreferredWidth(), state.getPreferredHeight());
            setMinimumSize(new Dimension(state.getPreferredWidth(), state.getPreferredHeight()));
            validate();

            pack();
        }

        JButton button = new JButton("config");

        private void buttonActionPerformed(java.awt.event.ActionEvent evt) {
            cardWallManager.configureCardWall(this);

        }

    }


}
