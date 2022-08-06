package com.atguigu.gulimall.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class SkuItemSaleAttrVo {
            /**
         * 属性id
         */
        private  Long attrId;
        /**
         * 属性名
         */
        private  String attrName;

        private List<AttrValueWithSkuIdVo> attrValues;
}
