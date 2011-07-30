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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: bpotter
 * Date: 3-Oct-2010
 * Time: 3:31:08 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "cell")
public class CardWallCardCellServerState  implements Serializable, StateConvertable {

    @XmlElement(name = "title")
    public String title = null;
    @XmlElement(name = "detail")
    public String detail = null;
    @XmlElement(name = "person")
    public String person = null;
    @XmlElement(name = "points")
    public String points = null;
    @XmlElement(name = "colour")
    public int colour = 0;
    @XmlElement(name = "sectionID")
    public int sectionID = -1;
    @XmlElement(name = "columnID")
    public int columnID = -1;
    @XmlElement(name = "relativeColumnID")
    private int relativeColumnID = -1;
    @XmlElement(name = "rowID")
    public int rowID = -1;
    @XmlElement(name = "uniqueID")
    public String uniqueID = null;

    public CardWallCardCellServerState() {
    }


    public CardWallCardCellServerState(int sectionID, int columnID, int relativeColumnID, int rowID, int colour, String title, String detail, String person, String points, String uniqueID) {
        this.sectionID = sectionID;
        this.columnID = columnID;
        this.relativeColumnID = relativeColumnID;
        this.rowID = rowID;
        this.detail = detail;
        this.title = title;
        this.colour = colour;
        this.points = points;
        this.person = person;
        this.uniqueID = uniqueID;

    }

    @XmlTransient
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @XmlTransient
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Deprecated
    @XmlTransient
    public int getColumnID() {
        return columnID;
    }

    @Deprecated
    public void setColumnID(int columnID) {
        this.columnID = columnID;
    }

    @XmlTransient
    public int getRelativeColumnID() {
        return relativeColumnID;
    }

    public void setRelativeColumnID(int relativeColumnID) {
        this.relativeColumnID = relativeColumnID;
    }

    @XmlTransient
    public int getRowID() {
        return rowID;
    }

    public void setRowID(int rowID) {
        this.rowID = rowID;
    }

    @XmlTransient
    public int getSectionID() {
        return sectionID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    @XmlTransient
    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

     @XmlTransient
    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

     @XmlTransient
    public int getColour() {
        return colour;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    @XmlTransient
    public CardWallCardCellServerState getCopyAsServerState() {
        CardWallCardCellServerState copyOfState = new CardWallCardCellServerState(sectionID, columnID, relativeColumnID, rowID, colour, title, detail, person, points, getUniqueID());
        return copyOfState;
    }

    @XmlTransient
    public CardWallCardCellClientState getCopyAsClientState() {
        CardWallCardCellClientState copyOfState = new CardWallCardCellClientState(sectionID, columnID, relativeColumnID, rowID, colour, title, detail, person, points, getUniqueID());
        return copyOfState;
    }

    @XmlTransient
    public String getUniqueID() {
        if (uniqueID == null){
            uniqueID = UUID.randomUUID().toString();
        }
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }
}
