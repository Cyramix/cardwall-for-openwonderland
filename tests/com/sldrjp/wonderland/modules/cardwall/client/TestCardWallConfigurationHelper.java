package com.sldrjp.wonderland.modules.cardwall.client;

import com.sldrjp.wonderland.modules.cardwall.common.CardWallDefaultConfiguration;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCardCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientState;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * Created by IntelliJ IDEA.
 * User: Bob
 * Date: 18/09/11
 * Time: 1:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestCardWallConfigurationHelper {


    private CardWallConfigurationHelper helper = null;
    private CardWallCellClientState initialState = null;

    @Before
    public void setup() {
        initialState = CardWallDefaultConfiguration.getDefaultState();
        helper = new CardWallConfigurationHelper(initialState);
    }

    @Test
    public void testTestNewModel() throws CardWallDialogDataException {
        // unchanged model
        helper.testNewModel(initialState.getNumberOfRows(), initialState.getNumberOfColumns(), initialState.getSectionStates().size(),
                CardWallDefaultConfiguration.sectionOrderArray, CardWallDefaultConfiguration.sectionTitles, CardWallDefaultConfiguration.layoutArray);
        assertFalse("Model should not report being changed", helper.isChanged());

        try {
            int[] badLayout = {2, 2, 2, 2, 2, 2, 2};
            helper.testNewModel(initialState.getNumberOfRows(), initialState.getNumberOfColumns(), initialState.getSectionStates().size(),
                    CardWallDefaultConfiguration.sectionOrderArray, CardWallDefaultConfiguration.sectionTitles, badLayout);
            fail("should have thrown an exception - incorrect number of rows in layout");
        } catch (CardWallDialogDataException e) {
            assertEquals("Wrong error type", CardWallDialogDataException.CARDWALL_CONFIGURATION_LOST_INTEGRITY, e.getErrorType());
        }

        try {
            int[] badColumnsLayout = {2, 1, 2, 2, 2};
            helper.testNewModel(initialState.getNumberOfRows(), initialState.getNumberOfColumns(), initialState.getSectionStates().size(),
                    CardWallDefaultConfiguration.sectionOrderArray, CardWallDefaultConfiguration.sectionTitles, badColumnsLayout);
            fail("should have thrown exception - bad number of columns in breakdown");
        } catch (CardWallDialogDataException e) {
            assertEquals("Wrong error type", CardWallDialogDataException.CARDWALL_CONFIGURATION_NUMBER_COLUMNS_ERROR, e.getErrorType());
        }


    }

    @Test
    public void testAdjustCardsPositions() {

        CardWallCardCellClientState cardState = new CardWallCardCellClientState(1, 2, 1, 3, 1234, "test", "test detail", "test person", "test points", "test uniqueID");
        CardWallCardCellClientState secondCardState = new CardWallCardCellClientState(2, 4, 1, 2, 1234, "test", "test detail", "test person", "test points", "test uniqueID");

        List<CardWallCardCellClientState> list = new ArrayList<CardWallCardCellClientState>();
        list.add(cardState);
        list.add(secondCardState);

        helper = new CardWallConfigurationHelper(null);
        // delete section
        helper.adjustCardsPositions(list, 1, 0, 4, 2);
        assertEquals("section id should be 0", 0, cardState.getSectionID());
        assertEquals("second card should not have changed", 2, secondCardState.getSectionID());

        // remove row
        helper.adjustCardsPositions(list, 0, 0, 3, 2);
        assertEquals("row id should be -1", -1, cardState.getRowID());
        assertEquals("relative id should be -1", -1, cardState.getRelativeColumnID());
        assertEquals("second card should not have changed", 2, secondCardState.getRowID());

        // remove column from section
        helper.adjustCardsPositions(list, 2, 2, 3, 1);
        assertEquals("relative id should be -1", -1,secondCardState.getRelativeColumnID());



    }
}

