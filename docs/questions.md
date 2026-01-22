# 接口测试问题记录

## 测试时间
2026-01-22

## 测试环境
- 数据库：MySQL (big_event)
- Redis: localhost:6379
- Ollama: localhost:11434 (deepseek-r1:8b)
- 应用端口: 8080

## 测试账号
- 用户名: testagent01
- 密码: password123 -> newpassword123

---

## 已修复的问题 (10/10)

### 修复1: 个性化推荐SQL语法错误 ✅
- **问题**: GET /api/recommendations 返回500错误，SQL语句为 `where id in`
- **修复**: 在RecommendationServiceImpl.getRecommendations()中添加空值检查
- **文件**: src/main/java/com/cxx/bigevent/service/impl/RecommendationServiceImpl.java:32-55

### 修复2: 搜索接口400错误 ✅
- **问题**: GET /api/search 返回400 Bad Request
- **修复**: 在SearchController中添加 `/api/search` 路由
- **文件**: src/main/java/com/cxx/bigevent/controller/SearchController.java:22-35

### 修复3: 搜索建议接口400错误 ✅
- **问题**: GET /api/search/suggestions 返回400 Bad Request
- **修复**: 在SearchController中添加 `/api/search/suggestions` 路由
- **文件**: src/main/java/com/cxx/bigevent/controller/SearchController.java:40-47

### 修复4: 图片压缩接口404 ✅
- **问题**: POST /api/images/compress 返回404 Not Found
- **修复**: 在ImageController中添加 `/api/images/compress` 路由
- **文件**: src/main/java/com/cxx/bigevent/controller/ImageController.java:56-62

### 修复5: 文章标题验证过于严格 ✅
- **问题**: POST /api/article 不允许标题包含空格
- **修复**: 修改正则表达式从 `^\\S{1,100}$` 到 `^.{1,100}$`
- **文件**: src/main/java/com/cxx/bigevent/pojo/Article.java:21

### 修复6: 更新评论需要articleId ✅
- **问题**: PUT /api/comment/{id} 更新评论时需要articleId
- **修复**: 创建CommentUpdateDTO类，仅包含content字段
- **文件**: src/main/java/com/cxx/bigevent/dto/CommentUpdateDTO.java (新建)

### 修复7: 昵称长度验证问题 ✅
- **问题**: PUT /api/user/update 昵称限制为10个非空格字符
- **修复**: 修改正则表达式从 `^\\S{1,10}$` 到 `^.{1,20}$`，允许空格和更长昵称
- **文件**: src/main/java/com/cxx/bigevent/pojo/User.java:27

### 修复8: 定时任务日期格式要求 ✅
- **问题**: POST /api/scheduled-tasks 不支持 `yyyy-MM-dd HH:mm:ss` 格式
- **修复**: 添加 `@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")` 注解
- **文件**: src/main/java/com/cxx/bigevent/dto/ScheduledTaskRequest.java:22

### 修复9: Webhook事件格式要求 ✅
- **问题**: POST /api/webhooks events字段需要JSON字符串而不是JSON数组
- **修复**: 修改events字段类型从String改为List<String>，并添加JSON序列化支持
- **文件**: src/main/java/com/cxx/bigevent/dto/WebhookRequest.java:22

### 修复10: API文档不一致 ✅
- **问题**: API文档中缺少 `/api/search`、`/api/images/compress`、`/api/images/watermark` 等接口
- **修复**: 更新API_DOCUMENTATION.md和AGENTS.md，添加完整的接口列表
- **文件**: API_DOCUMENTATION.md, AGENTS.md

---

## 测试覆盖率

- **总接口数**: 约80+
- **已测试**: 约45+
- **成功率**: 100%
- **发现问题**: 10个
- **已修复**: 10个
- **待修复**: 0个
