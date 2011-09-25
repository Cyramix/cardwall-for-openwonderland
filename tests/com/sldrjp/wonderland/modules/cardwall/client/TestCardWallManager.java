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
import com.sldrjp.wonderland.modules.cardwall.common.*;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCardCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientStateForTest;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bob
 * Date: 27/01/11
 * Time: 9:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestCardWallManager {

    private CardWallCellForTests cell;
    private CardWallManager manager;
    private CardWallCellClientState cellState;
    private MasterPanelMock masterPanelMock;

    @Before
    public void setUp() throws Exception {
        MethodCalled.reset();
        cell = new CardWallCellForTests();
        cellState = new CardWallCellClientStateForTest();
        masterPanelMock = new MasterPanelMock();
        manager = new CardWallManager(cell, cellState, masterPanelMock);
        manager.populateData();
        masterPanelMock.resetTests();
        cell.clearMessages();
    }

    @Test
    public void testGetFreeSlotFromColumn() {
        // use a new manager
        CardWallManager helper = new CardWallManager(null, null, new MasterPanelMock());
        Card[] cards = {new Card(), null, new Card()};
        Card[] cardsFirstFree = {null, new Card(), new Card()};
        Card[] cardsLastFree = {new Card(), new Card(), null};
        Card[] cardsNoneFree = {new Card(), new Card(), new Card()};
        assertEquals("should be middle card - wrong slot", 1, helper.getFreeSlotFromColumn(cards));
        assertEquals("should be first card - wrong slot", 0, helper.getFreeSlotFromColumn(cardsFirstFree));
        assertEquals("should be last card - wrong slot", 2, helper.getFreeSlotFromColumn(cardsLastFree));
        assertEquals("no card slot (-1)", -1, helper.getFreeSlotFromColumn(cardsNoneFree));
    }

    @Test
    public void testAddCardToSection() {
        manager.addCard(1);
        CardWallSyncMessage message = cell.getMessages().get(0);
        assertEquals("generated message type incorrect", CardWallSyncMessage.ADD_CARD, message.getMessageType());
        // we need to get the uniqueID to make the comparison work
        String uniqueId = message.getCardClientState().getUniqueID();
        CardWallCardCellClientState expectedCard = new CardWallCardCellClientState(1, 2, 0, 0, 0, null, null, null, null, uniqueId);
        assertEquals("card is not identical", expectedCard, message.getCardClientState());
        assertEquals("Expected one showcard called on Panel", 1, masterPanelMock.getShowCardCalled());
        assertEquals("X position on wall should be ", 2, masterPanelMock.getLastCardX());

        masterPanelMock.resetTests();
        // check for out of range value
        checkSectionRange(manager, 100);
        checkSectionRange(manager, -100);
        assertEquals("No showcard should have been called", 0, masterPanelMock.getShowCardCalled());


    }

    @Test
    public void testAddAdditionalCardToSection() {
        CardWallCardCellClientState cardState = new CardWallCardCellClientState(0, -1, -1, -1, 0, "Test", null, null, null, null);
        manager.getSection(0).getAdditionalCards().add(cardState);
        manager.addCard(0, cardState);
        CardWallSyncMessage message = cell.getMessages().get(0);
        assertEquals("generated message type incorrect", CardWallSyncMessage.MOVE_CARD, message.getMessageType());

    }

    private void checkSectionRange(CardWallManager helper, int sectionSelected) {
        try {
            helper.addCard(sectionSelected);
            fail("Exception should have been thrown - out of range");
        } catch (CardWallException e) {
            assertEquals("Wrong exception ID", CardWallException.SECTION_OUT_OF_RANGE, e.getErrorCode());
        } catch (Exception e) {
            fail("wrong type of exception thrown: was " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testAddExistingCard() throws Exception {

        CardWallCardCellClientState cardState = cellState.getCopyOfCards().get(0);

        try {
            manager.addCard(cardState);
            fail("should have thrown exception - duplicate card at same location");
        } catch (CardWallException e) {
            if (e.getErrorCode() != CardWallException.SAME_CARD_AT_LOCATION) {
                fail("wrong error message");
            }
        }
        assertEquals("No showcard should have been called", 0, masterPanelMock.getShowCardCalled());


        cardState.setTitle("new title");
        cardState.setDetail("new details");

        try {
            manager.addCard(cardState);
            fail("should have thrown exception - another card at same location");
        } catch (CardWallException e) {
            if (e.getErrorCode() != CardWallException.CARD_AT_LOCATION) {
                fail("wrong error message");
            }
        }
        assertEquals("No showcard should have been called", 0, masterPanelMock.getShowCardCalled());

        cardState.setSectionID(3);
        cardState.setColumnID(6);
        cardState.setRelativeColumnID(0);
        cardState.setRowID(2);
        manager.addCard(cardState);
        // find card at 2,3
        CardWallCardCellClientState addedCardState = manager.getCard(6, 2);
        assertEquals("incorrect card added", cardState.toString(), addedCardState.toString());
        assertEquals("Expected one showcard called on Panel", 1, masterPanelMock.getShowCardCalled());

        assertEquals("no messages should have been sent", 0, cell.getMessages().size());
    }

    @Test
    public void testDeleteCard() throws Exception {
        CardWallCardCellClientState cardState = manager.getCard(0, 1).getCopyAsClientState();
        manager.deleteCard(new CardPosition(0, 1));
        assertEquals("Wrong number of cards left", 1, cellState.getCards().size());

        CardWallSyncMessage message = cell.getMessages().get(0);
        assertEquals("Wrong message", CardWallSyncMessage.DELETE_CARD, message.getMessageType());
        assertEquals("Wrong card deleted ", cardState.toString(), message.getCardClientState().toString());
        assertNull("should not have a card at this location", manager.getCard(0, 1));

        assertEquals("Expected only 1 remove call", 1, masterPanelMock.getRemoveCardCalled());
        assertEquals("Expected only 1 method call total", 1, masterPanelMock.getMethodCalls());
    }

    @Test
    public void testRemoveCard() throws Exception {
        CardWallCardCellClientState cardState = manager.getCard(0, 1).getCopyAsClientState();
        manager.removeCard(new CardPosition(0, 1));
        assertEquals("Wrong number of cards left", 1, cellState.getCards().size());

        assertEquals("show have no messages", 0, cell.getMessages().size());
        assertNull("should not have a card at this location", manager.getCard(0, 1));

        assertEquals("Expected only 1 remove call", 1, masterPanelMock.getRemoveCardCalled());
        assertEquals("Expected only 1 method call total", 1, masterPanelMock.getMethodCalls());
    }

    @Test
    public void testUpdateCard() throws Exception {
        CardWallCardCellClientState cardState = manager.getCard(0, 1).getCopyAsClientState();
        cardState.setDetail("New Detail");
        manager.updateCard(cardState);
        CardWallCardCellClientState updatedCardState = manager.getCard(0, 1).getCopyAsClientState();
        assertFalse("make sure the cards are not the same object", updatedCardState.hashCode() == (cardState.hashCode()));
        assertEquals("card was not updated correctly", cardState.toString(), updatedCardState.toString());
        assertEquals("Expected one update card called on Panel", 1, masterPanelMock.getUpdateCardCalled());
        assertEquals("no messages should have been sent", 0, cell.getMessages().size());

    }

    @Test
    public void testGetSectionID() throws Exception {
        assertEquals("should be section 0", 0, manager.getSectionID(1));
        assertEquals("should be section 1", 1, manager.getSectionID(2));

    }

    @Test
    public void testMoveCardWithXY() throws Exception {
        CardWallCardCellClientState cardState = manager.getCard(0, 1).getCopyAsClientState();
        CardPosition existingPosition = cardState.getCardPosition();
        // should be in column 3
        double x = 4 * CardWallDefaultConfiguration.BLOCK_WIDTH - CardWallDefaultConfiguration.BLOCK_WIDTH / 2;
        // should be in row 1
        double y = 2 * CardWallDefaultConfiguration.BLOCK_HEIGHT - CardWallDefaultConfiguration.BLOCK_HEIGHT / 2 + CardWallDefaultConfiguration.TOP_HEIGHT;
        manager.moveCard(existingPosition, x, y);
        assertNull("there should not be a card at 0, 1", manager.getCard(0, 1));
        assertNotNull("there should be a card at 3 ,1", manager.getCard(3, 1));
        CardWallCardCellClientState newCardState = manager.getCard(3, 1).getCopyAsClientState();
        assertEquals("title should be the same", cardState.getTitle(), newCardState.getTitle());
        assertEquals("detail should be the same", cardState.getDetail(), newCardState.getDetail());
        assertEquals("section number should be 1", 1, newCardState.getSectionID());
        assertEquals("relative column position should be 1", 1, newCardState.getRelativeColumnID());
        assertEquals("Expected one move card called on Panel", 1, masterPanelMock.getMoveCardCalled());
        assertEquals("one message should have been sent", 1, cell.getMessages().size());
        CardWallSyncMessage message = cell.getMessages().get(0);
        assertEquals("message should have been move card", CardWallSyncMessage.MOVE_CARD, message.getMessageType());
        assertEquals("state should be the same in the message", newCardState.toString(), message.getCardClientState().toString());

        // try moving another card into this position
        cardState = manager.getCard(0, 2).getCopyAsClientState();
        existingPosition = cardState.getCardPosition();
        try {
            manager.moveCard(existingPosition, x, y);
            fail("should not have permitted the move");
        } catch (CardWallException e) {
            assertEquals("incorrect error code", CardWallException.CANNOT_MOVE, e.getErrorCode());
        } catch (Exception e) {
            fail("wrong exception");
        }
    }

    @Test
    public void testGetCards() throws Exception {
        List<CardWallCardCellClientState> cards = manager.getCards(0);
        assertEquals("should have had 2 cards", 2, cards.size());

    }


    @Test
    public void testReconfigureCardWall() {
        manager.reConfigureWall(null, true);
        assertEquals("Did not remove correct number of panels", 10, masterPanelMock.getRemovedPanels());
        assertTrue("Repaint called", MethodCalled.wasCalled("removeAndRepaint"));
    }
}
