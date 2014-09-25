package com.youwei.zjb.view.hetong;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.user.UserHelper;

public class view {

	public Document initPage(Document doc , HttpServletRequest req){
		User user = ThreadSession.getUser();
		String authParent = req.getParameter("authParent");
		Boolean has = UserHelper.hasAuthority(user, authParent+"_buzhou_edit");
		String html = doc.html();
		html = html.replace("$${canEditBlqk}", has.toString());
		doc = Jsoup.parse(html);
		return doc;
	}
}
