package com.magna.fairnessAction.Entity;

import javax.persistence.Embeddable;

@Embeddable
public class SectionEntity {

	private int id;
	private String com_Code;
	private String com_Name;
	private String creator;
	private String sec_type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCom_Code() {
		return com_Code;
	}

	public void setCom_Code(String com_Code) {
		this.com_Code = com_Code;
	}

	public String getCom_Name() {
		return com_Name;
	}

	public void setCom_Name(String com_Name) {
		this.com_Name = com_Name;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getSec_type() {
		return sec_type;
	}

	public void setSec_type(String sec_type) {
		this.sec_type = sec_type;
	}

}
