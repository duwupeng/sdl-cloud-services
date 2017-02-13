package com.talebase.cloud.os.examer.controller.cache;

import com.talebase.cloud.base.ms.examer.dto.DExamItem;
import com.talebase.cloud.base.ms.paper.dto.DWPaperResponse;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.os.examer.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * Created by eric.du on 2016-12-12.
 */
@RestController
public class ExercisesCacheController {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ExerciseService exerciseService;

    /**
     * 发卷
     */
    @GetMapping(value = "/exercise/dispatch/{taskId}/{paperId}/{examerId}")
    public ServiceResponse dispatchedPaper(@PathVariable("taskId") int taskId,
                                       @PathVariable("examerId") int examerId,
                                       @PathVariable("paperId") int paperId ){
        //Get paperId from project module by taskId
        String examerKey ="T"+taskId+"-P"+paperId+"-E"+examerId;
        String key ="T"+taskId+"-P"+paperId;
        DWPaperResponse dPaperResponse = null;
        //
        if(!redisTemplate.hasKey(key)){
            // Get paper from  paper module by paperId
            dPaperResponse = exerciseService.getPaper(paperId);
            if(dPaperResponse==null){
                return new ServiceResponse(BizEnums.EXAMREJECT.message);
            }
            // save the original paper to redis
            redisTemplate.opsForValue( ).set(key,dPaperResponse);
        }else {
            dPaperResponse = (DWPaperResponse)redisTemplate.opsForValue().get(key);
        }
        //  examerKey
        BoundHashOperations bop= redisTemplate.boundHashOps(examerKey);

        // compose the examer paper
        exerciseService.composePaper(dPaperResponse);

        if(!redisTemplate.hasKey(examerKey)){
            bop.put(key,dPaperResponse);
        }

        //返回卷子
        return new ServiceResponse(dPaperResponse);
    }

    /**
     * 开始答题
     */
//    @PostMapping(value = "/exercise/{taskId}/{paperId}/{examerId}")
//    public ServiceResponse exercise(@PathVariable("taskId")String taskId,@PathVariable("paperId")String paperId,@PathVariable("examerId")String examerId,String josnStr){
//        //  examerKey
////        String examerKey ="T"+taskId+"-P"+paperId+"-E"+examerId;
////        BoundHashOperations bop= redisTemplate.boundHashOps(examerKey);
////        bop.getOperations().opsForHash().put(examerKey ,"answers",josnStr);
//        return new ServiceResponse();
//    }

    /**        String examerKey ="T"+taskId+"-P"+paperId+"-E"+examerId;
     * 答题
     */
    @PutMapping(value = "/exercise/{taskId}/{paperId}/{examerId}/{itemId}")
    public ServiceResponse answer(@PathVariable("taskId")int taskId ,
                                    @PathVariable("paperId")int paperId ,
                                    @PathVariable("examerId")int examerId ,
                                    @PathVariable("itemId")int itemId ,
                                    String jsonStr){
        String examerKey ="T"+taskId+"-P"+paperId+"-E"+examerId;
        BoundHashOperations<String, String, Object> boundHashOperations = redisTemplate.boundHashOps(examerKey);
        DExamItem dExamItem = GsonUtil.fromJson(jsonStr,DExamItem.class);
        boundHashOperations.getOperations().opsForHash().put(examerKey,itemId,dExamItem);
        return new ServiceResponse();
    }

//    /**
//     * 答题提交
//     */
//    @GetMapping(value = "/exercise/submit/{taskId}/{paperId}/{examerId}")
//    public ServiceResponse answerSubmit(@PathVariable("taskId")int taskId ,
//                                        @PathVariable("paperId")int paperId ,
//                                        @PathVariable("examerId")int examerId ){
//        String examerKey ="T"+taskId+"-P"+paperId+"-E"+examerId;
//        BoundHashOperations<String, String, DExamItem> boundHashOperations = redisTemplate.boundHashOps(examerKey);
//        Map<String, DExamItem> map = boundHashOperations.entries();
//        return exerciseService.submit(new ServiceRequest(ServiceHeaderUtil.getRequestHeader(),map));
//    }

    /**
     * 答题评分
     */
    public ServiceResponse score(@PathVariable("paperId")Integer paperId ,
                                    @PathVariable("examerId")Integer examerId ){
//        String key="P"+paperId+"-E"+ examerId;
//        BoundHashOperations<String, String, String> boundHashOperations = redisTemplate.boundHashOps(key);
//        Map<String, String> map = boundHashOperations.entries();
//        return exerciseService.submit(new ServiceRequest(ServiceHeaderUtil.getRequestHeader(),map));
        return  null;
    }
}
