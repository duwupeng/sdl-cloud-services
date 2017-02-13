package com.talebase.cloud.ms.project.dao;

import com.talebase.cloud.base.ms.email.dto.DUser;
import com.talebase.cloud.base.ms.project.domain.*;
import com.talebase.cloud.base.ms.project.dto.*;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.ServiceHeader;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
@Mapper
public interface TaskMapper {

    @UpdateProvider(type = TaskSqlProvider.class, method = "updateStatusByProject")
    public Integer updateStatusByProject(@Param("operatorName") String operatorName, @Param("projectId") Integer projectId, @Param("newStatus") Integer newStatus);

    @Select("select tp.* from t_task_progress tp join t_task t on tp.task_id = t.id where t.project_id = #{projectId}")
    public List<TTaskProgress> getProgressByProject(Integer projectId);

    @Select("select * from t_task_progress where task_id = #{taskId}")
    public TTaskProgress getProgress(Integer taskId);

    @UpdateProvider(type = TaskSqlProvider.class, method = "updateStatus")
    public Integer updateStatus(@Param("operatorName") String operatorName, @Param("taskId") Integer taskId, @Param("newStatus") Integer newStatus);

    //用于项目列表组装到项目
    @SelectProvider(type = TaskSqlProvider.class, method = "findTasksByProjectIds")
    public List<DTTaskEx> findTasksByProjectIds(@Param("projectIds") List<Integer> projectIds);

    @SelectProvider(type = TaskSqlProvider.class, method = "findTaskExById")
    public DTTaskEx findTaskExById(@Param("id") Integer id);

    @UpdateProvider(type = TaskSqlProvider.class, method = "update")
    public Integer update(TTask task);

    @InsertProvider(type = TaskSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public Integer insert(TTask task);

    @InsertProvider(type = TaskSqlProvider.class, method = "insertTaskExaminer")
    public Integer insertTaskExaminer(TTaskExaminer taskExaminer);

    @Delete("delete from t_task_examiner where task_id = #{taskId}")
    public Integer deleteTaskExaminers(Integer taskId);

    @SelectProvider(type = TaskSqlProvider.class, method = "getTaskExaminers")
    public List<TTaskExaminer> getTaskExaminers(@Param("taskIds") List<Integer> taskIds);

    @Insert("insert into t_task_progress(task_id) values(#{taskId})")
    public Integer insertTaskProgress(Integer taskId);

    @SelectProvider(type = TaskSqlProvider.class, method = "findTasksNumByExaminer")
    public Integer findTasksNumByExaminer(@Param("operatorName") String operatorName, @Param("companyId") Integer companyId);

    @SelectProvider(type = TaskSqlProvider.class, method = "findTasksByExaminer")
    public List<DTaskInScore> findTasksByExaminer(@Param("operatorName") String operatorName, @Param("companyId") Integer companyId, @Param("pageReq") PageRequest pageRequest);

    @SelectProvider(type = TaskSqlProvider.class, method = "getTaskInMark")
    public DTaskMarked getTaskInMark(@Param("operatorName") String operatorName, @Param("companyId") Integer companyId);

    @Select("select * from t_task where id = #{taskId}")
    public TTask get(@Param("taskId") Integer taskId);

    @SelectProvider(type = TaskSqlProvider.class, method = "findUseStatics")
    public List<DUseStatics> findUseStatics(@Param("reqs") List<DUseStaticsQueryReq> reqs, @Param("serviceHeader") ServiceHeader serviceHeader);

    @UpdateProvider(type = TaskSqlProvider.class, method = "updatePaper")
    public Integer updatePaper(TTask task);

}
