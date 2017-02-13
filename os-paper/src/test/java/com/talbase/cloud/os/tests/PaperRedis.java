package com.talbase.cloud.os.tests;

import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.base.ms.paper.enums.DPaperMode;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.SequenceUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by eric.du on 2016-12-28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RedisConfig.class)
public class PaperRedis {
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void genPaper() {
        System.out.println(new Date());
        String sequencor = SequenceUtil.generateSequenceNo("P");
//            redisTemplate.opsForValue().set(sequencor+"-mode",D);
        System.out.println("sequencor: " + sequencor);
        String hashKey = sequencor + "-new";
        redisTemplate.opsForValue().set(sequencor, 0);
        BoundHashOperations<String, String, DOption> boundHashOperations = redisTemplate.boundHashOps(hashKey);
        String unicode = "";
        DPaper dPaper = new DPaper();
        dPaper.setCreator("OperatorName");
        unicode = String.format("%s-%s-%d", sequencor, "H", 1);
        dPaper.setUnicode(unicode);
        dPaper.setType(1);
        dPaper.setName("考试");
        dPaper.setDuration(200);
        dPaper.setComment("考试说明");

        boundHashOperations.getOperations().opsForHash().put(hashKey, unicode, dPaper);


        boundHashOperations.getOperations().opsForHash().put(hashKey, "remarks", dPaper);

        Long sequence = redisTemplate.boundValueOps(sequencor).increment(1);

        System.out.println(new Date());
    }


    @Test
    public void genPaperMap() {
        redisTemplate.opsForValue().set("P0106150139462-mode", DPaperMode.完成);
//        BoundHashOperations<String, String, DOption> boundHashOperations = redisTemplate.boundHashOps("PMAP");
//        redisTemplate.opsForValue("");
//        DOption dOption = new DOption();
//        dOption.setUnicode("optionUniCode");
//        dOption.setType(4);
//        DOptionScoreSetting dOptionScoreSetting = new DOptionScoreSetting();
//        dOptionScoreSetting.setScore(BigDecimal.valueOf(10));
//        dOptionScoreSetting.setScoreRule(3);
//        dOption.setdOptionScoreSetting(dOptionScoreSetting);
//        DOptionStemSetting dOptionStemSetting = new DOptionStemSetting();
//        dOptionStemSetting.setQuestion("单选择题");
//        List<DOptionItem> dOptionItems = new ArrayList<>();
//        DOptionItem dOptionItem = new DOptionItem();
//        dOptionItem.setLabel("选项一");
//        dOptionItem.setMaskCode("maskCodeOne");
//        dOptionItem.setAnswer(true);
//        dOptionItems.add(dOptionItem);
//        dOptionItem = new DOptionItem();
//        dOptionItem.setLabel("选项二");
//        dOptionItem.setMaskCode("maskCodeTwo");
//        dOptionItem.setAnswer(false);
//        dOptionItems.add(dOptionItem);
//        dOptionStemSetting.setOptions(dOptionItems);
//
//        dOption.setdOptionStemSetting(dOptionStemSetting);
//        boundHashOperations.getOperations().opsForHash().put("P0105192434209-modify", "itemcode2", dOption);

    }

    @Test
    public void genGetPaperMap() {

        BoundHashOperations<String, String, List<String>> boundHashOperations = redisTemplate.boundHashOps("AUTH-ROLE-URLS");
//        List list = new ArrayList();
//        list.add("/ospaper/question/paper/preview1/");
//        list.add("/ospaper/question/stem1/");
        List list = boundHashOperations.get("role" + 4);
        System.out.println(list);

//        list = new ArrayList();
//        list.add("/ospaper/question/paper/preview/");
//        list.add("/ospaper/question/stem/");
//        boundHashOperations.getOperations().opsForHash().put("AUTH-ROLE-URLS", "role" + 2, list);
//
//        list = new ArrayList();
//        list.add("/ospaper/question/paper/preview/");
//        list.add("/ospaper/question/stem/");
//        boundHashOperations.getOperations().opsForHash().put("AUTH-ROLE-URLS", "role" + 3, list);

    }


    @Test
    public void genRemarks_设置题干下一步() {
        Set<String> boundHashOperations = redisTemplate.boundHashOps("AUTH-ROLE-URLS").keys();
        Iterator it = boundHashOperations.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }


    @Test
    public void testGetAll() {
        HashOperations hashOperations=redisTemplate.boundHashOps("P0114153729819-modify-1").getOperations().opsForHash();
        Set<String> boundHashOperations = redisTemplate.boundHashOps("P0114153729819-modify").keys();
        Iterator it = boundHashOperations.iterator();
        String key;
        while (it.hasNext()) {
            key= (String) it.next();
            hashOperations.put("P0114153729819-modify-1",key,
                    redisTemplate.boundHashOps("P0114153729819-modify").get(key));
        }
//        System.out.println(sets.stream().filter(item->item.indexOf("-A-")!=-1).collect(Collectors.toSet()));
    }


    @Test
    public void testCopy() {
//        redisTemplate.boundHashOps("P0114153729819-modify").getOperations().opsForHash().keys()


//        Iterator it = boundHashOperations.iterator();
//        while (it.hasNext()) {
//            System.out.println(it.next());
//        }
//        System.out.println(sets.stream().filter(item->item.indexOf("-A-")!=-1).collect(Collectors.toSet()));
    }
}
