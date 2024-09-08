package com.magna.fairnessAction.Entity;

import javax.persistence.Embeddable;

@Embeddable
public class sapmesrequestlist {

	private int id;
	private String pjName;
	private String pjID;
	private String pjSpon;
	private String crBy;
	private String reqDate;
	private String prjct;
	private String scope;
	private String imp;
	private String critical;
	private String high;
	private String normal;
	private String low;
	private String business;
	private String bug;
	private String desc;
	private String scopeWork;
	private String exist;
	private String prepared;
	private String benefit;
	private String dept;
	private String isactive;
	private String rejreason;
	private String empname;
	
	
	public String getEmpname() {
		return empname;
	}

	public void setEmpname(String empname) {
		this.empname = empname;
	}

	
	

	public String getRejreason() {
		return rejreason;
	}

	public void setRejreason(String rejreason) {
		this.rejreason = rejreason;
	}

	public String getIsactive() {
		return isactive;
	}

	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	private String deptCode;

	public String getCrBy() {
		return crBy;
	}

	public void setCrBy(String crBy) {
		this.crBy = crBy;
	}

	public String getReqDate() {
		return reqDate;
	}

	public void setReqDate(String reqDate) {
		this.reqDate = reqDate;
	}

	public String getPrjct() {
		return prjct;
	}

	public void setPrjct(String prjct) {
		this.prjct = prjct;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getImp() {
		return imp;
	}

	public void setImp(String imp) {
		this.imp = imp;
	}

	public String getCritical() {
		return critical;
	}

	public void setCritical(String critical) {
		this.critical = critical;
	}

	public String getHigh() {
		return high;
	}

	public void setHigh(String high) {
		this.high = high;
	}

	public String getNormal() {
		return normal;
	}

	public void setNormal(String normal) {
		this.normal = normal;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public String getBug() {
		return bug;
	}

	public void setBug(String bug) {
		this.bug = bug;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getScopeWork() {
		return scopeWork;
	}

	public void setScopeWork(String scopeWork) {
		this.scopeWork = scopeWork;
	}

	public String getExist() {
		return exist;
	}

	public void setExist(String exist) {
		this.exist = exist;
	}

	public String getPrepared() {
		return prepared;
	}

	public void setPrepared(String prepared) {
		this.prepared = prepared;
	}

	public String getBenefit() {
		return benefit;
	}

	public void setBenefit(String benefit) {
		this.benefit = benefit;
	}

	public String getPjName() {
		return pjName;
	}

	public void setPjName(String pjName) {
		this.pjName = pjName;
	}

	public String getPjID() {
		return pjID;
	}

	public void setPjID(String pjID) {
		this.pjID = pjID;
	}

	public String getPjSpon() {
		return pjSpon;
	}

	public void setPjSpon(String pjSpon) {
		this.pjSpon = pjSpon;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
