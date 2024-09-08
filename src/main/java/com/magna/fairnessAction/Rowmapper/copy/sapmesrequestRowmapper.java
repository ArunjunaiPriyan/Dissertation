package com.magna.fairnessAction.Rowmapper.copy;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.magna.fairnessAction.Entity.sapmesrequestlist;

public class sapmesrequestRowmapper implements RowMapper<sapmesrequestlist> {

	@Override
	public sapmesrequestlist mapRow(ResultSet rs, int rowNum) throws SQLException {
		sapmesrequestlist fr = new sapmesrequestlist();
		fr.setId(rs.getInt("ID"));
		fr.setPjName(rs.getString("PROJECT_NAME"));
		fr.setPjID(rs.getString("PROJECT_ID"));
		fr.setPjSpon(rs.getString("PROJECT_SPON"));
		fr.setCrBy(rs.getString("CREATED_BY"));
		fr.setReqDate(rs.getString("REQUEST_DATE"));
		fr.setPrjct(rs.getString("PROJECT"));
		fr.setScope(rs.getString("SCOPE_CHANGE_NO"));
		fr.setImp(rs.getString("IMPLEMENT_BY"));
		fr.setCritical(rs.getString("CRITICAL"));
		fr.setHigh(rs.getString("HIGH"));
		fr.setNormal(rs.getString("NORMAL"));
		fr.setLow(rs.getString("LOW"));
		fr.setBusiness(rs.getString("BUSINESS_SCOPE"));
		fr.setBug(rs.getString("BUG_FIX"));
		fr.setDesc(rs.getString("DESCRIPTION"));
		fr.setScopeWork(rs.getString("SCOPE_WORK"));
		fr.setExist(rs.getString("EXISTING"));
		fr.setPrepared(rs.getString("PREPARED"));
		fr.setBenefit(rs.getString("BENEFITS"));
		fr.setDept(rs.getString("Dept_code"));
		fr.setIsactive(rs.getString("IS_ACTIVE"));
		fr.setRejreason(rs.getString("REJ_REASON"));
		
		return fr;
	}

}
