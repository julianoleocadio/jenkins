package com.api.pessoas.exceptions;

public class MissingServletRequestParameter extends RuntimeException{
    public MissingServletRequestParameter(String message) { super(message);}
}
