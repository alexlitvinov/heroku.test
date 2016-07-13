package com.demo;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class IshopApplication {

	/*public static void main(String[] args) {
		SpringApplication.run(IshopApplication.class, args);
	}*/
    
    public static void main(String args[])throws Exception{
        String base=TestController.doGet("http://localhost:3000/getbase64");
        System.out.print(base.substring(22, base.length()));
        Base64.getDecoder().decode(base.substring(22, base.length()));
    }
}
