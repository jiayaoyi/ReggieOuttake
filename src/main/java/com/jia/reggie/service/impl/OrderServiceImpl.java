package com.jia.reggie.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jia.reggie.entity.Orders;
import com.jia.reggie.mapper.OrderMapper;
import com.jia.reggie.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * @author kk
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper,Orders> implements OrderService {
}
