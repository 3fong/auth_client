package com.zzlh.auth_client.controller;

import java.text.ParseException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zzlh.auth_client.common.constant.HTTPCode;
import com.zzlh.auth_client.common.exception.ResException;
import com.zzlh.auth_client.domain.User;
import com.zzlh.auth_client.service.AuthService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description 认证控制器
 * @author liulei
 * @date 2018年10月21日 下午4:24:10
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin
@Api(value="认证接口",consumes="application/json")
public class AuthController {

	@Autowired
	private AuthService authService;
	
	/**
	 * @Description 登录
	 * @param user
	 * @return
	 */
	@ApiOperation(value="状态码", httpMethod = "POST", response = Map.class)
	@RequestMapping(value = "login",method = RequestMethod.POST)
	public ResponseEntity<Object> login(@RequestBody User user) {
		authService.getTicketGrantingTicket(user.getUsername(), user.getPassword());
		return ResponseEntity.generalResponse(HTTPCode.SUCCESS, HTTPCode.code.get(HTTPCode.SUCCESS), null);
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
		return ResponseEntity.generalResponse(HTTPCode.SUCCESS, HTTPCode.code.get(HTTPCode.SUCCESS), null);
	}
	
}
