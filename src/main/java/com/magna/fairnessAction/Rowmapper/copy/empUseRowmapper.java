package com.magna.fairnessAction.Rowmapper.copy;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import com.magna.fairnessAction.Entity.empUserEntity;

public class empUseRowmapper implements RowMapper<empUserEntity> {

	@Override
	public empUserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		empUserEntity fr = new empUserEntity();
		fr.setUsername(rs.getString("username"));
		fr.setUser_Type(rs.getString("ID"));
		fr.setPassword(rs.getString("password"));
	      return fr;
	}

}
