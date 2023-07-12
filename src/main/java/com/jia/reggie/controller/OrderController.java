package com.jia.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jia.reggie.common.R;
import com.jia.reggie.entity.Orders;
import com.jia.reggie.service.OrderDetailService;
import com.jia.reggie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("/page")
    public R<Page> page (int pageSize,int page,Long id ){
        Page<Orders> ordersPage = new Page<>();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id != null,Orders::getId,id);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(ordersPage,queryWrapper);
        return R.success(ordersPage);
    }
}
