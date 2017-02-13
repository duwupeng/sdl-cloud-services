package com.talebase.cloud.ms.paper.dao;


import com.talebase.cloud.base.ms.paper.domain.TBlank;
import com.talebase.cloud.base.ms.paper.domain.TInstruction;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by bin.yang on 2016-12-08.
 */

@Mapper
public interface InstructionMapper {

    @InsertProvider(type = InstructionSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(TInstruction tInstruction);

    @SelectProvider(type = InstructionSqlProvider.class, method = "query")
    List<TInstruction> query(@Param("params") List<String> params);

    @Select("select * from t_instruction where id = #{id}")
    TInstruction queryById(@Param("id") Integer id);

    @Select("select max(version) version from t_instruction where unicode = #{unicode}")
    Integer queryByUnicode(@Param("unicode") String unicode);

    @Select("select * from t_instruction where version = (select max(version)version from t_instruction where unicode = #{unicode})  and unicode = #{unicode}")
    TInstruction queryLatestByUnicode(@Param("unicode") String unicode);

    @UpdateProvider(type = InstructionSqlProvider.class, method = "update")
    Integer update(TInstruction tInstruction);
}
