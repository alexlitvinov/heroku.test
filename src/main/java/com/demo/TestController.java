/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    private final String MY_TOKEN = "430321c0901d0ee6a0eb3541a9b5d3c6";

    @RequestMapping("/")
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World1") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @RequestMapping("/webhook")
    @ResponseBody
    public String greeting(HttpServletRequest req, HttpServletResponse res) {
        if (req.getParameter("hub.verify_token").equals(MY_TOKEN)) {
            return req.getParameter("hub.challenge");
        } else {
            return "Error, wrong validation token";
        }
    }
}
