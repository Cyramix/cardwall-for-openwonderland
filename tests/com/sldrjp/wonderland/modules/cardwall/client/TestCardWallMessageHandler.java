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

import com.sldrjp.wonderland.modules.cardwall.client.cell.CardWallCellForTests;
import com.sldrjp.wonderland.modules.cardwall.common.CardPosition;
import com.sldrjp.wonderland.modules.cardwall.common.CardWallSyncMessage;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCardCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientStateForTest;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * Created by IntelliJ IDEA.
 * User: Bob
 * Date: 30/01/11
 * Time: 1:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestCardWallMessageHandler {
    private CardWallMessageHandler handler;
    private CardWallCellForTests cell;
    private CardWallManager manager;
    private CardWallCellClientState cellState;
    private MasterPanelMock masterPanelMock;

    @Before
    public void setUp() throws Exception {
        cell = new CardWallCellForTests();
        cellState = new CardWallCellClientStateForTest();
        masterPanelMock = new MasterPanelMock();
        manager = new CardWallManager(cell, cellState, masterPanelMock);
        handler = new CardWallMessageHandler(manager);
        manager.populateData();
        masterPanelMock.resetTests();
        cell.clearMessages();
    }

    @Test
    public void testAddMessage() throws Exception {
        CardWallSyncMessage message = new CardWallSyncMessage(CardWallSyncMessage.ADD_CARD);
        CardWallCardCellClientState cardState = new CardWallCardCellClientState(3, 2, 0, 2, 0, "title", "detail", null, null, null);
        message.setCardClientState(cardState);
        handler.handleMessage(message);
        assertEquals("Should have three cards", 3, cellState.getCards().size());
        CardWallCardCellClientState addedCardState = manager.getCard(6, 2);
        assertEquals("incorrect card added", cardState.toString(), addedCardState.toString());
        assertEquals("Expected one showcard called on Panel", 1, masterPanelMock.getShowCardCalled());
        assertEquals("no messages should have been sent", 0, cell.getMessages().size());


    }

    @Test
    public void testAddHiddenCardMessage() throws Exception {
        CardWallSyncMessage message = new CardWallSyncMessage(CardWallSyncMessage.ADD_CARD);
        CardWallCardCellClientState cardState = new CardWallCardCellClientState(3, -1, -1, -1, 0, "title", "detail", null, null, null);
        message.setCardClientState(cardState);
        handler.handleMessage(message);
        assertEquals("Should have three cards", 3, cellState.getCards().size());
        List<CardWallCardCellClientState> cards = manager.getAdditionalCards(3);
        assertEquals("should have one card not displayed", 1, cards.size());
        CardWallCardCellClientState addedCardState = cards.get(0);
        assertEquals("incorrect card added", cardState.toString(), addedCardState.toString());
        assertEquals("Expected no showcard to be called on Panel", 0, masterPanelMock.getShowCardCalled());
        assertEquals("Expected 1 additional card to be added to the additional card control for section 3", 1,
                masterPanelMock.getAdditionalCardCount(3));
        assertEquals("no messages should have been sent", 0, cell.getMessages().size());


    }

    @Test
    public void testMoveHiddenCardMessage() throws Exception {
        CardWallCardCellClientState cardState = manager.getCard(0, 1);
        String uniqueID = cardState.getUniqueID(); // force the cell to set its uniqueID
        CardPosition initialPosition = cardState.getCardPosition();
        manager.archiveCard(initialPosition);  // quick way to force the card into the archive list
        cell.clearMessages(); // we would have sent a message at this point
        cardState = cardState.getCopyAsClientState();
        cardState.setSectionID(0);
        cardState.setColumnID(1);
        cardState.setRowID(1);
        CardWallSyncMessage message = new CardWallSyncMessage(CardWallSyncMessage.MOVE_CARD);
        message.setCardClientState(cardState.getCopyAsClientState());
        message.setOriginalCardPosition(null);
        handler.handleMessage(message);
        CardWallCardCellClientState newCardState = manager.getCard(1, 1);
        assertEquals("title should be the same", cardState.getTitle(), newCardState.getTitle());
        assertEquals("detail should be the same", cardState.getDetail(), newCardState.getDetail());
        assertEquals("Expected one move card called on Panel", 1, masterPanelMock.getMoveCardCalled());
        assertEquals("no messages should have been sent", 0, cell.getMessages().size());
        assertEquals("one item was removed from the select form list", 1, ((SelectCardMock )manager.getSection(0).getSelectCard()).getItemsRemoved());
    }


    @Test
    public void testArchiveCardMessage() throws Exception {
        CardWallCardCellClientState originalCardState = manager.getCard(0,1);
        CardWallCardCellClientState cardState = originalCardState.getCopyAsClientState();
        cardState.setColumnID(-1);
        cardState.setRowID(-1);
        CardWallSyncMessage message = new CardWallSyncMessage(CardWallSyncMessage.MOVE_CARD);
        message.setCardClientState(cardState);
        message.setOriginalCardPosition(originalCardState.getCardPosition());
        assertEquals (" should have no cards in the additional cards", 0,manager.getSection(0).getAdditionalCards().size());
        handler.handleMessage(message);
        assertNull("should not be a card at 0, 1", manager.getCard(0,1));
        assertEquals (" should have one card in the additional cards", 1,manager.getSection(0).getAdditionalCards().size());
        assertEquals("cards are not the same", originalCardState.getUniqueID(), manager.getSection(0).getAdditionalCards().get(0).getUniqueID());
        assertEquals("no messages should have been sent", 0, cell.getMessages().size());


    }




    @Test
    public void testRemoveMessage() throws Exception {
        CardWallCardCellClientState cardToRemove = cellState.getCards().get(0).getCopyAsClientState();
        CardWallSyncMessage message = new CardWallSyncMessage(CardWallSyncMessage.DELETE_CARD);
        message.setCardClientState(cardToRemove);
        handler.handleMessage(message);
        assertEquals("Should only have one card left", 1, cellState.getCards().size());
        assertFalse("wrong card removed", cardToRemove.toString().equals(cellState.getCards().get(0).toString()));
        assertEquals("Expected one remove card called on Panel", 1, masterPanelMock.getRemoveCardCalled());
        assertEquals("no messages should have been sent", 0, cell.getMessages().size());

    }

    @Test
    public void testUpdateMessage() throws Exception {
        CardWallCardCellClientState cardState = manager.getCard(0, 1).getCopyAsClientState();
        cardState.setDetail("New Detail");
        CardWallSyncMessage message = new CardWallSyncMessage(CardWallSyncMessage.CHANGE_TEXT);
        message.setCardClientState(cardState);
        handler.handleMessage(message);
        CardWallCardCellClientState updatedCardState = manager.getCard(0, 1);
        assertFalse("make sure the cards are not the same object", updatedCardState.hashCode() == (cardState.hashCode()));
        assertEquals("card was not updated correctly", cardState.toString(), updatedCardState.toString());
        assertEquals("Expected one update card called on Panel", 1, masterPanelMock.getUpdateCardCalled());
        assertEquals("no messages should have been sent", 0, cell.getMessages().size());
    }


    @Test
    public void testMoveCardMessage() throws Exception {
        CardWallCardCellClientState cardState = manager.getCard(0, 1).getCopyAsClientState();
        CardPosition initialPosition = cardState.getCardPosition();
        cardState.setSectionID(1);
        cardState.setColumnID(3);
        cardState.setRowID(1);
        CardWallSyncMessage message = new CardWallSyncMessage(CardWallSyncMessage.MOVE_CARD);
        message.setCardClientState(cardState.getCopyAsClientState());
        message.setOriginalCardPosition(initialPosition);
        handler.handleMessage(message);
        assertNull("should no longer be a card at 0,1", manager.getCard(0,1));
        CardWallCardCellClientState newCardState = manager.getCard(3, 1);
        assertEquals("title should be the same", cardState.getTitle(), newCardState.getTitle());
        assertEquals("detail should be the same", cardState.getDetail(), newCardState.getDetail());
        assertEquals("Expected one move card called on Panel", 1, masterPanelMock.getMoveCardCalled());
        assertEquals("no messages should have been sent", 0, cell.getMessages().size());

    }
}
