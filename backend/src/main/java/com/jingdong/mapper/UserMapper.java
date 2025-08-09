package com.jingdong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jingdong.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 用户Mapper接口
 * 继承MyBatis Plus的BaseMapper，提供基础的CRUD操作
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    @Select("SELECT * FROM user WHERE username = #{username} AND deleted = 0")
    User findByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户信息
     */
    @Select("SELECT * FROM user WHERE email = #{email} AND deleted = 0")
    User findByEmail(@Param("email") String email);

    /**
     * 根据手机号查询用户
     * 
     * @param phone 手机号
     * @return 用户信息
     */
    @Select("SELECT * FROM user WHERE phone = #{phone} AND deleted = 0")
    User findByPhone(@Param("phone") String phone);

    /**
     * 更新用户最后登录信息
     * 
     * @param userId 用户ID
     * @param loginTime 登录时间
     * @param loginIp 登录IP
     * @return 影响行数
     */
    @Update("UPDATE user SET last_login_time = #{loginTime}, last_login_ip = #{loginIp}, update_time = NOW() WHERE user_id = #{userId}")
    int updateLastLoginInfo(@Param("userId") Long userId, 
                           @Param("loginTime") String loginTime, 
                           @Param("loginIp") String loginIp);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 存在返回1，不存在返回0
     */
    @Select("SELECT COUNT(1) FROM user WHERE username = #{username} AND deleted = 0")
    int checkUsernameExists(@Param("username") String username);

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @return 存在返回1，不存在返回0
     */
    @Select("SELECT COUNT(1) FROM user WHERE email = #{email} AND deleted = 0")
    int checkEmailExists(@Param("email") String email);

    /**
     * 检查手机号是否存在
     * 
     * @param phone 手机号
     * @return 存在返回1，不存在返回0
     */
    @Select("SELECT COUNT(1) FROM user WHERE phone = #{phone} AND deleted = 0")
    int checkPhoneExists(@Param("phone") String phone);
}
