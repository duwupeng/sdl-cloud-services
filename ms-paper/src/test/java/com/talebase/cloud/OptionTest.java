package com.talebase.cloud;

import com.google.gson.Gson;
import com.talebase.cloud.base.ms.paper.dto.*;
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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bin.yang on 2016-12-7.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class OptionTest {
    private int port = 28007;

    @Before
    public void before() {
    }

    @Test
    public void add() {
        String servicePath = "http://localhost:" + port + "/question/papers";
        List<DOption> list = new ArrayList<>();
        DOption dOption = new DOption();
        List<DOptionItem> list1 = new ArrayList<>();

        DOptionItem dOptionItem = new DOptionItem();
        dOptionItem.setMaskCode("1");
        dOptionItem.setLabel("饿死");
        list1.add(dOptionItem);

        DOptionItem dOptionItem1 = new DOptionItem();
        dOptionItem1.setMaskCode("2");
        dOptionItem1.setLabel("溺死");
        list1.add(dOptionItem1);

        DOptionItem dOptionItem2 = new DOptionItem();
        dOptionItem2.setMaskCode("3");
        dOptionItem2.setLabel("虐死");
        list1.add(dOptionItem2);

        DOptionItem dOptionItem3 = new DOptionItem();
        dOptionItem3.setMaskCode("3");
        dOptionItem3.setLabel("爽死");
        list1.add(dOptionItem3);

        List<DOptionScoreDetail> dOptionScoreDetails = new ArrayList<>();
        DOptionScoreDetail dOptionScoreDetail = new DOptionScoreDetail();
        dOptionScoreDetail.setMaskCode(dOptionItem.getMaskCode());
        BigDecimal bigDecimal = new BigDecimal(5);
        BigDecimal bigDecimal2 = new BigDecimal(2);
//        dOption.setScore(bigDecimal);
        dOptionScoreDetail.setScore(bigDecimal2);
        dOptionScoreDetails.add(dOptionScoreDetail);

        DOptionScoreDetail dOptionScoreDetail1 = new DOptionScoreDetail();
        dOptionScoreDetail1.setMaskCode(dOptionItem1.getMaskCode());
        BigDecimal bigDecimal3 = new BigDecimal(1);
        dOptionScoreDetail1.setScore(bigDecimal3);
        dOptionScoreDetails.add(dOptionScoreDetail1);

//        dOption.setQuestion("请选择你的死法(多选题)");
//        dOption.setOptions(gson.toJson(list1));
//        dOption.setAnswer(gson.toJson(dOptionScoreDetails));
        BigDecimal bigDecimal4 = new BigDecimal(3);
//        dOption.setScore(bigDecimal4);
//        dOption.setScoreRule(1);
//        dOption.setType(0);
//        dOption.setNo("2");
//        dOption.setVersion(1);
        list.add(dOption);

//        List<BlankScoreDetail> list2 = new ArrayList<>();
//        DOption dOption1 = new DOption();
//        BlankScoreDetail optionScoreDetail4 = new BlankScoreDetail();
//        optionScoreDetail4.setSeqNo("1");
//        optionScoreDetail4.setAnswer("daorong is pig");
//        optionScoreDetail4.setScore(1);
//        list2.add(optionScoreDetail4);
//
//        dOption1.setQuestion("word文档表达了_?_意思？");
//        dOption1.setAnswer(gson.toJson(list2));
//        dOption1.setType(1);
//        dOption1.setNo("2");
//        dOption1.setVersion(1);
//        list.add(dOption1);
//
//        List<BlankScoreDetail> list3 = new ArrayList<>();
//        DOption dOption2 = new DOption();
//        BlankScoreDetail optionScoreDetail5 = new BlankScoreDetail();
//        optionScoreDetail5.setSeqNo("1");
//        optionScoreDetail5.setAnswer("kanghong is pig");
//        optionScoreDetail5.setScore(1);
//        list3.add(optionScoreDetail5);
//        dOption2.setQuestion("excel文档表达了_?_意思？");
//        dOption2.setAnswer(gson.toJson(list3));
//        dOption2.setType(1);
//        dOption2.setNo("3");
//        dOption2.setVersion(1);
//        list.add(dOption2);

        ServiceHeader serviceHeader = new ServiceHeader();
        serviceHeader.setCompanyId(1);
        serviceHeader.setOperatorName("admin");
        ServiceRequest<List<DOption>> req = new ServiceRequest(serviceHeader, list);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse response = msInvoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<String>>() {});


    }
    @Test
    public void query() {
        String servicePath = "http://localhost:" + port + "/question/options";

        List list = new ArrayList<>();
//        DPageDetail dPageDetail = new DPageDetail();
//        dPageDetail.setSubjectNo("1");
//        dPageDetail.setSubjectVersion(1);
//        list.add(dPageDetail);
//
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
        ServiceRequest<List<DOption>> req = new ServiceRequest(serviceHeader, list);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse<List<DOption>> response = msInvoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<List<DOption>>>() {});
        System.out.println("dadasdasdasdasd");
    }

    @Test
    public void queryById() {
        String servicePath = "http://localhost:" + port + "/question/option/1";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse<DOption> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<DOption>>() {});
        System.out.println("dadasdasdasdasd");
    }

    @Test
    public void update() {
        String servicePath = "http://localhost:" + port + "/question/option";

        DOption dOption = new DOption();
        dOption.setId(1);
//        dOption.setQuestion("请选择你的死法(单选题)");
        ServiceHeader serviceHeader = new ServiceHeader();
        serviceHeader.setCompanyId(1);
        serviceHeader.setOperatorName("admin");
        ServiceRequest<DOption> req = new ServiceRequest(serviceHeader, dOption);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse response = msInvoker.put(servicePath,req, new ParameterizedTypeReference<ServiceResponse<String>>() {});
        System.out.println("dadasdasdasdasd");
    }

}
