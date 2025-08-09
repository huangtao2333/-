package com.jingdong.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单VO
 * 用于返回给前端的订单信息
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Data
public class OrderVO {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 实付金额
     */
    private BigDecimal payAmount;

    /**
     * 运费
     */
    private BigDecimal shippingFee;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 订单状态描述
     */
    private String statusDesc;

    /**
     * 支付方式
     */
    private Integer paymentMethod;

    /**
     * 支付方式描述
     */
    private String paymentMethodDesc;

    /**
     * 支付状态
     */
    private Integer paymentStatus;

    /**
     * 支付状态描述
     */
    private String paymentStatusDesc;

    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人手机号
     */
    private String receiverPhone;

    /**
     * 收货地址
     */
    private String receiverAddress;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 发货时间
     */
    private LocalDateTime shipTime;

    /**
     * 确认收货时间
     */
    private LocalDateTime confirmTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 订单详情列表
     */
    private List<OrderItemVO> orderItems;

    /**
     * 设置订单状态描述
     */
    public void setStatus(Integer status) {
        this.status = status;
        if (status != null) {
            switch (status) {
                case 0:
                    this.statusDesc = "待付款";
                    break;
                case 1:
                    this.statusDesc = "待发货";
                    break;
                case 2:
                    this.statusDesc = "待收货";
                    break;
                case 3:
                    this.statusDesc = "待评价";
                    break;
                case 4:
                    this.statusDesc = "已完成";
                    break;
                case 5:
                    this.statusDesc = "已取消";
                    break;
                case 6:
                    this.statusDesc = "已退款";
                    break;
                default:
                    this.statusDesc = "未知状态";
                    break;
            }
        }
    }

    /**
     * 设置支付方式描述
     */
    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
        if (paymentMethod != null) {
            switch (paymentMethod) {
                case 1:
                    this.paymentMethodDesc = "支付宝";
                    break;
                case 2:
                    this.paymentMethodDesc = "微信支付";
                    break;
                case 3:
                    this.paymentMethodDesc = "银行卡";
                    break;
                default:
                    this.paymentMethodDesc = "未知支付方式";
                    break;
            }
        }
    }

    /**
     * 设置支付状态描述
     */
    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
        if (paymentStatus != null) {
            switch (paymentStatus) {
                case 0:
                    this.paymentStatusDesc = "未支付";
                    break;
                case 1:
                    this.paymentStatusDesc = "已支付";
                    break;
                case 2:
                    this.paymentStatusDesc = "支付失败";
                    break;
                default:
                    this.paymentStatusDesc = "未知支付状态";
                    break;
            }
        }
    }
}
