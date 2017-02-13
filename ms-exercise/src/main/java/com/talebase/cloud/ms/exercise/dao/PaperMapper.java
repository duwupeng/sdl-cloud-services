package com.talebase.cloud.ms.exercise.dao;

import com.talebase.cloud.base.ms.examer.domain.TExercise;
import com.talebase.cloud.base.ms.examer.dto.DTimerExercise;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by erid.du on 2016-12-9.
 */
@Mapper
public interface PaperMapper {
    @InsertProvider(type = PaperSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public Integer insert(TExercise tExercise);

    @UpdateProvider(type = PaperSqlProvider.class, method = "update")
    public Integer update(TExercise tExercise);

    @Select("select * from t_exercise where id = #{exerciseId}")
    public TExercise query(Integer exerciseId);

    @Select("select sub_score + obj_score totalScore from t_exercise where projectId = #{projectId} and taskId = #{taskId} and userId = #{userId}")
    public BigDecimal getTotalScore(Integer projectId, Integer taskId, Integer userId);

    @UpdateProvider(type = PaperSqlProvider.class, method = "updateToSubmit")
    public Integer updateToSubmit(TExercise tExercise);

    @Select("select count(*) from t_exercise where task_id = #{taskId} and user_id = #{userId}")
    public Integer cntExists(@Param("taskId") Integer taskId, @Param("userId") Integer userId);

    @Select("select * from t_exercise where task_id = #{taskId} and user_id = #{userId}")
    public TExercise queryByTaskIdAndUserId(@Param("taskId") Integer taskId, @Param("userId") Integer userId);

    @SelectProvider(type = PaperSqlProvider.class, method = "getExerciseByTimer")
    public List<DTimerExercise> getExerciseByTimer(@Param("limit") Integer limit, @Param("delaySecond") Integer delaySecond);
}
