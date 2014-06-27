package com.youwei.zjb.admin.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="work_table_process")
public class Process {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	/**
	 * 制表人
	 */
	@Column(name="uid")
	public Integer creatorId;
	
	@Column(name="pid")
	public Integer tableId;
	
	/**
	 * 处理人
	 */
	@Column(name="title")
	public String uname;
	
	public String conts;
	
	/**
	 * 0 未处理，1 已处理
	 */
	public Integer flag;
	
	public Date addtime;
	
	public Integer ordera;
	
	/**
	 * 行政分类
	 */
	@Column(name="claid")
	public Integer adminClassId;
	
	/**
	 * 处理人
	 */
	@Column(name="userid")
	public Integer processorId;
}
