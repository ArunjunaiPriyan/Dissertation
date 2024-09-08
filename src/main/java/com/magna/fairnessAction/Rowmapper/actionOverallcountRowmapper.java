package com.magna.fairnessAction.Rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.magna.fairnessAction.Entity.actionOverallCountEntity;

public class actionOverallcountRowmapper implements RowMapper<actionOverallCountEntity> {
	@Override	
	public actionOverallCountEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		actionOverallCountEntity fr = new actionOverallCountEntity();
		fr.setOpenCount(rs.getInt("count"));
	      return fr;
	   }
}
