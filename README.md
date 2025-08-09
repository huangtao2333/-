# 仿京东商城项目

## 项目简介
这是一个基于Vue.js + Spring Boot的前后端分离仿京东商城项目，实现了电商平台的核心功能。

## 技术栈

### 前端
- Vue 3
- Vue Router
- Vuex/Pinia
- Element Plus
- Axios

### 后端
- Spring Boot 2.7+
- Spring Security
- MyBatis Plus
- MySQL
- Redis
- JWT

## 项目结构
```
仿京东商城/
├── frontend/          # 前端Vue项目
│   ├── src/
│   ├── public/
│   └── package.json
├── backend/           # 后端Spring Boot项目
│   ├── src/
│   ├── pom.xml
│   └── application.yml
├── docs/              # 项目文档
└── README.md
```

## 功能模块
- [x] 项目初始化
- [ ] 用户管理（注册、登录、个人信息）
- [ ] 商品管理（分类、搜索、详情）
- [ ] 购物车管理
- [ ] 订单管理
- [ ] 支付功能

## 开发规范
- 代码简洁明了，必须有注释
- 变量名直白易懂
- 逻辑清晰通顺
- 完成一部分提交一部分

## 启动说明
### 后端启动
```bash
cd backend
mvn spring-boot:run
```

### 前端启动
```bash
cd frontend
npm install
npm run serve
```

## 作者
项目地址：https://github.com/huangtao2333/-.git
