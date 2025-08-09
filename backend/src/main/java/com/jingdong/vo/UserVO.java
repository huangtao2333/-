package com.jingdong.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息VO
 * 用于返回给前端的用户信息（不包含敏感信息）
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Data
public class UserVO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号（脱敏处理）
     */
    private String phone;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别（0-未知，1-男，2-女）
     */
    private Integer gender;

    /**
     * 性别描述
     */
    private String genderDesc;

    /**
     * 生日
     */
    private LocalDateTime birthday;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 用户状态（0-禁用，1-正常）
     */
    private Integer status;

    /**
     * 用户类型（0-普通用户，1-VIP用户，2-管理员）
     */
    private Integer userType;

    /**
     * 用户类型描述
     */
    private String userTypeDesc;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 设置性别描述
     */
    public void setGender(Integer gender) {
        this.gender = gender;
        if (gender != null) {
            switch (gender) {
                case 1:
                    this.genderDesc = "男";
                    break;
                case 2:
                    this.genderDesc = "女";
                    break;
                default:
                    this.genderDesc = "未知";
                    break;
            }
        }
    }

    /**
     * 设置用户类型描述
     */
    public void setUserType(Integer userType) {
        this.userType = userType;
        if (userType != null) {
            switch (userType) {
                case 1:
                    this.userTypeDesc = "VIP用户";
                    break;
                case 2:
                    this.userTypeDesc = "管理员";
                    break;
                default:
                    this.userTypeDesc = "普通用户";
                    break;
            }
        }
    }

    /**
     * 设置脱敏手机号
     */
    public void setPhone(String phone) {
        if (phone != null && phone.length() == 11) {
            this.phone = phone.substring(0, 3) + "****" + phone.substring(7);
        } else {
            this.phone = phone;
        }
    }
}
