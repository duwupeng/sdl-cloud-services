package com.talebase.cloud.ms.paper.dao;

import com.talebase.cloud.base.ms.paper.domain.TBlank;
import com.talebase.cloud.base.ms.paper.dto.DBlank;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import com.talebase.cloud.common.util.StringUtil;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * Created by eric.du on 2016-12-6.
 */
public class BlankSqlProvider {
    public String insert(TBlank tBlank) {
        return (new SQL() {
            {
                INSERT_INTO("t_blank");
                VALUES("question", "#{question}");
                VALUES("answer", "#{answer}");
                VALUES("score", "#{score}");
                VALUES("score_rule", "#{scoreRule}");
                VALUES("`type`", "#{type}");
                VALUES("created_date", "now()");
                VALUES("creator", "#{creator}");
                VALUES("unicode", "#{unicode}");
                VALUES("version", "#{version}");
                VALUES("explanation", "#{explanation}");
                VALUES("style", "#{style}");
            }
        }).toString();
    }

    public String query(List<String> params) {
        return new SQL() {{
            SELECT("*");
            FROM("t_blank");
            WHERE("unicode" + SqlBuilderUtil.buildInSql("params", params.size()));
        }}.toString();
    }

    public String update(TBlank tBlank) {
        return new SQL() {{
            UPDATE("t_blank");
            if (!StringUtil.isEmpty(tBlank.getQuestion())) {
                SET("question=#{question}");
            }
            if (!StringUtil.isEmpty(tBlank.getAnswer())) {
                SET("answer=#{answer}");
            }
            if (tBlank.getExplanation() != null) {
                SET("explanation=#{explanation}");
            }
            if (tBlank.getScore() != null) {
                SET("score=#{score}");
            }
                SET("score_rule=#{scoreRule}");
                SET("type=#{type}");

            if (!StringUtil.isEmpty(tBlank.getUnicode())) {
                SET("unicode=#{unicode}");
            }
                SET("version=#{version}");
                SET("version_type=#{version_type}");
            if (tBlank.getStyle() != null) {
                SET("style=#{style}");
            }

            WHERE("id = #{id}");

        }}.toString();
    }
}
