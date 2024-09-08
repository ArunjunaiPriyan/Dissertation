package com.magna.fairnessAction.Rowmapper.copy;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.magna.fairnessAction.Entity.filemanagerEntity;

public class filemanagerRowmapper implements RowMapper<filemanagerEntity> {

	@Override
	public filemanagerEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		filemanagerEntity fr = new filemanagerEntity();
		fr.setFile_name(rs.getString("File_Name"));
		fr.setFile_type(rs.getString("File_Type"));
		fr.setSource_path(rs.getString("Source_Path"));
	      return fr;
	}

}
