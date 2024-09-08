package com.magna.user.entity;
import javax.persistence.Embeddable;
import javax.persistence.Table; 

@Embeddable
@Table(name = "users") 
public class UserEntity { 
	 private int accountUnlocked;
	   private String username;
	   private String password;
	   private String dept;
	   private String user;
	   private String empCode;
	   
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getAccountUnlocked() {
		return accountUnlocked;
	}
	public void setAccountUnlocked(Integer accountUnlocked) {
		this.accountUnlocked = accountUnlocked;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
    
	
	}