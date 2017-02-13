package com.talebase.cloud.ms.paper.dao;

import com.talebase.cloud.base.ms.paper.domain.TPage;
import com.talebase.cloud.base.ms.paper.domain.TPaperRemark;
import com.talebase.cloud.base.ms.paper.dto.DPage;
import com.talebase.cloud.base.ms.paper.dto.DPaperRemark;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import com.talebase.cloud.common.util.StringUtil;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * Created by bin.yang on 2016-12-08.
 */
public class PaperRemarkSqlProvider {

    public String insert(TPaperRemark tPaperRemark) {
        return (new SQL() {
            {
                INSERT_INTO("t_paper_remark");
                VALUES("start_score", "#{startScore}");
                VALUES("end_score", "#{endScore}");
                VALUES("description", "#{description}");
                VALUES("unicode", "#{unicode}");
                VALUES("version", "#{version}");
                VALUES("creator","#{creator}");
                VALUES("created_date","now()");
            }
        }).toString();
    }

    public String query(List<String> params) {
        return new SQL() {{
            SELECT("*");
            FROM("t_paper_remark");
            WHERE("unicode" + SqlBuilderUtil.buildInSql("params", params.size()));
        }}.toString();
    }

    public String update(TPaperRemark tPaperRemark) {
        return new SQL() {{
            UPDATE("t_paper_remark");
            if (tPaperRemark.getStartScore() != null) {
                SET("start_score=#{startScore}");
            }
            if (tPaperRemark.getEndScore() != null) {
                SET("end_score=#{endScore}");
            }

            if (!StringUtil.isEmpty(tPaperRemark.getDescription())) {
                SET("description=#{description}");
            }
            if (!StringUtil.isEmpty(tPaperRemark.getUnicode())) {
                SET("unicode=#{unicode}");
            }
            SET("version=#{version}");

            WHERE("id = #{id}");
        }}.toString();
    }
}
