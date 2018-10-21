package com.zzlh.auth_client.common.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description TODO
 * @author liulei
 * @date 2018年7月13日 下午4:00:47
 */
@Configuration
@EnableSwagger2
@Profile("dev")
public class SwaggerConfig {
	
	/**
	 * @Description 创建swagger访问api
	 * @return
	 */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zzlh.auth_client"))
                .paths(PathSelectors.any())
                .build();
    }
    
    /**
     * @Description 页面显示信息
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("债权Swagger2接口")
                .description("欢迎使用cas client")
                .termsOfServiceUrl("https://www.github.com")
                .contact(new Contact("liulei", "", ""))
                .version("1.0")
                .build();
    }
}
