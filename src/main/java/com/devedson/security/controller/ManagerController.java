package com.devedson.security.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/manager")
@PreAuthorize("hasRole('MANAGER')")
public class ManagerController {


    @GetMapping
    @PreAuthorize("hasAuthority('manager:read')")
    public ResponseEntity<String> getMethod(){
        return new ResponseEntity<>("GET::MANAGER", HttpStatus.OK);
    }


    @PostMapping
    @PreAuthorize("hasAuthority('manager:create')")
    public ResponseEntity<String> postMethod(){
        return new ResponseEntity<>("PUT::MANAGER", HttpStatus.CREATED);
    }


    @PutMapping
    @PreAuthorize("hasAuthority('manager:update')")
    public ResponseEntity<String> putMethod(){
        return new ResponseEntity<>("PUT::MANAGER",HttpStatus.NO_CONTENT);
    }



    @DeleteMapping
    @PreAuthorize("hasAuthority('manager:delete')")
    public ResponseEntity<String> deleteMethod(){
        return new ResponseEntity<>("DELETE::MANAGER",HttpStatus.NO_CONTENT);
    }
}
