package com.talebase.cloud.os.paper.controller;

import com.google.gson.reflect.TypeToken;
import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.base.ms.paper.enums.DPaperMode;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.CommonParams;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.SequenceUtil;
import com.talebase.cloud.os.paper.service.PaperExportService;
import com.talebase.cloud.os.paper.service.PaperImportService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by eric.du on 2016-12-7.
 */
@RestController
public class PaperManagementController extends PaperCache {

    @Autowired
    PaperImportService paperImportService;
    @Autowired
    PaperExportService paperExportService;

    /**
     * 刷新题目到DB
     *
     * @return ServiceResponse
     */
    @PostMapping(value = "/question/flush/{paperUnitCode}/{stepNo}")
    public ServiceResponse flush(@PathVariable String paperUnitCode, @PathVariable int stepNo, String jsonStr) {
        if (!StringUtils.isEmpty(jsonStr) && stepNo == 4){
            List<DPaperRemark> dPaperRemarks = GsonUtil.fromJson(jsonStr, new TypeToken<List<DPaperRemark>>() {
            }.getType());
            for (int i=0;i<dPaperRemarks.size();i++){
                DPaperRemark dPaperRemark = dPaperRemarks.get(i);
                int len = dPaperRemark.getDescription().length();
                if (len > CommonParams.TEXTLENGTH){
                    throw new WrappedException(BizEnums.TEXT_BEYOUND_LENGTH);
                }
            }

        }
        String hashKey = selectKey(paperUnitCode);
        // 刷新试卷到缓存
        if (jsonStr != null) {
            cachePaper(hashKey, stepNo, paperUnitCode, jsonStr);
        }

        // 刷新DB,更新试卷， 并且设置设置试卷为完成状态
        ServiceResponse response = paperService.flushPaper(hashKey, stepNo);

        int paperId = (Integer) response.getResponse();
        if (paperId != 0) {
            //切换key
            if (DPaperMode.新建中.name().equals(redisTemplate.opsForValue().get(paperUnitCode + "-mode"))) {
                // 更改卷key为modify
                redisTemplate.rename(paperUnitCode + "-new", paperUnitCode + "-modify");
            }

            // 设置试卷状态为完成
            redisTemplate.opsForValue().set(paperUnitCode + "-mode", DPaperMode.完成);

            changeCachedDPaper(paperUnitCode, paperId);
            paperService.updateMode(paperId, DPaperMode.完成, true);
        }
        return response;
    }

    private void changeCachedDPaper(String paperUnitCode, int paperId) {
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(paperUnitCode + "-modify");
        DPaper dPaper = (DPaper) hashOperations.get(String.format("%s-%s-%d", paperUnitCode, "H", 1));
        dPaper.setId(paperId);
        dPaper.setMode(DPaperMode.完成.getValue());
        dPaper.setStatus(true);
        hashOperations.put(String.format("%s-%s-%d", paperUnitCode, "H", 1), dPaper);
    }

    /**
     * 根据所处页面的步数，决定更新卷首，卷体还是卷尾
     *
     * @param stepNo
     * @param paperUnitCode
     * @param jsonStr
     * @return
     */
    private String cachePaper(String hashKey, int stepNo, String paperUnitCode, String jsonStr) {
        //刷新缓存,缓存头部
        if (stepNo == 1) {
            BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);
            DPaper dPaper = GsonUtil.fromJson(jsonStr, DPaper.class);
            hashOperations.put(String.format("%s-%s-%d", paperUnitCode, "H", 1), dPaper);
            //刷新缓存,缓存卷分
        } else if (stepNo == 3) {
            updateScores(hashKey, jsonStr);
            //刷新缓存,缓存卷尾
        } else if (stepNo == 4) {
            cacheRemarks(paperUnitCode, jsonStr, selectKey(paperUnitCode));
        }
        return hashKey;
    }

    /**
     * 复制试卷
     *
     * @return
     */
    @PostMapping(value = "/question/paper/copy/{paperId}")
    public ServiceResponse copy(@PathVariable("paperId") Integer paperId, String paperUnicode, String newName) throws InvocationTargetException, IllegalAccessException {
        String hashKey = selectKey(paperUnicode);
        String newPaperUnicode = SequenceUtil.generateSequenceNo("P");
        redisTemplate.opsForValue().set(newPaperUnicode, redisTemplate.opsForValue().get(paperUnicode));
        Integer newPaperId = paperService.copy(paperId, newPaperUnicode, newName);
        // 更改卷key为modify
        redisTemplate.opsForValue().set(newPaperUnicode + "-mode", redisTemplate.opsForValue().get(paperUnicode + "-mode"));

        String newHashKey = selectKey(newPaperUnicode);

        HashOperations hashOperations = redisTemplate.boundHashOps(newHashKey).getOperations().opsForHash();
        Set<String> boundHashOperations = redisTemplate.boundHashOps(hashKey).keys();
        Iterator it = boundHashOperations.iterator();
        String key;
        while (it.hasNext()) {
            key = (String) it.next();
            hashOperations.put(newHashKey, key, redisTemplate.boundHashOps(hashKey).get(key));
        }
        BoundHashOperations<String, String, Object> newHashOperations = redisTemplate.boundHashOps(newHashKey);
        DPaper dPaper = (DPaper) newHashOperations.get(paperUnicode + "-H-1");//旧试卷
        dPaper.setId(newPaperId);
        dPaper.setUnicode(newPaperUnicode.split("-")[0]);
        dPaper.setName(newName);
        String unitCode = newPaperUnicode.split("-")[0] + "-H-1";
        newHashOperations.delete(paperUnicode + "-H-1");
        newHashOperations.put(unitCode, dPaper);

        return new ServiceResponse();
    }

    public String paperCreate() {
        //生成试卷内容计序器
        String uniCode = SequenceUtil.generateSequenceNo("P");
        redisTemplate.opsForValue().set(uniCode, 0);
        redisTemplate.opsForValue().set(uniCode + "-mode", DPaperMode.新建中);

        List<String> list = new ArrayList<>();

        String hashKey = uniCode + "-new";
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);
        hashOperations.getOperations().opsForHash().put(hashKey, "sequences", list);

        return uniCode;
    }

    /**
     * 检查试卷名称是否重复
     *
     * @return
     */
    @PostMapping(value = "/question/paper/checkName/{paperId}")
    public ServiceResponse checkName(@PathVariable("paperId") Integer paperId, String newName) throws InvocationTargetException, IllegalAccessException {
        return paperService.checkName(paperId, newName);
    }

    /**
     * 试卷状态变更
     *
     * @param paperId
     * @return
     */
    @PutMapping(value = "/question/paper/status/{paperId}")
    public ServiceResponse paperEnableOrDisable(@PathVariable("paperId") Integer paperId, boolean status) {
        return paperService.updateStatus(paperId, status);
    }

    /**
     * 试卷删除
     *
     * @param paperId
     * @return
     */
    @PostMapping(value = "/question/paper/del/{id}")
    public ServiceResponse paperDelete(@PathVariable("id") Integer paperId) {
        paperService.delete(paperId);
        return new ServiceResponse();
    }

    /**
     * 试卷模式
     *
     * @param unicode
     * @return
     */
    @GetMapping(value = "/question/paper/mode/{unicode}")
    public ServiceResponse paperModeQuery(@PathVariable("unicode") String unicode) {
        if (!redisTemplate.opsForValue().get(unicode + "-mode").equals(DPaperMode.完成.name())) {
            return new ServiceResponse(BizEnums.PAPER_MODE_MODIFICATION, true);
        }
        return new ServiceResponse();
    }

    /**
     * 试卷导入
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/question/paper/import/{paperCode}", method = RequestMethod.POST)
    public ServiceResponse importExcel(@PathVariable("paperCode") String paperCode, MultipartHttpServletRequest req) throws Exception {
        MultipartFile file = req.getFile("importQues");
        ServiceResponse serviceResponse = new ServiceResponse();
        if (!file.isEmpty()) {
            InputStream InputStream = file.getInputStream();
            DPaperResponse dPaperResponse = paperImportService.readExcel(InputStream, file.getOriginalFilename());
            serviceResponse = paperImportService.insertRedis(paperCode, dPaperResponse);
        }
        return serviceResponse;
    }

    /**
     * 试卷导出
     *
     * @param paperId
     * @return
     */
    @GetMapping("/question/paper/export/{paperId}")
    public ServiceResponse export(HttpServletResponse response, @PathVariable("paperId") Integer paperId) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        String fileName = paperExportService.export(workbook, paperId);
        OutputStream output = response.getOutputStream();
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("utf8"), "iso-8859-1"));
        response.setContentType("application/octet-stream; charset=utf-8");
        workbook.write(output);
        output.close();
        return new ServiceResponse();
    }

}
