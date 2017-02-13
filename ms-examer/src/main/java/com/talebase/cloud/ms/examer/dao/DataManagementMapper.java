package com.talebase.cloud.ms.examer.dao;

import com.talebase.cloud.base.ms.examer.dto.DDataManagementRequest;
import com.talebase.cloud.base.ms.examer.dto.DDataManagementResponse;
import com.talebase.cloud.common.protocal.PageRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by daorong.li on 2016-12-19.
 */
@Mapper
public interface DataManagementMapper {

    @SelectProvider(type = DataManagementMapperSqlProvider.class, method = "query")
    List<DDataManagementResponse> query(@Param("pageRequest") PageRequest pageRequest, @Param("companyId") Integer companyId, @Param("dDataManagementRequest") DDataManagementRequest dDataManagementRequest);

    @SelectProvider(type = DataManagementMapperSqlProvider.class, method = "queryCount")
    Integer queryCount(@Param("companyId") Integer companyId, @Param("dDataManagementRequest") DDataManagementRequest dDataManagementRequest);

}
