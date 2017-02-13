package com.talebase.cloud.ms.examer.service;

import com.talebase.cloud.base.ms.project.domain.TTaskProgress;
import com.talebase.cloud.ms.examer.dao.TaskProgressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by kanghong.zhao on 2016-12-22.
 */
@Service
public class TaskProgressService {

    @Autowired
    private TaskProgressMapper taskProgressMapper;

    public Integer joinTask(Integer taskId){
        return taskProgressMapper.updateAdd(taskId, 1, 0, 0, 0);
    }

    public Integer startTask(Integer taskId){
        return taskProgressMapper.updateAdd(taskId, 0, 1, 0, 0);
    }

    public Integer finishTask(Integer taskId){
        return taskProgressMapper.updateAdd(taskId, 0, 0, 1, 0);
    }

    public Integer markedTask(Integer taskId){
        return taskProgressMapper.updateAdd(taskId, 0, 0, 0, 1);
    }

    public void refreshTask(Integer taskId){
        TTaskProgress progress = taskProgressMapper.getForRealTime(taskId);
        taskProgressMapper.update(taskId, progress.getPreNum(), progress.getInNum(), progress.getFinishNum(), progress.getMarkedNum());
    }
}
