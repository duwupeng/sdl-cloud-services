package com.talebase.cloud.ms.examer.test.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.talebase.cloud.base.ms.examer.dto.DImportReq;
import com.talebase.cloud.base.ms.examer.dto.DTUserImportLogEx;
import com.talebase.cloud.base.ms.examer.dto.DUserImportRecord;
import com.talebase.cloud.base.ms.examer.dto.DUserImportRecordQueryReq;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.util.GsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class UserImportLogControllerTest {

    private int port = 28008;
    private String servicePathPre = "";
    private RestTemplate restTemplate = new RestTemplate();

    private ServiceHeader requestHeader;

    @Before
    public void before(){

//        INSERT INTO `examerDb`.`t_user_show_field` (`field_name`, `field_key`, `isshow`, `ismandatory`, `isunique`, `isextension`, `sortnum`, `company_id`, `create_date`, `type`, `modify_date`) VALUES ('账号', 'account', '1', '1', '1', '0', '1', '2', '2016-12-09 21:18:19', '1', '2016-12-09 21:18:19');
//          INSERT INTO `examerDb`.`t_user_show_field` (`field_name`, `field_key`, `isshow`, `ismandatory`, `isunique`, `isextension`, `sortnum`, `company_id`, `create_date`, `type`, `modify_date`) VALUES ('密码', 'password', '1', '1', '0', '0', '2', '2', '2016-12-09 21:18:19', '1', '2016-12-09 21:18:19');
//        INSERT INTO `examerDb`.`t_user_show_field` (`field_name`, `field_key`, `isshow`, `ismandatory`, `isunique`, `isextension`, `sortnum`, `company_id`, `create_date`, `type`, `modify_date`) VALUES ('出生日期', 'birthday', '1', '0', '0', '0', '3', '2', '2016-12-09 21:18:19', '2', '2016-12-09 21:18:19');
//        INSERT INTO `examerDb`.`t_user_show_field` (`field_name`, `field_key`, `isshow`, `ismandatory`, `isunique`, `isextension`, `sortnum`, `company_id`, `create_date`, `type`, `modify_date`) VALUES ('学历', 'learn', '1', '0', '0', '1', '4', '2', '2016-12-09 21:18:19', '3', '2016-12-09 21:18:19');
//        INSERT INTO `examerDb`.`t_user_show_field` (`field_name`, `field_key`, `isshow`, `ismandatory`, `isunique`, `task_id`, `project_id`, `isextension`, `sortnum`, `company_id`, `create_date`, `type`, `modify_date`) VALUES ('账号', 'account', '1', '1', '1', '2', '2', '0', '1', '2', '2016-12-09 21:18:19', '1', '2016-12-09 21:18:19');
//        INSERT INTO `examerDb`.`t_user_show_field` (`field_name`, `field_key`, `isshow`, `ismandatory`, `isunique`, `task_id`, `project_id`, `isextension`, `sortnum`, `company_id`, `create_date`, `type`, `modify_date`) VALUES ('密码', 'password', '1', '1', '0', '2', '2', '0', '2', '2', '2016-12-09 21:18:19', '1', '2016-12-09 21:18:19');
//        INSERT INTO `examerDb`.`t_user_show_field` (`field_name`, `field_key`, `isshow`, `ismandatory`, `isunique`, `task_id`, `project_id`, `isextension`, `sortnum`, `company_id`, `create_date`, `type`, `modify_date`) VALUES ('出生日期', 'birthday', '1', '0', '0', '2', '2', '0', '3', '2', '2016-12-09 21:18:19', '2', '2016-12-09 21:18:19');
//        INSERT INTO `examerDb`.`t_user_show_field` (`field_name`, `field_key`, `isshow`, `ismandatory`, `isunique`, `task_id`, `project_id`, `isextension`, `sortnum`, `company_id`, `create_date`, `type`, `select_value`, `modify_date`) VALUES ('学历', 'learn', '1', '0', '0', '2', '2', '1', '4', '2', '2016-12-09 21:18:19', '3', '初中,高中,本科', '2016-12-09 21:18:19');
//        INSERT INTO `examerDb`.`t_user_show_field` (`field_name`, `field_key`, `isshow`, `ismandatory`, `isunique`, `isextension`, `sortnum`, `company_id`, `create_date`, `type`, `modify_date`) VALUES ('姓名', 'name', '1', '0', '0', '0', '5', '2', '2016-12-09 21:18:19', '1', '2016-12-09 21:18:19');
//        INSERT INTO `examerDb`.`t_user_show_field` (`field_name`, `field_key`, `isshow`, `task_id`, `project_id`, `isextension`, `sortnum`, `company_id`, `create_date`, `type`, `modify_date`) VALUES ('姓名', 'name', '1', '2', '2', '0', '5', '2', '2016-12-09 21:18:19', '1', '2016-12-09 21:18:19');
//        INSERT INTO `examerDb`.`t_user_show_field` (`field_name`, `field_key`, `isshow`, `ismandatory`, `isunique`, `task_id`, `project_id`, `isextension`, `sortnum`, `company_id`, `create_date`, `type`, `modify_date`) VALUES ('邮箱', 'email', '1', '0', '0', '2', '2', '0', '6', '1', '2016-12-09 21:18:19', '1', '2016-12-09 21:18:19');
//        INSERT INTO `examerDb`.`t_user_show_field` (`field_name`, `field_key`, `isshow`, `ismandatory`, `isunique`, `isextension`, `sortnum`, `company_id`, `create_date`, `type`, `modify_date`) VALUES ('邮箱', 'email', '1', '0', '0', '0', '6', '1', '2016-12-09 21:18:19', '1', '2016-12-09 21:18:19');
//        INSERT INTO `examerDb`.`t_user_show_field` (`id`, `field_name`, `field_key`, `isshow`, `ismandatory`, `isunique`, `isextension`, `sortnum`, `company_id`, `create_date`, `type`, `modify_date`) VALUES ('35', '手机号码', 'mobile', '1', '1', '1', '0', '7', '1', '2016-12-09 21:18:19', '1', '2016-12-09 21:18:19');
//        INSERT INTO `examerDb`.`t_user_show_field` (`field_name`, `field_key`, `isshow`, `ismandatory`, `isunique`, `task_id`, `project_id`, `isextension`, `sortnum`, `company_id`, `create_date`, `type`, `modify_date`) VALUES ('手机号码', 'mobile', '1', '1', '1', '2', '2', '0', '7', '1', '2016-12-09 21:18:19', '1', '2016-12-09 21:18:19');

        requestHeader = new ServiceHeader();
        requestHeader.operatorId = 1;
        requestHeader.operatorName = "zhaokh";
//        requestHeader.operatorName = "account1";
        requestHeader.callerFrom = CallerFrom.web;
        requestHeader.callerIP = "127.0.0.1";
        requestHeader.orgCode = "1_";
//        requestHeader.orgCode = "1_2_3_4_";
        requestHeader.companyId = 1;

        requestHeader.transactionId = "123456789";
    }

    @Test
    public void test_分页查询导入日志() {
//        String servicePath = "http://localhost:" + port + servicePathPre + "/" + 10; //这种也行
        String servicePath = "http://localhost:" + port + servicePathPre + "/examer/importLog/query";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        DUserImportRecordQueryReq req = new DUserImportRecordQueryReq();
        req.setProjectId(1);
        req.setTaskId(1);

        PageRequest pageRequest = new PageRequest();
        pageRequest.setLimit(10);
        pageRequest.setPageIndex(1);

        ServiceResponse<PageResponse<DUserImportRecord>> response = invoker.post(servicePath, new ServiceRequest<DUserImportRecordQueryReq>(requestHeader, req, pageRequest), new ParameterizedTypeReference<ServiceResponse<PageResponse<DUserImportRecord>>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0;
    }
//
    @Test
    public void test_根据batchNo导出错误数据() {
//        String servicePath = "http://localhost:" + port + servicePathPre + "/" + 10; //这种也行
        String servicePath = "http://localhost:" + port + servicePathPre + "/examer/failLog/export";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<DTUserImportLogEx> response = invoker.post(servicePath, new ServiceRequest<String>(requestHeader, "1110"), new ParameterizedTypeReference<ServiceResponse<DTUserImportLogEx>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_导入数据(){

        String servicePath = "http://localhost:" + port + servicePathPre + "/examer/import";

        String requsstStr = "{\"request\":{\"projectId\":1,\"taskId\":2,\"header\":[\"账号\",\"邮件\",\"出生日期\",\"xxxx\",\"学历\"],\"details\":[[\"examer1\",\"409649485@qq.com\",\"1988-01-26\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-27\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-28\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-29\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-30\",\"\",\"高中\"]]},\"requestHeader\":{\"operatorId\":1,\"operatorName\":\"zhaokh\",\"customerId\":1,\"customerName\":\"zhaokh\",\"callerFrom\":\"web\",\"callerIP\":\"127.0.0.1\",\"transactionId\":\"123456789\",\"seqId\":0,\"permissions\":[\"Permission1\",\"Permission2\",\"Permission3\"],\"orgCode\":\"1_\",\"companyId\":1}}\n";
        ServiceRequest<DImportReq> serviceRequest = GsonUtil.fromJson(requsstStr, new TypeToken<ServiceRequest<DImportReq>>(){}.getType());

        DImportReq req = getReq();

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<List<String>> response = invoker.post(servicePath, new ServiceRequest<DImportReq>(requestHeader, req), new ParameterizedTypeReference<ServiceResponse<List<String>>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_导入数据_账号已存在(){

        String servicePath = "http://localhost:" + port + servicePathPre + "/examer/import";

        String requsstStr = "{\"request\":{\"projectId\":1,\"taskId\":2,\"header\":[\"账号\",\"邮件\",\"出生日期\",\"xxxx\",\"学历\"],\"details\":[[\"examer1\",\"409649485@qq.com\",\"1988-01-26\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-27\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-28\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-29\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-30\",\"\",\"高中\"]]},\"requestHeader\":{\"operatorId\":1,\"operatorName\":\"zhaokh\",\"customerId\":1,\"customerName\":\"zhaokh\",\"callerFrom\":\"web\",\"callerIP\":\"127.0.0.1\",\"transactionId\":\"123456789\",\"seqId\":0,\"permissions\":[\"Permission1\",\"Permission2\",\"Permission3\"],\"orgCode\":\"1_\",\"companyId\":1}}\n";
        ServiceRequest<DImportReq> serviceRequest = GsonUtil.fromJson(requsstStr, new TypeToken<ServiceRequest<DImportReq>>(){}.getType());

        DImportReq req = getReq();
        //账号相同但手机不同
        req.getDetails().add(Arrays.asList("examer" + 0, "1358040635" + 6, "40964948" + 6 + "@qq.com", "1988-01-26", "", "高中", ""));

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<List<String>> response = invoker.post(servicePath, new ServiceRequest<DImportReq>(requestHeader, req), new ParameterizedTypeReference<ServiceResponse<List<String>>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_导入数据_密码规则问题(){

        String servicePath = "http://localhost:" + port + servicePathPre + "/examer/import";

        String requsstStr = "{\"request\":{\"projectId\":1,\"taskId\":2,\"header\":[\"账号\",\"邮件\",\"出生日期\",\"xxxx\",\"学历\",\"密码\"],\"details\":[[\"examer1\",\"409649485@qq.com\",\"1988-01-26\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-27\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-28\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-29\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-30\",\"\",\"高中\"]]},\"requestHeader\":{\"operatorId\":1,\"operatorName\":\"zhaokh\",\"customerId\":1,\"customerName\":\"zhaokh\",\"callerFrom\":\"web\",\"callerIP\":\"127.0.0.1\",\"transactionId\":\"123456789\",\"seqId\":0,\"permissions\":[\"Permission1\",\"Permission2\",\"Permission3\"],\"orgCode\":\"1_\",\"companyId\":1}}\n";
        ServiceRequest<DImportReq> serviceRequest = GsonUtil.fromJson(requsstStr, new TypeToken<ServiceRequest<DImportReq>>(){}.getType());

        DImportReq req = getReq();
        //账号相同但手机不同
        req.getDetails().add(Arrays.asList("examer" + 0, "1358040635" + 6, "40964948" + 6 + "@qq.com", "1988-01-26", "", "高中", "123456"));

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<List<String>> response = invoker.post(servicePath, new ServiceRequest<DImportReq>(requestHeader, req), new ParameterizedTypeReference<ServiceResponse<List<String>>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_导入数据_非下拉选项(){

        String servicePath = "http://localhost:" + port + servicePathPre + "/examer/import";

        String requsstStr = "{\"request\":{\"projectId\":1,\"taskId\":2,\"header\":[\"账号\",\"邮件\",\"出生日期\",\"xxxx\",\"学历\"],\"details\":[[\"examer1\",\"409649485@qq.com\",\"1988-01-26\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-27\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-28\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-29\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-30\",\"\",\"高中\"]]},\"requestHeader\":{\"operatorId\":1,\"operatorName\":\"zhaokh\",\"customerId\":1,\"customerName\":\"zhaokh\",\"callerFrom\":\"web\",\"callerIP\":\"127.0.0.1\",\"transactionId\":\"123456789\",\"seqId\":0,\"permissions\":[\"Permission1\",\"Permission2\",\"Permission3\"],\"orgCode\":\"1_\",\"companyId\":1}}\n";
        ServiceRequest<DImportReq> serviceRequest = GsonUtil.fromJson(requsstStr, new TypeToken<ServiceRequest<DImportReq>>(){}.getType());

        DImportReq req = getReq();
        //账号相同但手机不同
        req.getDetails().set(0, Arrays.asList("examer" + 0, "1358040635" + 0, "40964948" + 0 + "@qq.com", "1988-01-26", "", "非下拉选项", ""));

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<List<String>> response = invoker.post(servicePath, new ServiceRequest<DImportReq>(requestHeader, req), new ParameterizedTypeReference<ServiceResponse<List<String>>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_导入数据_手机号码验证不通过(){

        String servicePath = "http://localhost:" + port + servicePathPre + "/examer/import";

        String requsstStr = "{\"request\":{\"projectId\":1,\"taskId\":2,\"header\":[\"账号\",\"邮件\",\"出生日期\",\"xxxx\",\"学历\"],\"details\":[[\"examer1\",\"409649485@qq.com\",\"1988-01-26\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-27\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-28\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-29\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-30\",\"\",\"高中\"]]},\"requestHeader\":{\"operatorId\":1,\"operatorName\":\"zhaokh\",\"customerId\":1,\"customerName\":\"zhaokh\",\"callerFrom\":\"web\",\"callerIP\":\"127.0.0.1\",\"transactionId\":\"123456789\",\"seqId\":0,\"permissions\":[\"Permission1\",\"Permission2\",\"Permission3\"],\"orgCode\":\"1_\",\"companyId\":1}}\n";
        ServiceRequest<DImportReq> serviceRequest = GsonUtil.fromJson(requsstStr, new TypeToken<ServiceRequest<DImportReq>>(){}.getType());

        DImportReq req = getReq();
        //账号相同但手机不同
        req.getDetails().set(0, Arrays.asList("examer" + 0, "135" + 0, "40964948" + 0 + "@qq.com", "1988-01-26", "", "高中", ""));

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<List<String>> response = invoker.post(servicePath, new ServiceRequest<DImportReq>(requestHeader, req), new ParameterizedTypeReference<ServiceResponse<List<String>>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_导入数据_日期验证不通过(){

        String servicePath = "http://localhost:" + port + servicePathPre + "/examer/import";

        String requsstStr = "{\"request\":{\"projectId\":1,\"taskId\":2,\"header\":[\"账号\",\"邮件\",\"出生日期\",\"xxxx\",\"学历\"],\"details\":[[\"examer1\",\"409649485@qq.com\",\"1988-01-26\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-27\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-28\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-29\",\"\",\"高中\"],[\"examer1\",\"409649485@qq.com\",\"1988-01-30\",\"\",\"高中\"]]},\"requestHeader\":{\"operatorId\":1,\"operatorName\":\"zhaokh\",\"customerId\":1,\"customerName\":\"zhaokh\",\"callerFrom\":\"web\",\"callerIP\":\"127.0.0.1\",\"transactionId\":\"123456789\",\"seqId\":0,\"permissions\":[\"Permission1\",\"Permission2\",\"Permission3\"],\"orgCode\":\"1_\",\"companyId\":1}}\n";
        ServiceRequest<DImportReq> serviceRequest = GsonUtil.fromJson(requsstStr, new TypeToken<ServiceRequest<DImportReq>>(){}.getType());

        DImportReq req = getReq();
        //账号相同但手机不同
        req.getDetails().set(0, Arrays.asList("examer" + 0, "1358040635" + 0, "40964948" + 0 + "@qq.com", "191-36", "", "高中", ""));

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<List<String>> response = invoker.post(servicePath, new ServiceRequest<DImportReq>(requestHeader, req), new ParameterizedTypeReference<ServiceResponse<List<String>>>(){});

        System.out.println(GsonUtil.toJson(response));

        assert response != null && response.getCode() == 0;
    }

    private DImportReq getReq(){
        DImportReq req = new DImportReq();
        req.setTaskId(2);
        req.setProjectId(2);

        req.setHeader(Arrays.asList("账号", "手机号码", "邮箱", "出生日期", "xxxx", "学历", "密码"));
        req.setDetails(new ArrayList<>());
        for(int i = 0; i < 5; i++){
            req.getDetails().add(Arrays.asList("examer" + i, "1358040635" + i, "40964948" + i + "@qq.com", "1988-01-26", "", "高中", ""));
        }

        return req;
    }

}
