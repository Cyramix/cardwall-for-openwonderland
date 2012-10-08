package com.sldrjp.wonderland.modules.cardwall.common;

import org.jdesktop.wonderland.common.messages.MessageID;
import org.jdesktop.wonderland.common.messages.ResponseMessage;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 17/09/12
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class CardWallResponseMessage extends ResponseMessage
{
    private boolean failure = false;
    public CardWallResponseMessage(MessageID messageID) {
        super(messageID);
    }

    public boolean isFailure() {
        return failure;
    }

    public void setFailure(boolean failure) {
        this.failure = failure;
    }
}
