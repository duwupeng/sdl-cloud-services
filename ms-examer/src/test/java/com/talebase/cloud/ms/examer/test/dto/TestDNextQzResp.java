package com.talebase.cloud.ms.examer.test.dto;

import com.talebase.cloud.base.ms.examer.dto.DNextQzResp;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;

/**
 * Created by kanghong.zhao on 2017-1-18.
 */
public class TestDNextQzResp {

    public static void main(String[] args){
        DNextQzResp resp = new DNextQzResp();
        resp.setTaskId(1);
        resp.setPaperId(1);
        resp.setUserId(1);
        resp.setDone(1);
        resp.setScore("1");
        resp.setScoreCreater("创建人");
        resp.setTotal(1);
        resp.setUserExamId(1);
        resp.setSerialNo(1);

        System.out.println(GsonUtil.toJson(new ServiceResponse<>(resp)));
    }




}
