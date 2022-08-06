package com.atguigu.gulimall.product.app;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.gulimall.product.entity.SupermarketEntity;
import com.atguigu.gulimall.product.service.SupermarketService;
import com.bohui.common.utils.PageUtils;
import com.bohui.common.utils.R;



/**
 * 
 *
 * @author leifengyang
 *
 * @date 2020-09-11 13:50:53
 */
@RestController
@RequestMapping("product/supermarket")
public class SupermarketController {
    @Autowired
    private SupermarketService supermarketService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:supermarket:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = supermarketService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:supermarket:info")
    public R info(@PathVariable("id") Integer id){
		SupermarketEntity supermarket = supermarketService.getById(id);

        return R.ok().put("supermarket", supermarket);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:supermarket:save")
    public R save(@RequestBody SupermarketEntity supermarket){
		supermarketService.save(supermarket);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:supermarket:update")
    public R update(@RequestBody SupermarketEntity supermarket){
		supermarketService.updateById(supermarket);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:supermarket:delete")
    public R delete(@RequestBody Integer[] ids){
		supermarketService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
