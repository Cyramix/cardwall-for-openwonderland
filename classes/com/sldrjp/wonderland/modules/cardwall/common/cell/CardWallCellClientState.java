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

package com.sldrjp.wonderland.modules.cardwall.common.cell;

import com.jme.math.Vector2f;
import com.sldrjp.wonderland.modules.cardwall.common.CardWallDefaultConfiguration;
import org.jdesktop.wonderland.common.ExperimentalAPI;
import org.jdesktop.wonderland.modules.appbase.common.cell.App2DCellClientState;

import java.io.Serializable;
import java.util.*;

@ExperimentalAPI
public class CardWallCellClientState extends App2DCellClientState implements Serializable {

    private int numberOfColumns;
    private int numberOfRows;

    private List<CardWallSectionCellClientState> sectionStates = null;
    private List<CardWallCardCellClientState> cards = null;


    public CardWallCellClientState() {
        this(null);
    }

    public CardWallCellClientState(Vector2f pixelScale) {
        super(pixelScale);
    }

    public int getPreferredWidth() {

        return numberOfColumns * (CardWallDefaultConfiguration.SPACER_WIDTH + CardWallDefaultConfiguration.BLOCK_WIDTH)
                + sectionStates.size() * (CardWallDefaultConfiguration.INTERSECTION_WIDTH + CardWallDefaultConfiguration.SPACER_WIDTH)
                + CardWallDefaultConfiguration.preferredWidthBase;

    }

    public int getPreferredHeight() {
        return numberOfRows * (CardWallDefaultConfiguration.SPACER_HEIGHT + CardWallDefaultConfiguration.BLOCK_HEIGHT)
                + CardWallDefaultConfiguration.preferredHeightBase;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public List<CardWallSectionCellClientState> getSectionStates() {
        if (sectionStates == null) {
            sectionStates = new ArrayList<CardWallSectionCellClientState>();
        }
        return sectionStates;
    }

    public void setSectionStates(List<CardWallSectionCellClientState> sectionStates) {
        this.sectionStates = sectionStates;
    }

    public List<CardWallCardCellClientState> getCards() {
        if (cards == null) {
            cards = new ArrayList<CardWallCardCellClientState>();
        }
        return cards;
    }

    public void setCards(List<CardWallCardCellClientState> cards) {
        this.cards = cards;
    }

    public void copyLocal(CardWallCellClientState stateHolder) {
        stateHolder.setNumberOfColumns(getNumberOfColumns());
        stateHolder.setNumberOfRows(getNumberOfRows());
        stateHolder.setSectionStates(getCopyOfSections());
        stateHolder.setCards(getCopyOfCards());

    }

    public List<CardWallCardCellClientState> getCopyOfCards() {
        return ListConverter.copyAsClientState(CardWallCardCellClientState.class, getCards());
    }

    public List<CardWallCardCellServerState> getCopyOfCardsAsServerState() {
        return ListConverter.copyAsServerState(CardWallCellServerState.class, getCards());
    }

    public List<CardWallSectionCellClientState> getCopyOfSections() {
        return ListConverter.copyAsClientState(CardWallSectionCellClientState.class, getSectionStates());
    }

    public List<CardWallSectionCellServerState> getCopyOfSectionsAsServerState() {
        return ListConverter.copyAsServerState(CardWallSectionCellServerState.class, getSectionStates());

    }


}
