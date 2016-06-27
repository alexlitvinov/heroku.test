package com.hyurumi.fb_bot_boilerplate.models.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Created by genki.furumi on 4/14/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    public String mid;
    public int seq;
    public List<Attachment> attachments;
    public String text;
}
