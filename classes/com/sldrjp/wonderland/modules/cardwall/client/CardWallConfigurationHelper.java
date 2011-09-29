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

import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCardCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallSectionCellClientState;

import java.util.ArrayList;
import java.util.Arrays;
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
    private boolean sectionTitleChanged = false;
    private CardWallCellClientState originalState;

    int numberOfRows;
    int numberOfColumns;
    int numberOfSections;
    int[] workingSectionOrder;
    String[] workingTitles;
    int[] workingColumnLayout;


    public CardWallConfigurationHelper(CardWallCellClientState originalState) {
        this.originalState = originalState;
    }

    public boolean testNewModel(int numberOfRows, int numberOfColumns, int numberOfSections, int[] sectionOrder, String[] titles, int[] columnLayout) throws CardWallDialogDataException {
        if (sectionOrder.length != titles.length || columnLayout.length != sectionOrder.length)
            throw new CardWallDialogDataException(CardWallDialogDataException.CARDWALL_CONFIGURATION_LOST_INTEGRITY);

        // check if the number of columns add up
        if (sum(columnLayout) != numberOfColumns) {
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
        List<CardWallSectionCellClientState> sections = originalState.getSectionStates();
        if (numberOfSections != sections.size()) {
            changed = true;
        }

        // now we need to analyze the grid to determine if any titles or the order has changed or if a section has been removed or added


        // first check if the order is the same; at the same time count the number of rows with column values greater than 0
        boolean[] tempLayout = new boolean[columnLayout.length];
        for (int i = 0; i < tempLayout.length; i++) {
            tempLayout[i] = false;
        }
        int numberOfSectionsWithColumns = 0;
        for (int i = 0; i < columnLayout.length; i++) {
            if (columnLayout[i] > 0) {
                numberOfSectionsWithColumns++;
                try {
                    tempLayout[sectionOrder[i]] = true;
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new CardWallDialogDataException(CardWallDialogDataException.CARDWALL_CONFIGURATION_SECTION_OUT_OF_RANGE_ERROR);
                }
            } else {
                // make sure this section can be deleted
                if (!originalState.getSectionStates().get(i).isCanDelete()) {
                    throw new CardWallDialogDataException(CardWallDialogDataException.CARDWALL_CONFIGURATION_CANNOT_DELETE_SECTION);
                }
            }
        }
        if (numberOfSectionsWithColumns != numberOfSections) {
            changed = false;
            throw new CardWallDialogDataException(CardWallDialogDataException.CARDWALL_CONFIGURATION_NUMBER_SECTIONS_ERROR);
        }

        for (int i = 0; i < numberOfSections; i++) {
            if (!tempLayout[i]) {
                throw new CardWallDialogDataException(CardWallDialogDataException.CARDWALL_CONFIGURATION_MISSING_SECTION_ERROR);
            }
        }

        // now compare the existing sections with table
        for (int i = 0; i < sectionOrder.length; i++) {
            if (i == sectionOrder[i]) {
                if (i >= sections.size()) {
                    changed = true;
                } else if (sections.get(i).getNumberOfColumns() != columnLayout[i]) {
                    changed = true;
                } else if (!sections.get(i).getSectionTitle().equals(titles[i])) {
                    sectionTitleChanged = true;
                }
            } else {
                changed = true;
            }
        }

        // are all the sections present


        // preserve the data
        this.numberOfColumns = numberOfColumns;
        this.numberOfRows = numberOfRows;
        this.numberOfSections = numberOfSections;
        workingColumnLayout = columnLayout;
        workingSectionOrder = sectionOrder;
        workingTitles = titles;

        return changed;
    }

    public boolean isChanged() {
        return changed;
    }

    public boolean isSectionTitleChanged() {
        return sectionTitleChanged;
    }

    private int sum(int[] list) {
        int retval = 0;
        for (int i = 0; i < list.length; i++) {
            retval += list[i];
        }
        return retval;
    }

    public CardWallCellClientState getNewState() {
        if (!changed) {
            return originalState;
        }

        List<CardWallSectionCellClientState> originalSections = originalState.getSectionStates();
        CardWallSectionCellClientState[] newSections = new CardWallSectionCellClientState[numberOfSections];

        CardWallCellClientState newState = new CardWallCellClientState();
        newState.setNumberOfColumns(numberOfColumns);
        newState.setNumberOfRows(numberOfRows);

        List<CardWallCardCellClientState> newCards = new ArrayList<CardWallCardCellClientState>();
        for (int i = 0; i < workingColumnLayout.length; i++) {
            if (workingColumnLayout[i] > 0) {
                if (i < originalSections.size()) {
                    CardWallSectionCellClientState section = originalSections.get(i);
                    section.setSectionTitle(workingTitles[i]);
                    section.setNumberOfColumns(workingColumnLayout[i]);
                    section.setStartColumn(-1);
                    section.setEndColumn(-1);
                    section.setColumnPositions(-1);
                    newCards.addAll(adjustCardsPositions(originalState.getCards(), i, workingSectionOrder[i], numberOfRows, section.getNumberOfColumns()));
                    newSections[workingSectionOrder[i]] = section;
                }
            }
        }
        newState.setCards(newCards);
        List<CardWallSectionCellClientState> newSectionsList = Arrays.asList(newSections);
        newState.setSectionStates(newSectionsList);


        // set the starting columns for the sections
        int startingColumn = 0;
        for (int i = 0; i < newSectionsList.size(); i++) {
            CardWallSectionCellClientState cardWallSectionCellClientState = newSectionsList.get(i);
            cardWallSectionCellClientState.setStartColumn(startingColumn);
            startingColumn += cardWallSectionCellClientState.getNumberOfColumns();
            cardWallSectionCellClientState.setEndColumn(startingColumn - 1);

        }

//        newState.setCards(originalState.getCards());
        return newState;
    }

    protected List<CardWallCardCellClientState> adjustCardsPositions(List<CardWallCardCellClientState> cards, int oldSectionID, int newSectionID, int numberOfRows, int sectionNumberOfColumns) {
        List<CardWallCardCellClientState> newCards = new ArrayList<CardWallCardCellClientState>();

        for (int j = 0; j < cards.size(); j++) {
            CardWallCardCellClientState card = cards.get(j);
            if (card.getSectionID() == oldSectionID) {
                CardWallCardCellClientState newCard = card.getCopyAsClientState();
                newCard.setSectionID(newSectionID);
                newCard.setColumnID(-1);
                if ((card.getRowID() >= numberOfRows) || (card.getRelativeColumnID() >= sectionNumberOfColumns)) {
                    newCard.setRelativeColumnID(-1);
                    newCard.setRowID(-1);
                    newCard.setColumnID(-1);
                }
                newCards.add(newCard);
            }
        }
        return newCards;
    }

    public String[] getTitles() {
        return workingTitles;
    }
}

