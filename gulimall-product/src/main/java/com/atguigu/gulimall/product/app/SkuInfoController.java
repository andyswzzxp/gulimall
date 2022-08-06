package com.atguigu.gulimall.product.app;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.entity.ProductAttrValueEntity;
import com.atguigu.gulimall.product.service.BrandService;
import com.atguigu.gulimall.product.service.ProductAttrValueService;
import com.atguigu.gulimall.product.service.SpuInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gulimall.product.entity.SkuInfoEntity;
import com.atguigu.gulimall.product.service.SkuInfoService;
import com.bohui.common.utils.PageUtils;
import com.bohui.common.utils.R;



/**
 * sku信息
 *
 * @author leifengyang
 *
 * @date 2019-10-01 22:50:32
 */
@RestController
@RequestMapping("product/skuinfo")
public class SkuInfoController {
    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SpuInfoService spuInfoService;

    @Autowired
    private BrandService brandService;


    @Autowired
    private ProductAttrValueService productAttrValueService;


    @GetMapping("/{skuId}/price")
    public R getPrice(@PathVariable("skuId")Long skuId)
    {
        SkuInfoEntity byId = skuInfoService.getById(skuId);


        return R.ok().setData(byId.getPrice().toString());

    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:skuinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = skuInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{skuId}")
    //@RequiresPermissions("product:skuinfo:info")
    public R info(@PathVariable("skuId") Long skuId){
		SkuInfoEntity skuInfo = skuInfoService.getById(skuId);
        System.out.print(skuInfo);
        return R.ok().put("skuInfo", skuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:skuinfo:save")
    public R save(@RequestBody SkuInfoEntity skuInfo){
		skuInfoService.save(skuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:skuinfo:update")
    public R update(@RequestBody SkuInfoEntity skuInfo){
		skuInfoService.updateById(skuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:skuinfo:delete")
    public R delete(@RequestBody Long[] skuIds){
		skuInfoService.removeByIds(Arrays.asList(skuIds));

        return R.ok();
    }



    /**
     *生成xml文件 信息
     */
    @RequestMapping("/CreateXml")
    public R CreateXml(@RequestBody Long[] brandIds){
        SkuInfoEntity skuInfo = skuInfoService.getById(brandIds[0]);
        BrandEntity brand = brandService.getById(skuInfo.getBrandId());
        List<ProductAttrValueEntity> data = productAttrValueService.list(
                new QueryWrapper<ProductAttrValueEntity>().eq("spu_id",skuInfo.getSpuId())
        );
        try {
            // 1、创建document对象
            Document document = DocumentHelper.createDocument();
            // 2、创建根节点rss
            Element rss = document.addElement("BaseInfo");
            // 3、向rss节点添加version属性
            rss.addAttribute("version", "2.0");



            // 4、生成子节点及子节点内容--中台信息
            Element channel = rss.addElement("CenterForm");
            Element IpAddress = channel.addElement("IpAddress");
            IpAddress.setText(data.get(3).getAttrValue());


            Element DeviceId = channel.addElement("DeviceId");
            DeviceId.setText(data.get(4).getAttrValue());

            Element DevicePwd = channel.addElement("DevicePwd");
            DevicePwd.setText(data.get(5).getAttrValue());

            //本地mysql
            Element database = rss.addElement("MySQLDataBase");
            Element MySQLUrl= database.addElement("MySQLUrl");
            MySQLUrl.setText(data.get(8).getAttrValue());
            Element MySQLDBName= database.addElement("MySQLDBName");
            MySQLDBName.setText(data.get(9).getAttrValue());
            Element MySQLUserName = database.addElement("MySQLUserName");
            MySQLUserName.setText(data.get(10).getAttrValue());
            Element MySQLPassWord = database.addElement("MySQLPassWord");
            MySQLPassWord.setText(data.get(11).getAttrValue());

            Element productdatabase = rss.addElement("ProductDataBase");
            Element ProductUrl= productdatabase.addElement("ProductUrl");
            ProductUrl.setText(data.get(14).getAttrValue());
            Element ProductDBName= productdatabase.addElement("ProductDBName");
            ProductDBName.setText(data.get(15).getAttrValue());
            Element ProductUserName = productdatabase.addElement("ProductUserName");
            ProductUserName.setText(data.get(16).getAttrValue());
            Element ProductPassWord = productdatabase.addElement("ProductPassWord");
            ProductPassWord.setText(data.get(17).getAttrValue());

            if (data.get(19).getAttrValue().equals("不同库")) {
                Element vipdatabase = rss.addElement("VipDataBase");
                Element VipUrl = vipdatabase.addElement("VipUrl");
                VipUrl.setText(data.get(14).getAttrValue());
                Element VipDBName = vipdatabase.addElement("VipDBName");
                VipDBName.setText(data.get(15).getAttrValue());
                Element VipUserName = vipdatabase.addElement("VipUserName");
                VipUserName.setText(data.get(16).getAttrValue());
                Element VipPassWord = vipdatabase.addElement("VipPassWord");
                VipPassWord.setText(data.get(17).getAttrValue());
            }

            //同步活动synchro
            String[] splitsynchro= data.get(15).getAttrValue().split(";");

            Element synchro = rss.addElement("synchro");
            Element synchroProduct= synchro.addElement("synchroProduct");
            synchroProduct.setText(Arrays.asList(splitsynchro).contains("商品")?"1":"0");


//商品;分类;满减;满赠;组合
            Element Uploadclassify = synchro.addElement("Uploadclassify");
            Uploadclassify.setText(Arrays.asList(splitsynchro).contains("分类")?"1":"0");

            Element Uploaddiscount = synchro.addElement("Uploaddiscount");
            Uploaddiscount.setText(Arrays.asList(splitsynchro).contains("折扣")?"1":"0");

            Element Uploadfulldelivery = synchro.addElement("Uploadfulldelivery");
            Uploadfulldelivery.setText(Arrays.asList(splitsynchro).contains("满赠")?"1":"0");

            Element Uploadfullreduce = synchro.addElement("Uploadfullreduce");
            Uploadfullreduce.setText(Arrays.asList(splitsynchro).contains("满减")?"1":"0");

            Element Uploadcombination = synchro.addElement("Uploadcombination");
            Uploadcombination.setText(Arrays.asList(splitsynchro).contains("组合")?"1":"0");
            Element Uploadcounter = synchro.addElement("Uploadcounter");
            Uploadcounter.setText(Arrays.asList(splitsynchro).contains("专柜")?"1":"0");


            //上传间隔等uploadCycle uploadCount
            Element uploadCycle = synchro.addElement("uploadCycle");
            uploadCycle.setText(data.get(17).getAttrValue());
            Element uploadCount = synchro.addElement("uploadCount");
            uploadCount.setText(data.get(18).getAttrValue());

            //回写？？？？？？？？？？？




            //--基础信息配置
            Element baseset = rss.addElement("BaseSet");
            Element StoreName = baseset.addElement("StoreName");
            StoreName.setText(skuInfo.getSkuName());

            Element ERPName = baseset.addElement("ERPName");
            ERPName.setText(brand.getName());







            // 5、设置生成xml的格式
            OutputFormat format = OutputFormat.createPrettyPrint();
//            // 设置编码格式
            format.setEncoding("UTF-8");
//
//
//            // 6、生成xml文件
            File file = new File("D:\\config\\DataConfig.xml");
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
//            // 设置是否转义，默认使用转义字符
            writer.setEscapeText(false);
            writer.write(document);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();

        }


//        Centerstore pcenterstore = storeService.loadUpdateStore(centerstore);
//        XmlHandle.createXml( pcenterstore );
////        try {
////
////    			model.addAttribute("result", "导出店铺信息成功");
////    		} catch (Exception e) {
////    			// TODO Auto-generated catch block
////    			e.printStackTrace();
////    			model.addAttribute("result", "导出店铺信息失败");
////    		}
////
//
//
//
//
//
//
//        //获取下载的文件路径（注意获取这里获取的是绝对路径，先获取ServletContext再使用ServletContext的getRealPath方法获取绝对路径）
//
//        String path = "D:/config/DataConfig.xml";
//
//        //设置响应头控制浏览器以下载的形式打开文件
//
//        response.setHeader("content-disposition","attachment;fileName="+"DataConfig.xml");
//
//        InputStream in = new FileInputStream(path); //获取下载文件的输入流
//
//        int count =0;
//
//        byte[] by = new byte[1024];
//
//        //通过response对象获取OutputStream流
//
//        OutputStream out=  response.getOutputStream();
//
//        while((count=in.read(by))!=-1){
//
//            out.write(by, 0, count);//将缓冲区的数据输出到浏览器
//
//        }
//
//        in.close();
//
//        out.flush();
//
//        out.close();
//
//        //1.把信息查出来
//        //2.导出去
//        //3.提示成功
//        try {
//
//            model.addAttribute("result", "导出店铺信息成功");
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            model.addAttribute("result", "导出店铺信息失败");
//        }

        return R.ok().put("spuInfo", skuInfo);
    }

}
