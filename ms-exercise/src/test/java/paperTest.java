import com.talebase.cloud.base.ms.examer.dto.DAnswerScoreDetail;
import com.talebase.cloud.base.ms.examer.dto.DExerciseRequest;
import com.talebase.cloud.base.ms.examer.dto.DExerciseResponse;
import com.talebase.cloud.base.ms.examer.dto.DScoreDetail;
import com.talebase.cloud.base.ms.paper.dto.DOption;
import com.talebase.cloud.base.ms.paper.dto.DPaper;
import com.talebase.cloud.base.ms.paper.dto.DPaperQuery;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bin.yang on 2016-12-9.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class paperTest {
    private int port = 28008;

    @Before
    public void before() {
    }

    @Test
    public void getList() {
        String servicePath = "http://localhost:" + port + "/exam/exercise";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        DExerciseRequest dExerciseRequest = new DExerciseRequest();
        dExerciseRequest.setPaperId(2);
        dExerciseRequest.setTaskId(1);
        dExerciseRequest.setUserId(1);
        dExerciseRequest.setStartTime(simpleDateFormat.format(date));

//        List<DAnswerScoreDetail>  = new ArrayList<>();
//        DScoreDetail dScoreDetail = new DScoreDetail();
//        dScoreDetail.setExamerAnswer("鹅");
//        dScoreDetail.setExamerScore(new BigDecimal(0));
//        dScoreDetail.setSeqNo(1);
//        dScoreDetail.setStandwardAnswer("饿");
//        dScoreDetail.setStandwardScore(new BigDecimal(1));
//        dScoreDetail.setSubjectId(1);
//        dScoreDetail.setType(2);
//        dScoreDetails.add(dScoreDetail);

//        dExerciseRequest.setdAnswerScoreDetails(dScoreDetails);
        PageRequest pageRequest = new PageRequest();
        pageRequest.setLimit(10);
        pageRequest.setPageIndex(0);
        ServiceHeader serviceHeader = new ServiceHeader();
        serviceHeader.setCompanyId(1);
        serviceHeader.setOperatorName("admin");

        ServiceRequest<DExerciseRequest> req = new ServiceRequest(serviceHeader, dExerciseRequest, pageRequest);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse response = msInvoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<DExerciseResponse>>() {
        });
        System.out.println("通过了哟");
    }

    @Test
    public void updateScore() {
        String servicePath = "http://localhost:" + port + "/exam/exercise/1";
        ServiceRequest<BigDecimal> req = new ServiceRequest(new ServiceHeader(),new BigDecimal(50));
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse response = msInvoker.put(servicePath, req, new ParameterizedTypeReference<ServiceResponse<String>>() {
        });
        System.out.println("通过了哟");
    }

    @Test
    public void get() {
        String servicePath = "http://localhost:" + port + "/exam/exercise/1";

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<DExerciseResponse>>() {
        });
        System.out.println("通过了哟");
    }
}
