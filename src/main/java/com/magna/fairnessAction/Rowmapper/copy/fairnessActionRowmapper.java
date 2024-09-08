package com.magna.fairnessAction.Rowmapper.copy;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.magna.fairnessAction.Entity.fairnessActionEntity;

public final class fairnessActionRowmapper implements RowMapper<fairnessActionEntity> {

	@Override	
	public fairnessActionEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		fairnessActionEntity fr = new fairnessActionEntity();
		fr.setDate(rs.getString("Date"));
		fr.setActionPoint(rs.getString("Action_point"));
		fr.setResponsibility(rs.getString("Responsibility"));
		fr.setDepartment(rs.getString("Dept"));
		fr.setTarget_date(rs.getString("Target_date"));
		fr.setEvidance(rs.getString("Evdnce_File"));
		fr.setIsactive(rs.getInt("is_active"));
		fr.setId(rs.getInt("ID"));
		fr.setClosed_date(rs.getString("Closed_date"));
		fr.setCom_section(rs.getString("Com_Section"));
		fr.setResp_status(rs.getInt("Resp_Status"));
		fr.setTarget_status(rs.getInt("Target_Status"));
		fr.setSec_type(rs.getString("Sec_Type"));
		fr.setComment(rs.getString("comment"));
		fr.setBefore(rs.getString("Before_path"));
		fr.setReject_remark(rs.getString("Reject_remark"));
	      return fr;
	   }
	
}
