package com.talebase.cloud.ms.exercise.dao;

import com.talebase.cloud.base.ms.examer.domain.TExercise;
import com.talebase.cloud.base.ms.examer.domain.TUserInfo;
import com.talebase.cloud.base.ms.examer.enums.TUserExamStatus;
import com.talebase.cloud.base.ms.examer.enums.TUserInfoStatus;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by erid.du on 2016-12-7.
 */
public class PaperSqlProvider {
    public String insert() {
       /* return new SQL() {{
            INSERT_INTO("t_exercise");
            VALUES("user_id", "#{userId}");
            VALUES("task_id", "#{taskId}");
            VALUES("paper_id", "#{paperId}");
//            VALUES("sub_score", "#{sub_score}");
//            VALUES("answer_score_detail", "#{answer_score_detail}");
            VALUES("start_time", "now()");
//            VALUES("end_time", "#{endTime}");
//            VALUES("serial_no", "#{serial_no}");
        }}.toString();*/
        String sql = " INSERT ignore INTO t_exercise(user_id,task_id,paper_id,start_time) \n" +
        " VALUES(#{userId},#{taskId},#{paperId},now()) ";
       return sql;
    }

    public String update(TExercise tExercise) {
        return new SQL() {{
            UPDATE("t_exercise");
            SET("sub_score = #{subScore}");
            WHERE("id = #{id}");
        }}.toString();
    }

    public String updateToSubmit(TExercise tExercise) {
        String sql = "update t_exercise set obj_score = #{objScore}, end_time = now(), answer_score_detail = #{answerScoreDetail}, serial_no = (\n" +
                    "select num from (\n" +
                        "select case when max(serial_no) is null then 1 else max(serial_no) + 1 end as num from t_exercise \n" +
                        "where task_id = #{taskId}\n" +
                    ") a\n" +
                ")\n" ;
        if(tExercise.getSubScore() != null){
            sql = sql + ", sub_score = #{subScore} \n ";
        }
        sql = sql +", submit_type = #{submitType} \n";
        sql = sql + " where task_id = #{taskId} and user_id = #{userId} and serial_no is null ";
        return sql;
    }


    public String getExerciseByTimer(Integer limit,Integer delaySecond){
/*        String sql = " select e.*,tui.account,tui.company_id from t_exercise e  \n" +
                " inner join t_task t on e.task_id = t.id \n" +
                " inner join t_user_exam ue on e.user_id = ue.user_id and t.id = ue.task_id \n" +
                " inner join t_user_info tui on tui.id = e.user_id \n"+
                " where e.start_time is not null \n" +
                " and e.end_time is null \n" +
                " and ( \n" +
                " t.end_date < date_add(now(), interval #{delaySecond} second ) \n" +
                " or DATE_ADD(e.start_time, INTERVAL case when t.exam_time is null then 0 else t.exam_time end minute) < date_add(now(), interval #{delaySecond} second) \n" +
                " )  and ue.status in ("+ TUserExamStatus.ENABLED.getValue()+") limit 0,#{limit}";*/
        String sql = "select tt.* from (" +
                " select e.*,tui.account,tui.company_id from t_exercise e  \n" +
                " inner join t_task t on e.task_id = t.id \n" +
                " inner join t_user_exam ue on e.user_id = ue.user_id and t.id = ue.task_id \n" +
                " inner join t_user_info tui on tui.id = e.user_id \n"+
                " where e.start_time is not null \n" +
                " and e.end_time is null \n" +
                " and t.end_date < date_add(now(), interval #{delaySecond} second ) \n" +
                " and ue.status in ("+ TUserExamStatus.ENABLED.getValue()+") \n" +
                " union \n" +
                " select e.*,tui.account,tui.company_id from t_exercise e  \n" +
                " inner join t_task t on e.task_id = t.id \n" +
                " inner join t_user_exam ue on e.user_id = ue.user_id and t.id = ue.task_id \n" +
                " inner join t_user_info tui on tui.id = e.user_id \n"+
                " where e.start_time is not null \n" +
                " and e.end_time is null \n" +
                " and t.exam_time >=0 \n" +
                " and  DATE_ADD(e.start_time, INTERVAL t.exam_time minute) < date_add(now(), interval #{delaySecond} second) \n" +
                " and ue.status in ("+ TUserExamStatus.ENABLED.getValue()+") \n" +
                ")tt limit 0,#{limit}";
        return sql;
    }
}
