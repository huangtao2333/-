package com.jingdong.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库中的user表
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Data
@Entity
@Table(name = "user")
@TableName("user")
public class User {

    /**
     * 用户ID（主键）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名（唯一）
     */
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,20}$", message = "用户名只能包含字母、数字、下划线，长度4-20位")
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    /**
     * 密码（加密存储）
     */
    @NotBlank(message = "密码不能为空")
    @Column(nullable = false, length = 100)
    private String password;

    /**
     * 邮箱（唯一）
     */
    @Email(message = "邮箱格式不正确")
    @Column(unique = true, length = 100)
    private String email;

    /**
     * 手机号（唯一）
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Column(unique = true, length = 20)
    private String phone;

    /**
     * 真实姓名
     */
    @Column(length = 50)
    private String realName;

    /**
     * 性别（0-未知，1-男，2-女）
     */
    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private Integer gender;

    /**
     * 生日
     */
    private LocalDateTime birthday;

    /**
     * 头像URL
     */
    @Column(length = 200)
    private String avatar;

    /**
     * 用户状态（0-禁用，1-正常）
     */
    @Column(columnDefinition = "TINYINT DEFAULT 1")
    private Integer status;

    /**
     * 用户类型（0-普通用户，1-VIP用户，2-管理员）
     */
    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private Integer userType;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    @Column(length = 50)
    private String lastLoginIp;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Column(nullable = false)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标志（0-未删除，1-已删除）
     */
    @TableLogic
    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private Integer deleted;
}
