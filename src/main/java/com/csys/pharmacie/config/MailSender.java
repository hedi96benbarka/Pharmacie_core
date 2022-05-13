///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.csys.pharmacie.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Component;
//
///**
// *
// * @author USER
// */
//@Component("MailSender")
//public class MailSender {
//
//    @Autowired
//    JavaMailSender javaMailSender;
//
//    private final Logger log = LoggerFactory.getLogger(MailSender.class);
//
//    public String sendMail(String from, String[] to, String subject, String body) {
//        if (to == null || to.length == 0) {
//            return "There is no recipient";
//        }
//        SimpleMailMessage mail = new SimpleMailMessage();
//        mail.setFrom(from);
//        mail.setTo(to);
//        mail.setSubject(subject);
//        mail.setText(body);
//
//        log.debug("Sending...");
////        javaMailSender.send(mail);
//        log.debug("Done!");
//        return "Mail Sended Successfully";
//    }
//
//    public String sendMail(String from, String to, String subject, String body) {
//        if (to == null || to.isEmpty()) {
//            return "There is no recipient";
//        }
//        SimpleMailMessage mail = new SimpleMailMessage();
//        mail.setFrom(from);
//        mail.setTo(to);
//        mail.setSubject(subject);
//        mail.setText(body);
//
//        log.debug("Sending...");
//        javaMailSender.send(mail);
//        log.debug("Done!");
//        return "Mail Sended Successfully";
//    }
//
//}
