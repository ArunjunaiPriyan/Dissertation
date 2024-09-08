package com.magna.user.rowmapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.magna.user.entity.UserEntity;  

public final class UserRowmapper implements RowMapper<UserEntity> { 
	
	@Override	
	public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserEntity login = new UserEntity();
		login.setUsername(rs.getString("username"));
		login.setPassword(rs.getString("password"));
		login.setAccountUnlocked(rs.getInt("account_non_locked"));
		login.setDept(rs.getString("Dept"));
		login.setUser(rs.getString("User_Type"));
		login.setEmpCode(rs.getString("emp_Code"));
	      return login;
	   }
}