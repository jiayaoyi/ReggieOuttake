package com.jia.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jia.reggie.dto.DishDto;
import com.jia.reggie.entity.Dish;

/**
 * @author kk
 */
public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDTO);

    DishDto getWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDTO);

}
