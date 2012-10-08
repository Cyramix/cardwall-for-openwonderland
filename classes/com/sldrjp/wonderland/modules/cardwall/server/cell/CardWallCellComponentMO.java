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

import com.sldrjp.wonderland.modules.cardwall.common.CardWallResponseMessage;
import com.sldrjp.wonderland.modules.cardwall.common.CardWallSyncMessage;
import com.sun.sgs.app.ManagedReference;
import org.jdesktop.wonderland.common.ExperimentalAPI;
import org.jdesktop.wonderland.common.cell.messages.CellMessage;
import org.jdesktop.wonderland.common.messages.MessageID;
import org.jdesktop.wonderland.server.cell.AbstractComponentMessageReceiver;
import org.jdesktop.wonderland.server.cell.CellComponentMO;
import org.jdesktop.wonderland.server.cell.CellMO;
import org.jdesktop.wonderland.server.cell.ChannelComponentMO;
import org.jdesktop.wonderland.server.cell.annotation.UsesCellComponentMO;
import org.jdesktop.wonderland.server.comms.WonderlandClientID;
import org.jdesktop.wonderland.server.comms.WonderlandClientSender;

import java.math.BigInteger;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bpotter
 * Date: 3-Oct-2010
 * Time: 4:20:05 PM
 * To change this template use File | Settings | File Templates.
 */
@ExperimentalAPI
public class CardWallCellComponentMO extends CellComponentMO {

    private static Logger logger = Logger.getLogger(CardWallCellComponentMO.class.getName());

    @UsesCellComponentMO(ChannelComponentMO.class)
    private ManagedReference<ChannelComponentMO> channelComponentRef = null;

    public CardWallCellComponentMO(CellMO cell) {
        super(cell);
    }

    @Override
    protected void setLive(boolean live) {
        super.setLive(live);
        if (live) {
            channelComponentRef.getForUpdate().addMessageReceiver(CardWallSyncMessage.class, new CardWallCellComponentMOMessageReceiver(cellRef.get()));

        } else {
            channelComponentRef.getForUpdate().removeMessageReceiver(CardWallSyncMessage.class);
        }
    }

    @Override
    protected String getClientClass() {
        return "com.sldrjp.wonderland.modules.cardwall.client.CardWallComponent";
    }

    public void sendAllClients(WonderlandClientID clientID, CardWallSyncMessage message) {
       logger.fine("sending " + message.getMessageType());
       ChannelComponentMO channelComponent = channelComponentRef.getForUpdate();
        channelComponent.sendAll(clientID, message);
    }

    private static class CardWallCellComponentMOMessageReceiver extends AbstractComponentMessageReceiver{
        public CardWallCellComponentMOMessageReceiver(CellMO cellMO) {
            super(cellMO);
        }

        @Override
        public void messageReceived(WonderlandClientSender sender, WonderlandClientID clientID, CellMessage cellMessage) {
            CardWallSyncMessage cardWallMessage = (CardWallSyncMessage) cellMessage;
            ((CardWallCellMO) getCell()).receivedMessage(sender,clientID,cardWallMessage);


        }
    }
}
