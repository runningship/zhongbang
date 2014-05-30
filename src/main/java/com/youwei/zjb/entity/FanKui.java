package com.youwei.zjb.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户反馈信息
 */
@Entity
@Table(name="plugin_fankui")
public class FanKui {

	@Id
	public Integer id;
	
	/**
	 * 房产公司id
	 */
	public Integer cid;
	
	/**
	 * 区域
	 */
	public Integer qid;
	
	/**
	 * 店面
	 */
	public Integer did;
	
	/**
	 * 用户
	 */
	public Integer uid;
	
	public String conts;
	
	public Date addtime;
}
