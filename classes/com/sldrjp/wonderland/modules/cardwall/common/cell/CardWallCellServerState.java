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

import com.sldrjp.wonderland.modules.cardwall.common.CardWallDefaultConfiguration;
import org.jdesktop.wonderland.common.cell.state.annotation.ServerState;
import org.jdesktop.wonderland.modules.appbase.common.cell.App2DCellServerState;

import javax.xml.bind.annotation.*;
import java.util.*;

@XmlRootElement(name = "cardwall-cell")
@ServerState
public class CardWallCellServerState extends App2DCellServerState {

    @XmlElement
    public int numberOfColumns;

    @XmlElement
    public int numberOfRows;


    @XmlElementWrapper(name="sections")
    @XmlElement(name="section")
    public ArrayList<CardWallSectionCellServerState> sections = new ArrayList<CardWallSectionCellServerState>();

    @XmlElementWrapper(name="cards")
    @XmlElement(name="card")
    public List<CardWallCardCellServerState> cards = new ArrayList <CardWallCardCellServerState>();

    @Override
    public String getServerClassName() {
        return "com.sldrjp.wonderland.modules.cardwall.server.cell.CardWallCellMO";
    }

     @XmlTransient
    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }
    @XmlTransient
    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public void setState(CardWallCellClientState clientState) {

        numberOfColumns = clientState.getNumberOfColumns();
        numberOfRows = clientState.getNumberOfRows();
        cards = (ArrayList) clientState.getCopyOfCardsAsServerState();
        sections = (ArrayList) clientState.getCopyOfSectionsAsServerState();


    }

    @XmlTransient
    public List<CardWallCardCellServerState> getCopyOfCards() {
        if (cards == null) {
            return null;
        }

        return ListConverter.copyAsServerState(CardWallCardCellServerState.class, cards );

    }

    @Override
    public String toString() {
        return super.toString() + " [CardWallCellServerState]: " +
                "pixelScaleX=" + pixelScaleX + "," +
                "pixelScaleY=" + pixelScaleY;
    }

    @XmlTransient
    public List <CardWallCardCellClientState> getCopyOfCardsAsClientState() {
        if (cards == null) {
            return null;
        }
        return ListConverter.copyAsClientState(CardWallCardCellClientState.class, cards);

    }

    @XmlTransient
    public List<CardWallSectionCellClientState> getCopyOfSectionsAsClientState() {
        if (sections == null) {
            return null;
        }
        return ListConverter.copyAsClientState(CardWallSectionCellClientState.class, sections);
    }

    public void setSections(List<CardWallSectionCellServerState> sections) {
        this.sections = (ArrayList) sections;
    }

    public void setCards(List <CardWallCardCellServerState> cards) {
        this.cards = (List) cards;
    }
    @XmlTransient
    public List<CardWallSectionCellServerState> getSections() {
        return sections;
    }
}
