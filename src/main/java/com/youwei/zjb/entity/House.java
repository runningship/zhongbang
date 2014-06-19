package com.youwei.zjb.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class House {

	@Id
	public Integer id;

	/**
	 * 店面编号
	 */
	@Column(name = "did")
	public Integer deptId;

	@Column(name = "uid")
	public Integer userId;
	/**
	 * 房产公司编号
	 */
	public Integer cid;

	// /**
	// * 状态：4 在售，6 已售，7 停售
	// */
	// public String ztai;

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
	public Integer hxf;

	/**
	 * 房型(厅数)
	 */
	public Integer hxt;

	/**
	 * 房型(卫数)
	 */
	public Integer hxw;

	/**
	 * 房型
	 */
	public Integer hxy;

	public String chaoxiang;

	/**
	 * 装修
	 */
	public Integer zhuangxiu;

	public Float mianji;

	/**
	 * 租价
	 */
	public Float zjia;

	/**
	 * 售价
	 */
	public Float sjia;

	public Float djia;

	/**
	 * 联系人
	 */
	public String lxr;

	public String tel;

	public String forlxr;

	public String fortel;

	public String beizhu;

	// TODO
	@Column(name = "xxlb")
	public Integer jiaoyi;

	@Column(name = "yongtu")
	public String leibie;

	// 状态
	public String flag;

	@Column(name = "biaoshi")
	public String xingzhi;

	public String tuijie;

	public Integer dateyear;

	public Date dateweituo;
	public Date datejiaofang;

	public Date dateadd;

	public Date dategenjin;

	/**
	 * 收藏此房源的人
	 */
	public String fav;

	public String houseNumber;

	/**
	 * 1,已删除 0 或空未删除
	 */
	public Integer isdel;

	public Integer sh;

	// TODO
	public String chanquan;

	// TODO
	public Integer tudizheng;
	
	public String fordlr;
	
	public String fordlrtel;

}
