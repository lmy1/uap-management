package com.cd.uap.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TblAdminApp entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tbl_admin_app")
public class AdminApp implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer adminId;
	private Integer appId;

	// Constructors

	/** default constructor */
	public AdminApp() {
	}

	/** full constructor */
	public AdminApp(Integer adminId, Integer appId) {
		this.adminId = adminId;
		this.appId = appId;
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

	@Column(name = "ADMIN_ID", nullable = false)
	public Integer getAdminId() {
		return this.adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	@Column(name = "APP_ID", nullable = false)
	public Integer getAppId() {
		return this.appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

}