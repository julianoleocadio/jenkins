package com.api.pessoas.exceptions;

public class NoContentRuntimeException extends RuntimeException {
    public NoContentRuntimeException() { super();}
    public NoContentRuntimeException(String message) { super(message);}
    public NoContentRuntimeException(String message, Throwable cause) { super(message, cause);}
    public NoContentRuntimeException(String msg, NoContentRuntimeException exc) { super(msg, exc);}
}