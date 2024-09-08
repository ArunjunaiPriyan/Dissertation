package com.magna.fairnessAction.Rowmapper.copy;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.magna.fairnessAction.Entity.actionPointCountEntity;

public class actionpointcountRowmapper implements RowMapper<actionPointCountEntity> {
	@Override	
	public actionPointCountEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		actionPointCountEntity fr = new actionPointCountEntity();
		fr.setOpenCount(rs.getInt("count"));
		fr.setDept(rs.getString("Dept"));
	      return fr;
	   }
}
