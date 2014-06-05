package com.youwei.zjb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 日志
 */
@Entity
@Table(name="work_Journal")
public class Journal {

	@Id
	public Integer id;
	
	@Column(name="uid")
	public Integer userId;
	
	public String conta;
	
	
}
