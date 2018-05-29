package com.kreken.util;

/**
 * @category 工具类 - 返回结果
 * @author XJ
 * 
 */
public class ReturnMessage {

	private boolean result; // 返回的结果

	private String message; // 结果对应信息

	private Object o;       // 对象 

	public Object getO() {
		return o;
	}

	public void setO(Object o) {
		this.o = o;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
