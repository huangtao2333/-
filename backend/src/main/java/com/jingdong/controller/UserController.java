package com.jingdong.controller;

import com.jingdong.common.Result;
import com.jingdong.dto.UserLoginDTO;
import com.jingdong.dto.UserRegisterDTO;
import com.jingdong.dto.UserUpdateDTO;
import com.jingdong.service.UserService;
import com.jingdong.utils.JwtUtils;
import com.jingdong.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 用户控制器
 * 处理用户相关的HTTP请求
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 用户注册
     * 
     * @param registerDTO 注册信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        log.info("用户注册请求，用户名：{}", registerDTO.getUsername());
        String result = userService.register(registerDTO);
        return Result.success(result);
    }

    /**
     * 用户登录
     * 
     * @param loginDTO 登录信息
     * @param request HTTP请求对象
     * @return 登录结果（包含JWT令牌）
     */
    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody UserLoginDTO loginDTO, HttpServletRequest request) {
        log.info("用户登录请求，用户名：{}", loginDTO.getUsername());
        
        // 获取客户端IP
        String clientIp = getClientIp(request);
        
        String token = userService.login(loginDTO, clientIp);
        return Result.success("登录成功", token);
    }

    /**
     * 获取当前用户信息
     * 
     * @param request HTTP请求对象
     * @return 用户信息
     */
    @GetMapping("/current")
    public Result<UserVO> getCurrentUser(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        UserVO userVO = userService.getCurrentUser(userId);
        return Result.success(userVO);
    }

    /**
     * 更新用户信息
     * 
     * @param updateDTO 更新信息
     * @param request HTTP请求对象
     * @return 更新结果
     */
    @PutMapping("/update")
    public Result<String> updateUser(@Valid @RequestBody UserUpdateDTO updateDTO, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("更新用户信息请求，用户ID：{}", userId);
        
        String result = userService.updateUser(userId, updateDTO);
        return Result.success(result);
    }

    /**
     * 修改密码
     * 
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param request HTTP请求对象
     * @return 修改结果
     */
    @PutMapping("/password")
    public Result<String> changePassword(@NotBlank(message = "旧密码不能为空") @RequestParam String oldPassword,
                                       @NotBlank(message = "新密码不能为空") @RequestParam String newPassword,
                                       HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("修改密码请求，用户ID：{}", userId);
        
        String result = userService.changePassword(userId, oldPassword, newPassword);
        return Result.success(result);
    }

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 检查结果
     */
    @GetMapping("/check/username")
    public Result<Boolean> checkUsername(@NotBlank(message = "用户名不能为空") @RequestParam String username) {
        boolean exists = userService.checkUsernameExists(username);
        return Result.success(exists);
    }

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @return 检查结果
     */
    @GetMapping("/check/email")
    public Result<Boolean> checkEmail(@NotBlank(message = "邮箱不能为空") @RequestParam String email) {
        boolean exists = userService.checkEmailExists(email);
        return Result.success(exists);
    }

    /**
     * 检查手机号是否存在
     * 
     * @param phone 手机号
     * @return 检查结果
     */
    @GetMapping("/check/phone")
    public Result<Boolean> checkPhone(@NotBlank(message = "手机号不能为空") @RequestParam String phone) {
        boolean exists = userService.checkPhoneExists(phone);
        return Result.success(exists);
    }

    /**
     * 获取客户端IP地址
     * 
     * @param request HTTP请求对象
     * @return 客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 从请求中获取当前用户ID
     * 
     * @param request HTTP请求对象
     * @return 用户ID
     */
    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtUtils.getUserIdFromToken(token);
        }
        throw new RuntimeException("未登录或登录已过期");
    }
}
