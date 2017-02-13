package com.talebase.cloud.ms.notify.dao;

import com.talebase.cloud.base.ms.notify.domain.TNotifyTemplate;
import com.talebase.cloud.base.ms.notify.enumes.TNotifyTemplateMethod;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.ServiceHeader;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by bin.yang on 2016-11-24.
 */

@Mapper
public interface MailTemplateMapper {

    @SelectProvider(type = NotifyTemplateSqlProvider.class, method = "query")
    List<TNotifyTemplate> getAll(@Param("serviceHeader") ServiceHeader serviceHeader,@Param("pageReq") PageRequest pageReq,@Param("method") Integer method);

    @SelectProvider(type = NotifyTemplateSqlProvider.class, method = "queryCount")
    Integer getCount(@Param("serviceHeader")ServiceHeader serviceHeader,@Param("method") Integer method);

    @SelectProvider(type = NotifyTemplateSqlProvider.class, method = "queryTemplates")
    List<TNotifyTemplate> getTemplates(@Param("companyId") Integer companyId, @Param("creator") String creator, @Param("method") Integer method);

    @SelectProvider(type = NotifyTemplateSqlProvider.class, method = "queryTemplateByName")
    Integer getTemplateByName(TNotifyTemplate tNotifyTemplate);

    @Select("select * from t_notify_template where id = #{id}")
    TNotifyTemplate getTemplateById(Integer id);

    @InsertProvider(type = NotifyTemplateSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(TNotifyTemplate tNotifyTemplate);

    @UpdateProvider(type = NotifyTemplateSqlProvider.class, method = "updateEmail")
    Integer updateEmail(TNotifyTemplate tNotifyTemplate);

    @UpdateProvider(type = NotifyTemplateSqlProvider.class, method = "updateStatus")
    Integer updateStatus(TNotifyTemplate tNotifyTemplate);

    @UpdateProvider(type = NotifyTemplateSqlProvider.class, method = "delete")
    Integer delete(@Param("operatorName") String operatorName, @Param("id") List<Integer> id);

    @UpdateProvider(type = NotifyTemplateSqlProvider.class, method = "updateDefault")
    Integer updateDefault(@Param("companyId") Integer companyId, @Param("id") Integer id, @Param("method") Integer method);
}
