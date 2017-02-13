package com.talebase.cloud.ms.company.service;

import com.talebase.cloud.base.ms.admin.domain.TCompany;
import com.talebase.cloud.base.ms.admin.dto.DCompany;
import com.talebase.cloud.ms.company.dao.CompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rong on 2016/11/27.
 */
@Service
public class CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    public DCompany getCompany(Integer id){
        DCompany company = companyMapper.getCompany(id);
        return company;
    }

    public Integer insert(TCompany tCompany){
        companyMapper.insert(tCompany);
        return tCompany.getId();
    }

    public void updateCompanyLogo(DCompany dCompany){
        companyMapper.updateCompanyLogo(dCompany.getId(),dCompany.getLogo(),
                dCompany.getName(),dCompany.getIndustryId(),dCompany.getIndustryName());
    }
}
