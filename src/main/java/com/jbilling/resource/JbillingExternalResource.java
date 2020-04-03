package com.jbilling.resource;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping("api")
public class JbillingExternalResource {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @GetMapping("/test")
    public String test() {
        return "Hello From External API";
    }

    @PostMapping("/test")
    public ResponseEntity<String> printNotification(@RequestBody String message) {
        log.info("Request Body {}", message);
        return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
    }


}
