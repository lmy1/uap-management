package com.cd.uap.utils;

/**
 * 错误码及信息
 * @author li.mingyang
 *
 */
public enum CodeMessage {
	//登录专用码
	//用户无权限调起登录
	LOGIN_CODE("900","login"),
	//登录成功
	LOGIN_SUCCESS("901","success"),
	//登录失败
	LOGIN_ERROR("902","error"),
	//注销成功
	LOGOUT_SUCCESS("903","success"),
	
	LOGIN_PASSWORD_ERROR("904","密码错误"),
	//注销失败
	LOGOUT_ERROR("905","无效token"),
	
	//10
	//操作成功
	OPERATION_SUCCESS("1000", "操作成功"),
	//添加失败
	INSERT_FAILED("1001", "添加失败"),
	//删除失败
	DELETE_FAILED("1002", "删除失败"),
	//修改失败
	UPDATE_FAILED("1003", "修改失败"),
	//查询失败
	SELECT_FAILED("1004", "查询失败"),
	
	VALIDATE_FAILED("1005", "校验失败"),
	//MD5转化错误
	MD5_FAILED("1006", "MD5转换错误"),
	//获取当前security登录用户失败
	SECURITY_USER_FAILED("1007","获取当前security登录用户失败"),
	//导入相关
	TEMPLATEDOWNLOAD_FAILED("1008", "导入模板下载失败"),
	EXCEL_FORM_FAILED("1009","导入模板格式错误"),
	
	EXCEL_NOTEXIST_FAILED("1010","导出模板不存在"),

	//12
	ADMIN_HASEXIST_FAILED("1201", "保存失败，管理员用户名已存在"),
	
	ADMIN_NOCREATADMIN_FAILED("1202", "保存失败，该管理员的创建者不存在"),
	
	ADMIN_TYPELOW_FAILED("1203", "保存失败，创建者等级为四级，无法创建下级"),
	
	ADMIN_TYPE_FAILED("1204", "保存失败，该管理员等级不符合要求，应为创建者的下级"),
	
	ADMIN_PASSWORDRULE_FAILED("1205", "保存失败，该管理员密码复杂度不符合要求，规则：英文+数字+特殊字符8位以上"),
	
	ADMIN_PHONENUMBER_FAILED("1206", "保存失败，该管理员电话格式错误"),
	
	ADMIN_MD5_FAILED("1207", "保存失败，密码MD5加密错误"),
	
	ADMIN_NOTEXIST_FAILED("1208", "该应用管理员不存在"),
	
	ADMIN_NULLUPDATE_FAILED("1209", "保存失败，修改条件的字段名或值不能为空"),
	
	ADMIN_STATUS_FAILED("1210", "保存失败，可用状态错误"),
	
	ADMIN_NOEDIT_FAILED("1211", "保存失败，当前管理员没有修改该管理员信息的权利"),
	
	ADMIN_NO_UPDATE_PASSWORD_FAILED("1212", "保存失败，当前管理员没有修改该管理员密码的权利"),
	
	ADMIN_FROZEN_FAILED("1213","用户已被冻结"),
	//13
	APP_NOT_EXIST_FAILED("1301", "添加失败，该应用不存在"),
	
	APP_HAS_EXIST_FAILED("1302", "添加失败，该应用已存在"),
	
	APP_CURRENT_ADMIN_NOT_OPERATE_FAILED("1303", "当前管理员用户无法操作应用"),
	
	//14
	AUTHORITY_NOCREATADMIN_FAILED("1401", "保存失败，该权限的创建者不存在"),

	AUTHORITY_NOAPP_FAILED("1402", "保存失败，该权限所属的应用不存在"),
	
	AUTHORITY_NOPOWER_FAILED("1403", "保存失败，当前管理员没有创建该应用权限的权利"),
	
	AUTHORITY_NOEDIT_FAILED("1404", "保存失败，当前管理员没有修改该应用权限的权利"),
	
	AUTHORITY_NOT_EXIST_FAILED("1401", "权限不存在"),
	
	AUTHORITY_HAS_EXIST_FAILED("1401", "保存失败，权限名称已存在"),
	
	//15
	ROLE_NOT_EXIST_FAILED("1501", "角色不存在"),
	
	ROLE_NOT_ADD_AUTHORITY_FAILED("1502", "权限不足，角色无法添加该权限"),
	
	ROLE_HAS_EXIST_FAILED("1503", "该角色已经存在"),
	//18
	USER_NOT_EXIST_FAILED("1801", "用户不存在"),

	USER_NOT_CREATE_BY_ADMIN_FAILED("1802","非创建该用户的管理员无修改权限"),

	USER_NOT_ADD_ROLE_FAILED("1803","权限不足，用户无法添加该角色"),
	
	USER_HAS_EXIST_FAILED("1804","用户名已存在"),
	
	PHONE_NUMBER_HAS_EXIST_FAILED("1805","手机号已存在"),
	
	//19
	MESSAGE_SEND_FAILED("1901","短信发送失败"),
	
	REGIST_PHONE_ERROR("1902","手机号不合法或已注册"),
	
	REGIST_PASSWORD_ERROR("1903","密码格式错误"),
	
	VALIDATE_CODE_ERROR("1904","短信验证码错误"),
	
	REGIST_ROLEUSER_ERROR("1905","分配用户角色错误"),
	
	REGIST_ADMIN_ERROR("1906", "注册管理员registAdmin必须存在"),
	
	PASSWORD_PHONE_ERROR("1907", "手机号不合法或未注册"),
	
	MESSAGE_LIMIT_CONTROL("1908","短信发送过于频繁或已达到每日上限"),
	
	MOBILE_NUMBER_ILLEGAL("1909","手机号不合法"),
	
	AMOUNT_NOT_ENOUGH("1910","短信平台余额不足"),
	
	MESSAGE_SYSTEM_ERROR("1911","短信平台系统错误"),	//阿里的错误
	
	MESSAGE_CLIENT_ERROR("1912","短信平台客户端错误");	//uap调用阿里短信服务时发生的错误

    private String code;
    private String msg;

    private CodeMessage(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }



}
