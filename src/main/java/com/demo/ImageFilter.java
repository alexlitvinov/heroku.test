/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
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
    public void getPicture(HttpServletRequest req, HttpServletResponse res) throws Exception{
        URL imageURL = new URL("https://fbookbot.herokuapp.com/long.png");
        BufferedImage originalImage = ImageIO.read(imageURL);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(originalImage, "jpg", baos);
        
        res.setContentType("image/png");
        res.getOutputStream().write(baos.toByteArray());
    }
}
