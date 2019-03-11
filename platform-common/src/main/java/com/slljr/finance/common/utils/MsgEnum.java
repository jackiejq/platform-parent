package com.slljr.finance.common.utils;
/**
 * 返回自定义信息常量
 * 固定状态码：
 * 		200：成功
 * 		100：代码异常
 * 		101：服务不可用
 * 		102：登陆失效
 * 		103：固定常用状态码，所有返回出去的异常信息都使用
 * @author XueYi
 * 2018年12月5日 下午3:57:48
 */
public enum MsgEnum {

	/**
	 * 成功状态码
	 */
	SUCCESS(200, true, "操作成功"),
	
	/**
	 * 返回该异常信息表示代码异常
	 */
	SERVER_EXCEPTION(100, false, "操作失败"),
	
	/**
	 * 返回该异常信息表示服务不可用
	 */
	NOTSERVER(101, false, "请求超时"),
	
	/**
	 * 登陆失效
	 */
	NOTLOGIN(102, false, "登陆失效"),
	
	NOTPARAM(103, false, "有效参数不能为空"),
	
	WRONGPHONENUMBER(104, false, "请输入有效的手机号码"),
	
	UNREGISTERED(105, false, "无权限"),TOKEN_INVALID(401, false, "token无效"),
	
	VERIFICATIONCODEERROR(106, false, "验证码错误"),
	
	VERIFICATIONCODETIMEOUT(107, false, "验证码超时"),
	
	VERIFICATIONCODFAIL(108, false, "验证码发送失败"),
	
	ACCOUNTORPASSWORDERROR(109, false, "账号或密码错误"),
	
	PASSWORDERROR(110, false, "用户密码错误"),
	
	USERHASBEENDISABLED(111, false, "该账号已被禁用"),USER_EXISTED(111, false, "该账号已注册"),
	
	ROLENOTEXISTS(112, false, "该账号还未绑定角色，请联系超级管理员进行角色绑定"),
	
	AUTHNOTEXISTS(113, false, "该账号还未分配权限，请联系超级管理员进行权限分配"),
	
	EXISTS(114, false, "数据已存在"),
	
	NOTEXISTS(115, false, "数据不存在"),

	DATA_NULL(115, false, "数据为空"),

	FAIL(500, false, "操作失败"),

	PRIMARY_CLASSIFICATION(501, false, "一级分类不允许修改"),

	GET_SMS_DETAIL_FAIL(502, false, "查询短信详情失败"),
	//绑卡相关
	CARD_IS_BINDING(701, false, "新增卡片失败"),

	//签到
	SIGNED(801, false, "已签到，请勿重复签到"),PROXY_NOT_ALLOW_SIGN(802, false, "代理用户不允许签到"),EXIST_CHECKING(802, false, "当前用户已申请代理，请勿重复提交"),

	//交易相关
	ADD_RECEIVE_ORDER_FAIL(601, false, "添加收款交易失败"),
	ADD_COMPENSATION_ORDER_FAIL(602, false, "添加代偿交易失败"),
	CARD_VALID_FAIL(603, false, "验证卡片信息失败,请检查手机号码是否为银行预留手机号"),

	//代理审核
	AUDIT_STATUS_ERROR(701, false, "审核状态有误"),

	//修改账单日
	CARD_NOT_EXIST(702, false, "银行卡不存在"),
	NOT_ALLOWED_UPDATE(703, false, "当前用户不允许修改"),

	//银行卡校验相关
	ARREARS(901, false, "响应为空，请检查是否欠费"),

	//论坛
	NOT_ALLOW_DELETE_POST(1001, false, "不能删除非当前用户帖子"),
	NOT_ALLOW_DELETE_COMMENT(1002, false, "不能删除非当前用户评论"),
	NOT_ALLOW_OPERATE(1002, false, "当前用户被禁止操作社群"),

	//提现
	INSUFFICIENT_ACCOUNT_BALANCE(1003, false, "账户余额不足"),
	ACCOUNT_NOT_EXIST(1003, false, "账户不存在"),
	//重新设置代偿
	RENEW_SET_COMPENSATION(1004, false, "重新设置代偿失败，请检查条件是否满足"),
	CAN_NOT_RESET(1005, false, "不能重复重新设置代偿"),

	//商品
	STOCK_NOT_ENOUGH(2001, false, "商品库存不足"),
	CASH_NOT_ENOUGH(2002, false, "金币余额不足"),
	INTEGRAL_NOT_ENOUGH(2003, false, "积分余额不足"),
	NOT_ALLOW_DELETE(2004, false, "非当前用户订单不能删除"),;

	private int code;
	
	private boolean flag;
	
	private String msg;

	MsgEnum() {
		
	}

	private MsgEnum(int code, boolean flag, String msg) {
		this.code = code;
		this.flag = flag;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}
	
	public boolean getFlag() {
		return flag;
	}
	
	public String getMsg() {
		return msg;
	}
}