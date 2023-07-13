package com.jia.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jia.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜品mapper
 *
 * @author kk
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
