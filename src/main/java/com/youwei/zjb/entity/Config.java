package com.youwei.zjb.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 用户反馈信息
 */
@Entity
public class Config {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	/**
	 * quyu...
	 */
	public String type;
	public String name;
	public String pyShort;
}
