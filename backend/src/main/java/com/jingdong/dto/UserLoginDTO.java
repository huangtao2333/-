package com.jingdong.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录DTO
 * 用于接收用户登录请求参数
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Data
public class UserLoginDTO {

    /**
     * 用户名（可以是用户名、邮箱或手机号）
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 记住我（可选）
     */
    private Boolean rememberMe = false;
}
