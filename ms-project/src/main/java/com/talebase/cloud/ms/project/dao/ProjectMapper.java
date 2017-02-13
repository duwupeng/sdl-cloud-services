package com.talebase.cloud.ms.project.dao;

import com.talebase.cloud.base.ms.admin.domain.TAdmin;
import com.talebase.cloud.base.ms.project.domain.TProject;
import com.talebase.cloud.base.ms.project.domain.TProjectAdmin;
import com.talebase.cloud.base.ms.project.dto.DProjectQueryReq;
import com.talebase.cloud.base.ms.project.dto.DProjectSelect;
import com.talebase.cloud.base.ms.project.dto.DTProjectEx;
import com.talebase.cloud.base.ms.project.dto.DExamProjectTasks;
import com.talebase.cloud.common.protocal.PageRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
@Mapper
public interface ProjectMapper {

    @UpdateProvider(type = ProjectSqlProvider.class, method = "updateStatus")
    public Integer updateStatus(@Param("operatorName") String operatorName, @Param("projectId") Integer projectId, @Param("newStatus") Integer newStatus);

    @SelectProvider(type = ProjectSqlProvider.class, method = "get")
    public DTProjectEx get(@Param("projectId") Integer id);

    @SelectProvider(type = ProjectSqlProvider.class, method = "getByTask")
    public DTProjectEx getByTask(@Param("taskId") Integer taskId);

    @Select("select * from t_project_admin where project_id = #{projectId}")
    public List<TProjectAdmin> getAdmins(@Param("projectId") Integer projectId);

//    @Select("select * from t_project where company_id = #{companyId}")
//    @SelectProvider(type = ProjectSqlProvider.class, method = "find")
//    public List<TProject> find(@Param("companyId") Integer companyId, @Param("orgCode") String orgCode);

    @InsertProvider(type = ProjectSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public Integer insert(TProject project);

    @InsertProvider(type = ProjectSqlProvider.class, method = "insertProjectAdmin")
    public Integer insertProjectAdmin(TProjectAdmin projectAdmin);

    @UpdateProvider(type = ProjectSqlProvider.class, method = "update")
    public Integer update(TProject project);

    @Delete("delete from t_project_admin where project_id = #{projectId}")
    public Integer deleteProjectAdmins(Integer projectId);

    @Select("select * from t_project_admin where project_id = #{projectId}")
    public List<TProjectAdmin> findProjectAdmins(Integer projectId);

    @SelectProvider(type = ProjectSqlProvider.class, method = "queryTotal")
    public Integer queryTotal(@Param("account") String account, @Param("companyId") Integer companyId, @Param("orgCode") String orgCode, @Param("queryReq")DProjectQueryReq queryReq);

    @SelectProvider(type = ProjectSqlProvider.class, method = "query")
    public List<DTProjectEx> query(@Param("account") String account, @Param("companyId") Integer companyId, @Param("orgCode") String orgCode,
                                @Param("queryReq")DProjectQueryReq queryReq, @Param("pageReq")PageRequest pageReq);

    @SelectProvider(type = ProjectSqlProvider.class, method = "getProjectCntByName")
    public Integer getProjectCntByName(@Param("companyId") Integer companyId, @Param("name") String name, @Param("projectId") Integer projectId);

    @Insert("insert into t_project_err_notify(project_id, err_num) values(#{projectId}, 0)")
    public Integer insertProjectErrNotify(Integer projectId);

    @Update("update t_project_err_notify set err_num = err_num + #{addNum} where project_id = #{projectId}")
    public Integer updateProjectErrNotify(@Param("projectId") Integer projectId, @Param("addNum") Integer addNum);

    @Update("update t_project_err_notify set err_num = 0 where project_id = #{projectId}")
    public Integer clearProjectErrNotify(Integer projectId);

    @SelectProvider(type = ProjectSqlProvider.class, method = "findSelect")
    public List<DProjectSelect> findSelect(@Param("account") String account, @Param("companyId") Integer companyId, @Param("orgCode") String orgCode);

    @SelectProvider(type = ProjectSqlProvider.class, method = "queryTasksByUserId")
    public List<DExamProjectTasks> queryTasksByUserId(@Param("userId") Integer userId);

    @Insert("insert into t_group_admin(account, org_code, company_id) values(#{account}, #{orgCode}, #{companyId})")
    public void insertGroupAdmin(TAdmin admin);

    @Delete("delete from t_group_admin where account = #{account} and company_id = #{companyId}")
    public void deleteGroupAdmin(TAdmin admin);
}
