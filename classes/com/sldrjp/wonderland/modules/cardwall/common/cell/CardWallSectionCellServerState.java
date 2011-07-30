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

import org.jdesktop.wonderland.common.cell.state.CellServerState;
import org.jdesktop.wonderland.common.cell.state.annotation.ServerState;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;


/**
 * Created by IntelliJ IDEA.
 * User: bpotter
 * Date: 3-Oct-2010
 * Time: 3:29:33 PM
 * To change this template use File | Settings | File Templates.
 */

@XmlRootElement(name = "section")
public class CardWallSectionCellServerState  implements Serializable, StateConvertable{

    @XmlElement(name = "startColumn")
    public int startColumn = -1;
    @XmlElement(name = "endColumn")
    public int endColumn = -1;
    @XmlElement(name = "sectionTitle")
    public String sectionTitle = "";

    public CardWallSectionCellServerState() {
    }

    public CardWallSectionCellServerState(int startColumn, int endColumn, String sectionTitle) {
        this.startColumn = startColumn;
        this.endColumn = endColumn;
        this.sectionTitle = sectionTitle;
    }

    @XmlTransient
    public int getStartColumn() {
        return startColumn;
    }

    public void setStartColumn(int startColumn) {
        this.startColumn = startColumn;
    }

    @XmlTransient
    public int getEndColumn() {
        return endColumn;
    }

    public void setEndColumn(int endColumn) {
        this.endColumn = endColumn;
    }

    @XmlTransient
    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    @XmlTransient
    public CardWallSectionCellServerState getCopyAsServerState() {
        CardWallSectionCellServerState copyOfState = new CardWallSectionCellServerState(startColumn, endColumn, sectionTitle);
        return copyOfState;
    }

    @XmlTransient
    public CardWallSectionCellClientState getCopyAsClientState() {
        CardWallSectionCellClientState copyOfState = new CardWallSectionCellClientState(startColumn, endColumn, sectionTitle);
        return copyOfState;
    }


}
