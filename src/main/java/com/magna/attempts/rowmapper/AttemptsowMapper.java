package com.magna.attempts.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.magna.loginDtl.entity.AttemptsEntity;

public  final class AttemptsowMapper implements RowMapper<AttemptsEntity> { 
	
	@Override	
	public AttemptsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		AttemptsEntity login = new AttemptsEntity();
		login.setId(rs.getInt("id"));
		login.setUsername(rs.getString("username"));
		login.setAttempts(rs.getInt("attempts"));
	      return login;
	   }
}
