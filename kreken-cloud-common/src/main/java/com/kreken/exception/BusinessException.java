package com.kreken.exception;

public class BusinessException extends Exception {

	private String message;
	private Integer code;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public BusinessException(String message) {
		this.message = message;
	}

	public BusinessException() {
	}

	public BusinessException(Integer code){
		this.code = code;
	}
}
