package com.youwei.test.zjb.sys;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class NodeJSTest {

	public static void main(String[] args) {
//		Sigar sigar = new Sigar();
//		org.hyperic.sigar.FileSystem[] filesystems = sigar.getFileSystemList();
//		if (filesystems != null || filesystems.length > 0)
//			System.out.println("\n" + "硬盘第一个分区的卷标="
//					+ getHDSerial(filesystems[1].getDevName()));
		System.out.println(getMotherboardSN());
		// cpu();
	}


	public static String getCPUSerial() {
		String result = "";
		try {
			File file = new File("./tmp.vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);

			String vbs = "On Error Resume Next \r\n\r\n"
					+ "strComputer = \".\"  \r\n"
					+ "Set objWMIService = GetObject(\"winmgmts:\" _ \r\n"
					+ "    & \"{impersonationLevel=impersonate}!\\\\\" & strComputer & \"\\root\\cimv2\") \r\n"
					+ "Set colItems = objWMIService.ExecQuery(\"Select * from Win32_Processor\")  \r\n "
					+ "For Each objItem in colItems\r\n "
					+ "    Wscript.Echo objItem.ProcessorId  \r\n "
					+ "    exit for  ' do the first cpu only! \r\n"
					+ "Next                    ";

			fw.write(vbs);
			fw.close();
			Process p = Runtime.getRuntime().exec(
					"cscript //NoLogo " + file.getPath());
			BufferedReader input = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
			file.delete();
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		if (result.trim().length() < 1 || result == null) {
			result = "无CPU_ID被读取";
		}
		return result.trim();
	}
	
	/** 
     * 获取主板序列号 
     *  
     * @return 
     */  
    public static String getMotherboardSN() {  
        String result = "";  
        try {  
            File file = new File("./realhowto.vbs");  
//            file.deleteOnExit();  
            FileWriter fw = new java.io.FileWriter(file);  
            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"  
                    + "Set colItems = objWMIService.ExecQuery _ \n"  
                    + "   (\"Select * from Win32_BaseBoard\") \n"  
                    + "For Each objItem in colItems \n"  
                    + "    Wscript.Echo objItem.SerialNumber \n"  
                    + "    exit for  ' do the first cpu only! \n" + "Next \n";  
  
            fw.write(vbs);  
            fw.close();  
            Process p = Runtime.getRuntime().exec(  
                    "cscript //NoLogo " + file.getPath());  
            BufferedReader input = new BufferedReader(new InputStreamReader(p  
                    .getInputStream()));  
            String line;  
            while ((line = input.readLine()) != null) {  
                result += line;  
            }  
            input.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return result.trim();  
    }  

	public static String getHDSerial(String drive) {
		String result = "";
		try {
			File file = File.createTempFile("tmp", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);

			String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
					+ "Set colDrives = objFSO.Drives\n"
					+ "Set objDrive = colDrives.item(\""
					+ drive
					+ "\")\n"
					+ "Wscript.Echo objDrive.SerialNumber";
			fw.write(vbs);
			fw.close();
			Process p = Runtime.getRuntime().exec(
					"cscript //NoLogo " + file.getPath());
			BufferedReader input = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
			file.delete();
		} catch (Exception e) {

		}
		if (result.trim().length() < 1 || result == null) {
			result = "无磁盘ID被读取";

		}

		return result.trim();
	}
}
