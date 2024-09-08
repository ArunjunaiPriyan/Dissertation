package com.magna.fairnessAction.Rowmapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import com.magna.fairnessAction.Entity.departmentListEntity;

public final class departmentListRowmapper implements RowMapper<departmentListEntity> {

	@Override	
	public departmentListEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		departmentListEntity fr = new departmentListEntity();
		fr.setId(rs.getInt("ID"));
		fr.setEmp(rs.getString("Emp_Name"));
		fr.setEmp_code(rs.getString("Emp_Code"));
		fr.setDept(rs.getString("Dept"));
		fr.setEmp_type(rs.getInt("Emp_Type"));
		fr.setIs_active(rs.getString("is_active"));
		fr.setMail(rs.getString("Emp_Mail"));
	      return fr;
	   }
	
}
