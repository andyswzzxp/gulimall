package com.atguigu.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bohui.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.StoreInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author leifengyang
 *
 * @date 2020-09-17 11:07:25
 */
public interface StoreInfoService extends IService<StoreInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

