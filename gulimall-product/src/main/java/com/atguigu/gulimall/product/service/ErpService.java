package com.atguigu.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bohui.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.ErpEntity;

import java.util.Map;

/**
 * 
 *
 * @author leifengyang
 *
 * @date 2020-09-08 16:12:35
 */
public interface ErpService extends IService<ErpEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

