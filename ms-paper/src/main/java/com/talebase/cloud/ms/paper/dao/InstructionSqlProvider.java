package com.talebase.cloud.ms.paper.dao;

import com.talebase.cloud.base.ms.paper.domain.TInstruction;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import com.talebase.cloud.common.util.StringUtil;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * Created by bin.yang on 2016-12-08.
 */
public class InstructionSqlProvider {

    public String insert(TInstruction tInstruction) {
        return (new SQL() {
            {
                INSERT_INTO("t_instruction");
                VALUES("comment", "#{comment}");
                VALUES("file_path", "#{filePath}");
                VALUES("unicode", "#{unicode}");
                VALUES("version", "#{version}");
                VALUES("creator", "#{creator}");
                VALUES("created_date", "now()");
            }
        }).toString();
    }

    public String query(List<String> params) {
        return new SQL() {{
            SELECT("*");
            FROM("t_instruction");
            WHERE("unicode" + SqlBuilderUtil.buildInSql("params", params.size()));
        }}.toString();
    }

    public String update(TInstruction tInstruction) {
        return new SQL() {{
            UPDATE("t_instruction");
            if (!StringUtil.isEmpty(tInstruction.getComment())) {
                SET("comment=#{comment}");
            }
            if (!StringUtil.isEmpty(tInstruction.getFilePath())) {
                SET("file_path=#{filePath}");
            }
            if (!StringUtil.isEmpty(tInstruction.getUnicode())) {
                SET("unicode=#{unicode}");
            }
                SET("version=#{version}");
            WHERE("id = #{id}");
        }}.toString();
    }
}
