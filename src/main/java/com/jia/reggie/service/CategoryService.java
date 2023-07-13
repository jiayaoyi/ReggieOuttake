package com.jia.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jia.reggie.entity.Category;

/**
 * @author kk
 */
public interface CategoryService extends IService<Category> {
    void remove(Long ids);
}
