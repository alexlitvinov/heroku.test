package com.hyurumi.fb_bot_boilerplate.models.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hyurumi.fb_bot_boilerplate.models.common.Recipient;

/**
 * Created by genki.furumi on 4/14/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Messaging {
    public Sender sender;
    public Recipient recipient;
    public String timeStamp;
    public Message message;
    public Postback postback;
    public Delivery delivery;
}
