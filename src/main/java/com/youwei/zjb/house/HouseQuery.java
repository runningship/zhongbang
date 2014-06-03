package com.youwei.zjb.house;

import java.util.ArrayList;
import java.util.List;

import com.youwei.zjb.AbstractQuery;

public class HouseQuery extends AbstractQuery{

	public HouseAttribute xinzhi;
	public String quyu;
	public FangXing fangxing;
	public List<JiaoYi> jiaoyi = new ArrayList<JiaoYi>();
	public State state;
	public String leibie;
	public Float sjiaStart;
	public Float sjiaEnd;
	public String xpath;
	public DateType dateType;
	public String dateStart;
	public String dateEnd;
	public String louxing;
	public Float mianjiStart;
	public Float mianjiEnd;
	public Integer lcengStart;
	public Integer lcengEnd;
	public String chaoxiang;
	public String chanquan;
	public Integer userId;
	public Integer isdel = 0;
}
