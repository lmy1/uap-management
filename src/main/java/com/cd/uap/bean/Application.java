package com.cd.uap.bean;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * TblApplication entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tbl_application")
public class Application implements java.io.Serializable {

	// Fields

	private Integer id;
	@NotNull(message="应用名不能为空")
	@Size(max=20,message="应用名长度不能大于20位")
	private String appName;
	@Size(max=16,min=16,message="clientId长度必须为16位")
	private String clientId;
	@NotNull(message="secret不能为空")
	@Size(max=32,min=32,message="secret长度必须为32位")
	private String secret;
	@Size(max=200,message="应用首页URL长度不能大于200个字符")
	@Pattern(regexp="^((https|http|ftp|rtsp|mms)?:\\/\\/)[^\\s]+",message="URL格式错误")
	private String appUrl;
	@Size(max=50,message="应用介绍长度不能大于50个字符")
	private String introduction;
	private Integer createdAdminId;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date createdDatetime;
	@Size(max=500,message="应用备注长度不能大于500个字符")
	private String remarker;

	
	// Constructors
	public Application() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Application(Integer id, String appName, String clientId, String secret, String appUrl, String introduction,
			Integer createdAdminId, Date createdDatetime, String remarker) {
		super();
		this.id = id;
		this.appName = appName;
		this.clientId = clientId;
		this.secret = secret;
		this.appUrl = appUrl;
		this.introduction = introduction;
		this.createdAdminId = createdAdminId;
		this.createdDatetime = createdDatetime;
		this.remarker = remarker;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "APP_NAME", nullable = false, length = 20)
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	@Column(name = "CLIENT_ID", nullable = false, length = 16)
	public String getClientId() {
		return this.clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@Column(name = "SECRET", nullable = false, length = 32)
	public String getSecret() {
		return this.secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Column(name = "APP_URL", length = 200)
	public String getAppUrl() {
		return this.appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	@Column(name = "INTRODUCTION", length = 50)
	public String getIntroduction() {
		return this.introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@Column(name = "CREATED_ADMIN_ID", nullable = false)
	public Integer getCreatedAdminId() {
		return this.createdAdminId;
	}

	public void setCreatedAdminId(Integer createdAdminId) {
		this.createdAdminId = createdAdminId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATETIME")
	public Date getCreatedDatetime() {
		return this.createdDatetime;
	}

	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	@Column(name = "REMARKER", length = 500)
	public String getRemarker() {
		return this.remarker;
	}

	public void setRemarker(String remarker) {
		this.remarker = remarker;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appName == null) ? 0 : appName.hashCode());
		result = prime * result + ((appUrl == null) ? 0 : appUrl.hashCode());
		result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
		result = prime * result + ((createdAdminId == null) ? 0 : createdAdminId.hashCode());
		result = prime * result + ((createdDatetime == null) ? 0 : createdDatetime.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((introduction == null) ? 0 : introduction.hashCode());
		result = prime * result + ((remarker == null) ? 0 : remarker.hashCode());
		result = prime * result + ((secret == null) ? 0 : secret.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Application other = (Application) obj;
		if (appName == null) {
			if (other.appName != null)
				return false;
		} else if (!appName.equals(other.appName))
			return false;
		if (appUrl == null) {
			if (other.appUrl != null)
				return false;
		} else if (!appUrl.equals(other.appUrl))
			return false;
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		if (createdAdminId == null) {
			if (other.createdAdminId != null)
				return false;
		} else if (!createdAdminId.equals(other.createdAdminId))
			return false;
		if (createdDatetime == null) {
			if (other.createdDatetime != null)
				return false;
		} else if (!createdDatetime.equals(other.createdDatetime))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (introduction == null) {
			if (other.introduction != null)
				return false;
		} else if (!introduction.equals(other.introduction))
			return false;
		if (remarker == null) {
			if (other.remarker != null)
				return false;
		} else if (!remarker.equals(other.remarker))
			return false;
		if (secret == null) {
			if (other.secret != null)
				return false;
		} else if (!secret.equals(other.secret))
			return false;
		return true;
	}

	
}