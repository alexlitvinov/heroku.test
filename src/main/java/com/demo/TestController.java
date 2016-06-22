/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TestController {

    @RequestMapping("/")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World1") String name, Model model) {
        model.addAttribute("name", name);
        return  "greeting";
    }
    
    
}
