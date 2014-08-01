package com.youwei.zjb.work.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="work_jilu")
public class JiLu {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	@Column(name="uid")
	public Integer userId;
	
	public Date addtime;
	
	/**
	 * 0 会议记录，1 优质房源 , 2 月度总结, 3考勤表格
	 */
	@Column(name="cate")
	public Integer category;
	
	public String conts;
	
	@Column(nullable=false)
	public String title;
	
	@Column(name="datetime")
	public Date starttime;
	
	public Date endtime;
	
	public String goin;
	
}
