package com.devedson.security.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test-controller")
public class SecuredEndPointController {

    @GetMapping
    public ResponseEntity<String> seyHi(){
        return new ResponseEntity<>("Hi, secured endpoint", HttpStatus.OK);
    }
}
