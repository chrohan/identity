package com.assignment.identity.services;

import com.assignment.identity.dto.IdentityRequest;
import com.assignment.identity.dto.IdentityResponse;
import com.assignment.identity.model.Contact;
import com.assignment.identity.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ContactServices {

    private IdentityResponse buildResponse(Contact primary, List<Contact> secondaries) {
        Set<String> emails = new LinkedHashSet<>();
        Set<String> phoneNumbers = new LinkedHashSet<>();
        List<Long> secondaryIds = new ArrayList<>();

        if (primary.getEmail() != null)
            emails.add(primary.getEmail());
        if (primary.getPhoneNumber() != null)
            phoneNumbers.add(primary.getPhoneNumber());

        for (Contact c : secondaries) {
            if (c.getEmail() != null)
                emails.add(c.getEmail());
            if (c.getPhoneNumber() != null)
                phoneNumbers.add(c.getPhoneNumber());
            secondaryIds.add(c.getId());
        }

        IdentityResponse.ContactResponse contactResponse = new IdentityResponse.ContactResponse();
        contactResponse.setPrimaryContactId(primary.getId());
        contactResponse.setEmails(new ArrayList<>(emails));
        contactResponse.setPhoneNumbers(new ArrayList<>(phoneNumbers));
        contactResponse.setSecondaryContactIds(secondaryIds);

        IdentityResponse response = new IdentityResponse();
        response.setContact(contactResponse);
        return response;
    }

    private final ContactRepository contactRepository;

    @Autowired
    public ContactServices(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public IdentityResponse identityContact(IdentityRequest request) {
        String email = request.getEmail();
        String phoneNumber = request.getPhoneNumber();

        List<Contact> matched = contactRepository.findByEmailOrPhoneNumber(email, phoneNumber);

        // if I did not find any contacts matched
        if(matched.isEmpty()) {
            Contact newContact = new Contact();
            newContact.setEmail(email);
            newContact.setPhoneNumber(phoneNumber);
            newContact.setLinkPrecedence("primary");
            contactRepository.save(newContact);
            return buildResponse(newContact, Collections.emptyList());
        }

        Set<Contact> allRelatedContacts = new HashSet<>(matched);
        Queue<Contact> queue = new LinkedList<>(matched);

        // looping in bfs style to find all the contacts that can be related I mean if contact A has ph no. or email same as B and B has same as C they are related
        while(!queue.isEmpty()) {
            Contact curr = queue.poll();
            List<Contact> related = contactRepository.findByEmailOrPhoneNumber(curr.getEmail(), curr.getPhoneNumber());

            for(Contact c : related) {
                if(!allRelatedContacts.contains(c)) {
                    allRelatedContacts.add(c);
                    queue.add(c);
                }
            }
        }

        // primary will be the one which is oldest, finding primary contact
        Contact primary = null;
        for(Contact c : allRelatedContacts) {
            if(c.getLinkPrecedence().equals("primary")) {
                if(primary == null || c.getCreatedAt().isBefore(primary.getCreatedAt())) {
                    primary = c;
                }
            }
        }
        //setting all the contacts other than primary as "secondary"
        for(Contact c : allRelatedContacts) {
            if(c.getLinkPrecedence().equals("primary") && !c.getId().equals(primary.getId())) {
                c.setLinkPrecedence("secondary");
                c.setLinkedId(primary.getId());
                c.setUpdatedAt(LocalDateTime.now());
                contactRepository.save(c);
            }
        }

       // if the contact not there than make it secondary and add
        boolean alreadyExists = false;
        for(Contact c : allRelatedContacts) {
            if(c.getEmail() !=null && c.getEmail().equals(email)) {
                alreadyExists = true;
                break;
            }
            if(c.getPhoneNumber() != null && c.getPhoneNumber().equals(phoneNumber)) {
                alreadyExists = true;
                break;
            }
        }
        if(!alreadyExists) {
            Contact newSecondary = new Contact();
            newSecondary.setEmail(email);
            newSecondary.setPhoneNumber(phoneNumber);
            newSecondary.setLinkPrecedence("secondary");
            newSecondary.setLinkedId(primary.getId());
            contactRepository.save(newSecondary);
            allRelatedContacts.add(newSecondary);
        }

        List<Contact> secondaries = new ArrayList<>();
        for (Contact c : allRelatedContacts) {
            if (!c.getId().equals(primary.getId())) {
                secondaries.add(c);
            }
        }

        return buildResponse(primary, secondaries);

    }

}
