package com.models.common;

public class Recipient {

    public String id;
    public String phone_number;
    
    public Recipient() {

    }

    public Recipient(String id) {
        this.id = id;
    }

    public Recipient(String id, String phone_number) {
        this.id = id;
        this.phone_number = phone_number;
    }
    
    
}
