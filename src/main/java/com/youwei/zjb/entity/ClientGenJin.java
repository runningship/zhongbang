package com.youwei.zjb.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="client_follow")
public class ClientGenJin {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	@Column(name="uid")
	public Integer userId;
	
	public String title;
	
	public String conts;
	
	public Date addtime;
	
	@Column(name="pid")
	public Integer clientId;
	
	public Integer flag;
}
