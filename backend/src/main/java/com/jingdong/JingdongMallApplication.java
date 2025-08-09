package com.jingdong;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 仿京东商城主启动类
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@SpringBootApplication
@MapperScan("com.jingdong.mapper")  // 扫描Mapper接口
public class JingdongMallApplication {

    /**
     * 主方法，启动Spring Boot应用
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(JingdongMallApplication.class, args);
        System.out.println("=================================");
        System.out.println("仿京东商城后端服务启动成功！");
        System.out.println("访问地址：http://localhost:8080");
        System.out.println("=================================");
    }
}
