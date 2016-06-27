package com.hyurumi.fb_bot_boilerplate.models.send;

import com.google.gson.annotations.SerializedName;
import com.hyurumi.fb_bot_boilerplate.models.common.Action;

/**
 * Created by genki.furumi on 4/15/16.
 */
public class Button {
    public static enum Type {
        @SerializedName("postback")
        Postback,
        @SerializedName("web_url")
        WebUrl
    }
    private  Type type;
    private  String title;
    private  Action payload;
    private  String url;

    public Button(Type type, String title, String url, Action action){
        this.type = type;
        this.title = title;
        this.url = url;
        this.payload = action;
    }

    public static Button Url(String title, String url){
        return new Button(Type.WebUrl, title, url, null);
    }

    public static Button Postback(String title, Action action){
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

    public Action getPayload() {
        return payload;
    }

    public void setPayload(Action payload) {
        this.payload = payload;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    
}
