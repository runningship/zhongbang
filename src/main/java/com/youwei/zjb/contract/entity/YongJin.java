package com.youwei.zjb.contract.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="work_pact_yongjin")
public class YongJin {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public String beizhu;
	
	/**
	 * 1 佣金收费，3 代收费
	 */
	public Integer flag;
	
	@Column(name="proid")
	public Integer contractId;
	
	public Date dateadd;
	
	public Date dateA;
	
	public Date dateB;
	
	public Date dateC;
	
	/**
	 * 甲方佣金
	 * 评估费
	 */
	public Float qian_a;
	
	/**
	 * 乙方佣金
	 * 手续费
	 */
	public Float qian_b;
	
	/**
	 * 抵押登记费
	 */
	public Float qian_c;
	
	/**
	 * 办理人
	 */
	public String qian_r;
}
