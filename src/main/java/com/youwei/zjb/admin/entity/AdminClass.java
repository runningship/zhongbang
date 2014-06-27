package com.youwei.zjb.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *行政类别
 */
@Entity
@Table(name="work_table_class")
public class AdminClass {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public Integer fid;
	
	public String title;
	
	@Column(name="conts")
	public String template;
	
	public Integer flag;
}
