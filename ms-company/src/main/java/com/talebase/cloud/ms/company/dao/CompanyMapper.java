package com.talebase.cloud.ms.company.dao;


import com.talebase.cloud.base.ms.admin.domain.TCompany;
import com.talebase.cloud.base.ms.admin.dto.DCompany;
import org.apache.ibatis.annotations.*;

/**
 * Created by rong on 2016/11/27.
 */
@Mapper
public interface CompanyMapper {

    @InsertProvider(type = CompanySqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(TCompany tCompany);

    @SelectProvider(type = CompanySqlProvider.class, method = "builCompanySql")
    DCompany getCompany(@Param("id") Integer id);

    @UpdateProvider(type = CompanySqlProvider.class, method = "updateCompanySql")
    Integer updateCompanyLogo(@Param("id") Integer id,@Param("logoName") String logoName,
                              @Param("name") String name,@Param("industryId") Integer industryId,
                              @Param("industryName") String industryName );
}
