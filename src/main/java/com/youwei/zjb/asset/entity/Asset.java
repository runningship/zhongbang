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
	
	@Column(name="uid")
	public Integer userId;
	
	@Column(name="did")
	public Integer deptId;
	
	@Column(name="cla" ,nullable=false)
	public String name;
	
	@Column(name="num" , nullable=false)
	public Integer count;
	
	public String beizhu;
	
	public Date addtime;
	
	@Column(name="editime")
	public Date edittime;
	
	@Column(nullable=false)
	public Float djia;
	
	public Float zjia; 
}
