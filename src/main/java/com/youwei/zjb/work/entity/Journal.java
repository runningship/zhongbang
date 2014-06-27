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
	
	public String title;
	
	public String conta;
	
	public String contb;
	
	public Integer reply;
	
	public Integer integral;
	
	/**
	 * 1.请假 
	 * 0.工作日志
	 */
	@Column(name="cate")
	public Integer category;
	
	public Date addtime;
	
}
