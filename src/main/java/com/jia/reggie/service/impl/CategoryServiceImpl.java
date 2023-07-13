package com.jia.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jia.reggie.common.CustomException;
import com.jia.reggie.entity.Category;
import com.jia.reggie.entity.Dish;
import com.jia.reggie.entity.Setmeal;
import com.jia.reggie.mapper.CategoryMapper;
import com.jia.reggie.service.CategoryService;
import com.jia.reggie.service.DishService;
import com.jia.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author kk
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类
     * @param ids 菜品类型id
     */
    @Override
    public void remove(Long ids) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,ids);
        int dishCount = (int) dishService.count(dishLambdaQueryWrapper);
        //查询当前分类是否关联了菜品，如果已经关联直接抛出业务异常
        if (dishCount>0){
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        //查询当前分类是否关联了套餐，如果已经关联直接抛出业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,ids);
        int setmealCount = (int) setmealService.count(setmealLambdaQueryWrapper);
        if (setmealCount>0){
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        //都没有关联，则正常删除
        super.removeById(ids);
    }
}
