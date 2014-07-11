package com.youwei.zjb.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Client {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	/**
	 * 当前负责的业务员
	 */
	@Column(name="uid")
	public Integer salesman;
	
	/**
	 * 记录的最初登记人
	 */
	public Integer djr;
	
	@Column(name="lxras")
	public String lxr;
	
	@Column(name="telas")
	public String tel;
	
	@Column(name="areas")
	public String area;
	
	@Column(name="luduanas")
	public String luduan;
	
	@Column(name="louxingas")
	public String louxing;
	
	@Column(name="huxingas")
	public String huxing;
	
	@Column(name="zhuangxiuas")
	public String zhaungxiu;
	
	@Column(name="mianjias")
	public Float mianjiFrom;
	
	@Column(name="mianjibs")
	public Float mianjiTo;
	
	@Column(name="jiageas")
	public Float jiageFrom;
	
	@Column(name="jiagebs")
	public Float jiageTo;

	/**
	 * 付佣方式
	 */
	public String fuyongs;
	
	/**
	 * 潜在客户(0),标准客户(1)
	 */
	public String zhongyaos;
	
	public String beizhu;
	
	public Date addtime;
	
	/**
	 * 0 出售，1 出租
	 */
	public Integer chuzu=0;
	
	@Column(name="loucengas")
	public Integer loucengFrom;
	
	@Column(name="loucengbs")
	public Integer loucengTo;
	
	@Column(name="quyuas")
	public String quyu;
	
	public String source;
	
	/**
	 * 有效(1)，无效
	 */
	@Column(name="flag")
	public Integer valid;
}
