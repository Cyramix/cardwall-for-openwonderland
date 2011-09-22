package com.sldrjp.wonderland.modules.cardwall.client;

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

import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallSectionCellClientState;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bob
 * Date: 18/09/11
 * Time: 12:59 PM
 * To change this template use File | Settings | File Templates.
 */

public class CardWallConfigurationHelper {

    private boolean changed = false;
    private CardWallCellClientState originalState;

    int numberOfRows;
    int numberOfColumns;
    int numberOfSections;
    int [] workingSectionOrder;
    String [] workingTitles;
    int [] workingColumnLayout;



    public CardWallConfigurationHelper(CardWallCellClientState originalState) {
        this.originalState = originalState;
    }

    public boolean testNewModel(int numberOfRows, int numberOfColumns, int numberOfSections, int [] sectionOrder, String[] titles, int[] columnLayout ) throws CardWallDialogDataException {
        if (sectionOrder.length != titles.length || columnLayout.length != sectionOrder.length)
          throw new CardWallDialogDataException(CardWallDialogDataException.CARDWALL_CONFIGURATION_LOST_INTEGRITY);

        // check if the number of columns add up
        if (sum (columnLayout) != numberOfColumns){
          throw new CardWallDialogDataException(CardWallDialogDataException.CARDWALL_CONFIGURATION_NUMBER_COLUMNS_ERROR);
        }

        // has the number of rows changed
        if (numberOfRows != originalState.getNumberOfRows()) {
            changed = true;
        }
        // has the number of columns changed
        if (numberOfColumns != originalState.getNumberOfColumns()) {
            changed = true;
        }

        // has the number of sections changed
        List <CardWallSectionCellClientState> sections = originalState.getSectionStates();
        if(numberOfSections != sections.size()) {
          changed = true;
        }

        // now we need to analyze the grid to determine if any titles or the order has changed or if a section has been removed or added


        // first check if the order is the same
        for (int i = 0; i<sectionOrder.length;i++) {
            if (i != sectionOrder[i]){
                changed = true;
            }
        }

        // are there more rows in the working arrays than sections (indicates that a section has been deleted)





        // preserve the data
        this.numberOfColumns = numberOfColumns;
        this.numberOfRows = numberOfRows;
        this.numberOfSections = numberOfSections;
        workingColumnLayout = columnLayout;
        workingSectionOrder = sectionOrder;
        workingTitles = titles;


        return true;
    }

    public boolean isChanged() {
        return changed;
    }

    private int sum (int[] list) {
        int retval = 0;
        for(int i=0; i < list.length; i++){
          retval += list[i];
        }
        return retval;
    }

    public CardWallCellClientState getNewState() {
        if (!changed) {
            return originalState;
        }
        return new CardWallCellClientState();
    }
}

