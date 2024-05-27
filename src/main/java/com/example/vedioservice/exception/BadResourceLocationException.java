package com.example.vedioservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@Slf4j
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BadResourceLocationException extends RuntimeException{

    public BadResourceLocationException(String msg) {
        super(msg);
        log.error(msg);
    }

    public BadResourceLocationException(String msg, IOException ex) {
        super(msg, ex);
    }
}
