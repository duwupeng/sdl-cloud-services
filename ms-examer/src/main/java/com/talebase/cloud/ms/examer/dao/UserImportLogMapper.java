package com.talebase.cloud.ms.examer.dao;

import com.talebase.cloud.base.ms.examer.domain.TUserImportLog;
import com.talebase.cloud.common.protocal.ServiceHeader;
import org.apache.ibatis.annotations.*;

/**
 * Created by kanghong.zhao on 2016-12-7.
 */
@Mapper
public interface UserImportLogMapper {

    @InsertProvider(type = UserImportLogSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(TUserImportLog importLog);

    @UpdateProvider(type = UserImportLogSqlProvider.class, method = "updateToEnd")
    Integer updateToEnd(TUserImportLog TUserImportLog);

    @Select("select * from t_user_import_log where creater = #{account} order by batch_no desc limit 1")
    TUserImportLog getLastLog(String account);

    @Select("select * from t_user_import_log where batch_no = #{batchNo} order by batch_no desc limit 1")
    TUserImportLog getByBatchNo(String batchNo);
}
