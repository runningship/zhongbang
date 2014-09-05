package com.youwei.zjb.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.GException;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.sys.entity.PC;

public class SecurityHelper {

	public static void main(String[] args){
		System.out.println(Md5("111111"));
		
	}
	public static String Md5(String plainText) {
		try {
			if(plainText==null){
				return "";
			}
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static boolean validate(PC target){
		List<PC> list = SimpDaoTool.getGlobalCommonDaoService().listByParams(PC.class, new String[]{"deptId"}, new Object[]{target.deptId});
		if(list==null){
			return false;
		}
		for(PC pc : list){
			if(target.baseboard.equals(pc.baseboard) && target.bios.equals(pc.bios) && target.cpu.equals(pc.cpu) && target.harddrive.equals(pc.harddrive)){
				if(pc.lock==1){
					return true;
				}
				throw new GException(PlatformExceptionType.BusinessException, "授权审核中...");
			}
		}
//		if(hasMac(list,pc)){
//			return true;
//		}
//		if(hasCPU(list,pc)){
//			return true;
//		}
//		if(hasHarddrive(list,pc)){
//			return true;
//		}
//		if(hasUUID(list,pc)){
//			return true;
//		}
		return false;
	}
	
	private static boolean hasMac(List<PC> list, PC target){
		if(StringUtils.isEmpty(target.baseboard)){
			return false;
		}
		for(PC pc : list){
			if(pc.baseboard!=null && pc.baseboard.contains(target.baseboard)){
				return true;
			}
		}
		return false;
	}
	
	private static boolean hasCPU(List<PC> list, PC target){
		if(StringUtils.isEmpty(target.cpu)){
			return false;
		}
		for(PC pc : list){
			if(pc.cpu!=null && pc.cpu.contains(target.cpu)){
				return true;
			}
		}
		return false;
	}
	
	private static boolean hasHarddrive(List<PC> list, PC target){
		if(StringUtils.isEmpty(target.harddrive)){
			return false;
		}
		for(PC pc : list){
			if(pc.harddrive!=null && pc.harddrive.contains(target.harddrive)){
				return true;
			}
		}
		return false;
	}
	
	private static boolean hasUUID(List<PC> list, PC target){
		if(StringUtils.isEmpty(target.bios)){
			return false;
		}
		for(PC pc : list){
			if(pc.bios!=null && pc.bios.contains(target.bios)){
				return true;
			}
		}
		return false;
	}
}
