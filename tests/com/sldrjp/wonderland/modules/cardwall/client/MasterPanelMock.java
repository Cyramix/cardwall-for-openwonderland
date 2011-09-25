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

import com.sldrjp.wonderland.modules.cardwall.common.MethodCalled;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCardCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallSectionCellClientState;


import java.awt.*;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: Bob
 * Date: 27/01/11
 * Time: 10:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class MasterPanelMock implements MasterPanel {

    private int showCardCalled = 0;
    private int removeCardCalled = 0;
    private int updateCardCalled = 0;
    private int moveCardCalled = 0;
    private int addSectionCalled = 0;
    private int lastCardX = -1;
    private int lastCardY = -1;


    private CardWallManager cardWallManager;
    private int additionalCardSection;
    private int numberOfCardsAdded;
    private int removedPanels = 0;



    public void showCard(Card card, CardWallManager cardWallManager) {
        showCardCalled++;
        lastCardX = card.getCardState().getColumnID();
        lastCardY = card.getCardState().getRowID();

    }

    public void removeCard(Card card) {
        removeCardCalled++;
    }

    public void updateCard(CardWallCardCellClientState state, Card card) {
        updateCardCalled++;
    }

    public void addSection(CardWallSectionCellClientState sectionState, SectionHeaderImpl sectionHeader, SelectCardImpl selectCard, int noOfColumns) {
        addSectionCalled++;
    }

    public void moveCard(Card card) {
        moveCardCalled++;
    }

    public void resetTests() {
        showCardCalled = 0;
        removeCardCalled = 0;
        updateCardCalled = 0;
        moveCardCalled = 0;
        addSectionCalled = 0;
        lastCardX = -1;
        lastCardY = -1;
    }

    public int getShowCardCalled() {
        return showCardCalled;
    }

    public int getRemoveCardCalled() {
        return removeCardCalled;
    }

    public int getUpdateCardCalled() {
        return updateCardCalled;
    }

    public int getMoveCardCalled() {
        return moveCardCalled;
    }

    public int getAddSectionCalled() {
        return addSectionCalled;
    }

    public int getMethodCalls() {
        return showCardCalled + updateCardCalled + addSectionCalled + moveCardCalled + removeCardCalled;
    }

    public void setCardWallManager(CardWallManager cardWallManager) {
        this.cardWallManager = cardWallManager;
    }

    public CardWallManager getCardWallManager() {
        return cardWallManager;
    }

    public Component add(Component component) {
        return null;

    }

    public int getLastCardX() {
        return lastCardX;
    }

    public int getLastCardY() {
        return lastCardY;
    }

    public void addSection(Section section) {
        SectionHeaderMock sectionHeader = (SectionHeaderMock) getSectionHeader();
        SelectCardMock selectCard = (SelectCardMock) getSelectCard();
        section.setSectionHeader(sectionHeader);
        section.setSelectCard(selectCard);
        selectCard.setMasterPanel(this);
    }

    private SelectCardMock getSelectCard() {
        return new SelectCardMock();  //To change body of created methods use File | Settings | File Templates.
    }

    public SectionHeader getSectionHeader() {
        return new SectionHeaderMock();
    }

    public int getAdditionalCardCount(int i) {
        if (additionalCardSection == i) {
            return numberOfCardsAdded;
        }
        return 0;
    }

    public void repaint() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void cardAddedToAdditionalCards(int sectionNumber) {
        additionalCardSection = sectionNumber;
        numberOfCardsAdded++;
    }

    public void setMinimumSize(Dimension dimension) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void queueRemovePanel(Component c) {
        removedPanels++;
    }

    public int getRemovedPanels() {
        return removedPanels;
    }

    public void resetCountOfRemovedPanels(){
        removedPanels = 0;
    }

    public void removeAndRepaint() {
        MethodCalled.called("removeAndRepaint");
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void reconfigurePanel(CardWallCellClientState clientState) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
