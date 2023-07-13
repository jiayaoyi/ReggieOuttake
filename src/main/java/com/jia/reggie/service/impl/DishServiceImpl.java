package com.jia.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jia.reggie.dto.DishDto;
import com.jia.reggie.entity.Dish;
import com.jia.reggie.entity.DishFlavor;
import com.jia.reggie.mapper.DishMapper;
import com.jia.reggie.service.DishFlavorService;
import com.jia.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Transactional
    public void saveWithFlavor(DishDto dishDTO) {
        //保存菜品基本信息到菜品表
        this.save(dishDTO);
        //获得菜品id
        Long dishId = dishDTO.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        //菜品口味表
        dishFlavorService.saveBatch(flavors);
    }

    public DishDto getWithFlavor(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDTO = new DishDto();
        BeanUtils.copyProperties(dish, dishDTO);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDTO.setFlavors(flavors);
        return dishDTO;
    }

    public void updateWithFlavor(DishDto dishDTO) {
        this.updateById(dishDTO);
        Long dishId = dishDTO.getId();
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId, dishDTO.getId());
        dishFlavorService.remove(queryWrapper);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

}
