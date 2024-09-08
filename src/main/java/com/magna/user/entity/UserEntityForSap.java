
package com.magna.user.entity;
import javax.persistence.Embeddable;
import javax.persistence.Table; 

@Embeddable
@Table(name = "users_mst_sap") 
public class UserEntityForSap { 
	 private int accountUnlocked;
	   private String username;
	   private String password;
	   private String dept;
	   private String user;
	   private String empId;
	   
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
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

	}