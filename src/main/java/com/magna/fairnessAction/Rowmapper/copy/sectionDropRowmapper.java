package com.magna.fairnessAction.Rowmapper.copy;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.magna.fairnessAction.Entity.SectionEntity;

public class sectionDropRowmapper implements RowMapper<SectionEntity> {

	@Override
	public SectionEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		SectionEntity fr = new SectionEntity();
		fr.setId(rs.getInt("ID"));
		fr.setCom_Code(rs.getString("Com_Code"));
		fr.setCom_Name(rs.getString("Com_Name"));
		fr.setCreator(rs.getString("Creator"));
		fr.setSec_type(rs.getString("Sec_Type"));
	      return fr;
	}

}
