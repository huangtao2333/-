<template>
  <footer class="footer">
    <div class="container">
      <!-- 服务承诺 -->
      <div class="service-promises">
        <div class="promise-item">
          <el-icon class="promise-icon"><van /></el-icon>
          <div class="promise-content">
            <h4>正品保障</h4>
            <p>正品保障，假一赔十</p>
          </div>
        </div>
        <div class="promise-item">
          <el-icon class="promise-icon"><clock /></el-icon>
          <div class="promise-content">
            <h4>极速物流</h4>
            <p>闪电发货，极速配送</p>
          </div>
        </div>
        <div class="promise-item">
          <el-icon class="promise-icon"><headset /></el-icon>
          <div class="promise-content">
            <h4>优质服务</h4>
            <p>7x24小时客服</p>
          </div>
        </div>
        <div class="promise-item">
          <el-icon class="promise-icon"><refresh /></el-icon>
          <div class="promise-content">
            <h4>无忧退换</h4>
            <p>7天无理由退换</p>
          </div>
        </div>
      </div>

      <!-- 分割线 -->
      <div class="divider"></div>

      <!-- 底部信息 -->
      <div class="footer-content">
        <div class="footer-section">
          <h3 class="section-title">购物指南</h3>
          <ul class="section-links">
            <li><a href="#">购物流程</a></li>
            <li><a href="#">会员介绍</a></li>
            <li><a href="#">生活旅行</a></li>
            <li><a href="#">常见问题</a></li>
            <li><a href="#">大家电</a></li>
            <li><a href="#">联系客服</a></li>
          </ul>
        </div>

        <div class="footer-section">
          <h3 class="section-title">配送方式</h3>
          <ul class="section-links">
            <li><a href="#">上门自提</a></li>
            <li><a href="#">211限时达</a></li>
            <li><a href="#">配送服务查询</a></li>
            <li><a href="#">配送费收取标准</a></li>
            <li><a href="#">海外配送</a></li>
          </ul>
        </div>

        <div class="footer-section">
          <h3 class="section-title">支付方式</h3>
          <ul class="section-links">
            <li><a href="#">货到付款</a></li>
            <li><a href="#">在线支付</a></li>
            <li><a href="#">分期付款</a></li>
            <li><a href="#">邮局汇款</a></li>
            <li><a href="#">公司转账</a></li>
          </ul>
        </div>

        <div class="footer-section">
          <h3 class="section-title">售后服务</h3>
          <ul class="section-links">
            <li><a href="#">售后政策</a></li>
            <li><a href="#">价格保护</a></li>
            <li><a href="#">退款说明</a></li>
            <li><a href="#">返修/退换货</a></li>
            <li><a href="#">取消订单</a></li>
          </ul>
        </div>

        <div class="footer-section">
          <h3 class="section-title">特色服务</h3>
          <ul class="section-links">
            <li><a href="#">夺宝岛</a></li>
            <li><a href="#">DIY装机</a></li>
            <li><a href="#">延保服务</a></li>
            <li><a href="#">京东E卡</a></li>
            <li><a href="#">京东通信</a></li>
          </ul>
        </div>

        <div class="footer-section contact-section">
          <h3 class="section-title">联系我们</h3>
          <div class="contact-info">
            <div class="contact-item">
              <el-icon><phone /></el-icon>
              <span>400-606-5500</span>
            </div>
            <div class="contact-item">
              <el-icon><message /></el-icon>
              <span>7x24小时客服热线</span>
            </div>
            <div class="contact-item">
              <el-icon><location /></el-icon>
              <span>全国2000+服务网点</span>
            </div>
          </div>
          
          <!-- 二维码 -->
          <div class="qr-codes">
            <div class="qr-item">
              <div class="qr-image">
                <img src="/images/app-qr.png" alt="APP下载">
              </div>
              <p>手机京东</p>
            </div>
            <div class="qr-item">
              <div class="qr-image">
                <img src="/images/wechat-qr.png" alt="微信关注">
              </div>
              <p>微信关注</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 版权信息 -->
      <div class="copyright">
        <div class="copyright-links">
          <a href="#">关于我们</a>
          <span class="separator">|</span>
          <a href="#">联系我们</a>
          <span class="separator">|</span>
          <a href="#">联系客服</a>
          <span class="separator">|</span>
          <a href="#">合作招商</a>
          <span class="separator">|</span>
          <a href="#">商家帮助</a>
          <span class="separator">|</span>
          <a href="#">营销中心</a>
          <span class="separator">|</span>
          <a href="#">手机京东</a>
          <span class="separator">|</span>
          <a href="#">友情链接</a>
          <span class="separator">|</span>
          <a href="#">销售联盟</a>
          <span class="separator">|</span>
          <a href="#">京东社区</a>
          <span class="separator">|</span>
          <a href="#">风险监测</a>
          <span class="separator">|</span>
          <a href="#">隐私政策</a>
          <span class="separator">|</span>
          <a href="#">京东公益</a>
          <span class="separator">|</span>
          <a href="#">English Site</a>
        </div>
        
        <div class="copyright-text">
          <p>Copyright © 2025 仿京东商城 版权所有</p>
          <p>本项目仅供学习交流使用，不用于商业用途</p>
        </div>
      </div>
    </div>
  </footer>
</template>

<script>
import { Van, Clock, Headset, Refresh, Phone, Message, Location } from '@element-plus/icons-vue'

export default {
  name: 'Footer',
  components: {
    Van,
    Clock,
    Headset,
    Refresh,
    Phone,
    Message,
    Location
  }
}
</script>

<style scoped>
.footer {
  background: #fff;
  border-top: 1px solid var(--jd-border);
  margin-top: 40px;
}

/* 服务承诺 */
.service-promises {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 40px;
  padding: 40px 0;
}

.promise-item {
  display: flex;
  align-items: center;
  gap: 15px;
  text-align: left;
}

.promise-icon {
  font-size: 32px;
  color: var(--jd-primary);
  flex-shrink: 0;
}

.promise-content h4 {
  font-size: 16px;
  font-weight: 500;
  color: var(--jd-text-primary);
  margin-bottom: 4px;
}

.promise-content p {
  font-size: 12px;
  color: var(--jd-text-secondary);
}

/* 分割线 */
.divider {
  height: 1px;
  background: var(--jd-border);
  margin: 0 -15px;
}

/* 底部内容 */
.footer-content {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 40px;
  padding: 40px 0;
}

.footer-section {
  min-height: 200px;
}

.section-title {
  font-size: 16px;
  font-weight: 500;
  color: var(--jd-text-primary);
  margin-bottom: 20px;
}

.section-links {
  list-style: none;
}

.section-links li {
  margin-bottom: 8px;
}

.section-links a {
  font-size: 12px;
  color: var(--jd-text-secondary);
  text-decoration: none;
  transition: color 0.3s;
}

.section-links a:hover {
  color: var(--jd-primary);
}

/* 联系我们区域 */
.contact-section {
  grid-column: span 1;
}

.contact-info {
  margin-bottom: 20px;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  font-size: 12px;
  color: var(--jd-text-secondary);
}

.contact-item .el-icon {
  color: var(--jd-primary);
}

/* 二维码 */
.qr-codes {
  display: flex;
  gap: 20px;
}

.qr-item {
  text-align: center;
}

.qr-image {
  width: 60px;
  height: 60px;
  border: 1px solid var(--jd-border);
  border-radius: 4px;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
}

.qr-image img {
  width: 50px;
  height: 50px;
  object-fit: cover;
}

.qr-item p {
  font-size: 12px;
  color: var(--jd-text-secondary);
}

/* 版权信息 */
.copyright {
  border-top: 1px solid var(--jd-border);
  padding: 20px 0;
  text-align: center;
}

.copyright-links {
  margin-bottom: 15px;
}

.copyright-links a {
  font-size: 12px;
  color: var(--jd-text-secondary);
  text-decoration: none;
  transition: color 0.3s;
}

.copyright-links a:hover {
  color: var(--jd-primary);
}

.separator {
  margin: 0 8px;
  color: var(--jd-text-lighter);
}

.copyright-text {
  font-size: 12px;
  color: var(--jd-text-lighter);
  line-height: 1.6;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .service-promises {
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
  }
  
  .footer-content {
    grid-template-columns: repeat(3, 1fr);
    gap: 20px;
  }
  
  .contact-section {
    grid-column: span 1;
  }
}

@media (max-width: 768px) {
  .service-promises {
    grid-template-columns: 1fr;
    gap: 15px;
    padding: 20px 0;
  }
  
  .promise-item {
    justify-content: center;
    text-align: center;
  }
  
  .footer-content {
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
    padding: 20px 0;
  }
  
  .qr-codes {
    justify-content: center;
  }
  
  .copyright-links {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    gap: 5px;
  }
}

@media (max-width: 480px) {
  .footer-content {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .contact-section {
    grid-column: span 1;
  }
}
</style>
