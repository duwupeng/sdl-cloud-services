package com.talbase.cloud.os.tests;

import com.google.gson.Gson;
import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by eric.du on 2016-12-23.
 */
public class PaperDatas {
    @Test
    public void genPages(){
        DWPaper2 pages = new DWPaper2();
        DWPage  dWPageResponse  = new DWPage();

        DPageStyleSetting  dPageStyleSetting = new DPageStyleSetting();
        dPageStyleSetting.setSubjectOrder(1);
        dPageStyleSetting.setOptionOrder(1);

        dWPageResponse.setdPageStyleSetting(dPageStyleSetting);
//        dWPageResponse.setdBlankStyleSetting(dBlankStyleSetting);

//        DOptionStyleSetting dOptionStyleSetting = new DOptionStyleSetting();
//        dOptionStyleSetting.setOptionSetting(1);
//        dWPageResponse.setdOptionStyleSetting(dOptionStyleSetting);

        DPage dPage = new DPage();
        dPage.setUnicode("pageUnicode");
        dPage.setType(2);
        dWPageResponse.setItem(dPage);

        DOption dOption = new DOption();
        dOption.setUnicode("optionUniCode");
        dOption.setType(4);

        DOptionStemSetting dOptionStemSetting = new DOptionStemSetting();
//        dOptionStemSetting.setType(1);
        dOptionStemSetting.setQuestion("选择题");
        List<DOptionItem> dOptionItems = new ArrayList<>();
        DOptionItem  dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项一");
        dOptionItem.setMaskCode("maskCodeOne");
        dOptionItems.add(dOptionItem);
        dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项二");
        dOptionItem.setMaskCode("maskCodeTwo");
        dOptionItems.add(dOptionItem);
        dOptionStemSetting.setOptions(dOptionItems);

        dOption.setdOptionStemSetting(dOptionStemSetting);

        dWPageResponse.setItem(dOption);

        DBlank  dBlank = new DBlank();
        dBlank.setType(5);
        dBlank.setUnicode("dBlankUnicode");
        DBlankStemSetting dBlankStemSetting = new DBlankStemSetting();
        dBlankStemSetting.setNumbers(3);
        dBlankStemSetting.setQuestion("填空");
        dBlankStemSetting.setType(1);
        dBlank.setdBlankStemSetting(dBlankStemSetting);

        DBlankStyleSetting dBlankStyleSetting = new DBlankStyleSetting();
        dBlankStyleSetting.setHeight(50);
        dBlankStyleSetting.setLimit("10");
        dBlankStyleSetting.setWidth(51);
        dBlank.setdBlankStyleSetting(dBlankStyleSetting);

        dWPageResponse.setItem(dBlank);


        DInstruction dInstruction = new DInstruction();
        dInstruction.setUnicode("dInstructionUnicode");
        dInstruction.setType(3);
        dInstruction.setComment("说明");
        dInstruction.setFilePath("filePath");
        dWPageResponse.setItem(dInstruction);

        DAttachment dAttachment = new DAttachment();
        dAttachment.setType(6);
        dAttachment.setUnicode("dAttachmentUnicode");
        DAttachmentStemSetting dAttachmentStemSetting = new DAttachmentStemSetting();
        dAttachmentStemSetting.setQuestion("附件题目");


        dAttachment.setdAttachmentStemSetting(dAttachmentStemSetting);

        dWPageResponse.setItem(dAttachment);
        pages.addPage(dWPageResponse);

        ServiceResponse sp = new ServiceResponse(pages);
        sp.setMessage("create");
        System.out.println(GsonUtil.toJson(sp));
    }


    @Test
    public void delete() {
        LinkedHashMap map = new LinkedHashMap();
        map.put("2", "3LinkedHashMap");
        map.put("3", "3LinkedHashMap");
        map.put("10", "10LinkedHashMap");
        map.put("11", "11LinkedHashMap");
        map.put("110", "110LinkedHashMap");
        map.put("111", "110LinkedHashMap");
        map.put("112", "110LinkedHashMap");
        System.out.println("最后一个"+getSeq(2,-1,map));
        System.out.println("第一个"+getSeq(112,1,map));
        System.out.println("下一个"+getSeq(10,1,map));
        System.out.println("上一个"+getSeq(10,-1,map));


    }

    public int getSeq( int seqNo,  int previousOrNext, LinkedHashMap<String, Map> seqToItem) {
        if (previousOrNext == 0){
            return  seqNo;
        }

        Integer seqCircle =  getSeqOfCircle(seqNo,previousOrNext,seqToItem);
        if(seqCircle==null){
            Iterator it = seqToItem.keySet().iterator();
            int previousSeq=0;
            int seq = 0;
            while (it.hasNext()){
                //seq = (Integer)it.next();
                seq = Integer.valueOf(it.next().toString());
                if(previousOrNext==-1){//上一步
                    if(seq<seqNo) {
                        previousSeq =seq;
                    }else if(seq==seqNo){
                        break;
                    }
                }else if(previousOrNext==1){//下一步
                    if(seq>seqNo){
                        break;
                    }
                }
            }

            if(previousOrNext==-1){
                return previousSeq;
            }else{
                return seq;
            }
        }else{
            return seqCircle;
        }
    }
    public Integer getSeqOfCircle( int seqNo,  int previousOrNext, LinkedHashMap<String, Map > seqToItem) {
        Set<String> set= seqToItem.keySet();
        Object[] obj = set.toArray();
        int minSeq =  Integer.valueOf((String) obj[0]);
        int maxSeq =  Integer.valueOf((String) obj[set.size()-1]);
        if(seqNo==minSeq && previousOrNext==-1 ){
            return maxSeq;
        }
        if(seqNo==maxSeq && previousOrNext==1 ){
            return minSeq;
        }
        return null;
    }
}
