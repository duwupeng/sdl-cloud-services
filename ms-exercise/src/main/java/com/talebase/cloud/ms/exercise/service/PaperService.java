package com.talebase.cloud.ms.exercise.service;

import com.google.gson.reflect.TypeToken;
import com.talebase.cloud.base.ms.examer.domain.TExercise;
import com.talebase.cloud.base.ms.examer.dto.DAnswerScoreDetail;
import com.talebase.cloud.base.ms.examer.dto.DExerciseRequest;
import com.talebase.cloud.base.ms.examer.dto.DExerciseResponse;
import com.talebase.cloud.base.ms.examer.dto.DTimerExercise;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.BeanConverter;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.TimeUtil;
import com.talebase.cloud.ms.exercise.dao.PaperMapper;
import com.talebase.cloud.ms.exercise.dao.TaskProgressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by erid.du on 2016-12-7.
 */
@Service
public class PaperService {

    @Autowired
    private PaperMapper paperMapper;
    @Autowired
    private TaskProgressMapper taskProgressMapper;

    /**
     * 保存答题
     *
     * @param
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = false)
    public ServiceResponse examSubmit(TExercise req) throws Exception {

        TExercise tExercise = paperMapper.queryByTaskIdAndUserId(req.getTaskId(), req.getUserId());
        if(tExercise == null){
            throw new WrappedException(BizEnums.NoExerciseRecord);
        }
        if(tExercise.getEndTime() != null)
            throw new WrappedException(BizEnums.SubmitRepeatErr);

//        paperMapper.insert(tExercise);
        req.setId(tExercise.getId());
        paperMapper.updateToSubmit(req);
        int markedNum = req.getSubScore() == null ? 0 : 1;//若直接包含主观题分数，则表示无主观题，直接出全卷成绩
        taskProgressMapper.updateAdd(tExercise.getTaskId(), 0, 0, 1, markedNum);
        return new ServiceResponse();
    }

    /**
     * 查询答题明细
     *
     * @param
     * @return
     * @throws Exception
     */
    public ServiceResponse<DExerciseResponse> examDetail(Integer exerciseId) throws InvocationTargetException, IllegalAccessException {
        TExercise tExercise = paperMapper.query(exerciseId);
        DExerciseResponse dExerciseResponse = changeTExerciseToDExercise(tExercise);
        return new ServiceResponse<DExerciseResponse>(dExerciseResponse);
    }

    @Transactional(readOnly = false)
    public ServiceResponse updateSubScore(Integer id, BigDecimal subScore) throws Exception {
        TExercise tExercise = paperMapper.query(id);
        if(tExercise.getSubScore() != null){
            throw new WrappedException(BizEnums.SetSubScoreRepeatErr);
        }

        TExercise updateReq = new TExercise();
        updateReq.setId(id);
        updateReq.setSubScore(subScore);
        paperMapper.update(updateReq);
        taskProgressMapper.updateAdd(tExercise.getTaskId(), 0, 0, 0, 1);
        return new ServiceResponse();
    }

//    @Transactional(readOnly = false)
//    public ServiceResponse update(DExerciseRequest dExerciseRequest) throws Exception {
//        TExercise tExercise = changeDExerciseToTExercise(dExerciseRequest);
//        paperMapper.update(tExercise);
//        return new ServiceResponse();
//    }

    public ServiceResponse<BigDecimal> getTotalScore(Integer projectId, Integer taskId, Integer userId) {
        BigDecimal totalScore = paperMapper.getTotalScore(projectId, taskId, userId);
        return new ServiceResponse(totalScore);
    }

    private TExercise changeDExerciseToTExercise(DExerciseRequest dExerciseRequest) throws InvocationTargetException, IllegalAccessException {
        TExercise tExercise = new TExercise();
        BeanConverter.copyProperties(tExercise, dExerciseRequest);
        if (dExerciseRequest.getStartTime() != null) {
            Long time = TimeUtil.tempDateSecond(dExerciseRequest.getStartTime()).getTime();
            tExercise.setStartTime(new Timestamp(time));
        }
        if (dExerciseRequest.getEndTime() != null) {
            Long time = TimeUtil.tempDateSecond(dExerciseRequest.getEndTime()).getTime();
            tExercise.setEndTime(new Timestamp(time));
        }
        tExercise.setAnswerScoreDetail(GsonUtil.toJson(dExerciseRequest.getdAnswerScoreDetails()));
        return tExercise;
    }

    private DExerciseResponse changeTExerciseToDExercise(TExercise tExercise) throws InvocationTargetException, IllegalAccessException {
        DExerciseResponse dExerciseResponse = new DExerciseResponse();
        BeanConverter.copyProperties(dExerciseResponse, tExercise);
        if (tExercise.getStartTime() != null) {
            dExerciseResponse.setStartTime(tExercise.getStartTime().getTime());
        }
        if (tExercise.getEndTime() != null) {
            dExerciseResponse.setEndTime(tExercise.getEndTime().getTime());
        }
        List<DAnswerScoreDetail> dAnswerScoreDetails = GsonUtil.fromJson(tExercise.getAnswerScoreDetail(), new TypeToken<List<DAnswerScoreDetail>>() {
        }.getType());
        dExerciseResponse.setAnswerScoreDetail(dAnswerScoreDetails);
        return dExerciseResponse;
    }


    /**
     * 开始答题
     *
     * @param
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = false)
    public Integer examStart(TExercise tExercise) throws Exception {

        Integer cnt = paperMapper.cntExists(tExercise.getTaskId(), tExercise.getUserId());
        if(cnt > 0){
            throw new WrappedException(BizEnums.ReStartExerciseErr);
        }

        Integer exerciseId = paperMapper.insert(tExercise);
        taskProgressMapper.updateAdd(tExercise.getTaskId(), 0, 1, 0, 0);
        return exerciseId;
    }

    public ServiceResponse<List<DTimerExercise>> getExerciseByTimer(Integer limit,Integer delaySecond){
        List<DTimerExercise> list = paperMapper.getExerciseByTimer(limit,delaySecond);
        ServiceResponse<List<DTimerExercise>> response = new ServiceResponse<List<DTimerExercise>>();
        response.setResponse(list);
        return  response;
    }
}
