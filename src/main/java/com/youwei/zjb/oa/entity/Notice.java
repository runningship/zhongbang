package com.youwei.zjb.oa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="work_oa_gg")
public class Notice {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	@Column(name="uid")
	public Integer userId;
	
	@Column(nullable=false)
	public Integer claid;
	
	public String title;
	
	public String conts;
	
	public Date addtime;
	
	public Integer sh;
}
