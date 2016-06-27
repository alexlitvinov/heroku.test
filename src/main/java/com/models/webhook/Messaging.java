package com.models.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.models.common.Recipient;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Messaging {
    public Sender sender;
    public Recipient recipient;
    public String timeStamp;
    public Message message;
    public Postback postback;
    public Delivery delivery;
}
