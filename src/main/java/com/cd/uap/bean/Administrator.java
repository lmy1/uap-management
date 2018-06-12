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
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * TblAdministrator entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tbl_administrator")
public class Administrator implements java.io.Serializable {

	// Fields

	private Integer id;
	
	@NotNull(message="管理员用户名不能为空")
	@Size(max=20, message="管理员用户名长度不能大于20位")
	private String username;
	
	@NotNull(message="管理员密码不能为空")
	@Size(max=64, message="管理员密码长度不能大于64位")
	private String password;
	
	@Size(max=32, message="管理员昵称长度不能大于32位")
	private String nickname;
	
	@NotNull(message="管理员电话不能为空")
	private String phoneNumber;
	
	@Range(min=0, max=1, message="管理员状态只能是0和1")
	private Integer status;
	
	@NotNull(message="管理员级别不能为空")
	@Range(min=0, max=4,message="管理员级别只能在0~4之间")
	private Integer type;
	
	private Integer createdAdminId;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date createdDatetime;
	
	@Size(max=200, message="管理员备注长度不能大于200位")
	private String remarker;

	// Constructors

	/** default constructor */
	public Administrator() {
	}

	/** minimal constructor */
	public Administrator(String username, String password, String phoneNumber) {
		this.username = username;
		this.password = password;
		this.phoneNumber = phoneNumber;
	}

	/** full constructor */
	public Administrator(String username, String password, String nickname,
			String phoneNumber, Integer status, Integer type,
			Integer createdAdminId, Date createdDatetime, String remarker) {
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.phoneNumber = phoneNumber;
		this.status = status;
		this.type = type;
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

	@Column(name = "USERNAME", nullable = false, length = 20)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "PASSWORD", nullable = false, length = 64)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "NICKNAME", length = 64)
	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Column(name = "PHONE_NUMBER", nullable = false, length = 20)
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Column(name = "STATUS")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "TYPE")
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "CREATED_ADMIN_ID")
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

	@Column(name = "REMARKER")
	public String getRemarker() {
		return this.remarker;
	}

	public void setRemarker(String remarker) {
		this.remarker = remarker;
	}

}