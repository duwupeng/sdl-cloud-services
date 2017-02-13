package com.talebase.cloud.ms.email.dao;


import com.talebase.cloud.base.ms.common.domain.TEmailLog;
import com.talebase.cloud.base.ms.common.dto.DEmailLog;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * Created by kanghong.zhao on 2016-11-10.
 */
@Mapper
public interface EmailMapper {

    @InsertProvider(type = EmailSqlProvider.class, method = "buildInsertSql")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(DEmailLog dEmailLog);

    @SelectProvider(type = EmailSqlProvider.class, method = "buildSelectSql")
    List<TEmailLog> getEmails(@Param("tableNames") List<String> tableNames,@Param("tableIds") List<String> tableIds,@Param("statuss") List<String> status);
}
