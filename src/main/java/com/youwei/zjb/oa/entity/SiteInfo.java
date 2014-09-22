package com.youwei.zjb.oa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="work_oa_url")
public class SiteInfo {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public String title;
	
	public String conts;
	
	public String beizhu;
	
	public Integer ordera;
}
