package com.jia.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jia.reggie.common.BaseContext;
import com.jia.reggie.common.R;
import com.jia.reggie.entity.Orders;
import com.jia.reggie.service.OrderDetailService;
import com.jia.reggie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author kk
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 订单查询方法
     *
     * @param pageSize
     * @param page
     * @param id
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int pageSize, int page, Long id) {
        Page<Orders> ordersPage = new Page<>();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id != null, Orders::getId, id);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(ordersPage, queryWrapper);
        return R.success(ordersPage);
    }

    /**
     * 提交订单方法
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> orderSubmit(@RequestBody Orders orders) {
        orderService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page<Orders>> orderPage(int page, int pageSize) {
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        Long userId = BaseContext.getCurrentId();
        queryWrapper.eq(Orders::getUserId, userId);
        orderService.page(ordersPage, queryWrapper);
        return R.success(ordersPage);
    }
}
