package com.jia.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jia.reggie.common.R;
import com.jia.reggie.entity.Category;
import com.jia.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 *
 * @author jiayaoyi
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * 新增分类
     *
     * @param category 实体对象
     * @return 1为成功
     */
    @PostMapping
    @CachePut(value = "categoryCache", key = "#category.id")
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    @GetMapping("/page")
    public R<Page<Category>> page(int page, int pageSize) {
        //分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件，根据sort进行排序
        queryWrapper.orderByAsc(Category::getSort);
        //分页查询
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id删除分类
     *
     * @param ids 分类id
     * @return 消息体
     */
    @DeleteMapping
    @CacheEvict(value = "categoryCache", key = "#ids")
    public R<String> delete(Long ids) {
        log.info("删除分类，id为{}", ids);
        categoryService.removeById(ids);
        return R.success("分类删除成功");
    }

    /**
     * 根据ID修改分类信息
     *
     * @param category 分类信息
     * @return 消息体
     */
    @PutMapping
    @CachePut(value = "categoryCache", key = "#category.id")
    public R<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    /**
     * 获取分类列表
     *
     * @param category 分类
     * @return 消息体
     */
    @GetMapping("/list")
    @Cacheable(value = "categoryCache", key = "'list:' + #category.type")
    public R<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> categoryList = categoryService.list(queryWrapper);
        return R.success(categoryList);
    }

}
