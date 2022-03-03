/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author bassatine
 */
@Entity
@Table(name = "cron_error")
public class cron_error implements Serializable {

    @Column(name = "code")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer code;
    @Column(name = "error_msg")
    private String msg;
    @Column(name = " datesys")
    private LocalDateTime date;
    @Column(name = "error_cause")
    private String cause;

    public cron_error() {
    }

    public cron_error(String msg, LocalDateTime date, String cause) {
        this.msg = msg;
        this.date = date;
        this.cause = cause;

    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public cron_error(String msg, String cause) {
        this.msg = msg;
        this.cause = cause;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
