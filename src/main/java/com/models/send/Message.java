package com.models.send;

public class Message {

    final private Attachment attachment;
    final private String text;
    public String filedata;

    public Message(Attachment attachment, String text) {
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
