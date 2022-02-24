package com.example.paperlessmeeting_demo.tool;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileScreenshotUtil {

		public static String mstrRootPath;
	private static FileScreenshotUtil mFileScreenshotUtil = null;
    private static final String SCREENSHOTS_DIR_NAME = "Test_ScreenShots";
    private static final String SCREENSHOT_FILE_NAME_TEMPLATE = "Screenshot_%s.png";
    private static final String SCREENSHOT_FILE_PATH_TEMPLATE = "%s/%s/%s";
	
	public static FileScreenshotUtil GetInstance() {
		if (mFileScreenshotUtil == null) {
			mFileScreenshotUtil = new FileScreenshotUtil();
			mstrRootPath = Environment.getExternalStorageDirectory().toString();
		}
		return mFileScreenshotUtil;
	}
	
	// 判断sd卡是否存在
	public static boolean getExistStorage() {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		return sdCardExist;
	}
	
	public String getRootPath(){
		return mstrRootPath;
	}
	
	//没有这个文件夹，就创建，得加上SD卡的根目录
	public boolean onFolderAnalysis(String strPathFileName) {
		if (strPathFileName == null) return false;
		String[] Lookup = strPathFileName.split("/");
		String path = "";
		for (int i = 0; i < Lookup.length; i++) {
			if (Lookup[i] == null)  continue;
			if (i == Lookup.length - 1) continue;
			
			path = path + "/" + Lookup[i];
			// 判断是否存在这个路径是否存在这个文件
			File existFolder = new File(path);
			if (!existFolder.exists()) {
				existFolder.mkdirs();
			}			
		}
		return true;
	}
	
	// 打开文件
	public String OpenPrjFile(String strPathFileName) {
		String strData = "";
		if (getExistStorage()) {
			// 判断是否存在这个路径是否存在这个文件
			String strpathFileName = mstrRootPath + strPathFileName;
			File absolutPathFileName = new File(strpathFileName);
			if (!absolutPathFileName.exists()) {
				return strData;
			} else {
				try {
					FileInputStream fileInputStream = new FileInputStream(
							strpathFileName);
					int length = fileInputStream.available();
					byte[] buffer = new byte[length];
					fileInputStream.read(buffer);
					strData = new String(buffer);
					fileInputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return strData;
	}

	// 保存文件
	public boolean SavePrjFile(String strPath , String strSavePrjData) {
		if (getExistStorage()) {
			onFolderAnalysis(strPath);

			// 在指定的文件夹中创建文件
			String strPathFileName = mstrRootPath + strPath;
			File nameFile = new File(strPathFileName);{
				try {
					nameFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (!nameFile.exists())

			// 调用函数向文件写入数据
			return WitePrjDataSDFile(strPathFileName, strSavePrjData);
		}

		return true;
	}

	// 向文件写入数据
	private boolean WitePrjDataSDFile(String strPathFileName, String strSavePrjData){
		try {
			File writefile = new File(strPathFileName);
			FileOutputStream fileOutputStream = new FileOutputStream(writefile);
			byte[] byteBuffer = new byte[1024];
			byteBuffer = strSavePrjData.getBytes();
			fileOutputStream.write(byteBuffer);
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//使用系统时间获取文件命名
	public String getFilePathAndName(){
		long systemTime = System.currentTimeMillis();
        String imageDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date(systemTime));
        String mFileName = String.format(SCREENSHOT_FILE_NAME_TEMPLATE, imageDate);
        
        //"%s/%s/%s",跟目录，文件夹，文件名
        String mFilePath = String.format(SCREENSHOT_FILE_PATH_TEMPLATE, mstrRootPath,
                SCREENSHOTS_DIR_NAME, mFileName);
		return mFilePath;
	}
	
}
