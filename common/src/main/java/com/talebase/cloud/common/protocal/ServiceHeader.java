package com.talebase.cloud.common.protocal;

import org.springframework.util.StringUtils;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * 请求头
 * Created by eric on 16/11/14.
 */
public class ServiceHeader {
    /**
     * 操作人ID
     *
     */
    public int operatorId; // required
    /**
     * 用户账号
     *
     */
    public String account; // required
    /**
     * 操作人名称
     *
     */
    public String operatorName; // required
    /**
     * 客户编号
     *
     */
    public int customerId; // required
    /**
     * 客户名称
     *
     */
    public String customerName; // optional
    /**
     * 调用源
     *
     *
     * @see CallerFrom
     */
    public CallerFrom callerFrom; // optional
    /**
     * 调用源IP
     *
     */
    public String callerIP; // optional
    /**
     * 登录标记
     *
     */
    public String token; // optional
    /**
     * 会话编号
     *
     */
    public String transactionId; // optional

    /**
     * 会话序号
     *
     */
    public int seqId; // optional

    /**
     * 公司域名
     */
    public String companyDomain;

    /**
     * 会话序号
     */
    public java.util.List<java.lang.String> permissions = new ArrayList<>(); // optional

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public CallerFrom getCallerFrom() {
        return callerFrom;
    }

    public void setCallerFrom(CallerFrom callerFrom) {
        this.callerFrom = callerFrom;
    }

    public String getCallerIP() {
        return callerIP;
    }

    public void setCallerIP(String callerIP) {
        this.callerIP = callerIP;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    /**
     * 用户所属组织编码
     */
    public String orgCode; //optional

    /**
     * 所在公司id
     */
    public Integer companyId; //optional

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }
    public void setSeqId() {
        this.seqId = this.seqId+1;
    }

    public java.util.List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 判断用户是否有权限
     * @param rightCode
     * @return
     */
    public boolean hasRight(String rightCode){
        if (this.permissions == null || StringUtils.isEmpty(rightCode))
            return false;
        for(String code : this.permissions){
            if (code.equals(rightCode))
                return  true;
        }
        return false;
    }

    public String getCompanyDomain() {
        return companyDomain;
    }

    public void setCompanyDomain(String companyDomain) {
        this.companyDomain = companyDomain;
    }
}
