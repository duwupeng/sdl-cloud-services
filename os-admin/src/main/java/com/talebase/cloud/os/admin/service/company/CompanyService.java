package com.talebase.cloud.os.admin.service.company;

import com.talebase.cloud.base.ms.admin.domain.TAdmin;
import com.talebase.cloud.base.ms.admin.domain.TRole;
import com.talebase.cloud.base.ms.admin.dto.*;
import com.talebase.cloud.base.ms.admin.enums.TAdminStatus;
import com.talebase.cloud.base.ms.common.domain.TCode;
import com.talebase.cloud.base.ms.common.enumes.TypeEnume;
import com.talebase.cloud.base.ms.consume.domain.TAccount;
import com.talebase.cloud.base.ms.examer.domain.TUserShowField;
import com.talebase.cloud.base.ms.examer.dto.DProjectTaskReq;
import com.talebase.cloud.base.ms.notify.domain.TNotifyTemplate;
import com.talebase.cloud.base.ms.notify.dto.DNotifyTemplate;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by rong on 2016/11/27.
 */
@Service
public class CompanyService {

    private static Logger logger = LoggerFactory.getLogger(CompanyService.class);

    @Autowired
    MsInvoker msInvoker;

    @Autowired
    RedisTemplate redisTemplate;

    final static String SERVICE_NAME = "ms-company";

    final static String ADMIN_SERVICE_NAME ="ms-admin";

    final static String COMMON_SERVICE_NAME ="ms-common";

    final static String CONSUMPTION_SERVICE_NAME ="ms-consumption";

    final static String EXAMER_SERVICE_NAME ="ms-examer";

    final static String NOTIFY_SERVICE_NAME ="ms-notify";

    final static String PROJECT_SERVICE_NAME ="ms-project";


    public DCompany getCompany(Integer id) {
        String servicePath = "http://" + SERVICE_NAME + "/serviceCompany/{companyId}";
        ServiceResponse<DCompany> response = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<DCompany>>(){},id);
        return  response.getResponse();
    }

    public List<TCode> getIndustry() {
        String servicePath = "http://" + COMMON_SERVICE_NAME + "/common/type?type="+TypeEnume.INDUSTRY.getType();
        ServiceResponse<List<TCode>> response = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<List<TCode>>>(){});
        return  response.getResponse();
    }

    /**
     * 重置密码
     * @param req
     * @return
     */
    public ServiceResponse<DAdmin> editAdminPassword(ServiceRequest<DAdmin> req) {
        String servicePath = "http://" + ADMIN_SERVICE_NAME + "/serviceAdmin/reSetPassword";
        ServiceResponse<DAdmin> response = msInvoker.post(servicePath,req, new ParameterizedTypeReference<ServiceResponse<DAdmin>>(){});
        return  response;
    }


    public ServiceResponse<DCompany> getAdmins(Integer id) {
        String servicePath = "http://" + SERVICE_NAME + "/serviceCompany/{companyId}";
        ServiceResponse<DCompany> response = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<DCompany>>(){},id);
        return  response;
    }

    /**
     * 初始化公司
     * @param req
     * @return
     */
    @Transactional(readOnly = false)
    public ServiceResponse<Integer> initializationCompany(ServiceRequest<DCompany> req, DcjAdmin cjAdmin, DbzAdmin bzAdmin, TAccount tAccount) {
        //初始化公司
        try{
            logger.info("初始化公司");
            String servicePath = "http://" + SERVICE_NAME + "/serviceCompany/insert";
            ServiceResponse<Integer> companyResponse = msInvoker.put(servicePath,req, new ParameterizedTypeReference<ServiceResponse<Integer>>(){});

            //初始化分组
            logger.info("初始化分组");
            servicePath = "http://" + ADMIN_SERVICE_NAME + "/serviceAdmin/group";
            DGroupCreateReq dGroupCreateReq = new DGroupCreateReq();
            dGroupCreateReq.setParentId(0);
            dGroupCreateReq.setName(req.getRequest().getShortName());
            dGroupCreateReq.setCompanyId(companyResponse.getResponse());
            ServiceResponse<Integer> groupResponse = msInvoker.post(servicePath,new ServiceRequest<>(req.getRequestHeader(),dGroupCreateReq), new ParameterizedTypeReference<ServiceResponse<Integer>>(){});

            //初始化角色
            logger.info("初始化角色");
            servicePath = "http://" + ADMIN_SERVICE_NAME + "/serviceAdmin/roles/findALLCompanyRoles";
            ServiceResponse<List<TRole>> rolesResponse = msInvoker.post(servicePath,new ServiceRequest<>(req.getRequestHeader(),0), new ParameterizedTypeReference<ServiceResponse<List<TRole>>>(){});
            List<Integer> roleIds = null;
            if(rolesResponse != null && rolesResponse.getResponse() != null && rolesResponse.getResponse().size() > 0){
                roleIds = new ArrayList<>();
                servicePath = "http://" + ADMIN_SERVICE_NAME + "/serviceAdmin/addRoleBySys";
                for(TRole tRole : rolesResponse.getResponse()){
                    tRole.setId(null);
                    tRole.setCompanyId(companyResponse.getResponse());
                    roleIds.add(msInvoker.post(servicePath,new ServiceRequest<>(req.getRequestHeader(),tRole), new ParameterizedTypeReference<ServiceResponse<Integer>>(){}).getResponse());
                }
            }
            //初始化管理员
            logger.info("初始化管理员");
            servicePath = "http://" + ADMIN_SERVICE_NAME + "/serviceAdmin/add";
            DAdmin dAdmin1 = new DAdmin();
            dAdmin1.setAccount(cjAdmin.getCjAccount());
            dAdmin1.setName(cjAdmin.getCjName());
            dAdmin1.setPassword(cjAdmin.getCjPassword());
            dAdmin1.setEmail(cjAdmin.getCjEmail());
            dAdmin1.setModifier(cjAdmin.getCjMobile());
            dAdmin1.setMobile(cjAdmin.getCjMobile());
            dAdmin1.setCreater("system");
            dAdmin1.setModifier("system");
            dAdmin1.setRoleId(roleIds.get(0));
            dAdmin1.setGroupId(groupResponse.getResponse());
            dAdmin1.setCompanyId(companyResponse.getResponse());
            dAdmin1.setStatus(TAdminStatus.EFFECTIVE.getValue());
            ServiceResponse<DAdmin> dAdminResponse = msInvoker.post(servicePath,new ServiceRequest<>(req.getRequestHeader(),dAdmin1), new ParameterizedTypeReference<ServiceResponse<DAdmin>>(){});
            DAdmin dAdmin2 = new DAdmin();
            dAdmin2.setAccount(bzAdmin.getBzAccount());
            dAdmin2.setName(bzAdmin.getBzName());
            dAdmin2.setPassword(bzAdmin.getBzPassword());
            dAdmin2.setEmail(bzAdmin.getBzEmail());
            dAdmin2.setModifier(bzAdmin.getBzMobile());
            dAdmin2.setMobile(bzAdmin.getBzMobile());
            dAdmin2.setCreater("system");
            dAdmin2.setModifier("system");
            dAdmin2.setRoleId(roleIds.get(1));
            dAdmin2.setGroupId(groupResponse.getResponse());
            dAdmin2.setCompanyId(companyResponse.getResponse());
            dAdmin2.setStatus(TAdminStatus.EFFECTIVE.getValue());
            ServiceResponse<DAdmin> bzdAdminResponse = msInvoker.post(servicePath,new ServiceRequest<>(req.getRequestHeader(),dAdmin2), new ParameterizedTypeReference<ServiceResponse<DAdmin>>(){});
            servicePath = "http://" + PROJECT_SERVICE_NAME + "/project/group/admin";
            TAdmin tAdmin = new TAdmin();
            tAdmin.setAccount(dAdmin1.getAccount());
            tAdmin.setOrgCode(dAdminResponse.getResponse().getOrgCode());
            tAdmin.setCompanyId(companyResponse.getResponse());

            msInvoker.post(servicePath,new ServiceRequest<>(req.getRequestHeader(),tAdmin), new ParameterizedTypeReference<ServiceResponse<String>>(){});
            TAdmin bztAdmin = new TAdmin();
            bztAdmin.setAccount(dAdmin2.getAccount());
            bztAdmin.setOrgCode(bzdAdminResponse.getResponse().getOrgCode());
            bztAdmin.setCompanyId(companyResponse.getResponse());
            bztAdmin.setCreater("system");
            bztAdmin.setModifier("system");
            msInvoker.post(servicePath,new ServiceRequest<>(req.getRequestHeader(),bztAdmin), new ParameterizedTypeReference<ServiceResponse<String>>(){});

            //初始化公司账户
            logger.info("初始化公司账户");
            servicePath = "http://" + CONSUMPTION_SERVICE_NAME + "/account/save";
            tAccount.setCompanyId(companyResponse.getResponse());
            tAccount.setModifier("system");
            tAccount.setCompanyValidate(DateUtil.addInteger(new Date(), Calendar.YEAR,1));
            msInvoker.put(servicePath,new ServiceRequest<>(req.getRequestHeader(),tAccount), new ParameterizedTypeReference<ServiceResponse<Integer>>(){});

            //初始化全局账号字段
            servicePath = "http://" + EXAMER_SERVICE_NAME + "/exam/iniCompanyUserField/"+companyResponse.getResponse();
            logger.info("初始化全局账号字段 :" + servicePath);
            msInvoker.post(servicePath, new ServiceRequest<>(),new ParameterizedTypeReference<ServiceResponse<Integer>>(){});

            //初始化邮件和短信模板
            logger.info("初始化邮件和短信模板");
            servicePath = "http://" + NOTIFY_SERVICE_NAME + "/smsTemplate/initializationTemplates";
            DNotifyTemplate dNotifyTemplate = new DNotifyTemplate();
            dNotifyTemplate.setCompanyId(companyResponse.getResponse());
            dNotifyTemplate.setCreator(cjAdmin.getCjAccount());
            msInvoker.post(servicePath,new ServiceRequest<>(req.getRequestHeader(),dNotifyTemplate), new ParameterizedTypeReference<ServiceResponse<List<DNotifyTemplate>>>(){});

            //redis
            redisTemplate.opsForValue().set("domain_"+req.getRequest().getDomain(),companyResponse.getResponse());
            logger.info("初始化完成");
            return  new ServiceResponse(companyResponse.getResponse());
        }catch (WrappedException we){
            logger.error("初始化公司业务异常", we);
            throw we;
        }catch (Exception e){
            logger.error("初始化公司系统异常", e);
            throw e;
        }
    }
}
