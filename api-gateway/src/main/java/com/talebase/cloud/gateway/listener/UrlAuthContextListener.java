package com.talebase.cloud.gateway.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric.du on 2016-12-30.
 */
@WebListener
public class UrlAuthContextListener implements ServletContextListener {
    private static Logger logger = LoggerFactory.getLogger(UrlAuthContextListener.class);
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        logger.info("----------:ServletContext销毁");
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
//        logger.info("----------:ServletContext  contextInitialized");
//        BoundHashOperations<String, String, List<String>> boundHashOperations = redisTemplate.boundHashOps("AUTH-PERMISSION-URLS");
//        List list = new ArrayList();
//
//        //全局帐号设定
//        list.add("POST-/osexamer/examer/saveGlobalAll");//保存账号设定信息
//        list.add("POST-/osexamer/examer/del");//删除全局可选字段信息
//        list.add("POST-/osexamer/examer/add");//添加可选信息
//        list.add("GET-/osexamer/examers");//获取全局账号信息
//
//        //通知模板
//        list.add("POST-/osproject/mailTemplates");//邮件模板-查询列表
//        list.add("POST-/osproject/smsTemplates");//短信模板-查询列表
//        list.add("GET-/osproject/mailTemplate/check");//邮件模板-检验模板名称
//        list.add("POST-/osproject/mailTemplate/del");//邮件模板-删除模板
//        list.add("POST-/osproject/mailTemplate");//邮件模板-创建模板
//        list.add("PUT-/osproject/mailTemplate");//邮件模板-修改模板
//        list.add("PUT-/osproject/mailTemplate/status/");//邮件模板-修改状态
//        list.add("GET-/osproject/smsTemplate/check");//短信模板-检查模板名称
//        list.add("POST-/osproject/smsTemplate/del");//短信模板-删除模板
//        list.add("POST-/osproject/smsTemplate");//短信模板-创建模板
//        list.add("PUT-/osproject/smsTemplate");//短信模板-修改模板
//        list.add("PUT-/osproject/smsTemplate/status/");//短信模板-修改状态
//
//        //使用统计
//        list.add("POST-/osconsumption/consumecenter/getPays");//使用统计-查询充值记录列表
//        list.add("POST-/osconsumption/consumecenter/getConsumes");//使用统计-查询消费记录列表
//        list.add("PUT-/osconsumption/consumecenter/operate");//使用统计-扣除T币和短信操作
//        list.add("GET-/osconsumption/consumecenter/exportPays");//使用统计-导出充值记录
//        list.add("GET-/osconsumption/consumecenter/exportConsume");//使用统计-导出消费记录
//        list.add("GET-/osconsumption/consumecenter/qureyAccount/");//使用统计-查询账户余额
//
//        //创建/修改项目
//        list.add("PUT-/osproject/project/tasks/");//编辑任务提交(json字符串版)
//        list.add("GET-/osproject/project/tasks/");//编辑任务查询
//        list.add("PUT-/osproject/project/");//修改项目
//        list.add("POST-/osproject/project");//创建项目
//        list.add("GET-/osproject/project/edit/");//项目编辑界面查询
//        list.add("DELETE-/osproject/task/");//删除任务
//        list.add("GET-/osproject/project/admins/rest/");//查询项目可选的管理员
//
//        //复制项目
//        list.add("POST-/osproject/project/copy");//项目复制
//
//        //启用/禁用项目
//        list.add("PUT-/osproject/project/status/");//修改项目状态
//        list.add("PUT-/osproject/task/status/");//项目列表中修改任务状态
//
//        //删除项目
//        list.add("DELETE-/osproject/project/");//删除项目
//
//        //通知日志
//        list.add("GET-/osproject/notifyRecord/export/");//通知日志-导出
//        list.add("POST-/osproject/notify/reSend/sms");//通知日志-重新发送短信
//        list.add("POST-/osproject/notify/reSend/email");//通知日志-重新发送邮件
//        list.add("POST-/osproject/notifyRecord/select");//通知日志-项目下拉框
//        list.add("POST-/osproject/notifyRecords");//通知日志-查询列表
//
//        //帐号管理
//        list.add("GET-/osexamer/examers/project");//账号管理列表
//
//        //新建/修改帐号
//        list.add("POST-/osexamer/examer");//创建单个账号
//        list.add("POST-/osexamer/examer/modifyUserForPerfect");//修改考生资料（用于登录后完善个人信息)
//        list.add("POST-/osexamer/examers");//创建多个考生账号
//        list.add("PUT-/osexamer/examer/");//编辑单个账号
//        list.add("POST-/osexamer/examers");//创建多个考生账号
//        list.add("GET-/osexamer/examer/demo/download");//导入模板下载
//        list.add("GET-/osexamer/examer/importLog/query");//查询导入日志
//        list.add("PUT-/osproject/project/");//修改项目
//        list.add("POST-/osexamer/examer/import");//导入考生账号
//        list.add("GET-/osexamer/examer/failLog/export");//错误日志导出
//        list.add("GET-/osexamer/examer/project/newField");//获取创建帐号(单个)字段显示列表
//
//        //重置密码
//        list.add("POST-/osexamer/examer/project/reSetPassword");
//
//        //发送通知
//        list.add("POST-/osproject/notify/send/examinee");//发送通知-考生
//        list.add("POST-/osproject/notify/send/examiner");//发送通知-评卷人
//
//        //导出帐号
//        list.add("GET-/osexamer/examer/project/export/all");//导出全部账号
//
//        //移除帐号
//        list.add("POST-/osexamer/examer/project/del");//移除
//
//        //帐号信息设定
//        list.add("GET-/osexamer/examer/project/fields");//获取项目下的账号信息
//        list.add("POST-/osexamer/examer/project/saveAll");//保存账号设定信息
//        list.add("GET-/osexamer/examer/project/modifyField");//获取修改账号
//
//        //数据管理
//
//        //导出详细数据
//
//        //考官
//        list.add("GET-/osexamer/evaluate/taskDetail/");//试卷评卷详情查询
//        list.add("GET-/osexamer/evaluate/taskList");//评卷人任务列表查询
//        list.add("GET-/osexamer/exam/examer");//考试前端-任务列表
//        list.add("POST-/osexamer/exam/score/save");//考试前端-保存打分
//        list.add("POST-/osexamer/exam/score/check");//考试前端-检查是否能保存打分
//        list.add("GET-/osexamer/evaluate/examer/markList");//评卷页面(按考生评卷)
//        list.add("GET-/osexamer/evaluate/stem/markList");//评卷页面(按试题评卷)
//
//        //创建/修改试卷
//        list.add("POST-/ospaper/question/papers/");//试卷库-试卷列表
//        list.add("POST-/ospaper/question/paper/");//试卷库-试卷状态变更
//        list.add("POST-/ospaper/question/paper/import/");//试卷库-导入试卷
//        list.add("PUT-/ospaper/question/stem/");//修改卷首,题干,结束语
//        list.add("POST-/ospaper/question/stem/");//新建卷首,题干
//        list.add("DELETE-/ospaper/question/item/");//删除题目
//        list.add("POST-/ospaper/question/flush/");//完成（任何一个完成按钮）
//        list.add("GET-/ospaper/question/remark/");//结束语查询
//        list.add("GET-/ospaper/question/paper/examer/"); //考试库获得源试卷
//        list.add("GET-/ospaper/question/paper/"); //获得试卷头部信息
//        list.add("GET-/ospaper/question/score/"); //设置分数查询
//        list.add("POST-/ospaper/question/score/"); //设置分数（下一步按钮）
//        list.add("GET-/ospaper/question/stem/");//设置题干查询
//        list.add("POST-/ospaper/question/sequence/");//设置题序
//        list.add("GET-/ospaper/question/paper/preview/"); //试卷预览
//        list.add("POST-/ospaper/question/score/"); //选择题,填空题,上传体设置分数
//        list.add("PUT-/ospaper/question/stem/style/"); //题目样式转换接口(应用到同类的题目）
//
//        //复制/预览试卷
//        list.add("POST-/ospaper/question/paper/copy/");//试卷库-复制试卷
//        list.add("GET-/ospaper/question/paper/all/");//试卷库-查询单个试卷
//        list.add("POST-/ospaper/question/paper/checkName/");//试卷库-检查试卷名是否重复
//        list.add("POST-/osproject/notify/preview/examinee");//预览-考生
//        list.add("POST-/osproject/notify/preview/examiner");//预览-评卷人
//
//        //导出试卷
//        list.add("GET-/ospaper/question/paper/export");//试卷库-导出试卷
//
//        //删除试卷
//        list.add("DELETE- /ospaper/question/paper/");//试卷库-删除试卷
//
//        //角色管理
//        list.add("PUT-/osadmin/role/name/");//修改角色名称
//        list.add("POST-/osadmin/roles/delete");//删除角色
//        list.add("POST-/osadmin/role");//添加角色
//        list.add("GET-/osadmin/roles/queryByPage");//界面分页查询角色
//        list.add("GET-/osadmin/roles/query");//管理界面查询角色
//
//        //管理员列表
//        list.add("PUT-/osadmin/admin");//修改管理员
//        list.add("POST-/osadmin/admin");//创建管理员
//        list.add("DELETE-/osadmin/admin");//删除管理员
//        list.add("GET-/osadmin/admin/export");//导出管理员
//        list.add("GET-/osadmin/subordinate");//查询下级管理员
//        list.add("GET-/osadmin/examiners");//查询考官
//        list.add("PUT-/osadmin/admin/status");//状态设置
//        list.add("GET-/osadmin/admins");//管理员列表
//        list.add("GET-/osadmin/admin/edit/");//获取修改管理员信息
//        list.add("GET-/osadmin/roleAndGroup");//获取分组列表与角色列表
//        list.add("PUT-/osadmin/admin/password");//重置密码
//
//        //分组管理
//        list.add("PUT-/osadmin/group/name/");//修改分组名称
//        list.add("DELETE-/osadmin/groups/delete");//删除分组
//        list.add("GET-/osadmin/groups/query");//查询分组
//        list.add("POST-/osadmin/group");//添加下级分组
//
//        //权限管理
//        list.add("PUT-/osadmin/role/permissions/");//分配角色权限
//        list.add("GET-/osadmin/role/permissionOfRole/");//根据角色查询权限列表
//
//        //企业信息
//        list.add("POST-/osadmin/company/upload");//修改企业信息
//        list.add("GET-/osadmin/company");//获取企业基础信息
//
//        //修改个人密码
//        list.add("PUT- /osadmin/company");//修改密码
//
//        //无权限控制
//        list.add("GET-/osproject/projects");//查询项目列表
//        //登陆模块
//        list.add("POST-/oslogin/login/findpass/");//登陆模块-找回密码
//        list.add("POST-/oslogin/loginCheckForApp");//登陆模块-app登录
//        list.add("POST-/oslogin/loginCheckForWeb");//登陆模块-web登陆
//        list.add("POST-/oslogin/verification/send/");//登陆模块-发送短信验证码
//        list.add("POST-/oslogin/verification/getValidateCode");//登陆模块-获取图片验证码
//        list.add("POST-/oslogin/loginOut");//登陆模块-退出登陆
//
//
//        boundHashOperations.getOperations().opsForHash().put("AUTH-PERMISSION-URLS", "permissionCode" + 1, list);
    }
}
