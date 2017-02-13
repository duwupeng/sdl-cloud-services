package com.talebase.cloud.ms.consumption.controller;

import com.talebase.cloud.base.ms.consume.domain.TAccount;
import com.talebase.cloud.base.ms.consume.domain.TAccountLine;
import com.talebase.cloud.base.ms.consume.dto.DAccountCondition;
import com.talebase.cloud.base.ms.consume.dto.DAccountConsumeResult;
import com.talebase.cloud.base.ms.consume.dto.DAccountPayResult;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.ms.consumption.service.ConsumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by suntree.xu on 2016-12-7.
 */
@RestController
public class ConsumeController {

    @Autowired
    ConsumeService consumeService;

    /**
     * 分页查询消费记录
     * @param serviceRequest
     * @return
     */
    @PostMapping(value = "/consume/getConsumesPage")
   public ServiceResponse<PageResponseWithParam> getConsumesPage(@RequestBody ServiceRequest<DAccountCondition> serviceRequest){
        PageResponse<DAccountConsumeResult> response = consumeService.queryConsumes(serviceRequest.getRequest(),serviceRequest.getPageReq());
        PageResponseWithParam resp = new PageResponseWithParam(serviceRequest.getPageReq(),serviceRequest.getRequest(),response.getResults(),response.getTotal());
        return new ServiceResponse(resp);
    }
   /* public ServiceResponse<List<DAccountConsumeResult>> getConsumesPage(@RequestBody ServiceRequest<DAccountCondition> serviceRequest){
        PageResponse<DAccountConsumeResult> response = consumeService.queryConsumes(serviceRequest.getRequest(),serviceRequest.getPageReq());
        //PageResponseWithParam resp = new PageResponseWithParam(serviceRequest.getPageReq(),serviceRequest.getRequest(),response.getResults(),response.getTotal());
        return new ServiceResponse(response.getResults());
    }*/

    /**
     * 查询相同companyId的全部消费记录，以作导出
     * @param serviceRequest
     * @return
     */
    @PostMapping(value = "/consume/getConsumesTotal")
    public ServiceResponse<List<DAccountConsumeResult>> getConsumesTotal(@RequestBody ServiceRequest<DAccountCondition> serviceRequest){
        ServiceResponse<List<DAccountConsumeResult>> response = consumeService.queryConsumesList(serviceRequest.getRequest(),serviceRequest);
        return response;
    }

    /**
     * 分页查询充值记录
     * @param account
     * @param serviceRequest
     * @return
     */
    @PostMapping(value = "/consume/getPays/{account}")
    public ServiceResponse<PageResponseWithParam> getPays(@RequestBody ServiceRequest serviceRequest, @PathVariable("account") String account){
        PageResponse<DAccountPayResult> response = consumeService.queryPays(account,serviceRequest.getPageReq());
        PageResponseWithParam resp = new PageResponseWithParam(serviceRequest.getPageReq(),account,response.getResults(),response.getTotal());
        return new ServiceResponse(resp);
    }

    /**
     * 查询全部充值记录，以作导出
     * @param serviceRequest
     * @param serviceRequest
     * @return
     */
    @PostMapping(value = "/consume/getPaysTotal")
    public ServiceResponse<List<DAccountPayResult>> getPaysTotal(@RequestBody ServiceRequest serviceRequest){
        ServiceResponse<List<DAccountPayResult>> response = consumeService.queryPaysTotal(serviceRequest);
        return response;
    }

    /**
     * 根据公司id查询账户余额
     * @param companyId
     * @return
     */
    @GetMapping(value = "/consume/queryAccount/{companyId}")
    public ServiceResponse<TAccount> qureyAccount(@PathVariable("companyId") int companyId){
        TAccount tAccount = consumeService.queryAccount(companyId);
        return new ServiceResponse(tAccount);
    }

    /**
     * 插入操作记录
     * @param serviceRequest
     * @return
     */
    @PutMapping(value = "/consume/operate")
    public ServiceResponse operateConsume(@RequestBody ServiceRequest<TAccountLine> serviceRequest){
        TAccountLine tAccountLine = serviceRequest.getRequest();
        Integer companyId = serviceRequest.getRequestHeader().getCompanyId();
        return consumeService.opoperateConsume(companyId,tAccountLine);
    }

    @GetMapping(value = "/consume/getTips/{companyId}")
    public ServiceResponse getTips(@PathVariable("companyId") int companyId){
        return consumeService.getTips(companyId);
    }


//    @GetMapping(value = "/consume/test")
//    public String operateConsume(Integer a){
////        TAccountLine tAccountLine = serviceRequest.getRequest();
////        return consumeService.opoperateConsume(tAccountLine);
//        return "123";
//    }

}
