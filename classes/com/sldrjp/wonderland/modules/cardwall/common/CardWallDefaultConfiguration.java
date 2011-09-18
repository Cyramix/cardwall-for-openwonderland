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

package com.sldrjp.wonderland.modules.cardwall.common;

import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallSectionCellClientState;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CardWallDefaultConfiguration {


            private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(
            "com/sldrjp/wonderland/modules/cardwall/common/resources/Bundle");


    public static int TOP_HEIGHT = 50;
    public static int BOTTOM_HEIGHT = 50;
    public static int SPACER_HEIGHT = 15;
    public static int BLOCK_HEIGHT = 200;
    public static int BLOCK_WIDTH = 200;
    public static int INTERSECTION_WIDTH = 10;
    public static int SPACER_WIDTH = 5;

    public static int preferredWidthBase = SPACER_WIDTH;

    public static int preferredHeightBase = TOP_HEIGHT + SPACER_HEIGHT + BOTTOM_HEIGHT;


    public static CardWallCellClientState getDefaultState() {

        int DEFAULT_NUMBER_ROWS = 4;

        CardWallCellClientState defaultState = new CardWallCellClientState();
        
        List<CardWallSectionCellClientState> sections = new ArrayList<CardWallSectionCellClientState>();

        CardWallSectionCellClientState section = new CardWallSectionCellClientState(0,1,BUNDLE.getString("default.sectionTitle.section1"));
        sections.add(section);
        int columns = 2;

        section = new CardWallSectionCellClientState(2,3,BUNDLE.getString("default.sectionTitle.section2"));
        sections.add(section);
        columns += 2;

        section = new CardWallSectionCellClientState(4,5,BUNDLE.getString("default.sectionTitle.section3"));
        sections.add(section);
        columns += 2;

        section = new CardWallSectionCellClientState(6,7,BUNDLE.getString("default.sectionTitle.section4"));
        sections.add(section);
        columns += 2;

        section = new CardWallSectionCellClientState(8,9,BUNDLE.getString("default.sectionTitle.section5"));
        sections.add(section);
        columns +=2;

        defaultState.setSectionStates(sections);
        defaultState.setNumberOfColumns(columns);
        defaultState.setNumberOfRows(DEFAULT_NUMBER_ROWS);

        return defaultState;
    }

}
