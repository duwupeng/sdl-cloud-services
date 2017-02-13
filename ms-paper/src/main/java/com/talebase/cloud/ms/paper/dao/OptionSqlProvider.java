package com.talebase.cloud.ms.paper.dao;

import com.talebase.cloud.base.ms.paper.domain.TOption;
import com.talebase.cloud.base.ms.paper.dto.DOption;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import com.talebase.cloud.common.util.StringUtil;
import org.apache.ibatis.jdbc.SQL;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by eric.du on 2016-12-6.
 */
public class OptionSqlProvider {

    public String insert(TOption tOption) {
        return (new SQL() {
            {
                if (tOption.getSubScore().compareTo(new BigDecimal(-999)) == 0) {
                    tOption.setSubScore(null);
                }
                INSERT_INTO("t_option");
                VALUES("question", "#{question}");
                VALUES("options", "#{options}");
                VALUES("answer", "#{answer}");
                VALUES("score", "#{score}");
                VALUES("sub_score", "#{subScore}");
                VALUES("score_rule", "#{scoreRule}");
                VALUES("type", "#{type}");
                VALUES("created_date", "now()");
                VALUES("creator", "#{creator}");
                VALUES("unicode", "#{unicode}");
                VALUES("version", "#{version}");
                VALUES("style", "#{style}");
            }
        }).toString();
    }

    public String query(List<String> params) {
        return new SQL() {{
            SELECT("*");
            FROM("t_option");
            WHERE("unicode" + SqlBuilderUtil.buildInSql("params", params.size()));
        }}.toString();
    }

    public String update(TOption tOption) {
        return new SQL() {{
            UPDATE("t_option");
            if (!StringUtil.isEmpty(tOption.getQuestion())) {
                SET("question=#{question}");
            }
            if (!StringUtil.isEmpty(tOption.getOptions())) {
                SET("options=#{options}");
            }
            if (!StringUtil.isEmpty(tOption.getAnswer())) {
                SET("answer=#{answer}");
            }
            if (tOption.getScore() != null) {
                SET("score=#{score}");
            }
            if (tOption.getSubScore() != null) {
                SET("sub_score=#{subScore}");
            }
            SET("score_rule=#{scoreRule}");
            SET("type=#{type}");
            if (!StringUtil.isEmpty(tOption.getUnicode())) {
                SET("unicode = #(unicode}");
            }
            SET("version=#{version}");
            SET("version_type=#{versionType}");
            SET("style=#{style}");
            WHERE("id = #{id}");
        }}.toString();
    }
}
