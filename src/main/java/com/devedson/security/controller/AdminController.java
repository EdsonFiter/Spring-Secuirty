package com.devedson.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @GetMapping
    public ResponseEntity<String> getMethod(){
        return new ResponseEntity<>("GET::ADMIN", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> postMethod(){
        return new ResponseEntity<>("POST::ADMIN", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> putMethod(){
        return new ResponseEntity<>("PUT:ADMIN",HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteMethod(){
        return new ResponseEntity<>("DELETE::ADMIN",HttpStatus.NO_CONTENT);
    }
}
