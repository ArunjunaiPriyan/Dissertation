
package com.magna.fairnessAction.Entity;

import javax.persistence.Embeddable;

@Embeddable
public class actionPointApproveEntity {

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String actionPoint;
	private String empMail;
	private String targetdate;
	private String secName;
	
	public String getActionPoint() {
		return actionPoint;
	}
	public void setActionPoint(String actionPoint) {
		this.actionPoint = actionPoint;
	}

	
	public String getTargetdate() {
		return targetdate;
	}
	public void setTargetdate(String targetdate) {
		this.targetdate = targetdate;
	}
	public String getSecName() {
		return secName;
	}
	public void setSecName(String secName) {
		this.secName = secName;
	}
	public String getEmpMail() {
		return empMail;
	}
	public void setEmpMail(String empMail) {
		this.empMail = empMail;
	}

}
