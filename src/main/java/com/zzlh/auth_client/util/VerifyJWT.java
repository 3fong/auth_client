package com.zzlh.auth_client.util;

import org.apache.commons.codec.binary.Base64;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.JoseException;

import java.nio.charset.StandardCharsets;
import java.security.Key;

/**
 * @Description 校验jwt
 * @author liulei
 * @date 2018年10月19日 下午5:06:13
 */
public class VerifyJWT {

	public static void main(String[] args) throws Exception {
		final String signingKey = "43DHuYiDRtwVxS7YBNsOr4GvlvdktHSHedwspI4niDtmKk0KYhHKteI5Xukvi7XVBbaRmlYJR1SUUFW4HhhVaRgA46QEK9uzKQ6kBw6kRjgYvxVn33nFcz3oJD31HIvEYwrW3ML657PmUTb8mEYrsKJMB1vFnPu536UBVgTRr9I6c043xXbo8YQrGBugrzQ40iMkbo7HSntFTfKNzWuj07adtfJBPtpzrcAlzsbw63IjOEhqhCId0SI22oRVgjkK";
		final String encryptionKey = "Mv7oxiraFn5Vkc5O0v02FvgnaQkeIIUVm5TleLdbkw3EnEr8";
		
		final Key key = new AesKey(signingKey.getBytes(StandardCharsets.UTF_8));

		final JsonWebSignature jws = new JsonWebSignature();
//		jws.setAlgorithmHeaderValue("HS256");
		String jwt = "eyJhbGciOiJIUzUxMiJ9.ZXlKNmFYQWlPaUpFUlVZaUxDSmhiR2NpT2lKa2FYSWlMQ0psYm1NaU9pSkJNVEk0UTBKRExVaFRNalUySW4wLi55dl9JLWJOU1MwdTlya0swMmZuVm13LlQyT1RuMVlPbnBlbDJBbUUxWXpqOERjRzNxejR6X09jNGdMbF9SWXZwUjJ4RnJvREdYNEJzdzFuTG0zRHpHWWlpbE83Zll1aUctYjFpZFRQcjRqN0p0VHEyQWgyWndZZVZTVlJaVnZFb2pnYTZucHRRZksyQXRXTmV4SmhXVWRBZzBpT1M1bkZUYnFPYzJqMzF4U0FmdHVuck5OWFgxaFh2M2RBSE5IZ3Q2OC5iMnNaM2t3dFFOaEtkQWw3RGEwOFBB.uqkNfmoZJE61b4pATcWeUF7ll0DnlhmmDCGvTOh97wNYWYH2mg6bxluCbWaUga-ygSU_SLdz3MxziHcDJsqDgQ";
		jws.setCompactSerialization(jwt);
		jws.setKey(key);
		if (!jws.verifySignature()) {
		    throw new Exception("JWT verification failed");
		}

		final byte[] decodedBytes = Base64.decodeBase64(jws.getEncodedPayload().getBytes(StandardCharsets.UTF_8));
		final String decodedPayload = new String(decodedBytes, StandardCharsets.UTF_8);

		final JsonWebEncryption jwe = new JsonWebEncryption();
		final JsonWebKey jsonWebKey = JsonWebKey.Factory
		    .newJwk("\n" + "{\"kty\":\"oct\",\n" + " \"k\":\"" + encryptionKey + "\"\n" + "}");

		jwe.setCompactSerialization(decodedPayload);
		jwe.setKey(new AesKey(jsonWebKey.getKey().getEncoded()));
		System.out.println(jwe.getPlaintextString());
	}
	
}
