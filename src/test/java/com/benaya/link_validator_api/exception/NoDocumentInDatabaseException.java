package com.benaya.link_validator_api.exception;

public class NoDocumentInDatabaseException extends NullPointerException{
    public NoDocumentInDatabaseException() {
        super("No document in database");
    }
}
