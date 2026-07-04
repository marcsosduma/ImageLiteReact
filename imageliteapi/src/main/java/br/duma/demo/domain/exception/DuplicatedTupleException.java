package br.duma.demo.domain.exception;

public class DuplicatedTupleException extends RuntimeException{
    public DuplicatedTupleException(String message){
        super(message);
    }    
}
