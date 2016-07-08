/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.send.Button;
import com.models.send.Button.Type;
import com.models.send.Element;
import com.models.send.Message;
import com.models.webhook.AccountLinking;
import com.models.webhook.Entry;
import com.models.webhook.ReceivedMessage;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class TestController {

    private final String MY_TOKEN = "b2b07503c2439b2009ae61c2f3fd5014";

    private final String PAGE_TOKEN = "EAAEUQLpUz1YBANLyFeWjimqr9IOXxZAdx0fFzaTEQLve8dxZAdZAyOoAsX9f1iaHAPeBdnuBmTJdgBiC1RVCm9vK6MCFYwiq91lQQJZASnH2NT8TNEH6RZCQffLZAK7b6aMBnQxGXICFBAmmZCtQwgSXZBt3UvoXtxMZBM1DeBkTDm2UZAjZBkvZCbQA";

    private final HttpClientManagerImpl httpImpl = new HttpClientManagerImpl();   
    
    private String loginString="{\n" +
    "  \"recipient\":{\n" +
    "    \"id\":\"USER_ID\"\n" +
    "  },\n" +
    "  \"message\": {\n" +
    "    \"attachment\": {\n" +
    "      \"type\": \"template\",\n" +
    "      \"payload\": {\n" +
    "        \"template_type\": \"generic\",\n" +
    "        \"elements\": [{\n" +
    "          \"title\": \"Welcome to TEST BOT\",\n" +
    "          \"image_url\": \"http://www.example.com/images/m-bank.png\",\n" +
    "          \"buttons\": [{\n" +
    "            \"type\": \"account_link\",\n" +
    "            \"url\": \"https://fbookbot.herokuapp.com\"\n" +
    "          }]\n" +
    "        }]\n" +
    "      }\n" +
    "    }\n" +
    "  }\n" +
    "}";
    
    private ObjectMapper om = null;
    
    {
        om = new ObjectMapper();
        om.setSerializationInclusion(Include.NON_NULL);
    }
    
    private String END_POINT = "https://graph.facebook.com/v2.6/me/messages";
    
    private Map<String, User> users=new HashMap();
    
    private Map<String, String> fbtokens=new HashMap();
    
    private Map<String, String> apptokens=new HashMap();
   
    private Map<String, String> tokens=new HashMap();
        
    public TestController() throws Exception {

    }

   RestTemplate restTemplate = new RestTemplate();
   {
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
   }
        
    private void doPost(String url, String messageStr) {
        
        System.out.println("try to send to " + url);
        
        System.out.println("request body!!! " + messageStr);
         
        HttpHeaders headers = new HttpHeaders();
        
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> entity = new HttpEntity<String>(messageStr, headers);
        
        ResponseEntity<String> rese=restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        
        System.out.println("RESPONSE STRING " + rese.getBody());

    }
    
    private String doGet(String url) {
        HttpGet p = null;
        try {
            System.out.println("try to send to " + url);                                    

            p = new HttpGet(url);
            p.setHeader("Content-type", "application/json");
            p.setHeader("Accept", "application/json");
            
            HttpResponse response = httpImpl.getClient().execute(p);
            String responseStr = EntityUtils.toString(response.getEntity());
            System.out.println("RESPONSE STRING " + responseStr);
            return responseStr;
        } catch (Exception ex1) {
            ex1.printStackTrace();
        } finally {
            try {
                if (p != null) {
                    p.releaseConnection();
                }
            } catch (Exception ex2) {
            }
        }
        return null;
    }

    /**
     * этот кусок обрабатывает запрос на подключение аккаунта
     * @param token
     * @param red
     * @param model
     * @return 
     */    
    @RequestMapping("/")
    public String greeting(@RequestParam(value = "account_linking_token", required = false) String token, @RequestParam(value = "redirect_uri", required = false) String red, Model model)  {      
        return "greeting";
    }
    
    /*@RequestMapping("/authcomplete")
    public String authcomplete(String name, String pwd, Model model, String token) {
        try{
        if (name.equals("user") && pwd.equals("password")){
            boolean res=false;
            
            //регистрация успешна, сохраняю информацию о пользователе
            String appToken=UUID.randomUUID().toString();
            
            tokens.put(appToken, token );
            
            //вызвал связку аккаунта
            doGet("https://www.facebook.com/messenger_platform/account_linking/?account_linking_token="+token+"&authorization_code="+appToken);
            
            if (this.fbtokens.containsKey(token)){
                String userId=this.fbtokens.get(token);
                if (this.users.containsKey(userId) && this.users.get(userId).appToken!=null && this.users.get(userId).fbToken!=null){
                    res=true;
                }
            }
            
            if (res){
                model.addAttribute("res", "1");                
            }else {
                model.addAttribute("res", "0");                
            }
            return "okauth";
        } else{
            return "errauth";
        }   
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }*/

    private String[] getUserInfo(String id) {
        String url = "https://graph.facebook.com/v2.6/%id%?fields=first_name,last_name,profile_pic,locale,timezone,gender&access_token=%token%".replace("%id%", id).replace("%token%", this.PAGE_TOKEN);
        HttpGet p = null;
        String resp[] = new String[3];

        try {
            System.out.println("url get info "+url);
            

            p = new HttpGet(url);
            p.setHeader("Content-type", "application/json;");
            p.setHeader("Accept", "application/json");

            HttpResponse response = httpImpl.getClient().execute(p);

            String responseStr = EntityUtils.toString(response.getEntity());
            System.out.println("url get info resp "+responseStr);
            JsonNode d = om.readTree(responseStr);

            resp[0] = d.get("first_name").asText();
            resp[1] = d.get("last_name").asText();
            resp[2] = d.get("profile_pic").asText();

            System.out.println("RESPONSE STRING " + responseStr);

        } catch (Exception ex1) {
            ex1.printStackTrace();
        } finally {
            try {
                if (p != null) {
                    p.releaseConnection();
                }
            } catch (Exception ex2) {
            }
        }
        return resp;
    }
    
    private boolean isUserRegistred(String id){
        if (users.containsKey(id)){
            return users.get(id).fbToken!=null &&  users.get(id).appToken!=null;
        }
        return false;
    }
    
    private boolean isUserRegistred2(String id){
        if (users.containsKey(id)){
            return users.get(id).phone!=null &&  users.get(id).card!=null;
        }
        return false;
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

        System.out.println("requestBody: " + requestBody);

        //авторизация
        if (req.getParameter("hub.verify_token") != null && req.getParameter("hub.verify_token").equals(PAGE_TOKEN)) {
            return req.getParameter("hub.challenge");
        } else if (req.getParameter("hub.verify_token") != null) {
            return "Error, wrong validation token";
        }
        
        //если что обрабатываю сообщение                
        ReceivedMessage rm = om.readValue(requestBody, ReceivedMessage.class);
        
        String sender = null;

        String text = null;
        
        AccountLinking al=null;       
        
        for (Entry e : rm.entry) {
            sender = e.messaging.get(0).sender.id;
            text = e.messaging.get(0).message != null ? e.messaging.get(0).message.text : null;
            break;
        }
        
        String [] userInfo=this.getUserInfo(sender);
        
        String firstLast=userInfo[0]+" "+userInfo[1];
        //если пользователь не зарегистрированб то шлю ему сообщение чтобы зарегался
        if (!this.users.containsKey(sender)){
            Message m=Message.Text(firstLast+", для работы с ботом вам необходимо авторизоваться");
            this.doPost(END_POINT + "?access_token=" + PAGE_TOKEN,  om.writeValueAsString(new MessageWrapper(sender, m)));
            User usr=new User();
            this.users.put(sender, usr);
            //return "ok";
        }
        if (this.users.get(sender).st!=4){
            User u=this.users.get(sender);
            //запрашиваем телефон ,помечаем что юзер ждет телефона
            if (u.phone==null && u.st==0){
                Message m=Message.Text("Введите ваш телефон");
                this.doPost(END_POINT + "?access_token=" + PAGE_TOKEN,  om.writeValueAsString(new MessageWrapper(sender, m)));
                u.st=1;                
                this.users.put(sender, u);
            } else if (u.phone==null && u.st==1){
                if (text==null || text.trim().isEmpty()){
                    Message m=Message.Text("Телефон не может быть пустым введите телефон еще раз.");
                    this.doPost(END_POINT + "?access_token=" + PAGE_TOKEN,  om.writeValueAsString(new MessageWrapper(sender, m)));
                }else{
                    u.phone=text;
                    u.st=2;
                    this.users.put(sender, u);
                }
            } 
            if (u.card==null && u.st==2){
                Message m=Message.Text("Введите 4 номер карты");
                this.doPost(END_POINT + "?access_token=" + PAGE_TOKEN,  om.writeValueAsString(new MessageWrapper(sender, m)));
                u.st=3;                
                this.users.put(sender, u);                
            } else if (u.card==null && u.st==3){
                if (text==null || text.trim().isEmpty()){
                    Message m=Message.Text("Номер карты не может быть пустым. введите номер карты еще раз.");
                    this.doPost(END_POINT + "?access_token=" + PAGE_TOKEN,  om.writeValueAsString(new MessageWrapper(sender, m)));
                }else{
                    u.card=text;
                    u.st=4;
                    this.users.put(sender, u);
                    Message m=Message.Text("Регистрация успешно завершена :). Для продолжения работы напишите что-то в чат с ботом.");
                    this.doPost(END_POINT + "?access_token=" + PAGE_TOKEN,  om.writeValueAsString(new MessageWrapper(sender, m)));
                }
            }
            return "ok";
        }
        
                                
        System.out.println("try send to " + sender);
        if (sender != null && text!=null) {            
            
               Message m=Message.Generic();
               Element e=new Element("Первый шаблон","https://fbookbot.herokuapp.com/first.png", "Здесь будет лицевой счет или дата" );
               Button b=new Button(Type.WebUrl, "Оплатить", "https://fbookbot.herokuapp.com/?templ=1", null);    
               e.addButton(b);
               m.addElement(e);
               Element e2=new Element("Второй шаблон","https://fbookbot.herokuapp.com/second.png", "Здесь будет лицевой счет или дата" );
               Button b2=new Button(Type.WebUrl, "Оплатить", "https://fbookbot.herokuapp.com/?templ=2", null);    
               e2.addButton(b2);
               m.addElement(e2);
               Element e3=new Element("Третий шаблон","https://fbookbot.herokuapp.com/third.png", "Здесь будет лицевой счет или дата" );
               Button b3=new Button(Type.WebUrl, "Оплатить", "https://fbookbot.herokuapp.com/?templ=3", "template");    
               e3.addButton(b3);
               m.addElement(e3);
            
               this.doPost(END_POINT + "?access_token=" + PAGE_TOKEN,  om.writeValueAsString(new MessageWrapper(sender, m)));
                        
        }

        return "ok";
    }                

}
