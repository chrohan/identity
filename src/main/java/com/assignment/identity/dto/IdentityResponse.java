package com.assignment.identity.dto;

import java.util.List;

public class IdentityResponse {
    private ContactResponse contact;

    public static class ContactResponse {
        private Long primaryContactId;
        private List<String> emails;
        private List<String> phoneNumbers;
        private List<Long> secondaryContactIds;

        public Long getPrimaryContactId() {
            return primaryContactId;
        }

        public void setPrimaryContactId(Long primaryContactId) {
            this.primaryContactId = primaryContactId;
        }

        public List<String> getEmails() {
            return emails;
        }

        public void setEmails(List<String> emails) {
            this.emails = emails;
        }

        public List<String> getPhoneNumbers() {
            return phoneNumbers;
        }

        public void setPhoneNumbers(List<String> phoneNumbers) {
            this.phoneNumbers = phoneNumbers;
        }

        public List<Long> getSecondaryContactIds() {
            return secondaryContactIds;
        }

        public void setSecondaryContactIds(List<Long> secondaryContactIds) {
            this.secondaryContactIds = secondaryContactIds;
        }
    }

    public ContactResponse getContact() {
        return contact;
    }

    public void setContact(ContactResponse contact) {
        this.contact = contact;
    }
}
