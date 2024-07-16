package com.uniroma3.prog.model;

import org.springframework.web.multipart.MultipartFile;

public class RegistrationForm {

    private User user;
    private Credentials credentials;
    private MultipartFile file;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

}
