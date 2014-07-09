package com.youwei.zjb.oa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="work_oa_gg_class")
public class NoticeClass {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	@Column(nullable=false)
	public String title;
}
