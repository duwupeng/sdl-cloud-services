

import com.talebase.cloud.base.ms.admin.domain.TAdmin;
import com.talebase.cloud.base.ms.admin.dto.*;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by daorong.li on 2016-11-24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class AdminTestCase {
    private int port = 28000;

    //修改管理员测试
    @Test
    public void Test_Update(){
        String servicePath = "http://localhost:"+port+"/serviceAdmin/update";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        DAdmin tAdmin = new DAdmin();
       // tAdmin.setCompanyId(1);
        //tAdmin.setCreater("1");
        tAdmin.setModifier("2");
        tAdmin.setEmail("2");
        tAdmin.setName("updateadmin4");
       //tAdmin.setOrgCode("1_2_3_");
       // tAdmin.setPassword("aa");
        tAdmin.setStatus(1);
        tAdmin.setGroupId(27);
        tAdmin.setRoleId(1);
        tAdmin.setId(57);
        tAdmin.setCreatedDate(new Timestamp(new Date().getTime()));
        tAdmin.setModifiedDate(new Timestamp(new Date().getTime()));

        ServiceRequest<DAdmin> req = new ServiceRequest<DAdmin>();
        req.setRequest(tAdmin);

        ServiceResponse<DAdmin> response = msInvoker.post(servicePath,req,new ParameterizedTypeReference<ServiceResponse<DAdmin>>(){});
        System.out.println("======================="+response.getCode());
    }

    //修改管理员密码
    @Test
    public void Test_Password(){
        String servicePath = "http://localhost:"+port+"/serviceAdmin/reSetPassword";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        DAdmin tAdmin = new DAdmin();
        tAdmin.setPassword("ssssssssssssss2");
        tAdmin.setModifier("li");
        tAdmin.setIds("5,6");
        tAdmin.setModifiedDate(new Timestamp(new Date().getTime()));

        ServiceRequest<DAdmin> req = new ServiceRequest<DAdmin>();
        req.setRequest(tAdmin);

        ServiceResponse<DAdmin> response = msInvoker.post(servicePath,req,new ParameterizedTypeReference<ServiceResponse<DAdmin>>(){});
        System.out.println("======================="+response.getCode());
    }

    //修改管理员状态测试
    @Test
    public void Test_Status(){
        String servicePath = "http://localhost:"+port+"/serviceAdmin/setStatus";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        DAdmin tAdmin = new DAdmin();
        tAdmin.setModifier("li");
        tAdmin.setStatus(1);
        tAdmin.setIds("5,6");
        tAdmin.setModifiedDate(new Timestamp(new Date().getTime()));

        ServiceRequest<DAdmin> req = new ServiceRequest<DAdmin>();
        req.setRequest(tAdmin);

        ServiceResponse<DAdmin> response = msInvoker.post(servicePath,req,new ParameterizedTypeReference<ServiceResponse<DAdmin>>(){});
        System.out.println("======================="+response.getCode());
    }

    //删除管理员测试
    @Test
    public void Test_Del(){
        String servicePath = "http://localhost:"+port+"/serviceAdmin/del";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        DAdmin tAdmin = new DAdmin();
        tAdmin.setModifier("li");
        tAdmin.setId(5);
        tAdmin.setCreatedDate(new Timestamp(new Date().getTime()));
        tAdmin.setModifiedDate(new Timestamp(new Date().getTime()));

        ServiceRequest<DAdmin> req = new ServiceRequest<DAdmin>();
        req.setRequest(tAdmin);

        ServiceResponse<DAdmin> response = msInvoker.post(servicePath,req,new ParameterizedTypeReference<ServiceResponse<DAdmin>>(){});
        System.out.println("======================="+response.getCode());
    }

    //根据id获取管理员信息
    @Test
    public void Test_GetById(){
        String servicePath = "http://localhost:" + port + "/serviceAdmin/getAdmin/2";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);

        ServiceResponse<TAdmin> response = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<TAdmin>>(){});
        System.out.println("response" + response.getResponse());
    }

    //根据账号列表获取管理员列表
    @Test
    public void Test_GetAdminsByAccounts(){
        String servicePath = "http://localhost:" + port + "/serviceAdmin/getAdminsByAccounts";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);

        ServiceRequest<List<String>> req= new ServiceRequest<List<String>>();
        List<String>  data = new ArrayList<>();
        data.add("a");
        data.add("b");
        req.setRequest(data);
        ServiceHeader serviceHeader = new ServiceHeader();
        serviceHeader.setCompanyId(1);
        req.setRequestHeader(serviceHeader);

        ServiceResponse<List<TAdmin>>  response = msInvoker.post(servicePath,req,new ParameterizedTypeReference<ServiceResponse<List<TAdmin>>>(){});
        System.out.println("response" + response.getResponse());
    }

    //根据账号和公司ID获取管理员信息测试
    @Test
    public void Test_GetByAccountAndCompanyId(){
        String servicePath = "http://localhost:" + port + "/serviceAdmin/getByAccountAndCompanyId";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);

        DAdmin data = new DAdmin();
        data.setStatus(1);
        data.setCompanyId(1);
        data.setAccount("22222212");

        ServiceRequest<DAdmin> req = new ServiceRequest<DAdmin>();
        req.setRequest(data);

        ServiceResponse<TAdmin> response = msInvoker.post(servicePath,req,new ParameterizedTypeReference<ServiceResponse<TAdmin>>(){});
        System.out.println(response.getResponse());
    }

    //添加管理员测试
    @Test
    public void Test_Add(){
        String servicePath = "http://localhost:"+port+"/serviceAdmin/add";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        DAdmin tAdmin = new DAdmin();
        tAdmin.setAccount("add50");
        tAdmin.setCompanyId(1);
        tAdmin.setCreater("1");
        tAdmin.setEmail("1");
        tAdmin.setName("addadmin50");
        //tAdmin.setOrgCode("1_");
        tAdmin.setPassword("aa");
        tAdmin.setGroupId(28);
        tAdmin.setStatus(1);
        tAdmin.setRoleId(1);
        tAdmin.setMobile("131");
        tAdmin.setCreatedDate(new Timestamp(new Date().getTime()));

        ServiceRequest<DAdmin> req = new ServiceRequest<DAdmin>();
        req.setRequest(tAdmin);

        ServiceResponse<DAdmin> response = msInvoker.post(servicePath,req,new ParameterizedTypeReference<ServiceResponse<DAdmin>>(){});
        System.out.println("======================="+response.getCode());
    }

    //获取下级管理员
    @Test
    public void Test_GetSubordinate() {
        String servicePath = "http://localhost:" + port + "/serviceAdmin/subordinate?companyId={companyId}&orgCode={orgCode}";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);

        ServiceResponse<List<DSubordinate>> response= msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<List<DSubordinate>>>(){}, 1, "1_");
        System.out.println("response" + response.getResponse());
    }

    //获取分组与角色列表
    @Test
    public void Test_GetAdminsCompanyId(){
        String servicePath = "http://localhost:" + port + "/serviceAdmin/getAdmins/1";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);

        ServiceRequest<String> req= new ServiceRequest<String>();
        String data = "1_2_";
        req.setRequest(data);

        ServiceResponse<DGroupAndRole>  response = msInvoker.post(servicePath,req,new ParameterizedTypeReference<ServiceResponse<DGroupAndRole>>(){});
        System.out.println("response" + response.getResponse());
    }

    //获取管理员信息
    @Test
    public void Test_Edit(){
        String servicePath = "http://localhost:" + port + "/serviceAdmin/edit/1";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);

        ServiceResponse<DAdmin>  response = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<DAdmin>>(){});
        System.out.println("response" + response.getResponse());
    }

    //获取管理员列表测试
    @Test
    public void Test_GetAdmins(){
        String servicePath = "http://localhost:"+port+"/serviceAdmin/getAdmins";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);

        DPageSearchData data = new DPageSearchData();
        data.setStatus(1);
        data.setSearchType(DPageSearchData.SEARCH_NAME);
        data.setKey("管理员");

        PageRequest pageReq = new PageRequest();
        pageReq.setStart(1);
        pageReq.setLimit(20);

        ServiceRequest<DPageSearchData> req = new ServiceRequest<DPageSearchData>();
        req.setRequest(data);
        req.setPageReq(pageReq);
        ServiceHeader serviceHeader = new ServiceHeader();
        serviceHeader.setOrgCode("");
        req.setRequestHeader(serviceHeader);

        //ServiceResponse<DPageSearchData> response = msInvoker.get(servicePath, new TypeToken<ServiceResponse<DPageSearchData>>(){},req);
        ServiceResponse<PageResponse> response = msInvoker.post(servicePath,req,new ParameterizedTypeReference<ServiceResponse<PageResponse>>(){});
        System.out.println(response.getCode());
    }

    //获取管理员导出数据
    @Test
    public void Test_GetExportData() {
        String servicePath = "http://localhost:" + port + "/serviceAdmin/getExportData/1?orgCode={orgCode}";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);

        ServiceResponse<List<DUploadFileData>> response= msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<List<DUploadFileData>>>(){},"2_");
        System.out.println("response" + response.getResponse());
    }

    //获取考官
    @Test
    public void Test_GetExaminers() {
        String servicePath = "http://localhost:" + port + "/serviceAdmin/examiners?companyId={companyId}";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);

        ServiceResponse<List<DSubordinate>> response= msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<List<DSubordinate>>>(){}, 1);
        System.out.println("response" + response.getResponse());
    }

    //获取角色权限测试
    @Test
    public void Test_GetPermissionsByRoleId(){
        String servicePath = "http://localhost:"+port+"/serviceAdmin/permissions/role/1";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        //ServiceResponse<DPageSearchData> response = msInvoker.get(servicePath, new TypeToken<ServiceResponse<DPageSearchData>>(){},req);
        ServiceResponse<List<java.lang.String>> response = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<List<java.lang.String>>>(){});
        System.out.println("response size: " + response.getResponse().size());
    }

    @Test
    public void test_GroupLevelCul(){
        char h = '_';
        int i = 0;
        for(char c : "1_2_".toCharArray()){
            if(c == h)
                i++;
        }
        System.out.println(i);
    }
}
