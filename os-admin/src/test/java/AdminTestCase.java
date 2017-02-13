import com.google.gson.Gson;
import com.talebase.cloud.base.ms.admin.dto.DAdmin;
import com.talebase.cloud.base.ms.admin.dto.DPageSearchData;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by daorong.li on 2016-11-24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class AdminTestCase {


   // @Test
    public void Test_Add(){
        String servicePath = "http://localhost:27002/add";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        DAdmin tAdmin = new DAdmin();
        tAdmin.setAccount("1");
        tAdmin.setCompanyId(1);
        tAdmin.setCreater("1");
        tAdmin.setEmail("1");
        tAdmin.setName("1");
        tAdmin.setOrgCode("1");
        tAdmin.setPassword("1");
        tAdmin.setStatus(1);

        ServiceRequest<DAdmin> req = new ServiceRequest<DAdmin>();
        req.setRequest(tAdmin);

        //ServiceResponse<DAdmin> response = msInvoker.post(servicePath,req, new TypeToken<ServiceResponse<DAdmin>>(){});

    }

   // @Test
    public void Test_GetAdmins(){
        String servicePath = "http://localhost:27002/admins";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);

        DPageSearchData data = new DPageSearchData();
        data.setStatus(1);
        data.setSearchType(DPageSearchData.SEARCH_ACCOUNT);
        data.setKey("1");

        PageRequest pageReq = new PageRequest();
        pageReq.setStart(1);
        pageReq.setLimit(5);

        ServiceRequest<DPageSearchData> req = new ServiceRequest<DPageSearchData>();
        req.setRequest(data);
        req.setPageReq(pageReq);
        ServiceResponse<DPageSearchData> response = msInvoker.post(servicePath,req,new ParameterizedTypeReference<ServiceResponse<DPageSearchData>>(){});
        //ServiceResponse<DPageSearchData> response = msInvoker.get(servicePath, new TypeToken<ServiceResponse<DPageSearchData>>(){},req);
    }
    @Test
    public void gsonTest(){
        String jsonStr ="[{'fieldkey':'field1','fie':'darong'},{'key':'field2','username':'daorong2'},{'key':'field3','username':'daorong3'}]";
        List<Map<String,Object>> map = GsonUtil.fromJson(jsonStr,new ArrayList<Map<String,Object>>().getClass());

    }
}
