package com.zzlh.auth_client.common.constant;

import java.util.HashMap;
import java.util.Map;

public class HTTPCode {
	
	public static Map<String,String> code = new HashMap<>();
	public static final String SUCCESS = "0";
	public static final String ERROR = "-1";
	public static final String PARAMERROR = "10001";
	public static final String PARAMISNULL = "10002";
	public static final String CONTENTISNULL = "10003";
	public static final String UNLOGIN = "10004";
	public static final String LOGINERR = "10005";
	public static final String LOGERROR = "10006";
	public static final String SIGNERROR = "10007";
	static {
		code.put(SUCCESS,"访问成功");
		code.put(ERROR,"服务内部异常");
		code.put(PARAMERROR,"参数不正确");
		code.put(PARAMISNULL,"请求参数为空");
		code.put(CONTENTISNULL,"内容不存在");
		code.put(UNLOGIN,"当前用户未登录或会话超时");
		code.put(LOGINERR,"用户名或密码不正确");
		code.put(LOGERROR,"日志记录异常");
		code.put(SIGNERROR,"签名异常");
	}
}
