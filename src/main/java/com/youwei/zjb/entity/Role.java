package com.youwei.zjb.entity;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.youwei.zjb.SimpDaoTool;

/**
 * 
 *角色表，职务表
 */
@Entity
@Table(name="uc_unit")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Role {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	
	/**
	 * 职务，相当于角色名称
	 */
	public String title;
	
	/**
	 * 角色说明
	 */
	public String cont;
	
	public Integer flag;
	
	public Integer sh;
	
	public Integer num;
	
	List<RoleAuthority> Authorities(){
		return SimpDaoTool.getGlobalCommonDaoService().listByParams(RoleAuthority.class, new String[]{"roleId"}, new Object[]{id});
	}
}
