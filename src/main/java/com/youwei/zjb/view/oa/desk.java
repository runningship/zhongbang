package com.youwei.zjb.view.oa;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.RoleAuthority;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.util.DataHelper;

public class desk {

	String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
	
	public void initPage(Document doc , String dataScope){
		Elements list = doc.getElementsByAttributeValue("serverId","timeflash");
		Date now = new Date();
		SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy年MM月dd日");
		String nowStr = dateSdf.format(now);
	    String src = "/zb/style/image/clock.swf?h="+now.getHours()+"&amp;m="+now.getMinutes()+"&amp;s="+now.getSeconds()+"&amp;z=服务器 北京 时间&amp;t=今天是"+nowStr+" "+weekDays[now.getDay()]+"&amp;c=打卡时间按此系统为准";
	      
	    String flash = "<object  classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000'  width='244' height='160'><embed id='flashEid' name='calendarFlash' src='"+src+"' quality='high' wmode='transparent'  type='application/x-shockwave-flash' width='244' height='160'></object>";
	    if(!list.isEmpty()){
	    	list.get(0).html(flash);
	    }
	}
}
