package com.hyurumi.fb_bot_boilerplate.models.send;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by genki.furumi on 4/14/16.
 */
public class Payload {
    @SerializedName("template_type")
     private String template_type;
     private String text;
     private String url;
     private List<Element> elements;
     private List<Button> buttons;

    private Payload(String type, String text, String url, List<Element> elements, List<Button> buttons){
        this.template_type = type;
        this.text = text;
        this.url = url;
        this.elements = elements;
        this.buttons = buttons;
    }

    public static Payload Button(String text){
        return new Payload("button", text, null, null, new ArrayList<>());
    }

    public static Payload Generic(){
        return new Payload("generic", null, null, new ArrayList<>(), null);
    }

    public static Payload Image(String url) {
        return new Payload(null, null, url, null, null);

    }

    public boolean addButton(Button button) {
        if (buttons != null) {
            return buttons.add(button);
        }else {
            return false;
        }
    }

    public boolean addElement(Element element) {
        if (elements != null) {
            return elements.add(element);
        }else {
            return false;
        }
    }

   
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    public String getTemplate_type() {
        return template_type;
    }

    public void setTemplate_type(String template_type) {
        this.template_type = template_type;
    }
    
    
}
