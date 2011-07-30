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

import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCardCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientState;
import org.jdesktop.wonderland.common.cell.messages.CellMessage;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: bpotter
 * Date: 3-Oct-2010
 * Time: 4:49:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class CardWallSyncMessage extends CellMessage implements Serializable {

    private int messageType = 0;
    private String cardKey = null;
    private String text = null;
    private int rows;
    private int columns;
    private int section;
    private CardPosition originalCardPosition;
    private CardWallCellClientState clientState = null;
    private CardWallCardCellClientState cardClientState = null;
    public static final int CREATE_DEFAULT = 0;
    public static final int COMPLETE_STATE = 1;
    public static final int MOVE_CARD = 2;
    public static final int CHANGE_TEXT = 3;
    public static final int CHANGE_CARD_TITLE = 4;
    public static final int ADD_CARD = 5;
    public static final int DELETE_CARD = 6;
    public static final int UPDATE_SERVER_CARD_STATE_ONLY = -7;

    public CardWallSyncMessage() {
        this.messageType = CREATE_DEFAULT;
    }

    public CardWallSyncMessage(int messageType) {
        this.messageType = messageType;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getCardKey() {
        return cardKey;
    }

    public void setCardKey(String cardKey) {
        this.cardKey = cardKey;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public CardPosition getOriginalCardPosition() {
//        if ((originalCardPosition == null) && (cardClientState != null)) {
//            originalCardPosition = cardClientState.getCardPosition();
//        }
        return originalCardPosition;
    }

    public void setOriginalCardPosition(CardPosition originalCardPosition) {

        this.originalCardPosition = originalCardPosition;
    }

    public CardWallCellClientState getClientState() {
        return clientState;
    }

    public void setClientState(CardWallCellClientState clientState) {
        this.clientState = clientState;
    }

    public CardWallCardCellClientState getCardClientState() {
        return cardClientState;
    }

    public void setCardClientState(CardWallCardCellClientState cardClientState) {
        this.cardClientState = cardClientState;
    }

    @Override
    public String toString() {
        return  messageType + " " + cardClientState.toString();
    }
}
