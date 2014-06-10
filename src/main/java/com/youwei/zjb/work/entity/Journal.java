package com.youwei.zjb.work.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 日志
 */
@Entity
@Table(name="work_Journal")
public class Journal {

	@Id
	public Integer id;
	
	@Column(name="uid")
	public Integer userId;
	
	public String conta;
	
	public String contb;
	
	public Integer reply;
	
	public Integer integral;
	
	/**
	 * 1.请假 
	 * 0.工作日志
	 * 2.会议记录
	 */
	@Column(name="cate")
	public Integer category;
	
	public Date addtime;
	
}
