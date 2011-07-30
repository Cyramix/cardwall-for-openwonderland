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

import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCardCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallSectionCellClientState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bpotter
 * Date: 12-Oct-2010
 * Time: 9:14:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class Section {

    private CardWallSectionCellClientState state;

    private SectionHeader sectionHeader = null;
    private SelectCard selectCard = null;

    private int startColumn;
    private int endColumn;

    private int sectionNumber;
    private List<CardWallCardCellClientState> cardsNotVisible = new ArrayList<CardWallCardCellClientState>();
    private List<Card> cardsOnTheWall = new ArrayList<Card>();


    public Section(int sectionNumber, CardWallSectionCellClientState state) {
        this.sectionNumber = sectionNumber;
        this.startColumn = state.getStartColumn();
        this.endColumn = state.getEndColumn();
        this.state = state;

    }

    public int getStartColumn() {
        return startColumn;
    }

    public void setStartColumn(int startColumn) {
        this.startColumn = startColumn;
    }

    public int getEndColumn() {
        return endColumn;
    }

    public void setEndColumn(int endColumn) {
        this.endColumn = endColumn;
    }

    public CardWallSectionCellClientState getState() {
        return state;
    }

    public void setState(CardWallSectionCellClientState state) {
        this.state = state;
    }

    public SectionHeader getSectionHeader() {
        return sectionHeader;
    }

    public void setSectionHeader(SectionHeader sectionHeader) {
        this.sectionHeader = sectionHeader;
        sectionHeader.setSection(sectionNumber);
        sectionHeader.setText(state.getSectionTitle());
    }

    public SelectCard getSelectCard() {
        return selectCard;
    }

    public void setSelectCard(SelectCard selectCard) {
        this.selectCard = selectCard;
        selectCard.setSectionNumber(sectionNumber);
    }

    public int getNoOfColumns() {
        return state.getEndColumn() - state.getStartColumn() + 1;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public List<CardWallCardCellClientState> getAdditionalCards() {
        return cardsNotVisible;
    }

    public void addAdditionalCard(CardWallCardCellClientState cardState) {
        cardsNotVisible.add(cardState);
        selectCard.newCard(cardState); // always add to the end

    }
}
