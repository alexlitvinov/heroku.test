/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyurumi.fb_bot_boilerplate.models.send.Message;
import com.hyurumi.fb_bot_boilerplate.models.send.Response;
import com.hyurumi.fb_bot_boilerplate.models.webhook.Entry;
import com.hyurumi.fb_bot_boilerplate.models.webhook.ReceivedMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    private final String MY_TOKEN = "b2b07503c2439b2009ae61c2f3fd5014";
 
    private final String PAGE_TOKEN="EAAEUQLpUz1YBANLyFeWjimqr9IOXxZAdx0fFzaTEQLve8dxZAdZAyOoAsX9f1iaHAPeBdnuBmTJdgBiC1RVCm9vK6MCFYwiq91lQQJZASnH2NT8TNEH6RZCQffLZAK7b6aMBnQxGXICFBAmmZCtQwgSXZBt3UvoXtxMZBM1DeBkTDm2UZAjZBkvZCbQA";
    
    private final HttpClientManagerImpl httpImpl=new HttpClientManagerImpl();

    private ObjectMapper om=new ObjectMapper();
   
    public TestController()throws Exception{
        
    }
    
    private void doPost(String url, String messageStr){
         HttpPost p=null;        
        try{            
            System.out.println("try to send to "+url);
            System.out.println("request body!!! "+messageStr);
             
            URIBuilder b = new URIBuilder(url);
            
            p= new HttpPost(b.build());              
            p.setHeader("Content-type", "application/json");
            p.setHeader("Accept", "application/json");
            p.setEntity(new StringEntity(messageStr));            
            HttpResponse response = httpImpl.getClient().execute(p);            
            String responseStr=EntityUtils.toString(response.getEntity());
            
            System.out.println("RESPONSE STRING "+responseStr);
            
        }catch (Exception ex1){
            ex1.printStackTrace();
        }finally{
            try{
                if (p!=null) p.releaseConnection();
            }catch(Exception ex2){
            }
        }
    }
    

    @RequestMapping("/")
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World1") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
    
    /**
     * 
     * @param requestBody
     * @param req
     * @param res
     * @return
     * @throws Exception 
     */
    @RequestMapping("/webhook")
    @ResponseBody
    public String greeting(@RequestBody String requestBody, HttpServletRequest req, HttpServletResponse res) throws Exception {
         
        System.out.println("requestBody: "+requestBody);
        
        //авторизация
        if (req.getParameter("hub.verify_token")!=null && req.getParameter("hub.verify_token").equals(PAGE_TOKEN)) {
            return req.getParameter("hub.challenge");
        } else if (req.getParameter("hub.verify_token")!=null) {
            return "Error, wrong validation token";
        }        
        //если что обрабатываю сообщение                
        ReceivedMessage rm=om.readValue(requestBody, ReceivedMessage.class);
        
        String sender=null;
        
        for (Entry e: rm.entry){          
            sender=e.messaging.get(0).sender.id;
            break;
        }
        System.out.println("try send to "+sender);
        if (sender!=null){
            Message m=Message.Text("hello");
            Response r=m.sendTo(sender);
            System.out.println("response is "+om.writeValueAsString(r));
        }
        
        return "ok";
    }

    
}
