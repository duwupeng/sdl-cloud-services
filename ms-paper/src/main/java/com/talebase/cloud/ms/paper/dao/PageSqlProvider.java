package com.talebase.cloud.ms.paper.dao;

import com.talebase.cloud.base.ms.paper.domain.TPage;
import com.talebase.cloud.base.ms.paper.dto.DPage;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import com.talebase.cloud.common.util.StringUtil;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * Created by bin.yang on 2016-12-08.
 */
public class PageSqlProvider {

    public String insert(TPage tPage) {
        return (new SQL() {
            {
                INSERT_INTO("t_page");
                VALUES("subject_order", "#{subjectOrder}");
                VALUES("option_order", "#{optionOrder}");
                VALUES("created_date", "now()");
                VALUES("creator", "#{creator}");
                VALUES("unicode", "#{unicode}");
                VALUES("version", "#{version}");
            }
        }).toString();
    }

    public String query(List<String> params) {
        return new SQL() {{
            SELECT("*");
            FROM("t_page");
            WHERE("unicode" + SqlBuilderUtil.buildInSql("params", params.size()));
        }}.toString();
    }

    public String update(TPage tPage) {
        return new SQL() {{
            UPDATE("t_page");
                SET("subject_order=#{subjectOrder}");
                SET("option_order=#{optionOrder}");
            if (!StringUtil.isEmpty(tPage.getUnicode())) {
                SET("unicode=#{unicode}");
            }
                SET("version=#{version}");
            WHERE("id = #{id}");
        }}.toString();
    }
}
