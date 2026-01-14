# Big Event API 接口文档

## 文档概述

本文档详细描述了 Big Event 内容管理系统后端 API 接口的完整规范，包括接口路径、请求参数、响应格式、业务逻辑说明和错误处理机制。

**基础信息：**
- 基础URL: `http://localhost:8080`
- 数据格式: JSON
- 字符编码: UTF-8
- 认证方式: JWT Token (Bearer Token)

## 统一响应格式

所有接口均采用统一的响应格式：

```json
{
  "code": 0,
  "message": "操作成功",
  "data": {}
}
```

**字段说明：**

| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 响应状态码，0表示成功，非0表示失败 |
| message | String | 响应消息描述 |
| data | Object/String/Array/Null | 响应数据，成功时返回具体数据，失败时为null |

## 错误码说明

| 错误码 | 说明 | HTTP状态码 |
|--------|------|------------|
| 0 | 操作成功 | 200 |
| 1 | 操作失败 | 400/500 |
| 1001 | 用户名已存在 | 400 |
| 1002 | 用户名或密码错误 | 401 |
| 1006 | 原密码错误 | 400 |
| 1007 | 新密码与原密码一致 | 400 |
| 1008 | 新密码与确认密码不一致 | 400 |
| 2001 | 分类不存在 | 404 |
| 2002 | 分类下存在文章，无法删除 | 400 |
| 2003 | 文章不存在 | 404 |
| 3001 | 文件上传失败 | 500 |
| 3002 | 文件格式不支持 | 400 |
| 4002 | 必要参数不能为空 | 400 |
| 4003 | 参数格式错误 | 400 |

---

## 一、用户管理接口

### 1.1 用户注册

**接口描述：** 创建新用户账号，用户名必须唯一。

**请求信息：**
- **路径：** `/user/register`
- **方法：** POST
- **权限要求：** 无需认证
- **Content-Type：** application/json

**请求参数：**

| 字段名 | 类型 | 必填 | 约束条件 | 说明 | 示例 |
|--------|------|------|----------|------|------|
| username | String | 是 | 5-16位非空字符 | 用户名，全局唯一 | "zhangsan" |
| password | String | 是 | 5-16位非空字符 | 密码，明文传输 | "123456" |

**请求示例：**

```json
{
  "username": "zhangsan",
  "password": "123456"
}
```

**成功响应：**

```json
{
  "code": 0,
  "message": "操作成功",
  "data": null
}
```

**失败响应：**

```json
{
  "code": 1001,
  "message": "用户名已存在",
  "data": null
}
```

**业务逻辑说明：**
1. 校验用户名格式（5-16位非空字符）
2. 校验密码格式（5-16位非空字符）
3. 检查用户名是否已存在
4. 如果用户名不存在，则创建新用户，密码使用MD5加密存储
5. 返回注册成功结果

---

### 1.2 用户登录

**接口描述：** 用户使用用户名和密码登录，成功后返回JWT令牌。

**请求信息：**
- **路径：** `/user/login`
- **方法：** POST
- **权限要求：** 无需认证
- **Content-Type：** application/json

**请求参数：**

| 字段名 | 类型 | 必填 | 约束条件 | 说明 | 示例 |
|--------|------|------|----------|------|------|
| username | String | 是 | 5-16位非空字符 | 用户名 | "zhangsan" |
| password | String | 是 | 5-16位非空字符 | 密码 | "123456" |

**请求示例：**

```json
{
  "username": "zhangsan",
  "password": "123456"
}
```

**成功响应：**

```json
{
  "code": 0,
  "message": "操作成功",
  "data": "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJhZG1pbiIsImV4cCI6MTcwNjU1MjAwMH0.abc123..."
}
```

**失败响应：**

```json
{
  "code": 1002,
  "message": "用户名或密码错误",
  "data": null
}
```

**业务逻辑说明：**
1. 校验用户名和密码格式
2. 根据用户名查询用户信息
3. 使用MD5加密输入的密码，与数据库中的密码比对
4. 如果密码匹配，生成JWT令牌（包含用户ID和用户名）
5. 将令牌存储到Redis中，有效期6小时
6. 返回JWT令牌，后续请求需在请求头中携带该令牌

**认证方式：**
后续需要认证的接口，需在请求头中添加：
```
Authorization: Bearer {token}
```

---

### 1.3 获取用户信息

**接口描述：** 获取当前登录用户的详细信息。

**请求信息：**
- **路径：** `/user/userInfo`
- **方法：** GET
- **权限要求：** 需要认证
- **请求头：** Authorization: Bearer {token}

**请求参数：** 无

**成功响应：**

```json
{
  "code": 0,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "zhangsan",
    "nickname": "张三",
    "email": "zhangsan@example.com",
    "userPic": "https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_avatar.png",
    "createTime": "2026-01-13 15:41:46",
    "updateTime": "2026-01-13 15:41:46"
  }
}
```

**字段说明：**

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Integer | 用户ID |
| username | String | 用户名 |
| nickname | String | 昵称（1-10位非空字符） |
| email | String | 邮箱地址 |
| userPic | String | 头像URL |
| createTime | String | 创建时间 |
| updateTime | String | 更新时间 |

**业务逻辑说明：**
1. 从JWT令牌中解析用户名
2. 根据用户名查询用户信息
3. 返回用户详细信息（不包含密码字段）

---

### 1.4 更新用户信息

**接口描述：** 更新当前登录用户的昵称和邮箱信息。

**请求信息：**
- **路径：** `/user/update`
- **方法：** PUT
- **权限要求：** 需要认证
- **Content-Type：** application/json
- **请求头：** Authorization: Bearer {token}

**请求参数：**

| 字段名 | 类型 | 必填 | 约束条件 | 说明 | 示例 |
|--------|------|------|----------|------|------|
| id | Integer | 是 | 非空 | 用户ID | 1 |
| nickname | String | 是 | 1-10位非空字符 | 昵称 | "张三" |
| email | String | 是 | 邮箱格式 | 邮箱地址 | "zhangsan@example.com" |

**请求示例：**

```json
{
  "id": 1,
  "nickname": "张三",
  "email": "zhangsan@example.com"
}
```

**成功响应：**

```json
{
  "code": 0,
  "message": "操作成功",
  "data": null
}
```

**业务逻辑说明：**
1. 从JWT令牌中解析用户ID
2. 校验用户ID、昵称和邮箱格式
3. 更新用户信息（只能更新当前登录用户的信息）
4. 自动更新update_time字段

---

### 1.5 更新用户头像

**接口描述：** 更新当前登录用户的头像URL。

**请求信息：**
- **路径：** `/user/updateAvatar`
- **方法：** PATCH
- **权限要求：** 需要认证
- **Content-Type：** application/x-www-form-urlencoded 或 multipart/form-data
- **请求头：** Authorization: Bearer {token}

**请求参数：**

| 字段名 | 类型 | 必填 | 约束条件 | 说明 | 示例 |
|--------|------|------|----------|------|------|
| avatarUrl | String | 是 | URL格式 | 头像URL | "https://example.com/avatar.jpg" |

**请求示例：**

```
avatarUrl=https://example.com/avatar.jpg
```

**成功响应：**

```json
{
  "code": 0,
  "message": "操作成功",
  "data": null
}
```

**失败响应：**

```json
{
  "code": 4003,
  "message": "参数格式错误",
  "data": null
}
```

**业务逻辑说明：**
1. 从JWT令牌中解析用户ID
2. 校验avatarUrl格式（必须是有效的URL）
3. 更新用户头像URL
4. 自动更新update_time字段

---

### 1.6 修改密码

**接口描述：** 修改当前登录用户的密码。

**请求信息：**
- **路径：** `/user/updatePwd`
- **方法：** PATCH
- **权限要求：** 需要认证
- **Content-Type：** application/json
- **请求头：** Authorization: Bearer {token}

**请求参数：**

| 字段名 | 类型 | 必填 | 约束条件 | 说明 | 示例 |
|--------|------|------|----------|------|------|
| oldPwd | String | 是 | 非空 | 原密码 | "123456" |
| newPwd | String | 是 | 非空 | 新密码 | "654321" |
| rePwd | String | 是 | 非空 | 确认密码 | "654321" |

**请求示例：**

```json
{
  "oldPwd": "123456",
  "newPwd": "654321",
  "rePwd": "654321"
}
```

**成功响应：**

```json
{
  "code": 0,
  "message": "操作成功",
  "data": null
}
```

**失败响应：**

```json
{
  "code": 1006,
  "message": "原密码错误",
  "data": null
}
```

```json
{
  "code": 1007,
  "message": "新密码与原密码一致",
  "data": null
}
```

```json
{
  "code": 1008,
  "message": "新密码与确认密码不一致",
  "data": null
}
```

**业务逻辑说明：**
1. 从JWT令牌中解析用户名
2. 校验三个密码字段是否为空
3. 验证原密码是否正确（使用MD5加密比对）
4. 验证新密码不能与原密码相同
5. 验证新密码与确认密码是否一致
6. 更新密码（使用MD5加密存储）
7. 删除Redis中的旧令牌，强制用户重新登录
8. 自动更新update_time字段

---

## 二、文章分类接口

### 2.1 新增分类

**接口描述：** 创建新的文章分类。

**请求信息：**
- **路径：** `/category`
- **方法：** POST
- **权限要求：** 需要认证
- **Content-Type：** application/json
- **请求头：** Authorization: Bearer {token}

**请求参数：**

| 字段名 | 类型 | 必填 | 约束条件 | 说明 | 示例 |
|--------|------|------|----------|------|------|
| categoryName | String | 是 | 非空 | 分类名称 | "科技" |
| categoryAlias | String | 是 | 非空 | 分类别名，同一用户下唯一 | "tech" |

**请求示例：**

```json
{
  "categoryName": "科技",
  "categoryAlias": "tech"
}
```

**成功响应：**

```json
{
  "code": 0,
  "message": "操作成功",
  "data": null
}
```

**业务逻辑说明：**
1. 从JWT令牌中解析用户ID
2. 校验分类名称和分类别名格式
3. 创建分类，关联当前用户ID
4. 自动设置create_time和update_time

---

### 2.2 分类列表

**接口描述：** 获取当前登录用户创建的所有分类列表。

**请求信息：**
- **路径：** `/category`
- **方法：** GET
- **权限要求：** 需要认证
- **请求头：** Authorization: Bearer {token}

**请求参数：** 无

**成功响应：**

```json
{
  "code": 0,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "categoryName": "科技",
      "categoryAlias": "tech",
      "createTime": "2026-01-13 15:42:32",
      "updateTime": "2026-01-13 15:42:32"
    },
    {
      "id": 2,
      "categoryName": "生活",
      "categoryAlias": "life",
      "createTime": "2026-01-13 15:42:32",
      "updateTime": "2026-01-13 15:42:32"
    }
  ]
}
```

**业务逻辑说明：**
1. 从JWT令牌中解析用户ID
2. 查询该用户创建的所有分类
3. 返回分类列表（不包含createUser字段）

---

### 2.3 分类详情

**接口描述：** 根据分类ID获取分类详细信息。

**请求信息：**
- **路径：** `/category/{id}`
- **方法：** GET
- **权限要求：** 需要认证
- **请求头：** Authorization: Bearer {token}

**路径参数：**

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| id | Integer | 是 | 分类ID | 1 |

**成功响应：**

```json
{
  "code": 0,
  "message": "操作成功",
  "data": {
    "id": 1,
    "categoryName": "科技",
    "categoryAlias": "tech",
    "createTime": "2026-01-13 15:42:32",
    "updateTime": "2026-01-13 15:42:32"
  }
}
```

**业务逻辑说明：**
1. 从JWT令牌中解析用户ID
2. 根据分类ID和用户ID查询分类信息
3. 返回分类详细信息（不包含createUser字段）

---

### 2.4 获取分类下的文章

**接口描述：** 获取指定分类下的所有文章列表。

**请求信息：**
- **路径：** `/category/{id}/articles`
- **方法：** GET
- **权限要求：** 需要认证
- **请求头：** Authorization: Bearer {token}

**路径参数：**

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| id | Integer | 是 | 分类ID | 1 |

**成功响应：**

```json
{
  "code": 0,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "title": "人工智能在医疗领域的应用前景",
      "content": "随着人工智能技术的快速发展...",
      "coverImg": "https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png",
      "state": 0,
      "categoryId": 1,
      "createTime": "2026-01-13 15:43:13",
      "updateTime": "2026-01-13 15:43:13"
    }
  ]
}
```

**业务逻辑说明：**
1. 从JWT令牌中解析用户ID
2. 根据分类ID和用户ID查询该分类下的所有文章
3. 返回文章列表（不包含createUser字段）

---

### 2.5 更新分类

**接口描述：** 更新分类信息。

**请求信息：**
- **路径：** `/category/{id}`
- **方法：** PUT
- **权限要求：** 需要认证
- **Content-Type：** application/json
- **请求头：** Authorization: Bearer {token}

**路径参数：**

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| id | Integer | 是 | 分类ID | 1 |

**请求参数：**

| 字段名 | 类型 | 必填 | 约束条件 | 说明 | 示例 |
|--------|------|------|----------|------|------|
| categoryName | String | 是 | 非空 | 分类名称 | "科技" |
| categoryAlias | String | 是 | 非空 | 分类别名 | "tech" |

**请求示例：**

```json
{
  "categoryName": "科技",
  "categoryAlias": "tech"
}
```

**成功响应：**

```json
{
  "code": 0,
  "message": "操作成功",
  "data": null
}
```

**业务逻辑说明：**
1. 从JWT令牌中解析用户ID
2. 校验分类ID、分类名称和分类别名格式
3. 更新分类信息（只能更新当前用户创建的分类）
4. 自动更新update_time字段

---

### 2.6 删除分类

**接口描述：** 删除指定的分类。如果分类下存在文章，则不允许删除。

**请求信息：**
- **路径：** `/category/{id}`
- **方法：** DELETE
- **权限要求：** 需要认证
- **请求头：** Authorization: Bearer {token}

**路径参数：**

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| id | Integer | 是 | 分类ID | 1 |

**成功响应：**

```json
{
  "code": 0,
  "message": "操作成功",
  "data": null
}
```

**失败响应：**

```json
{
  "code": 2001,
  "message": "分类不存在",
  "data": null
}
```

```json
{
  "code": 2002,
  "message": "分类下存在文章，无法删除",
  "data": null
}
```

**业务逻辑说明：**
1. 从JWT令牌中解析用户ID
2. 检查分类是否存在且属于当前用户
3. 检查分类下是否存在文章
4. 如果分类下存在文章，返回错误
5. 如果分类下没有文章，删除分类
6. 使用事务保证数据一致性

---

### 2.7 强制删除分类及其文章

**接口描述：** 删除指定的分类及其下的所有文章。

**请求信息：**
- **路径：** `/category/{id}/force`
- **方法：** DELETE
- **权限要求：** 需要认证
- **请求头：** Authorization: Bearer {token}

**路径参数：**

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| id | Integer | 是 | 分类ID | 1 |

**成功响应：**

```json
{
  "code": 0,
  "message": "操作成功",
  "data": null
}
```

**失败响应：**

```json
{
  "code": 2001,
  "message": "分类不存在",
  "data": null
}
```

**业务逻辑说明：**
1. 从JWT令牌中解析用户ID
2. 检查分类是否存在且属于当前用户
3. 先删除该分类下的所有文章
4. 再删除分类本身
5. 使用事务保证数据一致性

---

## 三、文章管理接口

### 3.1 新增文章

**接口描述：** 创建新文章。

**请求信息：**
- **路径：** `/article`
- **方法：** POST
- **权限要求：** 需要认证
- **Content-Type：** application/json
- **请求头：** Authorization: Bearer {token}

**请求参数：**

| 字段名 | 类型 | 必填 | 约束条件 | 说明 | 示例 |
|--------|------|------|----------|------|------|
| title | String | 是 | 1-100位非空字符 | 文章标题 | "人工智能在医疗领域的应用前景" |
| content | String | 是 | 非空 | 文章内容，支持富文本 | "随着人工智能技术的快速发展..." |
| coverImg | String | 否 | URL格式 | 封面图片URL | "https://example.com/cover.jpg" |
| state | Integer | 否 | 0或1 | 文章状态：0-草稿，1-已发布 | 1 |
| categoryId | Integer | 是 | 非空 | 分类ID | 1 |

**请求示例：**

```json
{
  "title": "人工智能在医疗领域的应用前景",
  "content": "随着人工智能技术的快速发展，其在医疗领域的应用越来越广泛...",
  "coverImg": "https://example.com/cover.jpg",
  "state": 1,
  "categoryId": 1
}
```

**成功响应：**

```json
{
  "code": 0,
  "message": "操作成功",
  "data": null
}
```

**业务逻辑说明：**
1. 从JWT令牌中解析用户ID
2. 校验文章标题、内容、封面图片URL格式
3. 校验state字段（只能是0或1）
4. 校验categoryId是否存在
5. 创建文章，关联当前用户ID
6. 自动设置create_time和update_time

---

### 3.2 文章列表（分页）

**接口描述：** 分页查询当前用户的文章列表，支持按分类和状态筛选。

**请求信息：**
- **路径：** `/article`
- **方法：** GET
- **权限要求：** 需要认证
- **请求头：** Authorization: Bearer {token}

**查询参数：**

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| pageNum | Integer | 是 | 页码，从1开始 | 1 |
| pageSize | Integer | 是 | 每页数量 | 10 |
| categoryId | Integer | 否 | 分类ID（筛选条件） | 1 |
| state | Integer | 否 | 文章状态（筛选条件）：0-草稿，1-已发布 | 1 |

**请求示例：**

```
GET /article?pageNum=1&pageSize=10&categoryId=1&state=1
```

**成功响应：**

```json
{
  "code": 0,
  "message": "操作成功",
  "data": {
    "total": 30,
    "items": [
      {
        "id": 1,
        "title": "人工智能在医疗领域的应用前景",
        "content": "随着人工智能技术的快速发展...",
        "coverImg": "https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png",
        "state": 1,
        "categoryId": 1,
        "createTime": "2026-01-13 15:43:13",
        "updateTime": "2026-01-13 15:43:13"
      }
    ]
  }
}
```

**字段说明：**

| 字段名 | 类型 | 说明 |
|--------|------|------|
| total | Long | 总记录数 |
| items | Array | 当前页的文章列表 |

**业务逻辑说明：**
1. 从JWT令牌中解析用户ID
2. 使用PageHelper插件实现分页查询
3. 支持按categoryId和state进行筛选
4. 返回分页结果（包含总记录数和当前页数据）

---

### 3.3 文章详情

**接口描述：** 根据文章ID获取文章详细信息。

**请求信息：**
- **路径：** `/article/{id}`
- **方法：** GET
- **权限要求：** 需要认证
- **请求头：** Authorization: Bearer {token}

**路径参数：**

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| id | Integer | 是 | 文章ID | 1 |

**成功响应：**

```json
{
  "code": 0,
  "message": "操作成功",
  "data": {
    "id": 1,
    "title": "人工智能在医疗领域的应用前景",
    "content": "随着人工智能技术的快速发展，其在医疗领域的应用越来越广泛。从医学影像诊断到药物研发，AI正在改变传统医疗模式。本文将探讨AI在医疗中的具体应用场景和未来发展趋势...",
    "coverImg": "https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png",
    "state": 0,
    "categoryId": 1,
    "createTime": "2026-01-13 15:43:13",
    "updateTime": "2026-01-13 15:43:13"
  }
}
```

**失败响应：**

```json
{
  "code": 2003,
  "message": "文章不存在",
  "data": null
}
```

**业务逻辑说明：**
1. 从JWT令牌中解析用户ID
2. 根据文章ID和用户ID查询文章信息
3. 如果文章不存在，返回错误
4. 返回文章详细信息（不包含createUser字段）

---

### 3.4 更新文章

**接口描述：** 更新文章信息。

**请求信息：**
- **路径：** `/article/{id}`
- **方法：** PUT
- **权限要求：** 需要认证
- **Content-Type：** application/json
- **请求头：** Authorization: Bearer {token}

**路径参数：**

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| id | Integer | 是 | 文章ID | 1 |

**请求参数：**

| 字段名 | 类型 | 必填 | 约束条件 | 说明 | 示例 |
|--------|------|------|----------|------|------|
| title | String | 是 | 1-100位非空字符 | 文章标题 | "人工智能在医疗领域的应用前景" |
| content | String | 是 | 非空 | 文章内容 | "随着人工智能技术的快速发展..." |
| coverImg | String | 否 | URL格式 | 封面图片URL | "https://example.com/cover.jpg" |
| state | Integer | 否 | 0或1 | 文章状态：0-草稿，1-已发布 | 1 |
| categoryId | Integer | 是 | 非空 | 分类ID | 1 |

**请求示例：**

```json
{
  "title": "人工智能在医疗领域的应用前景",
  "content": "随着人工智能技术的快速发展，其在医疗领域的应用越来越广泛...",
  "coverImg": "https://example.com/cover.jpg",
  "state": 1,
  "categoryId": 1
}
```

**成功响应：**

```json
{
  "code": 0,
  "message": "操作成功",
  "data": null
}
```

**失败响应：**

```json
{
  "code": 2003,
  "message": "文章不存在",
  "data": null
}
```

**业务逻辑说明：**
1. 从JWT令牌中解析用户ID
2. 检查文章是否存在且属于当前用户
3. 校验文章标题、内容、封面图片URL格式
4. 校验state字段（只能是0或1）
5. 更新文章信息
6. 使用事务保证数据一致性

---

### 3.5 删除文章

**接口描述：** 删除指定的文章。

**请求信息：**
- **路径：** `/article/{id}`
- **方法：** DELETE
- **权限要求：** 需要认证
- **请求头：** Authorization: Bearer {token}

**路径参数：**

| 参数名 | 类型 | 必填 | 说明 | 示例 |
|--------|------|------|------|------|
| id | Integer | 是 | 文章ID | 1 |

**成功响应：**

```json
{
  "code": 0,
  "message": "操作成功",
  "data": null
}
```

**失败响应：**

```json
{
  "code": 2003,
  "message": "文章不存在",
  "data": null
}
```

**业务逻辑说明：**
1. 从JWT令牌中解析用户ID
2. 检查文章是否存在且属于当前用户
3. 如果文章不存在，返回错误
4. 删除文章
5. 使用事务保证数据一致性

---

## 四、文件上传接口

### 4.1 文件上传

**接口描述：** 上传图片文件到阿里云OSS，返回文件访问URL。

**请求信息：**
- **路径：** `/upload`
- **方法：** POST
- **权限要求：** 无需认证
- **Content-Type：** multipart/form-data

**请求参数：**

| 字段名 | 类型 | 必填 | 约束条件 | 说明 |
|--------|------|------|----------|------|
| file | File | 是 | 图片文件 | 支持的格式：jpg, jpeg, png, gif, bmp, webp，最大10MB |

**请求示例：**

```
POST /upload
Content-Type: multipart/form-data

file: [binary file data]
```

**成功响应：**

```json
{
  "code": 0,
  "message": "操作成功",
  "data": "https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/abc123-def456.jpg"
}
```

**失败响应：**

```json
{
  "code": 3001,
  "message": "文件上传失败",
  "data": null
}
```

```json
{
  "code": 3002,
  "message": "文件格式不支持",
  "data": null
}
```

```json
{
  "code": 3001,
  "message": "文件大小不能超过10MB",
  "data": null
}
```

**业务逻辑说明：**
1. 检查文件是否为空
2. 检查文件大小是否超过10MB
3. 检查文件格式是否支持（jpg, jpeg, png, gif, bmp, webp）
4. 生成唯一的文件名（使用UUID）
5. 上传文件到阿里云OSS
6. 返回文件访问URL

**支持的文件格式：**
- .jpg
- .jpeg
- .png
- .gif
- .bmp
- .webp

**文件大小限制：**
- 最大10MB

---

## 五、异常处理机制

### 5.1 全局异常处理

系统使用 `GlobalExceptionHandler` 统一处理所有异常，确保返回统一的响应格式。

### 5.2 业务异常

使用 `BusinessException` 抛出业务异常，配合 `ErrorCode` 枚举定义标准错误码。

### 5.3 参数校验

使用 Jakarta Validation 注解进行参数校验，包括：
- `@NotNull`: 字段不能为null
- `@NotEmpty`: 字符串不能为空
- `@Pattern`: 正则表达式校验
- `@Email`: 邮箱格式校验
- `@URL`: URL格式校验
- `@State`: 自定义状态校验（0或1）

### 5.4 日志记录

所有接口操作都会记录日志，包括：
- 成功操作：INFO级别
- 失败操作：WARN级别
- 异常情况：ERROR级别

---

## 六、认证与授权

### 6.1 JWT认证机制

1. 用户登录成功后，服务器生成JWT令牌
2. 令牌包含用户ID和用户名信息
3. 令牌有效期：6小时
4. 令牌存储在Redis中，用于会话管理

### 6.2 令牌使用

需要认证的接口，需在请求头中携带JWT令牌：

```
Authorization: Bearer {token}
```

### 6.3 令牌失效

以下情况会导致令牌失效：
1. 令牌过期（6小时后）
2. 用户修改密码（会删除Redis中的旧令牌）
3. 令牌被手动删除

令牌失效后，用户需要重新登录获取新令牌。

### 6.4 拦截器机制

使用 `LoginInterceptor` 拦截需要认证的接口，验证JWT令牌的有效性。

---

## 七、数据模型说明

### 7.1 用户表 (tb_user)

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

### 7.2 分类表 (tb_category)

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| id | BIGINT | 分类ID | 主键，自增 |
| category_name | VARCHAR(50) | 分类名称 | 非空 |
| category_alias | VARCHAR(50) | 分类别名 | 非空，同一用户下唯一 |
| create_user_id | BIGINT | 创建人ID | 外键，非空 |
| create_time | DATETIME | 创建时间 | 非空 |
| update_time | DATETIME | 更新时间 | 非空 |

### 7.3 文章表 (tb_article)

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

---

## 八、接口测试建议

### 8.1 测试工具

推荐使用以下工具进行API测试：
- Postman
- Apifox
- Insomnia
- curl命令行工具

### 8.2 测试流程

1. **用户注册测试**
   - 测试正常注册
   - 测试用户名已存在的情况
   - 测试参数格式校验

2. **用户登录测试**
   - 测试正常登录
   - 测试用户名或密码错误
   - 测试获取到的令牌

3. **认证接口测试**
   - 使用获取到的令牌访问需要认证的接口
   - 测试令牌过期情况
   - 测试令牌无效情况

4. **业务接口测试**
   - 测试增删改查功能
   - 测试参数校验
   - 测试权限控制
   - 测试异常情况

5. **文件上传测试**
   - 测试支持的图片格式
   - 测试文件大小限制
   - 测试不支持的格式

---

## 九、常见问题

### 9.1 如何获取JWT令牌？

调用 `/user/login` 接口，使用正确的用户名和密码登录，成功后会在响应的data字段中返回JWT令牌。

### 9.2 JWT令牌过期怎么办？

JWT令牌有效期为6小时，过期后需要重新调用 `/user/login` 接口获取新令牌。

### 9.3 如何处理文件上传？

调用 `/upload` 接口，使用 multipart/form-data 格式上传图片文件，成功后会返回文件的访问URL。

### 9.4 如何删除分类？

调用 `/category/{id}` 接口删除分类。如果分类下存在文章，会返回错误。如果需要强制删除分类及其文章，请调用 `/category/{id}/force` 接口。

### 9.5 如何分页查询文章？

调用 `/article` 接口，传入pageNum（页码）和pageSize（每页数量）参数，可选传入categoryId和state进行筛选。

---

## 十、更新日志

### V1.0 (2026-01-13)
- 初始版本发布
- 完成用户管理、分类管理、文章管理和文件上传接口
- 实现JWT认证机制
- 完善异常处理和参数校验

---

## 十一、联系方式

如有问题或建议，请联系项目维护者。
