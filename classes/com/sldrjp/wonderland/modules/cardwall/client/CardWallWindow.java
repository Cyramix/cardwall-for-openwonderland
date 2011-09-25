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

import com.jme.math.Vector2f;
import com.sldrjp.wonderland.modules.cardwall.client.cell.CardWallCell;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientState;
import org.jdesktop.wonderland.client.jme.JmeClientMain;
import org.jdesktop.wonderland.common.ExperimentalAPI;
import org.jdesktop.wonderland.modules.appbase.client.App2D;
import org.jdesktop.wonderland.modules.appbase.client.swing.WindowSwing;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.logging.Logger;

@ExperimentalAPI
public class CardWallWindow extends WindowSwing {


    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(
            "com/sldrjp/wonderland/modules/cardwall/client/resources/Bundle");
    /**
     * The logger used by this class.
     */
    private static final Logger logger =
            Logger.getLogger(CardWallWindow.class.getName());
    /**
     * The cell in which this window is displayed.
     */
    private CardWallCell cell;

    /**
     * The panel displayed within this window.
     */
    private MasterPanelImpl masterPanel;
    private CardWallManager cardWallManager;

    /**
     * Create a new instance of CardWallWindow.
     *
     * @param cell        The cell in which this window is displayed.
     * @param app         The app which owns the window.
     * @param width       The width of the window (in pixels).
     * @param height      The height of the window (in pixels).
     * @param decorated   Whether the window is decorated with a frame.
     * @param pixelScale  The size of the window pixels.
     * @param clientState
     */
    public CardWallWindow(final CardWallCell cell, App2D app, final int width,
                          final int height, boolean decorated, Vector2f pixelScale, final CardWallCellClientState clientState)
            throws InstantiationException {

        super(app, Type.PRIMARY, width, height, decorated, pixelScale);
        logger.fine("CardWallWindow - width " + width + " height " + height);
        this.cell = cell;

        setTitle(BUNDLE.getString("title.cardWall"));

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    // This must be invoked on the AWT Event Dispatch Thread
                    masterPanel = new MasterPanelImpl(cell, clientState);
                    logger.warning("masterPanel constructor completed");
                    cardWallManager = new CardWallManager(cell, clientState, masterPanel);
                    logger.warning("cardWallManager constructor completed");
                    // add details from current client state
                    masterPanel.configurePanel(clientState);
                    logger.warning("masterPanel configurePanel completed");
                    cardWallManager.populateData(clientState);
                    logger.warning("cardwallmanager populatedata completed");
                    masterPanel.setMinimumSize(new Dimension(clientState.getPreferredWidth(), clientState.getPreferredHeight()));
                    logger.fine("initial size - " + new Dimension(clientState.getPreferredWidth(), clientState.getPreferredHeight()).toString());


                }
            });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        // Parent to Wonderland main window for proper focus handling
        JmeClientMain.getFrame().getCanvas3DPanel().add(masterPanel);
        setComponent(masterPanel);
        logger.warning("completed");
//        JmeClientMain.getFrame().getCanvas3DPanel().setMinimumSize(new Dimension(clientState.getPreferredWidth(), clientState.getPreferredHeight()));

    }

    public MasterPanel getMasterPanel() {
        return masterPanel;
    }

    public CardWallManager getCardWallManager() {
        return cardWallManager;
    }
}
