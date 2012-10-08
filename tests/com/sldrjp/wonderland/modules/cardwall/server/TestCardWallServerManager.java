package com.sldrjp.wonderland.modules.cardwall.server;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 19/09/12
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */


import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientStateForTest;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellServerState;
import com.sldrjp.wonderland.modules.cardwall.server.cell.CardWallServerStateForTests;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class TestCardWallServerManager {

    CardWallCellClientState serverState;
    CardWallServerManager manager;

    @Before
    public void setup() throws Exception {
        serverState = new CardWallCellClientStateForTest();
        manager = new CardWallServerManager(serverState);
    }

    @Test
    public void testSetup() throws Exception {

        assertEquals("wrong number of sections", manager.getNumberOfSections(), serverState.getSectionStates().size());

    }

}
