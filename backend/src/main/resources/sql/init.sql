-- 仿京东商城数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS jingdong_mall CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE jingdong_mall;

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    email VARCHAR(100) UNIQUE COMMENT '邮箱',
    phone VARCHAR(20) UNIQUE COMMENT '手机号',
    real_name VARCHAR(50) COMMENT '真实姓名',
    gender TINYINT DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    birthday DATETIME COMMENT '生日',
    avatar VARCHAR(200) COMMENT '头像URL',
    status TINYINT DEFAULT 1 COMMENT '用户状态：0-禁用，1-正常',
    user_type TINYINT DEFAULT 0 COMMENT '用户类型：0-普通用户，1-VIP用户，2-管理员',
    last_login_time DATETIME COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 商品分类表
CREATE TABLE IF NOT EXISTS category (
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    category_name VARCHAR(100) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID，0表示顶级分类',
    level TINYINT DEFAULT 1 COMMENT '分类层级：1-一级，2-二级，3-三级',
    icon VARCHAR(200) COMMENT '分类图标',
    image VARCHAR(200) COMMENT '分类图片',
    description VARCHAR(500) COMMENT '分类描述',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '分类状态：0-禁用，1-启用',
    show_in_nav TINYINT DEFAULT 0 COMMENT '是否显示在导航栏：0-不显示，1-显示',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 商品表
CREATE TABLE IF NOT EXISTS product (
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '商品ID',
    product_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    subtitle VARCHAR(300) COMMENT '商品副标题',
    category_id BIGINT NOT NULL COMMENT '商品分类ID',
    brand VARCHAR(100) COMMENT '商品品牌',
    model VARCHAR(100) COMMENT '商品型号',
    price DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    original_price DECIMAL(10,2) COMMENT '商品原价',
    stock INT DEFAULT 0 COMMENT '商品库存',
    main_image VARCHAR(500) COMMENT '商品主图',
    detail_images VARCHAR(2000) COMMENT '商品详情图片',
    description TEXT COMMENT '商品详情描述',
    specifications TEXT COMMENT '商品规格参数',
    weight INT DEFAULT 0 COMMENT '商品重量（克）',
    status TINYINT DEFAULT 1 COMMENT '商品状态：0-下架，1-上架',
    is_recommend TINYINT DEFAULT 0 COMMENT '是否推荐：0-不推荐，1-推荐',
    is_hot TINYINT DEFAULT 0 COMMENT '是否热销：0-不是，1-是',
    is_new TINYINT DEFAULT 0 COMMENT '是否新品：0-不是，1-是',
    sales_count INT DEFAULT 0 COMMENT '销量',
    view_count INT DEFAULT 0 COMMENT '浏览量',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    INDEX idx_category_id (category_id),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 购物车表
CREATE TABLE IF NOT EXISTS cart (
    cart_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '购物车ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT DEFAULT 1 COMMENT '商品数量',
    selected TINYINT DEFAULT 1 COMMENT '是否选中：0-未选中，1-选中',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    UNIQUE KEY uk_user_product (user_id, product_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    pay_amount DECIMAL(10,2) NOT NULL COMMENT '实付金额',
    shipping_fee DECIMAL(10,2) DEFAULT 0.00 COMMENT '运费',
    discount_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '优惠金额',
    status TINYINT DEFAULT 0 COMMENT '订单状态：0-待付款，1-待发货，2-待收货，3-待评价，4-已完成，5-已取消，6-已退款',
    payment_method TINYINT DEFAULT 1 COMMENT '支付方式：1-支付宝，2-微信，3-银行卡',
    payment_status TINYINT DEFAULT 0 COMMENT '支付状态：0-未支付，1-已支付，2-支付失败',
    payment_time DATETIME COMMENT '支付时间',
    receiver_name VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    receiver_phone VARCHAR(20) NOT NULL COMMENT '收货人手机号',
    receiver_address VARCHAR(500) NOT NULL COMMENT '收货地址',
    remark VARCHAR(500) COMMENT '订单备注',
    ship_time DATETIME COMMENT '发货时间',
    confirm_time DATETIME COMMENT '确认收货时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    INDEX idx_user_id (user_id),
    INDEX idx_order_no (order_no),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单详情表
CREATE TABLE IF NOT EXISTS order_item (
    order_item_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单详情ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    product_image VARCHAR(500) COMMENT '商品图片',
    product_price DECIMAL(10,2) NOT NULL COMMENT '商品单价',
    quantity INT DEFAULT 1 COMMENT '购买数量',
    total_price DECIMAL(10,2) NOT NULL COMMENT '小计金额',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单详情表';

-- 插入初始数据
-- 插入管理员用户
INSERT INTO user (username, password, email, phone, real_name, user_type, create_time, update_time) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEIUi', 'admin@jingdong.com', '13800138000', '管理员', 2, NOW(), NOW());

-- 插入商品分类
INSERT INTO category (category_name, parent_id, level, sort_order, create_time, update_time) VALUES
('手机数码', 0, 1, 1, NOW(), NOW()),
('电脑办公', 0, 1, 2, NOW(), NOW()),
('家用电器', 0, 1, 3, NOW(), NOW()),
('服饰内衣', 0, 1, 4, NOW(), NOW()),
('图书音像', 0, 1, 5, NOW(), NOW());

-- 插入二级分类
INSERT INTO category (category_name, parent_id, level, sort_order, create_time, update_time) VALUES
('手机通讯', 1, 2, 1, NOW(), NOW()),
('数码配件', 1, 2, 2, NOW(), NOW()),
('笔记本', 2, 2, 1, NOW(), NOW()),
('台式机', 2, 2, 2, NOW(), NOW()),
('大家电', 3, 2, 1, NOW(), NOW()),
('小家电', 3, 2, 2, NOW(), NOW());

-- 插入示例商品
INSERT INTO product (product_name, subtitle, category_id, brand, price, original_price, stock, main_image, status, is_recommend, create_time, update_time) VALUES
('iPhone 15 Pro', '全新A17 Pro芯片，钛金属设计', 6, 'Apple', 7999.00, 8999.00, 100, '/images/iphone15pro.jpg', 1, 1, NOW(), NOW()),
('华为Mate 60 Pro', '卫星通话，麒麟9000S芯片', 6, '华为', 6999.00, 7999.00, 50, '/images/mate60pro.jpg', 1, 1, NOW(), NOW()),
('MacBook Pro 14', 'M3芯片，14英寸Liquid视网膜显示屏', 8, 'Apple', 14999.00, 16999.00, 30, '/images/macbookpro14.jpg', 1, 1, NOW(), NOW());
