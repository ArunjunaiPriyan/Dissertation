package com.magna.fairnessAction.Entity;
import javax.persistence.Embeddable;

@Embeddable
public class departmentListEntity {
	private int id;
	private String emp;
	private String emp_code;
	private String dept;
	private int emp_type;
	private String is_active;
	private String mail;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmp() {
		return emp;
	}
	public void setEmp(String emp) {
		this.emp = emp;
	}
	public String getEmp_code() {
		return emp_code;
	}
	public void setEmp_code(String emp_code) {
		this.emp_code = emp_code;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public int getEmp_type() {
		return emp_type;
	}
	public void setEmp_type(int emp_type) {
		this.emp_type = emp_type;
	}
	public String getIs_active() {
		return is_active;
	}
	public void setIs_active(String is_active) {
		this.is_active = is_active;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	
}
