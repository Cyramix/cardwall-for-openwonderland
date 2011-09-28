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

import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCardCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallSectionCellClientState;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bob
 * Date: 26/01/11
 * Time: 9:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MasterPanel {
    void showCard(Card card, CardWallManager cardWallManager);

    void removeCard(Card card);


    void updateCard(CardWallCardCellClientState state, Card card);



    void addSection(Section section);

    void moveCard(Card card);
    CardWallManager getCardWallManager();

    void setCardWallManager(CardWallManager cardWallManager);

    Component add(Component component);

    SectionHeader getSectionHeader();

    void repaint();

    void setMinimumSize(Dimension dimension);

    void queueRemovePanel(Component component);

    void removeAndRepaint();

    void reconfigurePanel(CardWallCellClientState clientState);

    Container getParent();

}
