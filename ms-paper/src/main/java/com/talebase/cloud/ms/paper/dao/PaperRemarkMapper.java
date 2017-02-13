package com.talebase.cloud.ms.paper.dao;


import com.talebase.cloud.base.ms.paper.domain.TPaperRemark;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by bin.yang on 2016-12-08.
 */

@Mapper
public interface PaperRemarkMapper {

    @InsertProvider(type = PaperRemarkSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(TPaperRemark tPaperRemark);

    @Select("select * from t_paper_remark where id = #{id}")
    TPaperRemark queryById(@Param("id")Integer id);

    @Select("select max(version) version from t_paper_remark where unicode = #{unicode}")
    Integer queryByUnicode(@Param("unicode")String unicode);

    @Select("select * from t_paper_remark where version = (select max(version)version from t_paper_remark where unicode = #{unicode})  and unicode = #{unicode}")
    TPaperRemark queryLatestByUnicode(@Param("unicode") String unicode);

    @UpdateProvider(type = PaperRemarkSqlProvider.class, method = "update")
    Integer update(TPaperRemark tPaperRemark);
}
