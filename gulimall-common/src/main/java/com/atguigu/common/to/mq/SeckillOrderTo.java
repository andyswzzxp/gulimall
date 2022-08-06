package com.bohui.common.to.mq;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SeckillOrderTo  implements Serializable {
    private  String orderSn;

    /**
     * 活动场次id
     */
    private Long promotionSessionId;
    /**
     * 商品id
     */
    private Long skuId;



    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    /**
     * 秒杀总量
     */
    private Integer num;

    private  Long memberId;


}
