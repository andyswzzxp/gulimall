package com.atguigu.gulimall.product.app;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.gulimall.product.entity.ErpEntity;
import com.atguigu.gulimall.product.service.ErpService;
import com.bohui.common.utils.PageUtils;
import com.bohui.common.utils.R;



/**
 * 
 *
 * @author leifengyang
 *
 * @date 2020-09-08 16:12:35
 */
@RestController
@RequestMapping("product/erp")
public class ErpController {
    @Autowired
    private ErpService erpService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:erp:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = erpService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:erp:info")
    public R info(@PathVariable("id") Integer id){
		ErpEntity erp = erpService.getById(id);

        return R.ok().put("erp", erp);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:erp:save")
    public R save(@RequestBody ErpEntity erp){
		erpService.save(erp);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:erp:update")
    public R update(@RequestBody ErpEntity erp){
		erpService.updateById(erp);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:erp:delete")
    public R delete(@RequestBody Integer[] ids){
		erpService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
