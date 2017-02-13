package com.talebase.cloud.ms.common.service;

import com.talebase.cloud.base.ms.common.domain.TCode;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.ms.common.dao.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by daorong.li on 2016-12-1.
 */
@Service
public class BaseCodeService {

    @Autowired
    private CommonMapper commonMapper;

    public ServiceResponse<List<TCode>> getCodeByType(String type){
        List<TCode> list = commonMapper.getCodeByType(type);
        ServiceResponse<List<TCode>> response = new ServiceResponse<List<TCode>>();
        response.setResponse(list);
        return response;

    }
}
