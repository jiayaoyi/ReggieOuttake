package com.jia.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jia.reggie.entity.Orders;

/**
 * @author kk
 */
public interface OrderService extends IService<Orders> {

    public void submit(Orders orders);
}
