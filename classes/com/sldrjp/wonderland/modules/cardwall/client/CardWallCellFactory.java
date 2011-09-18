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

import com.sldrjp.wonderland.modules.cardwall.client.cell.CardWallCellImportExportHelper;
import com.sldrjp.wonderland.modules.cardwall.common.CardWallDefaultConfiguration;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellServerState;
import org.jdesktop.wonderland.client.cell.registry.annotation.CellFactory;
import org.jdesktop.wonderland.client.cell.registry.spi.CellFactorySPI;
import org.jdesktop.wonderland.common.cell.state.CellServerState;
import org.jdesktop.wonderland.modules.appbase.client.swing.SwingCellFactoryUtils;

import java.awt.*;
import java.util.Properties;
import java.util.ResourceBundle;

@CellFactory
public class CardWallCellFactory implements CellFactorySPI {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(
            "com/sldrjp/wonderland/modules/cardwall/client/resources/Bundle");

    public String[] getExtensions() {
        return new String[]{"cardwall"};
    }

    public <T extends CellServerState> T getDefaultCellServerState(Properties properties) {
        CardWallCellServerState state = new CardWallCellServerState();

        CardWallCellClientState clientState = null;
        if (properties != null) {
            String uri = properties.getProperty("content-uri");
            if (uri != null) {
                clientState = (new CardWallCellImportExportHelper()).populateStateFromURI(uri);
            }
        }
        if (clientState == null) {
            clientState = CardWallDefaultConfiguration.getDefaultState();
        }
        state.setState(clientState);

        state.setName(BUNDLE.getString("title.cardWall"));


        SwingCellFactoryUtils.skipSystemInitialPlacement(state);

        return (T) state;
    }

    public String getDisplayName() {
        return BUNDLE.getString("title.cardWall");
    }

    public Image getPreviewImage() {
        return null;
    }
}
