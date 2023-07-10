package com.jia.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jia.reggie.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long ids);
}
