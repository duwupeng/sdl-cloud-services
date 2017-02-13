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
 * Created by zhangchunlin on 2017-1-12.
 */
@Mapper
public interface AccountMapper {

    @InsertProvider(type = AccountProvider.class, method = "buildInsertAccount")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public boolean save(TAccount tAccount);

}
