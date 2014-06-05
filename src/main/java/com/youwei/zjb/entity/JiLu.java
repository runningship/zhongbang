package com.youwei.zjb.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="work_jilu")
public class JiLu {

	@Id
	public Integer id;
	
	@Column(name="uid")
	public Integer userId;
	
	public Date addtime;
	
	@Column(name="cate")
	public Integer category;
	
	public String conts;
	
	public String title;
	
	public Date datetime;
	
	public Date endtime;
	
	public String goin;
	
	public String files;
}
