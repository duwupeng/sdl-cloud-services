package com.talebase.cloud.ms.paper.dao;

import com.talebase.cloud.base.ms.paper.domain.TAttachment;
import com.talebase.cloud.base.ms.paper.dto.DAttachment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by eric.du on 2016-12-6.
 */
@Mapper
public interface AttachmentMapper {
    @InsertProvider(type = AttachmentSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(TAttachment tAttachment);

    @SelectProvider(type = AttachmentSqlProvider.class, method = "query")
    List<TAttachment> query(@Param("params") List<String> params);

    @Select("select * from t_attachment where id = #{id}")
    TAttachment queryById(@Param("id") Integer id);

    @Select("select max(version)version from t_attachment where unicode = #{unicode} and version_type = #{versionType}")
    Integer queryByUnicode(@Param("unicode") String unicode,@Param("versionType") Integer versionType);

    @Select("select * from t_attachment where version = (select max(version)version from t_attachment where unicode = #{unicode}) and unicode = #{unicode}"  )
    TAttachment queryLatestByUnicode(@Param("unicode") String unicode);

    @UpdateProvider(type= AttachmentSqlProvider.class,method = "update")
    Integer update(TAttachment tAttachment);
}
