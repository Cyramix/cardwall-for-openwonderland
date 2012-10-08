package com.sldrjp.wonderland.modules.cardwall.server;

import com.sldrjp.wonderland.modules.cardwall.common.CardPosition;
import com.sldrjp.wonderland.modules.cardwall.common.CardWallSyncMessage;
import com.sldrjp.wonderland.modules.cardwall.common.cell.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 19/09/12
 * Time: 11:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class CardWallServerManager implements Serializable {

    private CardWallServerSection sections[] = null;
    private CardWallCellClientState state = null;
    private static final Logger logger =
            Logger.getLogger(CardWallServerManager.class.getName());

    public CardWallServerManager(CardWallCellClientState state) {
        // build the card layout based on the server state

        this.state = state;
        List<CardWallSectionCellClientState> clientSectionStates = state.getSectionStates();
        sections = new CardWallServerSection[clientSectionStates.size()];
        // fix if the number of rows or columns is zero
        if ((state.getNumberOfRows() == 0)) {
            //count the columns in each section
            List<CardWallCardCellClientState> cards = state.getCards();
            int maxRow = 0;
            for (int i = 0; i < cards.size(); i++) {
                CardWallCardCellClientState cardState = cards.get(i);
                maxRow = maxRow > cardState.getRowID() ? maxRow : cardState.getRowID();
            }
            state.setNumberOfRows(maxRow < 3 ? 4 : maxRow + 1);

        }
        int numberOfColumns = 0;
        for (int i = 0; i < clientSectionStates.size(); i++) {
            CardWallSectionCellClientState clientSectionState = clientSectionStates.get(i);
            sections[i] = new CardWallServerSection(i, clientSectionState.getNumberOfColumns(), state.getNumberOfRows(), clientSectionState.getStartColumn());
            numberOfColumns += clientSectionState.getNumberOfColumns();

        }

        state.setNumberOfColumns(numberOfColumns);


        // add cards to section
        List<CardWallCardCellClientState> cards = state.getCards();
        for (int i = 0; i < cards.size(); i++) {

            CardWallCardCellClientState cardState = cards.get(i);
            logger.fine(cardState.toString() + "  no sections " + sections.length);
            // version 0.2 imports do not have section ids
            if (cardState.getSectionID() == -1) {
              setSectionAndRelativeID(cardState);
            }
            sections[cardState.getSectionID()].addCard(cardState);
        }
    }

    public void setSectionAndRelativeID(CardWallCardCellClientState card) {
        int column = card.getColumnID();
        int sectionID = 0;
        for (CardWallServerSection section : sections) {
            if ((section.getStartColumn() <= column) && (section.getEndColumn() >= column)) {
                card.setSectionID(sectionID);
                card.setRelativeColumnID(column - section.getStartColumn());
                logger.fine("card modified " + card.toString());
                return;
            }
            sectionID++;
        }
        card.setSectionID(0);
        card.setRelativeColumnID(0);

    }

    private CardWallCardCellClientState findCard(CardWallCardCellClientState card) {
        List<CardWallCardCellClientState> cards = state.getCards();
        for (Iterator<CardWallCardCellClientState> iterator = cards.iterator(); iterator.hasNext(); ) {
            CardWallCardCellClientState aCard = iterator.next();
            logger.fine("aCard: " + aCard.getUniqueID() + " card: " + card.getUniqueID());
            if (aCard.getUniqueID().equals(card.getUniqueID())) {
                return aCard;
            }
        }
        return null;

    }

    public boolean addCardToServerState(CardWallCardCellClientState card) {
        if (state.getCards() == null) {
            state.setCards(new ArrayList<CardWallCardCellClientState>());
        }
        state.getCards().add(card);
        return false;
    }


    public void removeCardFromServerState(CardWallCardCellClientState card) {
        CardWallCardCellClientState aCard = findCard(card);
        if (aCard != null) {
            state.getCards().remove(aCard);
            sections[aCard.getSectionID()].removeCard(aCard);
        }

    }


    public void updateServerState(CardWallCardCellClientState card) {
        CardWallCardCellClientState aCard = findCard(card);
        if (aCard != null) {
            aCard.updateCard(card);
        }
    }


    private void updateCard(CardWallCardCellClientState card) {
        CardWallCardCellClientState aCard = findCard(card);
        if (aCard != null) {
            copyCardContents(card, aCard);
        }

    }

    private boolean moveCard(CardPosition originalCardPosition, CardWallCardCellClientState card) {

        CardWallCardCellClientState aCard = findCard(card);
        if (aCard != null) {
            logger.fine("existing card " + aCard.toString());
            if (sections[card.getSectionID()].isFree(card.getRelativeColumnID(), card.getRowID()))
                sections[aCard.getSectionID()].removeCard(aCard);
            aCard.setRowID(card.getRowID());
            aCard.setRelativeColumnID(card.getRelativeColumnID());
            aCard.setSectionID(card.getSectionID());
            sections[aCard.getSectionID()].addCard(aCard);
            logger.fine("new card " + card.toString());
            return true;
        }
        return false;
    }


    public synchronized boolean processMessage(CardWallSyncMessage message) {
        logger.fine("message " + message.getMessageType());
        CardWallCardCellClientState card = null;
        switch (message.getMessageType()) {
            case CardWallSyncMessage.REQUEST_NEW_CARD:
                logger.fine("request new card " + message.getSection());
                card = getSection(message.getSection()).addNewCard();
                if (card == null) {
                    return false;
                }
                addCardToServerState(card);
                message.setCardClientState(card);
                message.setMessageType(CardWallSyncMessage.ADD_CARD);
                logger.fine("request new card " + card.toString());
                break;
            case CardWallSyncMessage.INSERT_CARD:
                logger.fine("insert card " + message.getSection());
                CardWallCardCellClientState cardToInsert = message.getCardClientState();
                card = getSection(cardToInsert.getSectionID()).addNewCard();
                if (card == null) {
                    return false;
                }
                // copy card
                copyCardContents(cardToInsert, card);
                addCardToServerState(card);
                message.setCardClientState(card);
                message.setMessageType(CardWallSyncMessage.ADD_CARD);
                logger.fine("insert card " + card.toString());
                break;

            case CardWallSyncMessage.REQUEST_MOVE_CARD:
                if (moveCard(message.getOriginalCardPosition(), message.getCardClientState())) {
                    message.setMessageType(CardWallSyncMessage.MOVE_CARD);
                    return true;
                }
                return false;
            case CardWallSyncMessage.DELETE_CARD:
                removeCardFromServerState(message.getCardClientState());
                break;
            case CardWallSyncMessage.CHANGE_TEXT:
                updateCard(message.getCardClientState());
                break;
            case CardWallSyncMessage.UPDATE_SERVER_CARD_STATE_ONLY:
                updateServerState(message.getCardClientState());
                break;
            case CardWallSyncMessage.UPDATE_SECTION_TITLE:
                state.getSectionStates().get(message.getSection()).setSectionTitle(message.getText());
                break;
            case CardWallSyncMessage.REQUEST_RESTORE_CARD:
                if (getSection(message.getCardClientState().getSectionID()).restoreCard(message.getCardClientState())) {
                    message.setMessageType(CardWallSyncMessage.RESTORE_CARD);
                    return true;
                }
                return false;

        }
        return true;
    }

    private void copyCardContents(CardWallCardCellClientState cardToCopy, CardWallCardCellClientState returnCard) {
        returnCard.setColour(cardToCopy.getColour());
        returnCard.setTitle(cardToCopy.getTitle());
        returnCard.setDetail(cardToCopy.getDetail());
        returnCard.setPerson(cardToCopy.getPerson());
        returnCard.setPoints(cardToCopy.getPoints());
    }


    // Methods for testing
    protected int getNumberOfSections() {
        int count = 0;
        for (int i = 0; i < sections.length; i++) {
            if (sections[i] != null) {
                count++;
            }
        }
        return count;
    }

    protected CardWallServerSection getSection(int i) {
        return sections[i];
    }

}
