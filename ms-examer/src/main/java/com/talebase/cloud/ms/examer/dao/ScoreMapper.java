package com.talebase.cloud.ms.examer.dao;

import com.talebase.cloud.base.ms.examer.domain.TExercise;
import com.talebase.cloud.base.ms.examer.domain.TScore;
import com.talebase.cloud.base.ms.examer.dto.*;
import com.talebase.cloud.base.ms.examer.domain.TUserExam;
import com.talebase.cloud.base.ms.examer.dto.DUserExam;
import com.talebase.cloud.base.ms.examer.dto.DExamineeInTask;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by daorong.li on 2016-12-19.
 */
@Mapper
public interface ScoreMapper {

    @InsertProvider(type = ScoreSqlProvider.class, method = "buildInsertSql")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(TScore tScore);

    @Select("select * from t_score where serial_no = #{serialNo} and paper_id = #{paperId} and exam_id = #{examId}")
    TScore get(@Param("serialNo") String serial,@Param("paperId") Integer paperId,@Param("examId") Integer examId);

    @Select("select * from t_user_exam where task_id = #{taskId} and user_id = #{userId} and status >-1")
    TUserExam getTUserExamByTask(@Param("taskId") Integer taskId,@Param("userId") Integer userId);

    /*@Update("update t_score set score = #{score},done=#{done},total=#{total} where id = #{id}")
    Integer updateScore(TScore tScore);*/
    @UpdateProvider(type = ScoreSqlProvider.class, method = "updateScore")
    Integer updateScore(TScore tScore);

/*    @Select("select t.*,ts.score,ts.total,ts.done,ts.creater scoreCreater from t_user_exam t " +
            " left join t_score ts on ts.exam_id = t.id " +
            " where ts.total>0 " +
            " and t.task_id = #{taskId} and ts.paper_id = #{paperId} and ts.serial_no = #{serialNo}")
    List<DUserExam> getExamListOfSubject(@Param("paperId") String paperId, @Param("serialNo") String serial, @Param("taskId") String taskId);*/

/*    @Select("select * from t_score where exam_id = #{examId} and total>0")
    List<TScore> geScoreListOfExamer(String examId);*/

    @SelectProvider(type = ScoreSqlProvider.class, method = "getCompleteScoreOfExamer")
    List<TScore> getCompleteScoreOfExamer(@Param("examId") Integer examId);

    @Select("select * from t_score where exam_id = #{examId}")
    List<TScore> getScoreListOfExamer(Integer examId);

    @SelectProvider(type = ScoreSqlProvider.class, method = "getTaskDetailNum")
    Integer getTaskDetailNum(Integer taskId);

    @SelectProvider(type = ScoreSqlProvider.class, method = "getTaskDetail")
    List<DExamineeInTask> getTaskDetail(@Param("taskId") Integer taskId, @Param("pageReq") PageRequest pageReq);

    @Update("update t_exercise set sub_score = #{subScore} where user_id = #{userId} and task_id = #{taskId}")
    Integer updateExerciseScore(@Param("subScore") double subScore,@Param("userId") Integer userId,@Param("taskId") Integer taskId);

    @SelectProvider(type = ScoreSqlProvider.class, method = "getNextExamUser")
    TExercise getNextExamUser(@Param("taskId") Integer taskId, @Param("seq") Integer seq, Boolean ingoreMarke, String preOrNext);

    @Select("select * from t_exercise where task_id = #{taskId} and user_id = #{userId} ")
    TExercise getExercise(@Param("taskId") Integer taskId, @Param("userId") Integer userId);

    @SelectProvider(type = ScoreSqlProvider.class, method = "getMaxSerialNo")
    Integer getMaxSerialNo(@Param("taskId") Integer taskId, Boolean ingoreMarke);

    @Select("select count(*) from t_exercise where task_id = #{taskId} and serial_no is not null")
    Integer getFinishNum(@Param("taskId") Integer taskId);

    @SelectProvider(type = ScoreSqlProvider.class, method = "getMarkedQz")
    List<Integer> getMarkedQz(@Param("taskId") Integer taskId, @Param("finishCnt") Integer finishCnt);

    @SelectProvider(type = ScoreSqlProvider.class, method = "getNextQzForUsers")
    List<DNextQzResp> getNextQzForUsers(@Param("taskId") Integer taskId, @Param("serialNo") Integer serialNo);

    @Select("SELECT * FROM t_score where exam_id = #{examId}")
    List<TScore> getScoreByExamId(@Param("examId") Integer examId);


}
