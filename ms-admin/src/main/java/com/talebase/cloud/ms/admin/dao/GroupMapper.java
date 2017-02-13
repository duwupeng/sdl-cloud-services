package com.talebase.cloud.ms.admin.dao;

import com.talebase.cloud.base.ms.admin.domain.TGroup;
import com.talebase.cloud.base.ms.admin.dto.DGroup;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-11-25.
 */
@Mapper
public interface GroupMapper {

    @SelectProvider(type = GroupSqlProvider.class, method = "getCntByName")
    Integer getCntByName(@Param("companyId") Integer companyId, @Param("name") String name);

    @SelectProvider(type = GroupSqlProvider.class, method = "updateNameSameId")
    Integer updateNameSameId(@Param("companyId") Integer companyId, @Param("name") String name,@Param("groupId") Integer groupId);

    @Update("update t_group set name = #{groupName}, modifier = #{operatorName}, modified_date = now() where company_id = #{companyId} and id = #{groupId}")
    Integer updateGroupName(@Param("groupName") String groupName, @Param("operatorName") String operatorName, @Param("companyId") Integer companyId, @Param("groupId") Integer groupId);

    @UpdateProvider(type = GroupSqlProvider.class, method = "updateStatusToDelete")
    Integer updateStatusToDelete(@Param("orgCodes") List<String> orgCodes, @Param("companyId") Integer companyId, @Param("operatorName") String operatorName);

    @InsertProvider(type = GroupSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(TGroup group);

    @SelectProvider(type = GroupSqlProvider.class, method = "get")
    TGroup get(Integer id);

    @Select("select * from t_group where companyId = #{companyId}")
    List<TGroup> findGroups(Integer companyId);

    @SelectProvider(type = GroupSqlProvider.class, method = "findGroupsByIds")
    List<TGroup> findGroupsByIds(@Param("companyId") Integer companyId, @Param("groupIds") List<Integer> groupIds);

    @SelectProvider(type = GroupSqlProvider.class, method = "findAdminCntByGroups")
    Integer findAdminCntByGroups(@Param("orgCodes") List<String> orgCodes);

    @SelectProvider(type = GroupSqlProvider.class, method = "findGroupsWithAdminCnt")
    List<DGroup> findGroupsWithAdminCnt(@Param("companyId") Integer companyId,List<String> permissions,@Param("orgCode") String orgCode);

    @Update("update t_group set org_code = #{orgCode} where id = #{id}")
    Integer updateOrgCode(@Param("id") Integer id, @Param("orgCode") String orgCode);

    @SelectProvider(type = GroupSqlProvider.class, method = "findGroupsByOrgCode")
    List<TGroup> findGroupsByOrgCode(@Param("companyId") Integer companyId, @Param("orgCode") String orgCode);

    @Select("select * from t_group where id = #{id} and status=1")
    TGroup getgroup(@Param("id") Integer id);
}
