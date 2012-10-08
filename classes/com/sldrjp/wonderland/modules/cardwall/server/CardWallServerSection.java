package com.sldrjp.wonderland.modules.cardwall.server;

import com.sldrjp.wonderland.modules.cardwall.common.CardPosition;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCardCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCardCellServerState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallSectionCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallSectionCellServerState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 19/09/12
 * Time: 11:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class CardWallServerSection implements Serializable {

    private CardWallCardCellClientState[][] displayedCards;
    private List<CardWallCardCellClientState> additionalCards;
    private int sectionID;
    private int startColumn;
    private int numberOfRows;
    private int numberOfColumns;
    private static final Logger logger =
            Logger.getLogger(CardWallServerSection.class.getName());

    public CardWallServerSection(int sectionID, int numberOfColumns, int numberOfRows, int startColumn) {
        logger.fine("section ID" + sectionID + " number of columns " + numberOfColumns + " number of rows " + numberOfRows);
        displayedCards = new CardWallCardCellClientState[numberOfColumns][numberOfRows];
        additionalCards = new ArrayList<CardWallCardCellClientState>();
        this.numberOfColumns = numberOfColumns;
        this.startColumn = startColumn;
        this.numberOfRows = numberOfRows;
        this.sectionID = sectionID;
    }

    public CardWallCardCellClientState addNewCard() {
        // find a free space
        CardPosition position = findFreeSpace();
        if (position != null) {
            CardWallCardCellClientState state = new CardWallCardCellClientState();
            state.setSectionID(sectionID);
            state.setRowID(position.row);
            state.setRelativeColumnID(position.column);
            state.setUniqueID(null);
            displayedCards[position.column][position.row] = state;
            return state;
        }
        return null;
    }

    private CardPosition findFreeSpace() {
        for (int i = 0; i < numberOfColumns; i++) {
            for (int j = 0; j < numberOfRows; j++) {
                if (displayedCards[i][j] == null) {
                    return new CardPosition(i, j);
                }
            }
        }

        return null;
    }

    public void addCard(CardWallCardCellClientState cardState) {
        logger.fine("add card " + cardState.getUniqueID());
        if (cardState.getRelativeColumnID() >= 0 && cardState.getRowID() >= 0) {
            displayedCards[cardState.getRelativeColumnID()][cardState.getRowID()] = cardState;
        } else {
            logger.fine("add card to additional " + cardState.getUniqueID());
            additionalCards.add(cardState);
        }
    }

    protected CardWallCardCellClientState getCardAt(int i, int i1) {
        return displayedCards[i][i1];
    }

    protected CardWallCardCellClientState findFromAdditionalCards(CardWallCardCellClientState card) {
        for (int i = 0; i < additionalCards.size(); i++) {
            CardWallCardCellClientState aCard = additionalCards.get(i);
            logger.fine("card: " + card.getUniqueID() + " aCArd " + aCard.getUniqueID());
            if (card.getUniqueID().equals(aCard.getUniqueID())) {
                return aCard;
            }

        }
        return null;
    }

    public void removeCard(CardWallCardCellClientState aCard) {
        if ((aCard.getRelativeColumnID() >= 0) && (aCard.getRowID() >= 0)) {
            displayedCards[aCard.getRelativeColumnID()][aCard.getRowID()] = null;
        } else {
            additionalCards.remove(aCard);
        }

    }

    public boolean isFree(int relativeColumnID, int rowID) {
        if ((relativeColumnID >= 0) && (rowID >= 0)) {
            if (displayedCards[relativeColumnID][rowID] == null) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean restoreCard(CardWallCardCellClientState cardClientState) {
        CardPosition position = findFreeSpace();
        if (position != null) {
            CardWallCardCellClientState aCard = findFromAdditionalCards(cardClientState);
            aCard.setRelativeColumnID(position.column);
            aCard.setRowID(position.row);
            cardClientState.setRelativeColumnID(position.column);
            cardClientState.setRowID(position.row);
            displayedCards[position.column][position.row] = aCard;
            additionalCards.remove(aCard);
            return true;
        }
        return false;  //To change body of created methods use File | Settings | File Templates.
    }

    public int getEndColumn() {
        return startColumn + numberOfColumns - 1;
    }

    public int getStartColumn() {
        return startColumn;
    }
}