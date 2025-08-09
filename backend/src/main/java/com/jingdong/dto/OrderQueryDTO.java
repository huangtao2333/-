package com.jingdong.dto;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * 订单查询DTO
 * 用于接收订单查询请求参数
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Data
public class OrderQueryDTO {

    /**
     * 页码（从1开始）
     */
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    @Min(value = 1, message = "每页大小必须大于0")
    private Integer pageSize = 10;

    /**
     * 订单状态
     * 0-待付款，1-待发货，2-待收货，3-待评价，4-已完成，5-已取消，6-已退款
     */
    private Integer status;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 开始时间（格式：yyyy-MM-dd）
     */
    private String startDate;

    /**
     * 结束时间（格式：yyyy-MM-dd）
     */
    private String endDate;
}
