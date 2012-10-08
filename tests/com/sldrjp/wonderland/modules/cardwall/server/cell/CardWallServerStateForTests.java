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

package com.sldrjp.wonderland.modules.cardwall.server.cell;

import com.sldrjp.wonderland.modules.cardwall.client.cell.CardWallCell;
import com.sldrjp.wonderland.modules.cardwall.common.CardPosition;
import com.sldrjp.wonderland.modules.cardwall.common.CardWallSyncMessage;
import com.sldrjp.wonderland.modules.cardwall.common.cell.*;
import org.jdesktop.wonderland.common.cell.CellStatus;
import org.jdesktop.wonderland.common.cell.state.CellClientState;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: bpotter
 * Date: 16-Oct-2010
 * Time: 2:20:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class CardWallServerStateForTests extends CardWallCellServerState {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(
            "com/sldrjp/wonderland/modules/cardwall/common/resources/Bundle");


    public static String[] sectionTitles = {BUNDLE.getString("default.sectionTitle.section1"),
            BUNDLE.getString("default.sectionTitle.section2"),
            BUNDLE.getString("default.sectionTitle.section3"),
            BUNDLE.getString("default.sectionTitle.section4"),
            BUNDLE.getString("default.sectionTitle.section5")
    };

    public CardWallServerStateForTests() {
        setNumberOfColumns(10);
        setNumberOfRows(4);
        createTestSections();
        createTestCards();
    }

    private void createTestSections() {
        CardWallSectionCellServerState section = new CardWallSectionCellServerState(0,1,sectionTitles[0]);
        sections.add(section);
        section = new CardWallSectionCellServerState(2,3,sectionTitles[1]);
        sections.add(section);
        section = new CardWallSectionCellServerState(4,5,sectionTitles[2]);
        sections.add(section);
        section = new CardWallSectionCellServerState(6,7,sectionTitles[3]);
        sections.add(section);
        section = new CardWallSectionCellServerState(8,9,sectionTitles[4]);
        sections.add(section);

    }


    private void createTestCards() {
        CardWallCardCellServerState card = new CardWallCardCellServerState(0,0,0,0,0,"Test 1", "Detail 1", null, null, null );
        cards.add(card);
        card = new CardWallCardCellServerState(1,3,1,1,0,"Test 2", "Detail 2", null, null, null );
        cards.add(card);
        card = new CardWallCardCellServerState(3,7,1,3,0,"Test 3", "Detail 3", null, null, null );
        cards.add(card);

    }
}
