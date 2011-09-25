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

import java.io.Serializable;


/**
 * Created by IntelliJ IDEA.
 * User: bpotter
 * Date: 3-Oct-2010
 * Time: 3:29:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class CardWallSectionCellClientState implements StateConvertable, Serializable {

    private int numberOfColumns = -1;
    private int startColumn = -1;
    private int endColumn = -1;
    private String sectionTitle = "";
    private int numberOfCards = 0;

    public CardWallSectionCellClientState(int startColumn, int endColumn, String sectionTitle) {
        this.startColumn = startColumn;
        this.endColumn = endColumn;
        this.sectionTitle = sectionTitle;
        numberOfColumns = endColumn - startColumn + 1;
    }

    public CardWallSectionCellClientState(int numberOfColumns, String sectionTitle) {
        this.numberOfColumns = numberOfColumns;
        this.sectionTitle = sectionTitle;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public void setStartColumn(int startColumn) {
        this.startColumn = startColumn;
    }

    public void setColumnPositions(int startColumn) {
        this.startColumn = startColumn;
        this.endColumn = startColumn + numberOfColumns - 1;


    }

    public int getEndColumn() {
        return endColumn;
    }

    public void setEndColumn(int endColumn) {
        this.endColumn = endColumn;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public CardWallSectionCellClientState getCopyAsClientState() {
        CardWallSectionCellClientState copyOfState = new CardWallSectionCellClientState(startColumn, endColumn, sectionTitle);
        return copyOfState;
    }

    public CardWallSectionCellServerState getCopyAsServerState() {
        CardWallSectionCellServerState copyOfState = new CardWallSectionCellServerState(startColumn, endColumn, sectionTitle);
        return copyOfState;
    }

    public void incrementCards(){
        numberOfCards++;
    }

    public void decrementCards() {
        numberOfCards--;
    }

    public boolean isCanDelete() {
        return numberOfCards <= 0;
    }


}
