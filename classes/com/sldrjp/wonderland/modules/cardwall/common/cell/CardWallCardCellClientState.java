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

import com.sldrjp.wonderland.modules.cardwall.common.CardPosition;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: bpotter
 * Date: 3-Oct-2010
 * Time: 3:31:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class CardWallCardCellClientState implements Serializable, StateConvertable {

    private String title = null;
    private String detail = null;
    private String person = null;
    private String points = null;
    private int colour = 0;
    private int sectionID = -1;
    private int columnID = -1;
    private int relativeColumnID = -1;
    private int rowID = -1;
    private String uniqueID = null;


    public CardWallCardCellClientState() {
    }

    public CardWallCardCellClientState(int sectionID, int columnID, int relativeColumnID, int rowID, int colour, String title, String detail, String person, String points, String uniqueID) {
        updateCard(sectionID, columnID, relativeColumnID, rowID, colour, title, detail, person, points);
        this.uniqueID = uniqueID;
    }

    public void updateCard(int sectionID, int columnID, int relativeColumnID, int rowID, int colour, String title, String detail, String person, String points) {
        this.sectionID = sectionID;
        this.columnID = columnID;
        this.relativeColumnID = relativeColumnID;
        this.rowID = rowID;
        this.colour = colour;
        this.detail = detail;
        this.title = title;
        this.person = person;
        this.points = points;
    }

    public String getDetail() {
        if (detail == null) {
            detail = "";
        }
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTitle() {
        if (title == null) {
            title = "";
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPerson() {
        if (person == null) {
            person = "";
        }
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getPoints() {
        if (points == null) {
            points = "";
        }
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public int getColour() {
        return colour;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    public int getColumnID() {
        return columnID;
    }

    public void setColumnID(int columnID) {
        this.columnID = columnID;
    }

    public int getRelativeColumnID() {
        return relativeColumnID;
    }

    public void setRelativeColumnID(int relativeColumnID) {
        this.relativeColumnID = relativeColumnID;
    }

    public int getRowID() {
        return rowID;
    }

    public void setRowID(int rowID) {
        this.rowID = rowID;
    }

    public CardPosition getCardPosition() {
        return new CardPosition(columnID, rowID);
    }

    public int getSectionID() {
        return sectionID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    public CardWallCardCellClientState getCopyAsClientState() {
        CardWallCardCellClientState copyOfState = new CardWallCardCellClientState(sectionID, columnID, relativeColumnID, rowID, colour, title, detail, person, points, getUniqueID());
        return copyOfState;
    }

    public String getKey() {
        String id = Integer.toString(sectionID) + "-" + Integer.toString(columnID) + "-" + Integer.toString(rowID);
        return id;
    }

    public CardWallCardCellServerState getCopyAsServerState() {
        CardWallCardCellServerState copyOfState = new CardWallCardCellServerState(sectionID, columnID, relativeColumnID, rowID, colour, title, detail, person, points, getUniqueID());
        return copyOfState;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }
        CardWallCardCellClientState card = (CardWallCardCellClientState) obj;
        return (card.getUniqueID().equals(uniqueID));
//        return positionEquals(card.getCardPosition());
    }

    public boolean positionEquals(CardPosition testPosition) {
        return (testPosition.row == getRowID() && testPosition.column == getColumnID());
    }

    @Override
    public String toString() {
        return "CardWallCardCellClientState: Section ID: " + "," + sectionID + ", Column ID: " + columnID
                + ", Relative Column ID: " + relativeColumnID + ", Row ID: " + rowID + ", Colour: "
                + colour + ", Title: " + title + ", Detail: " + detail;
    }

    public boolean isVisible() {
        return rowID > -1;
    }

    public String getUniqueID() {
        if (uniqueID == null) {
            uniqueID = String.valueOf(UUID.randomUUID());
        }
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        if (uniqueID == null) {
            this.uniqueID = String.valueOf(UUID.randomUUID());
        } else {
            this.uniqueID = uniqueID;
        }
    }

    public void updateCard(CardWallCardCellClientState card) {
        updateCard(card.getSectionID(), card.getColumnID(), relativeColumnID, card.getRowID(), card.getColour(), card.getTitle(),
                card.getDetail(), card.getPerson(), card.getPoints());
    }
}

