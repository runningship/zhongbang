package com.youwei.zjb.admin.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Proxy;

/**
 * 行政表格
 */
@Entity
@Table(name="work_table")
public class AdminTable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	/**
	 * 创建人
	 */
	@Column(name="uid")
	public Integer userId;
	
	public String title;
	
	@Basic(fetch=FetchType.LAZY)
	public String conts;
	
	public Date addtime;
	
	@Column(name="claid")
	public Integer classId;
	
	public Integer sh;
	
}
