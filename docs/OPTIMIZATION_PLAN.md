# Big Event CMS 项目优化计划

## 一、安全问题（高优先级）

### 1.1 密钥硬编码问题

| 位置 | 问题 | 修复方案 |
|------|------|----------|
| `JwtUtil.java:11` | `KEY = "itheima"` 硬编码 | 改为从环境变量读取 |
| `application.yml:31-32` | OSS 密钥硬编码 | 使用 `${OSS_ACCESS_KEY_ID:默认值}` |
| `application.yml:6` | 数据库密码硬编码 | 使用环境变量或加密配置 |

### 1.2 SQL 注入风险

| 位置 | 问题 | 修复方案 |
|------|------|----------|
| `ArticleMapper.java:64-65` | `findRandomArticleIds()` 使用 `${excludeIds}` | 改用 `foreach` + `#{}` |
| `SearchServiceImpl.java:93` | 正则表达式拼接 | 添加关键字转义方法 |

### 1.3 XSS 风险

| 位置 | 问题 | 修复方案 |
|------|------|----------|
| `SearchServiceImpl.java:79-98` | `highlight()` 方法直接拼接 HTML | 对关键字进行 HTML 转义 |
| `AiServiceImpl` 多个方法 | AI 返回内容未转义 | 添加 HTML 转义处理 |

### 1.4 权限控制缺失

| 位置 | 问题 | 修复方案 |
|------|------|----------|
| `ExportController.java` | 导出接口无权限注解 | 添加 `@RequirePermission("article:export")` |
| `CategoryController.java:64-68` | `deleteWithArticles()` 无权限控制 | 添加权限注解 |
| `RbacServiceImpl.java` | 返回固定权限 | 从数据库查询用户实际权限 |

---

## 二、性能问题（高优先级）

### 2.1 N+1 查询问题

| 位置 | 问题 | 修复方案 |
|------|------|----------|
| `CommentServiceImpl.java:51-62` | 循环中调用 `userMapper.findUserVOById` | 使用 LEFT JOIN 一次性查询 |
| `RecommendationServiceImpl.java:43-49` | 循环中逐条查询 | 添加 `findByIds()` 批量查询方法 |
| `RecommendationServiceImpl.java:97-103` | 循环中查询热门文章 | 批量查询或缓存完整对象 |

### 2.2 缓存优化

| 位置 | 问题 | 修复方案 |
|------|------|----------|
| `RecommendationServiceImpl` | 只缓存文章 ID | 缓存完整 Article 对象 |
| `UserServiceImpl.findByUserName()` | 频繁查询未缓存 | 添加 Redis 缓存 |
| `CacheConfig` 已配置但未使用 | Caffeine 缓存未应用 | 对热点数据添加 `@Cacheable` |

---

## 三、代码质量问题（中优先级）

### 3.1 代码风格统一

| 位置 | 问题 | 修复方案 |
|------|------|----------|
| `Article.java` | `@Data` + 手动 getter | 删除手动方法 |
| `dto/` 目录 | DTO 风格不一致 | 统一使用 Lombok |
| 各 Controller | 异常处理风格不统一 | 统一使用 `BusinessException` |

### 3.2 代码重复

| 位置 | 问题 | 修复方案 |
|------|------|----------|
| `ThreadLocalUtil.get()` 调用 | 多个 Service 重复 | 创建 `UserContext` 工具类 |

### 3.3 方法/类过长

| 位置 | 问题 | 修复方案 |
|------|------|----------|
| `AiServiceImpl.java` (510行) | 类过长 | 拆分为 `OllamaService`、`AiSummaryService` 等 |
| `AiServiceImpl.generateContent()` (100+行) | 方法过长 | 拆分为多个私有方法 |

---

## 四、架构问题（中优先级）

### 4.1 分层问题

| 位置 | 问题 | 修复方案 |
|------|------|----------|
| `BatchOperationController.java` | Controller 直接调用 Mapper | 改为通过 Service 调用 |

---

## 五、最佳实践优化（中/低优先级）

### 5.1 API 文档

| 位置 | 问题 | 修复方案 |
|------|------|----------|
| 各 Controller | 缺少 `@Operation`、`@Parameter` 注解 | 添加 OpenAPI 文档注解 |

### 5.2 输入验证

| 位置 | 问题 | 修复方案 |
|------|------|----------|
| `ArticleController.list()` | pageNum/pageSize 未校验 | 添加 `@Min(1)` `@Max(100)` |
| `AiController.generateContent()` | 无内容长度限制 | 添加 `@Size(max=xxx)` |
| `SearchController.suggest()` | 无参数长度限制 | 添加参数校验 |

### 5.3 日志规范

| 问题 | 修复方案 |
|------|----------|
| 日志使用字符串拼接 | 统一使用 `log.info("msg: {}", value)` |
| 日志级别不统一 | 制定日志级别规范 |

### 5.4 常量管理

| 问题 | 修复方案 |
|------|----------|
| 魔法数字散落代码中 | 创建 `Constants` 类集中管理 |
| `TOKEN_EXPIRATION_MS` 等未提取 | 提取为具名常量 |

---

## 执行顺序建议

```
Phase 1: 安全修复（立即执行）
├── 1.1 JWT 密钥环境变量化
├── 1.2 SQL 注入修复
├── 1.3 配置文件敏感信息脱敏
└── 1.4 权限注解补充

Phase 2: 性能优化
├── 2.1 N+1 查询修复
├── 2.2 缓存策略优化
└── 2.3 批量查询添加

Phase 3: 代码质量
├── 3.1 代码风格统一
├── 3.2 代码重复消除
├── 3.3 AiServiceImpl 拆分
└── 3.4 Controller 移除 Mapper 直接调用

Phase 4: 最佳实践
├── 4.1 API 文档完善
├── 4.2 输入验证加强
├── 4.3 日志规范统一
└── 4.4 常量提取管理
```

---

## 详细问题清单

### 安全类

- [x] JwtUtil.java - 密钥环境变量化
- [x] ArticleMapper.java - SQL 注入风险修复 (`${excludeIds}`)
- [x] SearchServiceImpl.java - 正则表达式注入修复
- [x] SearchServiceImpl.java - XSS 风险修复
- [x] AiServiceImpl - XSS 风险修复
- [x] application.yml - 敏感信息脱敏
- [x] ExportController - 添加权限注解
- [x] CategoryController.deleteWithArticles - 添加权限控制
- [ ] RbacServiceImpl - 从数据库查询用户实际权限

### 性能类

- [x] CommentServiceImpl - N+1 查询修复
- [x] RecommendationServiceImpl - N+1 查询修复
- [x] RecommendationServiceImpl - 缓存优化
- [ ] UserServiceImpl - 缺少用户信息缓存
- [ ] CacheConfig - Caffeine 未使用

### 代码质量类

- [x] Article.java - @Data + 手动 getter 删除
- [x] dto/ 目录 - 风格统一使用 Lombok
- [x] Service - ThreadLocalUtil.get() 重复封装为 UserContext
- [ ] AiServiceImpl - 类过长需拆分
- [x] 各 Controller - 异常处理风格统一

### 架构类

- [x] BatchOperationController - 改为通过 Service 调用 Mapper

### 最佳实践类

- [x] 各 Controller - 添加 OpenAPI 文档注解
- [x] ArticleController.list() - 添加参数校验
- [ ] AiController - 缺少内容长度限制
- [ ] SearchController - 缺少参数长度限制
- [x] 日志规范统一使用占位符
- [x] 常量提取为 Constants 类
