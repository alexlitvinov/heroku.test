package com.models.send;

import java.util.ArrayList;
import java.util.List;


public class Element {

    private String title;

    private String image_url;
    private String subtitle;
    private List<Button> buttons;

    public Element(String title, String imageUrl, String subtitle) {
        this.title = title;
        this.image_url = imageUrl;
        this.subtitle = subtitle;
    }

    public boolean addButton(Button button) {
        if (buttons == null) {
            buttons = new ArrayList<>();
        }
        return buttons.add(button);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

}
