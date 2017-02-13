package com.talebase.cloud.ms.consumption.service;

import com.talebase.cloud.base.ms.consume.domain.TAccount;
import com.talebase.cloud.base.ms.consume.domain.TAccountLine;
import com.talebase.cloud.base.ms.consume.dto.DAccountCondition;
import com.talebase.cloud.base.ms.consume.dto.DAccountConsumeResult;
import com.talebase.cloud.base.ms.consume.dto.DAccountPayResult;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.DateUtil;
import com.talebase.cloud.ms.consumption.dao.AccountLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by suntree.xu on 2016-12-7.
 */
@Service
public class ConsumeService {

    @Autowired
    AccountLineMapper accountLineMapper;

    public PageResponse<DAccountConsumeResult> queryConsumes(DAccountCondition reqCondition, PageRequest pageRequest) {
        PageResponse<DAccountConsumeResult> response = new PageResponse<DAccountConsumeResult>();
        List<DAccountConsumeResult> results = new ArrayList<DAccountConsumeResult>();
        results = accountLineMapper.getAccountConsumes(reqCondition, pageRequest);
        int total = accountLineMapper.getAccountConsumeTotal(reqCondition);
        response.setTotal(total);
        response.setResults(results);
        return response;
    }

    public PageResponse<DAccountPayResult> queryPays(String account, PageRequest pageRequest) {
        PageResponse<DAccountPayResult> response = new PageResponse<DAccountPayResult>();
        List<DAccountPayResult> results = accountLineMapper.getAccountpays(account, pageRequest);
        int total = accountLineMapper.getAccountPayTotal(account);
        response.setTotal(total);
        response.setResults(results);
        return response;
    }

    public ServiceResponse<List<DAccountPayResult>> queryPaysTotal(ServiceRequest serviceRequest) {
        ServiceResponse<List<DAccountPayResult>> response = new ServiceResponse<List<DAccountPayResult>>();
        List<DAccountPayResult> results = accountLineMapper.getAccountPayTotalList(serviceRequest.getRequestHeader().getCompanyId());
        response.setResponse(results);
        return response;
    }

    public TAccount queryAccount(int company_id) {
        TAccount tAccount = accountLineMapper.getAccount(company_id);
        return tAccount;
    }

    public ServiceResponse opoperateConsume(Integer companyId, TAccountLine tAccountLine) {
        ServiceResponse response = new ServiceResponse();
        //类型为消费
        tAccountLine.setType(1);
        //判断逻辑
        TAccount tAccount = accountLineMapper.getAccount(companyId);
        tAccountLine.setCompanyId(companyId);
        if (tAccountLine.getSmsVar() == 0 && tAccountLine.getPointVar() == 0) {
            response.setCode(1);
            response.setBizError(true);
            response.setMessage("操作数额必须大于0");
        } else if (tAccount.getPointBalance() < tAccountLine.getPointVar() || tAccount.getSmsBalance() < tAccountLine.getSmsVar()) {
            response.setCode(1);
            response.setBizError(true);
            response.setMessage("余额不足");
        } else {
            //扣除余额并记录操作
            tAccount.setModifier(tAccountLine.getModifier());
//            tAccount.setModifiedDate(tAccountLine.getModifiedDate());
            tAccount.setPointBalance(tAccount.getPointBalance() - tAccountLine.getPointVar());
            tAccount.setSmsBalance(tAccount.getSmsBalance() - tAccountLine.getSmsVar());
            tAccount.setCompanyId(companyId);
            accountLineMapper.UpdateAccount(tAccount);
            saveAccountLine(tAccountLine);
        }
        if (tAccount.getPointBalance()<tAccount.getPointValid()||tAccount.getSmsBalance()<tAccount.getSmsValid()) {
            //扣除后余额少于阈值，发送MQ
        }
        return response;
    }

    /**
     * 充值
     * @param companyId
     * @param tAccountLine
     * @return
     */
    public ServiceResponse recharge(Integer companyId, TAccountLine tAccountLine) {
        ServiceResponse response = new ServiceResponse();
        //判断逻辑
        TAccount tAccount = new TAccount();
        tAccountLine.setCompanyId(companyId);
        if (tAccountLine.getSmsVar() == 0 && tAccountLine.getPointVar() == 0) {
            response.setCode(1);
            response.setBizError(true);
            response.setMessage("操作数额必须大于0");
        } else {
            tAccount.setModifier(tAccountLine.getModifier());
            tAccount.setPointBalance(tAccountLine.getPointVar());
            tAccount.setSmsBalance(tAccountLine.getSmsVar());
            tAccount.setCompanyId(companyId);
            accountLineMapper.UpdateAccount(tAccount);
            saveAccountLine(tAccountLine);
        }
        return response;
    }

    /**
     * 保存流水
     * @param tAccountLine
     */
    private void saveAccountLine(TAccountLine tAccountLine){
        if (tAccountLine.getSmsVar() > 0 && tAccountLine.getPointVar() > 0) {
            //拆分成两条记录存储
            int sms_var = tAccountLine.getSmsVar();
            tAccountLine.setSmsVar(0);
            accountLineMapper.operateConsume(tAccountLine);
            tAccountLine.setSmsVar(sms_var);
            tAccountLine.setPointVar(0);
            accountLineMapper.operateConsume(tAccountLine);
        }else{
            accountLineMapper.operateConsume(tAccountLine);
        }
    }
    public ServiceResponse<List<DAccountConsumeResult>> queryConsumesList(DAccountCondition reqCondition, ServiceRequest serviceRequest) {
        ServiceResponse<List<DAccountConsumeResult>> response = new ServiceResponse<List<DAccountConsumeResult>>();
        List<DAccountConsumeResult> results = accountLineMapper.getAccountConsumeTotalList(reqCondition, serviceRequest.getRequestHeader().getCompanyId());
        response.setResponse(results);
        return response;
    }

    public ServiceResponse getTips(int companyId){
        ServiceResponse response = new ServiceResponse();
        TAccount tAccount = accountLineMapper.getAccount(companyId);
        String tips = "";
        Date now = new Date();
        Date date60after = DateUtil.addDay(now,60);
        if(date60after.after(tAccount.getCompanyValidate())){
            tips += "您的平台服务时间还有60天过期，请尽快联系客服续期<br>";
        }
        if(tAccount.getPointBalance()<tAccount.getPointValid()){
            tips += "你的T币点数已不足"+tAccount.getPointValid()+"点，请尽快联系客服充值<br>";
        }
        if(tAccount.getSmsBalance()<tAccount.getSmsValid()){
            tips += "你的短信剩余量已不足"+tAccount.getSmsValid()+"条，请尽快联系客服充值<br>";
        }

        response.setResponse(tips);
        return  response;
    }
}
