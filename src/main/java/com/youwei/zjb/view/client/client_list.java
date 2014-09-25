package com.youwei.zjb.view.client;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.RoleAuthority;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.entity.UserAuthority;

public class client_list {

	public Document initPage(Document doc , HttpServletRequest req ){
		String dataScope = req.getParameter("dataScope");
		Elements list = doc.getElementsByAttributeValue("serverId","assign");
		if(list==null || list.isEmpty()){
			return doc;
		}
		Element assign = list.get(0);
		User user = ThreadSession.getUser();
		String auth = "";
		if(!"all".equals(dataScope)){
			if(user!=null){
				List<UserAuthority> authorities = user.Authorities();
				for(UserAuthority ra : authorities){
					if((dataScope+"_assign_by_dept").equals(ra.name)){
						auth += ra.name;
						break;
					}
					if((dataScope+"_assign_by_comp").equals(ra.name)){
						auth = ra.name;
					}
				}
			}
		}
		auth="'"+auth+"'";
		String show = assign.attr("show");
		show = show.replace("$${auth}", auth);
		assign.attr("show" , show);
		return doc;
	}
}
