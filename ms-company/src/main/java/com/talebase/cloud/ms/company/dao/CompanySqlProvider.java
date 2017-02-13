package com.talebase.cloud.ms.company.dao;

import com.talebase.cloud.base.ms.admin.domain.TCompany;
import com.talebase.cloud.base.ms.admin.dto.DCompany;
import com.talebase.cloud.base.ms.admin.enums.TCompanyStatus;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

/**
 * Created by rong on 2016/11/27.
 */
public class CompanySqlProvider {
    public String insert(TCompany tOption) {
        return (new SQL() {
            {
                INSERT_INTO("t_company");
                VALUES("name", "#{name}");
                VALUES("short_name", "#{shortName}");
                VALUES("logo", "#{logo}");
                VALUES("domain", "#{domain}");
                VALUES("status", TCompanyStatus.EFFECTIVE.getValue()+"");
                VALUES("web_site", "#{webSite}");
                VALUES("post_code", "#{postCode}");
                VALUES("industry_id", "#{industryId}");
                VALUES("industry_name", "#{industryName}");
            }
        }).toString();
    }

    public String builCompanySql(Integer id){
        return  new SQL(){{
            SELECT(" * ");
            FROM(" t_company ");
            WHERE(" id = #{id} ");
        }}.toString();
    }

    public String updateCompanySql(Integer id,String logoName,String name,Integer industryId,String industryName){
        return  new SQL(){{
            UPDATE( " t_company ");
            if (!StringUtils.isEmpty(logoName)){
                SET(" logo = #{logoName} ");
            }
            if (!StringUtils.isEmpty(name)){
                SET(" name=#{name} ");
            }
            if (!StringUtils.isEmpty(industryId)){
                SET(" industry_id=#{industryId} ");
            }
            if (!StringUtils.isEmpty(industryName)){
                SET(" industry_name=#{industryName} ");
            }
            WHERE(" id =#{id} ");
        }}.toString();
    }
}
