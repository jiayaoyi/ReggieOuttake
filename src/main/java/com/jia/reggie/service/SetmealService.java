package com.jia.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jia.reggie.dto.SetmealDTO;
import com.jia.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDTO setmealDTO);

    public SetmealDTO getWithDish(Long id);

    public void updateWithDish(SetmealDTO setmealDTO);

    public void deleteWithDish(List<Long> idss);
}
