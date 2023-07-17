package com.jia.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jia.reggie.common.R;
import com.jia.reggie.dto.SetmealDto;
import com.jia.reggie.entity.Setmeal;
import com.jia.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
@Slf4j
@EnableCaching
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @PostMapping
    @CachePut(value = "setmealCache", key = "#setmealDTO.id")
    public R<String> save(@RequestBody SetmealDto setmealDTO) {
        setmealService.saveWithDish(setmealDTO);
        return R.success("新增套餐成功");
    }

    @GetMapping("/page")
    public R<Page> page(int pageSize, int page, String name) {
        Page<Setmeal> setmealInfo = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Setmeal::getUpdateTime);
        queryWrapper.like(name != null, Setmeal::getName, name);
        setmealService.page(setmealInfo, queryWrapper);
        return R.success(setmealInfo);
    }

    @DeleteMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> delete(@RequestParam("ids") List<Long> ids) {
        setmealService.deleteWithDish(ids);
        return R.success("删除成功");
    }

    @GetMapping("/{id}")
    @Cacheable(value = "setmealCache", key = "#id")
    public R<SetmealDto> get(@PathVariable Long id) {
        SetmealDto setmealDTO = setmealService.getWithDish(id);
        return R.success(setmealDTO);
    }

    @PutMapping
    @CachePut(value = "setmealCache", key = "#setmealDTO.id")
    public R<String> update(@RequestBody SetmealDto setmealDTO) {
        setmealService.updateWithDish(setmealDTO);
        return R.success("更新成功");
    }

    @PostMapping("/status/{status}")
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> updateStatus(@RequestParam("ids") List<Long> ids, @PathVariable Integer status) {
        ids.forEach(id -> {
            LambdaUpdateWrapper<Setmeal> queryWrapper = new LambdaUpdateWrapper<>();
            queryWrapper.eq(Setmeal::getId, id).set(Setmeal::getStatus, status);
            setmealService.update(queryWrapper);
        });
        return R.success("更新成功");
    }

    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "'setmeal:' + #setmeal.categoryId + '_' + #setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }
}
