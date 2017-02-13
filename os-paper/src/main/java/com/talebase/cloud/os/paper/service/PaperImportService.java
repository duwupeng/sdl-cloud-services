package com.talebase.cloud.os.paper.service;

import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.base.ms.paper.enums.*;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.NumberUtil;
import com.talebase.cloud.common.util.SequenceUtil;
import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.os.paper.controller.PaperCache;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by bin.yang on 2016-12-20.
 */

@Service
public class PaperImportService extends PaperCache {
    @Autowired
    RedisTemplate redisTemplate;

    private static final DataFormatter FORMATTER = new DataFormatter();
    //------------单选题 begin-----------
    private static final int SINGLE_CHOICE = 0;
    private static final int SINGLE_CHOICE_STEM = 1;
    private static final int SINGLE_CHOICE_OPTION_SEQ = 2;
    private static final int SINGLE_CHOICE_OPTION_LABEL = 3;
    private static final int SINGLE_CHOICE_ANSWER = 4;
    private static final int SINGLE_CHOICE_SCORE = 5;
    //------------单选题 end-----------

    //------------多选题 begin-----------
    private static final int MULTIPLE_CHOICE = 0;
    private static final int MULTIPLE_CHOICE_STEM = 1;
    private static final int MULTIPLE_CHOICE_OPTION_SEQ = 2;
    private static final int MULTIPLE_CHOICE_OPTION_LABEL = 3;
    private static final int MULTIPLE_CHOICE_ANSWER = 4;
    private static final int MULTIPLE_CHOICE_SCORE = 5;
    private static final int MULTIPLE_CHOICE_SCORE_SET = 6;
    private static final int MULTIPLE_CHOICE_SCORE_LESS = 7;
    //------------多选题 end-----------

    //------------客观填空题 begin-----------
    private static final int OBJECTIVE_BLANK = 0;
    private static final int OBJECTIVE_BLANK_STEM = 1;
    private static final int OBJECTIVE_BLANK_OPTION_SEQ = 2;
    private static final int OBJECTIVE_BLANK_ANSWER = 4;
    private static final int OBJECTIVE_BLANK_SCORE = 5;
    private static final int OBJECTIVE_BLANK_SCORE_RULE = 6;
    private static final int OBJECTIVE_BLANK_EXPLANATION = 7;
    //------------客观填空题 end-----------

    //------------主观填空题 begin-----------
    private static final int SUBJECTIVE_BLANK = 0;
    private static final int SUBJECTIVE_BLANK_STEM = 1;
    private static final int SUBJECTIVE_BLANK_OPTION_SEQ = 2;
    private static final int SUBJECTIVE_BLANK_ANSWER = 4;
    private static final int SUBJECTIVE_BLANK_SCORE = 5;
    private static final int SUBJECTIVE_BLANK_SCORE_RULE = 6;
    private static final int SUBJECTIVE_BLANK_EXPLANATION = 7;
    //------------主观填空题 end-----------

    //------------上传题 begin-----------
    private static final int ATTACHMENT = 0;
    private static final int ATTACHMENT_STEM = 1;
    private static final int ATTACHMENT_SCORE = 2;
    //------------上传题 end-----------

    /**
     * 读取excel文件（同时支持2003和2007格式）
     *
     * @param fileName 文件名，绝对路径
     * @return list中的map的key是列的序号
     * @throws Exception io异常等
     */
    public DPaperResponse readExcel(InputStream inputStream, String fileName) throws Exception {
        FileInputStream fis = null;
        Workbook wb = null;
        DPaperResponse dPaperResponse = new DPaperResponse();
        try {
            String extension = FilenameUtils.getExtension(fileName);
            //验证是否是excel
            wb = read(inputStream, extension);
            //读数据
            dPaperResponse = readWorkbook(wb);
        } finally {
            if (null != wb) {
                wb.close();
            }
            if (null != fis) {
                fis.close();
            }
        }
        return dPaperResponse;

    }

    /**
     * 存储redis
     */

    public ServiceResponse insertRedis(String paperCode, DPaperResponse dPaperResponse) {
        switchMode(paperCode);
        String hashKey = selectKey(paperCode);
        Long sequence;
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);

        List<DOption> dOptions = dPaperResponse.getdOptions();
        List<DAttachment> dAttachments = dPaperResponse.getdAttachments();
        List<DBlank> dBlanks = dPaperResponse.getdBlanks();
        String unitCode;
        int total = 0;
        for (DOption dOption : dOptions) {
            sequence = redisTemplate.boundValueOps(paperCode).increment(1);
            unitCode = String.format("%s-%s-%d", paperCode, "O", sequence);
            dOption.setUnicode(unitCode);
            hashOperations.put(unitCode, dOption);
            cacheItems(hashOperations, unitCode);
            total++;
        }
        for (DBlank dBlank : dBlanks) {
            sequence = redisTemplate.boundValueOps(paperCode).increment(1);
            unitCode = String.format("%s-%s-%d", paperCode, "B", sequence);
            dBlank.setUnicode(unitCode);
            hashOperations.put(unitCode, dBlank);
            cacheItems(hashOperations, unitCode);
            total++;
        }

        for (DAttachment dAttachment : dAttachments) {
            sequence = redisTemplate.boundValueOps(paperCode).increment(1);
            unitCode = String.format("%s-%s-%d", paperCode, "A", sequence);
            dAttachment.setUnicode(unitCode);
            hashOperations.put(unitCode, dAttachment);
            cacheItems(hashOperations, unitCode);
            total++;
        }

        return new ServiceResponse(total);
    }

    /**
     * 读取excel文件（同时支持2003和2007格式）
     *
     * @param fis       文件输入流
     * @param extension 文件名扩展名: xls 或 xlsx 不区分大小写
     * @return list中的map的key是列的序号
     * @throws Exception io异常等
     */
    private static Workbook read(InputStream fis, String extension) throws Exception {
        Workbook wb = null;
        List<Map<Integer, String>> list = null;
        try {

            if ("xls".equalsIgnoreCase(extension)) {
                wb = new HSSFWorkbook(fis);
            } else if ("xlsx".equalsIgnoreCase(extension)) {
                wb = new XSSFWorkbook(fis);
            } else {
                throw new WrappedException(BizEnums.NO_EXCEL);
            }
            return wb;
        } finally {
            if (null != wb) {
                wb.close();
            }
        }

    }

    /**
     * 获取单元格内容
     *
     * @param cell 单元格对象
     * @return 转化为字符串的单元格内容
     */
    private static String getCellContent(Cell cell) {
        return FORMATTER.formatCellValue(cell);
    }

    private DPaperResponse readWorkbook(Workbook wb) throws Exception {
        DPaperResponse dPaperResponse = new DPaperResponse();
        List<DOption> dOptions = new ArrayList<DOption>();
        List<DAttachment> dAttachments = new ArrayList<DAttachment>();
        List<DBlank> dBlanks = new ArrayList<DBlank>();
        for (int k = 0; k < wb.getNumberOfSheets(); k++) {
            Sheet sheet = wb.getSheetAt(k);
            int rows = sheet.getPhysicalNumberOfRows();
            if (sheet.getSheetName().equals("单选题")) {
                parseSingleChoice(dOptions, sheet, rows);
            }
            if (sheet.getSheetName().equals("多选题")) {
                parseMultipleChoice(dOptions, sheet, rows);
            }
            if (sheet.getSheetName().equals("客观填空题")) {
                parseObjectiveBlank(dBlanks, sheet, rows);
            }
            if (sheet.getSheetName().equals("主观填空题")) {
                parseSubjectiveBlank(dBlanks, sheet, rows);
            }
            if (sheet.getSheetName().equals("上传题")) {
                parseAttachment(dAttachments, sheet, rows);
            }
        }
        dPaperResponse.setdOptions(dOptions);
        dPaperResponse.setdBlanks(dBlanks);
        dPaperResponse.setdAttachments(dAttachments);
        return dPaperResponse;
    }

    /**
     * 单选题
     *
     * @param dOptions
     * @param sheet
     * @param rows
     * @return
     */
    private void parseSingleChoice(List<DOption> dOptions, Sheet sheet, int rows) {
        String optionMaskCode = "";
        String standardAnswer = "";
        DOption dOption = new DOption();
        boolean flag = false;
        for (int r = 1; r < rows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            if (getCellContent(row.getCell(SINGLE_CHOICE)).trim().equals("") && getCellContent(row.getCell(SINGLE_CHOICE_STEM)).trim().equals("")
                    && getCellContent(row.getCell(SINGLE_CHOICE_OPTION_LABEL)).trim().equals("")
                    && getCellContent(row.getCell(SINGLE_CHOICE_SCORE)).trim().equals("")
                    && getCellContent(row.getCell(SINGLE_CHOICE_ANSWER)).trim().equals("")
                    && getCellContent(row.getCell(SINGLE_CHOICE_OPTION_SEQ)).trim().equals("")
                    && getCellContent(row.getCell(SINGLE_CHOICE_OPTION_LABEL)).trim().equals("")) {
                continue;
            }

            if (getCellContent(row.getCell(SINGLE_CHOICE_STEM)).trim().indexOf("(示例)") != -1||getCellContent(row.getCell(SINGLE_CHOICE_STEM)).trim().indexOf("（示例）") != -1) {
                flag = true;
                continue;
            }
            if (getCellContent(row.getCell(SINGLE_CHOICE)).trim().indexOf("备注:") != -1 || getCellContent(row.getCell(SINGLE_CHOICE)).trim().indexOf("备注：") != -1) {
                break;
            }
            optionMaskCode = UUID.randomUUID().toString().substring(0, 12);
            if (getCellContent(row.getCell(SINGLE_CHOICE)).trim().equals("单选题")) {
                flag = false;
                dOption = new DOption();
                dOption.setType(DItemType.SINGLE_CHOICE.getValue());
                dOption.setVersionType(DVersionType.BIG_VERSION.getValue());
                dOptions.add(dOption);
                //设置选择题题干
                DOptionStemSetting optionStemSetting = new DOptionStemSetting();

                optionStemSetting.setQuestion(getCellContent(row.getCell(SINGLE_CHOICE_STEM)));
                //设置选择题选项
                List<DOptionItem> options = new ArrayList<>();
                DOptionItem dOptionItem = new DOptionItem();
                dOptionItem.setMaskCode(optionMaskCode);
                dOptionItem.setLabel(getCellContent(row.getCell(SINGLE_CHOICE_OPTION_LABEL)).trim());
                options.add(dOptionItem);
                optionStemSetting.setOptions(options);

                //设置选择题得分
                DOptionScoreSetting dOptionScoreSetting = new DOptionScoreSetting();
                BigDecimal score = null;
                if (NumberUtil.isNumber(getCellContent(row.getCell(SINGLE_CHOICE_SCORE)).trim())) {
                    //保留1位小数，但是不四舍五入
                    score = new BigDecimal(getCellContent(row.getCell(SINGLE_CHOICE_SCORE)).trim()).setScale(1, BigDecimal.ROUND_DOWN);
                }
                dOptionScoreSetting.setScore(score);

                //标准答案
                standardAnswer = getCellContent(row.getCell(SINGLE_CHOICE_ANSWER)).trim();

                //如果是标准答案 则为true
                if (standardAnswer.equals(getCellContent(row.getCell(SINGLE_CHOICE_OPTION_SEQ)).trim())) {
                    dOptionItem.setAnswer(true);
                } else {
                    dOptionItem.setAnswer(false);
                }

                dOption.setdOptionStemSetting(optionStemSetting);
                dOption.setdOptionScoreSetting(dOptionScoreSetting);
            }
            //循环添加选项
            if (!flag && getCellContent(row.getCell(SINGLE_CHOICE)).trim().equals("") && getCellContent(row.getCell(SINGLE_CHOICE_STEM)).trim().equals("") && row.getPhysicalNumberOfCells() == 6) {
                DOptionItem dOptionItem = new DOptionItem();
                dOptionItem.setLabel(getCellContent(row.getCell(SINGLE_CHOICE_OPTION_LABEL)).trim());
                dOptionItem.setMaskCode(optionMaskCode);
                String answer = getCellContent(row.getCell(SINGLE_CHOICE_OPTION_SEQ)).trim();
                if (standardAnswer.equals(answer)) {
                    dOptionItem.setAnswer(true);
                } else {
                    dOptionItem.setAnswer(false);
                }
                if (dOption.getdOptionStemSetting() != null) {
                    dOption.getdOptionStemSetting().getOptions().add(dOptionItem);
                }
            }
        }

    }

    /**
     * 多选题
     *
     * @param dOptions
     * @param sheet
     * @param rows
     */
    private void parseMultipleChoice(List<DOption> dOptions, Sheet sheet, int rows) {
        String optionMaskCode = "";
        List<String> answers = new ArrayList<>();
        DOption dOption = new DOption();
        boolean flag = false;
        for (int r = 1; r < rows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            if (getCellContent(row.getCell(MULTIPLE_CHOICE)).trim().equals("") && getCellContent(row.getCell(MULTIPLE_CHOICE_STEM)).trim().equals("")
                    && getCellContent(row.getCell(MULTIPLE_CHOICE_OPTION_LABEL)).trim().equals("")
                    && getCellContent(row.getCell(MULTIPLE_CHOICE_SCORE)).trim().equals("")
                    && getCellContent(row.getCell(MULTIPLE_CHOICE_ANSWER)).trim().equals("")
                    && getCellContent(row.getCell(MULTIPLE_CHOICE_OPTION_SEQ)).trim().equals("")
                    && getCellContent(row.getCell(MULTIPLE_CHOICE_OPTION_LABEL)).trim().equals("")) {
                continue;
            }
            if (getCellContent(row.getCell(MULTIPLE_CHOICE_STEM)).trim().indexOf("(示例)") != -1 || getCellContent(row.getCell(MULTIPLE_CHOICE_STEM)).trim().indexOf("（示例）") != -1) {
                flag = true;
                continue;
            }
            if (getCellContent(row.getCell(MULTIPLE_CHOICE)).trim().indexOf("备注:") != -1 || getCellContent(row.getCell(MULTIPLE_CHOICE)).trim().indexOf("备注：") != -1) {
                break;
            }
            optionMaskCode = UUID.randomUUID().toString().substring(0, 12);
            if (getCellContent(row.getCell(MULTIPLE_CHOICE)).trim().equals("多选题")) {
                flag = false;
                dOption = new DOption();
                dOption.setType(DItemType.MULTIPLE_CHOICE.getValue());
                dOption.setVersionType(DVersionType.BIG_VERSION.getValue());
                dOptions.add(dOption);
                //设置选择题题干
                DOptionStemSetting optionStemSetting = new DOptionStemSetting();
                optionStemSetting.setQuestion(getCellContent(row.getCell(MULTIPLE_CHOICE_STEM)));

                //设置选择题选项
                List<DOptionItem> options = new ArrayList<>();
                DOptionItem dOptionItem = new DOptionItem();
                dOptionItem.setMaskCode(optionMaskCode);
                dOptionItem.setLabel(getCellContent(row.getCell(MULTIPLE_CHOICE_OPTION_LABEL)).trim());
                options.add(dOptionItem);
                optionStemSetting.setOptions(options);

                //设置选择题得分
                String seq = getCellContent(row.getCell(MULTIPLE_CHOICE_OPTION_SEQ)).trim();
                answers = StringUtil.toStrListByComma(getCellContent(row.getCell(MULTIPLE_CHOICE_ANSWER)).trim());
                DOptionScoreSetting dOptionScoreSetting = new DOptionScoreSetting();
                BigDecimal score = null;
                if (NumberUtil.isNumber(getCellContent(row.getCell(MULTIPLE_CHOICE_SCORE)).trim())) {
                    //保留1位小数，但是不四舍五入
                    score = new BigDecimal(getCellContent(row.getCell(MULTIPLE_CHOICE_SCORE)).trim()).setScale(1, BigDecimal.ROUND_DOWN);
                }
                dOptionScoreSetting.setScore(score);
                if (answers.contains(seq)) {
                    dOptionItem.setAnswer(true);
                } else {
                    dOptionItem.setAnswer(false);
                }
                BigDecimal scoreLess = null;
                if (NumberUtil.isNumber(getCellContent(row.getCell(MULTIPLE_CHOICE_SCORE_LESS)).trim())) {
                    //保留1位小数，但是不四舍五入
                    scoreLess = new BigDecimal(getCellContent(row.getCell(MULTIPLE_CHOICE_SCORE_LESS)).trim()).setScale(1, BigDecimal.ROUND_DOWN);
                }
                if (getCellContent(row.getCell(MULTIPLE_CHOICE_SCORE_SET)).trim().equals("全部答对才给分")) {
                    dOptionScoreSetting.setScoreRule(TOptionScoreRule.ALL.getValue());
                } else if (getCellContent(row.getCell(MULTIPLE_CHOICE_SCORE_SET)).trim().equals("少选统一给分")) {
                    dOptionScoreSetting.setScoreRule(TOptionScoreRule.PART_UNITY.getValue());
                    if (scoreLess != null && scoreLess.compareTo(score) > 0) {
                        dOptionScoreSetting.setSubScore(null);
                    } else {
                        dOptionScoreSetting.setSubScore(scoreLess);
                    }
                } else if (getCellContent(row.getCell(MULTIPLE_CHOICE_SCORE_SET)).trim().equals("少选按比例给分")) {
                    dOptionScoreSetting.setScoreRule(TOptionScoreRule.PART_AVG.getValue());
                    dOptionScoreSetting.setSubScore(scoreLess);
                }
                dOption.setdOptionStemSetting(optionStemSetting);
                dOption.setdOptionScoreSetting(dOptionScoreSetting);
            }
            if (!flag && StringUtil.isEmpty(getCellContent(row.getCell(MULTIPLE_CHOICE)).trim()) && StringUtil.isEmpty(getCellContent(row.getCell(MULTIPLE_CHOICE_STEM)).trim()) && row.getPhysicalNumberOfCells() == 8) {
                DOptionItem dOptionItem = new DOptionItem();
                dOptionItem.setLabel(getCellContent(row.getCell(MULTIPLE_CHOICE_OPTION_LABEL)).trim());
                dOptionItem.setMaskCode(optionMaskCode);

                String seq = getCellContent(row.getCell(MULTIPLE_CHOICE_OPTION_SEQ)).trim();
                if (answers.contains(seq)) {
                    dOptionItem.setAnswer(true);
                } else {
                    dOptionItem.setAnswer(false);
                }
                if (dOption.getdOptionStemSetting() != null) {
                    dOption.getdOptionStemSetting().getOptions().add(dOptionItem);
                }
            }
        }

    }

    /**
     * 客观填空题
     *
     * @param dBlanks
     * @param sheet
     * @param rows
     */
    private void parseObjectiveBlank(List<DBlank> dBlanks, Sheet sheet, int rows) {
        DBlank dBlank = new DBlank();
        int seqNo = 1;
        boolean flag = false;
        for (int r = 1; r < rows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            if (getCellContent(row.getCell(OBJECTIVE_BLANK)).trim().equals("") && getCellContent(row.getCell(OBJECTIVE_BLANK_STEM)).trim().equals("")
                    && getCellContent(row.getCell(OBJECTIVE_BLANK_OPTION_SEQ)).trim().equals("")
                    && getCellContent(row.getCell(OBJECTIVE_BLANK_SCORE)).trim().equals("")
                    && getCellContent(row.getCell(OBJECTIVE_BLANK_ANSWER)).trim().equals("")
                    && getCellContent(row.getCell(OBJECTIVE_BLANK_EXPLANATION)).trim().equals("")
                    && getCellContent(row.getCell(OBJECTIVE_BLANK_SCORE_RULE)).trim().equals("")) {
                continue;
            }
            if (getCellContent(row.getCell(OBJECTIVE_BLANK_STEM)).trim().indexOf("(示例)") != -1 || getCellContent(row.getCell(OBJECTIVE_BLANK_STEM)).trim().indexOf("（示例）") != -1) {
                flag = true;
                continue;
            }
            if (getCellContent(row.getCell(OBJECTIVE_BLANK)).trim().indexOf("备注:") != -1 || getCellContent(row.getCell(OBJECTIVE_BLANK)).trim().indexOf("备注：") != -1) {
                break;
            }
            if (getCellContent(row.getCell(OBJECTIVE_BLANK)).trim().trim().equals("客观填空题")) {
                flag = false;
                dBlank = new DBlank();
                dBlank.setType(DItemType.BLANK.getValue());
                dBlank.setVersionType(DVersionType.BIG_VERSION.getValue());
                dBlanks.add(dBlank);
                //设置填空题题干
                DBlankStemSetting dBlankStemSetting = new DBlankStemSetting();
                dBlankStemSetting.setQuestion(getCellContent(row.getCell(OBJECTIVE_BLANK_STEM)).trim());
                dBlankStemSetting.setType(TBlankType.OBJECTIVE.getValue());

                //设置填空题选项
                DBlankScoreDetail dBlankScoreDetail = new DBlankScoreDetail();
                seqNo = 1;
                dBlankScoreDetail.setSeqNo(Integer.parseInt(getCellContent(row.getCell(OBJECTIVE_BLANK_OPTION_SEQ)).trim().equals("") ? seqNo + "" : getCellContent(row.getCell(OBJECTIVE_BLANK_OPTION_SEQ)).trim()));
                BigDecimal score = null;
                if (NumberUtil.isNumber(getCellContent(row.getCell(OBJECTIVE_BLANK_SCORE)).trim())) {
                    //保留1位小数，但是不四舍五入
                    score = new BigDecimal(getCellContent(row.getCell(OBJECTIVE_BLANK_SCORE)).trim()).setScale(1, BigDecimal.ROUND_DOWN);
                }
                dBlankScoreDetail.setScore(score);
                dBlankScoreDetail.setAnswer(getCellContent(row.getCell(OBJECTIVE_BLANK_ANSWER)).trim());
                List<DBlankScoreDetail> dBlankScoreDetails = new ArrayList<>();
                dBlankScoreDetails.add(dBlankScoreDetail);
                DBlankScoreSetting dBlankScoreSetting = new DBlankScoreSetting();
                dBlankScoreSetting.setdBlankScoreDetails(dBlankScoreDetails);
                dBlankScoreSetting.setExplanation(getCellContent(row.getCell(OBJECTIVE_BLANK_EXPLANATION)).trim());
                if (getCellContent(row.getCell(OBJECTIVE_BLANK_SCORE_RULE)).trim().equals("完全一致")) {
                    dBlankScoreSetting.setScoreRule(TBlankScoreRule.IN_FULL_ACCORD.getValue());
                } else {
                    dBlankScoreSetting.setScoreRule(TBlankScoreRule.SEQUENTIAL_INCONSISTENCY.getValue());
                }
                dBlank.setdBlankStemSetting(dBlankStemSetting);
                dBlank.setdBlankScoreSetting(dBlankScoreSetting);
            }

            if (!flag && StringUtil.isEmpty(getCellContent(row.getCell(OBJECTIVE_BLANK)).trim()) && StringUtil.isEmpty(getCellContent(row.getCell(OBJECTIVE_BLANK_STEM)).trim()) && row.getPhysicalNumberOfCells() == 8) {
                seqNo++;
                DBlankScoreDetail dBlankScoreDetail = new DBlankScoreDetail();
                dBlankScoreDetail.setSeqNo(Integer.parseInt(getCellContent(row.getCell(SUBJECTIVE_BLANK_OPTION_SEQ)).trim().equals("") ? seqNo + "" : getCellContent(row.getCell(SUBJECTIVE_BLANK_OPTION_SEQ)).trim()));
                BigDecimal score = null;
                if (NumberUtil.isNumber(getCellContent(row.getCell(OBJECTIVE_BLANK_SCORE)).trim())) {
                    //保留1位小数，但是不四舍五入
                    score = new BigDecimal(getCellContent(row.getCell(OBJECTIVE_BLANK_SCORE)).trim()).setScale(1, BigDecimal.ROUND_DOWN);
                }
                dBlankScoreDetail.setScore(score);
                dBlankScoreDetail.setAnswer(getCellContent(row.getCell(OBJECTIVE_BLANK_ANSWER)));
                dBlank.getdBlankScoreSetting().getdBlankScoreDetails().add(dBlankScoreDetail);
            }
            if (dBlank.getdBlankStemSetting() != null) {
                dBlank.getdBlankStemSetting().setNumbers(dBlank.getdBlankScoreSetting().getdBlankScoreDetails().size());
            }
        }

    }

    /**
     * 主观填空题
     *
     * @param dBlanks
     * @param sheet
     * @param rows
     */
    private void parseSubjectiveBlank(List<DBlank> dBlanks, Sheet sheet, int rows) {
        DBlank dBlank = new DBlank();
        boolean flag = false;
        for (int r = 1; r < rows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            if (getCellContent(row.getCell(SUBJECTIVE_BLANK)).trim().equals("") && getCellContent(row.getCell(SUBJECTIVE_BLANK_STEM)).trim().equals("")
                    && getCellContent(row.getCell(SUBJECTIVE_BLANK_OPTION_SEQ)).trim().equals("")
                    && getCellContent(row.getCell(SUBJECTIVE_BLANK_SCORE)).trim().equals("")
                    && getCellContent(row.getCell(SUBJECTIVE_BLANK_ANSWER)).trim().equals("")
                    && getCellContent(row.getCell(SUBJECTIVE_BLANK_EXPLANATION)).trim().equals("")
                    && getCellContent(row.getCell(SUBJECTIVE_BLANK_SCORE_RULE)).trim().equals("")) {
                continue;
            }
            if (getCellContent(row.getCell(SUBJECTIVE_BLANK_STEM)).indexOf("(示例)") != -1 || getCellContent(row.getCell(SUBJECTIVE_BLANK_STEM)).indexOf("（示例）") != -1) {
                flag = true;
                continue;
            }
            if (getCellContent(row.getCell(SUBJECTIVE_BLANK)).trim().indexOf("备注:") != -1 || getCellContent(row.getCell(SUBJECTIVE_BLANK)).trim().indexOf("备注：") != -1) {
                break;
            }
            if (getCellContent(row.getCell(SUBJECTIVE_BLANK)).trim().equals("主观填空题")) {
                flag = false;
                dBlank = new DBlank();
                dBlank.setType(DItemType.BLANK.getValue());
                dBlanks.add(dBlank);
                //设置填空题题干
                DBlankStemSetting dBlankStemSetting = new DBlankStemSetting();
                dBlankStemSetting.setQuestion(getCellContent(row.getCell(SUBJECTIVE_BLANK_STEM)));
                dBlankStemSetting.setType(TBlankType.SUBJECTIVE.getValue());

                //设置填空题选项
                DBlankScoreDetail dBlankScoreDetail = new DBlankScoreDetail();
                dBlankScoreDetail.setSeqNo(Integer.parseInt(getCellContent(row.getCell(SUBJECTIVE_BLANK_OPTION_SEQ)).trim().equals("") ? "0" : getCellContent(row.getCell(SUBJECTIVE_BLANK_OPTION_SEQ)).trim()));
                BigDecimal score = null;
                if (NumberUtil.isNumber(getCellContent(row.getCell(SUBJECTIVE_BLANK_SCORE)).trim())) {
                    //保留1位小数，但是不四舍五入
                    score = new BigDecimal(getCellContent(row.getCell(SUBJECTIVE_BLANK_SCORE))).setScale(1, BigDecimal.ROUND_DOWN);
                }
                dBlankScoreDetail.setScore(score);
                dBlankScoreDetail.setAnswer(getCellContent(row.getCell(SUBJECTIVE_BLANK_ANSWER)));
                List<DBlankScoreDetail> dBlankScoreDetails = new ArrayList<>();
                dBlankScoreDetails.add(dBlankScoreDetail);
                DBlankScoreSetting dBlankScoreSetting = new DBlankScoreSetting();
                dBlankScoreSetting.setdBlankScoreDetails(dBlankScoreDetails);
                dBlankScoreSetting.setExplanation(getCellContent(row.getCell(SUBJECTIVE_BLANK_EXPLANATION)));
                if (getCellContent(row.getCell(SUBJECTIVE_BLANK_SCORE_RULE)).trim().equals("仅供参考"))
                    dBlankScoreSetting.setScoreRule(TBlankScoreRule.FOR_REFERENCE_ONLY.getValue());
                dBlank.setdBlankStemSetting(dBlankStemSetting);
                dBlank.setdBlankScoreSetting(dBlankScoreSetting);
            }

            if (!flag && StringUtil.isEmpty(getCellContent(row.getCell(SUBJECTIVE_BLANK)).trim()) && StringUtil.isEmpty(getCellContent(row.getCell(SUBJECTIVE_BLANK_STEM)).trim()) && row.getPhysicalNumberOfCells() == 8) {
                DBlankScoreDetail dBlankScoreDetail = new DBlankScoreDetail();
                dBlankScoreDetail.setSeqNo(Integer.parseInt(getCellContent(row.getCell(SUBJECTIVE_BLANK_OPTION_SEQ)).trim().equals("") ? "0" : getCellContent(row.getCell(SUBJECTIVE_BLANK_OPTION_SEQ)).trim()));
                BigDecimal score = null;
                if (NumberUtil.isNumber(getCellContent(row.getCell(SUBJECTIVE_BLANK_SCORE)).trim())) {
                    //保留1位小数，但是不四舍五入
                    score = new BigDecimal(getCellContent(row.getCell(SUBJECTIVE_BLANK_SCORE))).setScale(1, BigDecimal.ROUND_DOWN);
                }
                dBlankScoreDetail.setScore(score);
                dBlankScoreDetail.setAnswer(getCellContent(row.getCell(SUBJECTIVE_BLANK_ANSWER)));
                dBlank.getdBlankScoreSetting().getdBlankScoreDetails().add(dBlankScoreDetail);
            }
            if (dBlank.getdBlankStemSetting() != null) {
                dBlank.getdBlankStemSetting().setNumbers(dBlank.getdBlankScoreSetting().getdBlankScoreDetails().size());
            }
        }
    }

    /**
     * 上传题
     *
     * @param dAttachments
     * @param sheet
     * @param rows
     */
    private void parseAttachment(List<DAttachment> dAttachments, Sheet sheet, int rows) {
        for (int r = 1; r < rows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            if (getCellContent(row.getCell(ATTACHMENT)).trim().equals("") && getCellContent(row.getCell(ATTACHMENT_STEM)).trim().equals("")
                    && getCellContent(row.getCell(ATTACHMENT_SCORE)).trim().equals("")) {
                continue;
            }
            if (getCellContent(row.getCell(ATTACHMENT_STEM)).trim().indexOf("(示例)") != -1 || getCellContent(row.getCell(ATTACHMENT_STEM)).trim().indexOf("（示例）") != -1) {
                continue;
            }
            if (getCellContent(row.getCell(ATTACHMENT)).trim().indexOf("备注:") != -1 || getCellContent(row.getCell(ATTACHMENT)).trim().indexOf("备注：") != -1) {
                break;
            }
            if (getCellContent(row.getCell(ATTACHMENT)).trim().equals("上传题")) {
                DAttachment dAttachment = new DAttachment();
                dAttachment.setType(DItemType.ATTACHMENT.getValue());
                //设置上传题题干
                DAttachmentStemSetting dAttachmentStemSetting = new DAttachmentStemSetting();
                dAttachmentStemSetting.setQuestion(getCellContent(row.getCell(ATTACHMENT_STEM)));
                dAttachmentStemSetting.setType(TAttachmentType.PIC.getValue());
                //设置上传题分数
                DAttachmentScoreSetting dAttachmentScoreSetting = new DAttachmentScoreSetting();
                BigDecimal score = StringUtil.isEmpty(getCellContent(row.getCell(ATTACHMENT_SCORE)).trim()) ? null : new BigDecimal(getCellContent(row.getCell(ATTACHMENT_SCORE)));
                dAttachmentScoreSetting.setScore(score);
                dAttachment.setdAttachmentStemSetting(dAttachmentStemSetting);
                dAttachment.setdAttachmentScoreSetting(dAttachmentScoreSetting);
                dAttachments.add(dAttachment);
            }
        }

    }
}
