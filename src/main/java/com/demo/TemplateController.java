/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * контроллер, который формирует шаблоны
 * @author it060587las
 */
@Controller
public class TemplateController {
    /**
     * получение html шаблона
     * @param cn название компании
     * @param tn название шаблона
     * @param balance баланс 
     * @param date 
     * @param lastPay 
     * @param account лицевой счет
     * @param model
     * @param subId
     * @return 
     */
    @RequestMapping("template")
    public String getTepmplate(String cn, String tn, String balance, String date, String lastPay, String account, Model model, int subId){
        model.addAttribute("cn", cn);
        model.addAttribute("tn", tn);
        model.addAttribute("balance", balance);
        model.addAttribute("date", date);
        model.addAttribute("lastPay", lastPay);
        model.addAttribute("account", account);
        return "template";
    }
}

