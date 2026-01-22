package com.cxx.bigevent.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Big Event CMS API")
                .description("""
                    # Big Event 内容管理系统 API 文档

                    ## 简介
                    这是一个基于 Spring Boot 3.4.1 开发的内容管理系统后端API。

                    ## 主要功能模块
                    - **用户管理**: 用户注册、登录、信息管理
                    - **文章管理**: 文章CRUD、分类管理
                    - **AI功能**: 智能摘要、标签生成、SEO评分
                    - **搜索**: 全文搜索、建议词、热词
                    - **Webhook**: 自动化钩子
                    - **定时任务**: 任务调度

                    ## 认证方式
                    所有API需要通过JWT Token认证，请在请求头中添加:
                    ```
                    Authorization: Bearer <token>
                    ```

                    ## 响应格式
                    ```json
                    {
                        "code": 0,
                        "message": "操作成功",
                        "data": {}
                    }
                    ```
                    """)
                .version("3.0.0")
                .contact(new Contact()
                    .name("Developer")
                    .email("dev@bigevent.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0")))
            .schemaRequirement("Bearer", new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization"))
            .addSecurityItem(new SecurityRequirement().addList("Bearer"));
    }
}
