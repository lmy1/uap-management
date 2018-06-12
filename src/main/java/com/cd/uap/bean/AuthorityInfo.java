package com.cd.uap.bean;

public class AuthorityInfo extends Authority{

	private String createdAdminName;
	
	private String appName;

	public String getCreatedAdminName() {
		return createdAdminName;
	}

	public void setCreatedAdminName(String createdAdminName) {
		this.createdAdminName = createdAdminName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
}
