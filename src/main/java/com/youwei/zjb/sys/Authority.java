package com.youwei.zjb.sys;

import net.sf.json.JSONObject;

public enum Authority {
	fy(null,"房源管理"),
	fy_fy(fy,"房源"),
	fy_fy_on(fy_fy,"开启房源"),
	fy_fy_list(fy_fy,"房源列表"),
	fy_fy_add(fy_fy,"发布房源"),
	fy_fy_edit(fy_fy,"修改房源"),
	fy_fy_sh(fy_fy,"审核房源"),
	fy_fy_del(fy_fy,"删除房源"),
	fy_fy_recyle(fy_fy,"回收站"),
	
	fy_attr(fy,"房源特性"),
	fy_attr_fp_view(fy_attr,"封盘查看"),
	fy_attr_fp_edit(fy_attr,"封盘修改"),
	fy_attr_tp_view(fy_attr,"特盘查看"),
	fy_attr_tp_edit(fy_attr,"特盘查看"),
	fy_attr_sp_view(fy_attr,"私盘查看"),
	fy_attr_sp_edit(fy_attr,"私盘查看"),
	fy_attr_tel_view(fy_attr,"查看电话"),
	fy_attr_dhao_view(fy_attr,"查看栋号"),
	fy_attr_fhao_view(fy_attr,"查看房号"),
	
	fy_gj(fy,"跟进"),
	fy_gj_on(fy_gj,"开启跟进"),
	fy_gj_view(fy_gj,"查看跟进"),
	fy_gj_add(fy_gj,"添加跟进"),
	fy_gj_edit(fy_gj,"编辑跟进"),
	fy_gj_sh(fy_gj,"审核跟进"),
	fy_gj_del(fy_gj,"删除"),
	
	fy_zd(fy,"楼盘字典"),
	fy_zd_list(fy_zd,"楼盘字典列表"),
	fy_zd_add(fy_zd,"添加楼盘"),
	fy_zd_edit(fy_zd,"修改楼盘"),
	fy_zd_del(fy_zd,"删除楼盘"),
	
	ky(null,"客源管理"),
	ky_ky(ky,"客源"),
	ky_ky_list(ky_ky,"客源列表"),
	ky_ky_add(ky_ky,"添加客源"),
	ky_ky_edit(ky_ky,"修改客源"),
	ky_ky_del(ky_ky,"删除客源"),
	ky_ky_gj(ky_ky,"允许客源跟进"),
	yw_ky_data_dept(ky_ky,"本公司数据"),
	yw_ky_data_quyu(ky_ky,"本区域数据"),
	yw_ky_data_all(ky_ky,"所有数据"),
	
	yw(null,"业务管理"),
	yw_jc(yw,"业务基础设置"),
	yw_jc_on(yw_jc,"开启业务管理"),
	yw_wc(yw,"外出权限"),
	yw_wc_list(yw_wc,"查看外出列表"),
	yw_wc_add(yw_wc,"添加外出记录"),
	yw_wc_sp(yw_wc,"审批评分"),
	yw_wc_data_dept(yw_wc,"本公司数据"),
	yw_wc_data_quyu(yw_wc,"本区域数据"),
	yw_wc_data_all(yw_wc,"所有数据"),
	
	yw_qj(yw,"请假登记"),
	yw_qj_list(yw_qj,"请假登记列表"),
	yw_qj_add(yw_qj,"添加请假登记"),
	yw_qj_sp(yw_qj,"审批评分"),
	yw_qj_data_dept(yw_qj,"本公司数据"),
	yw_qj_data_quyu(yw_qj,"本区域数据"),
	yw_qj_data_all(yw_qj,"所有数据"),
	
	yw_rz(yw,"工作日志"),
	yw_rz_list(yw_rz,"工作日志列表"),
	yw_rz_add(yw_rz,"添加工作日志"),
	yw_rz_sp(yw_rz,"审批评分"),
	yw_rz_data_dept(yw_rz,"本公司数据"),
	yw_rz_data_quyu(yw_rz,"本区域数据"),
	yw_rz_data_all(yw_rz,"所有数据"),
	
	yw_jl(yw,"会议记录"),
	yw_jl_list(yw_jl,"会议记录列表"),
	yw_jl_add(yw_jl,"添加会议记录"),
	yw_jl_data_dept(yw_jl,"本公司数据"),
	yw_jl_data_quyu(yw_jl,"本区域数据"),
	yw_jl_data_all(yw_jl,"所有数据"),
	
	yw_zj(yw,"月度总结"),
	yw_zj_list(yw_zj,"月度总结列表"),
	yw_zj_add(yw_zj,"月度总结记录"),
	yw_zj_data_dept(yw_zj,"本公司数据"),
	yw_zj_data_quyu(yw_zj,"本区域数据"),
	yw_zj_data_all(yw_zj,"所有数据"),
	
	yw_bg(yw,"考勤表格"),
	yw_bg_list(yw_bg,"考勤表格列表"),
	yw_bg_add(yw_bg,"添加考勤表格"),
	yw_bg_data_dept(yw_bg,"本公司数据"),
	yw_bg_data_quyu(yw_bg,"本区域数据"),
	yw_bg_data_all(yw_bg,"所有数据"),
	
	ht(null,"合同管理"),
	ht_ht(ht,"合同"),
	ht_ht_list(ht_ht,"合同列表"),
	ht_ht_add(ht_ht,"添加合同"),
	ht_ht_detail(ht_ht,"查看详细"),
	ht_ht_edit(ht_ht,"修改合同"),
	ht_ht_del(ht_ht,"删除"),
	ht_ht_data_dept(ht_ht,"本公司数据"),
	ht_ht_data_quyu(ht_ht,"本区域数据"),
	ht_ht_data_all(ht_ht,"所有数据"),
	
	ht_bz(ht,"步骤与流程"),
	ht_bz_list(ht,"开启设置步骤与流程"),
	ht_bz_add(ht,"添加步骤"),
	ht_bz_edit(ht,"修改步骤"),
	ht_bz_del(ht,"删除步骤"),
	ht_bz_exec(ht,"执行步骤"),
	
	rs(null,"人事管理"),
	rs_rs(rs,"人事"),
	rs_rs_on(rs_rs,"开启人事权限"),
	rs_rs_list(rs_rs,"人事花名册和详细信息"),
	rs_rs_dj_add(rs_rs,"入职登记"),
	rs_rs_dj_sh(rs_rs,"入职登记审核"),
	rs_rs_dj_select_quyu(rs_rs,"入职登记可选区域"),
	rs_rs_dj_select_dept(rs_rs,"入职登记可选公司"),
	rs_rs_dj_select_duty(rs_rs,"入职登记可选职务"),
	rs_rs_zw_tz(rs_rs,"职位调整提交"),
	rs_rs_zw_sh(rs_rs,"职位调整审核"),
	rs_rs_lz_add(rs_rs,"离职登记提交"),
	rs_rs_lz_sh(rs_rs,"离职登记审核"),
	rs_rs_edit(rs_rs,"修改用户信息"),
	rs_rs_del(rs_rs,"删除用户(永久)"),
	rs_rs_data_dept(rs_rs,"本公司数据"),
	rs_rs_data_quyu(rs_rs,"本区域数据"),
	rs_rs_data_all(rs_rs,"所有数据"),
	
	rs_dept(rs,"分公司设置"),
	rs_dept_quyu_add(rs_dept,"添加区域"),
	rs_dept_quyu_edit(rs_dept,"修改区域"),
	rs_dept_quyu_del(rs_dept,"删除区域"),
	rs_dept_dept_add(rs_dept,"添加分公司"),
	rs_dept_dept_edit(rs_dept,"修改分公司"),
	rs_dept_dept_del(rs_dept,"删除分公司"),
	rs_dept_list(rs_dept,"分公司列表"),
	
	
	xz(null,"行政管理"),
	xz_xz(xz,"行政"),
	xz_xz_on(xz_xz,"是否开启"),
	
	xz_bg(xz,"报表管理"),
	xz_bg_fl_add(xz_bg,"添加分类"),
	xz_bg_add(xz_bg,"添加报表"),
	xz_bg_view(xz_bg,"查看报表"),
	xz_bg_edit(xz_bg,"修改报表"),
	xz_bg_bz_edit(xz_bg,"办理步骤修改"),
	xz_bg_data_dept(xz_bg,"本公司数据"),
	xz_bg_data_quyu(xz_bg,"本区域数据"),
	xz_bg_data_all(xz_bg,"所有数据"),
	
	zc(null,"资产管理"),
	zc_zc(zc,"资产"),
	zc_zc_on(zc_zc,"开启资产管理"),
	zc_gd(zc,"固定资产"),
	zc_gd_list(zc_gd,"固定资产列表"),
	zc_gd_add(zc_gd,"添加"),
	zc_gd_edit(zc_gd,"编辑"),
	zc_gd_del(zc_gd,"删除"),
	zc_gd_view(zc_gd,"查看详细"),
	zc_gd_data_dept(zc_gd,"本公司数据"),
	zc_gd_data_quyu(zc_gd,"本区域数据"),
	zc_gd_data_all(zc_gd,"所有数据"),
	
	zc_bg(zc,"办公用品"),
	zc_bg_list(zc_bg,"列表"),
	zc_bg_add(zc_bg,"申请"),
	zc_bg_edit(zc_bg,"修改"),
	zc_bg_del(zc_bg,"删除"),
	zc_bg_sh(zc_bg,"审核"),
	zc_bg_data_dept(zc_bg,"本公司数据"),
	zc_bg_data_quyu(zc_bg,"本区域数据"),
	zc_bg_data_all(zc_bg,"所有数据"),
	
	//系统或后台管理
	xt(null,"后台管理"),
	xt_zw(xt,"职务权限"),
	xt_zw_view(xt_zw,"职务表" , 1),
	xt_zw_add(xt_zw,"添加职务"),
	xt_zw_edit(xt_zw,"修改职务"),
	xt_zw_del(xt_zw,"删除职务"),
	
	xt_sq(xt,"电脑授权"),
	xt_sq_list(xt_sq,"列表" ,1),
	xt_sq_del(xt_sq,"删除"),
	xt_sq_edit(xt_sq,"修改"),
	xt_sq_data_dept(xt_sq,"本公司数据"),
	xt_sq_data_quyu(xt_sq,"本区域数据"),
	xt_sq_data_all(xt_sq,"所有数据"),
	
	xt_sq_sh(xt_sq,"授权审核"),
	xt_sq_sh_list(xt_sq_sh,"审核列表" , 1),
	
	xt_cz(xt,"操作记录"),
	xt_cz_list(xt_cz,"操作记录列表",1),
	xx(null,"房源");
	
	private Authority parent;
	private String text;
	private int isMenu=0;
	private Authority(Authority parent, String name){
		this.parent = parent;
		this.text = name;
	}
	private Authority(Authority parent, String name ,int isMenu){
		this.parent = parent;
		this.text = name;
		this.isMenu = isMenu;
	}
	public String getText() {
		return text;
	}
	public int IsMenu(){
		return isMenu;
	}
	public Authority getParent(){
		return parent;
	}
	
	public JSONObject toJSONObject(){
		JSONObject jobj = new JSONObject();
		jobj.put("name", this.name());
		jobj.put("text", this.text);
		jobj.put("parent", this.parent==null? "": this.parent.name());
		jobj.put("isMenu", this.isMenu);
		return jobj;
	}
}
