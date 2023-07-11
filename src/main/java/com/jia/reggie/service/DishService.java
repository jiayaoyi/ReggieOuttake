package com.jia.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jia.reggie.dto.DishDTO;
import com.jia.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDTO dishDTO);

    public DishDTO getWithFlavor(Long id);

    public void updateWithFlavor(DishDTO dishDTO);

}
