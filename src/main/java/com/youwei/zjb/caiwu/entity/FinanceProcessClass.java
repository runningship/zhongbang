package com.youwei.zjb.caiwu.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *财务办理步骤 
 */
@Entity
public class FinanceProcessClass {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	/**
	 * 分类编号
	 */
	public Integer claid;
	
	/**
	 * 处理人id
	 */
	@Column(nullable=false)
	public Integer uid;
	
	/**
	 * 处理人姓名
	 */
	public String username;
	
	public String conts;
	
	/**
	 * 序号
	 */
	@Column(nullable=false)
	public Integer ordera;
	
}
