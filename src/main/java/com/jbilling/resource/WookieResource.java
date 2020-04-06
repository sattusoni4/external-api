package com.jbilling.resource;


import com.fasterxml.jackson.databind.JsonNode;
import com.jbilling.service.WookieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;

@RestController
@RequestMapping("wookie")
public class WookieResource {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Logger emailLogger = LoggerFactory.getLogger("emailNotification");
    private static final STGroup stGroup = new STGroupFile("email.stg");
    private final WookieService wookieService;

    @Autowired
    public WookieResource(WookieService wookieService) {
        this.wookieService = wookieService;
    }

    @GetMapping("/email")
    public String test() {
        return "Hello From External API";
    }


    @PostMapping("/email")
    public ResponseEntity<String> printNotification(@RequestBody JsonNode message) throws IOException {
        String template = message.get("type").asText();
        System.out.println("template = " + template);

        String emailTemplate = null;
        try {
            switch (template) {
                case "Usage Pool":
                    emailTemplate = wookieService.usagesPoolTemplate(message);
                    break;
                case "Mobile Data":
                    emailTemplate = wookieService.mobileDataTemplate(message);
                    break;
                case "Data Boost 1":
                case "Data Boost 2":
                case "Data Boost 3":
                    emailTemplate = wookieService.dataBoostTemplate(message);
                    break;
                case "Credit Pool":
                    emailTemplate = wookieService.creditPoolTemplate(message);
                    break;
                default:
                    logger.error("No Email Template found for the type {}", template);
                    emailTemplate = "No Email Template found for the type " + template;

            }
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
        emailLogger.info(emailTemplate);
        return new ResponseEntity<>(emailTemplate, HttpStatus.ACCEPTED);
    }
}

