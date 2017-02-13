package com.talebase.cloud.ms.paper.dao;

import com.talebase.cloud.base.ms.paper.domain.TBlank;
import com.talebase.cloud.base.ms.paper.domain.TOption;
import com.talebase.cloud.base.ms.paper.dto.DOption;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by eric.du on 2016-12-6.
 */
@Mapper
public interface OptionMapper {

    @InsertProvider(type = OptionSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(TOption tOption);

    @SelectProvider(type = OptionSqlProvider.class, method = "query")
    List<TOption> query(@Param("params") List<String> params);

    @Select("select * from t_option where id = #{id}")
    TOption queryById(Integer id);

    @Select("select max(version)version from t_option where unicode = #{unicode} and version_type = #{versionType}")
    Integer queryByUnicode(@Param("unicode") String unicode, @Param("versionType") Integer versionType);

    @Select("select * from t_option where version = (select max(version)version from t_option where unicode = #{unicode})  and unicode = #{unicode}")
    TOption queryLatestByUnicode(@Param("unicode") String unicode);

    @UpdateProvider(type = OptionSqlProvider.class, method = "update")
    Integer update(TOption tOption);
}
