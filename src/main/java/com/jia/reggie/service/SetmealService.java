package com.jia.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jia.reggie.dto.SetmealDto;
import com.jia.reggie.entity.Setmeal;

import java.util.List;

/**
 * @author kk
 */
public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDTO);

    SetmealDto getWithDish(Long id);

    void updateWithDish(SetmealDto setmealDTO);

    void deleteWithDish(List<Long> idss);
}
