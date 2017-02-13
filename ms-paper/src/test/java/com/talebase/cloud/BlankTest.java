package com.talebase.cloud;

import com.google.gson.Gson;
import com.talebase.cloud.base.ms.paper.dto.DBlankScoreDetail;
import com.talebase.cloud.base.ms.paper.dto.DBlank;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bin.yang on 2016-12-7.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class BlankTest {
    private int port = 28007;

    @Before
    public void before() {
    }

    @Test
    public void add() {
        String servicePath = "http://localhost:" + port + "/question/blank";
        List<DBlank> list = new ArrayList<>();
        DBlank dBlank = new DBlank();
        List<DBlankScoreDetail> list1 = new ArrayList<>();
        DBlankScoreDetail blankScoreDetail = new DBlankScoreDetail();
//        blankScoreDetail.setSeqNo("1");
        blankScoreDetail.setAnswer("鹅");
//        blankScoreDetail.setScore(1);
        list1.add(blankScoreDetail);

        DBlankScoreDetail blankScoreDetail1 = new DBlankScoreDetail();
//        blankScoreDetail1.setSeqNo("1");
        blankScoreDetail1.setAnswer("向天");
//        blankScoreDetail1.setScore(1);
        list1.add(blankScoreDetail1);

        DBlankScoreDetail blankScoreDetail2 = new DBlankScoreDetail();
//        blankScoreDetail2.setSeqNo("1");
        blankScoreDetail2.setAnswer("水");
//        blankScoreDetail2.setScore(1);
        list1.add(blankScoreDetail2);

        DBlankScoreDetail blankScoreDetail3 = new DBlankScoreDetail();
//        blankScoreDetail3.setSeqNo("1");
        blankScoreDetail3.setAnswer("红掌");
//        blankScoreDetail3.setScore(1);
        list1.add(blankScoreDetail3);

//        dBlank.setQuestion("鹅_?_鹅,曲项_?_歌,白毛湖绿_?_,_?_拨清波");
//        dBlank.setAnswer(gson.toJson(list1));
//        dBlank.setType(1);
//        dBlank.setNo("1");
//        dBlank.setVersion(1);
        list.add(dBlank);

        List<DBlankScoreDetail> list2 = new ArrayList<>();
        DBlank dBlank1 = new DBlank();
        DBlankScoreDetail blankScoreDetail4 = new DBlankScoreDetail();
//        blankScoreDetail4.setSeqNo("1");
        blankScoreDetail4.setAnswer("daorong is pig");
//        blankScoreDetail4.setScore(1);
        list2.add(blankScoreDetail4);

//        dBlank1.setQuestion("word文档表达了_?_意思？");
//        dBlank1.setAnswer(gson.toJson(list2));
//        dBlank1.setType(1);
//        dBlank1.setNo("2");
//        dBlank1.setVersion(1);
        list.add(dBlank1);

        List<DBlankScoreDetail> list3 = new ArrayList<>();
        DBlank dBlank2 = new DBlank();
        DBlankScoreDetail blankScoreDetail5 = new DBlankScoreDetail();
//        blankScoreDetail5.setSeqNo("1");
        blankScoreDetail5.setAnswer("kanghong is pig");
//        blankScoreDetail5.setScore(1);
        list3.add(blankScoreDetail5);
//        dBlank2.setQuestion("excel文档表达了_?_意思？");
//        dBlank2.setAnswer(gson.toJson(list3));
//        dBlank2.setType(1);
//        dBlank2.setNo("3");
//        dBlank2.setVersion(1);
        list.add(dBlank2);

        ServiceHeader serviceHeader = new ServiceHeader();
        serviceHeader.setCompanyId(1);
        serviceHeader.setOperatorName("admin");
        ServiceRequest<List<DBlank>> req = new ServiceRequest(serviceHeader, list);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse response = msInvoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<String>>() {});


    }
    @Test
    public void query() {
        String servicePath = "http://localhost:" + port + "/question/blanks";

        List list = new ArrayList<>();
//        DPageDetail dPageDetail = new DPageDetail();
//        dPageDetail.setSubjectNo("1");
//        dPageDetail.setSubjectVersion(1);
//        list.add(dPageDetail);

//        DPageDetail dPageDetail1 = new DPageDetail();
//        dPageDetail1.setSubjectNo("2");
//        dPageDetail1.setSubjectVersion(1);
//        list.add(dPageDetail1);
//
//        DPageDetail dPageDetail2 = new DPageDetail();
//        dPageDetail2.setSubjectNo("3");
//        dPageDetail2.setSubjectVersion(1);
//        list.add(dPageDetail2);


        ServiceHeader serviceHeader = new ServiceHeader();
        serviceHeader.setCompanyId(1);
        serviceHeader.setOperatorName("admin");
        ServiceRequest<List<DBlank>> req = new ServiceRequest(serviceHeader, list);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse<List<DBlank>> response = msInvoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<List<DBlank>>>() {});
        System.out.println("dadasdasdasdasd");
    }

    @Test
    public void queryById() {
        String servicePath = "http://localhost:" + port + "/question/blank/11";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse<DBlank> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<DBlank>>() {});
        System.out.println("dadasdasdasdasd");
    }

    @Test
    public void update() {
        String servicePath = "http://localhost:" + port + "/question/blank";

        DBlank dBlank = new DBlank();
        dBlank.setId(12);
//        dBlank.setQuestion("修改后：word文档表达了_?_意思？");
        ServiceHeader serviceHeader = new ServiceHeader();
        serviceHeader.setCompanyId(1);
        serviceHeader.setOperatorName("admin");
        ServiceRequest<DBlank> req = new ServiceRequest(serviceHeader, dBlank);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse response = msInvoker.put(servicePath,req, new ParameterizedTypeReference<ServiceResponse<String>>() {});
        System.out.println("dadasdasdasdasd");
    }

}
