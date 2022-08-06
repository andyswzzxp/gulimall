package com.atguigu.gulimall.product.app;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.gulimall.product.entity.StoreInfoEntity;
import com.atguigu.gulimall.product.service.StoreInfoService;
import com.bohui.common.utils.PageUtils;
import com.bohui.common.utils.R;



/**
 * spu信息
 *
 * @author leifengyang
 *
 * @date 2020-09-17 11:07:25
 */
@RestController
@RequestMapping("product/storeinfo")
public class StoreInfoController {
    @Autowired
    private StoreInfoService storeInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:storeinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = storeInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:storeinfo:info")
    public R info(@PathVariable("id") Long id){
		StoreInfoEntity storeInfo = storeInfoService.getById(id);

        return R.ok().put("storeInfo", storeInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:storeinfo:save")
    public R save(@RequestBody StoreInfoEntity storeInfo){
		storeInfoService.save(storeInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:storeinfo:update")
    public R update(@RequestBody StoreInfoEntity storeInfo){
		storeInfoService.updateById(storeInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:storeinfo:delete")
    public R delete(@RequestBody Long[] ids){
		storeInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
