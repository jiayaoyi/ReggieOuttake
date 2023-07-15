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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    public R<Page> page(int pageSize, int page, Long id, @RequestParam(required = false) String number, @RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime) {
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id != null, Orders::getId, id);
        if (number != null) {
            queryWrapper.like(Orders::getId, number);
        }
        if (beginTime != null && endTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime begin = LocalDateTime.parse(beginTime, formatter);
            LocalDateTime end = LocalDateTime.parse(endTime, formatter);
            queryWrapper.between(Orders::getOrderTime, begin, end);
        }
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(ordersPage, queryWrapper);
        return R.success(ordersPage);
    }

    /**
     * 提交订单方法
     *
     * @param orders 订单
     * @return 消息
     */
    @PostMapping("/submit")
    public R<String> orderSubmit(@RequestBody Orders orders) {
        orderService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * 用户页面查询订单信息
     *
     * @param page     页码
     * @param pageSize 页面大小
     * @return 消息
     */
    @GetMapping("/userPage")
    public R<Page<Orders>> orderPage(int page, int pageSize) {
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        Long userId = BaseContext.getCurrentId();
        queryWrapper.eq(Orders::getUserId, userId);
        orderService.page(ordersPage, queryWrapper);
        return R.success(ordersPage);
    }

    /**
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> updateStatus(@RequestBody Orders orders) {
        orderService.updateById(orders);
        return R.success("更新状态成功");
    }

    @PostMapping("/again")
    public R<String> again(@RequestBody Orders orders) {
        Orders newOrder = orderService.getById(orders);
        newOrder.setId(null);
        newOrder.setCheckoutTime(LocalDateTime.now());
        newOrder.setOrderTime(LocalDateTime.now());
        newOrder.setStatus(2);

        orderService.save(newOrder);
        return R.success("再来一单成功");
    }
}
