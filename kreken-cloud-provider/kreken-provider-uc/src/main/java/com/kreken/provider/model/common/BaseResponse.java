package com.kreken.provider.model.common;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class BaseResponse  implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -940795926263067880L;

	//是否成功
	private boolean success;
	
	//结果
	private Object result;
	
	//描述
	private String desc;
	
	//描述编号
	private int descCode = -99999;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getDescCode() {
		return descCode;
	}

	public void setDescCode(int descCode) {
		this.descCode = descCode;
	}
	
	public static String BaseResponse(int resultState, String resultMsg) {
		BaseResponse resp = new BaseResponse();
		resp.setSuccess(false);
		resp.setDesc(resultMsg);
		resp.setDescCode(resultState);
		resp.setResult(resultMsg);
		return JSONObject.toJSONString(resp);
	}
}
