# Big Event 后端项目

基于 Spring Boot 的内容管理系统后端服务，提供用户管理、文章分类管理、文章内容管理和文件上传等核心功能。
前端项目地址：https://github.com/piece-weaver/big-event-vue

效果图展示：
<img src="https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/big_event_cover.png" alt="登录页面" style="zoom:50%;" />

<img src="https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/big_event_cover_article.png" alt="文章页面" style="zoom:50%;" />

<img src="https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/big_event_cover_category.png" alt="分类页面" style="zoom:50%;" />

## 技术栈

- **Spring Boot 3.4.1** - 基础框架
- **MyBatis 3.0.3** - 持久层框架
- **PageHelper 1.4.7** - 分页插件
- **MySQL** - 关系型数据库
- **Redis** - 缓存和会话管理
- **JWT (java-jwt 4.4.0)** - 令牌认证
- **阿里云 OSS** - 对象存储服务
- **Lombok 1.18.36** - 简化 Java 代码
- **spring-dotenv 4.0.0** - 环境变量管理

## 项目结构

```
big-event/
├── src/main/java/com/cxx/bigevent/
│   ├── annotation/           # 自定义注解
│   ├── config/               # 配置类
│   ├── controller/           # 控制器层
│   ├── dto/                  # 数据传输对象
│   ├── exception/            # 异常处理
│   ├── interceptors/         # 拦截器
│   ├── mapper/               # 数据访问层
│   ├── pojo/                 # 实体类
│   ├── service/              # 服务层
│   ├── security/             # 安全工具
│   ├── util/                 # 工具类
│   ├── validation/           # 校验器
│   └── BigEventApplication.java
├── src/main/resources/
│   ├── application.yml       # 应用配置文件
│   └── com/cxx/bigevent/mapper/
│       └── ArticleMapper.xml # MyBatis 映射文件
├── .env                      # 环境变量配置
├── .env.example              # 环境变量示例
├── big_event_database.sql     # 数据库初始化脚本
└── pom.xml                   # Maven 配置文件
```

## 快速开始

### 环境要求

- JDK 23+
- Maven 3.6+
- MySQL 5.7+
- Redis 5.0+

### 安装步骤

1. **克隆项目**
```bash
git clone <repository-url>
cd big-event
```

2. **配置环境变量**

复制 `.env.example` 为 `.env` 并修改配置：

```bash
cp .env.example .env
```

编辑 `.env` 文件，配置数据库、Redis 和阿里云 OSS 信息。

3. **初始化数据库**

```bash
mysql -u root -p < big_event.sql
```

4. **安装依赖**

```bash
mvn clean install
```

5. **启动项目**

```bash
mvn spring-boot:run
```

或使用 IDE 运行 `BigEventApplication.java` 主类。

6. **访问应用**

项目默认运行在 `http://localhost:8080`

## API 接口说明

### 统一响应格式

```json
{
  "code": 0,
  "message": "操作成功",
  "data": {}
}
```

### 错误码说明

| 错误码 | 说明 |
|--------|------|
| 0 | 操作成功 |
| 1 | 操作失败 |
| 1001 | 用户名已存在 |
| 1002 | 用户名或密码错误 |
| 1006 | 原密码错误 |
| 1007 | 新密码与原密码一致 |
| 1008 | 新密码与确认密码不一致 |
| 2001 | 分类不存在 |
| 2002 | 分类下存在文章，无法删除 |
| 2003 | 文章不存在 |
| 3001 | 文件上传失败 |
| 3002 | 文件格式不支持 |
| 4002 | 必要参数不能为空 |
| 4003 | 参数格式错误 |

### 用户管理接口

| 接口 | 方法 | 路径 | 认证 |
|------|------|------|------|
| 用户注册 | POST | /user/register | 否 |
| 用户登录 | POST | /user/login | 否 |
| 获取用户信息 | GET | /user/userInfo | 是 |
| 更新用户信息 | PUT | /user/update | 是 |
| 更新用户头像 | PATCH | /user/updateAvatar | 是 |
| 修改密码 | PATCH | /user/updatePwd | 是 |

### 文章分类接口

| 接口 | 方法 | 路径 | 认证 |
|------|------|------|------|
| 新增分类 | POST | /category | 是 |
| 分类列表 | GET | /category | 是 |
| 分类详情 | GET | /category/{id} | 是 |
| 获取分类下的文章 | GET | /category/{id}/articles | 是 |
| 更新分类 | PUT | /category/{id} | 是 |
| 删除分类 | DELETE | /category/{id} | 是 |
| 强制删除分类及其文章 | DELETE | /category/{id}/force | 是 |

### 文章管理接口

| 接口 | 方法 | 路径 | 认证 |
|------|------|------|------|
| 新增文章 | POST | /article | 是 |
| 文章列表 | GET | /article | 是 |
| 文章详情 | GET | /article/{id} | 是 |
| 更新文章 | PUT | /article | 是 |
| 删除文章 | DELETE | /article/{id} | 是 |

### 文件上传接口

| 接口 | 方法 | 路径 | 认证 | 说明 |
|------|------|------|------|------|
| 文件上传 | POST | /upload | 否 | 支持图片格式：jpg, jpeg, png, gif, bmp, webp，最大10MB |

## 数据库设计

### 用户表 (tb_user)

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| id | BIGINT | 用户ID | 主键，自增 |
| username | VARCHAR(16) | 用户名 | 唯一，非空，5-16位非空字符 |
| password | VARCHAR(255) | 密码（MD5加密） | 非空 |
| nickname | VARCHAR(20) | 昵称 | 非空，1-10位非空字符 |
| email | VARCHAR(100) | 邮箱 | 唯一，非空，邮箱格式 |
| user_pic | VARCHAR(255) | 头像URL | 可空 |
| create_time | DATETIME | 创建时间 | 非空 |
| update_time | DATETIME | 更新时间 | 非空 |

### 分类表 (tb_category)

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| id | BIGINT | 分类ID | 主键，自增 |
| category_name | VARCHAR(50) | 分类名称 | 非空 |
| category_alias | VARCHAR(50) | 分类别名 | 非空，同一用户下唯一 |
| create_user_id | BIGINT | 创建人ID | 外键，非空 |
| create_time | DATETIME | 创建时间 | 非空 |
| update_time | DATETIME | 更新时间 | 非空 |

### 文章表 (tb_article)

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| id | BIGINT | 文章ID | 主键，自增 |
| title | VARCHAR(100) | 文章标题 | 非空，1-100位非空字符 |
| content | TEXT | 文章内容 | 非空，支持富文本 |
| cover_img | VARCHAR(255) | 封面图片URL | 可空 |
| state | TINYINT(1) | 文章状态 | 0-草稿，1-已发布 |
| category_id | BIGINT | 分类ID | 外键，非空 |
| create_user_id | BIGINT | 创建人ID | 外键，非空 |
| create_time | DATETIME | 创建时间 | 非空 |
| update_time | DATETIME | 更新时间 | 非空 |

## 配置说明

### 环境变量配置

项目使用 `.env` 文件管理环境变量，主要配置项：

```bash
# 数据库配置
DB_URL=jdbc:mysql://localhost:3306/big_event
DB_USERNAME=root
DB_PASSWORD=123456

# Redis配置
REDIS_HOST=localhost
REDIS_PORT=6379

# 阿里云OSS配置
ALIYUN_OSS_ENDPOINT=https://oss-cn-beijing.aliyuncs.com
ALIYUN_OSS_ACCESS_KEY_ID=your_access_key_id
ALIYUN_OSS_ACCESS_KEY_SECRET=your_access_key_secret
ALIYUN_OSS_BUCKET_NAME=your_bucket_name
```

### application.yml 配置

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  data:
    redis:
      host: ${REDIS_HOST}
      port: ${REDIS_PORT}

aliyun:
  oss:
    endpoint: ${ALIYUN_OSS_ENDPOINT}
    access-key-id: ${ALIYUN_OSS_ACCESS_KEY_ID}
    access-key-secret: ${ALIYUN_OSS_ACCESS_KEY_SECRET}
    bucket-name: ${ALIYUN_OSS_BUCKET_NAME}

mybatis:
  configuration:
    map-underscore-to-camel-case: true
```

## 开发规范

### 代码规范

- 遵循阿里巴巴 Java 开发手册
- 使用 Lombok 简化代码
- 所有实体类使用 `@Data` 注解
- Controller 层只负责参数接收和响应返回
- Service 层处理业务逻辑
- Mapper 层负责数据访问

### 命名规范

- 包名：全小写，使用点分隔
- 类名：大驼峰命名法
- 方法名：小驼峰命名法
- 常量：全大写，下划线分隔
- 数据库表名：小写，下划线分隔，前缀 tb_
- 数据库字段名：小写，下划线分隔

### 异常处理

- 使用自定义异常类 `BusinessException` 抛出业务异常
- 使用 `GlobalExceptionHandler` 统一处理异常
- 使用 `ErrorCode` 枚举定义标准错误码

### 参数校验

- 使用 Jakarta Validation 注解进行参数校验
- 自定义校验注解处理特殊校验逻辑
- 在 Controller 层使用 `@Validated` 注解启用校验

## 部署说明

### 打包项目

```bash
mvn clean package -DskipTests
```

### 运行 JAR 包

```bash
java -jar target/big-event-0.0.1-SNAPSHOT.jar
```

### Docker 部署

创建 `Dockerfile`：

```dockerfile
FROM openjdk:23-jdk-slim
WORKDIR /app
COPY target/big-event-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

构建和运行：

```bash
docker build -t big-event .
docker run -p 8080:8080 --env-file .env big-event
```

## 测试

### 单元测试

```bash
mvn test
```

### API 测试

推荐使用 Postman 或 Apifox 进行 API 接口测试。

## 常见问题

### 1. 数据库连接失败

检查 `.env` 文件中的数据库配置是否正确，确保 MySQL 服务已启动。

### 2. Redis 连接失败

检查 `.env` 文件中的 Redis 配置是否正确，确保 Redis 服务已启动。

### 3. JWT 令牌过期

JWT 令牌默认有效期为 6 小时，过期后需要重新登录。

### 4. 文件上传失败

检查 `.env` 文件中的阿里云 OSS 配置是否正确，确保网络连接正常。

## 更新日志

### V2.0 (2026-01-13)

- 添加 spring-dotenv 依赖，支持环境变量管理
- 优化代码结构，删除冗余的 getter/setter 方法
- 删除无关文档文件，简化项目结构
- 完善 README 文档，提供更清晰的项目说明

### V1.0 (2023-09-01)

- 初始版本发布

## 许可证

本项目仅供学习和交流使用。

## 联系方式

如有问题或建议，请联系项目维护者。
