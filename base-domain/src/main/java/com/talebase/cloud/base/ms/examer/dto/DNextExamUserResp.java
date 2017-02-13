package com.talebase.cloud.base.ms.examer.dto;

import com.talebase.cloud.base.ms.examer.domain.TExercise;
import com.talebase.cloud.base.ms.examer.domain.TScore;
import com.talebase.cloud.base.ms.examer.domain.TUserExam;

import java.util.List;

/**
 * Created by kanghong.zhao on 2017-1-18.
 */
public class DNextExamUserResp {

    private TExercise exercise;
    private TUserExam userExam;
    private List<TScore> scoreList;

    public TExercise getExercise() {
        return exercise;
    }

    public void setExercise(TExercise exercise) {
        this.exercise = exercise;
    }

    public TUserExam getUserExam() {
        return userExam;
    }

    public void setUserExam(TUserExam userExam) {
        this.userExam = userExam;
    }

    public List<TScore> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<TScore> scoreList) {
        this.scoreList = scoreList;
    }
}
