package com.jingdong.service;

import com.jingdong.dto.UserLoginDTO;
import com.jingdong.dto.UserRegisterDTO;
import com.jingdong.dto.UserUpdateDTO;
import com.jingdong.vo.UserVO;

/**
 * 用户服务接口
 * 定义用户相关的业务操作
 * 
 * @author huangtao
 * @date 2025-08-09
 */
public interface UserService {

    /**
     * 用户注册
     * 
     * @param registerDTO 注册信息
     * @return 注册结果
     */
    String register(UserRegisterDTO registerDTO);

    /**
     * 用户登录
     * 
     * @param loginDTO 登录信息
     * @param clientIp 客户端IP
     * @return JWT令牌
     */
    String login(UserLoginDTO loginDTO, String clientIp);

    /**
     * 获取当前用户信息
     * 
     * @param userId 用户ID
     * @return 用户信息
     */
    UserVO getCurrentUser(Long userId);

    /**
     * 更新用户信息
     * 
     * @param userId 用户ID
     * @param updateDTO 更新信息
     * @return 更新结果
     */
    String updateUser(Long userId, UserUpdateDTO updateDTO);

    /**
     * 修改密码
     * 
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    String changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 是否存在
     */
    boolean checkUsernameExists(String username);

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @return 是否存在
     */
    boolean checkEmailExists(String email);

    /**
     * 检查手机号是否存在
     * 
     * @param phone 手机号
     * @return 是否存在
     */
    boolean checkPhoneExists(String phone);
}
