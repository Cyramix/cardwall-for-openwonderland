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

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: bpotter
 * Date: 12-Oct-2010
 * Time: 7:40:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class Card {
    private CardWallCardCellClientState cardState = null;
    private CardPanelImpl cardPanel = null;


    public Card() {
    }

    public Card(CardWallCardCellClientState cardState) {
        this.cardState = cardState;
    }


    public CardWallCardCellClientState getCardState() {
        if (cardState == null) {
            cardState = new CardWallCardCellClientState();
        }
        return cardState;
    }



    public void setCardState(CardWallCardCellClientState cardState) {
        this.cardState = cardState;
    }

    public CardPanelImpl getCardPanel() {
        return cardPanel;
    }

    public void setCardPanel(CardPanelImpl cardPanel) {
        this.cardPanel = cardPanel;
    }

    public CardWallCardCellClientState checkForChanges(Color colour, String titleText, String bodyText, String personText, String pointsText) {

        if (  !(colour.getRGB() == cardState.getColour()) ||!personText.equals(cardState.getPerson()) || !pointsText.equals(cardState.getPoints()) ||
                !titleText.equals(cardState.getTitle()) || !bodyText.equals(cardState.getDetail())) {
            // changes have been made
            cardState.setColour(colour.getRGB());
            cardState.setPerson(personText);
            cardState.setPoints(pointsText);
            cardState.setTitle(titleText);
            cardState.setDetail(bodyText);
            return cardState.getCopyAsClientState();
        }
        return null;
    }
}
