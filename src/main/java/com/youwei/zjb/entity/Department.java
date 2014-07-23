package com.youwei.zjb.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.youwei.zjb.SimpDaoTool;

/**
 * 房产公司，房产公司的子公司，店面，或部门，层级关系通过fid来关联
 * fid=0;表示为根级公司
 */
@Entity
@Table(name="uc_comp")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Department {

	@Id
	public Integer id;
	
	/**
	 * 上级公司id
	 */
	public Integer fid;
	
	public String namea;
	
	@Column(name="code_num")
	public String codeNum;
	
	public String path;
	
	public Department getParent(){
		return SimpDaoTool.getGlobalCommonDaoService().get(Department.class, fid);
	}
	public List<Department> getDirectChildren(){
		List<Department> children = SimpDaoTool.getGlobalCommonDaoService().listByParams(Department.class, new String[]{"fid"}, new Object[]{id});
		if(children==null){
			return new ArrayList<Department>();
		}
		return children;
	}
}
