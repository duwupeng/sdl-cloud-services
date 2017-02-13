package com.talebase.cloud.ms.examer.dao;

import com.talebase.cloud.base.ms.examer.dto.DReSetPassword;
import com.talebase.cloud.base.ms.examer.dto.DUpdatePassword;
import com.talebase.cloud.base.ms.examer.dto.DUserExamPageRequest;
import com.talebase.cloud.base.ms.notify.dto.DReceiverStatistics;
import org.apache.ibatis.annotations.*;
import com.talebase.cloud.base.ms.examer.domain.TUserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by kanghong.zhao on 2016-12-7.
 */
@Mapper
public interface UserInfoMapper {

    @InsertProvider(type = UserInfoSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(TUserInfo userInfo);

    @SelectProvider(type = UserInfoSqlProvider.class, method = "getCntForUniqueField")
    Integer getCntForUniqueField(String fieldName, @Param("value") String value, @Param("companyId") Integer companyId, @Param("projectId") Integer projectId, @Param("taskId") Integer taskId, @Param("account") String account);

    @SelectProvider(type = UserInfoSqlProvider.class, method = "getUserByAccount")
    TUserInfo getUserByAccount(@Param("account") String account, @Param("companyId") Integer companyId);

    @SelectProvider(type = UserInfoSqlProvider.class, method = "getUserByMobile")
    TUserInfo getUserByMobile(@Param("mobile") String mobile);

    @Select("select * from t_user_info where id = #{id}")
    TUserInfo getUserById(Integer id);

    @UpdateProvider(type = UserInfoSqlProvider.class, method = "update")
    Integer update(TUserInfo userInfo);

    @UpdateProvider(type = UserInfoSqlProvider.class, method = "builUpdatePasswordSql")
    Integer updatePassword(@Param("data") DReSetPassword data ,@Param("password") String password, @Param("idList") List<Integer> idList);

    @UpdateProvider(type = UserInfoSqlProvider.class, method = "updateUserPassword")
    Integer updateUserPassword(@Param("data") DUpdatePassword data);

    @Select(" select * from t_user_info where id = #{id}") @ResultType(java.util.HashMap.class)
    Map<String,Object> get(@Param("id") Integer id);

    @SelectProvider(type = UserInfoSqlProvider.class, method = "getMaxAccountByPreSubffix")
    Integer getMaxAccountByPreSubffix(@Param("companyId") Integer companyId, @Param("pre") String pre, @Param("suffix") String suffix);

    @SelectProvider(type = UserInfoSqlProvider.class, method = "getUser")
    TUserInfo getUser(@Param("id") Integer id, @Param("companyId") Integer companyId);

    @SelectProvider(type = UserInfoSqlProvider.class, method = "getReceiverStatistics")
    DReceiverStatistics getReceiverStatistics(@Param("data") DReSetPassword data,@Param("idList") List<Integer> idList);

    @SelectProvider(type = UserInfoSqlProvider.class, method = "getUserList")
    List<TUserInfo> getUserList(@Param("data") DReSetPassword data,@Param("idList") List<Integer> idList);

    @Select(" select * from t_user_info where account =#{account} and company_id = #{companyId} and status >-1")
    TUserInfo getByAccount(@Param("companyId") Integer companyId,@Param("account") String account);


    @Select(" select * from t_user_info where account =#{account} and company_id = #{companyId} and status >-1")
    Map<String,Object> getByAccountToMap(@Param("companyId") Integer companyId,@Param("account") String account);
}
