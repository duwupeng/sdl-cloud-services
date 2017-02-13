package com.talebase.cloud.ms.paper.dao;


import com.talebase.cloud.base.ms.paper.domain.TPage;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by bin.yang on 2016-12-08.
 */

@Mapper
public interface PageMapper {

    @InsertProvider(type = PageSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(TPage tPage);

    @SelectProvider(type = PageSqlProvider.class, method = "query")
    List<TPage> query(@Param("params") List<String> params);

    @Select("select * from t_page where id = #{id}")
    TPage queryById(Integer id);

    @Select("select max(version)version from t_page where unicode = #{unicode}")
    Integer queryByUnicode(@Param("unicode") String unicode);

    @Select("select * from t_page where version = (select max(version)version from t_page where unicode = #{unicode})  and unicode = #{unicode}")
    TPage queryLatestByUnicode(@Param("unicode") String unicode);

    @UpdateProvider(type = PageSqlProvider.class, method = "update")
    Integer update(TPage tPage);
}
