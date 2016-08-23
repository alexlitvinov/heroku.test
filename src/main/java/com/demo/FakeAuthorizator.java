/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * Тестовая авторизация из бота на фейсбуке
 *
 * @author it060587las
 */
@Controller
@RequestMapping("/test/")
public class FakeAuthorizator {

    @Value("${fbook.test.phone}")
    private String testPhone;

    @Value("${fbook.test.card}")
    private String testCard;

    @Value("${fbook.test.otp}")
    private String testOtp;
    //  
    private String errPhone = "errPhone";
    //
    private String errCard = "errCard";
    //
    private String errOtp = "errOtp";

    RestTemplate restTemplate = new RestTemplate();

    {
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        restTemplate.setErrorHandler(new ResponseErrorHandler() {

            @Override
            public boolean hasError(ClientHttpResponse chr) throws IOException {
                return chr.getRawStatusCode() != 200;
            }

            @Override
            public void handleError(ClientHttpResponse chr) throws IOException {
                String theString = getStringFromInputStream(chr.getBody());
                System.out.println("ERROR BODY " + theString);
            }

        });
    }

    public static class Response {

        public static final String SUCCESS = "success";
        public static final String FAILURE = "failure";

        private final String status;
        private final String message;

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        private Response(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public static Response success() {
            return new Response(Response.SUCCESS, null);
        }

        public static Response error(String error) {
            return new Response(Response.FAILURE, error);
        }
    }

    private void doGet(String url) {
        System.out.println("try to send to " + url);        
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>( headers);
        ResponseEntity<String> rese = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println("RESPONSE STRING " + rese.getBody());
    }

    private boolean safeCheck(String s1, String s2) {
        System.out.println(s1 + " " + s2);
        return s1 != null && s2 != null && s1.trim().equals(s2.trim());
    }

    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    @RequestMapping("phoneCheck")
    @ResponseBody
    public Response checkPhone(String phone) {
        return safeCheck(this.testPhone, phone) ? Response.success() : Response.error(this.errPhone);
    }

    @RequestMapping("cardCheck")
    @ResponseBody
    public Response checkCard(String card) {
        return safeCheck(this.testCard, card) ? Response.success() : Response.error(this.errCard);
    }

    @RequestMapping("otpCheck")
    @ResponseBody
    public Response checkOtp(String otp, String token) {
        boolean ok = safeCheck(this.testOtp, otp);
        if (ok) {
            this.doGet("https://www.facebook.com/messenger_platform/account_linking?account_linking_token="+token+"&authorization_code="+(new Date()).getTime());
        }
        return ok ? Response.success() : Response.error(this.errOtp);
    }

    @RequestMapping("register")
    public String checkReg(String account_linking_token, Model model) {
        model.addAttribute("token", account_linking_token);
        return "test";
    }

}
