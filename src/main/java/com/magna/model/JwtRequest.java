//package com.magna.model;
//
//import java.io.Serializable;
//
//public class JwtRequest implements Serializable {
//
//	private static final long serialVersionUID = 5926468583005150707L;
//	
//	private int accountUnlocked;
//	   private String username;
//	   private String password;
//	   private String dept;
//	
//	//need default constructor for JSON Parsing
//	public JwtRequest()
//	{
//		
//	}
//
//	public JwtRequest(String username, String password) {
//		this.setUsername(username);
//		this.setPassword(password);
//	}
//
//	public String getUsername() {
//		return username;
//	}
//	public void setUsername(String username) {
//		this.username = username;
//	}
//	public String getPassword() {
//		return password;
//	}
//	public void setPassword(String password) {
//		this.password = password;
//	}
//	public Integer getAccountUnlocked() {
//		return accountUnlocked;
//	}
//	public void setAccountUnlocked(Integer accountUnlocked) {
//		this.accountUnlocked = accountUnlocked;
//	}
//	public String getDept() {
//		return dept;
//	}
//	public void setDept(String dept) {
//		this.dept = dept;
//	}
//}