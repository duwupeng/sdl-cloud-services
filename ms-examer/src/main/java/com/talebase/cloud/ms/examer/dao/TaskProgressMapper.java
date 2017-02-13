package com.talebase.cloud.ms.examer.dao;

import com.talebase.cloud.base.ms.project.domain.TTaskProgress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-22.
 */
@Mapper
public interface TaskProgressMapper {

    @Update("update t_task_progress set pre_num = pre_num + #{preNumAdd}, in_num = in_num + #{inNumAdd}, \n" +
    "finish_num = finish_num + #{finishNumAdd}, marked_num = marked_num + #{markedNumAdd} \n " +
    "where task_id = #{taskId}")
    Integer updateAdd(@Param("taskId") Integer taskId, @Param("preNumAdd") Integer preNumAdd, @Param("inNumAdd") Integer inNumAdd, @Param("finishNumAdd") Integer finishNumAdd, @Param("markedNumAdd") Integer markedNumAdd);

    @Update("update t_task_progress set pre_num = #{preNum}, in_num =  #{inNum}, \n" +
            "finish_num = #{finishNum}, marked_num = #{markedNum} \n " +
            "where task_id = #{taskId}")
    Integer update(@Param("taskId") Integer taskId, @Param("preNum") Integer preNum, @Param("inNum") Integer inNum, @Param("finishNum") Integer finishNum, @Param("markedNum") Integer markedNum);

    @Select("select case when sum(pre_num) is null then 0 else sum(pre_num) end as pre_num,\n" +
            "case when sum(in_num) is null then 0 else sum(in_num) end as in_num,\n" +
            "case when sum(finish_num) is null then 0 else sum(finish_num) end as finish_num,\n" +
            "case when sum(marked_num) is null then 0 else sum(marked_num) end as marked_num from v_task_progress where task_id = #{taskId}")
    TTaskProgress getForRealTime(@Param("taskId") Integer taskId);

}
