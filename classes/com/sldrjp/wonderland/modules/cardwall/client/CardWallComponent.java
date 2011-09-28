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

package com.sldrjp.wonderland.modules.cardwall.client;

import com.sldrjp.wonderland.modules.cardwall.client.cell.CardWallCell;
import com.sldrjp.wonderland.modules.cardwall.common.CardWallSyncMessage;
import org.jdesktop.wonderland.client.cell.Cell;
import org.jdesktop.wonderland.client.cell.CellComponent;
import org.jdesktop.wonderland.client.cell.ChannelComponent;
import org.jdesktop.wonderland.common.ExperimentalAPI;
import org.jdesktop.wonderland.common.cell.CellStatus;
import org.jdesktop.wonderland.common.cell.messages.CellMessage;

/**
 * Created by IntelliJ IDEA.
 * User: bpotter
 * Date: 3-Oct-2010
 * Time: 4:26:36 PM
 * To change this template use File | Settings | File Templates.
 */
@ExperimentalAPI
public class CardWallComponent extends CellComponent{


    private ChannelComponent channelComp;
    private ChannelComponent.ComponentMessageReceiver messageReceiver;
    private CardWallCell cell = null;


    public CardWallComponent(Cell cell) {
        super(cell);
        this.cell = (CardWallCell) cell;
    }

    @Override
    protected void setStatus(CellStatus status, boolean increasing) {
        switch (status) {
            case ACTIVE:
                if (increasing){
                    channelComp = cell.getComponent(ChannelComponent.class);
                    if (channelComp == null) {
                        throw new IllegalStateException("Cell does not have a Chanel Component");
                    }
                    if (messageReceiver == null) {
                        messageReceiver = new ChannelComponent.ComponentMessageReceiver() {
                            public void messageReceived(CellMessage message) {
                                CardWallSyncMessage cardWallMessage = (CardWallSyncMessage) message;
                                cell.processMessage(cardWallMessage);
                            }
                        };
                        channelComp.addMessageReceiver(CardWallSyncMessage.class,  messageReceiver);
                    }
                }
                break;
            case DISK:
                if (messageReceiver != null) {
                    channelComp.removeMessageReceiver(CardWallSyncMessage.class);
                    messageReceiver = null;
                }
                channelComp = null;
                break;


        }
    }

    public void sendMessage (CardWallSyncMessage message) {
        channelComp.send(message);

    }

}
