package com.zzlh.auth_client.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zzlh.auth_client.common.constant.HTTPCode;
import com.zzlh.auth_client.common.exception.ResException;
import com.zzlh.auth_client.service.AuthService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 认证控制器
 * @author liulei
 * @date 2018年10月21日 下午4:24:10
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin
@Api(value="认证接口",consumes="application/json")
@Slf4j
public class AuthController {

	@Autowired
	private AuthService authService;
	
	/**
	 * @Description 登录
	 * @param user
	 * @return
	 * @throws ResException 
	 * @throws IOException 
	 */
	@ApiOperation(value="登录", httpMethod = "GET", response = Map.class)
	@RequestMapping(value = "login",method = RequestMethod.GET)
	public void login(@RequestParam("service") String service,@RequestParam("username") String username,
			@RequestParam("password") String password,HttpServletRequest request, HttpServletResponse response) throws ResException, IOException {
		String tgt = authService.getTicketGrantingTicket(service, username, password, request, response);
		String st = authService.getServiceTicket(tgt, service);
		log.info(request.getRequestURI());
		Cookie cookie = new Cookie("tgt",tgt);
		cookie.setMaxAge(600);
		response.addCookie(cookie);
		response.sendRedirect(service+"/cas/ticket?ticket="+st);
	}

	/**
	 * @Description 校验ticket
	 * @param service 重定向地址
	 * @param ticket 授权服务票证
	 * @param request http请求体
	 * @param response http响应体
	 * @return
	 * @throws ResException
	 * @throws IOException
	 */
	@ApiOperation(value="校验ticket", httpMethod = "GET", response = Map.class)
	@RequestMapping(value = "ticket",method = RequestMethod.GET)
	public ResponseEntity<Object> verifyTicket(@RequestParam("service") String service,@RequestParam("ticket") String ticket,
			HttpServletRequest request, HttpServletResponse response) throws ResException, IOException {
		return ResponseEntity.generalResponse(HTTPCode.SUCCESS, HTTPCode.code.get(HTTPCode.SUCCESS), 
				authService.validateServiceTicket(service, ticket));
	}
	
	/**
	 * @Description 获取接口状态码
	 * @return
	 * @throws ResException
	 * @throws ParseException
	 */
	@ApiOperation(value="状态码", httpMethod = "POST", response = Map.class)
	@RequestMapping(value = "code",method = RequestMethod.POST)
	public ResponseEntity<Object> getCode() {
		return ResponseEntity.generalResponse(HTTPCode.SUCCESS, HTTPCode.code.get(HTTPCode.SUCCESS), HTTPCode.code);
	}
	
}
