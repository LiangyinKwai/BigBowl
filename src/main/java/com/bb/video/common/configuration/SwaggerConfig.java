package com.bb.video.common.configuration;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by LiangyinKwai on 2018-12-20.
 */

@Configuration
@EnableSwagger2
@Profile({"dev","test"})
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public Docket createRestApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList();
        tokenPar.name("token")
                .description("请求凭证")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false);

        ParameterBuilder sourcePar = new ParameterBuilder();
        sourcePar.name("source")
                .description("请求来源: 1:android; 2:ios; 3:pc; 4:browser;5.h5")
                .modelRef(new ModelRef("int"))
                .parameterType("header")
                .required(true);

        pars.add(tokenPar.build());
        pars.add(sourcePar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //加了ApiOperation注解的类，才生成接口文档
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //包下的类，才生成接口文档
                //.apis(RequestHandlerSelectors.basePackage("cn.mc.*.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars)
                .securitySchemes(security());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("大碗影视后端APIS -- Ver 1.0.0")
                .description("<h1 style=\"color:red\"><b></b></h1> <br>路径中带open标示的接口不用传输token；参数类型说明: form为表单提交; query为url后拼参; path为动态路由; body统一为json提交; header为放在头部的参数")
                .termsOfServiceUrl("https://wwww.bb.com")
                .version("1.0.0")
                .build();
    }

    private List<ApiKey> security() {
        return newArrayList(
                new ApiKey("token", "token", "header")
        );
    }

}
