package com.models.webhook;


public class Attachment {
    public enum Type {
       audio,
       image,
       video
    }

    public Type type;
    public Payload payload;
}
