/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo;

import gui.ava.html.Html2Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        ByteArrayOutputStream baos =null;
        System.out.println(req.getRequestURL());
        try {
            System.out.println(req.getContextPath());
            Html2Image h2i = Html2Image.fromURL(new URL("http://localhost:8080/template?subId=1"));            
            h2i.getImageRenderer().setWidth(365);
            BufferedImage originalImage = h2i.getImageRenderer().getBufferedImage();
            baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "png", baos);
            baos.flush();            
            res.setHeader("Content-Type", "image/png");
            res.getOutputStream().write(baos.toByteArray());
        } catch (Exception e) {
            System.out.println("222222222!!!!!");
            e.printStackTrace();
        } finally{
            if (baos!=null){
                baos.close();
            }
        }

    }
}
