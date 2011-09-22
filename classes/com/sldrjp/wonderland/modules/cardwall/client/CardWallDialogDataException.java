package com.sldrjp.wonderland.modules.cardwall.client;

/**
* Created by IntelliJ IDEA.
* User: Bob
* Date: 18/09/11
* Time: 1:13 PM
* To change this template use File | Settings | File Templates.
*/
class CardWallDialogDataException extends Throwable {

    public static final int CARDWALL_CONFIGURATION_LOST_INTEGRITY = -1;
    public static final int CARDWALL_CONFIGURATION_NUMBER_COLUMNS_ERROR = 1;

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
}
