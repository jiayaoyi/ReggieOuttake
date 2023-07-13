package com.jia.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jia.reggie.entity.OrderDetail;
import com.jia.reggie.mapper.OrderDetailMapper;
import com.jia.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author kk
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
