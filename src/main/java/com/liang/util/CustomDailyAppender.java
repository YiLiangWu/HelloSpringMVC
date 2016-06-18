package com.liang.util;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.FileAppender;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

public class CustomDailyAppender extends FileAppender {

	private static final int maxFileNum = 7; // 最多保存7个日志文件
	private String scheduledFilename; // 缓存时间戳+当前写入的日志文件名
	Date now = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 启动log4j时初始化Appender设置相关属性
	 */
	@Override
	public void activateOptions() {
		super.activateOptions();
		if (fileName != null) {
			File file = new File(fileName);
			System.out.println("fileName is " + fileName);
			// 时间戳+文件名
			scheduledFilename = sdf.format(new Date(file.lastModified()))
					+ file.getName();
		}
	}

	/**
	 * 重写日志写入过程：<br/>
	 * 1. 写入日志前检查是否需要写入新的文件 <br/>
	 * 2. 检查是否存在超过指定天数的日志文件，如果存在则删除br/>
	 */
	@Override
	protected void subAppend(LoggingEvent event) {
		long n = System.currentTimeMillis();
		now.setTime(n);
		try {
			rollBack();
		} catch (IOException ioe) {
			if (ioe instanceof InterruptedIOException) {
				Thread.currentThread().interrupt();
			}
			LogLog.error("rollOver() failed.", ioe);
		}
		super.subAppend(event);
	}

	/**
	 * 滚动文件的函数:<br>
	 * 1. 对文件名带的时间戳进行比较, 确定是否更新<br>
	 * 2. 如果需要更新, 当前文件rename到文件名+日期, 重新开始写文件<br>
	 * 3. 针对配置的maxFileNum,删除过期的文件
	 */
	void rollBack() throws IOException {
		// 检查是否需要回滚文件
		String origiFile = new File(fileName).getName();
		String datedFilename = sdf.format(now) + origiFile;
		if (scheduledFilename.equals(datedFilename)) {
			System.out.println("不需要回滚文件");
			return;
		}
		this.closeFile();
		// 若存在同名文件则删除,切换到新文件写入日志
		String newFile = fileName.replaceAll(origiFile, scheduledFilename);
		File target = new File(newFile);
		if (target.exists()) {
			try {
				target.delete();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}

		File file = new File(fileName);
		if (file.renameTo(target)) {
			System.out.println("rename success " + target.getAbsolutePath());
			LogLog.debug(fileName + " -> " + scheduledFilename);
		} else {
			System.out.println("rename fail " + scheduledFilename);
			LogLog.error("Failed to rename [" + fileName + "] to ["
					+ scheduledFilename + "].");
		}
		System.out.println("delete Old Log");
		deleteOldLog(file);
		scheduledFilename = datedFilename; // 更新当前缓存的带日期文件名
	}

	/**
	 * 遍历日志文件名的时间戳部分，删除指定天数前的日志，待改进
	 * 
	 * @param file
	 */
	private void deleteOldLog(File file) {
		List<File> targets = getOldLog(file);
		for (int i = 0; i < targets.size(); i++) {
			if (targets.get(i).delete()) {
				LogLog.debug(targets.get(i).getName() + " -> deleted");
			} else {
				LogLog.error("Failed to deleted old DayRollingFileAppender file :"
						+ targets.get(i).getName());
			}
		}
		try {
			this.setFile(fileName, false, this.bufferedIO, this.bufferSize);
		} catch (IOException e) {
			errorHandler.error("setFile(" + fileName + ", false) call failed.");
		}

	}

	/**
	 * 获取应该删除的日志文件集合
	 * 
	 * @param file
	 * @return
	 */
	private List<File> getOldLog(File file) {
		List<File> targets = new ArrayList<File>();
		File folder = new File(file.getParent());
		File[] files = folder.listFiles();
		System.out.println(files.length);
		System.out.println("folder is " + folder.getName());
		if (files != null && files.length > 0) {
			for (File f : files) {
				String fileName = f.getName();
				if (fileName.length() > 10) {
					String date = fileName.substring(0, 10);
					System.out.println("date is " + date);
					try {
						Date d = DateFormatUtil.parseToDay(date);
						if (isDelTarget(d)) {
							targets.add(f);
						}
					} catch (ParseException e) {
						LogLog.error("getOldLog cause error ——> " + e);
					}
				}
			}
		}
		return targets;
	}

	/**
	 * 判断给定的日期相距是否大于最大值
	 * 
	 * @return
	 */
	private boolean isDelTarget(Date createTime) {
		Calendar c = Calendar.getInstance();
		c.setTime(createTime);
		int day1 = c.get(Calendar.DAY_OF_YEAR);
		c.setTime(now);
		int day2 = c.get(Calendar.DAY_OF_YEAR);
		int dif = day2 - day1;
		if (dif >= maxFileNum) {
			return true;
		}
		return false;
	}

}
