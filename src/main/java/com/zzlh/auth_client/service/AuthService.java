package com.zzlh.auth_client.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pac4j.cas.client.rest.CasRestFormClient;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.cas.profile.CasProfile;
import org.pac4j.cas.profile.CasRestProfile;
import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.util.HttpUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.zzlh.auth_client.common.constant.HTTPCode;
import com.zzlh.auth_client.common.exception.ResException;
import com.zzlh.auth_client.domain.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthService {

	private final String CAS_SERVER_HOME = "http://localhost:8080/cas";

	/**
	 * @Description 获取服务授权票证
	 * @param service 重定向地址
	 * @param username 用户名
	 * @param password 密码
	 * @param request 请求体
	 * @param response 响应体
	 * @return 
	 * @throws IOException
	 * @throws ResException
	 */
	public String getTicketGrantingTicket(String service, String username, String password, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ResException {
		String tgt = null;
		Cookie[] cookies = request.getCookies();
		if(cookies!=null&& cookies.length>0) {
			for (Cookie cookie : cookies) {
				if("tgt".equals(cookie.getName())) {
					tgt = cookie.getValue();
				}
			}
		}
		if(tgt == null) {
			if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
				response.sendRedirect(service+"/login");
				throw new ResException(HTTPCode.LOGINERR,HTTPCode.code.get(HTTPCode.LOGINERR));
			}else {
				tgt = this.createTicketGrantingTicket(username,password);
			}
		}
		return tgt;
	}
	
	/**
	 * @Description 获取授权票证
	 * @return TGT
	 * @throws ResException 
	 */
	private String createTicketGrantingTicket(String username,String password) throws ResException {
		CasConfiguration casConfiguration = new CasConfiguration(CAS_SERVER_HOME);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final WebContext webContext = new J2EContext(request, response);
		return requestTicketGrantingTicket(username, password, webContext, casConfiguration);
	}

	/**
	 * @Description 登出授权票证
	 * @param ticketGrantingTicket
	 * @param username
	 */
	public void logoutTicketGrantingTicket(String ticketGrantingTicket, String username) {
		CasConfiguration casConfiguration = new CasConfiguration(CAS_SERVER_HOME);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final WebContext webContext = new J2EContext(request, response);
		final CasRestFormClient client = new CasRestFormClient(casConfiguration, "username", "password");
		final CasRestProfile profile = new CasRestProfile(ticketGrantingTicket, username);
		client.destroyTicketGrantingTicket(profile, webContext);
	}
	
	/**
	 * @Description 获取服务证书
	 * @param ticketGrantingTicket 授权票证
	 * @param serviceUrl           待授权服务路径
	 * @return
	 */
	public String getServiceTicket(String ticketGrantingTicket, String serviceUrl) {
		String[] str = ticketGrantingTicket.split(":");
		CasConfiguration casConfiguration = new CasConfiguration(CAS_SERVER_HOME);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final WebContext webContext = new J2EContext(request, response);
		final CasRestProfile profile = new CasRestProfile(str[0], str[1]);
		final CasRestFormClient client = new CasRestFormClient(casConfiguration, "username", "password");
		final TokenCredentials casCredentials = client.requestServiceTicket(serviceUrl, profile, webContext);
		return casCredentials.getToken();
	}

	/**
	 * @Description 校验服务证书
	 * @param serviceUrl    待授权服务路径
	 * @param serviceTicket 服务证书
	 * @return
	 * @throws ResException 
	 */
	public CasProfile validateServiceTicket(String serviceUrl,String serviceTicket) throws ResException {
		CasConfiguration casConfiguration = new CasConfiguration(CAS_SERVER_HOME);
		final CasRestFormClient client = new CasRestFormClient(casConfiguration, "username", "password");
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final WebContext webContext = new J2EContext(request, response);
		TokenCredentials casCredentials = new TokenCredentials(serviceTicket); 
		final CasProfile casProfile = client.validateServiceTicket(serviceUrl, casCredentials, webContext);
		if(casProfile == null) {
			throw new ResException(HTTPCode.TICKETERR,HTTPCode.code.get(HTTPCode.TICKETERR));
		}
		Map<String, Object> attributes = casProfile.getAttributes();
		Set<Map.Entry<String, Object>> mapEntries = attributes.entrySet();
		for (Map.Entry<String, Object> entry : mapEntries) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		return casProfile;
	}

	private String requestTicketGrantingTicket(final String username, final String password,
			final WebContext context, final CasConfiguration configuration) throws ResException {
		HttpURLConnection connection = null;
		try {
			connection = HttpUtils.openPostConnection(new URL(configuration.computeFinalRestUrl(context)));
			final String payload = HttpUtils.encodeQueryParam(Pac4jConstants.USERNAME, username) + "&"
					+ HttpUtils.encodeQueryParam(Pac4jConstants.PASSWORD, password);

			final BufferedWriter out = new BufferedWriter(
					new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));
			out.write(payload);
			out.close();

			final String locationHeader = connection.getHeaderField("location");
			final int responseCode = connection.getResponseCode();
			if (locationHeader != null && responseCode == HttpConstants.CREATED) {
				return locationHeader.substring(locationHeader.lastIndexOf("/") + 1)+":"+username;
			}else {
				log.debug("获取授权票证(TGT)失败: " + locationHeader + " " + responseCode
						+ HttpUtils.buildHttpErrorMessage(connection));
				throw new ResException(HTTPCode.LOGINERR,HTTPCode.code.get(HTTPCode.LOGINERR));
			}
		} catch (final IOException e) {
			throw new TechnicalException(e);
		} finally {
			HttpUtils.closeConnection(connection);
		}
	}

}
