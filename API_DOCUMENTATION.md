# Big Event API 接口文档

## 文档概述

本文档描述了 Big Event 内容管理系统后端 API 接口的完整规范。

**基础信息：**
- 基础URL: `http://localhost:8080`
- 数据格式: JSON
- 字符编码: UTF-8
- 认证方式: JWT Token (Bearer Token)

## 统一响应格式

```json
{
  "code": 0,
  "message": "操作成功",
  "data": {}
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 响应状态码，0表示成功，非0表示失败 |
| message | String | 响应消息描述 |
| data | Object/String/Array/Null | 响应数据 |

---

## 一、用户管理接口 `/api/user`

### 1.1 用户注册
- **路径:** `/api/user/register`
- **方法:** POST
- **描述:** 根据用户名和密码注册新用户

### 1.2 用户登录
- **路径:** `/api/user/login`
- **方法:** POST
- **描述:** 根据用户名密码获取访问令牌

### 1.3 获取用户信息
- **路径:** `/api/user/userInfo`
- **方法:** GET
- **描述:** 获取当前登录用户的详细信息

### 1.4 更新用户信息
- **路径:** `/api/user/update`
- **方法:** PUT
- **描述:** 更新当前登录用户的详细信息

### 1.5 更新用户头像
- **路径:** `/api/user/updateAvatar`
- **方法:** PATCH
- **描述:** 更新当前登录用户的头像URL

### 1.6 修改密码
- **路径:** `/api/user/updatePwd`
- **方法:** PATCH
- **描述:** 更新当前登录用户的密码

---

## 二、分类管理接口 `/api/category`

### 2.1 创建分类
- **路径:** `/api/category`
- **方法:** POST
- **描述:** 创建一个新的文章分类

### 2.2 分类列表
- **路径:** `/api/category`
- **方法:** GET
- **描述:** 获取所有分类列表

### 2.3 获取分类
- **路径:** `/api/category/{id}`
- **方法:** GET
- **描述:** 根据ID获取分类详情

### 2.4 获取分类文章
- **路径:** `/api/category/{id}/articles`
- **方法:** GET
- **描述:** 获取指定分类下的所有文章

### 2.5 更新分类
- **路径:** `/api/category/{id}`
- **方法:** PUT
- **描述:** 根据ID更新分类信息

### 2.6 删除分类
- **路径:** `/api/category/{id}`
- **方法:** DELETE
- **描述:** 根据ID删除分类（仅当分类下无文章时）

### 2.7 强制删除分类
- **路径:** `/api/category/{id}/force`
- **方法:** DELETE
- **描述:** 强制删除分类及其所有关联文章

---

## 三、文章管理接口 `/api/article`

### 3.1 创建文章
- **路径:** `/api/article`
- **方法:** POST
- **描述:** 创建一篇新文章

### 3.2 文章列表
- **路径:** `/api/article`
- **方法:** GET
- **描述:** 分页查询文章列表，支持按分类和状态筛选
- **参数:** pageNum, pageSize, categoryId, state

### 3.3 获取文章
- **路径:** `/api/article/{id}`
- **方法:** GET
- **描述:** 根据ID获取文章详情

### 3.4 更新文章
- **路径:** `/api/article/{id}`
- **方法:** PUT
- **描述:** 根据ID更新文章内容

### 3.5 删除文章
- **路径:** `/api/article/{id}`
- **方法:** DELETE
- **描述:** 根据ID删除文章

---

## 四、文章互动接口 `/api/article`

### 4.1 点赞文章
- **路径:** `/api/article/{id}/like`
- **方法:** POST
- **描述:** 对指定文章点赞

### 4.2 取消点赞
- **路径:** `/api/article/{id}/like`
- **方法:** DELETE
- **描述:** 取消对指定文章的点赞

### 4.3 获取点赞状态
- **路径:** `/api/article/{id}/like/status`
- **方法:** GET
- **描述:** 获取指定文章的点赞数和当前用户的点赞状态

### 4.4 收藏文章
- **路径:** `/api/article/{id}/collect`
- **方法:** POST
- **描述:** 将指定文章收藏到个人收藏夹

### 4.5 取消收藏
- **路径:** `/api/article/{id}/collect`
- **方法:** DELETE
- **描述:** 取消对指定文章的收藏

### 4.6 获取收藏状态
- **路径:** `/api/article/{id}/collect/status`
- **方法:** GET
- **描述:** 获取指定文章的收藏状态

### 4.7 我的收藏
- **路径:** `/api/article/collect/my`
- **方法:** GET
- **描述:** 获取当前用户的所有收藏文章

---

## 五、评论管理接口 `/api/comment`

### 5.1 发表评论
- **路径:** `/api/comment`
- **方法:** POST
- **描述:** 对文章发表评论或回复

### 5.2 获取评论
- **路径:** `/api/comment/article/{articleId}`
- **方法:** GET
- **描述:** 获取指定文章的所有评论

### 5.3 更新评论
- **路径:** `/api/comment/{id}`
- **方法:** PUT
- **描述:** 更新指定评论的内容

### 5.4 删除评论
- **路径:** `/api/comment/{id}`
- **方法:** DELETE
- **描述:** 删除指定评论

### 5.5 点赞评论
- **路径:** `/api/comment/{id}/like`
- **方法:** POST
- **描述:** 对指定评论点赞

---

## 六、批量操作接口 `/api/articles`

### 6.1 批量删除
- **路径:** `/api/articles/batch`
- **方法:** DELETE
- **描述:** 批量删除指定ID的文章

### 6.2 批量更新状态
- **路径:** `/api/articles/batch/state`
- **方法:** PUT
- **描述:** 批量更新文章发布状态

### 6.3 批量移动分类
- **路径:** `/api/articles/batch/category`
- **方法:** PUT
- **描述:** 批量将文章移动到指定分类

### 6.4 批量导出
- **路径:** `/api/articles/batch/export`
- **方法:** GET
- **描述:** 根据ID列表批量导出文章

---

## 七、文章版本接口 `/api/articles`

### 7.1 获取版本历史
- **路径:** `/api/articles/{id}/versions`
- **方法:** GET
- **描述:** 获取指定文章的所有版本历史

### 7.2 获取版本详情
- **路径:** `/api/articles/{id}/versions/{versionId}`
- **方法:** GET
- **描述:** 获取指定文章的某个版本详情

### 7.3 恢复版本
- **路径:** `/api/articles/{id}/versions/{versionId}/restore`
- **方法:** POST
- **描述:** 将文章恢复到指定的历史版本

### 7.4 获取最新版本
- **路径:** `/api/articles/{id}/versions/latest`
- **方法:** GET
- **描述:** 获取指定文章的最新版本

---

## 八、AI服务接口 `/api/ai`

### 8.1 生成摘要
- **路径:** `/api/ai/summarize`
- **方法:** POST
- **描述:** 对给定内容生成简洁的摘要

### 8.2 生成文章摘要
- **路径:** `/api/ai/summarize/{articleId}`
- **方法:** POST
- **描述:** 根据文章ID生成文章摘要（带缓存）

### 8.3 生成标签
- **路径:** `/api/ai/tags`
- **方法:** POST
- **描述:** 根据内容自动生成相关标签

### 8.4 生成文章标签
- **路径:** `/api/ai/tags/{articleId}`
- **方法:** POST
- **描述:** 根据文章ID生成文章标签（带缓存）

### 8.5 优化内容
- **路径:** `/api/ai/improve`
- **方法:** POST
- **描述:** 使用AI优化和改写内容

### 8.6 续写内容
- **路径:** `/api/ai/continue`
- **方法:** POST
- **描述:** 根据给定内容进行AI续写

### 8.7 续写内容(带上下文)
- **路径:** `/api/ai/continue/{context}`
- **方法:** POST
- **描述:** 根据给定内容和上下文进行AI续写

### 8.8 内容翻译
- **路径:** `/api/ai/translate`
- **方法:** POST
- **描述:** 将内容翻译成指定语言
- **参数:** targetLanguage (默认: 中文)

### 8.9 生成标题
- **路径:** `/api/ai/headlines`
- **方法:** POST
- **描述:** 根据内容生成吸引人的标题建议

### 8.10 生成大纲
- **路径:** `/api/ai/outline`
- **方法:** POST
- **描述:** 根据内容生成文章大纲

### 8.11 SEO评分
- **路径:** `/api/ai/seo-score`
- **方法:** POST
- **描述:** 分析内容的SEO优化程度并给出评分
- **参数:** title

### 8.12 流式生成
- **路径:** `/api/ai/stream`
- **方法:** GET
- **描述:** 使用SSE流式输出AI生成结果
- **参数:** prompt, type

---

## 九、数据分析接口 `/api/analytics`

### 9.1 数据概览
- **路径:** `/api/analytics/overview`
- **方法:** GET
- **描述:** 获取当前用户的数据统计概览

### 9.2 文章分析
- **路径:** `/api/analytics/articles`
- **方法:** GET
- **描述:** 获取当前用户的文章数据分析

### 9.3 分类统计
- **路径:** `/api/analytics/categories`
- **方法:** GET
- **描述:** 获取当前用户的分类统计数据

### 9.4 趋势分析
- **路径:** `/api/analytics/trend`
- **方法:** GET
- **描述:** 获取近期数据变化趋势
- **参数:** days (默认: 7)

### 9.5 用户活动
- **路径:** `/api/analytics/activity`
- **方法:** GET
- **描述:** 获取用户活动数据统计
- **参数:** startTime, endTime

---

## 十、搜索服务接口 `/api/search`

### 10.1 搜索文章
- **路径:** `/api/search`
- **方法:** GET
- **描述:** 根据关键词搜索文章，支持分页和筛选

### 10.2 搜索文章
- **路径:** `/api/search/articles`
- **方法:** GET
- **描述:** 根据关键词搜索文章，支持分页和筛选

### 10.3 搜索建议
- **路径:** `/api/search/suggest`
- **方法:** GET
- **描述:** 根据前缀获取搜索建议词
- **参数:** prefix

### 10.4 搜索建议
- **路径:** `/api/search/suggestions`
- **方法:** GET
- **描述:** 根据前缀获取搜索建议词
- **参数:** prefix

### 10.5 热门搜索
- **路径:** `/api/search/hot`
- **方法:** GET
- **描述:** 获取热门搜索关键词
- **参数:** limit (默认: 10)

### 10.6 同步索引
- **路径:** `/api/search/sync`
- **方法:** POST
- **描述:** 将文章数据同步到搜索引擎索引

---

## 十一、图片处理接口 `/api/images`

### 11.1 上传图片
- **路径:** `/api/images/upload`
- **方法:** POST
- **描述:** 上传图片并支持压缩处理
- **参数:** file, compress (默认: true), quality (默认: 85)

### 11.2 获取图片信息
- **路径:** `/api/images/info`
- **方法:** GET
- **描述:** 获取图片的尺寸、格式等详细信息
- **参数:** url

### 11.3 处理图片
- **路径:** `/api/images/process`
- **方法:** POST
- **描述:** 对图片进行压缩、格式转换等处理

### 11.4 压缩图片
- **路径:** `/api/images/compress`
- **方法:** POST
- **描述:** 对图片进行压缩处理

### 11.5 添加水印
- **路径:** `/api/images/watermark`
- **方法:** POST
- **描述:** 为图片添加文字水印

### 11.6 图片OCR
- **路径:** `/api/images/ocr`
- **方法:** POST
- **描述:** 从图片中提取文字内容
- **参数:** url

---

## 十二、推荐服务接口 `/api/recommendations`

### 12.1 个性化推荐
- **路径:** `/api/recommendations`
- **方法:** GET
- **描述:** 根据用户行为获取个性化文章推荐
- **参数:** limit

### 12.2 热门文章
- **路径:** `/api/recommendations/hot`
- **方法:** GET
- **描述:** 获取热门文章排行榜
- **参数:** limit

### 12.3 相似文章
- **路径:** `/api/recommendations/similar/{articleId}`
- **方法:** GET
- **描述:** 获取与指定文章相似的内容推荐
- **参数:** limit

### 12.4 记录行为
- **路径:** `/api/recommendations/behavior`
- **方法:** POST
- **描述:** 记录用户的浏览、点赞等行为用于推荐计算

---

## 十三、Webhook管理接口 `/api/webhooks`

### 13.1 创建Webhook
- **路径:** `/api/webhooks`
- **方法:** POST
- **描述:** 创建一个新的 Webhook 配置

### 13.2 Webhook列表
- **路径:** `/api/webhooks`
- **方法:** GET
- **描述:** 获取所有 Webhook 配置列表

### 13.3 获取Webhook
- **路径:** `/api/webhooks/{id}`
- **方法:** GET
- **描述:** 根据ID获取 Webhook 配置详情

### 13.4 更新Webhook
- **路径:** `/api/webhooks/{id}`
- **方法:** PUT
- **描述:** 更新指定的 Webhook 配置

### 13.5 删除Webhook
- **路径:** `/api/webhooks/{id}`
- **方法:** DELETE
- **描述:** 删除指定的 Webhook 配置

### 13.6 测试Webhook
- **路径:** `/api/webhooks/{id}/test`
- **方法:** POST
- **描述:** 测试指定的 Webhook 是否可用

### 13.7 获取调用日志
- **路径:** `/api/webhooks/{id}/logs`
- **方法:** GET
- **描述:** 获取指定 Webhook 的调用记录

---

## 十四、定时任务接口 `/api/scheduled-tasks`

### 14.1 创建定时任务
- **路径:** `/api/scheduled-tasks`
- **方法:** POST
- **描述:** 创建一个新的定时发布任务

### 14.2 任务列表
- **路径:** `/api/scheduled-tasks`
- **方法:** GET
- **描述:** 获取所有定时任务列表

### 14.3 获取任务
- **路径:** `/api/scheduled-tasks/{id}`
- **方法:** GET
- **描述:** 根据ID获取定时任务详情

### 14.4 更新时间
- **路径:** `/api/scheduled-tasks/{id}/time`
- **方法:** PUT
- **描述:** 更新定时任务的执行时间
- **参数:** scheduledTime

### 14.5 取消任务
- **路径:** `/api/scheduled-tasks/{id}`
- **方法:** DELETE
- **描述:** 取消指定的定时任务

### 14.6 立即执行
- **路径:** `/api/scheduled-tasks/{id}/execute`
- **方法:** POST
- **描述:** 立即执行指定的定时任务

---

## 十五、文件上传接口

### 15.1 上传文件
- **路径:** `/upload`
- **方法:** POST
- **描述:** 上传图片文件到阿里云OSS

---

## 十六、管理员接口

### 16.1 角色管理
- **路径:** `/api/admin/roles`
- **方法:** GET
- **描述:** 列出所有角色

- **路径:** `/api/admin/roles`
- **方法:** POST
- **描述:** 创建角色

- **路径:** `/api/admin/roles/{id}`
- **方法:** GET
- **描述:** 根据ID获取角色

- **路径:** `/api/admin/roles/{id}`
- **方法:** PUT
- **描述:** 更新角色

- **路径:** `/api/admin/roles/{id}`
- **方法:** DELETE
- **描述:** 删除角色

- **路径:** `/api/admin/roles/{id}/permissions`
- **方法:** PUT
- **描述:** 为角色分配权限

- **路径:** `/api/admin/roles/{id}/permissions`
- **方法:** GET
- **描述:** 获取角色权限

### 16.2 数据导出
- **路径:** `/api/admin/export/articles`
- **方法:** GET
- **描述:** 导出所有文章数据（JSON格式）

- **路径:** `/api/admin/export/articles/Excel`
- **方法:** GET
- **描述:** 导出所有文章数据（Excel格式）

---

## 十七、错误码说明

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

---

## 十八、测试建议

1. 先调用 `/api/user/login` 获取token
2. 在需要认证的接口中添加请求头：`Authorization: Bearer {token}`
3. 测试AI功能时注意等待时间（60-120秒）
