package com.talebase.cloud.ms.consumption.dao;

import com.talebase.cloud.base.ms.consume.domain.TAccount;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by zhangchunlin on 2017-1-12.
 */
public class AccountProvider {

    /**
     * 保存
     *
     * @param tAccount
     * @return
     */
    public String buildInsertAccount(TAccount tAccount) {
        return new SQL() {{
            INSERT_INTO("t_account");
            VALUES("company_id", "#{companyId}");
            VALUES("point_balance", "#{pointBalance}");
            VALUES("sms_balance", "#{smsBalance}");
            VALUES("vail_code", "#{vailCode}");
            VALUES("modified_date", "now()");
            VALUES("modifier", "#{modifier}");
            VALUES("company_validate", "#{companyValidate}");
            VALUES("point_valid", "#{pointValid}");
            VALUES("sms_valid", "#{smsValid}");
            VALUES("peraccount_valid", "#{peraccountValid}");
        }}.toString();
    }

}
