package com.talebase.cloud.gateway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by eric.du on 2016-12-2.
 */
@RestController
@RequestMapping("/error")
public class GlobalErrorController extends AbstractErrorController {
    @Autowired
    public GlobalErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(produces = MediaType.ALL_VALUE)
    public ResponseEntity error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        if (status.is5xxServerError()) {
            return new ResponseEntity(status);
        }else if(status.is4xxClientError()){
            return new ResponseEntity(status);
        }
        return new ResponseEntity(status);
    }
}
