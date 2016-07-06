/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo;

/**
 *
 * @author 
 */
public class User {
    public String id;
    public String fbToken;
    public String appToken;
    
    public User(){
    }
    
    public User(String id, String fbToken, String appToken) {
        this.id = id;
        this.fbToken = fbToken;
        this.appToken = appToken;
    }
    
}
