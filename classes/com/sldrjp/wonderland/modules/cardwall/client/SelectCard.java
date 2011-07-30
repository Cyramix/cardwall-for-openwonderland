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

import java.awt.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bob
 * Date: 06/03/11
 * Time: 8:26 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SelectCard {

    void setSectionNumber(int i);

    void setVisible(boolean b);


    void newCard(CardWallCardCellClientState state);

    void removeFromSelectable(CardWallCardCellClientState state);

    Component getAsComponent();
}
