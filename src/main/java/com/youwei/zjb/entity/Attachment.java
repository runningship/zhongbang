package com.youwei.zjb.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Attachment {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public String bizType;
	
	public Integer recordId;
	
	public String filename;
	
	public transient String filename_encoded;
	
	public Date uploadTime;
}
