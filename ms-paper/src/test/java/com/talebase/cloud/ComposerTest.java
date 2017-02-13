package com.talebase.cloud;

import com.talebase.cloud.base.ms.paper.dto.DPaperComposer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric.du on 2017-1-3.
 */
public class ComposerTest {

    @Test
    public void add() {
        List list = new ArrayList<>();
        DPaperComposer dPaperComposer = new DPaperComposer();
        dPaperComposer.setScore(BigDecimal.valueOf(1));
        dPaperComposer.setSubjectId(1);
        dPaperComposer.setType(1);
        dPaperComposer.setUnicCode("xxxx1");
        dPaperComposer.setSeqNo(1);
        list.add(dPaperComposer);
        dPaperComposer = new DPaperComposer();
        dPaperComposer.setScore(BigDecimal.valueOf(1));
        dPaperComposer.setSubjectId(1);
        dPaperComposer.setType(1);
        dPaperComposer.setUnicCode("xxxx1");
        dPaperComposer.setSeqNo(1);
        list.add(dPaperComposer);


        List list1= new ArrayList<>();
         dPaperComposer = new DPaperComposer();
        dPaperComposer.setScore(BigDecimal.valueOf(1));
        dPaperComposer.setSubjectId(1);
        dPaperComposer.setType(1);
        dPaperComposer.setUnicCode("xxxx1");
        dPaperComposer.setSeqNo(1);
        list1.add(dPaperComposer);
        dPaperComposer = new DPaperComposer();
        dPaperComposer.setScore(BigDecimal.valueOf(1));
        dPaperComposer.setSubjectId(1);
        dPaperComposer.setType(1);
        dPaperComposer.setUnicCode("xxxx2");
        dPaperComposer.setSeqNo(1);
        list1.add(dPaperComposer);
        System.out.println("resutl:  --> " + list.equals(list1));
    }
}
