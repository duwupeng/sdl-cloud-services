package com.talebase.cloud.ms.admin.test;

import com.talebase.cloud.base.ms.admin.dto.*;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.CallerFrom;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-11-25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class RoleControllerTest {

    private int port = 28000;
    private String servicePathPre = "/serviceAdmin";
    private RestTemplate restTemplate = new RestTemplate();

    private ServiceHeader requestHeader;

    @Before
    public void before(){
//        INSERT INTO `adminDb`.`t_company` (`id`, `name`, `short_name`, `address`, `logo`, `domain`, `status`, `web_site`, `post_code`) VALUES ('1', '测试公司', '测试', '1', '1', '1', '1', '1', '1');
//        INSERT INTO `adminDb`.`t_permission` (`name`, `code`, `type`) VALUES ('权限1', 'p1', '1');
//        INSERT INTO `adminDb`.`t_permission` (`name`, `code`, `type`) VALUES ('权限2', 'p2', '2');
//        INSERT INTO `adminDb`.`t_permission` (`name`, `code`, `type`) VALUES ('权限3｀', 'p3', '1');
//        INSERT INTO `adminDb`.`t_permission` (`name`, `code`, `type`) VALUES ('权限4', 'P4', '3');
//        INSERT INTO `adminDb`.`t_role` (`name`, `status`, `created_date`, `creater`, `modified_date`, `modifier`, `company_id`) VALUES ('超级管理员', '1', '2016-01-01', 'talebase', '2016-01-01', 'talebase', '1');
//        INSERT INTO `adminDb`.`t_role` (`name`, `status`, `created_date`, `creater`, `modified_date`, `modifier`, `company_id`) VALUES ('咨询', '1', '2016-01-01', 'talebase', '2016-01-01', 'talebase', '1');
//        INSERT INTO `adminDb`.`t_role` (`name`, `status`, `created_date`, `creater`, `modified_date`, `modifier`, `company_id`) VALUES ('考官', '1', '2016-01-01', 'talebase', '2016-01-01', 'talebase', '1');
//        INSERT INTO `adminDb`.`t_role_permission` (`role_id`, `permission_id`) VALUES ('1', '1');
//        INSERT INTO `adminDb`.`t_role_permission` (`role_id`, `permission_id`) VALUES ('1', '2');
//        INSERT INTO `adminDb`.`t_role_permission` (`role_id`, `permission_id`) VALUES ('2', '1');
//        INSERT INTO `adminDb`.`t_role_permission` (`role_id`, `permission_id`) VALUES ('3', '1');

        requestHeader = new ServiceHeader();
        requestHeader.operatorId = 1;
        requestHeader.operatorName = "zhaokh";
        requestHeader.callerFrom = CallerFrom.web;
        requestHeader.callerIP = "127.0.0.1";
        requestHeader.orgCode = "1_";
        requestHeader.companyId = 1;
        requestHeader.transactionId = "123456789";
    }

    @Test
    public void test_创建角色() {
//        String servicePath = "http://localhost:" + port + servicePathPre + "/" + 10; //这种也行
        String servicePath = "http://localhost:" + port + servicePathPre + "/role";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse response = invoker.post(servicePath, new ServiceRequest<String>(requestHeader, "newRole1"), new ParameterizedTypeReference<ServiceResponse<String>>(){});

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_查看角色() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/roles/query";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<List<DRole>> response = invoker.post(servicePath, new ServiceRequest<Integer>(requestHeader, 1), new ParameterizedTypeReference<ServiceResponse<List<DRole>>>(){});

        assert response != null && response.getResponse().size() > 0;
    }

//    INSERT INTO `admindb`.`t_role` (`id`, `name`, `status`, `created_date`, `creater`, `modified_date`, `modifier`, `company_id`) VALUES ('4', 'role1', '1', '2016-01-01', '1', '2016-01-01', '1', '1');
//    INSERT INTO `admindb`.`t_role` (`id`, `name`, `status`, `created_date`, `creater`, `modified_date`, `modifier`, `company_id`) VALUES ('5', 'role2', '1', '2016-01-01', '1', '2016-01-01', '1', '1');
//    INSERT INTO `admindb`.`t_role` (`id`, `name`, `status`, `created_date`, `creater`, `modified_date`, `modifier`, `company_id`) VALUES ('6', 'role3', '1', '2016-01-01', '1', '2016-01-01', '1', '1');
    @Test
    public void test_删除角色() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/roles/delete";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse response = invoker.delete(servicePath, new ServiceRequest<List<Integer>>(requestHeader, Arrays.asList(4, 6)), new ParameterizedTypeReference<ServiceResponse<List<String>>>(){});

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_修改角色名称(){
        int roleId = 4;
        String servicePath = "http://localhost:" + port + servicePathPre + "/role/name/" + roleId;

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);


        ServiceResponse response = invoker.put(servicePath, new ServiceRequest<String>(requestHeader, "newRole3"), new ParameterizedTypeReference<ServiceResponse<String>>(){});

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_修改角色权限(){
        int roleId = 7;

        String servicePath = "http://localhost:" + port + servicePathPre + "/role/permissions/" + roleId;

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);


        ServiceResponse<List<DPermissionOfRole>> response= invoker.post(servicePath,  new ServiceRequest<List<Integer>>(requestHeader, Arrays.asList(3)), new ParameterizedTypeReference<ServiceResponse<List<DPermissionOfRole>>>(){});

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_查看角色权限(){
        String servicePath = "http://localhost:" + port + servicePathPre + "/role/permissionOfRole/{roleId}";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<List<DPermissionOfRole>> response = invoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<List<DPermissionOfRole>>>(){}, 1);

        assert response != null && response.getCode() == 0;
    }

}
