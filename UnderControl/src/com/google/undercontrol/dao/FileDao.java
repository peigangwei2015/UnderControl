package com.google.undercontrol.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.google.undercontrol.domain.FileInfo;

/**
 * @author Administrator
 *对文件的操作
 */
public class FileDao {
	/**
	 * 获取手机内存文件列表
	 * 
	 * @return 文件列表
	 */
	@SuppressLint("NewApi")
	public List<FileInfo> getRomFileList() {
		return getFileList(Environment.getExternalStorageDirectory().getAbsolutePath());
	}

	/**
	 * 获取文件列表
	 * 
	 * @return 文件列表
	 */
	public List<FileInfo> getFileList(String path) {
		List<FileInfo> list = new ArrayList<FileInfo>();
		File file = new File(path);
		if (file != null) {
			File[] fs = file.listFiles();
			for (int i = 0; fs!=null && i < fs.length; i++) {
				try {
					System.out.println(fs[i].getCanonicalPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
				FileInfo fileInfo=new FileInfo();
				fileInfo.setCreateDate(fs[i].lastModified());
				fileInfo.setDir(fs[i].isDirectory());
				fileInfo.setName(fs[i].getName());
				fileInfo.setPath(fs[i].getAbsolutePath());
				fileInfo.setSize(fs[i].length());
				list.add(fileInfo);
			}
		}
		return list;
	}
}
