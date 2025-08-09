package com.jingdong.service.impl;

import com.jingdong.dto.UserLoginDTO;
import com.jingdong.dto.UserRegisterDTO;
import com.jingdong.dto.UserUpdateDTO;
import com.jingdong.entity.User;
import com.jingdong.exception.BusinessException;
import com.jingdong.mapper.UserMapper;
import com.jingdong.service.UserService;
import com.jingdong.utils.JwtUtils;
import com.jingdong.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 用户服务实现类
 * 实现用户相关的业务逻辑
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtils jwtUtils;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 用户注册
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String register(UserRegisterDTO registerDTO) {
        log.info("用户注册，用户名：{}", registerDTO.getUsername());

        // 验证密码一致性
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new BusinessException("两次输入的密码不一致");
        }

        // 检查用户名是否已存在
        if (checkUsernameExists(registerDTO.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (checkEmailExists(registerDTO.getEmail())) {
            throw new BusinessException("邮箱已被注册");
        }

        // 检查手机号是否已存在
        if (checkPhoneExists(registerDTO.getPhone())) {
            throw new BusinessException("手机号已被注册");
        }

        // 创建用户对象
        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        
        // 加密密码
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        
        // 设置默认值
        user.setStatus(1);  // 正常状态
        user.setUserType(0);  // 普通用户
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        // 保存用户
        int result = userMapper.insert(user);
        if (result > 0) {
            log.info("用户注册成功，用户ID：{}", user.getUserId());
            return "注册成功";
        } else {
            throw new BusinessException("注册失败，请稍后重试");
        }
    }

    /**
     * 用户登录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String login(UserLoginDTO loginDTO, String clientIp) {
        log.info("用户登录，用户名：{}", loginDTO.getUsername());

        // 查找用户（支持用户名、邮箱、手机号登录）
        User user = findUserByLoginName(loginDTO.getUsername());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException("账户已被禁用，请联系管理员");
        }

        // 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // 更新最后登录信息
        String loginTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        userMapper.updateLastLoginInfo(user.getUserId(), loginTime, clientIp);

        // 生成JWT令牌
        String token = jwtUtils.generateToken(user.getUserId(), user.getUsername());
        
        log.info("用户登录成功，用户ID：{}", user.getUserId());
        return token;
    }

    /**
     * 获取当前用户信息
     */
    @Override
    public UserVO getCurrentUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        // 设置性别和用户类型描述
        userVO.setGender(user.getGender());
        userVO.setUserType(user.getUserType());
        userVO.setPhone(user.getPhone());  // 自动脱敏

        return userVO;
    }

    /**
     * 更新用户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateUser(Long userId, UserUpdateDTO updateDTO) {
        log.info("更新用户信息，用户ID：{}", userId);

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 检查邮箱是否被其他用户使用
        if (StringUtils.hasText(updateDTO.getEmail()) && !updateDTO.getEmail().equals(user.getEmail())) {
            User existUser = userMapper.findByEmail(updateDTO.getEmail());
            if (existUser != null && !existUser.getUserId().equals(userId)) {
                throw new BusinessException("邮箱已被其他用户使用");
            }
        }

        // 检查手机号是否被其他用户使用
        if (StringUtils.hasText(updateDTO.getPhone()) && !updateDTO.getPhone().equals(user.getPhone())) {
            User existUser = userMapper.findByPhone(updateDTO.getPhone());
            if (existUser != null && !existUser.getUserId().equals(userId)) {
                throw new BusinessException("手机号已被其他用户使用");
            }
        }

        // 更新用户信息
        BeanUtils.copyProperties(updateDTO, user);
        user.setUpdateTime(LocalDateTime.now());

        int result = userMapper.updateById(user);
        if (result > 0) {
            log.info("用户信息更新成功，用户ID：{}", userId);
            return "更新成功";
        } else {
            throw new BusinessException("更新失败，请稍后重试");
        }
    }

    /**
     * 修改密码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String changePassword(Long userId, String oldPassword, String newPassword) {
        log.info("修改密码，用户ID：{}", userId);

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(LocalDateTime.now());

        int result = userMapper.updateById(user);
        if (result > 0) {
            log.info("密码修改成功，用户ID：{}", userId);
            return "密码修改成功";
        } else {
            throw new BusinessException("密码修改失败，请稍后重试");
        }
    }

    /**
     * 检查用户名是否存在
     */
    @Override
    public boolean checkUsernameExists(String username) {
        return userMapper.checkUsernameExists(username) > 0;
    }

    /**
     * 检查邮箱是否存在
     */
    @Override
    public boolean checkEmailExists(String email) {
        return userMapper.checkEmailExists(email) > 0;
    }

    /**
     * 检查手机号是否存在
     */
    @Override
    public boolean checkPhoneExists(String phone) {
        return userMapper.checkPhoneExists(phone) > 0;
    }

    /**
     * 根据登录名查找用户（支持用户名、邮箱、手机号）
     */
    private User findUserByLoginName(String loginName) {
        // 先尝试用户名
        User user = userMapper.findByUsername(loginName);
        if (user != null) {
            return user;
        }

        // 再尝试邮箱
        if (loginName.contains("@")) {
            user = userMapper.findByEmail(loginName);
            if (user != null) {
                return user;
            }
        }

        // 最后尝试手机号
        if (loginName.matches("^1[3-9]\\d{9}$")) {
            user = userMapper.findByPhone(loginName);
        }

        return user;
    }
}
