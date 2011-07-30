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
 * MasterPanelImpl.java
 *
 * Created on Sep 30, 2010, 10:19:48 PM
 */

package com.sldrjp.wonderland.modules.cardwall.client;

import com.sldrjp.wonderland.modules.cardwall.client.cell.CardWallCell;
import com.sldrjp.wonderland.modules.cardwall.common.CardWallDefaultConfiguration;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCardCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallSectionCellClientState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * @author bob
 */
public class MasterPanelImpl extends javax.swing.JPanel implements MasterPanel {

    private static final Logger logger =
            Logger.getLogger(MasterPanelImpl.class.getName());
    private GridBagLayout gridBagLayout;
    private CardWallManager cardWallManager;

    private int gridHeight = 0;
    CardWallCell cell;
    CardWallCellClientState state;

    /**
     * Creates new form MasterPanelImpl
     */
    public MasterPanelImpl(CardWallCell cell, CardWallCellClientState state) {
        gridBagLayout = new GridBagLayout();
        this.cell = cell;
        this.state = state;
        this.setLayout(gridBagLayout);
        initComponents();

    }


    @SuppressWarnings("unchecked")
    private void initComponents() {

        setVisible(true);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });

    }

    //************************

        private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        logger.fine("moused pressed " + evt.getPoint());

    }//GEN-LAST:event_formMousePressed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        logger.fine("moused released " + evt.getPoint());

    }//GEN-LAST:event_formMouseReleased


    //************************

    public void addSection(Section section) {

        SectionHeaderImpl sectionHeader = (SectionHeaderImpl) getSectionHeader();
        SelectCardImpl selectCard = (SelectCardImpl) getSelectCard();
        section.setSectionHeader(sectionHeader);
        section.setSelectCard(selectCard);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = section.getStartColumn();
        constraints.gridy = 0;
        int noOfColumns = section.getNoOfColumns();
        constraints.gridwidth = noOfColumns;
        constraints.weightx = 0.5;
        sectionHeader.setPreferredSize(new Dimension(noOfColumns * CardWallDefaultConfiguration.BLOCK_WIDTH, CardWallDefaultConfiguration.TOP_HEIGHT));
        add(sectionHeader, constraints);
        sectionHeader.setVisible(true);
        constraints = new GridBagConstraints();
        constraints.gridx = section.getStartColumn();
        constraints.gridy = state.getNumberOfRows() + 2;
        constraints.gridwidth = noOfColumns;
        constraints.weightx = 0.5;
        add(selectCard, constraints);
        selectCard.setVisible(true);
        validate();
    }


    /**
     * Creates a CardPanelImpl from the Card cell client state, adds it to the master panel and sets the position correctly
     *
     * @param card
     * @param cardWallManager
     */
    public void showCard(Card card, CardWallManager cardWallManager) {

        CardPanelImpl cardPanel = new CardPanelImpl();
        cardPanel.setMasterPanel(this, cardWallManager);
        card.setCardPanel(cardPanel);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = card.getCardState().getColumnID();
        constraints.gridy = card.getCardState().getRowID() + 1;
        cardPanel.setCardPosition(card.getCardState().getCardPosition());
        constraints.weightx = 0.5;
        add(cardPanel, constraints);
        cardPanel.updateCard(card.getCardState().getColour(), card.getCardState().getTitle(), card.getCardState().getDetail(),
                card.getCardState().getPerson(), card.getCardState().getPoints());
        cardPanel.setVisible(true);
        validate();

    }


    public void configurePanel(CardWallCellClientState clientState) {
        // assume we are starting with a blank slate
        // insert the dummy controls to set the vertical positions correctly
        JPanel tpane = new JPanel();
        tpane.setMinimumSize(new Dimension(1, clientState.getPreferredHeight()));
        tpane.setPreferredSize(new Dimension(1, clientState.getPreferredHeight()));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = clientState.getNumberOfColumns();
        gridHeight = 2 + clientState.getNumberOfRows() + 1;
        constraints.gridheight = gridHeight;

        this.add(tpane, constraints);

        JPanel vertPane[] = new JPanel[clientState.getNumberOfRows()];
        for (int i = 0; i < clientState.getNumberOfRows(); i++) {
            constraints = new GridBagConstraints();
            constraints.gridy = i + 1;
            constraints.gridx = 10;
            vertPane[i] = new JPanel();
            vertPane[i].setMinimumSize(new Dimension(1, CardWallDefaultConfiguration.BLOCK_HEIGHT));
            vertPane[i].setPreferredSize(new Dimension(1, CardWallDefaultConfiguration.BLOCK_HEIGHT));
            this.add(vertPane[i], constraints);
        }


        JPanel horzPane[] = new JPanel[clientState.getNumberOfColumns()];
        for (int i = 0; i < clientState.getNumberOfColumns(); i++) {
            constraints = new GridBagConstraints();
            constraints.gridy = clientState.getNumberOfRows() + 3;
            constraints.gridx = i;
            horzPane[i] = new JPanel();
            horzPane[i].setMinimumSize(new Dimension(CardWallDefaultConfiguration.BLOCK_WIDTH, 1));
            horzPane[i].setPreferredSize(new Dimension(CardWallDefaultConfiguration.BLOCK_WIDTH, 1));
            this.add(horzPane[i], constraints);
        }
    }


    public void removeCard(final Card card) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                card.getCardPanel().setVisible(false);
                remove(card.getCardPanel());
                validate();
            }

        });
    }

    public void updateCard(final CardWallCardCellClientState state, final Card card) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                CardPanelImpl panel = card.getCardPanel();
                panel.updateCard(state.getColour(), state.getTitle(), state.getDetail(), state.getPerson(), state.getPoints());
                panel.repaint();
            }
        });
    }


    public void moveCard(Card card) {
        CardPanelImpl panel = card.getCardPanel();
        CardWallCardCellClientState state = card.getCardState();

        if (panel == null) {
            showCard(card, cardWallManager);

        } else {


            GridBagConstraints constraints = gridBagLayout.getConstraints(panel);
            constraints.gridx = state.getColumnID();
            constraints.gridy = state.getRowID() + 1;
            panel.setCardPosition(state.getCardPosition());
            panel.invalidate();
            constraints.weightx = 0.5;
            gridBagLayout.setConstraints(panel, constraints);
            validate();
        }
        repaint();
    }

    public void setCardWallManager(CardWallManager cardWallManager) {
        this.cardWallManager = cardWallManager;
    }

    public CardWallManager getCardWallManager() {
        return cardWallManager;
    }

    public SectionHeader getSectionHeader() {
        SectionHeaderImpl sectionHeader = new SectionHeaderImpl();
        sectionHeader.setMasterPanel(this, cardWallManager);
        return sectionHeader;

    }

    public SelectCard getSelectCard() {
        return new SelectCardImpl(cardWallManager);
    }

    private java.util.List<Component> toRemove = new ArrayList<Component>();

    public void queueRemovePanel(final Component component) {
        toRemove.add(component);
    }

    public void removeAndRepaint() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                for (int i = 0; i < toRemove.size(); i++) {
                    Component component = toRemove.get(i);
                    component.setVisible(false);
                    remove(component);
                    validate();
                }
                repaint();
                toRemove = new ArrayList<Component>();
            }
        });

    }
}