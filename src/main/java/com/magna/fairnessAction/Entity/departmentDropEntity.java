package com.magna.fairnessAction.Entity;
import javax.persistence.Embeddable;

@Embeddable
public class departmentDropEntity {
	private int id;
	private String dept;
	private String dept_code;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getDept_code() {
		return dept_code;
	}
	public void setDept_code(String dept_code) {
		this.dept_code = dept_code;
	}
}
