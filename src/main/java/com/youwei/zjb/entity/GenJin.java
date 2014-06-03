package com.youwei.zjb.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="house_gj")
public class GenJin {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	
	/**
	 * 用户id
	 */
	@Column(name="uid")
	public Integer userId;
	
	/**
	 * 房源id
	 */
	public Integer hid;
	
	/**
	 * 跟进信息
	 */
	public String conts;
	
	public Date addtime;
	
	public Integer sh =0;
	
	public Integer chuzu;
	
	public String area;
	
	public String bianhao;
}
