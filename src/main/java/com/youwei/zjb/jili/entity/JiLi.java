package com.youwei.zjb.jili.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class JiLi {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	/**
	 * 被激励人(业务员)
	 */
	public Integer uid;
	
	public Float score;
	
	public Date addtime;
	
	public String reason;
}
