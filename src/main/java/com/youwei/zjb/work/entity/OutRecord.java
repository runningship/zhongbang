package com.youwei.zjb.work.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="work_out")
public class OutRecord {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	@Column(name="uid")
	public Integer userId;
	
	public String clients;
	
	public String houses;
	
	public Date outTime;
	
	@Column(name="onTime")
	public Date backTime;
	
	/**
	 * 0 带看，1 陪看，2 自看
	 */
	public Integer outHouse;
	
	/**
	 * 外出原因
	 */
	public String outCont;
	
	public String onCont;
	
	/**
	 * 1,已完成，0或null 待批阅
	 */
	public Integer reply;
	
	/**
	 * 打分
	 */
	public Integer integral;
	
	/**
	 * 1 外出公事，0 外出看房
	 */
	@Column(name="cate")
	public Integer category;
	
	/**
	 * 批阅评价 
	 */
	public String conts;
	
	public String clientInfo;
	
	public String houseInfo;
	
}
