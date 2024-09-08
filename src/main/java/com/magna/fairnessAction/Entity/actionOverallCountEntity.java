package com.magna.fairnessAction.Entity;

import javax.persistence.Embeddable;
import javax.persistence.Table;

@Embeddable
@Table(name = "fairness_action_mst") 
public class actionOverallCountEntity {
	private int openCount;
	public int getOpenCount() {
		return openCount;
	}
	public void setOpenCount(int openCount) {
		this.openCount = openCount;
	}
}
