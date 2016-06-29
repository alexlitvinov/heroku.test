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
import com.models.webhook.Entry;
import com.models.webhook.ReceivedMessage;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
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

    private final String PAGE_TOKEN = "EAAEUQLpUz1YBANLyFeWjimqr9IOXxZAdx0fFzaTEQLve8dxZAdZAyOoAsX9f1iaHAPeBdnuBmTJdgBiC1RVCm9vK6MCFYwiq91lQQJZASnH2NT8TNEH6RZCQffLZAK7b6aMBnQxGXICFBAmmZCtQwgSXZBt3UvoXtxMZBM1DeBkTDm2UZAjZBkvZCbQA";

    private final HttpClientManagerImpl httpImpl = new HttpClientManagerImpl();

    private final String welcome = "Acceptable commands:\n"
            + "1)buttons - see buttons with choise\n"
            + "2)template - see template";

    private final String sorryText = "sorry i understand only text :(";

    private final String catImage = "http://d39kbiy71leyho.cloudfront.net/wp-content/uploads/2016/05/09170020/cats-politics-TN.jpg";

    private ObjectMapper om = null;

    {
        om = new ObjectMapper();
        om.setSerializationInclusion(Include.NON_NULL);
    }

    private Map commands = new HashMap();

    private Map<String, String[]> users = new HashMap();
    private Map<String, String> users2 = new HashMap();

    {

        commands.put("buttons", "buttons");
        commands.put("image", "image");
        commands.put("template", "template");

    }
    private String END_POINT = "https://graph.facebook.com/v2.6/me/messages";

    public TestController() throws Exception {

    }

    private void doPost(String url, String messageStr) {
        HttpPost p = null;
        try {
            System.out.println("try to send to " + url);
            System.out.println("request body!!! " + messageStr);

            URIBuilder b = new URIBuilder(url);

            p = new HttpPost(b.build());
            p.setHeader("Content-type", "application/json");
            p.setHeader("Accept", "application/json");
            p.setEntity(new StringEntity(messageStr));
            HttpResponse response = httpImpl.getClient().execute(p);
            String responseStr = EntityUtils.toString(response.getEntity());

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
    }

    private String getString(String str) throws Exception {
        return str;
    }

    @RequestMapping("/")
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World1") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    private String[] getUserInfo(String id) {
        String url = "https://graph.facebook.com/v2.6/%%id?fields=address,first_name,last_name,profile_pic,locale,timezone,gender&access_token=%token%".replace("%id%", id).replace("%token%", this.PAGE_TOKEN);
        HttpGet p = null;
        String resp[] = new String[3];

        try {
            System.out.println("url get info "+url);
            URIBuilder b = new URIBuilder(url);

            p = new HttpGet(b.build());
            p.setHeader("Content-type", "application/json");
            p.setHeader("Accept", "application/json");

            HttpResponse response = httpImpl.getClient().execute(p);

            String responseStr = EntityUtils.toString(response.getEntity());
            System.out.println("url get info resp "+resp);
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

        String postback = null;
        for (Entry e : rm.entry) {
            sender = e.messaging.get(0).sender.id;
            text = e.messaging.get(0).message != null ? e.messaging.get(0).message.text : null;
            postback = e.messaging.get(0).postback != null ? e.messaging.get(0).postback.payload.toString() : null;
            break;
        }
        /**if (postback != null && !postback.startsWith("accept")) {
            text = postback;
        }*/
        System.out.println("try send to " + sender);
        if (sender != null && text!=null) {
            
            if (!users.containsKey(sender)){
                String[] ret=this.getUserInfo(sender);                
                users.put(sender, ret);
                users2.put(ret[0]+" "+ret[1], sender);
            }
            
            Message m = null;
            
            String[] textArr=text.split("\\s+");
            
            if (!users2.containsKey(textArr[0]+" "+textArr[1])){
                m = Message.Text("user "+textArr[0]+" "+textArr[1]+" not found");
            }else {
                String id=users2.get(textArr[0]+" "+textArr[1]);
                m = Message.Generic();
                Element e = new Element("Send", users.get(id)[2], "send "+textArr[2]+"");
                Button b = new Button(Type.Postback, getString("Send"), null, "send "+textArr[2]+" "+users2.get(textArr[0]+" "+textArr[1]));
                e.addButton(b);
                Button b2 = new Button(Type.Postback, getString("Ask phone"), null, "askphone "+users2.get(textArr[0]+" "+textArr[1]));
                e.addButton(b2);
                m.addElement(e);                
            }
            
            /*if (text == null) {
                m = Message.Text(sorryText);
            } else if (text != null && !commands.containsKey(text)) {
                m = Message.Text(welcome);
            } else if (text.equalsIgnoreCase("image")) {
                m = Message.Image(catImage);
            } else if (text.equalsIgnoreCase("buttons")) {
                m = Message.Button("What message do you want to see");
                Button b = new Button(Type.Postback, "View image", null, "image");
                m.addButton(b);
                Button b1 = new Button(Type.Postback, "View Template",
                        null, "template");
                m.addButton(b1);
            } else if (text.equals("template")) {
                m = Message.Generic();
                Element e = new Element("first", catImage, "first subtitle");
                Button b = new Button(Type.Postback, getString("Accept"), null, "accept1");
                e.addButton(b);
                m.addElement(e);
                Element e2 = new Element("second", catImage, "second subtitle");
                Button b2 = new Button(Type.Postback, getString("Accept"), null, "accept2");
                e2.addButton(b2);
                m.addElement(e2);
                Element e3 = new Element("third", catImage, "third subtitle");
                Button b3 = new Button(Type.Postback, getString("View more"), null, "template");
                e3.addButton(b3);
                m.addElement(e3);
            }*/
            this.doPost(END_POINT + "?access_token=" + PAGE_TOKEN, om.writeValueAsString(new MessageWrapper(sender, m)));
        }

        return "ok";
    }
    
    
/*
public static void main(String args[])    {
    System.out.println(new String ("\u0410\u043b\u0435\u043a\u0441\u0430\u043d\u0434\u0440"));
    System.out.println(new String(Charset.forName("UTF-8").encode("вав").array()));
    System.out.println("eee ee".split("\\s+").length);
}*/
    
}
