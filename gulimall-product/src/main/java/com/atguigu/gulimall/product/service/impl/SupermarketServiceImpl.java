package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.entity.BrandEntity;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bohui.common.utils.PageUtils;
import com.bohui.common.utils.Query;

import com.atguigu.gulimall.product.dao.SupermarketDao;
import com.atguigu.gulimall.product.entity.SupermarketEntity;
import com.atguigu.gulimall.product.service.SupermarketService;
import org.springframework.util.StringUtils;


@Service("supermarketService")
public class SupermarketServiceImpl extends ServiceImpl<SupermarketDao, SupermarketEntity> implements SupermarketService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        //1、获取key
        String key = (String) params.get("key");
        QueryWrapper<SupermarketEntity> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(key)){
            queryWrapper.eq("id",key).or().like("name",key);
        }

        IPage<SupermarketEntity> page = this.page(
                new Query<SupermarketEntity>().getPage(params),
                queryWrapper

        );

        return new PageUtils(page);
    }

}