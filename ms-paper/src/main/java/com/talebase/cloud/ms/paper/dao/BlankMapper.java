package com.talebase.cloud.ms.paper.dao;

import com.talebase.cloud.base.ms.paper.domain.TAttachment;
import com.talebase.cloud.base.ms.paper.domain.TBlank;
import com.talebase.cloud.base.ms.paper.dto.DBlank;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by eric.du on 2016-12-6.
 */
@Mapper
public interface BlankMapper {

    @InsertProvider(type = BlankSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(TBlank tBlank);

    @SelectProvider(type = BlankSqlProvider.class, method = "query")
    List<TBlank> query(@Param("params") List<String> params);

    @Select("select * from t_blank where id = #{id}")
    TBlank queryById(@Param("id") Integer id);

    @Select("select max(version)version from t_blank where unicode = #{unicode} and version_type = #{versionType}")
    Integer queryByUnicode(@Param("unicode") String unicode,@Param("versionType") Integer versionType);

    @Select("select * from t_blank where version = (select max(version)version from t_blank where unicode = #{unicode})  and unicode = #{unicode}")
    TBlank queryLatestByUnicode(@Param("unicode") String unicode);

    @UpdateProvider(type= BlankSqlProvider.class,method = "update")
    Integer update(TBlank tBlank);
}
