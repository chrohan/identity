package com.assignment.identity.controller;

import com.assignment.identity.dto.IdentityRequest;
import com.assignment.identity.dto.IdentityResponse;
import com.assignment.identity.services.ContactServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/identify")
public class IdentityController {

    private ContactServices contactService;

    @Autowired
    public IdentityController(ContactServices contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<IdentityResponse> identify(@RequestBody IdentityRequest request) {
        IdentityResponse response = contactService.identityContact(request);
        return ResponseEntity.ok(response);
    }

}
