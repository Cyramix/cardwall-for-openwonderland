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
import com.sldrjp.wonderland.modules.cardwall.common.CardPosition;
import com.sldrjp.wonderland.modules.cardwall.common.CardWallDefaultConfiguration;
import com.sldrjp.wonderland.modules.cardwall.common.CardWallException;
import com.sldrjp.wonderland.modules.cardwall.common.CardWallSyncMessage;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCardCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallSectionCellClientState;
import org.jdesktop.wonderland.client.cell.utils.CellCreationException;
import org.jdesktop.wonderland.client.cell.utils.CellUtils;
import org.jdesktop.wonderland.modules.rockwellcollins.stickynote.common.cell.StickyNoteCellServerState;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Bob
 * Date: 26/01/11
 * Time: 9:54 PM
 * To change this template use File | Settings | File Templates.
 */


public class CardWallManager {

    private CardWallCell cell;
    private CardWallCellClientState state;
    private Card cards[][];
    private List<Section> sections = null;
    MasterPanel masterPanel;

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(
            "com/sldrjp/wonderland/modules/cardwall/client/resources/Bundle");

    private List<SectionHeader> sectionHeaders = null;

    private static final Logger logger = Logger.getLogger(CardWallManager.class.getName());

    public CardWallManager(CardWallCell cell, CardWallCellClientState state, MasterPanel masterPanel) {
        this.cell = cell;
        this.state = state;
        this.masterPanel = masterPanel;
        masterPanel.setCardWallManager(this);
    }

    /**
     * adds a blank card to the selected section in the first available location, if the card is added a blank form is
     * shown on the master panel (via masterPanel.showCard) and a message is sent to the server
     * see testAddCardToSection
     *
     * @param sectionSelected
     */
    public void addCard(int sectionSelected) {
        // check if section exists
        if ((sectionSelected < 0) || (sectionSelected >= sections.size())) {
            throw new CardWallException(CardWallException.SECTION_OUT_OF_RANGE_MSG + " " + Integer.toString(sectionSelected), CardWallException.SECTION_OUT_OF_RANGE, Integer.toString(sectionSelected));
        }

        CardPosition position = null;
        Section section = sections.get(sectionSelected);
        // find the first free space in this section
        position = getFreePosition(section);

        if (position != null) {
            Card card = new Card();
            CardWallCardCellClientState cardState = card.getCardState();
            cardState.setColumnID(position.column);
            cardState.setRelativeColumnID(relativeColumn(section.getSectionNumber(), position.column));
            cardState.setRowID(position.row);
            cardState.setSectionID(sectionSelected);
            cards[position.column][position.row] = card;
            masterPanel.showCard(card, this);
            state.getCards().add(card.getCardState());
            cell.sendMessage(CardWallSyncMessage.ADD_CARD, null, cardState);

        }


    }

    private CardPosition getFreePosition(Section section) {
        CardPosition position = null;
        int numberOfColumns = section.getEndColumn() - section.getStartColumn() + 1;
        int i = 0;
        int freeSlot = -1;
        int column = -1;
        while (freeSlot == -1 && i < numberOfColumns) {
            column = section.getStartColumn() + i;
            freeSlot = getFreeSlotFromColumn(cards[column]);
            i++;
        }
        if (freeSlot >= 0) {
            position = new CardPosition(column, freeSlot);
        }
        return position;
    }

    protected int getFreeSlotFromColumn(Card[] cards) {
        int i = 0;
        while (i < cards.length) {
            if (cards[i] == null) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * adds an existing card (with location and content embedded in the card state) to the cell state
     *
     * @param cardState
     */
    public void addCard(CardWallCardCellClientState cardState) {
        if (cardState.isVisible()) {
            showCard(cardState);
        } else {
            sections.get(cardState.getSectionID()).addAdditionalCard(cardState);
        }

        state.getCards().add(cardState);
    }

    public void addSection(int sectionNumber, CardWallSectionCellClientState sectionState) {
        Section section = new Section(sectionNumber, sectionState);
        sections.add(sectionNumber, section);
        masterPanel.addSection(section);
    }


    /**
     * Creates a Card object and adds it to the cards array in the correct position.  If a card already exists in the
     * location a Runtime exception is thrown
     * this method will then call showCard on the panel
     *
     * @param cardState
     */
    public void showCard(CardWallCardCellClientState cardState) {
        if (cardState.getRelativeColumnID() == -1) {
            addToAdditionalSection(cardState);
        } else {
            int actualColumnID = getActualColumnID(cardState);
            cardState.setColumnID(actualColumnID);
            logger.fine("relative column:" + cardState.getRelativeColumnID() + " actual column:" + actualColumnID);
            if (cards[actualColumnID][cardState.getRowID()] != null) {
                if (cards[actualColumnID][cardState.getRowID()].getCardState().toString().equals(cardState.toString())) {
                    throw new CardWallException(CardWallException.SAME_CARD_AT_LOCATION_MSG + actualColumnID + "," + cardState.getRowID(), CardWallException.SAME_CARD_AT_LOCATION);
                } else {
                    throw new CardWallException(CardWallException.CARD_AT_LOCATION_MSG + actualColumnID + "," + cardState.getRowID(), CardWallException.CARD_AT_LOCATION);
                }
            }
            Card card = new Card();
            card.setCardState(cardState);
            cards[actualColumnID][cardState.getRowID()] = card;
            masterPanel.showCard(card, this);
        }


//    if(cardState.getColumnID()==-1)
//
//    {
//
//        addToAdditionalSection(cardState);
//    }
//
//    else
//
//    {
//        if (cards[cardState.getColumnID()][cardState.getRowID()] != null) {
//            if (cards[cardState.getColumnID()][cardState.getRowID()].getCardState().toString().equals(cardState.toString())) {
//                throw new CardWallException(CardWallException.SAME_CARD_AT_LOCATION_MSG + cardState.getColumnID() + "," + cardState.getRowID(), CardWallException.SAME_CARD_AT_LOCATION);
//            } else {
//                throw new CardWallException(CardWallException.CARD_AT_LOCATION_MSG + cardState.getColumnID() + "," + cardState.getRowID(), CardWallException.CARD_AT_LOCATION);
//            }
//        }
//        Card card = new Card();
//        card.setCardState(cardState);
//        cards[cardState.getColumnID()][cardState.getRowID()] = card;
//        masterPanel.showCard(card, this);
//    }

    }

    private int getActualColumnID(CardWallCardCellClientState cardState) {
        return getSection(cardState.getSectionID()).getStartColumn() + cardState.getRelativeColumnID();
    }

    private void addToAdditionalSection(CardWallCardCellClientState cardState) {
        Section section = getSection(cardState.getSectionID());
        section.addAdditionalCard(cardState);
    }


    /**
     * deletes the card at the specified position, sends a message to the server (calls removeCard to remove the card
     * from the display)
     *
     * @param cardPosition
     */
    public void deleteCard(CardPosition cardPosition) {
        CardWallCardCellClientState cardToDelete = removeCard(cardPosition);
        cell.sendMessage(CardWallSyncMessage.DELETE_CARD, null, cardToDelete);

    }

    public CardWallCardCellClientState removeCard(CardPosition cardPosition) {
        Card card = cards[cardPosition.column][cardPosition.row];
        state.getCards().remove(card.getCardState());
        cards[cardPosition.column][cardPosition.row] = null;
        masterPanel.removeCard(card);
        return card.getCardState();

    }

    public CardWallCardCellClientState hideCard(CardPosition cardPosition) {
        Card card = cards[cardPosition.column][cardPosition.row];
        cards[cardPosition.column][cardPosition.row] = null;
        masterPanel.removeCard(card);
        return card.getCardState();

    }

    public void updateCard(final CardWallCardCellClientState state) {
        final Card card = cards[getActualColumnID(state)][state.getRowID()];
        card.getCardState().setTitle(state.getTitle());
        card.getCardState().setDetail(state.getDetail());
        masterPanel.updateCard(state, card);
    }

    public void moveCard(CardPosition cardPosition, Double x, Double y) {
        // determine where in the grid the mouse was released
        int possibleColumn = (int) (x / CardWallDefaultConfiguration.BLOCK_WIDTH);
        int possibleRow = (int) (y - CardWallDefaultConfiguration.TOP_HEIGHT) / CardWallDefaultConfiguration.BLOCK_HEIGHT;
        logger.fine("possible position " + possibleColumn + ", " + possibleRow);
        // make sure the new position is in range
        if ((possibleColumn >= 0) && (possibleRow >= 0) && (possibleColumn < cards.length) && (possibleRow < cards[0].length)) {
            if (cards[possibleColumn][possibleRow] == null) {
                CardPosition oldPosition = new CardPosition(cardPosition.column, cardPosition.row);
                CardWallCardCellClientState state = moveCard(cards[cardPosition.column][cardPosition.row], possibleColumn, possibleRow);
                state.setSectionID(getSectionID(possibleColumn));
                cell.sendMessage(CardWallSyncMessage.MOVE_CARD, oldPosition, state);
            } else {
                throw new CardWallException(CardWallException.CANNOT_MOVE_MSG + ": " + possibleColumn + ", " + possibleRow, CardWallException.CANNOT_MOVE);
            }
        }
    }

    public void moveCard(CardPosition currentPosition, CardWallCardCellClientState newState) {
        logger.fine(newState.getUniqueID());
        Card card = null;
        if (currentPosition == null) {
            logger.fine("from additional");
            card = findCardFromAdditionalItems(sections.get(newState.getSectionID()), newState.getUniqueID());


        } else {
            logger.fine("from card table");
            card = cards[currentPosition.column][currentPosition.row];
        }
        moveCard(card, newState.getColumnID(), newState.getRowID());
    }

    private Card findCardFromAdditionalItems(Section section, String uniqueID) {

        for (CardWallCardCellClientState state : section.getAdditionalCards()) {
            logger.fine("finding card " + state.getUniqueID());
            if (state.getUniqueID().equals(uniqueID)) {
                return new Card(state);
            }
        }
        return null;
    }

    private CardWallCardCellClientState moveCard(Card card, int possibleColumn, int possibleRow) {
        if (possibleColumn < 0) {
            return archiveCard(card);
        } else {
            if (cards[possibleColumn][possibleRow] != null) {
                throw new CardWallException(CardWallException.CANNOT_MOVE_MSG + ": " + possibleColumn + ", " + possibleRow, CardWallException.CANNOT_MOVE);
            }
            // can move card
            CardWallCardCellClientState state = card.getCardState();
            if (state.getColumnID() == -1) {
                getSection(state.getSectionID()).getSelectCard().removeFromSelectable(state);
            } else {
                cards[getActualColumnID(state)][state.getRowID()] = null;
            }
            state.setRowID(possibleRow);

            state.setColumnID(possibleColumn);
            state.setSectionID(getSectionID(possibleColumn));
            state.setRelativeColumnID(relativeColumn(state.getSectionID(), possibleColumn));
            cards[possibleColumn][possibleRow] = card;
            masterPanel.moveCard(card);
            return state;
        }
    }

    public void checkSendChanges(CardPosition cardPosition, Color colour, String titleText, String bodyText, String personText, String pointsText) {
        Card card = cards[cardPosition.column][cardPosition.row];
        if (card == null) {

            // card maybe in the process of being deleted
            logger.fine("No card found at " + cardPosition.column + ", " + cardPosition.row);

        } else {
            CardWallCardCellClientState state = card.checkForChanges(colour, titleText, bodyText, personText, pointsText);
            if (state != null) {
                cell.sendMessage(CardWallSyncMessage.CHANGE_TEXT, null, state);
            }
        }
    }

    public void populateData() {
        populateData(state);
    }

    public void populateData(CardWallCellClientState clientState) {
        if (clientState.getNumberOfColumns() == 0) {
            clientState.setNumberOfColumns(CardWallDefaultConfiguration.getDefaultState().getNumberOfColumns());
        }
        if (clientState.getNumberOfRows() == 0) {
            clientState.setNumberOfRows(CardWallDefaultConfiguration.getDefaultState().getNumberOfRows());
        }
        cards = new Card[clientState.getNumberOfColumns()][clientState.getNumberOfRows()];

        // setup the sections
        List<CardWallSectionCellClientState> sections = clientState.getSectionStates();
        this.sections = new ArrayList<Section>(sections.size());
        int i = 0;
        if (sections != null) {
            for (CardWallSectionCellClientState sectionState : sections) {
                addSection(i++, sectionState);

            }

        }

        List<CardWallCardCellClientState> cards = clientState.getCards();
        if (cards != null) {
            for (int j = 0; j < cards.size(); j++) {
                CardWallCardCellClientState cardWallCardCellClientState = cards.get(j);
                // determine section from card position - deprecated
                if (cardWallCardCellClientState.getSectionID() == -1) {
                    if (cardWallCardCellClientState.getColumnID() >= 0) {
                        cardWallCardCellClientState.setSectionID(getSectionID(cardWallCardCellClientState.getColumnID()));
                        cell.sendMessage(CardWallSyncMessage.UPDATE_SERVER_CARD_STATE_ONLY, null, cardWallCardCellClientState);
                    } else {
                        cardWallCardCellClientState.setSectionID(0); // by default put them all in the first section if we don't have any additional data
                    }
                }
                // set the relative position
                if ((cardWallCardCellClientState.getRelativeColumnID() == -1) && (cardWallCardCellClientState.getColumnID() >= 0)) {
                    cardWallCardCellClientState.setRelativeColumnID(relativeColumn(cardWallCardCellClientState.getSectionID(), cardWallCardCellClientState.getColumnID()));
                    cell.sendMessage(CardWallSyncMessage.UPDATE_SERVER_CARD_STATE_ONLY, null, cardWallCardCellClientState);
                }
                showCard(cardWallCardCellClientState);
            }
        }
    }

    protected int relativeColumn(int sectionID, int actualColumn) {
        if (actualColumn == -1) {
            return -1;
        }
        return actualColumn - getSection(sectionID).getStartColumn();

    }

    protected CardWallCardCellClientState getCard(int i, int i1) {
        Card card = cards[i][i1];
        return card == null ? null : cards[i][i1].getCardState();
    }

    protected List<CardWallCardCellClientState> getAdditionalCards(int section) {
        return sections.get(section).getAdditionalCards();
    }

    protected int getSectionID(int column) {
        int sectionID = 0;
        for (Section section : sections) {
            if ((section.getStartColumn() <= column) && (section.getEndColumn() >= column)) {
                return sectionID;
            }
            sectionID++;
        }
        return -1;  //To change body of created methods use File | Settings | File Templates.
    }

    public List<CardWallCardCellClientState> getCards(int section) {
        List<CardWallCardCellClientState> allCards = state.getCards();
        List<CardWallCardCellClientState> selectedCards = new ArrayList<CardWallCardCellClientState>();
        for (CardWallCardCellClientState cardState : allCards) {
            if (cardState.getSectionID() == section) {
                selectedCards.add(cardState);
            }
        }
        return selectedCards;
    }

    public CardWallCardCellClientState archiveCard(Card card) {
        moveCardToArchive(card.getCardState().getCardPosition());
        return card.getCardState();
    }

    public void archiveCard(CardPosition cardPosition) {
        CardWallCardCellClientState cardToArchive = moveCardToArchive(cardPosition);
        cell.sendMessage(CardWallSyncMessage.MOVE_CARD, cardPosition, cardToArchive);
    }

    private CardWallCardCellClientState moveCardToArchive(CardPosition cardPosition) {
        CardWallCardCellClientState cardToArchive = hideCard(cardPosition);
        cardToArchive.setColumnID(-1);
        cardToArchive.setRelativeColumnID(-1);
        cardToArchive.setRowID(-1);
        Section section = sections.get(cardToArchive.getSectionID());
        section.addAdditionalCard(cardToArchive);
        masterPanel.repaint();
        return cardToArchive;
    }

    /**
     * Adds an existing card back onto the wall and will send a message to sync
     *
     * @param sectionNumber section to add the card
     * @param state         client state of card - the state gets updated with the new position
     * @return boolean whether the card could be added or not (false if the section is full)
     */
    public boolean addCard(int sectionNumber, CardWallCardCellClientState state) {
        logger.fine("addCard " + state.toString());
        Section section = sections.get(sectionNumber);
        CardPosition position = getFreePosition(section);
        state.setColumnID(position.column);
        state.setRelativeColumnID(relativeColumn(sectionNumber, position.column));
        state.setRowID(position.row);

        if (position != null) {
            Card card = new Card(state);
            cards[position.column][position.row] = card;
            masterPanel.showCard(card, this);
            cell.sendMessage(CardWallSyncMessage.MOVE_CARD, null, state);
            return true;

        }
        return false;

    }

    public Section getSection(int i) {

        return sections.get(i);
    }

    public void configureCardWall(JFrame frame) {
        CardWallConfiguration configuration = new CardWallConfiguration(frame, true);
        configuration.setCardWallState(state);
        configuration.setVisible(true);


        if (configuration.isDirty()) {
            final CardWallCellClientState newState = configuration.getNewState();
            final String[] titles = configuration.getTitles();
            final boolean layoutChanged = configuration.isLayoutChanged();
            final boolean titleChanged = configuration.isTitleChanged();
//            try {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    if (layoutChanged) {
                        reConfigureWall(newState, true);
                    } else if (titleChanged) {
                        changeTitles(titles);

                    }
                }
            });
//            } catch (InterruptedException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
        }
        configuration.dispose();
    }

    public void reConfigureWall(CardWallCellClientState newState, boolean sendMessage) {


        // remove all existing cards from the master panel
        removeCardPanels();
        // remove all sections
        removeSectionPanels();
        // rebuild the layout
        masterPanel.reconfigurePanel(newState);
        populateData(newState);
        state = newState;
        if (sendMessage) {
            cell.sendMessage(CardWallSyncMessage.COMPLETE_STATE, newState);
        }

    }

    private void removeSectionPanels() {
        for (int i = 0; i < sections.size(); i++) {
            Section section = sections.get(i);
            masterPanel.queueRemovePanel(section.getSectionHeader().getAsComponent());
            section.setSectionHeader(null);
            masterPanel.queueRemovePanel(section.getSelectCard().getAsComponent());
            section.setSelectCard(null);
        }

    }

    private void removeCardPanels() {


        // iterate through all the cards
        for (int i = 0; i < cards.length; i++) {
            for (int j = 0; j < cards[i].length; j++) {
                if (cards[i][j] != null) {
                    masterPanel.queueRemovePanel(cards[i][j].getCardPanel());
                    cards[i][j] = null;
                }
            }
        }


    }

    public void changeSectionTitle(int sectionNumber, String newTitle) {
        Section section = sections.get(sectionNumber);
        section.getSectionHeader().setNewText(newTitle);
        section.getState().setSectionTitle(newTitle);

    }

    /**
     * iterates through all the sections - if the title has changed - modify the title in the section object,
     * the text displayed and send a message to the server
     *
     * @param titles the complete list of titles
     */
    public void changeTitles(String[] titles) {

        for (int i = 0; i < titles.length; i++) {
            if (!sections.get(i).getState().getSectionTitle().equals(titles[i])) {
                changeSectionTitle(i, titles[i]);
                cell.sendMessage(CardWallSyncMessage.UPDATE_SECTION_TITLE, i, titles[i]);
            }
        }


    }

    public void copyToStickNote(CardPosition cardPosition) {
        CardWallCardCellClientState card = getCard(cardPosition.column, cardPosition.row);

        try {
            StickyNoteCellServerState stickyNote = new StickyNoteCellServerState();
            stickyNote.setNoteText(card.getTitle() + "\n" + card.getDetail());
            if (card.getColour() != 0) {
                Color colour = new Color(card.getColour());
                String colourString = colour.getRed() + ":" + colour.getGreen() + ":" + colour.getBlue();
                stickyNote.setColor(colourString);
            }
            try {
                CellUtils.createCell(stickyNote);
            } catch (CellCreationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //To change body of created methods use File | Settings | File Templates.
    }

    public void copyToCardWall(CardPosition cardPosition) {

        List<CardWallCell> cardWallCells = cell.getCardWalls();

        if (cardWallCells.size() > 0) {
            String[] cardWallTitles = new String[cardWallCells.size()];

            for (int i = 0; i < cardWallCells.size(); i++) {
                CardWallCell cardWallCell = cardWallCells.get(i);
                cardWallTitles[i] = cardWallCell.getName();
            }

            JOptionPane.showInputDialog(masterPanel.getParent(), BUNDLE.getString("copyToCardWall.question"),
                    BUNDLE.getString("copyToCardWall.title"), JOptionPane.QUESTION_MESSAGE, null,
                    cardWallTitles, cardWallTitles[0]);

        }
    }
}
