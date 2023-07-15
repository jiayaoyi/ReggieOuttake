package com.jia.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jia.reggie.common.BaseContext;
import com.jia.reggie.common.R;
import com.jia.reggie.entity.ShoppingCart;
import com.jia.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author kk
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<String> add(@RequestBody ShoppingCart shoppingCart) {
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, setmealId);
        }
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
        if (cartServiceOne != null) {
            Integer numbuer = cartServiceOne.getNumber();
            cartServiceOne.setNumber(numbuer + 1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            shoppingCartService.save(shoppingCart);
        }
        return R.success("添加到购物车成功");
    }

    /**
     * 拿购物车信息
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> getListForShopingCartIcon() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        Long userId = BaseContext.getCurrentId();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);
        return R.success(shoppingCartList);
    }

    @PostMapping("/sub")
    public R<String> subForShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        if (shoppingCart.getDishId() != null) {
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
        if (cartServiceOne.getNumber() > 1) {
            Integer numbuer = cartServiceOne.getNumber();
            cartServiceOne.setNumber(numbuer - 1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            shoppingCartService.removeById(cartServiceOne);
        }
        return R.success("减少菜品成功");
    }

    @DeleteMapping("/clean")
    public R<String> cleanShoppingCart() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        Long userId = BaseContext.getCurrentId();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功");
    }
}
