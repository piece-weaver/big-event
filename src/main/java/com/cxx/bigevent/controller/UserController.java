package com.cxx.bigevent.controller;

import com.cxx.bigevent.dto.LoginRequest;
import com.cxx.bigevent.dto.PasswordUpdateRequest;
import com.cxx.bigevent.dto.RegisterRequest;
import com.cxx.bigevent.exception.ErrorCode;
import com.cxx.bigevent.pojo.Result;
import com.cxx.bigevent.pojo.User;
import com.cxx.bigevent.service.UserService;
import com.cxx.bigevent.security.JwtUtil;
import com.cxx.bigevent.util.Md5Util;
import com.cxx.bigevent.util.ThreadLocalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/register")
    public Result<String> register(@RequestBody @Validated RegisterRequest request) {
        User user = userService.findByUserName(request.getUsername());
        if (user == null) {
            userService.register(request.getUsername(), request.getPassword());
            log.info("用户注册成功：{}", request.getUsername());
            return Result.success();
        } else {
            log.warn("用户注册失败：用户名已存在，{}", request.getUsername());
            return Result.error(ErrorCode.USER_USERNAME_EXISTS);
        }
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody @Validated LoginRequest request) {
        User loginUser = userService.findByUserName(request.getUsername());
        if (loginUser != null) {
            if (Md5Util.getMD5String(request.getPassword()).equals(loginUser.getPassword())) {
                Map<String, Object> claims = new HashMap<>();
                claims.put("id", loginUser.getId());
                claims.put("username", loginUser.getUsername());
                String token = JwtUtil.genToken(claims);

                ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
                operations.set(token, token, 6, TimeUnit.HOURS);

                log.info("用户登录成功：{}", request.getUsername());
                return Result.success(token);
            } else {
                log.warn("用户登录失败：密码错误，{}", request.getUsername());
                return Result.error(ErrorCode.USER_LOGIN_ERROR);
            }
        } else {
            log.warn("用户登录失败：用户不存在，{}", request.getUsername());
            return Result.error(ErrorCode.USER_LOGIN_ERROR);
        }
    }

    @GetMapping("/userInfo")
    public Result<User> userInfo() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user) {
        userService.update(user);
        log.info("用户信息更新成功：{}", user.getUsername());
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @org.hibernate.validator.constraints.URL(message = "头像URL格式不正确") String avatarUrl) {
        userService.updateAvatar(avatarUrl);
        log.info("用户头像更新成功：{}", avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody @Validated PasswordUpdateRequest request, @RequestHeader("Authorization") String token) {
        if (!StringUtils.hasLength(request.getOldPwd()) || !StringUtils.hasLength(request.getNewPwd()) || !StringUtils.hasLength(request.getRePwd())) {
            return Result.error(ErrorCode.PARAM_NOT_EMPTY);
        }

        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User loginUser = userService.findByUserName(username);
        if (!loginUser.getPassword().equals(Md5Util.getMD5String(request.getOldPwd()))) {
            log.warn("用户密码更新失败：原密码错误，{}", username);
            return Result.error(ErrorCode.USER_PASSWORD_ERROR);
        }

        if (loginUser.getPassword().equals(Md5Util.getMD5String(request.getNewPwd()))) {
            log.warn("用户密码更新失败：新密码与原密码一致，{}", username);
            return Result.error(ErrorCode.USER_PASSWORD_SAME);
        }

        if (!request.getNewPwd().equals(request.getRePwd())) {
            log.warn("用户密码更新失败：新密码与确认密码不一致，{}", username);
            return Result.error(ErrorCode.USER_PASSWORD_NOT_MATCH);
        }

        userService.updatePwd(request.getNewPwd());

        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);

        log.info("用户密码更新成功：{}", username);
        return Result.success();
    }
}
