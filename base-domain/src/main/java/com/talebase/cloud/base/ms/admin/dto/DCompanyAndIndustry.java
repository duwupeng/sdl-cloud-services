package com.talebase.cloud.base.ms.admin.dto;

import com.talebase.cloud.base.ms.common.domain.TCode;

import java.util.List;

/**
 * Created by daorong.li on 2016-12-2.
 */
public class DCompanyAndIndustry {
    private DCompany dCompany;

    private List<TCode>  industryList;

    public DCompanyAndIndustry(DCompany dCompany,List<TCode>  industryList){
        this.dCompany = dCompany;
        this.industryList = industryList;
    }

    public DCompany getdCompany() {
        return dCompany;
    }

    public void setdCompany(DCompany dCompany) {
        this.dCompany = dCompany;
    }

    public List<TCode> getIndustryList() {
        return industryList;
    }

    public void setIndustryList(List<TCode> industryList) {
        this.industryList = industryList;
    }
}
