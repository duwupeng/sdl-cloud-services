package com.talebase.cloud.base.ms.admin.domain;
  /**
  * @author auto-tool
  */public class TCompany {
   /**
   * 企业id
   */
   Integer id;

   /**
   * 公司名称
   */
   String name;

   /**
   * 公司简称
   */
   String shortName;

   /**
   * 公司地址
   */
   String address;

   /**
   * 公司logo
   */
   String logo;

   /**
   * 公司二级域名
   */
   String domain;

   /**
   * 公司状态,0:无效(invalid);1:有效(effective);-1:删除(deleted)
   */
   Integer status;

   /**
   * 公司网址
   */
   String webSite;

   /**
   * 公司所在地邮件编码
   */
   String postCode;

   /**
    * 行业id
    */
   Integer industryId;

   /**
    * 行业名称
    */
   String industryName;

   public Integer getId() {
    return id;
   }

   public void setId(Integer id) {
    this.id = id;
   }

   public String getName() {
    return name;
   }

   public void setName(String name) {
    this.name = name;
   }

   public String getShortName() {
    return shortName;
   }

   public void setShortName(String shortName) {
    this.shortName = shortName;
   }

   public String getAddress() {
    return address;
   }

   public void setAddress(String address) {
    this.address = address;
   }

   public String getLogo() {
    return logo;
   }

   public void setLogo(String logo) {
    this.logo = logo;
   }

   public String getDomain() {
    return domain;
   }

   public void setDomain(String domain) {
    this.domain = domain;
   }

   public Integer getStatus() {
    return status;
   }

   public void setStatus(Integer status) {
    this.status = status;
   }

   public String getWebSite() {
    return webSite;
   }

   public void setWebSite(String webSite) {
    this.webSite = webSite;
   }

   public String getPostCode() {
    return postCode;
   }

   public void setPostCode(String postCode) {
    this.postCode = postCode;
   }

   public Integer getIndustryId() {
    return industryId;
   }

   public void setIndustryId(Integer industryId) {
    this.industryId = industryId;
   }

   public String getIndustryName() {
    return industryName;
   }

   public void setIndustryName(String industryName) {
    this.industryName = industryName;
   }
  }
