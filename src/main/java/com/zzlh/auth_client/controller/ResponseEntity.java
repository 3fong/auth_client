package com.zzlh.auth_client.controller;

/**
 * @Description 响应体
 * @author liulei
 * @date 2018年10月21日 下午6:00:53
 */
public class ResponseEntity<T> {

	// 响应状态码
	private String status;

	// 响应消息
	private String message;

	// 响应数据
	private T data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public static <T> ResponseEntity<T> generalResponse(String status, String msg,T data) {
		ResponseEntity<T> r = new ResponseEntity<T>();
		r.setStatus(status);
		r.setMessage(msg);
		r.setData(data);
		return r;
	}
}
