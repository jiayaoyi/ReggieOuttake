package com.jia.reggie.dto;

import com.jia.reggie.entity.Setmeal;
import com.jia.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDTO extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
