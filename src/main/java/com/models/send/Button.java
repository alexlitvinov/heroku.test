package com.models.send;

import com.google.gson.annotations.SerializedName;

public class Button {
    public static enum Type {
        @SerializedName("postback")
        Postback,
        @SerializedName("web_url")
        web_url
    }
    private  Type type;
    private  String title;
    private  String payload;
    private  String url;

    public Button(Type type, String title, String url, String action){
        this.type = type;
        this.title = title;
        this.url = url;
        this.payload = action;
    }

    public static Button Url(String title, String url){
        return new Button(Type.web_url, title, url, null);
    }

    public static Button Postback(String title, String action){
        return new Button(Type.Postback, title, null, action);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
}
