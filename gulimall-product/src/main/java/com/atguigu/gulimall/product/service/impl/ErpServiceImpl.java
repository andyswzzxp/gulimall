package com.atguigu.gulimall.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bohui.common.utils.PageUtils;
import com.bohui.common.utils.Query;

import com.atguigu.gulimall.product.dao.ErpDao;
import com.atguigu.gulimall.product.entity.ErpEntity;
import com.atguigu.gulimall.product.service.ErpService;


@Service("erpService")
public class ErpServiceImpl extends ServiceImpl<ErpDao, ErpEntity> implements ErpService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ErpEntity> page = this.page(
                new Query<ErpEntity>().getPage(params),
                new QueryWrapper<ErpEntity>()
        );

        return new PageUtils(page);
    }

}