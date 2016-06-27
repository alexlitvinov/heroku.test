package com.hyurumi.fb_bot_boilerplate.models.webhook;

import com.google.gson.annotations.SerializedName;

/**
 * Created by genki.furumi on 4/23/16.
 */
public class Attachment {
    public enum Type {
       audio,
       image,
       video
    }

    public Type type;
    public Payload payload;
}
