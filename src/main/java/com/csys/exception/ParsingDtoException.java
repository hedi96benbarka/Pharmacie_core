/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.exception;


/**
 *
 * @author Farouk
 */
//@ResponseStatus(value=HttpStatus.UNPROCESSABLE_ENTITY, reason="Invalid JSON object")
public class ParsingDtoException extends RuntimeException  {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name ;
    private Exception cause;
    public ParsingDtoException(String name, Exception ex) {
        
        this.name=name;
        cause=ex;
    }

    public ParsingDtoException() {
    }

    public ParsingDtoException(String message) {
        super(message);
    }

    public ParsingDtoException(Throwable cause) {
        super(cause);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Exception getCause() {
        return cause;
    }

    public void setCause(Exception cause) {
        this.cause = cause;
    }
    
}
