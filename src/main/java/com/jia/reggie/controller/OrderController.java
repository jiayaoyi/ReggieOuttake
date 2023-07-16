package com.jia.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jia.reggie.common.BaseContext;
import com.jia.reggie.common.R;
import com.jia.reggie.dto.OrdersDto;
import com.jia.reggie.entity.OrderDetail;
import com.jia.reggie.entity.Orders;
import com.jia.reggie.service.OrderDetailService;
import com.jia.reggie.service.OrderService;
import com.jia.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kk
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private ShoppingCartService shoppingCartService;

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
    public R<Page<OrdersDto>> orderPage(int page, int pageSize) {
        Page<OrdersDto> ordersPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        Long userId = BaseContext.getCurrentId();
        queryWrapper.eq(Orders::getUserId, userId);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        List<Orders> ordersList = orderService.list(queryWrapper);

        List<OrdersDto> ordersDtoList = ordersList.stream().map((item) -> {
            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, item.getId());
            List<OrderDetail> orderDetailList = orderDetailService.list(orderDetailLambdaQueryWrapper);

            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            ordersDto.setOrderDetails(orderDetailList);

            return ordersDto;
        }).collect(Collectors.toList());

        ordersPage.setRecords(ordersDtoList);

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
        Orders newOrder = orderService.getById(orders.getId()); // Retrieve the old order
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, orders.getId());
        List<OrderDetail> oldOrderDetails = orderDetailService.list(queryWrapper);

        newOrder.setId(null);
        newOrder.setCheckoutTime(LocalDateTime.now());
        newOrder.setOrderTime(LocalDateTime.now());
        newOrder.setStatus(2);

        orderService.saveOrUpdate(newOrder);


        List<OrderDetail> newOrderDetails = oldOrderDetails.stream().map(oldDetail -> {
            OrderDetail newDetail = new OrderDetail();
            BeanUtils.copyProperties(oldDetail, newDetail);
            newDetail.setId(null);
            newDetail.setOrderId(newOrder.getId());
            return newDetail;
        }).collect(Collectors.toList());

        orderDetailService.saveBatch(newOrderDetails);

        return R.success("再来一单成功");
    }


}
