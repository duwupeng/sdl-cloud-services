package com.talebase.cloud.os.consumption.controller;

import com.talebase.cloud.base.ms.admin.dto.DGroupAndRole;
import com.talebase.cloud.base.ms.admin.enums.PermissionCode;
import com.talebase.cloud.base.ms.consume.domain.TAccount;
import com.talebase.cloud.base.ms.consume.domain.TAccountLine;
import com.talebase.cloud.base.ms.consume.dto.*;
import com.talebase.cloud.base.ms.project.dto.DProjectSelect;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.util.DateStyle;
import com.talebase.cloud.common.util.DateUtil;
import com.talebase.cloud.common.util.JxlsUtil;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.os.consumption.service.ConsumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by suntree.xu on 2016-12-8.
 */
@RestController
public class ConsumeCenterCotroller {

    @Autowired
    ConsumeService consumeService;

    /**
     * 导出消费记录
     * @param response
     * @param
     * @return
     */
    @GetMapping("/consumecenter/exportConsume")
    public ServiceResponse exportConsume(HttpServletRequest request, HttpServletResponse response){
        DAccountCondition dAccountCondition = new DAccountCondition();
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        dAccountCondition.setAccount(serviceHeader.getAccount());
        Map<String,Object> map = new HashMap<String,Object>();
        List<DAccountComsumeDisplay> resultList = new ArrayList<DAccountComsumeDisplay>();
        resultList = consumeService.getDAccountComsumeDisplayList(dAccountCondition);
        map.put("datas",resultList);
        try {
            InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream("/xls/static_export_consume_template.xls"));
            JxlsUtil.write(inputXML, response, map, "消费统计.xls", "sheet2!A1");
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ServiceResponse();
    }

    /**
     * 导出充值记录
     * @param response
     * @param serviceRequest
     * @return
     */
    @GetMapping("/consumecenter/exportPays")
    public ServiceResponse exportPays(HttpServletRequest request,HttpServletResponse response,ServiceRequest serviceRequest){
        Map<String,Object> map = new HashMap<String,Object>();
        List<DAccountPayResult> resultList = new ArrayList<DAccountPayResult>();
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        serviceRequest.setRequestHeader(serviceHeader);
        resultList = consumeService.getDAccountPayResultList(serviceRequest);
        List<DAccountPayResultDisplayExport> displayList = new ArrayList<DAccountPayResultDisplayExport>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        //转换
        for(DAccountPayResult dAccountPayResult :resultList){
            DAccountPayResultDisplayExport dAccountPayResultDisplay = new DAccountPayResultDisplayExport();
            dAccountPayResultDisplay.setAccount(dAccountPayResult.getAccount());
            dAccountPayResultDisplay.setModifiedDate(simpleDateFormat.format(dAccountPayResult.getModifiedDate()));
            if(dAccountPayResult.getPointVar()==0) {
                dAccountPayResultDisplay.setType("短信");
                dAccountPayResultDisplay.setVal(dAccountPayResult.getSmsVar());
            }else if(dAccountPayResult.getSmsVar()==0){
                dAccountPayResultDisplay.setType("T币数");
                dAccountPayResultDisplay.setVal(dAccountPayResult.getPointVar());
            }
            displayList.add(dAccountPayResultDisplay);
        }
        map.put("datas",displayList);
        try {
            InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream("/xls/static_export_pays_template.xls"));
            JxlsUtil.write(inputXML, response, map, "充值记录.xls", "sheet2!A1");
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ServiceResponse();
    }

    /**
     * 分页查询消费记录
     * @param reqcondition
     * @param pageRequest
     * @return
     */
    @PostMapping(value = "/consumecenter/getConsumes")
    public ServiceResponse<PageResponseWithParam<DAccountComsumeDisplay,DAccountCondition>> getConsumes(DAccountCondition reqcondition, PageRequest pageRequest){
        //获取当前操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        ServiceRequest request = new ServiceRequest();

        //获取当前用户操作权限
//        boolean canEdit  = serviceHeader.hasRight(PermissionCode.ADMIN_LIST_RIGHT.getCode());
//        if (!canEdit){
//            serviceHeader.setOrgCode("");
//        }
        request.setRequestHeader(serviceHeader);
        PageResponse<DAccountComsumeDisplay> response = consumeService.getDAccountComsumeDisplayPage(reqcondition,pageRequest);
        ServiceResponse<List<DProjectSelect>> proList = consumeService.getGroupAndRoleForSelect(request);
        TAccount tAccount = consumeService.queryAccount(serviceHeader.getCompanyId());
        reqcondition.setPointCount(tAccount.getPointBalance());
        reqcondition.setSmsCount(tAccount.getSmsBalance());
        reqcondition.setdProjectSelectList(proList.getResponse());
        PageResponseWithParam<DAccountComsumeDisplay,DAccountCondition> param = new PageResponseWithParam<DAccountComsumeDisplay,DAccountCondition>(pageRequest,reqcondition);
        param.setResults(response.getResults());
        param.setTotal(response.getTotal());

        //PageResponseWithParam<PageResponse,DAccountCondition> resp = new PageResponseWithParam(pageRequest,reqcondition,response.getResults(),response.getTotal());
        //设置当前用户操作权限
        Map<String,Boolean> orgCode = new HashMap<String,Boolean>();
//        orgCode.put("canEdit",canEdit);//判断用户是否有编辑权限

        ServiceResponse<PageResponseWithParam<DAccountComsumeDisplay,DAccountCondition>> pageResponseWithParam = new ServiceResponse<PageResponseWithParam<DAccountComsumeDisplay,DAccountCondition>>(param);
        pageResponseWithParam.setPermission(orgCode);
        return pageResponseWithParam;
    }

    /**
     * 分页查询充值记录
     * @param pageRequest
     * @return
     */
    @PostMapping(value = "/consumecenter/getPays")
    public ServiceResponse<PageResponse> getPays(PageRequest pageRequest,DAccountCondition dAccountCondition){

        PageResponse<DAccountPayResultDisplay> response = consumeService.queryPays(pageRequest,dAccountCondition);
        PageResponse resp = new PageResponse(pageRequest, response.getResults(),response.getTotal());
        return new ServiceResponse(resp);
    }

    /**
     * 根据公司id查询账户余额
     * @param companyId
     * @return
     */
    @GetMapping(value = "/consumecenter/qureyAccount/{companyId}")
    public ServiceResponse<TAccount> qureyAccount(@PathVariable("companyId") int companyId){
        TAccount tAccount = consumeService.queryAccount(companyId);
        return new ServiceResponse(tAccount);
    }

    /**
     * 插入操作记录
     * @return
     */
    @PutMapping(value = "/consumecenter/operate")
    public ServiceResponse operateConsume(DAccountLineOperateCondition dAccountLineOperateCondition){
        //DAccountLineOperateCondition dAccountLineOperateCondition = serviceRequest.getRequest();
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        TAccountLine tAccountLine = new TAccountLine();
        tAccountLine.setCompanyId(serviceHeader.getCompanyId());
        tAccountLine.setModifiedDate(new Date());
        tAccountLine.setModifier(serviceHeader.getOperatorName());
        tAccountLine.setPointVar(dAccountLineOperateCondition.getPointVar());
        tAccountLine.setSmsVar(dAccountLineOperateCondition.getSmsVar());
        tAccountLine.setProjectId(dAccountLineOperateCondition.getProjectId());
        tAccountLine.setTaskId(dAccountLineOperateCondition.getTaskId());
        tAccountLine.setType(1);
        return consumeService.opoperateConsume(tAccountLine);
    }

}
