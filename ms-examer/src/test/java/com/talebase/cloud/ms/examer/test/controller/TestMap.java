package com.talebase.cloud.ms.examer.test.controller;

import com.google.gson.Gson;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.protocal.ServiceResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kanghong.zhao on 2016-12-13.
 */
public class TestMap {


    public static void main(String[] args){
        //Map map = new HashMap<>();
       // map.put("name", "1234");
       // System.out.println(new Gson().toJson(map));
        TestMap testMap = new TestMap();
        System.out.println(testMap.getCode(2001100,"1231231zhong23").getMessage());
    }

    public BizEnums getCode(int code ,String message){
        return BizEnums.getCustomize(code,message);
    }
}
