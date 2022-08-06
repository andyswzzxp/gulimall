package com.atguigu.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author leifengyang
 *
 * @date 2020-09-11 13:50:53
 */
@Data
@TableName("pms_supermarket")
public class SupermarketEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 
	 */
	private String name;
	/**
	 * 0停用，1启用
	 */
	private Integer status;
	/**
	 * 商超logo地址
	 */
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 检索首字母
	 */
	private String firstLetter;
	/**
	 * 排序
	 */
	private Integer sort;

}
