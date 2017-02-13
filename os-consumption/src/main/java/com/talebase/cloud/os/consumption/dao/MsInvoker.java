package com.talebase.cloud.os.consumption.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by suntree.xu on 2016-12-9.
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

    public <T, L> T exchange(String url, HttpMethod method, HttpEntity entity, ParameterizedTypeReference<T> responseType, Object... urlVariables){
        ResponseEntity<T> resp = restTemplate.exchange(url, method, entity, responseType, urlVariables);
        return resp.getBody();
    }

    public <T> T get(String url, ParameterizedTypeReference<T> responseType, Object... urlVariables){
        return exchange(url, HttpMethod.GET, HttpEntity.EMPTY, responseType, urlVariables);
    }

    public <T, L> T put(String url, L req, ParameterizedTypeReference<T> responseType){
        return exchange(url, HttpMethod.PUT, new HttpEntity(req), responseType);
    }

    public <T, L> T post(String url, L req, ParameterizedTypeReference<T> responseType){
        return exchange(url, HttpMethod.POST, new HttpEntity(req), responseType);
    }

    public <T, L> T delete(String url, L req, ParameterizedTypeReference<T> responseType){
        return exchange(url, HttpMethod.DELETE, new HttpEntity(req), responseType);
    }

}