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
    "          \"title\": \"Welcome to M-Bank\",\n" +
    "          \"image_url\": \"http://www.example.com/images/m-bank.png\",\n" +
    "          \"buttons\": [{\n" +
    "            \"type\": \"account_link\",\n" +
    "            \"url\": \"https://privat24.ua\"\n" +
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
    public String greeting(@RequestParam(value = "account_linking_token", required = false) String token, @RequestParam(value = "redirect_uri", required = false) String red, Model model) {
        model.addAttribute("token", token);
        model.addAttribute("cb", red);
        return "greeting";
    }

    private String[] getUserInfo(String id) {
        String url = "https://graph.facebook.com/v2.6/%id%?fields=first_name,last_name,profile_pic,locale,timezone,gender&access_token=%token%".replace("%id%", id).replace("%token%", this.PAGE_TOKEN);
        HttpGet p = null;
        String resp[] = new String[3];

        try {
            System.out.println("url get info "+url);
            

            p = new HttpGet(url);
            p.setHeader("Content-type", "application/json");
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

        for (Entry e : rm.entry) {
            sender = e.messaging.get(0).sender.id;
            text = e.messaging.get(0).message != null ? e.messaging.get(0).message.text : null;
            break;
        }
        
        System.out.println("try send to " + sender);
        if (sender != null && text!=null) {
            
            
            Message m = null;
            
            this.doPost(END_POINT + "?access_token=" + PAGE_TOKEN, this.loginString.replace("USER_ID", sender));
            m = Message.Text("input first_name last_name sum");            
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
