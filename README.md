# Big Event 后端项目

基于 Spring Boot 的内容管理系统后端服务，提供用户管理、文章分类管理、文章内容管理和文件上传等核心功能，并集成AI智能增强、统计分析、批量操作、版本控制、Webhooks、定时任务、高级搜索和图片处理等高级功能。

## 技术栈

- **Spring Boot 3.4.1** - 基础框架
- **MyBatis 3.0.3** - 持久层框架
- **PageHelper 1.4.7** - 分页插件
- **MySQL** - 关系型数据库
- **Redis** - 缓存和会话管理
- **JWT (java-jwt 4.4.0)** - 令牌认证
- **阿里云 OSS** - 对象存储服务
- **Ollama + DeepSeek** - 本地AI大语言模型
- **Lombok 1.18.36** - 简化 Java 代码
- **spring-dotenv 4.0.0** - 环境变量管理

## 功能模块

### 核心功能

- 用户管理（注册、登录、信息修改）
- 文章分类管理（CRUD）
- 文章内容管理（CRUD）
- 文件上传（阿里云OSS）

### Phase 1 增强功能

- AI内容增强（摘要、标签、润色、续写）
- 统计分析（数据概览、趋势分析）
- 批量操作（批量删除、更新状态/分类）
- 版本控制（版本历史、版本恢复）

### Phase 2 系统增强

- Webhooks事件回调系统
- 定时发布/删除任务
- 高级搜索（全文搜索、搜索建议、热门关键词）
- 图片智能处理（上传、压缩、信息获取）

### Phase 3 社交功能与AI增强

- 评论系统（支持回复、点赞）
- 文章点赞/收藏功能
- 个性化文章推荐（基于用户行为）
- 热门文章推荐
- 相似文章推荐
- RBAC角色权限管理
- 限流与熔断保护

## 快速开始

### 1. 环境准备

```bash
# 启动Redis
redis-server

# 启动Ollama
ollama serve
ollama pull deepseek-r1:8b
```

### 2. 初始化数据库

```bash
mysql -u root -p big_event < big_event.sql
```

### 3. 构建应用

```bash
# 编译项目
mvn clean compile -DskipTests

# 运行单测试
mvn test -Dtest=ArticleServiceTest

# 运行所有测试
mvn test

# 打包项目
mvn clean package -DskipTests

# 运行应用
mvn spring-boot:run
```

应用将在 http://localhost:8080 启动

### 4. 访问 Swagger API 文档

启动应用后，可通过以下地址访问 Swagger UI：

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI 文档: http://localhost:8080/v3/api-docs

## API文档

完整API文档请查看 API_DOCUMENTATION.md

## 开发文档

AI开发规范请查看 AGENTS.md

## 项目结构

```
big-event/
├── src/main/java/com/cxx/bigevent/
│   ├── annotation/           # 自定义注解
│   ├── config/               # 配置类
│   ├── controller/           # 控制器层
│   │   ├── AiController.java           # AI功能控制器
│   │   ├── AnalyticsController.java    # 统计分析控制器
│   │   ├── ArticleVersionController.java # 版本控制控制器
│   │   ├── BatchOperationController.java # 批量操作控制器
│   │   ├── ArticleController.java
│   │   ├── CategoryController.java
│   │   ├── UserController.java
│   │   ├── UploadController.java
│   │   ├── ImageController.java        # 图片处理控制器
│   │   ├── SearchController.java       # 搜索控制器
│   │   ├── ScheduledTaskController.java # 定时任务控制器
│   │   ├── WebhookController.java      # Webhook控制器
│   │   ├── CommentController.java      # 评论控制器（V3.0新增）
│   │   ├── ArticleInteractionController.java # 点赞收藏控制器（V3.0新增）
│   │   ├── RecommendationController.java # 推荐控制器（V3.0新增）
│   │   ├── RoleController.java         # 角色管理控制器（V3.0新增）
│   │   └── ExportController.java       # 导出控制器（V3.0新增）
│   ├── dto/                  # 数据传输对象
│   │   ├── AiSummaryResponse.java       # AI摘要响应
│   │   ├── AiTagsResponse.java          # AI标签响应
│   │   ├── AnalyticsResponse.java       # 统计分析响应
│   │   ├── BatchOperationRequest.java   # 批量操作请求
│   │   ├── SearchRequest.java           # 搜索请求
│   │   ├── SearchResponse.java          # 搜索响应
│   │   ├── ImageProcessRequest.java     # 图片处理请求
│   │   ├── ImageInfoResponse.java       # 图片信息响应
│   │   ├── ScheduledTaskRequest.java    # 定时任务请求
│   │   ├── CommentDTO.java              # 评论DTO（V3.0新增）
│   │   ├── CollectDTO.java              # 收藏DTO（V3.0新增）
│   │   └── BehaviorDTO.java             # 行为DTO（V3.0新增）
│   ├── dto/admin/           # 管理员DTO
│   │   └── RoleDTO.java              # 角色DTO（V3.0新增）
│   ├── event/                # Spring事件
│   │   ├── ArticleCreateEvent.java
│   │   ├── ArticleUpdateEvent.java
│   │   └── ArticleDeleteEvent.java
│   ├── exception/            # 异常处理
│   ├── health/               # 健康检查（V3.0新增）
│   │   └── CustomHealthIndicator.java
│   ├── interceptors/         # 拦截器
│   ├── mapper/               # 数据访问层
│   │   ├── ArticleVersionMapper.java    # 版本控制
│   │   ├── ArticleStatsMapper.java      # 统计
│   │   ├── ArticleAiTagsMapper.java     # AI标签
│   │   ├── AuditLogMapper.java          # 审计日志
│   │   ├── WebhookMapper.java           # Webhook
│   │   ├── WebhookLogMapper.java        # Webhook日志
│   │   ├── WebhookRetryMapper.java      # Webhook重试
│   │   ├── ScheduledTaskMapper.java     # 定时任务
│   │   ├── CommentMapper.java           # 评论（V3.0新增）
│   │   ├── ArticleLikeMapper.java       # 点赞（V3.0新增）
│   │   ├── ArticleCollectMapper.java    # 收藏（V3.0新增）
│   │   ├── UserBehaviorMapper.java      # 用户行为（V3.0新增）
│   │   └── UserMapper.java
│   ├── pojo/                 # 实体类
│   │   ├── ArticleVersion.java          # 文章版本
│   │   ├── ArticleStats.java            # 文章统计
│   │   ├── ArticleAiTags.java           # AI标签
│   │   ├── ArticleSummary.java          # AI摘要
│   │   ├── AuditLog.java                # 审计日志
│   │   ├── ScheduledTask.java           # 定时任务
│   │   ├── Webhook.java                 # Webhook配置
│   │   ├── WebhookLog.java              # Webhook日志
│   │   ├── WebhookRetry.java            # Webhook重试
│   │   ├── Comment.java                 # 评论（V3.0新增）
│   │   ├── CommentVO.java               # 评论VO（V3.0新增）
│   │   ├── UserVO.java                  # 用户VO（V3.0新增）
│   │   ├── ArticleLike.java             # 点赞（V3.0新增）
│   │   ├── ArticleCollect.java          # 收藏（V3.0新增）
│   │   ├── ArticleCollectVO.java        # 收藏VO（V3.0新增）
│   │   ├── UserBehavior.java            # 用户行为（V3.0新增）
│   │   ├── PageBean.java
│   │   ├── Result.java
│   │   └── Category.java
│   ├── security/             # 安全工具
│   │   ├── JwtUtil.java
│   │   └── rbac/             # RBAC实现（V3.0新增）
│   │       ├── entity/
│   │       ├── service/
│   │       ├── annotation/
│   │       └── aspect/
│   ├── service/              # 服务层
│   │   ├── AiService.java               # AI服务
│   │   ├── AiServiceImpl.java           # AI服务实现
│   │   ├── AnalyticsService.java        # 统计分析服务
│   │   ├── SearchService.java           # 搜索服务
│   │   ├── ImageService.java            # 图片处理服务
│   │   ├── WebhookService.java          # Webhook服务
│   │   ├── WebhookRetryService.java     # Webhook重试服务
│   │   ├── ScheduledTaskService.java    # 定时任务服务
│   │   ├── CommentService.java          # 评论服务（V3.0新增）
│   │   ├── ArticleInteractionService.java # 互动服务（V3.0新增）
│   │   ├── RecommendationService.java   # 推荐服务（V3.0新增）
│   │   ├── ExportService.java           # 导出服务（V3.0新增）
│   │   └── impl/
│   ├── scheduler/            # 调度器
│   │   ├── TaskScheduler.java
│   │   └── WebhookRetryScheduler.java
│   ├── annotation/           # 自定义注解
│   │   ├── State.java
│   │   └── RequireRole.java  # 角色注解（V3.0新增）
│   ├── config/               # 配置类
│   │   ├── WebConfig.java
│   │   ├── CacheConfig.java  # 缓存配置（V3.0增强）
│   │   ├── I18nConfig.java
│   │   └── OpenApiConfig.java
│   ├── util/                 # 工具类
│   ├── validation/           # 校验器
│   └── BigEventApplication.java
├── src/main/resources/
│   ├── application.yml       # 应用配置文件
│   └── com/cxx/bigevent/mapper/
│       └── ArticleMapper.xml # MyBatis 映射文件
├── .env                      # 环境变量配置
├── .env.example              # 环境变量示例
├── big_event.sql             # 数据库初始化脚本
├── API_DOCUMENTATION.md      # API接口文档
├── AGENTS.md                 # 开发指南
└── pom.xml                   # Maven 配置文件
```

## 快速开始

### 环境要求

- JDK 23+
- Maven 3.6+
- MySQL 5.7+
- Redis 5.0+
- Ollama（可选，用于AI功能）

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

5. **启动Ollama（可选，用于AI功能）**

```bash
# 启动Ollama服务
ollama serve

# 拉取AI模型
ollama pull deepseek-r1:8b
```

6. **启动项目**

```bash
mvn spring-boot:run
```

7. **访问应用**

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

### 基础接口（已添加 /api 前缀）

| 接口     | 方法     | 路径                     | 认证  |
| ------ | ------ | ---------------------- | --- |
| 用户注册   | POST   | /api/user/register     | 否   |
| 用户登录   | POST   | /api/user/login        | 否   |
| 获取用户信息 | GET    | /api/user/userInfo     | 是   |
| 更新用户信息 | PUT    | /api/user/update       | 是   |
| 更新用户头像 | PATCH  | /api/user/updateAvatar | 是   |
| 修改密码   | PATCH  | /api/user/updatePwd    | 是   |
| 新增分类   | POST   | /api/category          | 是   |
| 分类列表   | GET    | /api/category          | 是   |
| 分类详情   | GET    | /api/category/{id}     | 是   |
| 更新分类   | PUT    | /api/category/{id}     | 是   |
| 删除分类   | DELETE | /api/category/{id}     | 是   |
| 新增文章   | POST   | /api/article           | 是   |
| 文章列表   | GET    | /api/article           | 是   |
| 文章详情   | GET    | /api/article/{id}      | 是   |
| 更新文章   | PUT    | /api/article/{id}      | 是   |
| 删除文章   | DELETE | /api/article/{id}      | 是   |
| 文件上传   | POST   | /api/upload            | 否   |

### AI智能功能（新增）

| 接口       | 方法   | 路径                | 说明               |
| -------- | ---- | ----------------- | ---------------- |
| AI生成摘要   | POST | /api/ai/summarize | 生成文章摘要、关键词、SEO评分 |
| AI生成标签   | POST | /api/ai/tags      | 智能标签推荐           |
| AI内容润色   | POST | /api/ai/improve   | 润色和优化内容          |
| AI内容续写   | POST | /api/ai/continue  | 根据前文续写内容         |
| AI翻译     | POST | /api/ai/translate | 多语言翻译            |
| AI生成标题   | POST | /api/ai/headlines | 标题建议             |
| AI生成大纲   | POST | /api/ai/outline   | 文章大纲             |
| AI SEO评分 | POST | /api/ai/seo-score | SEO分析            |

### 统计分析功能（新增）

| 接口    | 方法  | 路径                        | 说明     |
| ----- | --- | ------------------------- | ------ |
| 数据概览  | GET | /api/analytics/overview   | 整体数据统计 |
| 文章统计  | GET | /api/analytics/articles   | 文章数据统计 |
| 分类统计  | GET | /api/analytics/categories | 分类数据统计 |
| 趋势数据  | GET | /api/analytics/trend      | 数据趋势   |
| 用户活跃度 | GET | /api/analytics/activity   | 用户活跃数据 |

### 批量操作功能（新增）

| 接口     | 方法     | 路径                           | 说明     |
| ------ | ------ | ---------------------------- | ------ |
| 批量删除   | DELETE | /api/articles/batch          | 批量删除文章 |
| 批量更新状态 | PUT    | /api/articles/batch/state    | 批量更新状态 |
| 批量更新分类 | PUT    | /api/articles/batch/category | 批量移动分类 |
| 批量导出   | GET    | /api/articles/batch/export   | 批量导出文章 |

### 版本控制功能（新增）

| 接口     | 方法   | 路径                                              | 说明      |
| ------ | ---- | ----------------------------------------------- | ------- |
| 获取版本历史 | GET  | /api/articles/{id}/versions                     | 获取版本列表  |
| 获取版本详情 | GET  | /api/articles/{id}/versions/{versionId}         | 获取指定版本  |
| 恢复版本   | POST | /api/articles/{id}/versions/{versionId}/restore | 恢复到指定版本 |
| 最新版本   | GET  | /api/articles/{id}/versions/latest              | 获取最新版本  |

### 社交功能（Phase 3新增）

| 接口   | 方法     | 路径                               | 说明      |
| ---- | ------ | -------------------------------- | ------- |
| 发布评论 | POST   | /api/comment                     | 发表评论/回复 |
| 评论列表 | GET    | /api/comment/article/{articleId} | 获取文章评论  |
| 更新评论 | PUT    | /api/comment/{id}                | 更新评论内容  |
| 删除评论 | DELETE | /api/comment/{id}                | 删除评论    |
| 点赞评论 | POST   | /api/comment/{id}/like           | 点赞评论    |
| 点赞文章 | POST   | /api/article/{id}/like           | 点赞文章    |
| 取消点赞 | DELETE | /api/article/{id}/like           | 取消点赞    |
| 收藏文章 | POST   | /api/article/{id}/collect        | 收藏文章    |
| 取消收藏 | DELETE | /api/article/{id}/collect        | 取消收藏    |
| 我的收藏 | GET    | /api/article/collect/my          | 获取收藏列表  |

### 推荐系统（Phase 3新增）

| 接口    | 方法   | 路径                                       | 说明           |
| ----- | ---- | ---------------------------------------- | ------------ |
| 个性化推荐 | GET  | /api/recommendations                     | 基于用户行为的个性化推荐 |
| 热门文章  | GET  | /api/recommendations/hot                 | 热门文章排行       |
| 相似文章  | GET  | /api/recommendations/similar/{articleId} | 相关文章推荐       |
| 记录行为  | POST | /api/recommendations/behavior            | 记录用户浏览行为     |

### RBAC权限管理（Phase 3新增）

| 接口   | 方法     | 路径                                | 说明     |
| ---- | ------ | --------------------------------- | ------ |
| 角色列表 | GET    | /api/admin/roles                  | 获取角色列表 |
| 创建角色 | POST   | /api/admin/roles                  | 创建新角色  |
| 角色详情 | GET    | /api/admin/roles/{id}             | 获取角色详情 |
| 更新角色 | PUT    | /api/admin/roles/{id}             | 更新角色信息 |
| 删除角色 | DELETE | /api/admin/roles/{id}             | 删除角色   |
| 分配权限 | PUT    | /api/admin/roles/{id}/permissions | 分配权限   |
| 获取权限 | GET    | /api/admin/roles/{id}/permissions | 获取角色权限 |

### 数据导出（Phase 3新增）

| 接口   | 方法  | 路径                         | 说明        |
| ---- | --- | -------------------------- | --------- |
| 导出文章 | GET | /api/admin/export/articles | 导出文章Excel |

## 数据库设计

### 核心表

| 表名          | 说明  |
| ----------- | --- |
| tb_user     | 用户表 |
| tb_category | 分类表 |
| tb_article  | 文章表 |

### 新增表

| 表名                 | 说明           |
| ------------------ | ------------ |
| tb_article_version | 文章版本历史表      |
| tb_article_stats   | 文章统计数据表      |
| tb_article_ai_tags | AI生成标签表      |
| tb_article_summary | AI生成摘要表      |
| tb_audit_log       | 审计日志表        |
| tb_webhook         | Webhook配置表   |
| tb_webhook_log     | Webhook执行日志表 |
| tb_webhook_retry   | Webhook重试任务表 |
| tb_scheduled_task  | 定时任务表        |
| tb_comment         | 评论表          |
| tb_article_like    | 文章点赞表        |
| tb_article_collect | 文章收藏表        |
| tb_user_behavior   | 用户行为表        |
| tb_role            | 角色表          |
| tb_permission      | 权限表          |
| tb_role_permission | 角色权限关联表      |
| tb_user_role       | 用户角色关联表      |

## AI配置

项目集成了Ollama本地大语言模型，支持以下功能：

```yaml
# application.yml
ollama:
  host: http://localhost:11434
  model: deepseek-r1:8b
```

**支持的模型：**

- `deepseek-r1:8b` - 推理模型，适合复杂分析任务
- `qwen3:8b` - 通用模型，响应速度较快

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

## 更新日志

### V3.0 (2026-01-21)

**Phase 3 社交功能与AI增强：**

**社交功能：**

- 评论系统（支持嵌套回复、评论点赞）
- 文章点赞/取消点赞
- 文章收藏/取消收藏
- 我的收藏列表管理

**推荐系统：**

- 基于用户行为的个性化推荐
- 热门文章排行
- 相似文章推荐
- Redis缓存优化

**RBAC权限管理：**

- 基于角色的访问控制
- 角色创建和管理
- 权限分配
- 接口级别的权限校验

**系统稳定性：**

- Resilience4j限流配置
- Resilience4j熔断保护
- Actuator + Prometheus监控
- 多级缓存（Caffeine + Redis）

**新增数据表：**

- tb_comment - 评论表
- tb_article_like - 文章点赞表
- tb_article_collect - 文章收藏表
- tb_user_behavior - 用户行为表
- tb_role - 角色表
- tb_permission - 权限表
- tb_role_permission - 角色权限关联表
- tb_user_role - 用户角色关联表

### V2.1 (2026-01-12)

**系统增强功能：**

**Webhooks系统：**

- 事件驱动Webhook通知
- 支持文章创建/更新/删除事件
- HMAC-SHA256签名验证
- 失败自动重试机制（最多3次）
- 执行日志记录

**定时发布任务：**

- 文章定时发布/删除
- 每分钟自动扫描执行
- 任务状态跟踪
- 支持取消和修改时间

**高级搜索：**

- 全文关键词搜索
- 搜索结果高亮显示
- 搜索建议和热词排行
- 基于Redis的热词统计

**图片智能处理：**

- 阿里云OSS集成
- 图片压缩和格式转换
- 水印添加
- 图片信息获取

**新增数据表：**

- tb_webhook - Webhook配置表
- tb_webhook_log - Webhook执行日志表
- tb_webhook_retry - Webhook重试任务表
- tb_scheduled_task - 定时任务表

**技术改进：**

- 数据库时区配置（Asia/Shanghai）
- 异步事件机制
- 重试调度器

### V2.0 (2026-01-08)

**重大更新：**

**AI智能功能：**

- AI生成摘要、关键词、SEO评分
- AI智能标签推荐
- AI内容润色和续写
- AI翻译、标题生成、大纲生成
- 支持Ollama本地大语言模型

**统计分析功能：**

- 数据概览仪表板
- 文章/分类/趋势统计
- 用户活跃度分析

**批量操作功能：**

- 批量删除、更新、导出
- 批量移动分类

**版本控制功能：**

- 文章版本历史记录
- 版本对比和恢复

**接口调整：**

- 所有接口路径添加 `/api` 前缀
- 统一使用 RESTful API 设计规范

**新增数据表：**

- tb_article_version
- tb_article_stats
- tb_article_ai_tags
- tb_article_summary
- tb_audit_log

### V1.0 (2026-01-01)

- 初始版本发布
- 用户管理、分类管理、文章管理
- 文件上传功能
- JWT认证机制

## 测试

### 单元测试

```bash
mvn test
```

### API 测试

推荐使用 Postman 或 Apifox 进行 API 接口测试。

**测试流程：**

1. 调用 `/api/user/login` 获取token
2. 在需要认证的接口中添加：`Authorization: Bearer {token}`
3. 测试AI功能时注意等待时间（60-120秒）

## 常见问题

### 1. 数据库连接失败

检查 `.env` 文件中的数据库配置是否正确，确保 MySQL 服务已启动。

### 2. Redis 连接失败

检查 `.env` 文件中的 Redis 配置是否正确，确保 Redis 服务已启动。

### 3. JWT 令牌过期

JWT 令牌默认有效期为 6 小时，过期后需要重新登录。

### 4. 文件上传失败

检查 `.env` 文件中的阿里云 OSS 配置是否正确，确保网络连接正常。

### 5. AI功能无法使用

确保Ollama服务已启动：

```bash
ollama serve
ollama pull deepseek-r1:8b
```

### 6. AI响应时间过长

deepseek-r1:8b是推理模型，生成响应需要60-120秒。建议前端添加loading状态。

## 许可证

本项目仅供学习和交流使用。

## 联系方式

如有问题或建议，请联系项目维护者。
