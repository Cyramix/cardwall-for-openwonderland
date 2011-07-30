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

import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCardCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientStateForTest;
import org.junit.Test;

import java.io.*;
import java.net.MalformedURLException;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: Bob
 * Date: 4-Nov-2010
 * Time: 9:13:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestCardWallCellImportExportHelper {

    private String headerForCSV = "\"recordType\",\"section\",\"column\",\"relativeColumn\",\"row\",\"colour\",\"title\",\"detail\",\"person\",\"points\"\n";
    private String cardwallRecord = "\"cardWall\",\"4\",\"7\",\"\",\"4\",\"\",\"test for cardwall\",\"\",\"\",\"\"";
    private String firstSectionRecord = "\"section\",\"0\",\"2\",\"\",\"\",\"\",\"section 0\",\"\",\"\",\"\"";
    private String firstCard = "\"card\",\"0\",\"0\",\"0\",\"1\",\"99\",\"Title\",\"Body Text\",\"Person Text\",\"Points Text\"\n";
    private String secondCard = "\"card\",\"0\",\"0\",\"0\",\"2\",\"999\",\"Title\",\"Body \"\"Text\"\"\",\"Person Text\",\"Points Text\"\n";


    @Test
    public void test_escapeForCSV() {
        CardWallCellClientState state = new CardWallCellClientStateForTest();
        CardWallCellImportExportHelper helper = new CardWallCellImportExportHelper(state);

        assertEquals("Not escaped for CSV", "test \"\"in quotes\"\"", helper.escapeForCSV("test \"in quotes\""));
        assertEquals("Not escaped for CSV", "\"\"test\"\" in quotes", helper.escapeForCSV("\"test\" in quotes"));
    }

    @Test
    public void test_getCSVLine() {
        CardWallCellClientState state = new CardWallCellClientStateForTest();
        CardWallCellImportExportHelper helper = new CardWallCellImportExportHelper(state);

        CardWallCardCellClientState card = state.getCopyOfCards().get(0);
        assertEquals("CSV line does not match", firstCard, helper.getCSVLine(card));

        card = state.getCopyOfCards().get(1);
        assertEquals("CSV line with embedded quotes does not match", secondCard, helper.getCSVLine(card));
    }


    @Test
    public void test_exportToCSV() throws IOException {
        File tmpOutputFile = File.createTempFile("testToCSV", ".csv");
        CardWallCellClientState state = new CardWallCellClientStateForTest();
        CardWallCellImportExportHelper helper = new CardWallCellImportExportHelper(state);
        helper.exportToCSV(tmpOutputFile);


        BufferedReader inputStream = new BufferedReader(new FileReader(tmpOutputFile));
        assertEquals("CSV line  header", headerForCSV, inputStream.readLine() + "\n");
        // first record should be cardwall
        assertEquals("CSV cardwall line in error", cardwallRecord, inputStream.readLine());
        // next four are sections
         assertEquals("CSV section one error",firstSectionRecord ,inputStream.readLine());
        inputStream.readLine();
        inputStream.readLine();
        inputStream.readLine();

        // next two are cards
        assertEquals("CSV line does not match", firstCard, inputStream.readLine() + "\n");
        assertEquals("CSV line with embedded quotes does not match", secondCard, inputStream.readLine() + "\n");

    }


    @Test
    public void test_populateStateFromCSVV02() throws MalformedURLException {
        CardWallCellImportExportHelper helper = new CardWallCellImportExportHelper();
   //     URL fullFileName = TestCardWallCellImportExportHelper.class.getResource("test.cardwall");
        CardWallCellClientState state = helper.populateState("c:\\work\\wonderland\\trunk\\wonderland-modules\\unstable\\cardwall\\src\\tests\\com\\sldrjp\\wonderland\\modules\\cardwall\\client\\cell\\test02.cardwall");

        assertEquals("wrong number of cards", 2, state.getCards().size());
        CardWallCardCellClientState card = state.getCards().get(0);
        assertEquals("first card section incorrect", -1, card.getSectionID());
        assertEquals("first card column incorrect", 0, card.getColumnID());
        assertEquals("first card row incorrect", 1, card.getRowID());
        assertEquals("first card colour incorrect", 99, card.getColour());
        assertEquals("first card title incorrect", "test title", card.getTitle());
        assertEquals("first card detail incorrect", "test detail", card.getDetail());
        assertEquals("first card person incorrect", "person text", card.getPerson());
        assertEquals("first card points incorrect", "points text", card.getPoints());
        card = state.getCards().get(1);
        assertEquals("second card section incorrect", -1, card.getSectionID());
        assertEquals("second card column incorrect", 1, card.getColumnID());
        assertEquals("second card row incorrect", 2, card.getRowID());
        assertEquals("second card colour incorrect", 999, card.getColour());
        assertEquals("second card title incorrect", "test \"title\"", card.getTitle());
        assertEquals("second card detail incorrect", "test\r\nwith new line", card.getDetail());
        assertEquals("second card person incorrect", "person text", card.getPerson());
        assertEquals("second card points incorrect", "points text", card.getPoints());
        
    }

    @Test
    public void test_populateStateFromCSVV03() throws MalformedURLException {
        CardWallCellImportExportHelper helper = new CardWallCellImportExportHelper();
   //     URL fullFileName = TestCardWallCellImportExportHelper.class.getResource("test.cardwall");
        CardWallCellClientState state = helper.populateState("c:\\work\\wonderland\\trunk\\wonderland-modules\\unstable\\cardwall\\src\\tests\\com\\sldrjp\\wonderland\\modules\\cardwall\\client\\cell\\testV03.csv");

        assertEquals("wrong number of sections", 4, state.getSectionStates().size());

        assertEquals("wrong number of cards", 3, state.getCards().size());
        CardWallCardCellClientState card = state.getCards().get(0);
        assertEquals("first card section incorrect", 0, card.getSectionID());
        assertEquals("first card column incorrect", 0, card.getColumnID());
        assertEquals("first card relative column incorrect", 0, card.getRelativeColumnID());
        assertEquals("first card row incorrect", 1, card.getRowID());
        assertEquals("first card colour incorrect", 99, card.getColour());
        assertEquals("first card title incorrect", "test title", card.getTitle());
        assertEquals("first card detail incorrect", "test detail", card.getDetail());
        assertEquals("first card person incorrect", "person text", card.getPerson());
        assertEquals("first card points incorrect", "points text", card.getPoints());
        card = state.getCards().get(1);
        assertEquals("second card section incorrect", 0, card.getSectionID());
        assertEquals("second card column incorrect", 1, card.getColumnID());
        assertEquals("second card relative column incorrect", 1, card.getRelativeColumnID());
        assertEquals("second card row incorrect", 2, card.getRowID());
        assertEquals("second card colour incorrect", 999, card.getColour());
        assertEquals("second card title incorrect", "test \"title\"", card.getTitle());
//        assertEquals("second card detail incorrect", "test\r\nwith new line", card.getDetail());
        assertEquals("second card person incorrect", "person text", card.getPerson());
        assertEquals("second card points incorrect", "points text", card.getPoints());
        card = state.getCards().get(2);
        assertEquals("third card section incorrect", 1, card.getSectionID());
        assertEquals("third card column incorrect", 2, card.getColumnID());
        assertEquals("third card relative column incorrect", 0, card.getRelativeColumnID());
        assertEquals("third card row incorrect", 1, card.getRowID());
        assertEquals("third card colour incorrect", 9999, card.getColour());
        assertEquals("third card title incorrect", "extra card", card.getTitle());
        assertEquals("third card detail incorrect", "test detail", card.getDetail());
        assertEquals("third card person incorrect", "person text", card.getPerson());
        assertEquals("third card points incorrect", "points text", card.getPoints());
    }
}

