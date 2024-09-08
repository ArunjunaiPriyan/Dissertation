package com.magna.fairnessAction.Rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.magna.fairnessAction.Entity.departmentDropEntity;

public class departmentDropRowmapper implements RowMapper<departmentDropEntity> {

	@Override
	public departmentDropEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		departmentDropEntity fr = new departmentDropEntity();
		fr.setId(rs.getInt("ID"));
		fr.setDept_code(rs.getString("Department"));
		fr.setDept(rs.getString("Dept_code"));
	      return fr;
	}

}
