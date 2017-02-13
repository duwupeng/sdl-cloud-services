package com.talebase.cloud.ms.consumption.dao;

import com.talebase.cloud.base.ms.consume.domain.TAccount;
import com.talebase.cloud.base.ms.consume.domain.TAccountLine;
import com.talebase.cloud.base.ms.consume.dto.DAccountCondition;
import com.talebase.cloud.base.ms.consume.dto.DAccountConsumeResult;
import com.talebase.cloud.base.ms.consume.dto.DAccountPayResult;
import com.talebase.cloud.common.protocal.PageRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by suntree.xu on 2016-12-7.
 */
@Mapper
public interface AccountLineMapper {

    @SelectProvider(type = AccountLineProvider.class, method = "buildGetAccountConsumes")
    public List<DAccountConsumeResult> getAccountConsumes(DAccountCondition reqCondition, PageRequest pageReq) ;

    @SelectProvider(type = AccountLineProvider.class, method = "buildGetAccountConsumesTotal")
    @ResultType(java.lang.Integer.class)
    public int getAccountConsumeTotal(DAccountCondition reqCondition)
            ;
    @SelectProvider(type = AccountLineProvider.class, method = "buildGetAccountConsumesTotalList")
    public List<DAccountConsumeResult> getAccountConsumeTotalList(DAccountCondition reqCondition, int companyId);

    @SelectProvider(type = AccountLineProvider.class, method = "buildGetAccountPays")
    public List<DAccountPayResult> getAccountpays(@Param("account") String account, PageRequest pageReq) ;

    @SelectProvider(type = AccountLineProvider.class, method = "buildGetAccountPayTotal")
    public int getAccountPayTotal(@Param("account") String account);

    @SelectProvider(type = AccountLineProvider.class, method = "buildGetAccountPayTotalList")
    public List<DAccountPayResult> getAccountPayTotalList(@Param("companyId") int companyId);

    @Select("select * from t_account where company_id = #{companyId}")
    public TAccount getAccount(@Param("companyId") Integer companyId);

    @InsertProvider(type = AccountLineProvider.class, method = "buildInsertAccountLine")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public boolean operateConsume(TAccountLine tAccountLine);

    @UpdateProvider(type = AccountLineProvider.class, method = "buildUpdateAccount")
    public Integer UpdateAccount(TAccount tAccount);
}
