package com.magna.user.rowmapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.magna.user.entity.UserEntityForSap;  

public final class UserMstForSapRowmapper implements RowMapper<UserEntityForSap> { 
	
	@Override	
	public UserEntityForSap mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserEntityForSap login = new UserEntityForSap();
		login.setUsername(rs.getString("username"));
		login.setPassword(rs.getString("password"));
		login.setAccountUnlocked(rs.getInt("account_non_locked"));
		login.setDept(rs.getString("Dept"));
		login.setUser(rs.getString("User_Type"));
		login.setEmpId(rs.getString("emp_Code"));
	      return login;
	   }
}