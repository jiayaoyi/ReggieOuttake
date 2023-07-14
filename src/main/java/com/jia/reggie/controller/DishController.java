package com.jia.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jia.reggie.common.R;
import com.jia.reggie.dto.DishDto;
import com.jia.reggie.entity.Category;
import com.jia.reggie.entity.Dish;
import com.jia.reggie.entity.DishFlavor;
import com.jia.reggie.service.CategoryService;
import com.jia.reggie.service.DishFlavorService;
import com.jia.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    DishService dishService;

    @Autowired
    DishFlavorService dishFlavorService;

    @Autowired
    CategoryService categoryService;

    /**
     * 新增菜品方法
     *
     * @param dishDTO 实体类
     * @return 消息体
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDTO) {
        dishService.saveWithFlavor(dishDTO);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(int pageSize, int page, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, queryWrapper);
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dishDtoList = records.stream().map(item -> {
            DishDto dishDTO = new DishDto();
            BeanUtils.copyProperties(item, dishDTO);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            dishDTO.setCategoryName(category.getName());
            return dishDTO;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(dishDtoList);
        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDTO = dishService.getWithFlavor(id);
        return R.success(dishDTO);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDTO) {
        dishService.updateWithFlavor(dishDTO);
        return R.success("修改菜品成功");
    }

    /**
     * 删除菜品
     *
     * @param ids 菜品id
     * @return 消息体
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        ids.forEach(id -> {
            LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Dish::getId, id);
            dishService.remove(queryWrapper);
        });
        return R.success("删除成功");
    }

    /**
     * 更新菜品状态
     * @param ids 更新菜品ID
     * @param status 菜品状态码
     * @return 消息返回体
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@RequestParam List<Long> ids, @PathVariable Integer status) {
        ids.forEach(id -> {
            LambdaUpdateWrapper<Dish> queryWrapper = new LambdaUpdateWrapper<>();
            queryWrapper.eq(Dish::getId, id).set(Dish::getStatus, status);
            dishService.update(queryWrapper);
        });
        return R.success("更新成功");
    }

//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish) {
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
//        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
//        queryWrapper.eq(Dish::getStatus, 1);
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> dishList = dishService.list(queryWrapper);
//        return R.success(dishList);
//    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishList = dishService.list(queryWrapper);


        List<DishDto> dishDtoList = dishList.stream().map(item -> {
            DishDto dishDTO = new DishDto();
            BeanUtils.copyProperties(item, dishDTO);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            dishDTO.setCategoryName(category.getName());
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorQueryWrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorQueryWrapper);
            dishDTO.setFlavors(dishFlavorList);
            return dishDTO;
        }).collect(Collectors.toList());
        return R.success(dishDtoList);
    }
}
