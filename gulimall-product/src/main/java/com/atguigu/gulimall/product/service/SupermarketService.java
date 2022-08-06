package com.atguigu.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bohui.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.SupermarketEntity;

import java.util.Map;

/**
 * 
 *
 * @author leifengyang
 *
 * @date 2020-09-11 13:50:53
 */
public interface SupermarketService extends IService<SupermarketEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

