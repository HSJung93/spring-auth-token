package com.example.demo.service.mail;

public interface EmailService {

    void sendMail(String to, String sub, String text);
}
