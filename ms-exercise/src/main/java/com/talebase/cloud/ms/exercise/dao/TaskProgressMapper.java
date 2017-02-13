package com.talebase.cloud.ms.exercise.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * Created by kanghong.zhao on 2016-12-22.
 */
@Mapper
public interface TaskProgressMapper {

    @Update("update t_task_progress set pre_num = pre_num + #{preNumAdd}, in_num = in_num + #{inNumAdd}, \n" +
    "finish_num = finish_num + #{finishNumAdd}, marked_num = marked_num + #{markedNumAdd} \n " +
    "where task_id = #{taskId}")
    Integer updateAdd(@Param("taskId") Integer taskId, @Param("preNumAdd") Integer preNumAdd, @Param("inNumAdd") Integer inNumAdd, @Param("finishNumAdd") Integer finishNumAdd, @Param("markedNumAdd") Integer markedNumAdd);

}
