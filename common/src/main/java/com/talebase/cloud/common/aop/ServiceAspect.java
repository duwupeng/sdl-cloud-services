package com.talebase.cloud.common.aop;

import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Created by eric on 16/11/17.
 */
@Aspect
@Component
public class ServiceAspect {

    private static Logger logger = LoggerFactory.getLogger(ServiceAspect.class);

    @Pointcut("execution( * com.talebase.cloud.*.*.controller..*(..))")
    public void webLog(){}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        try{
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            // 记录下请求内容
            if(attributes != null && attributes.getRequest() != null){
                HttpServletRequest request = attributes.getRequest();
                logger.info("URL : " + request.getRequestURL().toString());
                logger.info("HTTP_METHOD : " + request.getMethod());
                logger.info("IP : " + request.getRemoteAddr());
            }
            logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
//            logger.info("ARGS : " + GsonUtil.toJson(joinPoint.getArgs()));
        }catch (Throwable throwable){
            logger.error("do Before 异常:" + throwable.getMessage());
        }
    }

    @Around("webLog()") //环绕通知的方法签名是规定的！！！
    public Object doAround(ProceedingJoinPoint pjp){
        Object result = null;
        try {
             result = pjp.proceed();
        }catch (WrappedException we){
            result = new ServiceResponse(we);
            System.out.println(GsonUtil.toJson(result));
        } catch (Throwable e){
            e.printStackTrace();
            result= new ServiceResponse();
            ((ServiceResponse)result).setCode(500);
            ((ServiceResponse)result).setMessage("服务繁忙，敬请谅解!");
        }

        return result;
    }
}
