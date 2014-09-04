package com.youwei.zjb.util;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.bc.sdak.utils.LogUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.youwei.zjb.house.HouseAttribute;
import com.youwei.zjb.house.JiaoYi;
import com.youwei.zjb.house.State;
import com.youwei.zjb.house.ZhuangXiu;
import com.youwei.zjb.house.entity.House;

public class DataHelper {

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
	public static final String User_Default_Password = "123456";
	private static final HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
	static{
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void fillDefaultValue(List<Map> list , String key,Object defValue){
		for(Map map : list){
			if(map.get(key)==null){
				map.put(key, defValue);
			}
		}
	}
	
	public static void escapeHtmlField(List<Map> list , String key){
		for(Map map : list){
			if(map.get(key)==null){
				continue;
			}
			Document doc = Jsoup.parse(map.get(key).toString());
			map.put(key, doc.text());
		}
	}
	
	public static String toPinyin(String hanzi){
		try {
			String pinyin="";
			for(int i=0;i<hanzi.length();i++){
				String[] arr = PinyinHelper.toHanyuPinyinStringArray(hanzi.charAt(i), format);
				if(arr!=null && arr.length>0){
					pinyin+=arr[0];
				}else{
					LogUtil.warning("汉字["+hanzi.charAt(i)+"]转拼音失败,");
					continue;
				}
			}
			return pinyin;
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			LogUtil.log(Level.WARN, "汉字["+hanzi+"]转拼音失败", e);
		}
		return "";
	}
	
	public static String toPinyinShort(String hanzi){
		try {
			String pinyin="";
			for(int i=0;i<hanzi.length();i++){
				String[] arr = PinyinHelper.toHanyuPinyinStringArray(hanzi.charAt(i), format);
				if(arr!=null && arr.length>0){
					if(StringUtils.isNotEmpty(arr[0])){
						pinyin+=arr[0].charAt(0);
					}
				}else{
					LogUtil.warning("汉字["+hanzi.charAt(i)+"]转拼音失败,");
					continue;
				}
			}
			return pinyin;
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			LogUtil.log(Level.WARN, "汉字["+hanzi+"]转拼音失败", e);
		}
		return "";
	}
	
	public static String compareHouse(House old, House newH){
		StringBuilder sb = new StringBuilder();
		if(StringUtils.isNotEmpty(old.area) && StringUtils.isNotEmpty(newH.area) && !newH.area.equals(old.area)){
			sb.append("楼盘名称: "+old.area+"-->"+newH.area).append("<br/>");
		}
		if(StringUtils.isNotEmpty(old.beizhu) && StringUtils.isNotEmpty(newH.beizhu) && !newH.beizhu.equals(old.beizhu)){
			sb.append("备注: "+old.beizhu+"-->"+newH.beizhu).append("<br/>");
		}
		if(StringUtils.isNotEmpty(old.chanquan) && StringUtils.isNotEmpty(newH.chanquan) && !newH.chanquan.equals(old.chanquan)){
			sb.append("产权证: "+old.chanquan+"-->"+newH.chanquan).append("<br/>");
		}
		if(StringUtils.isNotEmpty(old.chaoxiang) && StringUtils.isNotEmpty(newH.chaoxiang) && !newH.chaoxiang.equals(old.chaoxiang)){
			sb.append("朝向: "+old.chaoxiang+"-->"+newH.chaoxiang).append("<br/>");
		}
		if(StringUtils.isNotEmpty(old.dhao) && StringUtils.isNotEmpty(newH.dhao) && !newH.dhao.equals(old.dhao)){
			sb.append("楼栋号: "+old.dhao+"-->"+newH.dhao).append("<br/>");
		}
		if(StringUtils.isNotEmpty(old.fhao) && StringUtils.isNotEmpty(newH.fhao) && !newH.fhao.equals(old.fhao)){
			sb.append("房号: "+old.fhao+"-->"+newH.fhao).append("<br/>");
		}
		if(StringUtils.isNotEmpty(old.forlxr) && StringUtils.isNotEmpty(newH.forlxr) && !newH.forlxr.equals(old.forlxr)){
			sb.append("业务员: "+old.forlxr+"-->"+newH.forlxr).append("<br/>");
		}
		if(StringUtils.isNotEmpty(old.fortel) && StringUtils.isNotEmpty(newH.fortel) && !newH.fortel.equals(old.fortel)){
			sb.append("业务员电话: "+old.fortel+"-->"+newH.fortel).append("<br/>");
		}
		if(StringUtils.isNotEmpty(old.leibie) && StringUtils.isNotEmpty(newH.leibie) && !newH.leibie.equals(old.leibie)){
			sb.append("房屋类别: "+old.leibie+"-->"+newH.leibie).append("<br/>");
		}
		if(StringUtils.isNotEmpty(old.luduan) && StringUtils.isNotEmpty(newH.luduan) && !newH.luduan.equals(old.luduan)){
			sb.append("路段: "+old.luduan+"-->"+newH.luduan).append("<br/>");
		}
		if(StringUtils.isNotEmpty(old.lxing) && StringUtils.isNotEmpty(newH.lxing) && !newH.lxing.equals(old.lxing)){
			sb.append("楼型: "+old.lxing+"-->"+newH.lxing).append("<br/>");
		}
		if(StringUtils.isNotEmpty(old.lxr) && StringUtils.isNotEmpty(newH.lxr) && !newH.lxr.equals(old.lxr)){
			sb.append("业主名: "+old.lxr+"-->"+newH.lxr).append("<br/>");
		}
		if(old.lceng!=null && newH.lceng!=null && !newH.lceng.equals(old.lceng)){
			sb.append("楼层: "+old.lceng+"-->"+newH.lceng).append("<lceng/>").append("<br/>");
		}
		if(StringUtils.isNotEmpty(old.quyu) && StringUtils.isNotEmpty(newH.quyu) && !newH.quyu.equals(old.quyu)){
			sb.append("区域: "+old.quyu+"-->"+newH.quyu).append("<br/>");
		}
		if(StringUtils.isNotEmpty(old.tel) && StringUtils.isNotEmpty(newH.tel) && !newH.tel.equals(old.tel)){
			sb.append("业主电话: "+old.tel+"-->"+newH.tel).append("<br/>");
		}
		if(StringUtils.isNotEmpty(old.ztai) && StringUtils.isNotEmpty(newH.ztai) && !newH.ztai.equals(old.ztai)){
			sb.append("状态: "+State.parse(old.ztai)+"-->"+State.parse(newH.ztai)).append("<br/>");
		}
		if(StringUtils.isNotEmpty(old.xingzhi) && StringUtils.isNotEmpty(newH.xingzhi) && !newH.xingzhi.equals(old.xingzhi)){
			sb.append("性质: "+HouseAttribute.parse(old.xingzhi)+"-->"+HouseAttribute.parse(newH.xingzhi)).append("<br/>");
		}
		if(old.zjia!=null && newH.zjia!=null && !newH.zjia.equals(old.zjia)){
			sb.append("租金: "+old.zjia+"-->"+newH.zjia).append("<br/>");
		}
		if(old.zhuangxiu!=null && newH.zhuangxiu!=null && !newH.zhuangxiu.equals(old.zhuangxiu)){
			sb.append("装修: "+ZhuangXiu.parse(old.zhuangxiu)+"-->"+ ZhuangXiu.parse(newH.zhuangxiu)).append("<br/>");
		}
		if(old.zceng!=null && newH.zceng!=null && !newH.zceng.equals(old.zceng)){
			sb.append("总层数: "+old.zceng+"-->"+newH.zceng).append("<br/>");
		}
		if(old.tudizheng!=null && newH.tudizheng!=null && !newH.tudizheng.equals(old.tudizheng)){
			sb.append("土地证: "+old.tudizheng+"-->"+newH.tudizheng).append("<br/>");
		}
		if(old.sjia!=null && newH.sjia!=null && !newH.sjia.equals(old.sjia)){
			sb.append("总价: "+old.sjia+"-->"+newH.sjia).append("<br/>");
		}
		if(old.mianji!=null && newH.mianji!=null && !newH.mianji.equals(old.mianji)){
			sb.append("面积: "+old.mianji+"-->"+newH.mianji).append("<br/>");
		}
		if(old.djia!=null && newH.djia!=null && !newH.djia.equals(old.djia)){
			sb.append("单价: "+old.djia+"-->"+newH.djia).append("<br/>");
		}
		if(old.jiaoyi!=null && newH.jiaoyi!=null && !newH.jiaoyi.equals(old.jiaoyi)){
			sb.append("交易: "+JiaoYi.valueOf(old.jiaoyi)+"-->"+JiaoYi.valueOf(newH.jiaoyi)).append("<br/>");
		}
		if(old.hxf!=null && newH.hxf!=null && !newH.hxf.equals(old.hxf)){
			sb.append("户型(房数): "+old.hxf+"-->"+newH.hxf).append("<br/>");
		}
		if(old.hxt!=null && newH.hxt!=null && !newH.hxt.equals(old.hxt)){
			sb.append("户型(厅数): "+old.hxt+"-->"+newH.hxt).append("<br/>");
		}
		if(old.hxw!=null && newH.hxw!=null && !newH.hxw.equals(old.hxw)){
			sb.append("户型(卫数): "+old.hxw+"-->"+newH.hxw).append("<br/>");
		}
		if(old.dateyear!=null && newH.dateyear!=null && !newH.dateyear.equals(old.dateyear)){
			sb.append("年代: "+old.dateyear+"-->"+newH.dateyear).append("<br/>");
		}
		return sb.toString();
	}
}
