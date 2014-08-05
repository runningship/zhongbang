package com.youwei.zjb.caiwu.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Finance {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	@Column(name="uid")
	public Integer userId;
	
	@Column(nullable=false)
	public Integer claid;
	
	@Column(nullable=false)
	public String title;
	
	public String conts;
	
	public Date addtime;
	
	public Integer sh;
}
