package com.google.undercontrol.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.google.undercontrol.domain.FileInfo;

/**
 * @author Administrator 对文件的操作
 */
public class FileDao {
	private String romPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath();

	/**
	 * 获取手机内存文件列表
	 * 
	 * @return 文件列表
	 */
	@SuppressLint("NewApi")
	public List<FileInfo> getRomFileList() {
		return getFileList(romPath);
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
			boolean isRom = path.equals(romPath);
			if (!isRom) {
				FileInfo f = new FileInfo();
				f.setCreateDate(System.currentTimeMillis());
				f.setDir(true);
				f.setName("...");
				f.setParent("父目录");
				String parent = file.getParentFile().getAbsolutePath();
				f.setPath(parent);
				list.add(f);
			}
			for (int i = 0; fs != null && i < fs.length; i++) {
				FileInfo fileInfo = new FileInfo();
				fileInfo.setCreateDate(fs[i].lastModified());
				fileInfo.setDir(fs[i].isDirectory());
				fileInfo.setName(fs[i].getName());
				fileInfo.setSize(fs[i].length());
				fileInfo.setPath(fs[i].getAbsolutePath());
				list.add(fileInfo);
			}
		}
		return list;
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 *            要删除的文件路径
	 * @return 如果删除成功返回true，否则返回false
	 */
	public boolean deleteFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			if (file.delete()) {
				return true;
			}
		}
		return false;
	}
}
