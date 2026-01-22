package com.cxx.bigevent.exception;

public enum ErrorCode {
    SUCCESS(0, "操作成功"),
    ERROR(1, "操作失败"),
    
    USER_USERNAME_EXISTS(1001, "用户名已存在"),
    USER_LOGIN_ERROR(1002, "用户名或密码错误"),
    USER_PASSWORD_ERROR(1006, "原密码错误"),
    USER_PASSWORD_SAME(1007, "新密码与原密码一致"),
    USER_PASSWORD_NOT_MATCH(1008, "新密码与确认密码不一致"),
    
    CATEGORY_NOT_FOUND(2001, "分类不存在"),
    CATEGORY_HAS_ARTICLES(2002, "分类下存在文章，无法删除"),
    
    ARTICLE_NOT_FOUND(2003, "文章不存在"),
    
    COMMENT_NOT_FOUND(2004, "评论不存在"),

    FILE_UPLOAD_FAILED(3001, "文件上传失败"),
    FILE_FORMAT_NOT_SUPPORTED(3002, "文件格式不支持"),
    
    PARAM_NOT_EMPTY(4002, "必要参数不能为空"),
    PARAM_FORMAT_ERROR(4003, "参数格式错误"),

    WEBHOOK_NOT_FOUND(5001, "Webhook不存在"),
    WEBHOOK_EXECUTION_FAILED(5002, "Webhook执行失败"),
    
    SCHEDULED_TASK_NOT_FOUND(6001, "定时任务不存在"),
    SCHEDULED_TASK_EXECUTION_FAILED(6002, "定时任务执行失败"),

    FORBIDDEN(7001, "权限不足"),
    ROLE_NOT_FOUND(7002, "角色不存在"),
    PERMISSION_NOT_FOUND(7003, "权限不存在"),
    USER_ROLE_ASSIGN_FAILED(7004, "用户角色分配失败"),
    UNAUTHORIZED(7005, "请先登录");
    
    private final Integer code;
    private final String message;
    
    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
}
