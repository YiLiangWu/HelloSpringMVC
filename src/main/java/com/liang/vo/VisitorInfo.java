package com.liang.vo;


public class VisitorInfo {

	private String username;
	private String ip;
	private String accessTime;

	public VisitorInfo() {

	}

	public VisitorInfo(String username, String ip, String accessTime) {
		this.username = username;
		this.ip = ip;
		this.accessTime = accessTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(String accessTime) {
		this.accessTime = accessTime;
	}

	@Override
	public String toString() {
		return "VisitorInfo [username=" + username + ", ip=" + ip
				+ ", accessTime=" + accessTime + "]";
	}

}
