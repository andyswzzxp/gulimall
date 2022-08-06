package com.atguigu.gulimall.product.feign;

import com.bohui.common.to.es.SkuEsModel;
import com.bohui.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gulimall-search")
public interface SearchFeignService {

    @PostMapping("/search/product")
    public R productStatusup(@RequestBody List<SkuEsModel> skuEsModels);
}
