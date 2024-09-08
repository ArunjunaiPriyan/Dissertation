
package com.magna.fairnessAction.Rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.magna.fairnessAction.Entity.actionPointApproveEntity;

public  final class actionPointApproveRowmapper implements RowMapper<actionPointApproveEntity> { 
	
	@Override	
	public actionPointApproveEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		actionPointApproveEntity approve = new actionPointApproveEntity();
		 
		approve.setEmpMail(rs.getString("Emp_Mail"));
		approve.setActionPoint(rs.getString("Action_Point"));
		approve.setSecName(rs.getString("Com_Name"));
		approve.setTargetdate(rs.getString("Target_date"));
		
		return approve;
		
	   }
}

