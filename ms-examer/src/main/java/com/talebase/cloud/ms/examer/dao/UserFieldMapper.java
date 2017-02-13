package com.talebase.cloud.ms.examer.dao;

import com.talebase.cloud.base.ms.common.dto.DEmailLog;
import com.talebase.cloud.base.ms.examer.domain.TUserShowField;
import com.talebase.cloud.base.ms.examer.dto.DUserExamPageRequest;
import com.talebase.cloud.base.ms.examer.dto.DUserInfoAndExamField;
import com.talebase.cloud.base.ms.examer.dto.DUserShowField;
import com.talebase.cloud.base.ms.examer.enums.TUserInfoStatus;
import com.talebase.cloud.base.ms.examer.enums.TUserShowFieldIsshow;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.ms.examer.cfg.Config;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daorong.li on 2016-12-7.
 */
@Mapper
public interface UserFieldMapper {

    @InsertProvider(type = UserFieldSqlProvider.class, method = "buildInsertSql")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(DUserShowField dUserShowField);

    @InsertProvider(type = UserFieldSqlProvider.class, method = "iniCompanyUserField")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void iniCompanyUserField(Integer companyId);

    @UpdateProvider(type = UserFieldSqlProvider.class,method = "buildUpdateByGlobalKeySql")
    Integer updateByGlobalKey(DUserShowField dUserShowField);

    @Update("update t_user_show_field set isshow=#{isshow},ismandatory=#{ismandatory},isunique=#{isunique} " +
            "where company_id =#{companyId} and project_id is null and task_id is null")
    Integer updateByGlobalKeyStatus(@Param("isshow") Integer isshow,@Param("ismandatory") Integer ismandatory,@Param("isunique") Integer isunique,
                                    @Param("companyId") Integer companyId);

    @Update("update t_user_show_field set ismandatory=#{ismandatory} " +
            " where company_id =#{companyId} and project_id = #{projectId} and task_id = #{taskId} and field_key = #{fieldKey}")
    Integer updateProjectExamMandatory(@Param("ismandatory") Integer ismandatory,
                                       @Param("taskId") Integer taskId,
                                       @Param("projectId") Integer projectId,
                                       @Param("companyId") Integer companyId,
                                       @Param("fieldKey") String fieldKey);

    @Update("update t_user_show_field set isunique=#{isunique} " +
            " where company_id =#{companyId} and project_id = #{projectId} and task_id = #{taskId} and field_key = #{fieldKey}")
    Integer updateProjectExamUnique(@Param("isunique") Integer isunique,
                                       @Param("taskId") Integer taskId,
                                       @Param("projectId") Integer projectId,
                                       @Param("companyId") Integer companyId,
                                       @Param("fieldKey") String fieldKey);

    @Delete(" delete from t_user_show_field where field_key = #{fieldKey} and company_id =#{companyId}  ")
    Integer del(@Param("fieldKey") String fieldKey,@Param("companyId") Integer companyId);

    @Select("select count(1) total from t_user_info where company_id = #{companyId} and status > #{status} and extension_field like concat('%', #{fieldKey}, '%')") @ResultType(java.lang.Integer.class)
    Integer checkExtension(@Param("companyId") Integer companyId,@Param("status") Integer status,@Param("fieldKey") String fieldKey);

    @Delete("delete from t_user_show_field where task_id = #{taskId} and project_id=#{projectId} and company_id=#{companyId}")
    Integer delProjectField(@Param("taskId") Integer taskId,@Param("projectId") Integer projectId,@Param("companyId") Integer companyId);

    @Select(" select * from t_user_show_field where field_key = #{filedKey} and  company_id = #{companyId} and task_id is null and project_id is null")
    DUserShowField getProjectExam(@Param("filedKey") String filedKey,@Param("companyId") Integer companyId);

    @Select("select * from t_user_show_field where field_name = #{fieldName} and company_id = #{companyId}")
    List<DUserShowField> searchFieldByName(@Param("fieldName") String fieldName,@Param("companyId") Integer companyId);

    @Select(" select * from t_user_show_field where field_key = #{filedKey} and  company_id = #{companyId} ")
    List<DUserShowField> searchFieldByKey(@Param("filedKey") String filedKey,@Param("companyId") Integer companyId);

    @Select("select REPLACE(a.field_key,#{filePrefix},'') field_key,a.sortnum  from t_user_show_field  a order by a.id desc limit 0,1")
    TUserShowField getTopOneUserShowFieldByCompanyId(@Param("filePrefix")String filePrefix,@Param("companyId") Integer companyId);

//    @Select(" select * from t_user_show_field where company_id = #{companyId} and task_id is null and projectid is null ")
//    List<TUserShowField> getGlobalExamers(@Param("companyId") Integer companyId);

    @SelectProvider(type = UserFieldSqlProvider.class,method = "findExamers")
    List<TUserShowField> findExamers(@Param("companyId") Integer companyId, @Param("projectId") Integer projectId, @Param("taskId") Integer taskId);

    @SelectProvider(type = UserFieldSqlProvider.class,method = "getProjectExamersPageSql")
    List<Map<String,Object>> getProjectExamers(@Param("data") DUserExamPageRequest data,@Param("start") Integer start,@Param("limit") Integer limit);

    @SelectProvider(type = UserFieldSqlProvider.class,method = "getCountProjectExamersSql") @ResultType(java.lang.Integer.class)
    Integer getCountProectExamers(@Param("data") DUserExamPageRequest data);

    @SelectProvider(type = UserFieldSqlProvider.class,method = "getProjectExamersSql")
    List<DUserInfoAndExamField> getAllProjectExamers(@Param("data") DUserExamPageRequest data,@Param("ids") List<Integer> ids);

    @SelectProvider(type = UserFieldSqlProvider.class,method = "getProjectExamersSql")
    List<Map<String,Object>> getAllProjectExamersByMap(@Param("data") DUserExamPageRequest data, @Param("ids") List<Integer> ids);

    @Select("select count(*) tatol from t_user_show_field where field_key like  concat('%', #{fieldName}, '%') and company_id =#{companyId} and task_id is null and project_id is null") @ResultType(java.lang.Integer.class)
    Integer beyoundFieldNumber(@Param("fieldName") String fieldName,@Param("companyId") Integer companyId);
}
