package com.devedson.security.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class SecuredEndPoint {

    @GetMapping
    public ResponseEntity<String> seyHello(){
        return new ResponseEntity<>("Hello secured endpoint", HttpStatus.OK);
    }
}
