package com.youwei.zjb.sys.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 签证员 
 * 已经和办证员合并为交易员
 */
@Entity
public class Qzy {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public Integer userId;
	
}
