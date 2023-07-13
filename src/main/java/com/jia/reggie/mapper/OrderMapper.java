package com.jia.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jia.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
/**
 * @author kk
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
