package com.zzlh.auth_client.common.conf;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 认证拦截
 * @author liulei
 * @date 2018年7月13日 下午2:42:57
 */
@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
	
	private static final String[] whiteList = {"/auth/login","/auth/logout"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    	/*String requestURI = request.getRequestURI();
    	// 需要进行请求认证
    	if(filterURI(requestURI)) {
    		// 1 进行tgt校验
    		 * 2 st校验
    		 * 3 
    	}*/
    	log.info("请求路径：{}", request.getRequestURI());
        return true;
    }
    
    /**
     * @Description 判断是否进行路径拦截
     * @param uri 请求路径
     * @return
     */
    private boolean filterURI(String uri) {
    	for (String list : whiteList) {
			if(list.equals(uri)) {
				return false;
			}
		}
    	return true;
    }
}
