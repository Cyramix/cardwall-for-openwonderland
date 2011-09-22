package com.sldrjp.wonderland.modules.cardwall.client;

import com.sldrjp.wonderland.modules.cardwall.common.CardWallDefaultConfiguration;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientState;
import org.junit.Before;
import org.junit.Test;

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
}

