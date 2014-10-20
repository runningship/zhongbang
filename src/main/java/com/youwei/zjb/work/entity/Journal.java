package com.youwei.zjb.work.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 日志
 */
@Entity
@Table(name="work_Journal")
public class Journal {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	@Column(name="uid")
	public Integer userId;
	
	/**
	 * 对于请假登记而言，这个是请假类别
	 */
	@Column(nullable=false)
	public String title;
	
	public String conta;
	
	public String contb;
	
	public Integer reply;
	
	public Integer integral;
	
	/**
	 * 考核评级
	 */
	public String pingji;
	/**
	 * 2.周呈报表
	 * 1.请假 
	 * 0.工作日清
	 */
	@Column(name="cate")
	public Integer category;
	
	/**
	 * 请假开始时间
	 */
	public Date starttime;
	
	/**
	 * 请假结束时间
	 */
	public Date endtime;
	
	public Date addtime;
	
	/**
	 * 请假有效天数
	 */
	public Float qjdays;
}
