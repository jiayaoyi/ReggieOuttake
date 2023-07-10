package com.jia.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jia.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜品mapper
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
