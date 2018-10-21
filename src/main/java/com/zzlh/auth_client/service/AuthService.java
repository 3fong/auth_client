package com.zzlh.auth_client.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthService {

	private static final String CAS_SERVER_HOME = "http://localhost:8080/cas";

	/**
	 * @Description 获取授权票证
	 * @return TGT
	 */
	public static String getTicketGrantingTicket(String username, String password) {
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
	public static void logoutTicketGrantingTicket(String ticketGrantingTicket, String username) {
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
	public static String getServiceTicket(String ticketGrantingTicket, String serviceUrl, String username) {
		CasConfiguration casConfiguration = new CasConfiguration(CAS_SERVER_HOME);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final WebContext webContext = new J2EContext(request, response);
		final CasRestProfile profile = new CasRestProfile(ticketGrantingTicket, username);
		final CasRestFormClient client = new CasRestFormClient(casConfiguration, "username", "password");
		final TokenCredentials casCredentials = client.requestServiceTicket(serviceUrl, profile, webContext);
		return casCredentials.getToken();
	}

	/**
	 * @Description 校验服务证书
	 * @param serviceUrl    待授权服务路径
	 * @param serviceTicket 服务证书
	 * @return
	 */
	public static void validateServiceTicket(String serviceUrl,String serviceTicket) {
		CasConfiguration casConfiguration = new CasConfiguration(CAS_SERVER_HOME);
		final CasRestFormClient client = new CasRestFormClient(casConfiguration, "username", "password");
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final WebContext webContext = new J2EContext(request, response);
		TokenCredentials casCredentials = new TokenCredentials(serviceTicket); 
		final CasProfile casProfile = client.validateServiceTicket(serviceUrl, casCredentials, webContext);
		Map<String, Object> attributes = casProfile.getAttributes();
		Set<Map.Entry<String, Object>> mapEntries = attributes.entrySet();
		for (Map.Entry<String, Object> entry : mapEntries) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
	}

	private static String requestTicketGrantingTicket(final String username, final String password,
			final WebContext context, final CasConfiguration configuration) {
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
				return locationHeader.substring(locationHeader.lastIndexOf("/") + 1);
			}
			log.debug("获取授权票证(TGT)失败: " + locationHeader + " " + responseCode
					+ HttpUtils.buildHttpErrorMessage(connection));
			return null;
		} catch (final IOException e) {
			throw new TechnicalException(e);
		} finally {
			HttpUtils.closeConnection(connection);
		}
	}

	public static void main(String[] args) {
//		log.info(getTGT("huan", "Huan1"));
		String serviceTicket = getServiceTicket("TGT-2-5v-yighJulULdaf3WBZT9yZzYVHkvnCrXCRvC89mjloubKqG-pE2jtytd7PSJS-xqLYDavid","http://localhost:8080", "huan");
		log.info(serviceTicket);
//		validateServiceTicket("http://localhost:8080",serviceTicket);
		logoutTicketGrantingTicket("TGT-2-5v-yighJulULdaf3WBZT9yZzYVHkvnCrXCRvC89mjloubKqG-pE2jtytd7PSJS-xqLYDavid","huan");
	}
}
