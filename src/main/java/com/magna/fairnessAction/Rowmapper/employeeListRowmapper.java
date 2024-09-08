package com.magna.fairnessAction.Rowmapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import com.magna.fairnessAction.Entity.employeeListEntity;

public final class employeeListRowmapper implements RowMapper<employeeListEntity> {

	@Override	
	public employeeListEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		employeeListEntity fr = new employeeListEntity();
		fr.setId(rs.getInt("ID"));
		fr.setEmp(rs.getString("Emp_Name"));
		fr.setEmp_code(rs.getString("Emp_Code"));
		fr.setDept(rs.getString("Dept"));
		fr.setEmp_type(rs.getInt("Emp_Type"));
		fr.setIs_active(rs.getInt("is_active"));
		fr.setMail(rs.getString("Emp_Mail"));
		fr.setDept_code(rs.getString("Dept_code"));
	      return fr;
	   }
	
}
