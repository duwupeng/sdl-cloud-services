package com.talebase.cloud.ms.examer.dao;

import com.talebase.cloud.base.ms.examer.domain.TExercise;
import com.talebase.cloud.base.ms.examer.domain.TUserExam;
import com.talebase.cloud.base.ms.examer.dto.DExaminerTaskListToNotify;
import com.talebase.cloud.base.ms.examer.dto.DReSetPassword;
import com.talebase.cloud.base.ms.examer.dto.DUserExamInfo;
import com.talebase.cloud.base.ms.examer.dto.DUserExamPermission;
import com.talebase.cloud.base.ms.project.domain.TProject;
import com.talebase.cloud.base.ms.project.domain.TTask;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-9.
 */
@Mapper
public interface UserExamMapper {

    @SelectProvider(type = UserExamSqlProvider.class, method = "getCntForExamAndUser")
    public Integer getCntForExamAndUser(@Param("userId") Integer userId, @Param("projectId") Integer projectId, @Param("taskId") Integer taskId);

    @SelectProvider(type = UserExamSqlProvider.class, method = "getCntForExamAndUserByMobileAndProjectId")
    public Integer getCntForExamAndUserByMobileAndProjectId(@Param("mobile") String mobile, @Param("projectId") Integer projectId);

    @SelectProvider(type = UserExamSqlProvider.class, method = "getUserExam")
    public List<TUserExam> getUserExam(@Param("userId") Integer userId);

    @SelectProvider(type = UserExamSqlProvider.class, method = "getTaksCountToNotify")
    Integer getTaksCountToNotify(@Param("companyId") Integer companyId, @Param("userId") Integer userId, @Param("projectId") Integer projectId, @Param("taskId") Integer taskId);

    @SelectProvider(type = UserExamSqlProvider.class, method = "getTaksListToNotify")
    List<DExaminerTaskListToNotify> getTaksListToNotify(@Param("companyId") Integer companyId, @Param("userId") Integer userId, @Param("projectId") Integer projectId, @Param("taskId") Integer taskId);

    @InsertProvider(type = UserExamSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public Integer insert(TUserExam userExam);

    @UpdateProvider(type = UserExamSqlProvider.class, method = "updateStatus")
    Integer updateStatus(@Param("data") DReSetPassword data, @Param("status") Integer status, @Param("idList") List<Integer> idList);

    @Select("select * from t_user_exam where id =#{id}")
    TUserExam get(@Param("id") Integer id);

    @Select("select * from t_exercise where user_id=#{userId} and task_id =#{taskId} and serial_no is not null ")
    TExercise getByUserIdAndTaskId(@Param("userId") Integer userId, @Param("taskId") Integer taskId);

    @UpdateProvider(type = UserExamSqlProvider.class, method = "delExamer")
    Integer delExamer(@Param("data") DReSetPassword data, @Param("ids") List<Integer> ids);

    @SelectProvider(type = UserExamSqlProvider.class, method = "queryStartExamCnt")
    Integer queryStartExamCnt(@Param("data") DReSetPassword data, @Param("ids") List<Integer> ids);

    @DeleteProvider(type = UserExamSqlProvider.class, method = "delStartExamExercise")
    Integer delStartExamExercise(@Param("data") DReSetPassword data, @Param("ids") List<Integer> ids);

    @Select("select * from t_project where id =#{id}")
    TProject getProjectById(@Param("id") Integer id);

    @UpdateProvider(type = UserExamSqlProvider.class, method = "updateProjectById")
    Integer updateProjectById(@Param("data") TProject data);

    @Select("select * from t_task where id =#{id}")
    TTask getTaskById(@Param("id") Integer id);

    @SelectProvider(type = UserExamSqlProvider.class, method = "getUserExamPermission")
    DUserExamPermission getUserExamPermission(@Param("taskId") Integer taskId, @Param("userId") Integer userId);

    @Select("select * from t_user_exam where task_id = #{taskId} and user_id = #{userId} and status <> -1")
    TUserExam getUserExamByTaskAndUser(@Param("taskId") Integer taskId, @Param("userId") Integer userId);

    @UpdateProvider(type = UserExamSqlProvider.class, method = "updateSendStatus")
    Integer updateSendStatus(@Param("projectId") Integer projectId, @Param("taskId") Integer taskId, @Param("account") String account, @Param("type") Integer type, @Param("status") Integer status);

    @Select("select * from t_user_exam where task_id=#{taskId} and status >#{status} ")
    List<TUserExam> getUserExamByTaskId(@Param("taskId") Integer taskId, @Param("status") Integer status);

    @SelectProvider(type = UserExamSqlProvider.class, method = "getUserExamInfos")
    List<DUserExamInfo> getUserExamInfos(@Param("taskId") Integer taskId);
}
