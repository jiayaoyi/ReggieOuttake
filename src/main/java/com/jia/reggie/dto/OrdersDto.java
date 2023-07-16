package com.jia.reggie.dto;

import com.jia.reggie.entity.OrderDetail;
import com.jia.reggie.entity.Orders;
import lombok.Data;

import java.util.List;

/**
 * OrdersDto
 *
 * @author Jia Yaoyi
 * @date 2023/07/16
 */
@Data
public class OrdersDto extends Orders {

    private List<OrderDetail> orderDetails;

    private Long id;
}
