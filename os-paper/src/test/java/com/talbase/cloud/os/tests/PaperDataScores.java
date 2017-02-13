package com.talbase.cloud.os.tests;

import com.google.gson.Gson;
import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric.du on 2016-12-23.
 */
public class PaperDataScores {
    @Test
    public void genPages(){
        List dWPageResponse  = new ArrayList();

        DOption dOption = new DOption();
        dOption.setUnicode("optionUniCode");
        dOption.setType(4);

        DOptionScoreSetting dOptionScoreSetting = new DOptionScoreSetting();
        dOptionScoreSetting.setScore(BigDecimal.valueOf(10));
        dOptionScoreSetting.setScoreRule(1);

        List<DOptionScoreDetail>  dOptionScoreDetails= new ArrayList<>();
        DOptionScoreDetail  dOptionScoreDetail = new DOptionScoreDetail();
        dOptionScoreDetail.setMaskCode("optionMaskCode1");
        dOptionScoreDetail.setScore(BigDecimal.valueOf(2));
        dOptionScoreDetails.add(dOptionScoreDetail);
        dOptionScoreDetail = new DOptionScoreDetail();
        dOptionScoreDetail.setMaskCode("optionMaskCode2");
        dOptionScoreDetail.setScore(BigDecimal.valueOf(2));
        dOptionScoreDetails.add(dOptionScoreDetail);

//        dOptionScoreSetting.setOptionScoreDetails(dOptionScoreDetails);
        dOption.setdOptionScoreSetting(dOptionScoreSetting);
        dWPageResponse.add(dOption);

        DBlank  dBlank = new DBlank();
        dBlank.setType(5);
        dBlank.setUnicode("dBlankUnicode");

        DBlankScoreSetting dBlankScoreSetting = new DBlankScoreSetting();
        dBlankScoreSetting.setScore(BigDecimal.valueOf(5));
        dBlankScoreSetting.setScoreRule(0);
        dBlankScoreSetting.setExplanation("题目解析");
        List<DBlankScoreDetail> dBlankScoreDetails = new ArrayList();

        DBlankScoreDetail dBlankScoreDetail = new DBlankScoreDetail();
        dBlankScoreDetail.setAnswer("填空答案一");
        dBlankScoreDetail.setScore(BigDecimal.valueOf(1));
        dBlankScoreDetail.setSeqNo(1);
        dBlankScoreDetails.add(dBlankScoreDetail);

        dBlankScoreDetail = new DBlankScoreDetail();
        dBlankScoreDetail.setAnswer("填空答案二");
        dBlankScoreDetail.setScore(BigDecimal.valueOf(1));
        dBlankScoreDetail.setSeqNo(2);
        dBlankScoreDetails.add(dBlankScoreDetail);


        dBlankScoreSetting.setdBlankScoreDetails(dBlankScoreDetails);
        dBlank.setdBlankScoreSetting(dBlankScoreSetting);

        dWPageResponse.add(dBlank);


        DAttachment dAttachment = new DAttachment();
        dAttachment.setType(6);
        DAttachmentScoreSetting dAttachmentScoreSetting = new DAttachmentScoreSetting();
        dAttachmentScoreSetting.setScore(BigDecimal.valueOf(5));
        dAttachment.setdAttachmentScoreSetting(dAttachmentScoreSetting);
        dAttachment.setUnicode("dAttachmentUnicode");
        dAttachmentScoreSetting.setScoreRule("得分标准");
        dWPageResponse.add(dAttachment);

        ServiceRequest sp = new ServiceRequest();
        sp.setRequest(dWPageResponse);
        System.out.println(GsonUtil.toJson(sp));
    }
}
