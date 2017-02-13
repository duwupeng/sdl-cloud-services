package com.talebase.cloud.ms.paper.dao;

import com.talebase.cloud.base.ms.paper.domain.TAttachment;
import com.talebase.cloud.base.ms.paper.dto.DAttachment;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import com.talebase.cloud.common.util.StringUtil;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * Created by bin.yang on 2016-11-28.
 */
public class AttachmentSqlProvider {
    public String insert(TAttachment tAttachment) {
        return (new SQL() {
            {
                INSERT_INTO("t_attachment");
                VALUES("question", "#{question}");
                VALUES("score", "#{score}");
                VALUES("score_rule", "#{scoreRule}");
                VALUES("type", "#{type}");
                VALUES("creator", "#{creator}");
                VALUES("created_date", "now()");
                VALUES("unicode", "#{unicode}");
                VALUES("version", "#{version}");
            }
        }).toString();
    }

    public String query(List<String> params) {
        return new SQL() {{
            SELECT("*");
            FROM("t_attachment");
            WHERE("unicode" + SqlBuilderUtil.buildInSql("params", params.size()));
        }}.toString();
    }

    public String update(TAttachment tAttachment) {
        return new SQL() {{
            UPDATE("t_attachment");
            if (!StringUtil.isEmpty(tAttachment.getQuestion())) {
                SET("question=#{question}");
            }
            if (tAttachment.getScore() != null) {
                SET("score=#{score}");
            }
            if (tAttachment.getScoreRule() != null) {
                SET("score_rule=#{scoreRule}");
            }
                SET("type=#{type}");
            if (!StringUtil.isEmpty(tAttachment.getUnicode())) {
                SET("unicode=#{unicode}");
            }
                SET("version=#{version}");
                SET("version_type=#{versionType}");
            WHERE("id = #{id}");
        }}.toString();
    }
}
