package com.youwei.zjb.oa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Attence {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public Integer userId;
	
	/**
	 * 1 上午上班，2上午下班，3下午上班 ，4下午下班
	 */
	public Integer type;
	
	public Date checktime;
}
