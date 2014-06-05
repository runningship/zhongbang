package com.youwei.zjb.entity;

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
	
	@Column(name="cate")
	public Integer category;
	
	public Date addtime;
	
	public String files;
	
}
