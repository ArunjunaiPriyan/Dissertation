package com.magna.fairnessAction.Entity;

import javax.persistence.Embeddable;
import javax.persistence.Table;

@Embeddable
@Table(name = "fairness_action_mst")
public class fairnessActionEntity {
	private String date;
	private String actionPoint;
	private String responsibility;
	private String department;
	private String target_date;
	private int isactive;
	private String evidance;
	private int id;
	private int resp_status;
	private int target_status;
	private String closed_date;
	private String com_section;
	private String sec_type;
	private String comment;
	private String before;
	private String reject_remark;
	private int app_status;
	private String sendback_comment;
    private String sec_owner;
	

    
	public String getSec_owner() {
		return sec_owner;
	}

	public void setSec_owner(String sec_owner) {
		this.sec_owner = sec_owner;
	}

	public String getSendback_comment() {
		return sendback_comment;
	}

	public void setSendback_comment(String string) {
		this.sendback_comment = string;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getActionPoint() {
		return actionPoint;
	}

	public void setActionPoint(String actionPoint) {
		this.actionPoint = actionPoint;
	}

	public String getResponsibility() {
		return responsibility;
	}

	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getTarget_date() {
		return target_date;
	}

	public void setTarget_date(String target_date) {
		this.target_date = target_date;
	}

	public String getEvidance() {
		return evidance;
	}

	public void setEvidance(String evidance) {
		this.evidance = evidance;
	}

	public int getIsactive() {
		return isactive;
	}

	public void setIsactive(int isactive) {
		this.isactive = isactive;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClosed_date() {
		return closed_date;
	}

	public void setClosed_date(String closed_date) {
		this.closed_date = closed_date;
	}

	public String getCom_section() {
		return com_section;
	}

	public void setCom_section(String com_section) {
		this.com_section = com_section;
	}

	public int getResp_status() {
		return resp_status;
	}

	public void setResp_status(int resp_status) {
		this.resp_status = resp_status;
	}

	public int getTarget_status() {
		return target_status;
	}

	public void setTarget_status(int target_status) {
		this.target_status = target_status;
	}

	public String getSec_type() {
		return sec_type;
	}

	public void setSec_type(String sec_type) {
		this.sec_type = sec_type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getBefore() {
		return before;
	}

	public void setBefore(String before) {
		this.before = before;
	}

	public String getReject_remark() {
		return reject_remark;
	}

	public void setReject_remark(String reject_remark) {
		this.reject_remark = reject_remark;
	}

	public int getApp_status() {
		return app_status;
	}

	public void setApp_status(int app_status) {
		this.app_status = app_status;
	}
    
	
}
