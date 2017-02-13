package com.talebase.cloud.os.paper.service;

import com.google.common.reflect.TypeToken;
import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.base.ms.paper.enums.DItemType;
import com.talebase.cloud.base.ms.paper.enums.DPaperMode;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.BeanConverter;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
public class PaperService {
    @Autowired
    MsInvoker msInvoker;

    final static String SERVICE_NAME = "ms-paper";


    public ServiceResponse<DWPaper1> getHeader(Integer paperId) {
        DWPaper1 dWPaper = new DWPaper1();
        dWPaper.setComment("初级技术人员水平考试");
        dWPaper.setName("初级技术人员水平考试");
        dWPaper.setDuration(1);
        dWPaper.setId(paperId);
        dWPaper.setType(1);
        dWPaper.setUnicode("x8881821xxx");
        return new ServiceResponse(dWPaper);
    }

    /**
     * 保存卷首
     *
     * @param serviceRequest
     * @return
     */
    public Integer savePaperHeader(ServiceRequest<DPaper> serviceRequest) {
        String servicePath = "http://" + SERVICE_NAME + "/question/paper/header";
        ServiceResponse<Integer> response = msInvoker.post(servicePath, serviceRequest, new ParameterizedTypeReference<ServiceResponse<Integer>>() {
        });
        return response.getResponse();
    }

    /**
     * 刷新试卷
     *
     * @param hashKey
     * @return
     */
    public ServiceResponse flushPaper(String hashKey, int stepNo) {
        String servicePath = "http://" + SERVICE_NAME + "/question/paper/" + hashKey + "/" + stepNo;
        ServiceRequest<String> req = new ServiceRequest();
        req.setRequestHeader(ServiceHeaderUtil.getRequestHeader());
        ServiceResponse response = msInvoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<Integer>>() {
        });
        return response;
    }


    /**
     * 获得整个试卷
     *
     * @param paperId
     * @return
     */
    public ServiceResponse<DWPaperResponse> getPaper(int paperId) {
        //获得整个试卷
        String servicePath = "http://" + SERVICE_NAME + "/question/paper/" + paperId;
        ServiceResponse<DWPaperResponse> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<DWPaperResponse>>() {
        });
        DWPaperResponse dPaperResponse = response.getResponse();
        return new ServiceResponse<>(dPaperResponse);
    }

    public ServiceResponse queryList(DPaperQuery dPaperQuery, PageRequest pageRequest) throws InvocationTargetException, IllegalAccessException {
        String servicePath = "http://" + SERVICE_NAME + "/question/papers";
        ServiceResponse<PageResponse<DPaper>> response = msInvoker.post(servicePath, new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader(), dPaperQuery, pageRequest), new ParameterizedTypeReference<ServiceResponse<PageResponse<DPaper>>>() {
        });

        PageResponse<DPaper> paperPageResponse = response.getResponse();
        List<DWPaper> dwPaperList = new ArrayList<>();
        for (DPaper dPaper : paperPageResponse.getResults()) {
            DWPaper dwPaper = new DWPaper();
            BeanConverter.copyProperties(dwPaper, dPaper);
            dwPaper.setTotalScore(dPaper.getScore());
            dwPaperList.add(dwPaper);
        }
        PageResponse<DWPaper> paperPageResponse1 = new PageResponse<>();
        paperPageResponse1.setResults(dwPaperList);
        paperPageResponse1.setPageIndex(paperPageResponse.getPageIndex());
        paperPageResponse1.setLimit(paperPageResponse.getLimit());
        paperPageResponse1.setTotal(paperPageResponse.getTotal());
        paperPageResponse1.setStart(paperPageResponse.getStart());

        return new ServiceResponse(paperPageResponse1);
    }

    public ServiceResponse updateStatus(Integer paperId, boolean status) {
        String servicePath = "http://" + SERVICE_NAME + "/question/paper/" + paperId;
        ServiceResponse response = msInvoker.put(servicePath, new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader(), status), new ParameterizedTypeReference<ServiceResponse<String>>() {
        });
        return response;
    }

    public ServiceResponse updateMode(int id, DPaperMode dPageMode, boolean status) {
        String servicePath = "http://" + SERVICE_NAME + "/question/paper/mode/" + id + "/" + status;
        ServiceResponse response = msInvoker.put(servicePath, new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader(), dPageMode.getValue()), new ParameterizedTypeReference<ServiceResponse<String>>() {
        });
        return response;
    }

    public ServiceResponse delete(Integer id) {
        String servicePath = "http://" + SERVICE_NAME + "/question/paper/" + id;
        ServiceResponse response = msInvoker.delete(servicePath, new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader()), new ParameterizedTypeReference<ServiceResponse<String>>() {
        });
        return response;
    }

    /**
     * 复制试卷
     *
     * @return
     */
    public Integer copy(Integer paperId, String paperUnicode, String newName) {
        String servicePath = "http://" + SERVICE_NAME + "/question/paper/copy/" + paperId + "/" + paperUnicode;
        ServiceResponse<Integer> response = msInvoker.post(servicePath, new ServiceRequest(ServiceHeaderUtil.getRequestHeader(), newName), new ParameterizedTypeReference<ServiceResponse<Integer>>() {
        });
        return response.getResponse();
    }

    /**
     * 预览数据
     *
     * @param paperUnicode
     * @return
     */
    public DWPaper3 previewByDb(String paperUnicode, Integer pageNo) {
        String servicePath = "http://" + SERVICE_NAME + "/question/paper/preview/{paperUnicode}";
        ServiceResponse<DWPaper3> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<DWPaper3>>() {
        }, paperUnicode);
        DWPaper3 dwPaper3 = changeData(response.getResponse(), pageNo);
        return dwPaper3;
    }

    private DWPaper3 changeData(DWPaper3 dwPaper3, Integer pageNo) {
        List items = new ArrayList();
        List list = dwPaper3.getItems();
        DInstruction dInstruction;
        int page = 0;
        List<Map> pageList = new ArrayList<>();//每页有多少题[{"pageIndex":1,"itemSize":5},{"pageIndex":2,"itemSize":3}]
        Map map = new HashMap();
        Integer totalNumber = 0;//题序，从1累加
        int itemNo = 0;
        for (int i = 0; i < list.size(); i++) {
            dInstruction = new DInstruction();
            LinkedHashMap linkedHashMap = (LinkedHashMap) list.get(i);
            if (Integer.parseInt(linkedHashMap.get("type").toString()) == DItemType.PAGE.getValue()) {
                map = new HashMap();
                itemNo = 0;
                page++;
                map.put("pageIndex", page);
                pageList.add(map);
            } else if (Integer.parseInt(linkedHashMap.get("type").toString()) != DItemType.PAGE.getValue()
                    && Integer.parseInt(linkedHashMap.get("type").toString()) != DItemType.INSTRUCTION.getValue()) {
                totalNumber++;
                itemNo++;
                map.put("itemSize", itemNo);
            }
            if (page == pageNo) {
                if (Integer.parseInt(linkedHashMap.get("type").toString()) == DItemType.INSTRUCTION.getValue()) {
                    dInstruction = GsonUtil.fromJson(GsonUtil.toJson(linkedHashMap), DInstruction.class);
                    dInstruction.setType(DItemType.INSTRUCTION.getValue());
                }
                switch (Integer.parseInt(linkedHashMap.get("type").toString())) {
                    case 4:
                        DOption s_dOption = GsonUtil.fromJson(GsonUtil.toJson(linkedHashMap), DOption.class);
                        DOptionStem s_dOptionStem = new DOptionStem();
                        s_dOptionStem.setdOptionStyleSetting(s_dOption.getdOptionStyleSetting());
                        s_dOptionStem.setType(DItemType.SINGLE_CHOICE.getValue());
                        s_dOptionStem.setQuestion(s_dOption.getdOptionStemSetting().getQuestion());
                        s_dOptionStem.setOptions(s_dOption.getdOptionStemSetting().getOptions());
                        s_dOptionStem.setSeqNo(totalNumber);
                        s_dOptionStem.setdInstruction(dInstruction);
                        items.add(s_dOptionStem);
                        break;
                    case 5:
                        DOption m_dOption = GsonUtil.fromJson(GsonUtil.toJson(linkedHashMap), DOption.class);
                        DOptionStem m_dOptionStem = new DOptionStem();
                        m_dOptionStem.setdOptionStyleSetting(m_dOption.getdOptionStyleSetting());
                        m_dOptionStem.setQuestion(m_dOption.getdOptionStemSetting().getQuestion());
                        m_dOptionStem.setOptions(m_dOption.getdOptionStemSetting().getOptions());
                        m_dOptionStem.setSeqNo(totalNumber);
                        m_dOptionStem.setType(DItemType.MULTIPLE_CHOICE.getValue());
                        m_dOptionStem.setdInstruction(dInstruction);
                        items.add(m_dOptionStem);
                        break;
                    case 6:
                        DBlank dBlank = GsonUtil.fromJson(GsonUtil.toJson(linkedHashMap), DBlank.class);
                        DBlankStem dBlankStem = new DBlankStem();
                        dBlankStem.setQuestion(dBlank.getdBlankStemSetting().getQuestion());
                        dBlankStem.setNumbers(dBlank.getdBlankStemSetting().getNumbers());
                        dBlankStem.setBlankType(dBlank.getdBlankStemSetting().getType());
                        dBlankStem.setdBlankStyleSetting(dBlank.getdBlankStyleSetting());
                        dBlankStem.setSeqNo(totalNumber);
                        dBlankStem.setType(DItemType.BLANK.getValue());
                        dBlankStem.setdInstruction(dInstruction);
                        items.add(dBlankStem);
                        break;
                    case 7:
                        DAttachment dAttachment = GsonUtil.fromJson(GsonUtil.toJson(linkedHashMap), DAttachment.class);
                        DAttachmentStem dAttachmentStem = new DAttachmentStem();
                        dAttachmentStem.setQuestion(dAttachment.getdAttachmentStemSetting().getQuestion());
                        dAttachmentStem.setType(DItemType.ATTACHMENT.getValue());
                        dAttachmentStem.setSeqNo(totalNumber);
                        dAttachmentStem.setdInstruction(dInstruction);
                        items.add(dAttachmentStem);
                        break;
                    case 8:
                        DPaperRemark dPaperRemark = GsonUtil.fromJson(GsonUtil.toJson(linkedHashMap), DPaperRemark.class);
                        dPaperRemark.setType(DItemType.REMARK.getValue());
                        items.add(dPaperRemark);
                        break;
                    default:
                }
            }
        }
        DWPaper3 dwPaper = new DWPaper3();
        dwPaper.setItems(items);
        dwPaper.setName(dwPaper3.getName());
        dwPaper.setNumber(totalNumber);
        dwPaper.setTotalPage(page);
        dwPaper.setPageList(pageList);
        return dwPaper;
    }

    public ServiceResponse getSimplePaperById(Integer paperId) {
        String servicePath = "http://" + SERVICE_NAME + "/question/paper/simple/{id}";
        ServiceResponse response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<String>>() {
        }, paperId);
        return response;
    }

    /**
     * 检查试卷名称是否重复
     *
     * @param newName
     * @return
     */
    public ServiceResponse checkName(Integer paperId, String newName) {
        String servicePath = "http://" + SERVICE_NAME + "/question/paper/checkName/" + paperId;
        ServiceResponse response = msInvoker.post(servicePath, new ServiceRequest(ServiceHeaderUtil.getRequestHeader(), newName), new ParameterizedTypeReference<ServiceResponse<String>>() {
        });
        return response;
    }
}
