package com.talebase.cloud.ms.consumption.service;

import com.talebase.cloud.base.ms.consume.domain.TAccount;
import com.talebase.cloud.base.ms.consume.domain.TAccount;
import com.talebase.cloud.base.ms.consume.dto.DAccountCondition;
import com.talebase.cloud.base.ms.consume.dto.DAccountConsumeResult;
import com.talebase.cloud.base.ms.consume.dto.DAccountPayResult;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.DateUtil;
import com.talebase.cloud.ms.consumption.dao.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangchunlin on 2017-1-12.
 */
@Service
public class AccountService {

    @Autowired
    AccountMapper accountMapper;

    public ServiceResponse save(TAccount tAccount) {
        accountMapper.save(tAccount);
        return new ServiceResponse();
    }

}
