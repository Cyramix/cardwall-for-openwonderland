package com.sldrjp.wonderland.modules.cardwall.server;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 19/09/12
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */



import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCardCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCardCellServerState;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class TestCardWallServerSection {

    CardWallServerSection testSection = null;

    @Before
    public void setup() throws Exception {
       testSection = new CardWallServerSection(1,2,4,0);

    }

    @Test
    public void testAddCard() throws Exception {
        CardWallCardCellClientState card = new CardWallCardCellClientState(1,0,1,2,0,"Test", null, null, null, null);
        testSection.addCard(card);
        CardWallCardCellClientState returnedCard = testSection.getCardAt(1,2);
        assertNotNull("card is not in the right location", returnedCard);

    }

    @Test
    public void testAddNewCard() throws Exception {
        CardWallCardCellServerState card = new CardWallCardCellServerState(1,0,1,2,0,"Test", null, null, null, null);
        testSection.addNewCard();
        CardWallCardCellClientState returnedCard = testSection.getCardAt(0,0);
        assertNotNull("card is not in the right location", returnedCard);
    }


}
