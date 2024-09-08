package com.magna.user.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import com.magna.user.rowmapper.UserRowmapper;
import com.magna.user.rowmapper.UserMstForSapRowmapper;
import com.magna.AESencrypt;
import com.magna.user.controller.UserController;
import com.magna.user.entity.UserEntity;
import com.magna.user.entity.UserEntityForSap;

@Service
public class UserService {
	Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	public String getUserDtl(String username, String userType) {
		String dcryptPass = null;
		try {
			RowMapper<UserEntity> loginRowmapper = new UserRowmapper();
			String loginQry = "select * from users_mst where username = ? and User_Type = ?";
			List<UserEntity> loginQurry = this.jdbcTemplate.query(loginQry, loginRowmapper, username, userType);
			
			if (loginQurry.size() > 0) {
				//String deptFetch = this.jdbcTemplate.query(loginQry, loginRowmapper, username, userType);
				dcryptPass = AESencrypt.decrypt(loginQurry.get(0).getPassword()) +"/"+loginQurry.get(0).getUsername() +"/" + loginQurry.get(0).getDept() + "/" + loginQurry.get(0).getEmpCode();
				
			}
		} catch (Exception ex) {
			logger.error("login method exception" + ex);
		}
		return dcryptPass;
	}

	public String setpws() {
		String resultdata = null;
		try {

			String passwd = "Magna@123";
			String encryptedString = AESencrypt.encrypt(passwd);

			String loginpwdQry = "UPDATE users_mst \r\n" + "SET \r\n" + "    password = ?\r\n" + "WHERE\r\n"
					+ "    username = ?;";
			int loginpwd = this.jdbcTemplate.update(loginpwdQry, encryptedString, "Ramesh");
			if (loginpwd > 0) {
				resultdata = "success";
			} else {
				resultdata = "error";
			}
		} catch (Exception ex) {
			logger.error("setpws method exception" + ex);
		}
		return resultdata;
	}

	public String setNewPassword(String username, String codePass, String userType, String codeNew) {
		String resultdata = null;
		try {
			String encryptedString = AESencrypt.encrypt(codeNew);
			String getLastCode = "select password from users_mst where username ='"+username+"' and User_Type = '"+userType+"' ;";
			String lastCode = this.jdbcTemplate.queryForObject(getLastCode, String.class);
			if(!lastCode.equalsIgnoreCase("") && lastCode != null) {
				String PassCodeString = AESencrypt.decrypt(lastCode);
				if(PassCodeString.equalsIgnoreCase(codePass)) {
					String loginpwdQry = "UPDATE users_mst SET password = ? WHERE username = ? and User_Type = ?;";
					int loginpwd = this.jdbcTemplate.update(loginpwdQry,encryptedString, username, userType);
					if (loginpwd > 0) {
						resultdata = "success";
					} else {
						resultdata = "error";
					}
				}else {
					resultdata = "NOM";
				}
			}
		} catch (Exception ex) {
			logger.error("setNewPassword method exception" + ex);
		}
		return resultdata;
	}

	public List<String> getUserDtlForSap(String username, int userType) {
		List<String> result = new ArrayList<>();
		String dcryptPass = null;
		try {
			logger.info("loginUserForSap method start ");
			RowMapper<UserEntityForSap> loginRowmapper = new UserMstForSapRowmapper();
			String loginQry = "select * from users_mst_sap where username = ? and User_Type = ?";
			List<UserEntityForSap> loginQurry = this.jdbcTemplate.query(loginQry, loginRowmapper, username, userType);
			if (loginQurry.size() > 0) {
				//String deptFetch = this.jdbcTemplate.query(loginQry, loginRowmapper, username, userType);
				dcryptPass = AESencrypt.decrypt(loginQurry.get(0).getPassword()) +"/"+loginQurry.get(0).getUsername() +"/" + loginQurry.get(0).getDept() + "/" + loginQurry.get(0).getEmpId();
				result.add(dcryptPass);
			}else {
				result.add("");	
			}
			
		} catch (Exception ex) {
			logger.error("loginUserForSap method exception" + ex);
		}
		return result;
	}
}
