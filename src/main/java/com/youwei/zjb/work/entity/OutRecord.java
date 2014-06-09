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
	
	public Integer outHouse;
	
	public String outCont;
	
	public String onCont;
	
	public Integer reply;
	
	public Integer integral;
	
	@Column(name="cate")
	public Integer category;
	
	public String conts;
	
	public String clientInfo;
	
	public String houseInfo;
	
}
