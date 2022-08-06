package com.atguigu.gulimall.product.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bohui.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.BrandEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌
 *
 * @author shangweizheng
 * @email siwad@126.com
 * @date 2021-12-28 15:20:00
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);


    void updateDetail(BrandEntity brand);

    List<BrandEntity> getBrandsByIds(List<Long> brandIds);
}



