package com.talebase.cloud.ms.notify.dao;

import com.talebase.cloud.base.ms.notify.dto.DNotifyRecordStatus;
import com.talebase.cloud.base.ms.notify.dto.DPageSearchData;
import com.talebase.cloud.base.ms.notify.dto.DReceiverStatistics;
import com.talebase.cloud.base.ms.notify.domain.TNotifyRecord;
import com.talebase.cloud.common.protocal.PageRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by bin.yang on 2016-11-28.
 */

@Mapper
public interface NotifyRecordMapper {

    @SelectProvider(type = NotifyRecordSqlProvider.class, method = "getNotifyRecords")
    List<TNotifyRecord> getAll(@Param("orgCode") String orgCode, @Param("companyId") Integer companyId, @Param("sender") String sender,
                               @Param("pageReq") PageRequest pageReq, @Param("dPageSearchData") DPageSearchData dPageSearchData,
                               @Param("ids") List<Integer> ids);

    @SelectProvider(type = NotifyRecordSqlProvider.class, method = "getCount")
    Integer getCount(@Param("orgCode") String orgCode, @Param("companyId") Integer companyId, @Param("sender") String sender, @Param("dPageSearchData") DPageSearchData dPageSearchData);

    @Select("select * from t_notify_record where id = #{id}")
    TNotifyRecord getTNotifyRecordById(Integer id);

    @InsertProvider(type = NotifyRecordSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(TNotifyRecord tNotifyRecord);

    @SelectProvider(type = NotifyRecordSqlProvider.class, method = "getExportList")
    List<TNotifyRecord> getExportList(@Param("orgCode") String orgCode, @Param("companyId") Integer companyId, @Param("sender") String sender, @Param("sendType") Integer sendType);

    @UpdateProvider(type = NotifyRecordSqlProvider.class, method = "updateTimes")
    Integer updateTimes(@Param("idList") List<Integer> idList);

    @UpdateProvider(type = NotifyRecordSqlProvider.class, method = "updateStatus")
    Integer updateStatus(DNotifyRecordStatus dNotifyRecordStatus);

    @SelectProvider(type = NotifyRecordSqlProvider.class, method = "querySendding")
    List<TNotifyRecord> querySendding();

    @SelectProvider(type = NotifyRecordSqlProvider.class, method = "getById")
    List<TNotifyRecord> getById(@Param("idList") List<Integer> idList);


}
