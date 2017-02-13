package com.talebase.cloud.common;

import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.ServiceResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by eric.du on 2016-12-2.
 */
//@ControllerAdvice
//public class GlobalExceptionHandler {
//    @ExceptionHandler(value = WrappedException.class)
//    public ServiceResponse jsonErrorHandler(HttpServletRequest req, WrappedException e) throws Exception {
//        ServiceResponse serviceResponse = new ServiceResponse<>();
//        serviceResponse.setMessage(e.getOpenServiceErrEnum().getMessage());
//        serviceResponse.setCode(e.getOpenServiceErrEnum().getCode());
//        serviceResponse.setResponse(req.getRequestURL().toString());
//        return serviceResponse;
//    }
//}
