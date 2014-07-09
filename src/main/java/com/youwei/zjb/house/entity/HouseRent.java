package com.youwei.zjb.house.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="house_rent")
public class HouseRent {

	@Id
	public Integer id;
	
	/**
	 * 用户id
	 */
	public Integer uid;
	
	/**
	 * 店面编号
	 */
	public Integer did;
	
	/**
	 * 房产公司编号
	 */
	public Integer cid;
	
	/**
	 * 状态：4 在售，6 已售，7 停售
	 */
	public String ztai;
	
	
	public String quyu;
	
	/**
	 * 楼盘名称
	 */
	public String area;
	
	/**
	 * 楼栋号
	 */
	public String dhao;
	
	/**
	 * 房号
	 */
	public String fhao;
	
	public String luduan;
	
	/**
	 * 楼层
	 */
	public Integer lceng;
	
	/**
	 * 总层
	 */
	public Integer zceng;
	
	/**
	 * 楼型
	 */
	public String lxing;
	
	/**
	 * 房型(房数)
	 */
	public String hxf;
	
	/**
	 * 房型(厅数)
	 */
	public String hxt;
	
	/**
	 * 房型(卫数)
	 */
	public String hxw;
	
	/**
	 * 装修
	 */
	public String zxiu;
	
	public Float mji;
	
	public Float zjia;
	
	public Float djia;
	
	/**
	 * 联系人
	 */
	public String lxr;
	
	public String tel;
	
	public String forlxr;
	
	public String fortel;
	
	public String beizhu;
	
	public String flag;
	
	public String dateyear;
	
	public Date dateadds;
	
	public Date datagenjin;
	
	/**
	 * 收藏此房源的人
	 */
	public String fav;
	
	public Integer seeGX;
	
	public Integer seeHM;
	
	public Integer seeFH;
	
	public Integer isdel;
	
	public Integer sh;
	
	public String fangshi;
	
	public Date dategjlock;

}
