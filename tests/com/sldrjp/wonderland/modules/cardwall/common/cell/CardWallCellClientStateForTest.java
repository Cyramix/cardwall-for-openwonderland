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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bob
 * Date: 4-Nov-2010
 * Time: 9:24:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class CardWallCellClientStateForTest extends CardWallCellClientState {

    public CardWallCellClientStateForTest() {
        List<CardWallCardCellClientState> cards = new ArrayList<CardWallCardCellClientState>();
        setNumberOfColumns(7);
        setNumberOfRows(4);
        CardWallCardCellClientState card = new CardWallCardCellClientState(0, 0, 0, 1, 99, "Title", "Body Text", "Person Text", "Points Text", null);
        cards.add(card);
        card = new CardWallCardCellClientState(0, 0, 0, 2, 999, "Title", "Body \"Text\"", "Person Text", "Points Text", null);
        cards.add(card);
        setCards(cards);
        List<CardWallSectionCellClientState> sections = new ArrayList<CardWallSectionCellClientState>();
        CardWallSectionCellClientState section = new CardWallSectionCellClientState(0, 1, "section 0");
        sections.add(section);
        section = new CardWallSectionCellClientState(2, 3, "section 1");
        sections.add(section);
        section = new CardWallSectionCellClientState(4, 5, "section 2");
        sections.add(section);
        section = new CardWallSectionCellClientState(6, 7, "section 3");
        sections.add(section);
        setSectionStates(sections);
        setName("test for cardwall");
    }


}
