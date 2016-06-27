package com.hyurumi.fb_bot_boilerplate.models.send;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import com.hyurumi.fb_bot_boilerplate.models.common.Recipient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;

import okhttp3.MediaType;

/**
 * Created by genki.furumi on 4/15/16.
 */
public class Message {
   

    final private Attachment attachment;
    final private String text;

    public  Message(Attachment attachment, String text) {
        this.attachment = attachment;
        this.text = text;
    }

    public static Message Text(String text) {
        return new Message(null, text);
    }

    public static Message Image(String url) {
        return new Message(Attachment.Image(url), null);
    }

    public static Message Button(String text) {
        return new Message(Attachment.Template(Payload.Button(text)), null);
    }

    public static Message Generic() {
        return new Message(Attachment.Template(Payload.Generic()), null);
    }

    public boolean addElement(Element element) {
        return attachment.addElement(element);
    }

    public boolean addButton(Button button) {
        return attachment.addButton(button);
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public String getText() {
        return text;
    }

   
}
