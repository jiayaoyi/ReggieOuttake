package com.jia.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jia.reggie.common.CustomException;
import com.jia.reggie.dto.SetmealDto;
import com.jia.reggie.entity.Setmeal;
import com.jia.reggie.entity.SetmealDish;
import com.jia.reggie.mapper.SetmealMapper;
import com.jia.reggie.service.SetmealDishService;
import com.jia.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kk
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDTO) {
        this.save(setmealDTO);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDTO.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public SetmealDto getWithDish(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDTO = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDTO);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> dishes = setmealDishService.list(queryWrapper);
        setmealDTO.setSetmealDishes(dishes);
        return setmealDTO;
    }

    @Override
    public void updateWithDish(SetmealDto setmealDTO) {
        this.updateById(setmealDTO);
        Long setmealId = setmealDTO.getId();
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealId);
        setmealDishService.remove(queryWrapper);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = (int) this.count(queryWrapper);
        if (count > 0) {
            throw new CustomException("套餐正在出售中，无法删除");
        }
        this.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
    }
}