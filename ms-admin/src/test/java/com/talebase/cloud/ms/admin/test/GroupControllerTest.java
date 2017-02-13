package com.talebase.cloud.ms.admin.test;

import com.talebase.cloud.base.ms.admin.domain.TGroup;
import com.talebase.cloud.base.ms.admin.dto.DGroup;
import com.talebase.cloud.base.ms.admin.dto.DGroupCreateReq;
import com.talebase.cloud.base.ms.admin.dto.DGroupNameModifyReq;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.CallerFrom;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;
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
public class GroupControllerTest {

    private int port = 28000;
    private String servicePathPre = "/serviceAdmin";
    private RestTemplate restTemplate = new RestTemplate();

    private ServiceHeader requestHeader;

    @Before
    public void before(){
//        INSERT INTO `admindb`.`t_company` (`id`, `name`, `short_name`, `address`, `logo`, `domain`, `status`, `web_site`, `post_code`) VALUES ('1', '测试公司', '测试', '1', '1', '1', '1', '1', '1');
//        INSERT INTO `admindb`.`t_group` (`id`, `parent_id`, `name`, `status`, `created_date`, `creater`, `modified_date`, `modifier`, `org_code`, `company_id`) VALUES ('1', '0', '第一组', '1', '2016-01-01', '1', '2016-01-01', '1', '1_', '1');
//        INSERT INTO `admindb`.`t_group` (`id`, `parent_id`, `name`, `status`, `created_date`, `creater`, `modified_date`, `modifier`, `org_code`, `company_id`) VALUES ('2', '1', '第二组', '1', '2016-01-01', '1', '2016-01-01', '1', '1_2_', '1');
//        INSERT INTO `admindb`.`t_group` (`id`, `parent_id`, `name`, `status`, `created_date`, `creater`, `modified_date`, `modifier`, `org_code`, `company_id`) VALUES ('3', '2', 'Team3', '1', '2016-01-01', '1', '2016-01-01', '1', '1_2_3_', '1');
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
    public void test_创建分组() {
//        String servicePath = "http://localhost:" + port + servicePathPre + "/" + 10; //这种也行
        String servicePath = "http://localhost:" + port + servicePathPre + "/group";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        DGroupCreateReq createReq = new DGroupCreateReq();
        createReq.setName("newTeam6");
        createReq.setParentId(6);

        ServiceResponse response = invoker.post(servicePath, new ServiceRequest<DGroupCreateReq>(requestHeader, createReq), new ParameterizedTypeReference<ServiceResponse<String>>(){});

        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_查看分组() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/groups/query";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<List<DGroup>> response = invoker.post(servicePath, new ServiceRequest<Integer>(requestHeader, 1), new ParameterizedTypeReference<ServiceResponse<List<DGroup>>>(){});
        assert response != null && response.getResponse().size() > 0;
    }

    @Test
    public void test_删除分组() {
        String servicePath = "http://localhost:" + port + servicePathPre + "/groups/delete";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse<String> response = invoker.delete(servicePath, new ServiceRequest<List<Integer>>(requestHeader, Arrays.asList(4)), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_修改分组名称(){
        String servicePath = "http://localhost:" + port + servicePathPre + "/group/name/11";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse response = invoker.put(servicePath, new ServiceRequest<String>(requestHeader,"newTeam3"), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        assert response != null && response.getCode() == 0;
    }

    @Test
    public void test_获取分组信息(){
        String servicePath = "http://localhost:" + port + servicePathPre + "/group/{groupId}";

        MsInvoker invoker = new MsInvoker();
        invoker.setRestTemplate(restTemplate);

        ServiceResponse response = invoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<TGroup>>(){}, 1);

        System.out.println(GsonUtil.toJson(response));
        assert response != null && response.getCode() == 0;
    }

}
