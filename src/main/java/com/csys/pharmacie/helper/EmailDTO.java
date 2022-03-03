/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author DELL
 */
public class EmailDTO {
   
    @Email
    @NotNull
    private String from;

    @NotNull
    @NotEmpty
    private List<String> to;
    @NotNull
    private String subject;
    
    private byte[] attachement;
    @NotNull
    private String body;
    @NotNull
    private Boolean useHtmlInBody;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public byte[] getAttachement() {
        return attachement;
    }

    public void setAttachement(byte[] attachement) {
        this.attachement = attachement;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getUseHtmlInBody() {
        return useHtmlInBody;
    }

    public void setUseHtmlInBody(Boolean useHtmlInBody) {
        this.useHtmlInBody = useHtmlInBody;
    }
    
    

    @Override
    public String toString() {
        return "EmailDTO{" + "from=" + from + ", to=" + to + ", subject=" + subject + ", attachement=" + attachement + ", body=" + body + ", useHtmlInBody=" + useHtmlInBody + '}';
    } 
    
    
}
