package com.slljr.finance.common.utils;

import com.github.pagehelper.PageInfo;
import org.springframework.ui.ModelMap;

/**
 * 统一返回的json封装类
 */
public class WriteJson {

	public static ModelMap success() {
		return jsonMsg(MsgEnum.SUCCESS.getFlag(), MsgEnum.SUCCESS.getCode(), MsgEnum.SUCCESS.getMsg(),  null, null,
				null);
	}

	public static ModelMap success(String message) {
		return jsonMsg(MsgEnum.SUCCESS.getFlag(), MsgEnum.SUCCESS.getCode(), message,  null, null,
				null);
	}

	public static ModelMap successData(Object data) {
		return jsonMsg(MsgEnum.SUCCESS.getFlag(), MsgEnum.SUCCESS.getCode(), MsgEnum.SUCCESS.getMsg(),  data, null,
				null);
	}

	public static ModelMap successData(Object data, String token) {
		return jsonMsg(MsgEnum.SUCCESS.getFlag(), MsgEnum.SUCCESS.getCode(), MsgEnum.SUCCESS.getMsg(),  data, null,
				token);
	}

	public static ModelMap successData(Object data, String token, String msg) {
		return jsonMsg(MsgEnum.SUCCESS.getFlag(), MsgEnum.SUCCESS.getCode(), msg,  data, null,
				token);
	}

	public static ModelMap successPage(Object data, Object total) {
		return jsonMsg(MsgEnum.SUCCESS.getFlag(), MsgEnum.SUCCESS.getCode(), MsgEnum.SUCCESS.getMsg(),  data,
				total, null);
	}
	public static ModelMap successPage(PageInfo pageInfo){
		return jsonMsg(MsgEnum.SUCCESS.getFlag(), MsgEnum.SUCCESS.getCode(), MsgEnum.SUCCESS.getMsg(),  pageInfo.getList(),
				pageInfo.getTotal(), null);
	}


	public static ModelMap successPage(Object data, Object total, String token) {
		return jsonMsg(MsgEnum.SUCCESS.getFlag(), MsgEnum.SUCCESS.getCode(), MsgEnum.SUCCESS.getMsg(),  data,
				total, token);
	}

	public static ModelMap errorWebMsg(MsgEnum cons) {
		return jsonMsg(cons.getFlag(), cons.getCode(), cons.getMsg(),  null, null, null);
	}

	public static ModelMap errorWebMsg(String msg) {
		return jsonMsg(false, 100, msg,  null, null, null);
	}

	public static ModelMap errorWebMsg(Integer code, String msg) {
		return jsonMsg(false, code, msg, null, null, null);
	}
	
	public static ModelMap errorWebMsg(MsgEnum cons, String token) {
		return jsonMsg(cons.getFlag(), cons.getCode(), cons.getMsg(),  null, null, token);
	}
	
	public static ModelMap json(MsgEnum cons, String token) {
		return jsonMsg(cons.getFlag(), cons.getCode(), cons.getMsg(),  null, null, token);
	}
	
	public static ModelMap json(MsgEnum cons) {
		return jsonMsg(cons.getFlag(), cons.getCode(), cons.getMsg(),  null, null, null);
	}

	/**
	 * 返回json数据基础模板
	 * @param success true/false
	 * @param code
	 * @param message
	 * @param data
	 * @param total
	 * @param token
	 * @return
	 */
	public static ModelMap jsonMsg(Boolean success, Integer code, String message, Object data,
									Object total, String token) {
		ModelMap map = new ModelMap();
		map.addAttribute("success", success);
		map.addAttribute("code", code);
		map.addAttribute("msg", message);
		map.addAttribute("data", data);
		map.addAttribute("total", total);
		map.addAttribute("token", token);
		return map;
	}

}
