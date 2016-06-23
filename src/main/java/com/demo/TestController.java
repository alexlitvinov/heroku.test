/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
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

    private final String MY_TOKEN = "430321c0901d0ee6a0eb3541a9b5d3c6";

    private final HttpClientManagerImpl httpImpl=new HttpClientManagerImpl();

    private ObjectMapper om=new ObjectMapper();
    
    private String messageTemplate="{\"recipient\":{\"id\": \"@rid\"}, \"message\": {\"text\": \"@text\"}}";
    
    public TestController()throws Exception{
        
    }
    
    private void doPost(String url, String messageStr){
         HttpPost p=null;        
        try{            
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
    
    private String getMessage(String recepient, String text){
        return this.messageTemplate.replace("@rid", recepient).replace("@text", text);
    }
    
    @RequestMapping("/")
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World1") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
    
    /**
     * 
     * @param requrestBody
     * @param req
     * @param res
     * @return
     * @throws Exception 
     */
    @RequestMapping("/webhook")
    @ResponseBody
    public String greeting(@RequestBody String requrestBody, HttpServletRequest req, HttpServletResponse res) throws Exception {
        
        System.out.println("requestBody: "+requrestBody);
        
        //авторизация
        if (req.getParameter("hub.verify_token")!=null && req.getParameter("hub.verify_token").equals(MY_TOKEN)) {
            return req.getParameter("hub.challenge");
        } else if (req.getParameter("hub.verify_token")!=null) {
            return "Error, wrong validation token";
        }        
        //если что обрабатываю сообщение                
        JsonNode n=om.readTree(requrestBody);
        
        ArrayNode a=(ArrayNode) n.get("entry");
        
        String recepuient=a.get(0).get("id").asText();
        String message=this.getMessage(recepuient, "HELLO !!!");
        
        
        this.doPost("https://graph.facebook.com/v2.6/me/messages?access_token="+this.MY_TOKEN, message);
        
        return "ok";
    }

    
}
