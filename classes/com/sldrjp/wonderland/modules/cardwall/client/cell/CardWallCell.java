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

import com.sldrjp.wonderland.modules.cardwall.client.*;
import com.sldrjp.wonderland.modules.cardwall.common.CardPosition;
import com.sldrjp.wonderland.modules.cardwall.common.CardWallSyncMessage;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCardCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientState;
import org.jdesktop.wonderland.client.cell.Cell;
import org.jdesktop.wonderland.client.cell.CellCache;
import org.jdesktop.wonderland.client.cell.CellCacheBasicImpl;
import org.jdesktop.wonderland.client.cell.annotation.UsesCellComponent;
import org.jdesktop.wonderland.client.comms.WonderlandSession;
import org.jdesktop.wonderland.client.contextmenu.ContextMenuActionListener;
import org.jdesktop.wonderland.client.contextmenu.ContextMenuItem;
import org.jdesktop.wonderland.client.contextmenu.ContextMenuItemEvent;
import org.jdesktop.wonderland.client.contextmenu.SimpleContextMenuItem;
import org.jdesktop.wonderland.client.contextmenu.cell.ContextMenuComponent;
import org.jdesktop.wonderland.client.contextmenu.spi.ContextMenuFactorySPI;
import org.jdesktop.wonderland.client.jme.JmeClientMain;
import org.jdesktop.wonderland.client.scenemanager.event.ContextEvent;
import org.jdesktop.wonderland.common.ExperimentalAPI;
import org.jdesktop.wonderland.common.cell.CellID;
import org.jdesktop.wonderland.common.cell.CellStatus;
import org.jdesktop.wonderland.common.cell.state.CellClientState;
import org.jdesktop.wonderland.common.messages.ResponseMessage;
import org.jdesktop.wonderland.modules.appbase.client.cell.App2DCell;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

@ExperimentalAPI
public class CardWallCell extends App2DCell {

    /**
     * The (singleton) window created by the Swing example app
     */
    private CardWallWindow window;
    private CardWallManager cardWallManager;
    private CardWallMessageHandler cardWallMessageHandler = null;
    private CardWallCellClientState clientState;
    private CardWallComponent commComponent;
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(
            "com/sldrjp/wonderland/modules/cardwall/client/resources/Bundle");

    private static final Logger logger = Logger.getLogger(CardWallCell.class.getName());

    @UsesCellComponent
    ContextMenuComponent menuComponent;

    public CardWallCell(CellID cellID, CellCache cellCache) {
        super(cellID, cellCache);
    }

    @Override
    public void setClientState(CellClientState clientState) {
        super.setClientState(clientState);
        this.clientState = (CardWallCellClientState) clientState;

    }

    /**
     * This is called when the status of the cell changes.
     */
    @Override
    protected void setStatus(CellStatus status, boolean increasing) {
        super.setStatus(status, increasing);

        switch (status) {

            // The cell is now visible
            case ACTIVE:
                if (increasing) {
                    commComponent = getComponent(CardWallComponent.class);
                    if (getName() == null) {
                        setName(BUNDLE.getString("title.cardWall"));
                    }
                    logger.severe("card wall name " + getName());
                    CardWallApp app = new CardWallApp(getName(), clientState.getPixelScale());
                    logger.fine("pixel scale " + clientState.getPixelScale());
                    setApp(app);

                    // Tell the app to be displayed in this cell.
                    app.addDisplayer(this);

                    // This app has only one window, so it is always top-level
                    try {
                        logger.fine("CardWallCell width " + clientState.getPreferredWidth() + ", numberOfColumns = " + clientState.getNumberOfColumns());
                        window = new CardWallWindow(this, app, clientState.getPreferredWidth(),
                                clientState.getPreferredHeight(), true, pixelScale, clientState);
                        cardWallManager = window.getCardWallManager();
                        cardWallMessageHandler = new CardWallMessageHandler(cardWallManager);

                    } catch (InstantiationException ex) {
                        throw new RuntimeException(ex);
                    }

                    menuComponent.addContextMenuFactory(
                            new CardWallContextMenuFactory());

                    // Both the app and the user want this window to be visible
//                    window.setSize(CardWallDefaultConfiguration.preferredWidth, CardWallDefaultConfiguration.preferredHeight);
                    final CardWallCell cell = this;
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            window.setVisibleApp(true);
                            window.setVisibleUser(cell, true);
                        }
                    });

                }
                break;

            // The cell is no longer visible
            case DISK:
                if (!increasing) {
                    window.setVisibleApp(false);
                    window = null;
                    removeComponent(CardWallComponent.class);
                    commComponent = null;
                }
                break;
            case INACTIVE:
                break;
            case RENDERING:
                break;
            case VISIBLE:
                break;
        }
    }

    private CardWallCell getCell() {
        return this;
    }

    public void processMessage(final CardWallSyncMessage cardWallMessage) {
        logger.fine("Receiving message - " + cardWallMessage.getMessageType());
        WonderlandSession session = getCellCache().getSession();
        if ((cardWallMessage.getMessageType() == CardWallSyncMessage.ADD_CARD) ||
                (cardWallMessage.getMessageType() == CardWallSyncMessage.MOVE_CARD) ||
                (cardWallMessage.getMessageType() == CardWallSyncMessage.RESTORE_CARD)) {
            cardWallMessageHandler.handleMessage(cardWallMessage);
        } else {
            if (!cardWallMessage.getSenderID().equals(session.getID())) {
                cardWallMessageHandler.handleMessage(cardWallMessage);

            }
        }
    }

    /**
     * Requests a list of the current CardWalls in the system
     *
     * @return List of CardWalls cells (except this CardWall)
     */
    public List<CardWallCell> getCardWalls() {
        CellCache cache = this.getCellCache();
        List<CardWallCell> cardWallCells = new ArrayList<CardWallCell>();
        if (cache instanceof CellCacheBasicImpl) {
            CellCacheBasicImpl basicCache = (CellCacheBasicImpl) cache;
            Cell[] cells = basicCache.getCells();
            for (Cell cell : cells) {
                if (cell instanceof CardWallCell) {
                    if (!cell.equals(this)) {
                        cardWallCells.add((CardWallCell) cell);
                        logger.warning(cell.getName() + " " + cell.getClass());
                    }
                }
            }
        }
        return cardWallCells;
    }

    public List<Section> getSections() {
        return cardWallManager.getSections();

    }

    /**
     * Inserts an existing card typically from another card wall in the section
     *
     * @param sectionNumber
     * @param cardState
     */
    public void insertCard(int sectionNumber, CardWallCardCellClientState cardState) {


        CardWallCardCellClientState workingState = new CardWallCardCellClientState(
                sectionNumber, -1, -1, -1, cardState.getColour(), cardState.getTitle(), cardState.getDetail(),
                cardState.getPerson(), cardState.getPoints(), null);
        sendMessage(CardWallSyncMessage.INSERT_CARD, null, workingState);
    }


    public void sendMessage(int messageType, CardPosition originalPosition, CardWallCardCellClientState card) {
        logger.fine("Sending message - " + messageType + " " + card);
        CardWallSyncMessage message = prepareMessage(messageType, originalPosition, card);
        commComponent.sendMessage(message);
    }

    protected CardWallSyncMessage prepareMessage(int messageType, CardPosition originalPosition, CardWallCardCellClientState card) {
        CardWallSyncMessage message = new CardWallSyncMessage();
        message.setMessageType(messageType);
        message.setCardClientState(card.getCopyAsClientState());
        message.setOriginalCardPosition(originalPosition);
        return message;
    }

    public void sendMessage(int messageType, CardWallCellClientState state) {
        logger.fine("Sending message - " + messageType + " " + state.toString());
        CardWallSyncMessage message = prepareMessage(messageType, state);
        commComponent.sendMessage(message);
    }

    protected CardWallSyncMessage prepareMessage(int messageType, CardWallCellClientState state) {
        CardWallSyncMessage message = new CardWallSyncMessage();
        message.setMessageType(messageType);
        message.setClientState(state);
        return message;
    }

    public void sendMessage(int messageType, int i, String title) {
        logger.fine("Sending message - " + messageType + " " + i + ":" + title);
        CardWallSyncMessage message = prepareMessage(messageType, i, title);
        commComponent.sendMessage(message);
    }

    protected CardWallSyncMessage prepareMessage(int messageType, int position, String text) {
        CardWallSyncMessage message = new CardWallSyncMessage();
        message.setMessageType(messageType);
        message.setSection(position);
        message.setText(text);
        return message;
    }

    /**
     * Context menu factory
     */

    class CardWallContextMenuFactory implements ContextMenuFactorySPI {

        public ContextMenuItem[] getContextMenuItems(ContextEvent event) {
            return new ContextMenuItem[]{new SimpleContextMenuItem(
                    BUNDLE.getString("menu.exportData"), null,
                    new CardWallContextExportMenuListener()),

//                    new SimpleContextMenuItem(
//                            BUNDLE.getString("Import_data"), null,
//                            new CardWallContextImportMenuListener()),
                    new SimpleContextMenuItem(
                            BUNDLE.getString("menu.configure"), null,
                            new CardWallContextConfigureMenuListener())
            };
        }
    }

    /**
     * Listener for event when the Tools option selected
     */
    class CardWallContextExportMenuListener implements ContextMenuActionListener {

        public void actionPerformed(ContextMenuItemEvent event) {
            JFileChooser jChooser = new JFileChooser();
//            jChooser.setFileFilter(new FileNameExtensionFilter(BUNDLE.getString("Csv_file"), "cardwall.csv"));
            jChooser.addChoosableFileFilter(new FileNameExtensionFilter(BUNDLE.getString("file.type.cardwall"), "cardwall"));

            int response = jChooser.showSaveDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {

                CardWallCellImportExportHelper helper = new CardWallCellImportExportHelper(clientState);
                try {
                    helper.exportToCSV(jChooser.getSelectedFile());
                } catch (IOException e) {
                    logger.severe("IO error during export to csv \n" + e.getStackTrace());
                }
            }

        }
    }

    class CardWallContextImportMenuListener implements ContextMenuActionListener {

        public void actionPerformed(ContextMenuItemEvent event) {
            JFileChooser jChooser = new JFileChooser();
//            jChooser.setFileFilter(new FileNameExtensionFilter(BUNDLE.getString("Csv_file"), "cardwall.csv"));
            jChooser.addChoosableFileFilter(new FileNameExtensionFilter(BUNDLE.getString("file.type.cardwall"), "cardwall"));

            int response = jChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {

                CardWallCellImportExportHelper helper = new CardWallCellImportExportHelper(clientState);

                helper.importFromCSV(jChooser.getSelectedFile(), cardWallManager, getCell());

            }

        }
    }

    class CardWallContextConfigureMenuListener implements ContextMenuActionListener {

        public void actionPerformed(ContextMenuItemEvent event) {

            cardWallManager.configureCardWall(JmeClientMain.getFrame().getFrame());


        }


    }
}
