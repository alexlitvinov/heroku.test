package com.models.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    public String mid;
    public int seq;
    public List<Attachment> attachments;
    public String text;
}
