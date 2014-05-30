package com.youwei.zjb.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="house_gj")
public class GenJin {
	
	@Id
	public Integer id;
	
	public Integer cid;
	
	/**
	 * 区域id
	 */
	public Integer qid;
	
	/**
	 * 店面id
	 */
	public Integer did;
	
	
	/**
	 * 用户id
	 */
	public Integer uid;
	
	/**
	 * 房源id
	 */
	public Integer hid;
	
	/**
	 * 跟进信息
	 */
	public String conts;
	
	public Date addtime;
	
	public Integer sh;
	
	public Integer chuzu;
	
	public Integer flag;
	
	public String ztai;
}
