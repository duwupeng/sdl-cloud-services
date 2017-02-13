package com.talebase.cloud.ms.paper.service;

import com.talebase.cloud.base.ms.paper.domain.TPage;
import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.BeanConverter;
import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.ms.paper.dao.PageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric.du  on 2016-11-24.
 */

@Service
@Transactional(readOnly = true)
public class PageService {
    @Autowired
    private PageMapper pageMapper;

    @Transactional(readOnly = false)
    public DQuestionCount save(ServiceHeader serviceHeader, List<DPage> dPages) throws InvocationTargetException, IllegalAccessException {
        DQuestionCount dQuestionCount = new DQuestionCount();
        List<DUnicode> list = new ArrayList<>();
        TPage tPage;
        TPage tPageSaved;
        DUnicode dUnicode;
        for (DPage dPage : dPages) {
            tPageSaved = pageMapper.queryLatestByUnicode(dPage.getUnicode());
            tPage = changeDPageToTPage(dPage);
            Integer version;
            dUnicode = new DUnicode();
            dUnicode.setUnicode(tPage.getUnicode());
            if (tPageSaved == null) {
                version = 1;
            } else {
                if (tPageSaved.equals(tPage)) {
                    dUnicode.setId(tPageSaved.getId());
                    list.add(dUnicode);
                    continue;
                } else {
                    version = tPageSaved.getVersion() + 1;
                }
            }
            tPage.setVersion(version);
            tPage.setCreator(serviceHeader.getOperatorName());
            pageMapper.insert(tPage);
            dQuestionCount.setHasChange(true);
            dUnicode.setId(tPage.getId());
            list.add(dUnicode);
        }
        dQuestionCount.setUnicodeList(list);
        return dQuestionCount;
    }

    public ServiceResponse<List<DPage>> query(String unicodes) throws InvocationTargetException, IllegalAccessException {
        List<String> paramList = StringUtil.toStrListByComma(unicodes);
        List<TPage> tPages = pageMapper.query(paramList);
        List<DPage> dPages = new ArrayList<>();
        for (TPage tPage : tPages) {
            DPage dPage = changeTPageToDPage(tPage);
            dPages.add(dPage);
        }
        return new ServiceResponse(dPages);
    }

    public DPage queryByUnicode(String unicode) throws InvocationTargetException, IllegalAccessException {
        TPage tPage = pageMapper.queryLatestByUnicode(unicode);
        DPage dPage = changeTPageToDPage(tPage);
        return dPage;
    }

    public DPage queryById(Integer id) throws InvocationTargetException, IllegalAccessException {
        TPage tPage = pageMapper.queryById(id);
        DPage dPage = changeTPageToDPage(tPage);
        return dPage;
    }

    @Transactional(readOnly = false)
    public ServiceResponse update(ServiceHeader serviceHeader, DPage dPage) throws InvocationTargetException, IllegalAccessException {
        TPage tPage = changeDPageToTPage(dPage);
        Integer count = pageMapper.update(tPage);
        return new ServiceResponse(count);
    }

    private DPage changeTPageToDPage(TPage tPage) throws InvocationTargetException, IllegalAccessException {
        DPage dPage = new DPage();
        dPage.setId(tPage.getId());
        dPage.setUnicode(tPage.getUnicode());
        dPage.setCreator(tPage.getCreator());
//        BeanConverter.copyProperties(dPage, tPage);
        DPageStyleSetting dPageStyleSetting = new DPageStyleSetting();
        dPageStyleSetting.setSubjectOrder(tPage.getSubjectOrder());
        dPageStyleSetting.setOptionOrder(tPage.getOptionOrder());
        dPage.setdPageStyleSetting(dPageStyleSetting);
        return dPage;
    }

    private TPage changeDPageToTPage(DPage dPage) throws InvocationTargetException, IllegalAccessException {
        TPage tPage = new TPage();
        tPage.setId(dPage.getId());
        tPage.setUnicode(dPage.getUnicode());
        tPage.setCreator(dPage.getCreator());
//        BeanConverter.copyProperties(tPage, dPage);
        DPageStyleSetting dPageStyleSetting = dPage.getdPageStyleSetting();
        tPage.setSubjectOrder(dPageStyleSetting.getSubjectOrder());
        tPage.setOptionOrder(dPageStyleSetting.getOptionOrder());
        return tPage;
    }
}