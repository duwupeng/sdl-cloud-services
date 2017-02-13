package com.talebase.cloud.os.paper.controller;

import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.os.paper.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by eric.du on 2016-12-7.
 */
@RestController
public class PaperQueryController extends PaperCache {

    @Autowired
    PaperService paperService;

    /**
     * 查询试卷列表
     *
     * @return
     */
    @PostMapping(value = "/question/papers")
    public ServiceResponse<PageResponse<DWPaper>> paperList(DPaperQuery dPaperQuery, PageRequest pageRequest) throws InvocationTargetException, IllegalAccessException {
        ServiceResponse<PageResponse<DWPaper>> dwPaperPageResponse = paperService.queryList(dPaperQuery, pageRequest);
//        List<DWPaper> dWPapers = dwPaperPageResponse.getResponse().getResults();
//        for (DWPaper dWPaper: dWPapers ){
//            redisTemplate.opsForValue().set(dWPaper.getUnicode()+"-mode",dWPaper.getMode());
//        }
        return dwPaperPageResponse;
    }

    /**
     * 根据试卷paperUnitCode查询试卷头部
     *
     * @param paperUnitCode
     * @return
     */
    @GetMapping(value = "/question/paper/{paperUnitCode}")
    public ServiceResponse<DWPaper1> paperHeader(@PathVariable("paperUnitCode") String paperUnitCode) {
        String hashKey = selectKey(paperUnitCode);
        BoundHashOperations<String, String, DPaper> hashOperations = redisTemplate.boundHashOps(hashKey);
        DPaper dPaper = (DPaper) hashOperations.get(paperUnitCode + "-H-1");
        DWPaper1 dWPaper1 = new DWPaper1();
        dWPaper1.setUnicode(dPaper.getUnicode());
        dWPaper1.setComment(dPaper.getComment());
        dWPaper1.setType(dPaper.getType());
        dWPaper1.setName(dPaper.getName());
        dWPaper1.setDuration(dPaper.getDuration());
        return new ServiceResponse(dWPaper1);
    }

    /**
     * 设置题干查询
     * 试卷内容列表，列表中会有 页，说明，选择题，填空题，上传题
     *
     * @param paperUnitCode
     * @return
     */
    @GetMapping(value = "/question/stems/{paperUnitCode}")
    public ServiceResponse<DWPaper4> getPaperForStemSetting(@PathVariable("paperUnitCode") String paperUnitCode) {
        String hashKey = selectKey(paperUnitCode);
        BoundHashOperations<String, String, List<String>> hashOperations = redisTemplate.boundHashOps(hashKey);
        DPaper dPaper = (DPaper) hashOperations.get( paperUnitCode + "-H-1");
        String mode = (String) redisTemplate.opsForValue().get(paperUnitCode + "-mode");
        List<String> sequences = (List<String>) hashOperations.get("sequences");
        List items = new ArrayList();

        if (sequences != null) {
            String unicode;
            for (int i = 0; i < sequences.size(); i++) {
                unicode = sequences.get(i);
                if (unicode != null) {
                    if (unicode.indexOf("R") != -1) {
                        continue;
                    }
                    items.add(hashOperations.getOperations().opsForHash().get(hashKey, unicode));
                }
            }
        }
        DWPaper4 dwPaper4 = new DWPaper4();
        dwPaper4.setName(dPaper.getName());
        dwPaper4.setMode(mode.equals("新建中") ? 1 : mode.equals("修改中") ? 2 : 3);
        dwPaper4.setItems(items);
        return new ServiceResponse(dwPaper4);
    }

    /**
     * 设置分数查询
     * 试卷内容， 包含了分页符说明，选择题，填空题，上传题
     *
     * @param paperUnitCode
     * @return
     */
    @GetMapping(value = "/question/scores/{paperUnitCode}")
    public ServiceResponse getPaperForScoreSetting(@PathVariable("paperUnitCode") String paperUnitCode, String jsonStr) {
        List items = new ArrayList();
        String hashKey = selectKey(paperUnitCode);
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);
        List<String> list;
        DPaper dPaper = (DPaper) hashOperations.get(paperUnitCode + "-H-1");
        String mode = (String) redisTemplate.opsForValue().get(paperUnitCode + "-mode");
        if (jsonStr != null) {
            list = GsonUtil.fromJson(jsonStr, List.class);
            hashOperations.put("sequences", list);
        } else {
            list = (List<String>) hashOperations.get("sequences");
        }

        if (list == null) {
            return new ServiceResponse();
        }

        String unitCode;
        for (int i = 0; i < list.size(); i++) {
            unitCode = list.get(i);
            if (unitCode.indexOf("-H-") != -1) {
                continue;
            }
            items.add(hashOperations.getOperations().opsForHash().get(hashKey, unitCode));
        }
        DWPaper4 dwPaper4 = new DWPaper4();
        dwPaper4.setName(dPaper.getName());
        dwPaper4.setMode(mode.equals("新建中") ? 1 : mode.equals("修改中") ? 2 : 3);
        dwPaper4.setItems(items);
        return new ServiceResponse(dwPaper4);
    }

    /**
     * 试卷预览
     * 试卷内容， 包含 说明，选择题，填空题，上传题
     * OptionStem, BlankStem, AttachmentStem
     * 分为正式版本预览和草稿版本预览， 正式版本取自数据库， 草稿版本取自缓存
     *
     * @param paperUnitCode
     * @return
     */
    @GetMapping(value = "/question/paper/preview/{paperUnitCode}")
    public ServiceResponse<DWPaper3> getPaperForPreview(@PathVariable("paperUnitCode") String paperUnitCode, String viewSrc, int pageNo) {
        DWPaper3 dWPaper3 = new DWPaper3();
        String hashKey = selectKey(paperUnitCode);
        if ("0".equals(viewSrc)) {
            dWPaper3 = paperService.previewByDb(paperUnitCode, pageNo);
        } else {
            BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);
            List<String> sequences = (List<String>) hashOperations.get("sequences");

            DPaper dPaper = (DPaper) hashOperations.get(paperUnitCode + "-H-1");
            dWPaper3 = getStemsForPreview(dPaper, hashKey, pageNo, hashOperations, sequences);
        }
        return new ServiceResponse<>(dWPaper3);
    }

    private DWPaper3 getStemsForPreview(DPaper dPaper, String hashKey, int pageNo, BoundHashOperations<String, String, Object> hashOperations, List<String> sequences) {
        DWPaper3 dWPaper3 = new DWPaper3();
        List items = new ArrayList();//题目
        String unicode;
        int page = 0;
        int currentInstructionIndex = 0;
        DInstruction currentInstruction = null;
        DOption dOption;
        DBlank dBlank;
        DAttachment dAttachment;
        DOptionStem dOptionStem;
        DBlankStem dBlankStem;
        DAttachmentStem dAttachmentStem;
        List<Map> pageList = new ArrayList<>();//每页有多少题[{"pageIndex":1,"itemSize":5},{"pageIndex":2,"itemSize":3}]
        Map map = new HashMap();
        Integer totalNumber = 0;//题序，从1累加
        int itemNo = 0;
        if (sequences != null) {
            for (int i = 0; i < sequences.size(); i++) {
                unicode = sequences.get(i);
                if (unicode == null) {
                    continue;
                }
                if (unicode.indexOf("-P-") != -1) {
                    map = new HashMap();
                    itemNo = 0;
                    page++;
                    map.put("pageIndex", page);
                    pageList.add(map);
                } else if (unicode.indexOf("-P-") == -1 && unicode.indexOf("-I-") == -1) {
                    totalNumber++;
                    itemNo++;
                    map.put("itemSize", itemNo);
                }
                if (page == pageNo) {
                    if (unicode.indexOf("-I-") != -1) {
                        currentInstruction = (DInstruction) hashOperations.getOperations().opsForHash().get(hashKey, unicode);
                        currentInstructionIndex = i;
                    } else if (unicode.indexOf("-O-") != -1) {
                        dOption = (DOption) hashOperations.get(unicode);
                        dOptionStem = new DOptionStem();
                        dOptionStem.setSeqNo(totalNumber);
                        dOptionStem.setQuestion(dOption.getdOptionStemSetting().getQuestion());
                        dOptionStem.setType(dOption.getType());
                        dOptionStem.setdOptionStyleSetting(dOption.getdOptionStyleSetting());
                        dOptionStem.setOptions(dOption.getdOptionStemSetting().getOptions());
                        if ((currentInstructionIndex + 1) == i) {
                            dOptionStem.setdInstruction(currentInstruction);
                        }
                        items.add(dOptionStem);
                    } else if (unicode.indexOf("-B-") != -1) {
                        dBlank = (DBlank) hashOperations.get(unicode);
                        dBlankStem = new DBlankStem();
                        dBlankStem.setSeqNo(totalNumber);
                        dBlankStem.setQuestion(dBlank.getdBlankStemSetting().getQuestion());
                        dBlankStem.setNumbers(dBlank.getdBlankStemSetting().getNumbers());
                        dBlankStem.setType(dBlank.getType());
                        dBlankStem.setdBlankStyleSetting(dBlank.getdBlankStyleSetting());
                        dBlankStem.setBlankType(dBlank.getdBlankStemSetting().getType());
                        if ((currentInstructionIndex + 1) == i) {
                            dBlankStem.setdInstruction(currentInstruction);
                        }
                        items.add(dBlankStem);
                    } else if (unicode.indexOf("-A-") != -1) {
                        dAttachment = (DAttachment) hashOperations.get(unicode);
                        dAttachmentStem = new DAttachmentStem();
                        dAttachmentStem.setSeqNo(totalNumber);
                        dAttachmentStem.setQuestion(dAttachment.getdAttachmentStemSetting().getQuestion());
                        dAttachmentStem.setType(dAttachment.getType());
                        if ((currentInstructionIndex + 1) == i) {
                            dAttachmentStem.setdInstruction(currentInstruction);
                        }
                        items.add(dAttachmentStem);
                    }
                }

            }
        }
        dWPaper3.setItems(items);
        dWPaper3.setName(dPaper.getName());
        dWPaper3.setNumber(totalNumber);
        dWPaper3.setTotalPage(page);
        dWPaper3.setPageList(pageList);
        return dWPaper3;
    }

    /**
     * ToCheck
     * 根据试卷id查询整个试卷，为考试库使用
     * 取自数据库
     *
     * @param paperId
     * @return
     */
    @GetMapping(value = "/question/paper/examer/{paperId}")
    public ServiceResponse<DWPaperResponse> getPaper(@PathVariable("paperId") Integer paperId) {
        return paperService.getPaper(paperId);
    }

    /**
     * 根据试卷id查询整个结束语
     *
     * @param paperUnitCode
     * @return
     */
    @GetMapping(value = "/question/remark/{paperUnitCode}")
    public ServiceResponse<DWPaper4> getPaperRemarks(@PathVariable("paperUnitCode") String paperUnitCode) {
        DWPaper4 dWPaper4 = new DWPaper4();
        String hashKey = selectKey(paperUnitCode);
        BoundHashOperations<String, String, List<String>> hashOperations = redisTemplate.boundHashOps(hashKey);
        DPaper dPaper = (DPaper) hashOperations.get(paperUnitCode + "-H-1");
        List<DPaperRemark> dPaperRemarks = (List<DPaperRemark>) hashOperations.getOperations().opsForHash().get(hashKey, "remarks");
        dWPaper4.setItems(dPaperRemarks);
        dWPaper4.setName(dPaper.getName());
        //试卷分数
        BigDecimal totalScore = (BigDecimal) hashOperations.get("totalScore");
        //设置最后一个结束语的结束分数=总分
        if (dPaperRemarks != null && dPaperRemarks.size() > 0) {
            dPaperRemarks.get(dPaperRemarks.size() - 1).setEndScore(totalScore);
        }
        dWPaper4.setScore(totalScore);
        return new ServiceResponse(dWPaper4);
    }

    /**
     * 试卷列表
     *
     * @param companyId
     * @return
     */
    @GetMapping(value = "/question/papers/mock/{companyId}")
    public ServiceResponse<List<DWPaper>> papersMock(@PathVariable("companyId") Integer companyId) {
        List<DWPaper> list = new ArrayList<DWPaper>();
        DWPaper dWPaper = new DWPaper();
        dWPaper.setId(1);
        dWPaper.setName("初级技术人员水平考试");
        dWPaper.setTotalNum(25);
        dWPaper.setTotalScore(BigDecimal.valueOf(100));
        dWPaper.setDuration(60);
        dWPaper.setUsage(10);
        dWPaper.setCreator("admin");
        dWPaper.setCreatedDate(new Date().getTime());
        dWPaper.setStatus(true);
        list.add(dWPaper);
        dWPaper = new DWPaper();
        dWPaper.setId(2);
        dWPaper.setName("初级技术人员水平考试");
        dWPaper.setTotalNum(20);
        dWPaper.setTotalScore(BigDecimal.valueOf(100));
        dWPaper.setDuration(50);
        dWPaper.setUsage(4);
        dWPaper.setCreator("admin");
        dWPaper.setCreatedDate(new Date().getTime());
        dWPaper.setStatus(true);
        list.add(dWPaper);

        dWPaper = new DWPaper();
        dWPaper.setId(3);
        dWPaper.setName("初级顾问业务水平考试");
        dWPaper.setTotalNum(20);
        dWPaper.setTotalScore(BigDecimal.valueOf(100));
        dWPaper.setDuration(50);
        dWPaper.setUsage(6);
        dWPaper.setCreator("admin");
        dWPaper.setCreatedDate(new Date().getTime());
        dWPaper.setStatus(false);
        list.add(dWPaper);


        PageResponse pageResponse = new PageResponse();
        pageResponse.setResults(list);

        return new ServiceResponse(pageResponse);
    }


    //    /**
//     * 查询试卷页
//     *
//     * @param jsonStr
//     * @return
//     */
//    @GetMapping(value = "/question/pages")
//    public ServiceResponse<List<DWPage>> pages(String jsonStr) {
//        List<String> unicodes = GsonUtil.fromJson(jsonStr, List.class);
//        String unicode;
//        List<DWPage> dWPageResponses = new ArrayList<>();
//        DWPage dWPageResponse = null;
//        for (int i = 0; i < unicodes.size(); i++) {
//            unicode = unicodes.get(i);
//            GsonUtil.fromJson(jsonStr, List.class);
//            if (unicode.indexOf("P") != -1) {
//                DPage dPage = (DPage) redisTemplate.opsForValue().get(unicode);
//                dWPageResponse = new DWPage();
//                dWPageResponses.add(dWPageResponse);
//                dWPageResponse.setdPageStyleSetting(dPage.getdPageStyleSetting());
//
//            } else if (unicode.indexOf("I") != -1) {
//                DInstruction dInstruction = (DInstruction) redisTemplate.opsForValue().get(unicode);
//                dWPageResponse.setItem(dInstruction);
//            } else if (unicode.indexOf("O") != -1) {
//                DOption dOption = (DOption) redisTemplate.opsForValue().get(unicode);
//                dWPageResponse.setItem(dOption);
//
//            } else if (unicode.indexOf("B") != -1) {
//                DBlank dBlank = (DBlank) redisTemplate.opsForValue().get(unicode);
//                dWPageResponse.setItem(dBlank);
//            } else if (unicode.indexOf("A") != -1) {
//                DAttachment dAttachment = (DAttachment) redisTemplate.opsForValue().get(unicode);
//                dWPageResponse.setItem(dAttachment);
//            }
//        }
//        return new ServiceResponse(dWPageResponses);
//    }
}
