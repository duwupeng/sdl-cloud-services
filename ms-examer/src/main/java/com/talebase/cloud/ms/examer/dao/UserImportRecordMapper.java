package com.talebase.cloud.ms.examer.dao;

import com.talebase.cloud.base.ms.examer.domain.TUserImportRecord;
import com.talebase.cloud.base.ms.examer.dto.DImportStatics;
import com.talebase.cloud.base.ms.examer.dto.DUserImportRecord;
import com.talebase.cloud.base.ms.examer.dto.DUserImportRecordQueryReq;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.ServiceHeader;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-7.
 */
@Mapper
public interface UserImportRecordMapper {

    @InsertProvider(type = UserImportRecordSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(TUserImportRecord importRecord);

    @SelectProvider(type = UserImportRecordSqlProvider.class, method = "queryStaticsNum")
    DImportStatics queryStaticsNum(String batchNo);

    @SelectProvider(type = UserImportRecordSqlProvider.class, method = "queryCnt")
    Integer queryCnt(@Param("queryReq") DUserImportRecordQueryReq queryReq, @Param("serviceHeader") ServiceHeader serviceHeader);

    @SelectProvider(type = UserImportRecordSqlProvider.class, method = "query")
    List<DUserImportRecord> query(@Param("queryReq") DUserImportRecordQueryReq queryReq, @Param("serviceHeader") ServiceHeader serviceHeader, @Param("pageReq") PageRequest pageReq);

    @SelectProvider(type = UserImportRecordSqlProvider.class, method = "findFailRecords")
    List<TUserImportRecord> findFailRecords(@Param("batchNo") String batchNo);

}
