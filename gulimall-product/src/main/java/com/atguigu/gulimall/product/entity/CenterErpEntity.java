package com.atguigu.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * erp三级分类
 * 
 * @author shangweizheng
 * @email siwad@gmail.com
 * @date 2021-01-07 13:42:09
 */
@Data
@TableName("center_erp")
public class CenterErpEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * erpId
	 */
	@TableId
	private Long erpId;
	/**
	 * erp名称
	 */
	private String name;
	/**
	 * 父类id
	 */
	private Long parentCid;
	/**
	 * 层级
	 */
	private Integer erpLevel;
	/**
	 * 是否显示[0-不显示，1显示]
	 */
	private Integer showStatus;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 图标地址
	 */
	private String icon;


	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@TableField(exist=false)
	private List<CenterErpEntity> children;

}
