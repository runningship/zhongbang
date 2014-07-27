package com.youwei.zjb.entity;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.bc.web.WebParam;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.youwei.zjb.SimpDaoTool;

/**
 * 终端用户
 */
@Entity
@Table(name="uc_user")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@WebParam(name="userId")
	public Integer id;
	
	@Column(name="did")
	public Integer deptId;
	
	public String uname;
	
	@Column(name="lpwds")
	public String pwd;
	
	public Date addtime;
	
	@Column(name="unid")
	public Integer roleId;
	
	@Column(name="llock")
	public Integer lock;
	
	public String tel;
	
	@Column(name="dizhi")
	public String address;
	
	/**
	 * 0 或null 在职,1 离职
	 */
	public Integer flag;
	
	/**
	 * 入职登记是否审核
	 */
	public Integer sh;
	
	@Column(name="sex")
	public Integer gender;
	
	public String xueli;
	
	@Column(name="nling")
	public Integer age;
	
	public String sfz;
	
	public Integer hunyin;
	
	/**
	 * 入企时间
	 */
	public Date rqsj;
	
	/**
	 * 离职时间
	 */
	public Date lzsj;
	
	/**
	 * 入企途径
	 */
	public String rqtj;
	
	/**
	 * 部门架构中的层次
	 */
	public String orgpath;
	
	/**
	 * 人员架构中的层次
	 */
	public String dutypath;
	
	public Integer avatar;
	
	public Role getRole(){
		return SimpDaoTool.getGlobalCommonDaoService().get(Role.class, roleId);
	}
	
	public Department Department(){
		return SimpDaoTool.getGlobalCommonDaoService().get(Department.class, deptId);
	}
}
