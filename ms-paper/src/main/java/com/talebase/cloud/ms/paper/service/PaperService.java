package com.talebase.cloud.ms.paper.service;

import com.google.gson.reflect.TypeToken;
import com.talebase.cloud.base.ms.paper.domain.TPaper;
import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.base.ms.paper.enums.*;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.util.BeanConverter;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.SequenceUtil;
import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.ms.paper.dao.PaperMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric.du on 2016-11-24.
 */

@Service
@Transactional(readOnly = true)
public class PaperService {

    @Autowired
    private PaperMapper paperMapper;
    @Autowired
    private PageService pageService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private BlankService blankService;
    @Autowired
    private OptionService optionService;
    @Autowired
    private InstructionService instructionService;
    @Autowired
    private PaperRemarkService paperRemarkService;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Integer savePaperHeader(ServiceHeader serviceHeader, DPaper dPaper) throws InvocationTargetException, IllegalAccessException {
        TPaper tPaper = changeDPaperToTPaper(dPaper);
        tPaper.setVersion(BigDecimal.valueOf(1));
        tPaper.setCompanyId(serviceHeader.getCompanyId());
        tPaper.setCreator(serviceHeader.getOperatorName());
        tPaper.setMode(DPaperMode.新建中.getValue());
        tPaper.setVersion(BigDecimal.ONE);
        tPaper.setVersionType(DVersionType.BIG_VERSION.getValue());
        tPaper.setComposer("{}");
        tPaper.setStatus(0);
        paperMapper.insert(tPaper);
        return tPaper.getId();
    }

    /**
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ServiceResponse flush(ServiceHeader serviceHeader, String hashKey, int stepNo) throws InvocationTargetException, IllegalAccessException {
        String paperUnitCode = hashKey.split("-")[0];
        String mode = paperUnitCode + "-mode";
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);
        String headerKey = paperUnitCode + "-H-1";
        DPaper dPaper = (DPaper) hashOperations.get(headerKey);
        dPaper.setUnicode(paperUnitCode);
//        DPaper dPaper = queryLatestByUnicode(paperUnitCode);
        List<DPage> dPages = new ArrayList<>();
        List<DInstruction> dInstructions = new ArrayList<>();
        List<DOption> dOptions = new ArrayList<>();
        List<DBlank> dBlanks = new ArrayList<>();
        List<DAttachment> dAttachments = new ArrayList<>();
        String remarkKey = "remarks";
        String sequenceKey = "sequences";
        List<DPaperRemark> paperRemarks = (List<DPaperRemark>) hashOperations.get(remarkKey);

        List<String> sequences = (List<String>) hashOperations.get(sequenceKey);
        int seqSize = sequences.size();
        List<DPaperComposer> dPaperComposers = new ArrayList<>();
        DPaperComposer dPaperComposer = null;
        String unicode;
        for (int i = 0; i < seqSize; i++) {
            unicode = sequences.get(i);
            if (unicode.indexOf("-O-") != -1) {
                DOption doption = (DOption) hashOperations.getOperations().opsForHash().get(hashKey, unicode);
                if (doption.getdOptionScoreSetting() == null) {
                    return new ServiceResponse(BizEnums.PAPER_NO_SCORE_SETTING);
                } else {
                    if (doption.getType() == DItemType.SINGLE_CHOICE.getValue()) {
                        if (StringUtil.isEmpty(doption.getdOptionScoreSetting().getAnswer())) {
                            return new ServiceResponse(BizEnums.PAPER_NO_SCORE_SETTING);
                        }
                    } else if (doption.getType() == DItemType.MULTIPLE_CHOICE.getValue()) {
                        if (doption.getdOptionScoreSetting().getAnswers() == null || doption.getdOptionScoreSetting().getAnswers().length <= 0) {
                            return new ServiceResponse(BizEnums.PAPER_NO_SCORE_SETTING);
                        }
                    }
                }
                dPaperComposer = convertToDcompose(doption);
                dPaperComposer.setScore(doption.getdOptionScoreSetting().getScore());
                dOptions.add(doption);
            } else if (unicode.indexOf("-B-") != -1) {
                DBlank dBlank = (DBlank) hashOperations.get(unicode);
                if (dBlank.getdBlankScoreSetting() == null) {
                    return new ServiceResponse(BizEnums.PAPER_NO_SCORE_SETTING);
                }
                dPaperComposer = convertToDcompose(dBlank);
                dPaperComposer.setScore(dBlank.getdBlankScoreSetting().getScore());
                dBlanks.add(dBlank);
            } else if (unicode.indexOf("-A-") != -1) {
                DAttachment dAttachment = (DAttachment) hashOperations.get(unicode);
                if (dAttachment.getdAttachmentScoreSetting() == null) {
                    return new ServiceResponse(BizEnums.PAPER_NO_SCORE_SETTING);
                }
                dPaperComposer = convertToDcompose(dAttachment);
                dPaperComposer.setScore(dAttachment.getdAttachmentScoreSetting().getScore());
                dAttachments.add(dAttachment);
            } else if (unicode.indexOf("-P-") != -1) {
                DPage dPage = (DPage) hashOperations.get(unicode);
                dPaperComposer = convertToDcompose(dPage);
                dPages.add(dPage);
            } else if (unicode.indexOf("-I-") != -1) {
                DInstruction dInstruction = (DInstruction) hashOperations.get(unicode);
                dPaperComposer = convertToDcompose(dInstruction);
                dInstructions.add(dInstruction);
            }
            dPaperComposer.setSeqNo(i);
            dPaperComposers.add(dPaperComposer);
        }
        DItem ditem;
        BigDecimal endScore = BigDecimal.ZERO;
        if (paperRemarks != null) {
            for (int j = 0; j < paperRemarks.size(); j++) {
                ditem = paperRemarks.get(j);
                if (j == paperRemarks.size() - 1) {
                    endScore = paperRemarks.get(j).getEndScore();
                }
                dPaperComposer = convertToDcompose(ditem);
                dPaperComposer.setSeqNo(seqSize + j);
                dPaperComposers.add(dPaperComposer);
                dPaperComposer.setScore(BigDecimal.valueOf(0));
            }
        }
        String totalScoreKey = "totalScore";
        BigDecimal totalScore = (BigDecimal) hashOperations.get(totalScoreKey);
        if (totalScore.compareTo(endScore) != 0) {
            return new ServiceResponse(BizEnums.PAPER_REMARK_SCORE_UNCORRECT);
        }
        int paperId = 0;
        if (DPaperMode.新建中.name().equals(redisTemplate.opsForValue().get(mode))) {
            dPaper.setVersionType(DVersionType.BIG_VERSION.getValue());
            paperId = flushAll(true, serviceHeader, dPaper, dPages, dInstructions, dOptions, dBlanks, dAttachments, paperRemarks, dPaperComposers);
        } else {
            paperId = diffVersionAndFlush(serviceHeader, dPaper, dPages, dInstructions, dOptions, dBlanks, dAttachments, paperRemarks, dPaperComposers);
        }
        return new ServiceResponse(paperId);
    }

    private int flushAll(boolean hashChange,
                         ServiceHeader serviceHeader,
                         DPaper dPaper,
                         List<DPage> dPages,
                         List<DInstruction> dInstructions,
                         List<DOption> dOptions,
                         List<DBlank> dBlanks,
                         List<DAttachment> dAttachments,
                         List<DPaperRemark> dPaperRemarks,
                         List<DPaperComposer> dPaperComposers) throws InvocationTargetException, IllegalAccessException {
        //上传题保存
        DQuestionCount attachmentCount = attachmentService.save(serviceHeader, dAttachments);
        updateDPaperComposers(attachmentCount, dPaperComposers);
        //填空题保存
        DQuestionCount blankCount = blankService.save(serviceHeader, dBlanks);
        updateDPaperComposers(blankCount, dPaperComposers);
        //选择题保存
        DQuestionCount optionCount = optionService.save(serviceHeader, dOptions);
        updateDPaperComposers(optionCount, dPaperComposers);
        //说明保存
        DQuestionCount instructionCount = instructionService.save(serviceHeader, dInstructions);
        updateDPaperComposers(instructionCount, dPaperComposers);
        //页码保存
        DQuestionCount pageCount = pageService.save(serviceHeader, dPages);
        updateDPaperComposers(pageCount, dPaperComposers);
        //结束语保存
        DQuestionCount paperRemarkCount = paperRemarkService.save(serviceHeader, dPaperRemarks);
        updateDPaperComposers(paperRemarkCount, dPaperComposers);

        dPaper.setComposer(GsonUtil.toJson(dPaperComposers));

        //总题目数=上传题+填空题+选择题
        Integer totalNum = attachmentCount.getTotalCount() + blankCount.getTotalCount() + optionCount.getTotalCount();
        //主观题目数=上传题+主观填空题
        Integer subjectNum = attachmentCount.getSubjectiveCount() + blankCount.getSubjectiveCount();
        //总分=上传题+填空题+选择题
        BigDecimal totalScore = attachmentCount.getTotalScore().add(blankCount.getTotalScore()).add(optionCount.getTotalScore());

        //试卷保存
        Integer paperId;
        dPaper.setTotalNum(totalNum);
        dPaper.setSubjectNum(subjectNum);
        dPaper.setScore(totalScore);
        //将题目ID设置到dPaperComposers中
        TPaper tPaper = paperMapper.queryLatestByUnicode(dPaper.getUnicode());

        if (tPaper.getComment() == null) {
            tPaper.setComment("");
        }
        if (tPaper != null) {
            dPaper.setId(tPaper.getId());
        }
        boolean paperHeaderChanged = !(tPaper.getName().equals(dPaper.getName()) &&
                tPaper.getComment().equals(dPaper.getComment()) &&
                tPaper.getDuration() == dPaper.getDuration());

        boolean contentChanged = attachmentCount.isHasChange()
                || blankCount.isHasChange()
                || optionCount.isHasChange()
                || instructionCount.isHasChange()
                || pageCount.isHasChange()
                || paperRemarkCount.isHasChange();

        if (paperHeaderChanged || contentChanged || hashChange) {
            dPaper.setMode(DPaperMode.修改中.getValue());
            paperId = save(serviceHeader, dPaper);
            dPaper.setId(paperId);
        }
        return dPaper.getId();
    }

    /**
     * 更新组卷对象
     *
     * @param dQuestionCount
     * @param dPaperComposers
     */
    private void updateDPaperComposers(DQuestionCount dQuestionCount, List<DPaperComposer> dPaperComposers) {
        List<DUnicode> unicodeList = dQuestionCount.getUnicodeList();
        for (DPaperComposer dPaperComposer : dPaperComposers) {
            for (DUnicode dUnicode : unicodeList) {
                if (dPaperComposer.getUnicCode().equals(dUnicode.getUnicode())) {
                    dPaperComposer.setSubjectId(dUnicode.getId());
                }
            }
        }
    }

    /**
     * 1.  约定只改变字眼,顺序,结束语，说明增删、分页符增删的改动，为次要改动，次要改动产生小版本号增加,试卷版本为小版本
     * 2.  约定改动涉及分数、题型、题目增减的，都为全面改动，全面改动产生大版本号增加,试卷版本为大版本
     *
     * @param serviceHeader
     * @param dPages
     * @param dInstructions
     * @param dOptions
     * @param dBlanks
     * @param dAttachments
     * @param paperRemarks
     * @param dPaperComposers
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private int diffVersionAndFlush(ServiceHeader serviceHeader,
                                    DPaper dPaper,
                                    List<DPage> dPages,
                                    List<DInstruction> dInstructions,
                                    List<DOption> dOptions,
                                    List<DBlank> dBlanks,
                                    List<DAttachment> dAttachments,
                                    List<DPaperRemark> paperRemarks,
                                    List<DPaperComposer> dPaperComposers) throws InvocationTargetException, IllegalAccessException {
        //------------------小版本变化校验开始------------------------------------
        boolean hashChange = false;
        List<DPaperComposer> dPaperComposerSaved = queryPaperComposers(dPaper.getUnicode());
        DPaperComposer tempSaved;
        DPaperComposer temp;
        //整份试卷是否有题目增减
        if (dPaperComposers.size() != dPaperComposerSaved.size()) {
            hashChange = true;
        } else {
            //试卷是否有题序变化
            int i = 0;
            while (i < dPaperComposerSaved.size()) {
                tempSaved = dPaperComposerSaved.get(i);
                temp = dPaperComposers.get(i);
                if (!(tempSaved.getUnicCode().equals(temp.getUnicCode()) &&
                        tempSaved.getType().equals(temp.getType()) &&
                        tempSaved.getSeqNo().equals(tempSaved.getSeqNo()))) {
                    hashChange = true;
                    break;
                }
                i++;
            }
        }
        //------------------小版本变化校验结束------------------------------------
        //--------------------------------------------------------------------------------
        //获得数据库中已经保存的paperComposers对象
        boolean isBigVersion = false;
        List<DPaperComposer> dPaperComposerSavedFiltered = new ArrayList<>();
        List<DPaperComposer> toUpdate = new ArrayList();
        for (int i = 0; i < dPaperComposerSaved.size(); i++) {
            temp = dPaperComposerSaved.get(i);
            if (temp.getType() == 4 || temp.getType() == 5 || temp.getType() == 6 || temp.getType() == 7) {
                dPaperComposerSavedFiltered.add(temp);
            }
        }
        for (int i = 0; i < dPaperComposers.size(); i++) {
            temp = dPaperComposers.get(i);
            if (temp.getType() == 4 || temp.getType() == 5 || temp.getType() == 6 || temp.getType() == 7) {
                toUpdate.add(temp);
            }
        }


        //分数、题型，题目增减的大版本比对
        if (!dPaperComposerSavedFiltered.equals(toUpdate)) {
            isBigVersion = true;
        }

        if (isBigVersion) {
            dPaper.setVersionType(DVersionType.BIG_VERSION.getValue());
        } else {
            dPaper.setVersionType(DVersionType.LITTLE_VERSION.getValue());
        }
        return flushAll(hashChange, serviceHeader, dPaper, dPages, dInstructions, dOptions, dBlanks, dAttachments, paperRemarks, dPaperComposers);
    }

    /**
     * yb 2017-1-14 17:15 修改
     *
     * @param paperUnicode
     * @return
     */
    private List<DPaperComposer> queryPaperComposers(String paperUnicode) {
        TPaper tPaper = paperMapper.queryLatestByUnicode(paperUnicode);
        List<DPaperComposer> dPaperComposers = GsonUtil.fromJson(tPaper.getComposer(), new TypeToken<List<DPaperComposer>>() {
        }.getType());
        return dPaperComposers;
    }
//    private List<DPaperComposer> queryPaperComposers(int paperId) {
//        String composers = paperMapper.queryPaperComposers(paperId);
//        List<DPaperComposer> dPaperComposers = GsonUtil.fromJson(composers, new TypeToken<List<DPaperComposer>>() {
//        }.getType());
//        return dPaperComposers;
//    }

    private DPaperComposer convertToDcompose(DItem dItem) {
        DPaperComposer dPaperComposer = new DPaperComposer();
        dPaperComposer.setSubjectId(dItem.getId());
        dPaperComposer.setType(dItem.getType());
        dPaperComposer.setUnicCode(dItem.getUnicode());
        return dPaperComposer;
    }

    /**
     * @return
     */
//    @Transactional(rollbackFor = Exception.class)
//    public ServiceResponse accemblyAndFlush(ServiceHeader serviceHeader, DPaperRequest dPaperRequest) throws InvocationTargetException, IllegalAccessException {
//        DPaper dPaper = dPaperRequest.getdPaper();
//        List<DPage> dPages = dPaperRequest.getdPages();
//        List<DInstruction> dInstructions = dPaperRequest.getdInstructions();
//        List<DOption> dOptions = dPaperRequest.getdOptions();
//        List<DBlank> dBlanks = dPaperRequest.getdBlanks();
//        List<DAttachment> dAttachments = dPaperRequest.getdAttachments();
//        List<DPaperRemark> dPaperRemarks = dPaperRequest.getdPaperRemarks();
//        //试卷保存
//        Integer paperId = save(serviceHeader, dPaper);
//        //上传题保存
//        DQuestionCount attachmentCount = attachmentService.save(serviceHeader, dAttachments);
//        //填空题保存
//        DQuestionCount blankCount = blankService.save(serviceHeader, dBlanks);
//        //选择题保存
//        DQuestionCount optionCount = optionService.save(serviceHeader, dOptions);
//        //说明保存
//        DQuestionCount instructionCount = instructionService.save(serviceHeader, dInstructions);
//        //页码保存
//        DQuestionCount pageCount = pageService.save(serviceHeader, dPages);
//        //结束语保存
//        DQuestionCount paperCount = paperRemarkService.save(serviceHeader, dPaperRemarks);
//
//        //获得试卷内容
//        List<DPaperComposer> dPaperComposers = dPaperRequest.getdPaperComposers();
//        //合并结果集，为了塞题目编号
//        List<DUnicode> dUnicodes = new ArrayList<>();
//        dUnicodes.addAll(attachmentCount.getUnicodeList());
//        dUnicodes.addAll(blankCount.getUnicodeList());
//        dUnicodes.addAll(optionCount.getUnicodeList());
//        dUnicodes.addAll(instructionCount.getUnicodeList());
//        dUnicodes.addAll(pageCount.getUnicodeList());
//        dUnicodes.addAll(paperCount.getUnicodeList());
//        //为组卷赋值题目编号
//        for (DPaperComposer dPaperComposer : dPaperComposers) {
//            for (DUnicode dUnicode : dUnicodes) {
//                if (dPaperComposer.getUnicCode().equals(dUnicode.getUnicode())) {
//                    dPaperComposer.setSubjectId(dUnicode.getId());//设置题目id
//                }
//            }
//        }
//        dPaper.setComposer(GsonUtil.toJson(dPaperComposers));
//        //总题目数=上传题+填空题+选择题
//        Integer totalNum = attachmentCount.getTotalCount() + blankCount.getTotalCount() + optionCount.getTotalCount();
//        //主观题目数=上传题+主观填空题
//        Integer subjectNum = attachmentCount.getSubjectiveCount() + blankCount.getSubjectiveCount();
//        //总分=上传题+填空题+选择题
//        BigDecimal totalScore = attachmentCount.getTotalScore().add(blankCount.getTotalScore()).add(optionCount.getTotalScore());
//        dPaper.setId(paperId);
//        dPaper.setTotalNum(totalNum);
//        dPaper.setSubjectNum(subjectNum);
//        dPaper.setScore(totalScore);
//        update(serviceHeader, dPaper);
//        return new ServiceResponse(paperId);
//    }

    /**
     * 大小版本都不会影响Unicode的生成
     *
     * @param serviceHeader
     * @param dPaper
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Transactional(readOnly = false)
    public Integer save(ServiceHeader serviceHeader, DPaper dPaper) throws InvocationTargetException, IllegalAccessException {
        TPaper tPaper = changeDPaperToTPaper(dPaper);

        BigDecimal version = paperMapper.queryByUnicode(dPaper.getUnicode());

        if (dPaper.getVersionType() == DVersionType.BIG_VERSION.getValue()) {
            version = version.add(BigDecimal.valueOf(1));
        } else if (dPaper.getVersionType() == DVersionType.LITTLE_VERSION.getValue()) {
            version = version.add(BigDecimal.valueOf(0.1));
        }

        tPaper.setVersion(version);
        tPaper.setCompanyId(serviceHeader.getCompanyId());
        tPaper.setCreator(serviceHeader.getOperatorName());
        if (tPaper.getMode() == DPaperMode.新建中.getValue()) {
            tPaper.setVersion(BigDecimal.ONE);
            tPaper.setMode(DPaperMode.完成.getValue());
            tPaper.setVersionType(DVersionType.BIG_VERSION.getValue());
            tPaper.setStatus(TPaperStatus.ENABLED.getValue());
            paperMapper.update(tPaper);
        } else if (tPaper.getMode() == DPaperMode.修改中.getValue()) {
            tPaper.setMode(DPaperMode.完成.getValue());
            tPaper.setStatus(TPaperStatus.ENABLED.getValue());
            paperMapper.updateMode(tPaper.getId(), DPaperMode.完成.getValue(), true);
            paperMapper.insert(tPaper);
        }
        return tPaper.getId();
    }

    public ServiceResponse<PageResponse<DPaper>> queryList(ServiceHeader serviceHeader, DPaperQuery dPaperQuery, PageRequest pageRequest) throws InvocationTargetException, IllegalAccessException {
        List<DPaper> dPapers = new ArrayList<>();
        List<TPaper> tPapers = paperMapper.queryList(serviceHeader.getCompanyId(), serviceHeader.getOperatorName(), dPaperQuery, pageRequest);
        for (TPaper tPaper : tPapers) {
            DPaper dPaper = changeTPaperToDPaper(tPaper);
            dPapers.add(dPaper);
        }
        Integer count = paperMapper.queryCount(serviceHeader.getCompanyId(), serviceHeader.getOperatorName(), dPaperQuery);
        PageResponse<DPaper> pageResponse = new PageResponse<>();
        pageResponse.setResults(dPapers);
        pageResponse.setTotal(count);
        pageResponse.setPageIndex(pageRequest.getPageIndex());
        pageResponse.setLimit(pageRequest.getLimit());
        return new ServiceResponse(pageResponse);
    }

    public ServiceResponse<PageResponse<DPaper>> queryListByIds(ServiceHeader serviceHeader, List<Integer> ids, PageRequest pageRequest) throws InvocationTargetException, IllegalAccessException {
        List<DPaper> dPapers = new ArrayList<>();
        ids.add(-1);
        List<TPaper> tPapers = paperMapper.queryListByIds(serviceHeader.getCompanyId(), serviceHeader.getOperatorName(), ids, pageRequest);
        for (TPaper tPaper : tPapers) {
            DPaper dPaper = changeTPaperToDPaper(tPaper);
            dPapers.add(dPaper);
        }
        Integer count = paperMapper.queryCountByIds(serviceHeader.getCompanyId(), serviceHeader.getOperatorName(), ids);
        PageResponse<DPaper> pageResponse = new PageResponse<>();
        pageResponse.setResults(dPapers);
        pageResponse.setTotal(count);
        pageResponse.setPageIndex(pageRequest.getPageIndex());
        pageResponse.setLimit(pageRequest.getLimit());
        return new ServiceResponse(pageResponse);
    }

    public ServiceResponse getPaperForExport(Integer id) throws InvocationTargetException, IllegalAccessException {
        TPaper tPaper = paperMapper.queryById(id);
        DPaper dPaper = changeTPaperToDPaper(tPaper);
        DPaperResponse dPaperResponse = new DPaperResponse();
        List<DPaperComposer> dPaperComposers = GsonUtil.fromJson(dPaper.getComposer(), new TypeToken<List<DPaperComposer>>() {
        }.getType());
        dPaper.setComposer("[]");
        dPaperResponse.setdPaper(dPaper);
        dPaperResponse.setdPaperComposers(dPaperComposers);
        for (DPaperComposer dPaperComposer : dPaperComposers) {
            switch (dPaperComposer.getType()) {
//                case 2:
//                    DPage dPage = pageService.queryById(dPaperComposer.getSubjectId());
//                    dPage.setType(DItemType.PAGE.getValue());
//                    dPaperResponse.setdPage(dPage);
//                    break;
//                case 3:
//                    DInstruction dInstruction = instructionService.queryById(dPaperComposer.getSubjectId());
//                    dInstruction.setType(DItemType.INSTRUCTION.getValue());
//                    dPaperResponse.setdInstruction(dInstruction);
//                    break;
                case 4:
                    DOption s_dOption = optionService.queryById(dPaperComposer.getSubjectId());
                    s_dOption.setType(DItemType.SINGLE_CHOICE.getValue());
                    dPaperResponse.setdOption(s_dOption);
                    break;
                case 5:
                    DOption m_dOption = optionService.queryById(dPaperComposer.getSubjectId());
                    m_dOption.setType(DItemType.MULTIPLE_CHOICE.getValue());
                    dPaperResponse.setdOption(m_dOption);
                    break;
                case 6:
                    DBlank dBlank = blankService.queryById(dPaperComposer.getSubjectId());
                    dBlank.setType(DItemType.BLANK.getValue());
                    dPaperResponse.setdBlank(dBlank);
                    break;
                case 7:
                    DAttachment dAttachment = attachmentService.queryById(dPaperComposer.getSubjectId());
                    dAttachment.setType(DItemType.ATTACHMENT.getValue());
                    dPaperResponse.setdAttachment(dAttachment);
                    break;
//                case 8:
//                    DPaperRemark dPaperRemark = paperRemarkService.queryById(dPaperComposer.getSubjectId());
//                    dPaperResponse.setdPaperRemark(dPaperRemark);
//                    break;
                default:
            }
        }
        return new ServiceResponse(dPaperResponse);
    }

    public TPaper getSimpleById(Integer id) {
        return paperMapper.queryById(id);
    }

    /**
     * 1. 试卷信息
     * 2. 页码
     * 3. 说明
     * 4. 单择题
     * 5. 多择题
     * 6. 填空题
     * 7. 上传题目
     * 8. 结束语
     *
     * @param id
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public ServiceResponse getById(Integer id) throws InvocationTargetException, IllegalAccessException {
        TPaper tPaper = paperMapper.queryById(id);
        DPaper dPaper = changeTPaperToDPaper(tPaper);

        DWPaperResponse dPaperResponse = new DWPaperResponse();
        List<DPaperComposer> dPaperComposers = GsonUtil.fromJson(dPaper.getComposer(), new TypeToken<List<DPaperComposer>>() {
        }.getType());

        dPaper.setComposer("[]");
        dPaperResponse.setdPaper(dPaper);
        dPaperResponse.setdPaperRemarks(new ArrayList<>());
        List<DWPage> pages = new ArrayList<DWPage>();
        dPaperResponse.setPages(pages);

        DPaperComposer dPaperComposer;
        DWPage page = null;
        for (int i = 0; i < dPaperComposers.size(); i++) {
            dPaperComposer = dPaperComposers.get(i);
            switch (dPaperComposer.getType()) {
                case 2:
                    DPage dPage = pageService.queryById(dPaperComposer.getSubjectId());
                    page = new DWPage();
                    dPage.setType(DItemType.PAGE.getValue());
                    page.setdPageStyleSetting(dPage.getdPageStyleSetting());
                    pages.add(page);
                    page.setItems(new ArrayList());
                    break;
                case 3:
                    DInstruction dInstruction = instructionService.queryById(dPaperComposer.getSubjectId());
                    dInstruction.setType(DItemType.INSTRUCTION.getValue());
                    page.setItem(dInstruction);
                    break;
                case 4:
                    DOption s_dOption = optionService.queryById(dPaperComposer.getSubjectId());
                    s_dOption.setType(DItemType.SINGLE_CHOICE.getValue());
                    page.setItem(s_dOption);
                    break;
                case 5:
                    DOption m_dOption = optionService.queryById(dPaperComposer.getSubjectId());
                    m_dOption.setType(DItemType.MULTIPLE_CHOICE.getValue());
                    page.setItem(m_dOption);
                    break;
                case 6:
                    DBlank dBlank = blankService.queryById(dPaperComposer.getSubjectId());
                    dBlank.setType(DItemType.BLANK.getValue());
                    page.setItem(dBlank);
                    break;
                case 7:
                    DAttachment dAttachment = attachmentService.queryById(dPaperComposer.getSubjectId());
                    dAttachment.setType(DItemType.ATTACHMENT.getValue());
                    page.setItem(dAttachment);
                    break;
                case 8:
                    DPaperRemark dPaperRemark = paperRemarkService.queryById(dPaperComposer.getSubjectId());
                    dPaperRemark.setType(DItemType.REMARK.getValue());
                    dPaperResponse.setdPaperRemark(dPaperRemark);
                    break;
                default:
            }
        }
        return new ServiceResponse(dPaperResponse);
    }

    /**
     * 预览试卷
     *
     * @param unicode
     * @return
     */
    public ServiceResponse<DWPaper3> previewByUnicode(String unicode) throws InvocationTargetException, IllegalAccessException {
        TPaper tPaper = paperMapper.queryLatestByUnicode(unicode);
        if (tPaper.getMode() == DPaperMode.修改中.getValue()) {
            return new ServiceResponse(BizEnums.PAPER_MODE_MODIFICATION, true);
        }
        DWPaper3 dwPaper3 = new DWPaper3();
        List<DPaperComposer> dPaperComposers = GsonUtil.fromJson(tPaper.getComposer(), new TypeToken<List<DPaperComposer>>() {
        }.getType());
        dwPaper3.setName(tPaper.getName());
        dwPaper3.setNumber(dPaperComposers.size());
        List<Object> items = new ArrayList<>();
        dwPaper3.setItems(items);

        DPaperComposer dPaperComposer;
        for (int i = 0; i < dPaperComposers.size(); i++) {
            dPaperComposer = dPaperComposers.get(i);
            switch (dPaperComposer.getType()) {
                case 2:
                    DPage dPage = pageService.queryById(dPaperComposer.getSubjectId());
                    dPage.setType(DItemType.PAGE.getValue());
                    items.add(dPage);
                    break;
                case 3:
                    DInstruction dInstruction = instructionService.queryById(dPaperComposer.getSubjectId());
                    dInstruction.setType(DItemType.INSTRUCTION.getValue());
                    items.add(dInstruction);
                    break;
                case 4:
                    DOption s_dOption = optionService.queryById(dPaperComposer.getSubjectId());
                    s_dOption.setType(DItemType.SINGLE_CHOICE.getValue());
                    items.add(s_dOption);
                    break;
                case 5:
                    DOption m_dOption = optionService.queryById(dPaperComposer.getSubjectId());
                    m_dOption.setType(DItemType.MULTIPLE_CHOICE.getValue());
                    items.add(m_dOption);
                    break;
                case 6:
                    DBlank dBlank = blankService.queryById(dPaperComposer.getSubjectId());
                    dBlank.setType(DItemType.BLANK.getValue());
                    items.add(dBlank);
                    break;
                case 7:
                    DAttachment dAttachment = attachmentService.queryById(dPaperComposer.getSubjectId());
                    dAttachment.setType(DItemType.ATTACHMENT.getValue());
                    items.add(dAttachment);
                    break;
                case 8:
                    DPaperRemark dPaperRemark = paperRemarkService.queryById(dPaperComposer.getSubjectId());
                    dPaperRemark.setType(DItemType.REMARK.getValue());
                    items.add(dPaperRemark);
                    break;
                default:
            }
        }
        return new ServiceResponse(dwPaper3);
    }

    public ServiceResponse queryPaperByUnicodes(List<String> unicodes) {
        List<TPaper> dPapers = paperMapper.queryPaperByUnicodes(unicodes);
        return new ServiceResponse(dPapers);
    }

    @Transactional(readOnly = false)
    public ServiceResponse update(ServiceHeader serviceHeader, DPaper dPaper) throws InvocationTargetException, IllegalAccessException {
        TPaper tPaper = changeDPaperToTPaper(dPaper);
        if (tPaper.getSubjectNum() > 0) {
            tPaper.setMark(TPaperMark.YES.getValue());
        } else {
            tPaper.setMark(TPaperMark.NO.getValue());
        }
        Integer count = paperMapper.update(tPaper);
        return new ServiceResponse(count);
    }

    @Transactional(readOnly = false)
    public ServiceResponse updateStatus(Integer id, boolean status) {
        Integer count = paperMapper.updateStatus(id, status);
        return new ServiceResponse(count);
    }

    @Transactional(readOnly = false)
    public ServiceResponse updateStatusByUnicode(String unicode, boolean status) {
        Integer count = paperMapper.updateStatusByUnicode(unicode, status);
        return new ServiceResponse(count);
    }


    @Transactional(readOnly = false)
    public ServiceResponse updateMode(int id, int dPageMode, boolean status) {
        Integer count = paperMapper.updateMode(id, dPageMode, status);
        return new ServiceResponse(count);
    }

    @Transactional(readOnly = false)
    public ServiceResponse updateAddTimes(List<Integer> ids) {
        Integer count = paperMapper.updateAddTimes(ids);
        return new ServiceResponse(count);
    }

    @Transactional(readOnly = false)
    public ServiceResponse updateMinusTimes(List<Integer> ids) {
        Integer count = paperMapper.updateMinusTimes(ids);
        return new ServiceResponse(count);
    }

    @Transactional(readOnly = false)
    public ServiceResponse deletById(Integer id, ServiceHeader serviceHeader) {
        Integer count = paperMapper.deleteById(id);
        return new ServiceResponse(count);
    }

    @Transactional(readOnly = false)
    public ServiceResponse deleteByUnicode(String unicode, ServiceHeader serviceHeader) {
        Integer count = paperMapper.deleteByUnicode(unicode);
        return new ServiceResponse(count);
    }

    private DPaper changeTPaperToDPaper(TPaper tPaper) throws InvocationTargetException, IllegalAccessException {
        DPaper dPaper = new DPaper();
        dPaper.setCreator(tPaper.getCreator());
        dPaper.setId(tPaper.getId());
        dPaper.setScore(tPaper.getScore());
        dPaper.setUnicode(tPaper.getUnicode());
        dPaper.setComment(tPaper.getComment());
        dPaper.setDuration(tPaper.getDuration());
        dPaper.setComposer(tPaper.getComposer());
        dPaper.setUsage(tPaper.getUsage());
        dPaper.setSubjectNum(tPaper.getSubjectNum());
        dPaper.setTotalNum(tPaper.getTotalNum());
        dPaper.setName(tPaper.getName());
        dPaper.setCreatedDate(tPaper.getCreatedDate().getTime());
        dPaper.setStatus(tPaper.getStatus() == TPaperStatus.ENABLED.getValue() ? true : false);
        dPaper.setMode(tPaper.getMode());
        dPaper.setVersionType(tPaper.getVersionType());
//        BeanConverter.copyProperties(dPaper, tPaper);
        return dPaper;
    }

    private TPaper changeDPaperToTPaper(DPaper dPaper) throws InvocationTargetException, IllegalAccessException {
        TPaper tPaper = new TPaper();
        tPaper.setCreator(dPaper.getCreator());
        tPaper.setId(dPaper.getId());
        tPaper.setScore(dPaper.getScore());
        tPaper.setUnicode(dPaper.getUnicode());
        tPaper.setComment(dPaper.getComment());
        tPaper.setDuration(dPaper.getDuration());
        tPaper.setComposer(dPaper.getComposer());
        tPaper.setUsage(dPaper.getUsage());
        tPaper.setSubjectNum(dPaper.getSubjectNum());
        tPaper.setTotalNum(dPaper.getTotalNum());
        tPaper.setName(dPaper.getName());
        tPaper.setMode(dPaper.getMode());
        tPaper.setVersionType(dPaper.getVersionType());
        if (dPaper.getSubjectNum() != null && dPaper.getSubjectNum() > 0) {
            tPaper.setMark(TPaperMark.YES.getValue());
        } else {
            tPaper.setMark(TPaperMark.NO.getValue());
        }
//        BeanConverter.copyProperties(tPaper, dPaper);
        return tPaper;
    }

    public List<TPaper> getSimpleByIds(List<Integer> paperIds) {
        return paperMapper.getByIds(paperIds);
    }

    @Transactional(readOnly = false)
    public ServiceResponse copy(Integer paperId, String newPaperUnicode, String newName, String creator) {
        TPaper tPaper = paperMapper.queryById(paperId);
        tPaper.setId(null);
        tPaper.setName(newName);
        tPaper.setUnicode(newPaperUnicode);
        tPaper.setVersion(BigDecimal.valueOf(1));
        tPaper.setVersionType(DVersionType.BIG_VERSION.getValue());
        tPaper.setMode(DPaperMode.完成.getValue());
        tPaper.setUsage(0);
        tPaper.setStatus(TPaperStatus.ENABLED.getValue());
        tPaper.setCreator(creator);
        paperMapper.insert(tPaper);
        return new ServiceResponse(tPaper.getId());
    }

    public ServiceResponse checkName(Integer paperId, String name, Integer companyId) {
        TPaper tPaper = getSimpleById(paperId);
        Integer count = paperMapper.checkName(tPaper.getUnicode(), name, companyId);
        if (count > 0) {
            return new ServiceResponse<String>(BizEnums.PAPER_NAME_ISDUPLICATE, true);
        }
        return new ServiceResponse<String>();
    }

    public DPaper queryLatestByUnicode(String paperUnicode) throws InvocationTargetException, IllegalAccessException {
        TPaper tPaper = paperMapper.queryLatestByUnicode(paperUnicode);
        DPaper dPaper = changeTPaperToDPaper(tPaper);
        return dPaper;
    }
}