package com.atguigu.gulimall.product.service;

import com.bohui.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.CenterErpEntity;
import com.baomidou.mybatisplus.extension.service.IService;


import java.util.List;
import java.util.Map;

/**
 * erp三级分类
 *
 * @author shangweizheng
 * @email siwad@gmail.com
 * @date 2021-01-07 13:42:09
 */
public interface CenterErpService extends IService<CenterErpEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CenterErpEntity> listWithTree();
}

