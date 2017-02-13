package com.talebase.cloud.common;


import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by eric on 16/11/18.
 */
@Component
public class MsInvoker {

    public enum RestAction{
         GET,
         POST ;
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Autowired
    RestTemplate restTemplate;

//    public <T> ServiceResponse<T> restForService(String url, RestAction action, ServiceRequest<?> req, Type responseType){
//        String result;
//        Gson gSon = new Gson();
//        HttpHeaders headers =new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity request=new HttpEntity(req, headers);
//        Object t = restTemplate.postForObject(url, request, String.class);
//        result = gSon.toJson(t).replace("\\", "");
//        return gSon.fromJson(result.substring(1, result.length() - 1), responseType);
//    }
//
//    public <T> T getForObject(String url, Type responseType, Object... urlVariables) throws RestClientException{
//        Gson gSon = new Gson();
//        Object t = urlVariables == null ?  restTemplate.getForObject(url, String.class) : restTemplate.getForObject(url, String.class, urlVariables);
//        String result = gSon.toJson(t).replace("\\", "");
//        result = result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1);
//        return gSon.fromJson(result, responseType);
//    }

//    public <T, L> ServiceResponse<T> post(String url, ServiceRequest<L> req, TypeToken<ServiceResponse<T>> responseType) {
//        return postService(url, req, responseType.getType());
//    }
//
//    public <T> ServiceResponse<T> get(String url, TypeToken<ServiceResponse<T>> responseType, Object... urlVariables) throws RestClientException{
//        return getForObject(url, responseType.getType(), urlVariables);
//    }
//
//    public <T, L> ServiceResponse<T> postService(String url, ServiceRequest<L> req, Type responseType){
//        return restForService(url, RestAction.POST,req,responseType);
//    }

    public void setRestTemplate(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public <T> ServiceResponse<T> exchange(String url, HttpMethod method, HttpEntity entity, ParameterizedTypeReference<ServiceResponse<T>> responseType, Boolean ingoreRespCode, Object... urlVariables){
        ResponseEntity<ServiceResponse<T>> resp = restTemplate.exchange(url, method, entity, responseType, urlVariables);
        ServiceResponse serviceResponse = resp.getBody();
        if(!ingoreRespCode)
            if(serviceResponse.getCode() != 0)
                throw new WrappedException(serviceResponse.getCode(), serviceResponse.getMessage());
        return resp.getBody();
    }

    public <T> ServiceResponse<T> get(String url, ParameterizedTypeReference<ServiceResponse<T>> responseType, Object... urlVariables){
        return exchange(url, HttpMethod.GET, HttpEntity.EMPTY, responseType, false, urlVariables);
    }

    public <T, L> ServiceResponse<T> put(String url, L req, ParameterizedTypeReference<ServiceResponse<T>> responseType){
        return exchange(url, HttpMethod.PUT, new HttpEntity(req), responseType, false);
    }

    public <T, L> ServiceResponse<T> post(String url, L req, ParameterizedTypeReference<ServiceResponse<T>> responseType){
        return exchange(url, HttpMethod.POST, new HttpEntity(req), responseType, false);
    }

    public <T, L> ServiceResponse<T> delete(String url, L req, ParameterizedTypeReference<ServiceResponse<T>> responseType){
        return exchange(url, HttpMethod.DELETE, new HttpEntity(req), responseType, false);
    }

    public <T> ServiceResponse<T> getHandleCodeSelf(String url, ParameterizedTypeReference<ServiceResponse<T>> responseType, Object... urlVariables){
        return exchange(url, HttpMethod.GET, HttpEntity.EMPTY, responseType, true, urlVariables);
    }

    public <T, L> ServiceResponse<T> putHandleCodeSelf(String url, L req, ParameterizedTypeReference<ServiceResponse<T>> responseType){
        return exchange(url, HttpMethod.PUT, new HttpEntity(req), responseType, true);
    }

    public <T, L> ServiceResponse<T> postHandleCodeSelf(String url, L req, ParameterizedTypeReference<ServiceResponse<T>> responseType){
        return exchange(url, HttpMethod.POST, new HttpEntity(req), responseType, true);
    }

    public <T, L> ServiceResponse<T> deleteHandleCodeSelf(String url, L req, ParameterizedTypeReference<ServiceResponse<T>> responseType){
        return exchange(url, HttpMethod.DELETE, new HttpEntity(req), responseType, true);
    }

    public <T> T exchangeNative(String url, HttpMethod method, HttpEntity entity, ParameterizedTypeReference<T> responseType, Object... urlVariables){
        ResponseEntity<T> resp = restTemplate.exchange(url, method, entity, responseType, urlVariables);
        return resp.getBody();
    }

    public <T> T getNative(String url, ParameterizedTypeReference<T> responseType, Object... urlVariables){
        return exchangeNative(url, HttpMethod.GET, HttpEntity.EMPTY, responseType, urlVariables);
    }

    public <T, L> T putNative(String url, L req, ParameterizedTypeReference<T> responseType){
        return exchangeNative(url, HttpMethod.PUT, new HttpEntity(req), responseType);
    }

    public <T, L> T postNative(String url, L req, ParameterizedTypeReference<T> responseType){
        return exchangeNative(url, HttpMethod.POST, new HttpEntity(req), responseType);
    }

    public <T, L> T deleteNative(String url, L req, ParameterizedTypeReference<T> responseType){
        return exchangeNative(url, HttpMethod.DELETE, new HttpEntity(req), responseType);
    }

}
