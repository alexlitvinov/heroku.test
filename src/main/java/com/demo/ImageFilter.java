/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author
 */
@Controller
public class ImageFilter {

    @RequestMapping("img/{name}.{ext}")
    public void getPicture(HttpServletRequest req, HttpServletResponse res) throws Exception {
        try {
            URL imageURL = new URL("https://fbookbot.herokuapp.com/long.png");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream is = null;
            try {
                is = imageURL.openStream();
                byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
                int n;

                while ((n = is.read(byteChunk)) > 0) {
                    baos.write(byteChunk, 0, n);
                }
            } catch (IOException e) {                
                e.printStackTrace();
                // Perform any other exception handling that's appropriate.
            } finally {
                if (is != null) {
                    is.close();
                }
            }

            res.setContentType("image/png");
            res.getOutputStream().write(baos.toByteArray());
        } catch (Exception e) {
            System.out.println("222222222!!!!!");
            e.printStackTrace();
        }
    }
    
    public String getPl(){
        try {
            URL imageURL = new URL("https://fbookbot.herokuapp.com/long.png");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream is = null;
            try {
                is = imageURL.openStream();
                byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
                int n;

                while ((n = is.read(byteChunk)) > 0) {
                    baos.write(byteChunk, 0, n);
                }
            } catch (IOException e) {                
                e.printStackTrace();
                // Perform any other exception handling that's appropriate.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
            return Base64.encodeBase64String(baos.toByteArray());
        } catch (Exception e) {
            System.out.println("222222222!!!!!");
            e.printStackTrace();
        }
        return null;
    }
}
