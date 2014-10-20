package com.youwei.zjb.work.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 批阅项
 */
@Entity
public class PYItem {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public Integer uid;
	
	public String uname;
	
	public Date addtime;
	
	public Integer bizId;
	
	/**
	 * 0 会议记录，1 优质房源 , 2 月度总结, 3考勤表格
	 */
	@Column(name="cate")
	public Integer category;
	
	public String conts;
	
	public Integer finish;
}
