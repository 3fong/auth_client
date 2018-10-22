package com.zzlh.auth_client.common.exception;

import org.jasig.cas.client.validation.TicketValidationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.zzlh.auth_client.common.constant.HTTPCode;
import com.zzlh.auth_client.controller.ResponseEntity;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
class GlobalExceptionHandler {

	/**
	 * @Description 参数异常
	 * @param e 方法参数无效异常
	 * @return
	 */
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<Object> validErrorHandler(MethodArgumentNotValidException e) {
		log.error("validError: {}", e.getCause());
		return ResponseEntity.generalResponse(HTTPCode.PARAMERROR,HTTPCode.code.get(HTTPCode.PARAMERROR),null);
	}

	/**
	 * @Description http请求消息格式不正确异常
	 * @param e 请求参数无法识别异常
	 * @return
	 */
	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public ResponseEntity<Object> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
		log.error("typeError: {}", e.getMessage());
		String msg = HTTPCode.code.get(HTTPCode.PARAMERROR);
		if(!e.getMessage().contains("a")) {
			msg = e.getMessage();
		}
		return ResponseEntity.generalResponse(HTTPCode.PARAMERROR,msg,null);
	}
	
	/**
	 * @Description 请求参数为空异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = MissingServletRequestParameterException.class)
	public ResponseEntity<Object> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
		log.error("request param: {}", e.getMessage());
		return ResponseEntity.generalResponse(HTTPCode.PARAMISNULL,HTTPCode.code.get(HTTPCode.PARAMISNULL),null);
	}

	@ExceptionHandler(value = TicketValidationException.class)
	public ResponseEntity<Object> ticketValidationExceptionHandler(TicketValidationException e) {
		log.error("request param: {}", e.getMessage());
		return ResponseEntity.generalResponse(HTTPCode.PARAMISNULL,HTTPCode.code.get(HTTPCode.PARAMISNULL),null);
	}
	
	/**
	 * @Description 返回接口提示异常
	 * @param e 自定义异常
	 * @return
	 */
	@ExceptionHandler(value = ResException.class)
	public ResponseEntity<Object> resExceptionHandler(ResException e) {
		log.error("resError: {}", e.getMsg());
		return ResponseEntity.generalResponse(e.getCode(),e.getMsg(),null);
	}

	/**
	 * @Description 统一异常
	 * @param e 编译器异常
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> defaultExceptionHandler(Exception e) {
		log.error("Exception: {}", e);
		return ResponseEntity.generalResponse(HTTPCode.ERROR,HTTPCode.code.get(HTTPCode.ERROR),null);
	}
	
}