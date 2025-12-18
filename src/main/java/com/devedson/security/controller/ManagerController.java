package com.devedson.security.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/manager")
public class ManagerController {

    @GetMapping
    public ResponseEntity<String> getMethod(){
        return new ResponseEntity<>("GET : Manager", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> postMethod(){
        return new ResponseEntity<>("PUT : Manager", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> putMethod(){
        return new ResponseEntity<>("GET : Manager",HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteMethod(){
        return new ResponseEntity<>("DELETE : Manager",HttpStatus.NO_CONTENT);
    }
}
