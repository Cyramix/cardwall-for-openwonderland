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

package com.sldrjp.wonderland.modules.cardwall.common;

/**
 * Created by IntelliJ IDEA.
 * User: Bob
 * Date: 28/01/11
 * Time: 9:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class CardWallException extends RuntimeException{
    private int errorCode = 0;
    private String additionalDetails = null;

    public static int SECTION_OUT_OF_RANGE = 1;
    public static String SECTION_OUT_OF_RANGE_MSG = "Section out of range";

    public static int CARD_AT_LOCATION = 2;
    public static String CARD_AT_LOCATION_MSG = "A card already exists at this location";

    public static int SAME_CARD_AT_LOCATION = 3;
    public static String SAME_CARD_AT_LOCATION_MSG = "A card with the same values already exists at this location";

    public static int CANNOT_MOVE = 4;
    public static String CANNOT_MOVE_MSG = "Cannot move card to this position, a card already exists at the selected location";

    public CardWallException(String message, int errorCode, String additionalDetails) {
        super(message);
        this.errorCode = errorCode;
        this.additionalDetails = additionalDetails;
    }

    public CardWallException(String message, Throwable cause, int errorCode, String additionalDetails) {
        super(message, cause);
        this.errorCode = errorCode;
        this.additionalDetails = additionalDetails;
    }

    public CardWallException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CardWallException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }
}

