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

package com.sldrjp.wonderland.modules.cardwall.client.cell;

import com.sldrjp.wonderland.modules.cardwall.common.CardPosition;
import com.sldrjp.wonderland.modules.cardwall.common.CardWallSyncMessage;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCardCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientState;
import org.jdesktop.wonderland.common.cell.CellStatus;
import org.jdesktop.wonderland.common.cell.state.CellClientState;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bpotter
 * Date: 16-Oct-2010
 * Time: 2:20:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class CardWallCellForTests extends CardWallCell {

    public CardWallCellForTests() {
        super(null, null);
    }

    List<CardWallSyncMessage> messages = new ArrayList<CardWallSyncMessage>();

    @Override
    public void setClientState(CellClientState clientState) {
        //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void setStatus(CellStatus status, boolean increasing) {
        //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void processMessage(CardWallSyncMessage cardWallMessage) {
        //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void sendMessage(int messageType, CardPosition position, CardWallCardCellClientState card) {
        messages.add(prepareMessage(messageType, position, card));
    }

    @Override
    public void sendMessage(int messageType, int i, String title) {
        messages.add(prepareMessage(messageType, i, title));
    }


    @Override
    public void sendMessage(int messageType, CardWallCellClientState state) {


        messages.add(prepareMessage(messageType, state));
    }

    public List<CardWallSyncMessage> getMessages() {
        return messages;
    }

    public void clearMessages() {
        messages.clear();
    }
}
