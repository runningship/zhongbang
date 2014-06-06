package com.youwei.zjb.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *办理步骤 
 */
@Entity
@Table(name="work_table_process_class")
public class ProcessClass {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	/**
	 * 行政分类编号
	 */
	@Column(name="claid")
	public Integer adminClassId;
	
	/**
	 * 处理人id
	 */
	@Column(name="uid")
	public Integer userId;
	
	/**
	 * 处理人姓名
	 */
	@Column(name="title")
	public String username;
	
	public String conts;
	
	/**
	 * 序号
	 */
	public Integer ordera;
	
}
