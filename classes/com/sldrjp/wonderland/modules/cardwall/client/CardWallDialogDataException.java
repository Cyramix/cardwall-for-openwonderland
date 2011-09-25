package com.sldrjp.wonderland.modules.cardwall.client;

import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: Bob
 * Date: 18/09/11
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
class CardWallDialogDataException extends Throwable {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(
            "com/sldrjp/wonderland/modules/cardwall/client/resources/Bundle");


    public static final int CARDWALL_CONFIGURATION_LOST_INTEGRITY = -1;
    private static final String CARDWALL_CONFIGURATION_LOST_INTEGRITY_KEY = "errorMessage.lostOfIntegrity";
    public static final int CARDWALL_CONFIGURATION_NUMBER_COLUMNS_ERROR = 1;
    private static final String CARDWALL_CONFIGURATION_NUMBER_COLUMNS_ERROR_KEY = "errorMessage.numberOfColumns";
    public static final int CARDWALL_CONFIGURATION_NON_NUMERIC = 2;
    private static final String CARDWALL_CONFIGURATION_NON_NUMERIC_KEY = "errorMessage.nonNumeric";
    public static final int CARDWALL_CONFIGURATION_MISSING_SECTION_ERROR = 3;
    private static final String CARDWALL_CONFIGURATION_MISSING_SECTION_ERROR_KEY = "errorMessage.missingSection";
    public static final int CARDWALL_CONFIGURATION_SECTION_OUT_OF_RANGE_ERROR = 4;
    private static final String CARDWALL_CONFIGURATION_SECTION_OUT_OF_RANGE_ERROR_KEY = "errorMessage.sectionOutOfRange";
    public static final int CARDWALL_CONFIGURATION_CANNOT_DELETE_SECTION = 5;
    private static final String CARDWALL_CONFIGURATION_CANNOT_DELETE_SECTION_KEY = "errorMessage.cannotDeleteSection";


    private int errorType = 0;


    public CardWallDialogDataException(Exception e) {
        super(e);
    }

    public CardWallDialogDataException(int errorType) {
        super("Message not provided");
        this.errorType = errorType;
    }

    public int getErrorType() {
        return errorType;
    }

    public String getErrorMessage() {
        String key = "errorMessage.unknown";
        switch (errorType) {
            case CARDWALL_CONFIGURATION_LOST_INTEGRITY:
                key = CARDWALL_CONFIGURATION_LOST_INTEGRITY_KEY;
                break;
            case CARDWALL_CONFIGURATION_NUMBER_COLUMNS_ERROR:
                key = CARDWALL_CONFIGURATION_NUMBER_COLUMNS_ERROR_KEY;
                break;
            case CARDWALL_CONFIGURATION_NON_NUMERIC:
                key = CARDWALL_CONFIGURATION_NON_NUMERIC_KEY;
                break;
            case CARDWALL_CONFIGURATION_MISSING_SECTION_ERROR:
                key = CARDWALL_CONFIGURATION_MISSING_SECTION_ERROR_KEY;
                break;

            case CARDWALL_CONFIGURATION_SECTION_OUT_OF_RANGE_ERROR:
                key = CARDWALL_CONFIGURATION_SECTION_OUT_OF_RANGE_ERROR_KEY;
                break;
            case CARDWALL_CONFIGURATION_CANNOT_DELETE_SECTION:
                key = CARDWALL_CONFIGURATION_CANNOT_DELETE_SECTION_KEY;
                break;


        }

        return BUNDLE.getString(key);
    }
}
