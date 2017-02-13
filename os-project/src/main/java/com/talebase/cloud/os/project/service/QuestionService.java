package com.talebase.cloud.os.project.service;

import com.talebase.cloud.base.ms.admin.domain.TAdmin;
import com.talebase.cloud.base.ms.paper.domain.TPaper;
import com.talebase.cloud.base.ms.paper.dto.DPaper;
import com.talebase.cloud.base.ms.question.dto.DPaperUpgradeItem;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-5.
 */
@Service
public class QuestionService {

    final static String SERVICE_NAME = "ms-paper";

    @Autowired
    private MsInvoker msInvoker;

    //// TODO: 2016-12-5  
    public List<TPaper> checkPaperHasNewVersion(List<String> unicodes){
        if(unicodes.isEmpty())
            return new ArrayList<>();

        String servicePath = "http://" + SERVICE_NAME + "/question/paper/byUnicode";
        ServiceResponse<List<TPaper>> response = msInvoker.post(servicePath, new ServiceRequest(ServiceHeaderUtil.getRequestHeader(), unicodes), new ParameterizedTypeReference<ServiceResponse<List<TPaper>>>(){});
        return response.getResponse();
    }

    public List<TPaper> findPapers(List<Integer> paperIds){
        if(paperIds.isEmpty())
            return new ArrayList<>();

        String servicePath = "http://" + SERVICE_NAME + "/question/papers/query";
        ServiceResponse<List<TPaper>> response = msInvoker.post(servicePath, new ServiceRequest(ServiceHeaderUtil.getRequestHeader(), paperIds), new ParameterizedTypeReference<ServiceResponse<List<TPaper>>>(){});
        return response.getResponse();
    }

    public void usePaper(List<Integer> paperIds){
        if(paperIds.isEmpty())
            return;

        String servicePath = "http://" + SERVICE_NAME + "/question/paper/times";
        msInvoker.postHandleCodeSelf(servicePath, new ServiceRequest(ServiceHeaderUtil.getRequestHeader(), paperIds), new ParameterizedTypeReference<ServiceResponse<String>>(){});
    }

    public void unUsePaper(List<Integer> paperIds){
        if(paperIds.isEmpty())
            return;

        String servicePath = "http://" + SERVICE_NAME + "/question/paper/minusTimes";
        msInvoker.postHandleCodeSelf(servicePath, new ServiceRequest(ServiceHeaderUtil.getRequestHeader(), paperIds), new ParameterizedTypeReference<ServiceResponse<String>>(){});
    }

    public PageResponse<DPaper> queryPaperByPage(List<Integer> paperIds, PageRequest pageRequest){
        if(paperIds == null)
            paperIds = new ArrayList<>();

        String servicePath = "http://" + SERVICE_NAME + "/question/papers/byIds";
        ServiceResponse<PageResponse<DPaper>> response = msInvoker.post(servicePath, new ServiceRequest(ServiceHeaderUtil.getRequestHeader(), paperIds, pageRequest), new ParameterizedTypeReference<ServiceResponse<PageResponse<DPaper>>>(){});
        return response.getResponse();
    }
}
