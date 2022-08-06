package com.atguigu.gulimall.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bohui.common.utils.PageUtils;
import com.bohui.common.utils.Query;

import com.atguigu.gulimall.product.dao.StoreInfoDao;
import com.atguigu.gulimall.product.entity.StoreInfoEntity;
import com.atguigu.gulimall.product.service.StoreInfoService;


@Service("storeInfoService")
public class StoreInfoServiceImpl extends ServiceImpl<StoreInfoDao, StoreInfoEntity> implements StoreInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<StoreInfoEntity> page = this.page(
                new Query<StoreInfoEntity>().getPage(params),
                new QueryWrapper<StoreInfoEntity>()
        );

        return new PageUtils(page);
    }

}