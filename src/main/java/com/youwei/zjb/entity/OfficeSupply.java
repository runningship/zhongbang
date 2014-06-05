package com.youwei.zjb.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="work_res")
public class OfficeSupply {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer Id;
	
	public String cla;
	
	public Date addtime;
	
	public String names;
	
	@Column(name="num")
	public Integer count;
	
	public String beizhu;
	
	public Float djia;
	
	public Float zjia;
	
	/**
	 * 相关人
	 */
	public String xgr;
	
	/**
	 * 0 未审核，1 已审核
	 */
	public Integer shenhe;
}
