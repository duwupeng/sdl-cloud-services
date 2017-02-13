package com.talebase.cloud.ms.examer.dao;

import com.talebase.cloud.base.ms.examer.domain.TScore;
import com.talebase.cloud.base.ms.examer.dto.DNextExamUserReq;
import com.talebase.cloud.base.ms.examer.dto.DUserShowField;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.util.SqlBuilderUtil;
import com.talebase.cloud.common.util.StringUtil;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * Created by daorong.li on 2016-12-19.
 */
public class ScoreSqlProvider {

    public String buildInsertSql(TScore tScore){
/*        return new SQL(){{
            INSERT_INTO("t_score");
            VALUES("serial_no", "#{serialNo}");
            VALUES("paper_id", "#{paperId}");
            VALUES("score",  "#{score}");
            VALUES("exam_id",  "#{examId}");
            VALUES("creater",  "#{creater}");
            VALUES("create_time",  "now()");
            VALUES("modify",  "#{modify}");
            VALUES("modify_time",  "#{modifyTime}");
            VALUES("done",  "#{done}");
            VALUES("total",  "#{total}");
        }}.toString();*/
        String sql = " INSERT ignore INTO t_score(serial_no,paper_id,score,exam_id,creater,create_time,modify,modify_time,done,total) \n" +
                " VALUES(#{serialNo},#{paperId},#{score},#{examId},#{creater},now(),#{modify},#{modifyTime},#{done},#{total}) ";
        return sql;
    }

    public String updateScore(TScore tScore){
        return new SQL(){{
            UPDATE(" t_score ");
            SET("score = #{score}");
            SET("done = #{done}");
            SET("total = #{total}");
            WHERE("id = #{id}");
        }}.toString();
    }

    public String getTaskDetailNum(Integer taskId){
        String sql = "select count(*) from t_exercise e \n" +
                "join t_user_exam ue on e.user_id = ue.user_id \n" +
                "and e.task_id = ue.task_id and ue.status not in (-1)\n" +
                "where e.end_time is not null\n" +
                "and ue.task_id = #{taskId}";

        return sql;
    }

    public String getTaskDetail(Integer taskId, PageRequest pageReq){
        String sql = "select ue.user_id, e.serial_no, e.sub_score from t_exercise e \n" +
                "join t_user_exam ue on e.user_id = ue.user_id \n" +
                "and e.task_id = ue.task_id and ue.status not in (-1)\n" +
                "where e.end_time is not null\n" +
                "and ue.task_id = #{taskId}\n" +
                "order by e.serial_no asc";
        sql += SqlBuilderUtil.buildPageLimit(pageReq);
        return sql;
    }

    public String getCompleteScoreOfExamer(Integer examId){
        String sql = "select * from t_score where total = done and exam_id = #{examId}";
        return sql;
    }

    public String getNextExamUser(Integer taskId, Integer seq, Boolean ingoreMarke, String preOrNext){
        String sql = "";
        if(!StringUtil.isEmpty(preOrNext) && ("up".equals(preOrNext) || "next".equals(preOrNext))){
            String sort = "next".equals(preOrNext) ? "asc" : "desc";
            String flag = "next".equals(preOrNext) ? ">" : "<";

            sql = "select * from t_exercise \n" +
                    "where task_id = #{taskId} \n" +
                    "and serial_no is not null \n";

            if(ingoreMarke){
                sql += "and sub_score is null \n";
            }
            sql += " and serial_no " + flag + " #{seq} \n";
            sql += " order by serial_no " + sort + " limit 1 ";
        }else{
            //按试题评卷切换为按考生评卷的时候，显示未打分考生时，升序获取第一个未完成打分的考试号
            if (ingoreMarke){
                sql = "select * from t_exercise \n" +
                        "where task_id = #{taskId} \n" +
                        "and serial_no is not null \n" +
                        " and sub_score is null \n" +
                        " order by serial_no asc limit 1";
            }else{
                sql = "select * from t_exercise \n" +
                        "where task_id = #{taskId} \n" +
                        "and serial_no is not null \n" +
                        " and serial_no = #{seq} \n";
            }

        }
        return sql;
    }

    public String getMaxSerialNo(Integer taskId, Boolean ingoreMarke){
        String sql = "select max(serial_no) from t_exercise where task_id = #{taskId}  ";
        if(ingoreMarke){
            sql = sql + " and sub_score is not null";
        }
        return sql;
    }

    public String getMarkedQz(Integer taskId, Integer finishCnt){
        return "select serial_no from t_score \n" +
                "where done = total \n" +
                "and exam_id in (\n" +
                "select id from t_user_exam \n" +
                "where task_id = #{taskId} \n" +
                "and status <> -1\n" +
                ")\n" +
                "group by serial_no \n" +
                "having count(*) >= #{finishCnt} order by serial_no asc \n";
    }

    public String getNextQzForUsers(Integer taskId, Integer serialNo){
        return "select e.user_id, e.task_id, e.serial_no, e.paper_id, \n" +
                "ue.id as user_exam_id, \n" +
                "s.creater as score_creater, s.done, s.total, s.score\n" +
                " from t_exercise e \n" +
                "join t_user_exam ue on e.task_id = ue.task_id and e.user_id = ue.user_id \n" +
                "left join t_score s on ue.id = s.exam_id and s.serial_no = #{serialNo} \n" +
                "where e.serial_no is not null \n" +
                "and ue.status <> -1 \n" +
                "and e.task_id = #{taskId} order by e.serial_no asc\n";
    }

}
