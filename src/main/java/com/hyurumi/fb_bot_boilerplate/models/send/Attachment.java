package com.hyurumi.fb_bot_boilerplate.models.send;

import com.google.gson.annotations.SerializedName;

/**
 * Created by genki.furumi on 4/14/16.
 */
public class Attachment {

    public enum Type {
        @SerializedName("template")
        TEMPLATE,
        @SerializedName("image")
        IMAGE
    }

     private Type type;
     private Payload payload;

    private Attachment(Type type, Payload payload) {
        this.type = type;
        this.payload = payload;
    }

    public static Attachment Template(Payload payload) {
        return new Attachment(Type.TEMPLATE, payload);
    }

    public static Attachment Image(String url) {
        return new Attachment(Type.IMAGE, Payload.Image(url));
    }

    public boolean addElement(Element element) {
        return payload.addElement(element);
    }

    public boolean addButton(Button button) {
        return payload.addButton(button);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

}
