package com.youwei.zjb.asset.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="work_assets")
public class Asset {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public Integer userId;
	
	@Column(name="cla")
	public String name;
	
	@Column(name="num")
	public Integer count;
	
	public String beizhu;
	
	public Date addtime;
	
	public Date edittime;
	
	public Float djia;
	
	public Float zjia; 
}
