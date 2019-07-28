package com.example.school.app.config;

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
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/11/29 15:23
 * description:
 */
@Profile({"dev","test"})
@EnableSwagger2
@Configuration
public class Swagger2Config {

    @Bean
    public Docket createRestfulApi() {//api文档实例
        return new Docket(DocumentationType.SWAGGER_2)//文档类型：DocumentationType.SWAGGER_2
                .apiInfo(apiInfo())//api信息
                .select()//构建api选择器
                .apis(RequestHandlerSelectors.basePackage("com.example.school.app.controller"))//api选择器选择api的包
                .paths(PathSelectors.any())//api选择器选择包路径下任何api显示在文档中
                .build();//创建文档
    }

    private ApiInfo apiInfo() {//接口的相关信息
        return new ApiInfoBuilder()
                .title("手机接口")
                .description("手机接口")
                .termsOfServiceUrl("termsOfServiceUrl")
                .contact(new Contact("MarryFeng", "http://www.baidu.com", ""))
                .version("1.0")
                .license("http://springfox.github.io/springfox/docs/current/")
                .licenseUrl("http://springfox.github.io/springfox/docs/current/")
                .build();
    }

}
