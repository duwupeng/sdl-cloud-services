package com.talebase.cloud.ms.paper.dao;

import com.talebase.cloud.base.ms.paper.domain.TPaper;
import com.talebase.cloud.base.ms.paper.domain.TPaper;
import com.talebase.cloud.base.ms.paper.dto.DPaper;
import com.talebase.cloud.base.ms.paper.dto.DPaperQuery;
import com.talebase.cloud.base.ms.paper.enums.DPaperMode;
import com.talebase.cloud.base.ms.paper.enums.TPaperStatus;
import com.talebase.cloud.base.ms.paper.enums.TPaperType;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.util.NumberUtil;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import com.talebase.cloud.common.util.StringUtil;
import org.apache.ibatis.jdbc.SQL;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by bin.yang on 2016-12-08.
 */
public class PaperSqlProvider {

    public String insert(TPaper tPaper) {
        return (new SQL() {
            {
                INSERT_INTO("t_paper");
                VALUES("name", "#{name}");
                VALUES("company_id", "#{companyId}");
                VALUES("unicode", "#{unicode}");
                VALUES("version", "#{version}");
                VALUES("version_type", "#{versionType}");
                VALUES("composer", "#{composer}");
                VALUES("`type`", TPaperType.EXAM.getValue() + "");
                VALUES("mark", "#{mark}");
                VALUES("score", "#{score}");
                VALUES("mode", "#{mode}");
                if (tPaper.getStatus() != null) {
                    VALUES("status", "#{status}");
                }
                if (tPaper.getUsage() != null) {
                    VALUES("`usage`", "#{usage}");
                }
                if (tPaper.getTotalNum() != null) {
                    VALUES("total_num", "#{totalNum}");
                }
                if (tPaper.getSubjectNum() != null) {
                    VALUES("subject_num", "#{subjectNum}");
                }
                VALUES("creator", "#{creator}");
                VALUES("created_date", "now()");
                VALUES("comment", "#{comment}");
                if (tPaper.getDuration() != null) {
                    VALUES("duration", "#{duration}");
                }
            }
        }).toString();
    }

    public String queryList(Integer companyId, String creator, DPaperQuery dPaperQuery, PageRequest pageReq) {
        String sql = "select * from(" +
                " select a.id,a.name,a.company_id,a.unicode," +
                "a.status,a.mode,a.version,a.version_type,a.composer,a.type,a.mark," +
                "a.score,a.total_num,a.subject_num,a.creator,a.created_date,a.duration,a.comment,b.`usage`,case when creator = #{creator} then 0 else 1 end sort from t_paper a" +
                " JOIN(" +
                " select unicode,max(version)version,sum(`usage`)`usage` from t_paper " +
                " where status in (" + TPaperStatus.ENABLED.getValue() + ", " + TPaperStatus.DISABLED.getValue() + ")" +
                " GROUP BY unicode)b" +
                " on a.unicode = b.unicode and a.version = b.version" +
                " where a.company_id = #{companyId}" +
                " and status in (" + TPaperStatus.ENABLED.getValue() + ", " + TPaperStatus.DISABLED.getValue() + ")" +
                " and ((creator = #{creator}) or (creator != #{creator} and mode = " + DPaperMode.完成.getValue() + "))";
        if (dPaperQuery.isCreatedDateBegin() && dPaperQuery.isCreatedDateEnd()) {
            sql = sql + " and created_date between #{dPaperQuery.key1Begin} and #{dPaperQuery.key1End} ";
        } else if (dPaperQuery.isCreatedDateBegin() && !dPaperQuery.isCreatedDateEnd()) {
            sql = sql + " and created_date >= #{dPaperQuery.key1Begin} ";
        } else if (!dPaperQuery.isCreatedDateBegin() && dPaperQuery.isCreatedDateEnd()) {
            sql = sql + " and created_date <= #{dPaperQuery.key1End} ";
        }
        if (dPaperQuery.isDuration()) {
            sql = sql + " and concat(duration,'') = #{dPaperQuery.key2}";
        } else if (dPaperQuery.isTotalScore()) {
            sql = sql + " and score = #{dPaperQuery.key2}";
        } else if (dPaperQuery.isTotalNum()) {
            sql = sql + " and concat(total_num,'')= #{dPaperQuery.key2}";
        } else if (dPaperQuery.isUsage()) {
            sql = sql + " and concat(b.`usage`,'') = #{dPaperQuery.key2}";
        } else if (dPaperQuery.isCreator()) {
            sql = sql + " and creator like concat('%',#{dPaperQuery.key2},'%')";
        } else if (dPaperQuery.isName()) {
            sql = sql + " and name like concat('%',#{dPaperQuery.key2},'%')";
        }
        if (dPaperQuery.getStatus() != null) {
            sql = sql + " and status = #{dPaperQuery.status}";
        }
        sql = sql + " ORDER BY sort ASC,mode ASC,created_date DESC )c ";
        sql = sql + SqlBuilderUtil.buildPageLimit(pageReq);
        return sql;
    }

    public String queryCount(Integer companyId, String creator, DPaperQuery dPaperQuery) {
        String sql = "select count(*)cnt from (select * from(" +
                " select a.id,a.name,a.company_id,a.unicode," +
                "a.status,a.mode,a.version,a.version_type,a.composer,a.type,a.mark," +
                "a.score,a.total_num,a.subject_num,a.creator,a.created_date,a.duration,a.comment,b.`usage`,case when creator = #{creator} then 0 else 1 end sort from t_paper a" +
                " JOIN(" +
                " select unicode,max(version)version,sum(`usage`)`usage` from t_paper " +
                " where status in (" + TPaperStatus.ENABLED.getValue() + ", " + TPaperStatus.DISABLED.getValue() + ")" +
                " GROUP BY unicode)b" +
                " on a.unicode = b.unicode and a.version = b.version" +
                " where a.company_id = #{companyId}" +
                " and status in (" + TPaperStatus.ENABLED.getValue() + ", " + TPaperStatus.DISABLED.getValue() + ")" +
                " and ((creator = #{creator}) or (creator != #{creator} and mode = " + DPaperMode.完成.getValue() + "))";
        if (dPaperQuery.isCreatedDateBegin() && dPaperQuery.isCreatedDateEnd()) {
            sql = sql + " and created_date between #{dPaperQuery.key1Begin} and #{dPaperQuery.key1End} ";
        } else if (dPaperQuery.isCreatedDateBegin() && !dPaperQuery.isCreatedDateEnd()) {
            sql = sql + " and created_date >= #{dPaperQuery.key1Begin} ";
        } else if (!dPaperQuery.isCreatedDateBegin() && dPaperQuery.isCreatedDateEnd()) {
            sql = sql + " and created_date <= #{dPaperQuery.key1End} ";
        }
        if (dPaperQuery.isDuration()) {
            sql = sql + " and concat(duration,'') = #{dPaperQuery.key2}";
        } else if (dPaperQuery.isTotalScore()) {
            sql = sql + " and score = #{dPaperQuery.key2}";
        } else if (dPaperQuery.isTotalNum()) {
            sql = sql + " and concat(total_num,'')= #{dPaperQuery.key2}";
        } else if (dPaperQuery.isUsage()) {
            sql = sql + " and concat(b.`usage`,'') = #{dPaperQuery.key2}";
        } else if (dPaperQuery.isCreator()) {
            sql = sql + " and creator like concat('%',#{dPaperQuery.key2},'%')";
        } else if (dPaperQuery.isName()) {
            sql = sql + " and name like concat('%',#{dPaperQuery.key2},'%')";
        }
        if (dPaperQuery.getStatus() != null) {
            sql = sql + " and status = #{dPaperQuery.status}";
        }
        sql = sql + " ORDER BY sort ASC,mode ASC,created_date DESC)c) d";
        return sql;
    }

    public String queryListByIds(Integer companyId, String creator, List<Integer> ids, PageRequest pageReq) {
        String sql = "select * from (" +
                " SELECT a.id,a. NAME,a.company_id,a.unicode," +
                " 1 STATUS,a. MODE,a.version,a.version_type," +
                " a.composer,a.type,a.mark,a.score," +
                " a.total_num,a.subject_num,a.creator," +
                " a.created_date,a.duration,a.COMMENT,b.`usage`,a.sort" +
                " FROM" +
                " (" +
                " SELECT *, CASE WHEN creator = #{creator} THEN 0 ELSE 1 END sort" +
                " FROM t_paper" +
                " WHERE company_id = #{companyId} and id " + SqlBuilderUtil.buildInSql("ids", ids.size()) + ")a " +
                " join (select sum(`usage`) `usage`,unicode from t_paper " +
                " where company_id = #{companyId} and unicode in(select unicode from t_paper " +
                " where company_id = #{companyId} and id " + SqlBuilderUtil.buildInSql("ids", ids.size()) + ") GROUP BY unicode )b " +
                " on a.unicode = b.unicode" +
                " UNION ALL " +
                " select c.id,c. NAME,c.company_id,c.unicode,1 STATUS," +
                " c. MODE,c.version,c.version_type,c.composer," +
                " c.type,c.mark,c.score,c.total_num," +
                " c.subject_num,c.creator,c.created_date,c.duration," +
                " c.COMMENT,d.`usage`," +
                " case when creator = #{creator} then 3 else 4 end sort from t_paper c" +
                " JOIN(select unicode,max(version)version,sum(`usage`)`usage` from t_paper  " +
                " where company_id = #{companyId} and unicode not in (select unicode from t_paper " +
                " where company_id = #{companyId} and id " + SqlBuilderUtil.buildInSql("ids", ids.size()) + ") GROUP BY unicode)d on c.unicode = d.unicode and c.version = d.version  " +
                " where c.unicode not in (select unicode from t_paper where company_id = #{companyId} and id " + SqlBuilderUtil.buildInSql("ids", ids.size()) + ")" +
                " and mode = " + DPaperMode.完成.getValue() +
                " and status =" + TPaperStatus.ENABLED.getValue() +
                " )e" +
                " ORDER BY sort ASC,created_date desc ";
        sql = sql + SqlBuilderUtil.buildPageLimit(pageReq);
        return sql;
    }

    public String queryCountByIds(Integer companyId, String creator, List<Integer> ids) {
        String sql = "select count(*)cnt from (" +
                " SELECT a.id,a. NAME,a.company_id,a.unicode," +
                " 1 STATUS,a. MODE,a.version,a.version_type," +
                " a.composer,a.type,a.mark,a.score," +
                " a.total_num,a.subject_num,a.creator," +
                " a.created_date,a.duration,a.COMMENT,b.`usage`,a.sort" +
                " FROM" +
                " (" +
                " SELECT *, CASE WHEN creator = #{creator} THEN 0 ELSE 1 END sort" +
                " FROM t_paper" +
                " WHERE id " + SqlBuilderUtil.buildInSql("ids", ids.size()) + ")a " +
                " join (select sum(`usage`) `usage`,unicode from t_paper " +
                " where unicode in(select unicode from t_paper " +
                " where id " + SqlBuilderUtil.buildInSql("ids", ids.size()) + ") GROUP BY unicode )b " +
                " on a.unicode = b.unicode" +
                " UNION ALL " +
                " select c.id,c. NAME,c.company_id,c.unicode,1 STATUS," +
                " c. MODE,c.version,c.version_type,c.composer," +
                " c.type,c.mark,c.score,c.total_num," +
                " c.subject_num,c.creator,c.created_date,c.duration," +
                " c.COMMENT,d.`usage`," +
                " case when creator = #{creator} then 3 else 4 end sort from t_paper c" +
                " JOIN(select unicode,max(version)version,sum(`usage`)`usage` from t_paper  " +
                " where unicode not in (select unicode from t_paper " +
                " where id " + SqlBuilderUtil.buildInSql("ids", ids.size()) + ") GROUP BY unicode)d on c.unicode = d.unicode and c.version = d.version  " +
                " where c.unicode not in (select unicode from t_paper where id " + SqlBuilderUtil.buildInSql("ids", ids.size()) + ")" +
                " and mode = " + DPaperMode.完成.getValue() +
                " and status =" + TPaperStatus.ENABLED.getValue() +
                " )e" +
                " ORDER BY sort ASC,created_date desc";
        return sql;
    }

    public String queryPaperByUnicode(List<String> unicodes) {
        String sql = "SELECT * FROM t_paper a" +
                " JOIN (" +
                " SELECT unicode, max(version) version FROM t_paper" +
                " WHERE unicode " + SqlBuilderUtil.buildInSql("unicodes", unicodes.size()) +
                " GROUP BY unicode ) b " +
                " ON a.unicode = b.unicode" +
                " AND a.version = b.version ";

        return sql;
    }

    public String update(TPaper tPaper) {
        return new SQL() {{
            UPDATE("t_paper");
            if (tPaper.getComposer() != null) {
                SET("composer = #{composer}");
            }
            SET("name = #{name}");
            SET("comment = #{comment}");
            SET("duration = #{duration}");
            SET("mark = #{mark}");
            SET("score = #{score}");
            SET("`mode` = #{mode}");
            SET("`status` = #{status}");
            if (tPaper.getUsage() == null) {
                SET("`usage` = 0");
            } else {
                SET("`usage` = #{usage}");
            }
            SET("total_num = #{totalNum}");
            SET("subject_num = #{subjectNum}");
            if (tPaper.getVersion() != null) {
                SET("version = #{version}");
            }
            if (tPaper.getVersionType() == null && tPaper.getVersionType() == 0) {
                SET("version_type = 0");
            } else {
                SET("version_type = 1");
            }
            WHERE("id = #{id}");
        }}.toString();
    }

    public String updateStatus() {
        return new SQL() {{
            UPDATE("t_paper");
            SET("status = #{status}");
            WHERE("id = #{id}");
        }}.toString();
    }

    public String updateStatusByUnicode(String unicode, boolean status) {
        return new SQL() {{
            UPDATE("t_paper");
            SET("status = #{status}");
            WHERE("unicode = #{unicode}");
        }}.toString();
    }

    public String updateMode() {
        return new SQL() {{
            UPDATE("t_paper");
            SET("mode = #{mode}");
            SET("status = #{status}");
            WHERE("id=#{id}");
        }}.toString();
    }

    public String updateAddTimes(List<Integer> ids) {
        return new SQL() {{
            UPDATE("t_paper");
            SET("`usage` = `usage` + 1");
            WHERE("id " + SqlBuilderUtil.buildInSql("ids", ids.size()));
        }}.toString();
    }

    public String updateMinusTimes(List<Integer> ids) {
        return new SQL() {{
            UPDATE("t_paper");
            SET("`usage` = `usage` - 1");
            WHERE("id " + SqlBuilderUtil.buildInSql("ids", ids.size()));
        }}.toString();
    }

    public String deleteById() {
        return new SQL() {{
            UPDATE("t_paper");
            SET("status = " + TPaperStatus.DELETED.getValue());
            WHERE("id = #{id}");
        }}.toString();
    }

    public String deleteByUnicode() {
        return new SQL() {{
            UPDATE("t_paper");
            SET("status = " + TPaperStatus.DELETED.getValue());
            WHERE("unicode = #{unicode}");
        }}.toString();
    }

    public String getByIds(List<Integer> ids) {
        return new SQL() {{
            SELECT("*");
            FROM("t_paper");
            WHERE("status in ( " + TPaperStatus.ENABLED.getValue() + "," + TPaperStatus.DISABLED.getValue() + ")");
            WHERE("id " + SqlBuilderUtil.buildInSql("ids", ids.size()));
        }}.toString();
    }

    public String checkName(String paperUnicode, String name, Integer companyId) {
        return new SQL() {{
            SELECT("count(*)cnt");
            FROM("t_paper");
            WHERE("status in ( " + TPaperStatus.ENABLED.getValue() + "," + TPaperStatus.DISABLED.getValue() + ")");
            WHERE("unicode != #{paperUnicode}");
            WHERE("name = #{name}");
            WHERE("company_id = #{companyId}");
        }}.toString();
    }
}
