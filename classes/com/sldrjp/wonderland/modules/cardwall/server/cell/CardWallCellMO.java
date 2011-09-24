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

package com.sldrjp.wonderland.modules.cardwall.server.cell;

import com.jme.math.Vector2f;
import com.sldrjp.wonderland.modules.cardwall.common.CardPosition;
import com.sldrjp.wonderland.modules.cardwall.common.CardWallSyncMessage;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCardCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellServerState;
import com.sun.sgs.app.ManagedReference;
import org.jdesktop.wonderland.common.ExperimentalAPI;
import org.jdesktop.wonderland.common.cell.ClientCapabilities;
import org.jdesktop.wonderland.common.cell.state.CellClientState;
import org.jdesktop.wonderland.common.cell.state.CellServerState;
import org.jdesktop.wonderland.modules.appbase.server.cell.App2DCellMO;
import org.jdesktop.wonderland.server.cell.annotation.UsesCellComponentMO;
import org.jdesktop.wonderland.server.comms.WonderlandClientID;
import org.jdesktop.wonderland.server.comms.WonderlandClientSender;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

@ExperimentalAPI
public class CardWallCellMO extends App2DCellMO {

    private static Logger logger = Logger.getLogger(CardWallCellMO.class.getName());

    @UsesCellComponentMO(CardWallCellComponentMO.class)
    private ManagedReference<CardWallCellComponentMO> commComponentRef;
    private CardWallCellClientState stateHolder = new CardWallCellClientState();

    public CardWallCellMO() {
        super();
    }

    @Override
    protected String getClientCellClassName(WonderlandClientID wonderlandClientID, ClientCapabilities clientCapabilities) {
        return "com.sldrjp.wonderland.modules.cardwall.client.cell.CardWallCell";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CellClientState getClientState(CellClientState cellClientState, WonderlandClientID clientID, ClientCapabilities capabilities) {
        if (cellClientState == null) {
            cellClientState = new CardWallCellClientState(pixelScale);
        }

        stateHolder.copyLocal(((CardWallCellClientState) cellClientState));
        return super.getClientState(cellClientState, clientID, capabilities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setServerState(CellServerState state) {
        super.setServerState(state);

        CardWallCellServerState serverState = (CardWallCellServerState) state;

        stateHolder.setNumberOfColumns(serverState.getNumberOfColumns());
        stateHolder.setNumberOfRows(serverState.getNumberOfRows());
        stateHolder.setCards(serverState.getCopyOfCardsAsClientState());
        stateHolder.setSectionStates(serverState.getCopyOfSectionsAsClientState());
        pixelScale = new Vector2f(serverState.getPixelScaleX(), serverState.getPixelScaleY());
    }

    public void setServerState(CardWallCellClientState state) {


        stateHolder.setNumberOfColumns(state.getNumberOfColumns());
        stateHolder.setNumberOfRows(state.getNumberOfRows());
        stateHolder.setCards(state.getCopyOfCards());
        stateHolder.setSectionStates(state.getCopyOfSections());

    }

    private void addCardToServerState(CardWallCardCellClientState card) {
        if (stateHolder.getCards() == null) {
            stateHolder.setCards(new ArrayList<CardWallCardCellClientState>());
        }
        stateHolder.getCards().add(card);
    }


    private void removeCardFromServerState(CardWallCardCellClientState card) {
        List<CardWallCardCellClientState> cards = stateHolder.getCards();
        for (Iterator<CardWallCardCellClientState> iterator = cards.iterator(); iterator.hasNext();) {
            CardWallCardCellClientState aCard = iterator.next();
            if (aCard.equals(card)) {
                cards.remove(aCard);
                return;
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CellServerState getServerState(CellServerState stateToFill) {
        if (stateToFill == null) {
            stateToFill = new CardWallCellServerState();
        }

        super.getServerState(stateToFill);

        CardWallCellServerState state = (CardWallCellServerState) stateToFill;

        state.setPixelScaleX(pixelScale.getX());
        state.setPixelScaleY(pixelScale.getY());
        state.setCards(stateHolder.getCopyOfCardsAsServerState());
        state.setSections(stateHolder.getCopyOfSectionsAsServerState());

        return stateToFill;
    }

    private void updateCard(CardWallCardCellClientState card) {
        List<CardWallCardCellClientState> cards = stateHolder.getCards();
        for (Iterator<CardWallCardCellClientState> iterator = cards.iterator(); iterator.hasNext();) {
            CardWallCardCellClientState aCard = iterator.next();
            if (aCard.equals(card)) {
                aCard.setColour(card.getColour());
                aCard.setTitle(card.getTitle());
                aCard.setDetail(card.getDetail());
                aCard.setPerson(card.getPerson());
                aCard.setPoints(card.getPoints());
                return;
            }
        }

    }

    private void moveCard(CardPosition originalCardPosition, CardWallCardCellClientState card) {
        List<CardWallCardCellClientState> cards = stateHolder.getCards();
        for (Iterator<CardWallCardCellClientState> iterator = cards.iterator(); iterator.hasNext();) {
            CardWallCardCellClientState aCard = iterator.next();
            if (aCard.getUniqueID().equals(card.getUniqueID())) {
                //            if (aCard.positionEquals(originalCardPosition )) {
                aCard.setRowID(card.getRowID());
                aCard.setColumnID(card.getColumnID());
                return;
            }
        }
    }



    public void receivedMessage(WonderlandClientSender sender, WonderlandClientID clientID, CardWallSyncMessage message) {
        CardWallCellComponentMO commComponent = commComponentRef.getForUpdate();
        if (message.getMessageType() > 0) {
            commComponent.sendAllClients(clientID, message);
        }
        switch (message.getMessageType()) {
            case CardWallSyncMessage.COMPLETE_STATE:
                setServerState(message.getClientState());
                break;
            case CardWallSyncMessage.ADD_CARD:
                addCardToServerState(message.getCardClientState());
                break;
            case CardWallSyncMessage.DELETE_CARD:
                removeCardFromServerState(message.getCardClientState());
                break;
            case CardWallSyncMessage.CHANGE_TEXT:
                updateCard(message.getCardClientState());
                break;
            case CardWallSyncMessage.MOVE_CARD:
                moveCard(message.getOriginalCardPosition(), message.getCardClientState());
                break;
            case CardWallSyncMessage.UPDATE_SERVER_CARD_STATE_ONLY:
                updateServerState(message.getCardClientState());
                break;
            case CardWallSyncMessage.UPDATE_SECTION_TITLE:
                stateHolder.getSectionStates().get(message.getSection()).setSectionTitle(message.getText());
                break;



        }

    }

    private void updateServerState(CardWallCardCellClientState card) {
        List<CardWallCardCellClientState> cards = stateHolder.getCards();
        for (Iterator<CardWallCardCellClientState> iterator = cards.iterator(); iterator.hasNext();) {
            CardWallCardCellClientState aCard = iterator.next();
            if (aCard.getUniqueID().equals(card.getUniqueID())) {
                aCard.updateCard(card);
                return;
            }
        }
    }


}
