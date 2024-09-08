package com.magna.fairnessAction.Entity;

import javax.persistence.Embeddable;

@Embeddable
public class filemanagerEntity {

	private String file_name;
	private String file_type;
	private String source_path;
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getFile_type() {
		return file_type;
	}
	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}
	public String getSource_path() {
		return source_path;
	}
	public void setSource_path(String source_path) {
		this.source_path = source_path;
	}
	
}
