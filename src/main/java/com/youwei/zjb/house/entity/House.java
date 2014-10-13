package com.youwei.zjb.house.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class House {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;

	/**
	 * 店面编号
	 */
	@Column(name = "did")
	public Integer deptId;

	/**
	 * 发布人
	 */
	@Column(name = "uid")
	public Integer userId;

	@Column(nullable=false)
	public String quyu;

	/**
	 * 区域编号.区域编号+"-"+房源ID=房源编号
	 */
	public String quyuCode;
	
	/**
	 * 楼盘名称
	 */
	@Column(nullable=false)
	public String area;

	/**
	 * 楼栋号
	 */
	@Column(nullable=false)
	public String dhao;

	/**
	 * 房号
	 */
	public String fhao;

	public String luduan;

	/**
	 * 楼层
	 */
	@Column(nullable=false)
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
	public Float hxf;

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

	@Column(nullable=false)
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

	/**
	 * 房主电话
	 */
	public String tel;

	/**
	 * 发布人姓名
	 */
	public String fbr;
	
	/**
	 * 发布人id
	 */
	public Integer fbrId;

	//业务员电话
	public String fortel;

	public String beizhu;

	// TODO
	
	@Column(name = "xxlb")
	public Integer jiaoyi;

	@Column(name = "yongtu")
	public String leibie;

	 /**
	 * 状态：4 在售，6 已售，7 停售
	 */
	@Column(name="flag")
	public String ztai;

	@Column(name = "biaoshi")
	public String xingzhi;

	public String tuijie;

	public Integer dateyear;

	public Date dateweituo;
	public Date datejiaofang;

	public Date dateadd;

	public Date dategenjin;
	
	public Date datedel;
	
	/**
	 * 删除人
	 */
	@Column(name="fordel")
	public String userdel;

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
	/**
	 * 1 有，0 无
	 */
	public Integer tudizheng;
	
	public String fordlr;
	
	public String fordlrtel;

}
