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
 * @date 2020-09-08 16:12:35
 */
@Data
@TableName("pms_erp")
public class ErpEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * erp名
	 */
	private String erpname;
	/**
	 * 1.启用 0.停止
	 */
	private Integer status;
	/**
	 * 品牌logo地址
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
