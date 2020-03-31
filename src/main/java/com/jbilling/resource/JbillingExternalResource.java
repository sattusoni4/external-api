package com.jbilling.resource;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

@RestController
@RequestMapping("api")
public class JbillingExternalResource {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    final
    Environment environment;

    public JbillingExternalResource(Environment environment) {
        this.environment = environment;
    }

    /* @Value("${server.address}")
     private String server;
 */
    @GetMapping("/test")
    public String test() {
        return "Hello From External API";
    }


    InetAddress ip;

    @PostMapping("/test")
    public ResponseEntity<String> printNotification(@RequestBody String message) throws UnknownHostException {
        log.debug("Request Body {}", message);
        ip=InetAddress.getLocalHost();
        System.out.println("server::" + ip);
        System.out.println("server::" + ip.getHostAddress());
        return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
    }

}
