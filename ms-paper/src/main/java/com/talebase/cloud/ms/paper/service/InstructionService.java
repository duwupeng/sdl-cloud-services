package com.talebase.cloud.ms.paper.service;

import com.talebase.cloud.base.ms.paper.domain.TInstruction;
import com.talebase.cloud.base.ms.paper.dto.DInstruction;
import com.talebase.cloud.base.ms.paper.dto.DQuestionCount;
import com.talebase.cloud.base.ms.paper.dto.DUnicode;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.BeanConverter;
import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.ms.paper.dao.InstructionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bin.yang on 2016-12-09
 */

@Service
@Transactional(readOnly = true)
public class InstructionService {
    @Autowired
    private InstructionMapper instructionMapper;

    @Transactional(readOnly = false)
    public  DQuestionCount save(ServiceHeader serviceHeader, List<DInstruction> dInstructions) throws InvocationTargetException, IllegalAccessException {
        DQuestionCount dQuestionCount = new DQuestionCount();
        List<DUnicode> list = new ArrayList<>();
        TInstruction tInstruction;
        TInstruction tInstructionSaved;
        DUnicode dUnicode ;
        for (DInstruction dInstruction : dInstructions) {
            tInstructionSaved = instructionMapper.queryLatestByUnicode(dInstruction.getUnicode());
            tInstruction = new TInstruction();
            BeanConverter.copyProperties(tInstruction, dInstruction);
            Integer version;
            dUnicode = new DUnicode();
            dUnicode.setUnicode(tInstruction.getUnicode());
            if (tInstructionSaved == null) {
                version = 1;
            } else {
                if(tInstructionSaved.equals(tInstruction)){
                    dUnicode.setId(tInstructionSaved.getId());
                    list.add(dUnicode);
                    continue;
                }else{
                    version = tInstructionSaved.getVersion() + 1;
                }
            }
            tInstruction.setVersion(version);
            tInstruction.setCreator(serviceHeader.getOperatorName());
            instructionMapper.insert(tInstruction);
            dQuestionCount.setHasChange(true);
            dUnicode.setId(tInstruction.getId());
            list.add(dUnicode);
        }
        dQuestionCount.setUnicodeList(list);
        return dQuestionCount;
    }

    public ServiceResponse<List<DInstruction>> query(String unicodes) throws InvocationTargetException, IllegalAccessException {
        List<String> paramList = StringUtil.toStrListBySpliter(unicodes,",");
        List<TInstruction> tInstructions = instructionMapper.query(paramList);
        List<DInstruction> dInstructions = new ArrayList<>();
        for (TInstruction tInstruction : tInstructions) {
            DInstruction dInstruction = changeTInstructionToDInstruction(tInstruction);
            dInstructions.add(dInstruction);
        }
        return new ServiceResponse(dInstructions);
    }

    public DInstruction queryByUnicode(String unicode) throws InvocationTargetException, IllegalAccessException {
        TInstruction tInstruction = instructionMapper.queryLatestByUnicode(unicode);
        DInstruction dInstruction = changeTInstructionToDInstruction(tInstruction);
        return dInstruction;
    }
    public DInstruction queryById(Integer id) throws InvocationTargetException, IllegalAccessException {
        TInstruction tInstruction = instructionMapper.queryById(id);
        DInstruction dInstruction = changeTInstructionToDInstruction(tInstruction);
        return dInstruction;
    }

    @Transactional(readOnly = false)
    public ServiceResponse update(ServiceHeader serviceHeader, DInstruction dInstruction) throws InvocationTargetException, IllegalAccessException {
        TInstruction tInstruction = changeDInstructionToTInstruction(dInstruction);
        Integer count = instructionMapper.update(tInstruction);
        return new ServiceResponse(count);
    }

    private DInstruction changeTInstructionToDInstruction(TInstruction tInstruction) throws InvocationTargetException, IllegalAccessException {
        DInstruction dInstruction = new DInstruction();
        dInstruction.setCreator(tInstruction.getCreator());
        dInstruction.setId(tInstruction.getId());
        dInstruction.setUnicode(tInstruction.getUnicode());
        dInstruction.setComment(tInstruction.getComment());
        dInstruction.setFilePath(tInstruction.getFilePath());
//        BeanConverter.copyProperties(dInstruction, tInstruction);
        return dInstruction;
    }

    private TInstruction changeDInstructionToTInstruction(DInstruction dInstruction) throws InvocationTargetException, IllegalAccessException {
        TInstruction tInstruction = new TInstruction();
        tInstruction.setCreator(dInstruction.getCreator());
        tInstruction.setId(dInstruction.getId());
        tInstruction.setUnicode(dInstruction.getUnicode());
        tInstruction.setComment(dInstruction.getComment());
        tInstruction.setFilePath(dInstruction.getFilePath());
//        BeanConverter.copyProperties(tInstruction, dInstruction);
        return tInstruction;
    }
}