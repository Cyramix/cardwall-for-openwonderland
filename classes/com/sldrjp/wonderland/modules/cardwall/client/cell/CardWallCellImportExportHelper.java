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

import com.csvreader.CsvReader;
import com.sldrjp.wonderland.modules.cardwall.client.CardWallManager;
import com.sldrjp.wonderland.modules.cardwall.common.CardWallDefaultConfiguration;
import com.sldrjp.wonderland.modules.cardwall.common.CardWallException;
import com.sldrjp.wonderland.modules.cardwall.common.CardWallSyncMessage;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCardCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallCellClientState;
import com.sldrjp.wonderland.modules.cardwall.common.cell.CardWallSectionCellClientState;
import org.jdesktop.wonderland.client.cell.asset.AssetUtils;


import java.io.*;
import java.lang.annotation.Target;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Bob
 * Date: 4-Nov-2010
 * Time: 9:08:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class CardWallCellImportExportHelper {


    /**
     * The logger used by this class.
     */
    private static final Logger logger =
            Logger.getLogger(CardWallCellImportExportHelper.class.getName());
    private static final String SECTION_COL = "section";
    private static final String COLUMN_COL = "column";
    private static final String RELATIVE_COLUMN_COL = "relativeColumn";
    private static final String ROW_COL = "row";
    private static final String COLOUR_COL = "colour";
    private static final String TITLE_COL = "title";
    private static final String DETAIL_COL = "detail";
    private static final String PERSON_COL = "person";
    private static final String POINTS_COL = "points";
    private static final String RECORD_TYPE_COL = "recordType";

    private static final String[] headersV02 = {RECORD_TYPE_COL, SECTION_COL, COLUMN_COL,  RELATIVE_COLUMN_COL,ROW_COL, COLOUR_COL, TITLE_COL, DETAIL_COL, PERSON_COL, POINTS_COL};


    private static final String CARD_WALL_RECORD_TYPE = "cardWall";
    private static final String SECTION_RECORD_TYPE = "section";
    private static final String CARD_RECORD_TYPE = "card";

    private CardWallCellClientState state = null;
    private List<CardWallCardCellClientState> cards = null;

    public CardWallCellImportExportHelper(CardWallCellClientState state) {
        this.state = state;
        this.cards = state.getCards();
    }

    public CardWallCellImportExportHelper() {
    }

    private String getHeaderLine() {
        String quote = "\"";
        String comma = "";
        StringBuffer out = new StringBuffer();
        for (int i = 0; i < headersV02.length; i++) {
            out.append(comma).append(quote).append(headersV02[i]).append(quote);
            comma = ",";
        }
        out.append("\n");
        return out.toString();
    }

    String getCSVLine(CardWallCardCellClientState card) {
        String quote = "\"";
        String comma = ",";
        StringBuffer out = new StringBuffer();
        out.append(quote).append(CARD_RECORD_TYPE).append(quote);
        out.append(comma);
        out.append(quote).append(card.getSectionID()).append(quote);
        out.append(comma);
        out.append(quote).append(card.getColumnID()).append(quote);
        out.append(comma);
        out.append(quote).append(card.getRelativeColumnID()).append(quote);
        out.append(comma);
        out.append(quote).append(card.getRowID()).append(quote);
        out.append(comma);
        logger.warning("Colour to export " + card.getColour());
        out.append(quote).append(card.getColour()).append(quote);
        logger.warning(out.toString());
        out.append(comma);
        out.append(quote).append(escapeForCSV(card.getTitle())).append(quote);
        out.append(comma);
        out.append(quote).append(escapeForCSV(card.getDetail())).append(quote);
        out.append(comma);
        out.append(quote).append(escapeForCSV(card.getPerson())).append(quote);
        out.append(comma);
        out.append(quote).append(escapeForCSV(card.getPoints())).append(quote);
        out.append("\n");


        return out.toString();
    }

    protected String getCSVCardWallLine() {
        String quote = "\"";
        String comma = ",";
        StringBuffer out = new StringBuffer();
        out.append(quote).append(CARD_WALL_RECORD_TYPE).append(quote);
        out.append(comma);
        out.append(quote).append(state.getSectionStates().size()).append(quote);
        out.append(comma);
        out.append(quote).append(state.getNumberOfColumns()).append(quote);
        out.append(comma);
        out.append(quote).append(quote);
        out.append(comma);
        out.append(quote).append(state.getNumberOfRows()).append(quote);
        out.append(comma);
        out.append(quote).append(quote);
        out.append(comma);
        out.append(quote).append(escapeForCSV(state.getName())).append(quote);
        out.append(comma);
        out.append(quote).append(quote);
        out.append(comma);
        out.append(quote).append(quote);
        out.append(comma);
        out.append(quote).append(quote);
        out.append("\n");
        return out.toString();

    }
    protected String getCSVSectionLine(int sectionNumber,CardWallSectionCellClientState sectionState) {
        String quote = "\"";
        String comma = ",";
        StringBuffer out = new StringBuffer();
        out.append(quote).append(SECTION_RECORD_TYPE).append(quote);
        out.append(comma);
        out.append(quote).append(sectionNumber).append(quote);
        out.append(comma);
        out.append(quote).append(sectionState.getNumberOfColumns()).append(quote);
        out.append(comma);
        out.append(quote).append(quote);
        out.append(comma);
        out.append(quote).append(quote);
        out.append(comma);
        out.append(quote).append(quote);
        out.append(comma);
        out.append(quote).append(escapeForCSV(sectionState.getSectionTitle())).append(quote);
        out.append(comma);
        out.append(quote).append(quote);
        out.append(comma);
        out.append(quote).append(quote);
        out.append(comma);
        out.append(quote).append(quote);
        out.append("\n");
        return out.toString();
    }


    public void exportToCSV(File exportFile) throws IOException {

        FileOutputStream outStream = new FileOutputStream(exportFile);
        outStream.write(getHeaderLine().getBytes());
        // output cardwall
        outStream.write(getCSVCardWallLine().getBytes());
        int sectionNumber = 0;
        for (CardWallSectionCellClientState sectionState: state.getSectionStates()){
          outStream.write(getCSVSectionLine(sectionNumber++, sectionState).getBytes());
        }
        for (CardWallCardCellClientState card : cards) {
            outStream.write(getCSVLine(card).getBytes());
        }
        outStream.close();
    }

    String escapeForCSV(String textToEscape) {

        if (textToEscape.contains("\"")) {
            String doubleQuote = "\"\"";

            StringTokenizer st = new StringTokenizer(textToEscape, "\"");
            StringBuffer newText = new StringBuffer();
            if (textToEscape.charAt(0) == '"') {
                newText.append(doubleQuote);
            }

            while (st.hasMoreTokens()) {
                newText.append(st.nextToken());
                if (st.hasMoreTokens()) {
                    newText.append(doubleQuote);
                }
            }
            if (textToEscape.charAt(textToEscape.length() - 1) == '"') {
                newText.append(doubleQuote);
            }
            return newText.toString();
        } else {
            return textToEscape;
        }
    }

    public CardWallCellClientState populateStateFromCsvReaderV02(CsvReader reader) throws IOException {
        if (state == null) {
            state = CardWallDefaultConfiguration.getDefaultState();
        }
        while (reader.readRecord()) {
            CardWallCardCellClientState card = extractNextLineV02(reader);
            state.getCards().add(card);
        }
        return state;
    }


    private CardWallCardCellClientState extractNextLineV02(CsvReader reader) throws IOException {
        CardWallCardCellClientState card = new CardWallCardCellClientState();
        //"section", "column", "row", "title", "detail"
        extractBasicCardData(reader, card);
        return card;
    }

    private void extractBasicCardData(CsvReader reader, CardWallCardCellClientState card) throws IOException {
        card.setSectionID(Integer.parseInt(reader.get(SECTION_COL)));
        card.setColumnID(Integer.parseInt(reader.get(COLUMN_COL)));
        card.setRowID(Integer.parseInt(reader.get(ROW_COL)));
        int colour = Integer.parseInt(reader.get(COLOUR_COL));
        logger.warning("colour - " + colour );
        card.setColour(colour);
        card.setTitle(reader.get(TITLE_COL));
        card.setDetail(reader.get(DETAIL_COL));
        card.setPerson(reader.get(PERSON_COL));
        card.setPoints(reader.get(POINTS_COL));
    }


    private CardWallCardCellClientState extractNextLineV03(CsvReader reader) throws IOException {
        CardWallCardCellClientState card = new CardWallCardCellClientState();
        card.setRelativeColumnID(Integer.parseInt(reader.get(RELATIVE_COLUMN_COL)));
        extractBasicCardData(reader, card);

        return card;
    }
    private CardWallCellClientState extractHighLevelState(CsvReader reader) throws IOException {
        if (reader.readRecord()) {
            String recordType = reader.get(RECORD_TYPE_COL);
            if ((recordType != null) && recordType.equals(CARD_WALL_RECORD_TYPE)) {
                state = new CardWallCellClientState();
                int columns = Integer.parseInt(reader.get(COLUMN_COL));
                int rows = Integer.parseInt(reader.get(ROW_COL));
                int sections = Integer.parseInt(reader.get(SECTION_COL));

                state.setNumberOfColumns(columns);
                state.setNumberOfRows(rows);
                state.setSectionStates(new ArrayList<CardWallSectionCellClientState>(sections));
                return state;
            }

        }
        throw new CardWallException("Bad wall description in CardWall import file", 99);
    }


    public CardWallCellClientState populateStateFromCsvReader(CsvReader reader) throws IOException {

        boolean haveRecord;
        // first row will populate the CardWallCellClient state
        state = extractHighLevelState(reader);

        List<CardWallSectionCellClientState> sections = state.getSectionStates();
        int currentStartColumn = 0;
        int numberOfSectionsAdded = 0;
        while ((haveRecord = reader.readRecord()) && reader.get(RECORD_TYPE_COL).equals(SECTION_RECORD_TYPE)) {
            CardWallSectionCellClientState section = parseCSVForSection(reader);
            section.setColumnPositions(currentStartColumn);
            currentStartColumn += section.getNumberOfColumns();
            if (currentStartColumn > state.getNumberOfColumns()){
                throw new CardWallException("Wrong number of columns in import file", 98);
            }

            numberOfSectionsAdded++;
            sections.add(section);
        }
        if (haveRecord && reader.get(RECORD_TYPE_COL).equals(CARD_RECORD_TYPE)) {
            // read the cards
            do {
                CardWallCardCellClientState card = extractNextLineV03(reader);
                state.getCards().add(card);
            } while (reader.readRecord());

        }
        return state;
    }

    private CardWallSectionCellClientState parseCSVForSection(CsvReader reader) throws IOException {
        int numberOfColumns = Integer.parseInt(reader.get(COLUMN_COL));
        CardWallSectionCellClientState sectionState = new CardWallSectionCellClientState(numberOfColumns, reader.get(TITLE_COL));
        return sectionState;
    }

    public CardWallCellClientState populateStateFromCsvReaderDispatcher(CsvReader reader) throws IOException {

        reader.readHeaders();

        if (reader.getIndex(RECORD_TYPE_COL) == -1) {
            // original version of CSV file
            return populateStateFromCsvReaderV02(reader);
        } else {
            // use the current version
            return populateStateFromCsvReader(reader);
        }

    }


    /**
     * used for testing only
     *
     * @param fileName
     */
    protected CardWallCellClientState populateState(String fileName) {


        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fileName));
            return populateState(inputStreamReader);

        } catch (IOException e) {
            logger.severe("File not found or cannot be accessed - " + fileName);
        }
        return null;
    }


    public CardWallCellClientState populateStateFromURI(String uri) {

        try {
            URL url = AssetUtils.getAssetURL(uri);
            InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());

            return populateState(inputStreamReader);

        } catch (IOException e) {
            logger.severe("File not found or cannot be accessed - " + uri);

        }
        return null;
    }

    private CardWallCellClientState populateState(InputStreamReader inputStreamReader) throws IOException {

        CsvReader reader = new CsvReader(inputStreamReader);
        CardWallCellClientState state = populateStateFromCsvReaderDispatcher(reader);
        reader.close();
        return state;
    }

    public void importFromCSV(File selectedFile, CardWallManager cardWallManager, CardWallCell cell) {
        try {

            CsvReader reader = new CsvReader(new InputStreamReader(new FileInputStream(selectedFile)));
            List<CardWallCardCellClientState> currentCards = state.getCopyOfCards();
            for (CardWallCardCellClientState cardState : currentCards) {
                cardWallManager.deleteCard(cardState.getCardPosition());
            }
            reader.readHeaders();
            while (reader.readRecord()) {
                CardWallCardCellClientState card = extractNextLineV02(reader);
                cardWallManager.addCard(card);
                cell.sendMessage(CardWallSyncMessage.ADD_CARD, null, card);
            }
            reader.close();

        } catch (IOException
                e) {
            logger.severe("File not found or cannot be accessed - " + selectedFile.getName());

        }
    }
}
