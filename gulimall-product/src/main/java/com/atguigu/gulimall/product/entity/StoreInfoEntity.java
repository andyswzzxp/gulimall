package com.atguigu.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * spu信息
 * 
 * @author leifengyang
 *
 * @date 2020-09-17 11:07:25
 */
@Data
@TableName("pms_store_info")
public class StoreInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 店铺id
	 */
	@TableId
	private Long id;
	/**
	 * 店铺名称
	 */
	private String storeName;
	/**
	 * 店铺描述
	 */
	private String storeDescription;
	/**
	 * 所属商超id
	 */
	private Long marketId;
	/**
	 * erpid
	 */
	private Long erpId;
	/**
	 * 向日葵号
	 */
	private String sunflower;
	/**
	 * 向日葵密码
	 */
	private String sunflowerpwd;
	/**
	 * 店铺状态[0 - 下架，1 - 上架]
	 */
	private Integer storeStatus;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private Date updateTime;

}
