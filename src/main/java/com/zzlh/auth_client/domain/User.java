package com.zzlh.auth_client.domain;

import lombok.Data;

/**
 * @Description 用户实体
 * @author liulei
 * @date 2018年10月21日 下午6:00:53
 */

@Data
public class User {
	// 用户名
	private String username;
	// 密码
	private String password;
	// 请求类型 1:ca登录 其他:用户登录
	private String type;
}
