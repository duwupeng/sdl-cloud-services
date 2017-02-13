package com.talebase.cloud.common.exception;

/**
 * Created by eric on 16/11/10.
 */
//业务异常
public enum BizEnums implements ExceptionEnums{
    /**
     *无权限访问该URL
     **/
    NOACCESSRIGHT(10001, "not authorized"),
    UNIQUE_KEY(10001, "答题生成出错"),

    DataTypeErr(20000001, "数据类型不匹配"),

    //通知模块
    NOTIFY_UNIQUE_KEY(28004001, "模板名称重复"),
    INSUFFICIENT_NUMBER_OF_REMAINING_SMS(28004002,"短信剩余量不足,请选择其他方式进行发送；如有需要，请找客服进行充值，谢谢"),
    FAILED_DEDUCTIONS(28004003,"扣款失败，请重新尝试"),
    NO_SEND_EXAMER(28004004,"未选中评卷人，请在项目中添加评卷人"),
    NO_SEND_EXAMEE(28004005,"没有可以发送信息的考生"),
    NO_EMAIL_AND_SMS(28004006,"没有可以发送的邮箱和手机，请选择正确的发送方式"),

    //通知模板
    NO_TEMPLATE_CONTENT(27005001,"请输入模板内容"),
    NO_TEMPLATE_NAME(27005002,"请输入模板名称"),
    NO_TEMPLATE_SUBJECT(27005003,"请输入模板主题"),
    NO_PERMISSION_OPERATION_TEMPLATE(27005004,"您没有权限操作他人创建的模板"),

    //管理员设置模块
    ADMIN_ADD_EXIST(28002001,"存在相同帐号"),
    ADMIN_MODIFY_EXIST(28002002,"已存在相同帐号"),
    ADMIN_OLDPASSWORD_NOEXIST(28002003,"不存在旧密码"),
    ADMIN_PASSWORD_NOEXIST(28002004,"请输入新密码"),
    ADMIN_SAME_PASSWORD(28002005,"新密码和旧密码输入一样，请重新输入"),
    Password_TooShort(28002006, "新密码不能小于6位"),
    Password_NotVail(28008007, "新密码必须含有至少一个数字、字母和字符"),
    Password_CannotHas_Chinese(28008019, "新密码不能包含汉字"),
    UpdatePasswordByOther(28008020, "只能修改当前帐号的密码"),
    ADMIN_CREATE_PASSWORD_NOEXIST(280020021,"请输入密码"),
    ADMIN_CREATE_Password_TooShort(28002022, "密码不能小于6位"),
    ADMIN_CREATE_Password_NotVail(28008023, "密码必须含有至少一个数字、字母和字符"),
    ADMIN_CREATE_Password_CannotHas_Chinese(28008024, "新密码不能包含汉字"),
    ADMIN_LENGTH_TOO_LONG(28008025, "密码长度不能超过50个字符串"),
    ADMIN_NAME_LENGTH_TOO_LONG(28008026, "姓名长度不能超过50个字符串"),
    ADMIN_ACCOUNT_LENGTH_TOO_LONG(28008027, "帐号长度不能超过50个字符串"),
    //公司
    COMPANY_LOGO_NULL(28003001,"没有上传内容"),
    COMPANY_UPLOGO_TYPEERROR(28003002,"请上传jif,bmp,jpg,jpeg,tif,png,pcx,ico,cur后缀名的文件"),
    COMPANY_UPLOGO_SIZEEERROR(28003003,"上传图片超过2兆"),
    //测试账号管理
    EXAM_FIELD_EXIST(28008001,"已存在该字段名称"),
    EXAM_FIELD_ADD_FIELD_NULL(28008002,"请输入名称"),
    EXAM_FIELD_DEL_FIELD_NULL(28008002,"名称参数名为空"),
    EXAM_FIELD_DEL_COMPANY_NULL(28008003,"找不到操作人企业信息"),
    EXAM_FIELD_IN_USE(28008004,"名称已被使用，不能删除"),
    EXAM_FIELD_BEYOUND_LENGTH(28008005,"名称最长超过50个字符"),
    EXAM_FIELD_BEYOUND_NUMBER(28008006,"自定义名称最多20"),
    EXAM_FIELD_DEL_NO_ACCOUNT(28008002,"无此账号信息"),
    CUSTOMIZE(0,""),
    //发送邮件
    EMAIL_SEND_ERROR(28005001,"邮件发送失败"),

    GroupNameRepeat(28002011, "组名已存在"),
    GroupNameUpdateNoResult(28002003, "更新组名没有更新到相关数据"),
    MainGroupDelete(28002004, "顶级组不能被删除"),
    HasAdminGroupDelete(28002005, "有管理员的组不能被删除"),
    ParentGroupNotExists(28002006, "父级组不存在"),
    MaxGroupErr(28002007, "分组已达到最大层级"),
    GroupNameLENGTHTOOLONG(28002008, "组名长度不能超过50个字符串"),


    RoleNameRepeat(28002007, "角色名已存在"),
    RoleNameUpdateNoResult(28002008, "更新角色没有更新到相关数据"),
    RoleCannotDelete(28002009, "系统默认角色不能被删除"),
    HasAdminRoleDelete(28002010, "有管理员的角色不能被删除"),
    RoleNameIsDefault(28002012, "不能写入系统默认角色"),
    DefaultRoleCannotModify(28002013, "系统默认角色不能修改"),
    NameCannotNull(28002014, "名称不能为空"),
    RoleNameLENGTHTOOLONG(28002015, "角色名长度不能超过50个字符串"),


    //项目管理
    HasExaminee(28005001, "已经有考生参与"),
    UnknownStatus(28005002, "未知的状态更新"),
    ProjectNameRepeat(28005003, "项目名称已存在"),
    ProjectNotExist(28005004, "项目不存在"),
    ProjectNoPermission(28005005, "没该项目访问权限"),
    TaskNotFoundExaminer(28005006, "找不到相关考官"),
    ProjectNotFoundAdmin(28005007, "找不到相关管理员"),
    ProjectEndTimeStartTimeNotVail(28005008, "开始时间不能晚于结束时间"),
    ProjectScanTimeNotInclude(28005009, "项目扫码时间必须在开始结束时间之内"),
    ProjectTaskTimeNotInclude(28005010, "考试时间必须在项目开始结束时间之内"),
    TaskDelayLimitTimeSmallerThanZero(28005011, "考试开始延迟时间不能小于0"),
    TaskFinishTypeErr(28005012, "交卷类型错误"),
    TaskExamTimeSmallerThanZero(28005013, "考试时间不能小于0"),
    TaskChangeLimitSmallerThanZero(28005013, "考试切换次数设定不能小于0"),
    TaskTimeErr(28005014, "考试开始时间，结束时间/最晚开始时间不能为空"),
    TasktEndTimeStartTimeNotVail(28005015, "考试开始时间不能晚于结束时间/最晚开始时间；最晚开始时间不能晚于结束时间"),
    TaskProjectTimeNotInclude(28005016, "考试时间必须在项目开始结束时间之内"),
    TaskCannotModifyAfterStart(28005017, "考试开始后只能修改评卷人和最大切换次数"),
    TaskNotExists(28005018, "考试不存在"),
    TaskNoMarkPermission(28005019, "不具有评分此任务的权限"),
    TaskPaperNotSelect(28005020, "存在没有选择试卷的考试"),
    TaskPaperCannotUse(28005021, "选择了不可使用的试卷"),
    ProjectNameIsNull(28005022, "项目名称不能为空"),
    TaskNameIsNull(28005023, "考试名称不能为空"),
    TaskNotExist(28005024, "考试不存在"),
    TaskNewPaperNotFound(28005025, "没有可更新的版本"),
    ScanMaxErr(28005026, "扫码人次设置不能小于0"),

    //考生管理
    ImportLogNotFound(28008001, "找不到相关的导入日志"),
    ImportLack(28008002, "请确认文件中存在帐号列"),
    Exam_Repassword_Not_Found_Id(28008003, "请选择要操作的帐号"),
    CreateNoAmount(28008004, "请输入有效的创建数量"),
    PasswordNotVail(28008005, "密码必须含有至少一个数字和字母和字符"),
    NoAccount(28008006, "没有帐号字段"),
    MobileNotVail(28008007, "手机规则不通过"),
    EmailNotVail(28008008, "邮箱规则不通过"),
    InputTooLong(28008009, "输入框输入过长"),
    DateNotVail(28008010, "日期格式有问题"),
    SelectNotVail(28008011, "非可选择值"),
    IDNotVail(28008012, "身份证不通过"),
    WorkTimeNotVail(28008013, "工作时长异常"),
    FieldRepeat(28008014, "字段重复"),
    AccountExists(28008015, "帐号已存在"),
    UserIdNotExists(28008016, "用户id不存在"),
    AccountCannotModify(28008017, "帐号不允许修改"),
    PasswordTooShort(28008018, "密码不能小于6位"),
    PasswordCannotHasChinese(28008019, "密码不能包含汉字"),
    PasswordCannotBeBlank(28008020, "密码不能为空"),
    FieldCannotBeBlank(28008021, "必填项为空"),
    ScanDisable(28008022, "当前状态不允许扫码登陆"),
    ScanCodeDisable(28008023, "无效二维码"),
    ScanCodeOver(28008024, "已超过二维码扫码上限"),
    ScanCodePast(28008025, "二维码已失效"),

    EditAccountErr(28008022, "创建/修改考生帐号异常"),
    AccountConflictErr(28008023, "导入帐号冲突(姓名、手机号码不一致)"),
    AccountImportRepeatErr(28008024, "导入帐号与数据库中信息不符合"),
    ExamerStartExamCannotBeDelete(28008025, "答题中和已完成的考生不能被移除"),
    //containChinese
    PAPER_NO_SCORE_SETTING(28008026,"第三步尚有答案或分数未设置，请先前往设置"),
    PAPER_REMARK_SCORE_UNCORRECT(28008027,"第四步尚有结束语分值区间未设置，请先前往设置"),
    //题库管理
    PaperCreateReject(27003000, "试卷创建无效"),
    PaperEditReject(27003001, "试卷创建无效"),
    SubjectNotSurported(27003002, "不支持该类型题目"),
    PAPERSAVEFAILED(27003003, "试卷首部保存失败"),
    PAPERREDITREJECT(27003004, "创建中，不能修改试卷"),
    NO_EXCEL(27003005, "暂不支持该类型的文件"),
    TEXT_BEYOUND_LENGTH(27003006, "文字和样式已超出限制，请减少文字或样式设定。"),
    EMAIL_TEXT_BEYOUND_LENGTH(27003007, "邮件内容的文字和样式已超出限制，请减少文字或样式设定。"),
    SIGN_TEXT_BEYOUND_LENGTH(27003008, "邮件签的名文字和样式已超出限制，请减少文字或样式设定。"),
    //考试
    EXAMREJECT(27003006, "不支持该类型题目"),
    //试卷列表 ms-service
    NO_PERMISSION_OPERATION_PAPER(28007001,"您没有权限操作他人创建的试卷"),
    NO_DELETE(28007002,"试卷已被引用，无法删除"),
    PAPER_MODE_MODIFICATION (28007003,"当前试卷正在被创建人修改中，暂时无法进行此操作"),
    PAPER_NAME_ISDUPLICATE(28007004,"试卷名称重复，请重新输入"),

    //登录
    AdminLoginErr(27003001, "帐号或者密码错误"),
    UserLoginErr(27003002, "帐号或者密码错误"),
    AdminLoginDisable(27003003, "您输入的帐号不存在"),
    UserLoginDisable(27003004, "您输入的帐号不存在"),
    EmailDisable(27003005, "您输入的邮箱有误，请输入您帐号中使用的邮箱"),

    //答题
    TaskIsStop(27002001, "考试已暂停，请稍后再试"),
    TaskNotStart(27002002, "考试未开始"),
    TaskIsEnd(27002003, "考试已结束"),
    TaskTooLate(27002004, "超过最晚开考时间"),
    NoUserExam(27002005, "没有预约本场考试"),
    NoPaper(27002006, "不存在该考试试卷"),
    UserExamIsFinish(27002007, "考生已交卷"),
    PaperUpdate(27002008, "试卷已更新，请重新进行考试"),
    SubmitRepeatErr(27002009, "已交卷"),
    SetSubScoreRepeatErr(27002010, "已评卷"),
    NotInErr(27002011, "考生还没开始考试"),
    ExamTimeOver(27002012, "考试已超过时长,将自动交卷"),
    ExamNotExists(27002013, "没有相关的考试记录"),
    AnswerPaperErr(27002014, "答题试卷版本异常"),
    ReStartExerciseErr(27002015, "考试已开始"),
    NoExerciseRecord(27002016, "不存在考试记录"),
    EmptyFile(27002017, "上传空文件"),
    FileTooBig(27002018, "文件过大"),
//    TaskStop(27002001, "任务已暂停"),
//    TaskStop(27002001, "任务已暂停"),
//    TaskStop(27002001, "任务已暂停"),

    //考生评卷
    NextUserNotExists(27002018, "不存在下一个待评考生"),
    ExamNotExistsToSave(27002019, "不存在考生信息"),
    AllFinish(27002018, "当前考试的考生试卷已全部评完"),
    DataNotComplete(27002019, "数据记录不完整"),
    NoOneFinish(27002020, "当前考试暂无考生提交试卷"),

//    消费中心
    CostPointNotEnough(2800001, "考试点数不足"),

    XX(1,"");

    public int code;
    public String message;

	private BizEnums(int code, String message){
        this.code = code;
        this.message = message;
    }


    @Override
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static BizEnums getCustomize(Integer code,String message){
        BizEnums.CUSTOMIZE.setMessage(message);
        BizEnums.CUSTOMIZE.setCode(code);
        return BizEnums.CUSTOMIZE;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public BizEnums getBizEnumsByCode(int code){
        for(BizEnums enums : BizEnums.values()){
            if(enums.getCode() == code)
                return enums;
        }
        return null;
    }
}
