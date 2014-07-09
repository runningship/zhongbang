package com.youwei.zjb.house.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 都盘字典(小区表)
 */
@Entity
@Table(name="house_annex")
public class District {

	@Id
	public Integer id;
	
	public String quyu;
	
	/**
	 * 小区名称
	 */
	@Column(name="area")
	public String name;
	
	/**
	 * 小区首字母
	 */
	@Column(name="pin")
	public String pyShort;
	
	/**
	 * 小区全拼
	 */
	public String pinyin;
	
	/**
	 * 价格，均价
	 */
	public String jizhunjia;
	
	public String address;
	
	/**
	 * 维度
	 */
	public String maplat;
	
	/**
	 * 经度
	 */
	public String maplng;
	
	public Date addtime;
	
	/**
	 * 是否审核
	 * 1 已审核
	 * 0 未审核
	 */
	public Integer sh;
	
	public Integer uid;
	
}
