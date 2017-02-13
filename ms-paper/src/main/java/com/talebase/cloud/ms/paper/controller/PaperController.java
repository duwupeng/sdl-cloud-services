package com.talebase.cloud.ms.paper.controller;

import com.talebase.cloud.base.ms.paper.domain.TPaper;
import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.base.ms.paper.enums.DPaperMode;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.PermissionEnum;
import com.talebase.cloud.ms.paper.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 */
@RestController
public class PaperController {

    @Autowired
    PaperService paperService;


    /**
     * 保存试卷基本信息(除组卷字段以外的其他信息)
     *
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/question/paper/header")
    public ServiceResponse savePaperHeader(@RequestBody ServiceRequest<DPaper> req) throws Exception {
        return new ServiceResponse(paperService.savePaperHeader(req.getRequestHeader(), req.getRequest()));
    }

    /**
     * 添加整张试卷
     *
     * @param hashKey
     * @param stepNo
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/question/paper/{hashKey}/{stepNo}")
    public ServiceResponse flush(@PathVariable("hashKey") String hashKey,
                                 @PathVariable("stepNo") Integer stepNo,
                                 @RequestBody ServiceRequest<String> req) throws Exception {
//        paperService.accemblyAndFlush(req.getRequestHeader(), req.getRequest());
        return paperService.flush(req.getRequestHeader(), hashKey, stepNo);
    }

    /**
     * 考试库试卷列表
     *
     * @param req
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @PostMapping(value = "/question/papers")
    public ServiceResponse<PageResponse<DPaper>> query(@RequestBody ServiceRequest<DPaperQuery> req) throws InvocationTargetException, IllegalAccessException {
        return paperService.queryList(req.getRequestHeader(), req.getRequest(), req.getPageReq());
    }

    /**
     * 通过ids查询试卷列表（供项目使用，引用试卷）
     *
     * @param req
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @PostMapping(value = "/question/papers/byIds")
    public ServiceResponse<PageResponse<DPaper>> queryByIds(@RequestBody ServiceRequest<List<Integer>> req) throws InvocationTargetException, IllegalAccessException {
        return paperService.queryListByIds(req.getRequestHeader(), req.getRequest(), req.getPageReq());
    }

    /**
     * 修改试卷
     *
     * @param req
     * @return
     * @throws Exception
     */
    @PutMapping(value = "/question/paper")
    public ServiceResponse<Integer> update(@RequestBody ServiceRequest<DPaper> req) throws Exception {
        TPaper tPaper = paperService.getSimpleById(req.getRequest().getId());
        if (req.getRequestHeader().getOrgCode() != PermissionEnum.c99_3.name()) {
            if (!tPaper.getCreator().equals(req.getRequestHeader().getOperatorName())) {
                return new ServiceResponse(BizEnums.NO_PERMISSION_OPERATION_PAPER, true);
            }
        }
        return paperService.update(req.getRequestHeader(), req.getRequest());
    }

    /**
     * 通过paperId修改试卷状态
     *
     * @param id
     * @param req
     * @return
     * @throws Exception
     */
    @PutMapping(value = "/question/paper/{id}")
    public ServiceResponse<Integer> updateStatusByUnicode(@PathVariable("id") Integer id, @RequestBody ServiceRequest<Boolean> req) throws Exception {
        TPaper tPaper = paperService.getSimpleById(id);
        if (req.getRequestHeader().getOrgCode() != PermissionEnum.c99_3.name()) {
            if (!tPaper.getCreator().equals(req.getRequestHeader().getOperatorName())) {
                return new ServiceResponse(BizEnums.NO_PERMISSION_OPERATION_PAPER, true);
            }
        }
        return paperService.updateStatusByUnicode(tPaper.getUnicode(), req.getRequest());
    }

    /**
     * 通过paperId修改mode模式
     *
     * @param id
     * @param req
     * @return
     * @throws Exception
     */
    @PutMapping(value = "/question/paper/mode/{id}/{status}")
    public ServiceResponse<Integer> updateMode(@PathVariable("id") int id,@PathVariable("status") boolean status, @RequestBody ServiceRequest<Integer> req) throws Exception {
        return paperService.updateMode(id, req.getRequest(),status);
    }

    /**
     * 通过idList修改发送次数(+1)
     *
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/question/paper/times")
    public ServiceResponse<Integer> updateAddTimes(@RequestBody ServiceRequest<List<Integer>> req) throws Exception {
        return paperService.updateAddTimes(req.getRequest());
    }

    /**
     * 通过idList修改发送次数(-1)
     *
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/question/paper/minusTimes")
    public ServiceResponse<Integer> updateMinusTimes(@RequestBody ServiceRequest<List<Integer>> req) throws Exception {
        return paperService.updateMinusTimes(req.getRequest());
    }

    /**
     * 删除试卷
     *
     * @param id
     * @param req
     * @return
     * @throws Exception
     */
    @DeleteMapping(value = "/question/paper/{id}")
    public ServiceResponse<Integer> deleteByUnicode(@PathVariable("id") Integer id, @RequestBody ServiceRequest req) throws Exception {
        TPaper tPaper = paperService.getSimpleById(id);
        if (req.getRequestHeader().getOrgCode() != PermissionEnum.c99_4.name()) {
            if (!tPaper.getCreator().equals(req.getRequestHeader().getOperatorName())) {
                return new ServiceResponse(BizEnums.NO_PERMISSION_OPERATION_PAPER, true);
            }
        }
        if (tPaper.getUsage() != null && tPaper.getUsage() > 0) {
            return new ServiceResponse<>(BizEnums.NO_DELETE, true);
        }
        return paperService.deleteByUnicode(tPaper.getUnicode(), req.getRequestHeader());
    }

    /**
     * 通过id查询整张试卷 考试模块（发卷）
     *
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/question/paper/{id}")
    public ServiceResponse<DWPaperResponse> queryById(@PathVariable("id") Integer id) throws Exception {
        return paperService.getById(id);
    }

    /**
     * 导出
     *
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/question/paper/forExport/{id}")
    public ServiceResponse<DPaperResponse> getPaperForExport(@PathVariable("id") Integer id) throws Exception {
        return paperService.getPaperForExport(id);
    }

    /**
     * 通过一组unicode查询试卷
     *
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/question/paper/byUnicode")
    public ServiceResponse<TPaper> queryByUnicode(@RequestBody ServiceRequest<List<String>> req) throws Exception {
        return paperService.queryPaperByUnicodes(req.getRequest());
    }

    /**
     * 通过paperId查询试卷基本信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/question/paper/simple/{id}")
    public ServiceResponse<TPaper> querySimpleById(@PathVariable("id") Integer id) throws Exception {
        return new ServiceResponse<>(paperService.getSimpleById(id));
    }

    /**
     * 通过ids查询试卷集合(项目中使用)
     *
     * @param serviceRequest
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/question/papers/query")
    public ServiceResponse<List<TPaper>> queryPapersByIds(@RequestBody ServiceRequest<List<Integer>> serviceRequest) throws Exception {
        return new ServiceResponse<>(paperService.getSimpleByIds(serviceRequest.getRequest()));
    }

    /**
     * 复制试卷
     *
     * @param paperId
     * @param serviceRequest
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/question/paper/copy/{paperId}/{newPaperUnicode}")
    public ServiceResponse<Integer> copy(@PathVariable("paperId") Integer paperId,@PathVariable("newPaperUnicode") String newPaperUnicode, @RequestBody ServiceRequest<String> serviceRequest) throws Exception {
        TPaper tPaper = paperService.getSimpleById(paperId);
        if (tPaper.getMode() == DPaperMode.修改中.getValue()) {
            return new ServiceResponse(BizEnums.PAPER_MODE_MODIFICATION, true);
        }
        return paperService.copy(paperId,newPaperUnicode, serviceRequest.getRequest(), serviceRequest.getRequestHeader().getOperatorName());
    }

    /**
     * 检验试卷名是否重复
     *
     * @param paperId
     * @param serviceRequest
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/question/paper/checkName/{paperId}")
    public ServiceResponse<String> checkName(@PathVariable("paperId") Integer paperId, @RequestBody ServiceRequest<String> serviceRequest) throws Exception {
        return paperService.checkName(paperId, serviceRequest.getRequest(), serviceRequest.getRequestHeader().getCompanyId());
    }

    /**
     * 试卷预览（考试库-试卷列表）
     */
    @GetMapping(value = "/question/paper/preview/{paperUnicode}")
    public ServiceResponse<DWPaper3> preview(@PathVariable("paperUnicode") String paperUnicode) throws Exception {
        ServiceResponse<DWPaper3> serviceResponse = paperService.previewByUnicode(paperUnicode);
        return serviceResponse;
    }
}
